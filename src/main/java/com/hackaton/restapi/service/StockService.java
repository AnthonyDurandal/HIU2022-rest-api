package com.hackaton.restapi.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import com.hackaton.restapi.entity.Centre;
import com.hackaton.restapi.entity.Stock;
import com.hackaton.restapi.entity.Vaccin;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.StockRepository;
import com.hackaton.restapi.util.Util;

import com.hackaton.restapi.query.SearchCriteria;
import com.hackaton.restapi.query.SearchOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock addNewStock(Stock stock) {
        if(stock == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(stock.getCentre()==null || stock.getDateArrivee()==null || stock.getDatePeremption()==null || stock.getVaccin() == null) 
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        return stockRepository.save(stock);
    }

    @Transactional
    public void updateStock(Long idStock, Stock stock) {
        Stock stockModif = stockRepository.findById(idStock)
        .orElseThrow(()-> new ApiRequestException("Ce stock n'existe pas"));
        if(stock.getCentre() != null)
            stockModif.setCentre(stock.getCentre());
        if(stock.getDateArrivee() != null)
            stockModif.setDateArrivee(stock.getDateArrivee());
        if(stock.getDatePeremption() != null)
            stockModif.setDatePeremption(stock.getDatePeremption());
        if(stock.getVaccin() != null)
            stockModif.setVaccin(stock.getVaccin());
    }

    public Page<Stock> getStock(String sort, Integer page, Integer size,
            String id, String expressionVaccin,
            String expressionCentre, String expressionDateArrivee, String expressionDatePeremtion) {
        Specification<Stock> specification = getAllSpecifications( id, expressionVaccin,
             expressionCentre, expressionDateArrivee, expressionDatePeremtion);
        specification = (specification != null) ? Specification.where(specification) : null;
        if (page == null)
            page = 1;
        if (size == null || size == 0)
            size = stockRepository.findAll().size();
        if (page < 1)
            throw new ApiRequestException("L'index de la page ne doit pas être inférieur à un : page = " + page);
        if (size < 0)
            throw new ApiRequestException(
                    "La taille de la page ne doit pas être inférieure à un : size = " + size + "    " + (size == 0));
        Pageable pageable = Util.pageable(sort, page - 1, size);
        Page<Stock> res = stockRepository.findAll(specification, pageable);
        if (res.getSize() == 0)
            throw new ApiRequestException("Aucun élement trouvé");
        return res;
    }

    public Specification<Stock> getAllSpecifications(String id,String expressionVaccin,
            String expressionCentre,String expressionDateArrivee,String expressionDatePeremtion) {
        Specification<Stock> specification = ajouterSiNonNull(null, equalsIdMultiple(id,"id"));
        specification = ajouterSiNonNull(specification, equalsIdVaccin(expressionVaccin));
        specification = ajouterSiNonNull(specification, equalsIdCentre(expressionCentre));
        specification = ajouterSiNonNull(specification, dateCreationSpecification(expressionDateArrivee,"dateArrivee"));
        specification = ajouterSiNonNull(specification, dateCreationSpecification(expressionDatePeremtion,"datePeremption"));
        return specification;
    }

    public static Specification<Stock> equalsIdSimple(String value,String intituleColonne) {
        if (Util.isNullOrEmpty(value))
            return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(intituleColonne), value);
    }

    public static <T> Specification<Stock> equalsIdMultiple(String listeId,String intitule) {
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

    public static <T> Specification<Stock> equalsIdVaccin(String listeId) {
        if (listeId == null || listeId.length() == 0)
            return null;
        return (root, query, criteriaBuilder) -> {
            // System.out.println("foreignKeyClass   :  "+T.class);
            Join<Stock, Vaccin> jointure = root.join("vaccin", JoinType.LEFT);
            List<Predicate> conditionsDansOr = new ArrayList<Predicate>();
            for (String idVaccin : listeId.split(",")) {
                if (idVaccin.compareTo("") != 0) {
                    Predicate equalsIdVaccin = criteriaBuilder.equal(jointure.get("id"), idVaccin);
                    conditionsDansOr.add(equalsIdVaccin);
                }
            }
            return criteriaBuilder.or(conditionsDansOr.toArray(new Predicate[conditionsDansOr.size()]));
        };
    }

    public static <T> Specification<Stock> equalsIdCentre(String listeId) {
        if (listeId == null || listeId.length() == 0)
            return null;
        return (root, query, criteriaBuilder) -> {
            // System.out.println("foreignKeyClass : "+T.class);
            Join<Stock, Centre> jointure = root.join("centre", JoinType.LEFT);
            List<Predicate> conditionsDansOr = new ArrayList<Predicate>();
            for (String idVaccin : listeId.split(",")) {
                if (idVaccin.compareTo("") != 0) {
                    Predicate equalsIdVaccin = criteriaBuilder.equal(jointure.get("id"), idVaccin);
                    conditionsDansOr.add(equalsIdVaccin);
                }
            }
            return criteriaBuilder.or(conditionsDansOr.toArray(new Predicate[conditionsDansOr.size()]));
        };
    }

    // public static <T> Specification<Stock> equalsIdMultipleZ(String listeId, String intitule, Class<T> foreignKeyClass) {
    //     if (listeId == null || listeId.length() == 0)
    //         return null;
    //     return (root, query, criteriaBuilder) -> {
    //         // System.out.println("foreignKeyClass : "+T.class);
    //         Join<Stock, foreignKeyClass> jointure = (foreignKeyClass != null)
    //                 ? jointure = root.join(intitule, JoinType.LEFT)
    //                 : null;
    //         List<Predicate> conditionsDansOr = new ArrayList<Predicate>();
    //         for (String idVaccin : listeId.split(",")) {
    //             if (idVaccin.compareTo("") != 0) {
    //                 Predicate equalsIdVaccin = (jointure != null)
    //                         ? criteriaBuilder.equal(jointure.get(intitule), idVaccin)
    //                         : criteriaBuilder.equal(root.get(intitule), idVaccin);
    //                 conditionsDansOr.add(equalsIdVaccin);
    //             }
    //         }
    //         return criteriaBuilder.or(conditionsDansOr.toArray(new Predicate[conditionsDansOr.size()]));
    //     };
    // }

    public static Specification<Stock> dateCreationSpecification(String expression,String intitule) 
    {
        if (Util.isNullOrEmpty(expression))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria(intitule, expression);
        if (critere.getOperator() == SearchOperator.BETWEEN) {
            String[] values = (String[]) critere.getValue();
            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(
                    intitule)
                    .as(Timestamp.class), Timestamp.valueOf(values[0]), Timestamp.valueOf(values[1]));
        }
        Timestamp valueToTimestamp = Timestamp.valueOf((String) critere.getValue());
        critere.setValue(valueToTimestamp);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get(
                                intitule).as(Timestamp.class),
                        valueToTimestamp);
            case GT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(
                        root.get(
                                intitule).as(Timestamp.class),
                        valueToTimestamp);
            case GTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(
                        intitule)
                        .as(Timestamp.class), valueToTimestamp);
            case LT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(
                        root.get(
                                intitule).as(Timestamp.class),
                        (Timestamp) critere.getValue());
            case LTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(
                        intitule)
                        .as(Timestamp.class), valueToTimestamp);
            default:
                return null;
        }
    }

    public Specification<Stock> ajouterSiNonNull(Specification<Stock> specificationMere,
            Specification<Stock> specificationAAjouter) {
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
