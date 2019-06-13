package com.example.demo.constant;

public class SynonymTemplateConstant {

    /**
     * 同义词模板
     */
    public static final String SYNONYM_TEMPLATE = "create public synonym {tableName} for {owner}.{tableName};";

    private SynonymTemplateConstant() {
        throw new AssertionError("The Class cannot be instance....");
    }
}
