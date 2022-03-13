package com.hackaton.restapi.service;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import com.hackaton.restapi.entity.Centre;
import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.CentreRepository;
import com.hackaton.restapi.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hackaton.restapi.query.SearchCriteria;
import com.hackaton.restapi.query.SearchOperator;

import java.time.format.DateTimeFormatter;

@Service
public class CentreService {
    private CentreRepository centreRepository;
    private UserService userService;

    @Autowired
    public CentreService(CentreRepository centreRepository, UserService userService) {
        this.centreRepository = centreRepository;
        this.userService = userService;
    }

    public Centre addNewCentre(Centre centre) {
        if (centre == null)
            throw new ApiRequestException("Aucune données à ajouter");
        
        if(centre.getNom() == null ||
            centre.getLongitude() == null ||
            centre.getLatitude() == null ||
            centre.getEstOuvert() == null ||
            centre.getOuverture() == null ||
            centre.getFermeture() == null ||
            centre.getNombrePersonnel() == null ||
            centre.getUser() == null){
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        }

        User user = userService.addNewUser(centre.getUser(), "centreVaccination");
        centre.setUser(user);
        return centreRepository.save(centre);
    }

    public Centre findById(Long id) {
        return centreRepository.findById(id)
        .orElseThrow(() -> new ApiRequestException("Aucun centre n'a cet id"));
    }

    /*
     * getNom
     * getLongitude
     * getLatitude
     * getEstOuvert
     * getOuverture
     * getFermeture
     * getNombrePersonnel
     * getUser
     */

     public Page<Centre> getCentre(String sort, Integer page, Integer size,
            String id, String nom,String expressionLongitude, String expressionLatitude, String expressionOuverture, String expressionFermeture,String nombrePersonnel,String idUser) {
                
        Specification<Centre> specification = getAllSpecifications( id,  nom, expressionLongitude,  expressionLatitude, expressionOuverture, expressionFermeture, nombrePersonnel, idUser);
        specification = (specification != null) ? Specification.where(specification) : null;
        if (page == null)
            page = 1;
        if (size == null || size == 0)
            size = centreRepository.findAll().size();
        if (page < 1)
            throw new ApiRequestException("L'index de la page ne doit pas être inférieur à un : page = " + page);
        if (size < 0)
            throw new ApiRequestException(
                    "La taille de la page ne doit pas être inférieure à un : size = " + size + "    " + (size == 0));
        Pageable pageable = Util.pageable(sort, page - 1, size);
        Page<Centre> res = centreRepository.findAll(specification, pageable);
        if (res.getSize() == 0)
            throw new ApiRequestException("Aucun élement trouvé");
        return res;
    }
    // id, nom, expressionLongitude, expressionLatitude, expressionOuverture,
    // expressionFermeture, nombrePersonnel, idUser
    public Specification<Centre> getAllSpecifications(String id, String nom, String expressionLongitude,
            String expressionLatitude, String expressionOuverture, String expressionFermeture, String nombrePersonnel, String idUser) {
        Specification<Centre> specification = ajouterSiNonNull(null, equalsIdMultiple(id, "id"));
        specification = ajouterSiNonNull(specification, equalsOrLikeNom(nom));
        specification = ajouterSiNonNull(specification, numberSpecification(expressionLongitude, "longitude"));
        specification = ajouterSiNonNull(specification, numberSpecification(expressionLatitude, "latitude"));
        specification = ajouterSiNonNull(specification, dateCreationSpecification(expressionFermeture,"fermeture"));
        specification = ajouterSiNonNull(specification, dateCreationSpecification(expressionOuverture,"ouverture"));
        specification = ajouterSiNonNull(specification, numberSpecification(nombrePersonnel, "nombrePersonnel"));
        specification = ajouterSiNonNull(specification, equalsIdUser(idUser));
        return specification;
    }

