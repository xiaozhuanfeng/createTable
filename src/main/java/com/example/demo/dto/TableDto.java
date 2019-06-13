package com.example.demo.dto;

import java.util.List;

public class TableDto {

    public static final String REPLACE_OWNER = "{owner}";
    public static final String REPLACE_TABLENAME = "{tableName}";
    public static final String REPLACE_COLUMNS = "{columns}";
    public static final String REPLACE_TABLEDESCRIBE = "{tableDescribe}";
    public static final String REPLACE_COMMENTS = "{comments}";

    public static final String AUTOFLAG_YES = "Yes";

    private String owner;
    private String tableName;
    private String tableDescribe;

    /**
     * 是否自动生成
     *  created_by   VARCHAR2(30),
     *  created_date DATE default sysdate,
     *  updated_by   VARCHAR2(30),
     *  updated_date DATE default sysdate,
     */
    private String autoFlag;

    private List<ColumnDto> columnList;
    private List<IndexDto> inxList;
    private List<GrantDto> grtList;

    public TableDto() {
    }

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

    public String getTableDescribe() {
        return tableDescribe;
    }

    public void setTableDescribe(String tableDescribe) {
        this.tableDescribe = tableDescribe;
    }

    public List<ColumnDto> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnDto> columnList) {
        this.columnList = columnList;
    }

    public String getAutoFlag() {
        return autoFlag;
    }

    public void setAutoFlag(String autoFlag) {
        this.autoFlag = autoFlag;
    }

    public List<IndexDto> getInxList() {
        return inxList;
    }

    public void setInxList(List<IndexDto> inxList) {
        this.inxList = inxList;
    }

    public List<GrantDto> getGrtList() {
        return grtList;
    }

    public void setGrtList(List<GrantDto> grtList) {
        this.grtList = grtList;
    }

    @Override
    public String toString() {
        return "TableDto{" +
                "owner='" + owner + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableDescribe='" + tableDescribe + '\'' +
                ", autoFlag='" + autoFlag + '\'' +
                ", columnList=" + columnList +
                ", inxList=" + inxList +
                ", grtList=" + grtList +
                '}';
    }
}
