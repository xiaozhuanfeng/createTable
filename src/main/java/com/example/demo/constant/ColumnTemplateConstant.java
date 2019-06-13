package com.example.demo.constant;

public class ColumnTemplateConstant {

    public static final String COLUMN_HAS_DEFAULT_NOT_NULL = "{columnName} {columnType} default {defaultVal} not null";

    public static final String COLUMN_NOT_NULL = "{columnName} {columnType} not null";

    public static final String COLUMN_HAS_DEFAULT_NULL = "{columnName} {columnType} default {defaultVal}";

    public static final String COLUMN_NULL = "{columnName} {columnType}";

    /**
     * varchar类型默认值
     */
    public static final String COLUMN_HAS_SINGLEQUOTES_DEFAULT_NOT_NULL = "{columnName} {columnType} default '{defaultVal}' not null";

    /**
     * varchar类型默认值
     */
    public static final String COLUMN_HAS_SINGLEQUOTES_DEFAULT_NULL = "{columnName} {columnType} default '{defaultVal}'";

    /**
     * 列注释
     */
    public static final String COLUMN_COMMENT = "comment on column {owner}.{tableName}.{columnName} is '{columnDescribe}';";

    private ColumnTemplateConstant() {
        throw new AssertionError("The Class cannot be instance....");
    }
}
