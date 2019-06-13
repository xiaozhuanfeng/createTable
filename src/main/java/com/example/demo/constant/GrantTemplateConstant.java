package com.example.demo.constant;

public class GrantTemplateConstant {
    public static final String GRANT_TEMPLATE = "grant {operateArr} on {owner}.{tableName} to {roleName};";

    private GrantTemplateConstant() {
        throw new AssertionError("The Class cannot be instance....");
    }
}