    public static Specification<Centre> numberSpecification(String expression, String intitule) {
        if (Util.isNullOrEmpty(expression))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria(intitule, expression);
        if (critere.getOperator() == SearchOperator.BETWEEN) {
            String[] values = (String[]) critere.getValue();
            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(critere.getKey())
                    .as(Double.class), Double.valueOf(values[0]), Double.valueOf(values[1]));
        }
        Double valueToDouble = Double.valueOf((String) critere.getValue());
        critere.setValue(valueToDouble);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get(critere.getKey()).as(Double.class),
                        valueToDouble);
            case GT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(
                        root.get(critere.getKey()).as(Double.class),
                        valueToDouble);
            case GTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(critere
                        .getKey())
                        .as(Double.class), valueToDouble);
            case LT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(
                        root.get(critere.getKey()).as(Double.class),
                        (Double) critere.getValue());
            case LTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(critere.getKey())
                        .as(Double.class), valueToDouble);
            default:
                return null;
        }
    }

    public static Specification<Centre> dateCreationSpecification(String expression, String intitule) {
        if (Util.isNullOrEmpty(expression))
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        SearchCriteria critere = SearchCriteria.getSearchCriteria(intitule, expression);
        if (critere.getOperator() == SearchOperator.BETWEEN) {
            String[] values = (String[]) critere.getValue();
            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(critere.getKey())
                    .as(LocalTime.class), LocalTime.parse(values[0],formatter), LocalTime.parse(values[1],formatter));
        }
        LocalTime valueToLocalTime = LocalTime.parse((String) critere.getValue(), formatter);
        critere.setValue(valueToLocalTime);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get(critere.getKey()).as(LocalTime.class),
                        valueToLocalTime);
            case GT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(
                        root.get(critere.getKey()).as(LocalTime.class),
                        valueToLocalTime);
            case GTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreation")
                        .as(LocalTime.class), valueToLocalTime);
            case LT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(
                        root.get(critere.getKey()).as(LocalTime.class),
                        (LocalTime) critere.getValue());
            case LTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(critere.getKey())
                        .as(LocalTime.class), valueToLocalTime);
            default:
                return null;
        }
    }

    public static Specification<Centre> equalsIdMultiple(String listeId, String intitule) {
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

    public static Specification<Centre> equalsIdUser(String idUser) {
        if (Util.isNullOrEmpty(idUser))
            return null;
        return (root, query, criteriaBuilder) -> {
            Join<Centre, User> jointure = root.join("user");
            return criteriaBuilder.equal(jointure.get("id"), idUser);
        };
    }

    public static Specification<Centre> equalsOrLikeNom(String expression) {
        if (Util.isNullOrEmpty(expression))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria("nom", expression);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(critere.getKey()),
                        critere.getValue());
            case LIKE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(
                        critere.getKey()).as(String.class),
                        "%" + critere.getValue().toString() + "%");
            default:
                return null;
            // throw new Exception("TypeSignalementService.equalsOrLikeNom() n'accepte pas
            // d'autres
            // critères ");
        }
    }

    public static Specification<Centre> equalsFermeture(String expression) {
        if (Util.isNullOrEmpty(expression))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria("fermeture", expression);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(critere.getKey()),
                        critere.getValue());
            default:
                return null;
            // throw new Exception("TypeSignalementService.equalsOrLikeNom() n'accepte pas
            // d'autres
            // critères ");
        }
    }
    
    public static Specification<Centre> equalsId(String expression) {
        if (Util.isNullOrEmpty(expression))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria("nom", expression);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(critere.getKey()),
                        critere.getValue());
            case LIKE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(
                        critere.getKey()).as(String.class),
                        "%" + critere.getValue().toString() + "%");
            default:
                return null;
            // throw new Exception("TypeSignalementService.equalsOrLikeNom() n'accepte pas
            // d'autres
            // critères ");
        }
    }

    public Specification<Centre> ajouterSiNonNull(Specification<Centre> specificationMere,
            Specification<Centre> specificationAAjouter) {
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
