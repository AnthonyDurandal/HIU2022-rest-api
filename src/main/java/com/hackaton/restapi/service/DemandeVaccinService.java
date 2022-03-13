package com.hackaton.restapi.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import com.hackaton.restapi.entity.DemandeVaccin;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.DemandeVaccinRepository;
import com.hackaton.restapi.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hackaton.restapi.query.SearchCriteria;
import com.hackaton.restapi.query.SearchOperator;

@Service
public class DemandeVaccinService {

    private final DemandeVaccinRepository demandeVaccinRepository;
    private final CentreService centreService;

    @Autowired
    public DemandeVaccinService(DemandeVaccinRepository demandeVaccinRepository, CentreService centreService) {
        this.demandeVaccinRepository = demandeVaccinRepository;
        this.centreService = centreService;
    }

    public DemandeVaccin addNewDemandeVaccin(DemandeVaccin demandeVaccin) {
        if(demandeVaccin == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(demandeVaccin.getCentre() == null || demandeVaccin.getDateDemande() == null || 
        demandeVaccin.getRemarque() == null || demandeVaccin.getPersonne() == null || 
        demandeVaccin.getVaccin() == null || demandeVaccin.isEstAnnule() == null)
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        if(!centreService.findById(demandeVaccin.getCentre().getId()).getEstOuvert())
            throw new ApiRequestException("Vous devez choisir un centre ouvert");    
        return demandeVaccinRepository.save(demandeVaccin);
    }

    @Transactional
    public void updateDemandeVaccin(Long id, DemandeVaccin demandeVaccin) {
        DemandeVaccin demandeVaccinModif = demandeVaccinRepository.findById(id)
        .orElseThrow(()-> new ApiRequestException("Cette demande vaccin n'existe pas"));
        if(demandeVaccin.getCentre() != null)
            demandeVaccinModif.setCentre(demandeVaccin.getCentre());
        if(demandeVaccin.getDateDemande() != null)
            demandeVaccinModif.setDateDemande(demandeVaccin.getDateDemande());
        if(demandeVaccin.getRemarque() != null)
            demandeVaccinModif.setRemarque(demandeVaccin.getRemarque());
        if(demandeVaccin.getPersonne() != null)
            demandeVaccinModif.setPersonne(demandeVaccin.getPersonne());
        if(demandeVaccin.getVaccin() != null)
            demandeVaccinModif.setVaccin(demandeVaccin.getVaccin());
        if(demandeVaccin.isEstAnnule() != null)
            demandeVaccinModif.setEstAnnule(demandeVaccin.isEstAnnule());
    }

    public Page<DemandeVaccin> getDemandeVaccin(String sort, Integer page, Integer size,
            String id, String expressionNom) {
        Specification<DemandeVaccin> specification = getAllSpecifications(id, expressionNom);
        specification = (specification != null) ? Specification.where(specification) : null;
        if (page == null)
            page = 1;
        if (size == null || size == 0)
            size = demandeVaccinRepository.findAll().size();
        if (page < 1)
            throw new ApiRequestException("L'index de la page ne doit pas être inférieur à un : page = " + page);
        if (size < 0)
            throw new ApiRequestException(
                    "La taille de la page ne doit pas être inférieure à un : size = " + size + "    " + (size == 0));
        Pageable pageable = Util.pageable(sort, page - 1, size);
        Page<DemandeVaccin> res = demandeVaccinRepository.findAll(specification, pageable);
        if (res.getSize() == 0)
            throw new ApiRequestException("Aucun élement trouvé");
        return res;
    }

    public Specification<DemandeVaccin> getAllSpecifications(String id, String idDemandeVaccin) {
        Specification<DemandeVaccin> specification = ajouterSiNonNull(null, equalsIdMultiple(id, "id"));
        specification = ajouterSiNonNull(specification, equalsIdDemandeVaccinMultiple(idDemandeVaccin));
        return specification;
    }

    public static Specification<DemandeVaccin> equalsIdMultiple(String listeId, String intitule) {
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

    public static Specification<DemandeVaccin> equalsIdDemandeVaccinMultiple(String expressionIdDemandeVaccindMultiple) {
        if (expressionIdDemandeVaccindMultiple == null || expressionIdDemandeVaccindMultiple.length() == 0)
            return null;
        return (root, query, criteriaBuilder) -> {
            Join<DemandeVaccin, DemandeVaccin> jointure = root.join("demandeVaccin");
            List<Predicate> conditionsDansOr = new ArrayList<Predicate>();
            for (String idVaccin : expressionIdDemandeVaccindMultiple.split(",")) {
                if (idVaccin.compareTo("") != 0) {
                    Predicate equalsIdVaccin = criteriaBuilder.equal(jointure.get("id"), idVaccin);
                    conditionsDansOr.add(equalsIdVaccin);
                }
            }
            return criteriaBuilder.or(conditionsDansOr.toArray(new Predicate[conditionsDansOr.size()]));
        };
    }

    public static Specification<DemandeVaccin> dateCreationSpecification(String expression, String intitule) {
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

    public Specification<DemandeVaccin> ajouterSiNonNull(Specification<DemandeVaccin> specificationMere,
            Specification<DemandeVaccin> specificationAAjouter) {
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
