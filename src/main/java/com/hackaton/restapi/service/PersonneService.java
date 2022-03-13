package com.hackaton.restapi.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import com.hackaton.restapi.entity.Centre;
import com.hackaton.restapi.entity.Personne;
import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.PersonneRepository;
import com.hackaton.restapi.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.hackaton.restapi.query.SearchCriteria;
import com.hackaton.restapi.query.SearchOperator;

@Service
public class PersonneService {
    private  PersonneRepository personneRepository;
    private  UserService userService;

    @Autowired
    public PersonneService(PersonneRepository personneRepository, UserService userService) {
        this.personneRepository = personneRepository;
        this.userService = userService;
    }

    public Personne addNewPersonne(Personne personne) {
        if (personne == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if (personne.getNom() == null ||
                personne.getPrenom() == null ||
                personne.getDateDeNaissance() == null ||
                personne.getCin() == null ||
                personne.getMail() == null ||
                personne.getUser() == null) {
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        }
        if(personne.getDateDeNaissance().getTime() >= new Timestamp(System.currentTimeMillis()).getTime()){
            throw new ApiRequestException("Date de naissance ne peut pas être au dela de ce jour");
        }
        if(personne.getCin().length() != 12) {
            throw new ApiRequestException("Numéro de CIN non valide");
        }
        if (personne.getCin().length() != 12) {
            throw new ApiRequestException("Numéro de CIN non valide");
        }
        String regexMail = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (!personne.getMail().matches(regexMail)) {
            throw new ApiRequestException("Format de mail invalide");
        }
        
        User user = userService.addNewUser(personne.getUser(), "personne");
        personne.setUser(user);
        return personneRepository.save(personne);
    }


    public Page<Personne> getPersonne(String sort, Integer page, Integer size,
            String id, String expressionNom,
            String expressionPrenom, String expressionDateNaissance, String expressionCIN,String expressionMail,String expressionIdUser) {
        Specification<Personne> specification = getAllSpecifications( id,  expressionNom,
             expressionPrenom,  expressionDateNaissance,  expressionCIN, expressionMail, expressionIdUser);
        specification = (specification != null) ? Specification.where(specification) : null;
        if (page == null)
            page = 1;
        if (size == null || size == 0)
            size = personneRepository.findAll().size();
        if (page < 1)
            throw new ApiRequestException("L'index de la page ne doit pas être inférieur à un : page = " + page);
        if (size < 0)
            throw new ApiRequestException(
                    "La taille de la page ne doit pas être inférieure à un : size = " + size + "    " + (size == 0));
        Pageable pageable = Util.pageable(sort, page - 1, size);
        Page<Personne> res = personneRepository.findAll(specification, pageable);
        if (res.getSize() == 0)
            throw new ApiRequestException("Aucun élement trouvé");
        return res;
    }

    public Specification<Personne> getAllSpecifications(String id, String expressionNom,
            String expressionPrenom, String expressionDateNaissance, String expressionCIN, String expressionMail,
            String expressionIdUser) {
        Specification<Personne> specification = ajouterSiNonNull(null, equalsIdMultiple(id, "id"));
        specification = ajouterSiNonNull(specification, equalsOrLikeString(expressionNom,"nom"));
        specification = ajouterSiNonNull(specification, equalsOrLikeString(expressionPrenom,"prenom"));
        specification = ajouterSiNonNull(specification,
                dateCreationSpecification(expressionDateNaissance, "dateDeNaissance"));
        specification = ajouterSiNonNull(specification, equalsOrLikeString(expressionPrenom, "prenom"));
        specification = ajouterSiNonNull(specification, equalsIdUser(expressionIdUser));
        return specification;
    }

    public static Specification<Personne> equalsIdSimple(String value, String intituleColonne) {
        if (Util.isNullOrEmpty(value))
            return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(intituleColonne), value);
    }

    public static Specification<Personne> equalsIdMultiple(String listeId, String intitule) {
        if (listeId == null || listeId.length() == 0)
            return null;
        return (root, query, criteriaBuilder) -> {
            List<Predicate> conditionsDansOr = new ArrayList<Predicate>();
            for (String idVaccin : listeId.split(",")) {
                if (idVaccin.compareTo("") != 0) {
                    Predicate equalsIdVaccin = criteriaBuilder.equal(root.get(intitule), idVaccin);
                    conditionsDansOr.add(equalsIdVaccin);
                }
            }
            return criteriaBuilder.or(conditionsDansOr.toArray(new Predicate[conditionsDansOr.size()]));
        };
    }

    public static Specification<Personne> equalsOrLikeString(String expression,String intitule) {
        if (Util.isNullOrEmpty(expression))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria(intitule, expression);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(
                        intitule),
                        critere.getValue());
            case LIKE:
                System.out.println("like: " + critere.getValue());
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(
                        intitule).as(String.class),
                        "%" + critere.getValue().toString() + "%");
            default:
                return null;
        }
    }

    public static Specification<Personne> equalsIdUser(String expressionIdUser) {
        if (expressionIdUser == null || expressionIdUser.length() == 0)
            return null;
        return (root, query, criteriaBuilder) -> {
            Join<Personne,User>jointure = root.join("user");
            List<Predicate> conditionsDansOr = new ArrayList<Predicate>();
            for (String idVaccin : expressionIdUser.split(",")) {
                if (idVaccin.compareTo("") != 0) {
                    Predicate equalsIdVaccin = criteriaBuilder.equal(jointure.get("id"), idVaccin);
                    conditionsDansOr.add(equalsIdVaccin);
                }
            }
            return criteriaBuilder.or(conditionsDansOr.toArray(new Predicate[conditionsDansOr.size()]));
        };
    }

    public static Specification<Personne> dateCreationSpecification(String expression,String intitule) {
        if (Util.isNullOrEmpty(expression))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria(intitule, expression);
        if (critere.getOperator() == SearchOperator.BETWEEN) {
            String[] values = (String[]) critere.getValue();
            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(critere.getKey())
                    .as(Timestamp.class), Timestamp.valueOf(values[0]), Timestamp.valueOf(values[1]));
        }
        Timestamp valueToTimestamp = Timestamp.valueOf((String) critere.getValue());
        critere.setValue(valueToTimestamp);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get(critere.getKey()).as(Timestamp.class),
                        valueToTimestamp);
            case GT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(
                        root.get(critere.getKey()).as(Timestamp.class),
                        valueToTimestamp);
            case GTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreation")
                        .as(Timestamp.class), valueToTimestamp);
            case LT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(
                        root.get(critere.getKey()).as(Timestamp.class),
                        (Timestamp) critere.getValue());
            case LTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(critere.getKey())
                        .as(Timestamp.class), valueToTimestamp);
            default:
                return null;
        }
    }

    public Specification<Personne> ajouterSiNonNull(Specification<Personne> specificationMere,
            Specification<Personne> specificationAAjouter) {
        if (specificationAAjouter == null)
            return specificationMere;
        // choix 1: ajout de where :if(specificationMere == null)return
        // Specification.where(specificationAAjouter);
        /* choix 2: on ajoute pas directement where */
        if (specificationMere == null)
            return specificationAAjouter;
        return specificationMere.and(specificationAAjouter);
    }

}


