package com.hackaton.restapi.query;

public enum SearchOperator {
    //EQUALS, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;
    EQUALS, GT, GTE, LT, LTE, BEFORE, AFTER,LIKE, STARTS_WITH, ENDS_WITH, CONTAINS, BETWEEN;
    //gt , gte, lt,lte, before, after

    public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~" };

    
}
