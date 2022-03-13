package com.hackaton.restapi.query;

import java.util.HashMap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchCriteria {
    private String key;
    private SearchOperator operator;
    private Object value;

    /*
     * permet de chercher le SearchOperator correspondant
     * ex URL: /signalements?dateCr­eation=gt:86654&dateCreation=lt:7544
     * dateCreation.value = "gt:86654"
     * commence par gt: => return SearchOperator.GT
     */
    public static SearchCriteria getSearchCriteria(String key,String expression)
    {
        if (expression.contains("lt:") && expression.contains("gt:") )
        {
            String[]valeurs = null;
            boolean ltDevantGt = (expression.indexOf("lt:") < expression.indexOf("gt:"));
            if(ltDevantGt)
            {
                valeurs = expression.split("gt:");
                String temp = valeurs[0];
                valeurs[0] = valeurs[1].replace("gt:", "");
                valeurs[1] = temp.replace("lt:", "");
            }else{
                valeurs = expression.split("lt:");
                valeurs[0] = valeurs[0].replace("gt:", "");
                valeurs[1] = valeurs[1].replace("lt:", "");
            }
            /*
             * if(ltDevantGt)
             * {
             * valeurs = expression.split("gt:");
             * valeurs[0] = valeurs[0].replace("lt:", "");
             * valeurs[1] = valeurs[1].replace("gt:", "");
             * 
             * }else{
             * valeurs = expression.split("lt:");
             * String temp = valeurs[0].replace("gt:", "");
             * valeurs[0] = valeurs[1].replace("lt:", "");
             * valeurs[1] = temp;
             * }
             */
            return SearchCriteria.builder().key(key).operator(SearchOperator.BETWEEN).value(valeurs)
                    .build();
        }
        if (expression.startsWith("gt:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.GT).value(expression.replace("gt:","")).build();
        else if (expression.startsWith("gte:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.GTE).value(expression.replace("gte:","")).build();
        else if (expression.startsWith("lt:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.LT).value(expression.replace("lt:","")).build();
        else if (expression.startsWith("lte:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.LTE).value(expression.replace("lte:","")).build();
        else if (expression.startsWith("before:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.BEFORE).value(expression.replace("before:","")).build();
        else if (expression.startsWith("after:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.AFTER).value(expression.replace("after:","")).build();
        else if (expression.startsWith("like:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.LIKE).value(expression.replace("like:","")).build();
        else if (expression.startsWith("starts_with:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.STARTS_WITH).value(expression.replace("starts_with:","")).build();
        else if (expression.startsWith("ends_with:"))
            return SearchCriteria.builder().key(key).operator(SearchOperator.ENDS_WITH).value(expression.replace("ends_with:","")).build();
        else
            return SearchCriteria.builder().key(key).operator(SearchOperator.EQUALS)
                    .value(expression).build();
        // throw new Exception("expression non prise en charge");
    }
}