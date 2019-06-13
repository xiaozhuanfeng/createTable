package com.example.demo.dto;

public class ColumnDto {

    public static final String REPLACE_COLUMNNAME = "{columnName}";

    public static final String REPLACE_COLUMNDESCRIBE = "{columnDescribe}";

    public static final String REPLACE_COLUMNTYPE = "{columnType}";

    public static final String REPLACE_DEFAULTVAL = "{defaultVal}";

    /**
     * 可空
     */
    public static final String COLUMNNULLFLAG_YES = "Yes";

    /**
     * 不可为空
     */
    public static final String COLUMNNULLFLAG_NO = "No";

    /**
     * 列名
     */
    private String columnName;

    /**
     * 类型
     */
    private String columnType;

    /**
     * 是否空
     */
    private String columnNullFlag;

    /**
     * 注释
     */
    private String columnDescribe;

    /**
     * 默认值
     */
    private String defaultVal;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnNullFlag() {
        return columnNullFlag;
    }

    public void setColumnNullFlag(String columnNullFlag) {
        this.columnNullFlag = columnNullFlag;
    }

    public String getColumnDescribe() {
        return columnDescribe;
    }

    public void setColumnDescribe(String columnDescribe) {
        this.columnDescribe = columnDescribe;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }
}
