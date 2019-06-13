package com.example.demo.dto;

public class IndexDto {

    public static final String REPLACE_OWNER = "{owner}";

    public static final String REPLACE_TABLENAME = "{tableName}";

    public static final String REPLACE_INDEXNAME = "{indexName}";

    public static final String REPLACE_INXCOLUMNNAMES = "{inxColumnNames}";

    private String owner;

    private String tableName;

    private String indexName;

    private String indexType;

    private String inxColumnNames;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getInxColumnNames() {
        return inxColumnNames;
    }

    public void setInxColumnNames(String inxColumnNames) {
        this.inxColumnNames = inxColumnNames;
    }
}
