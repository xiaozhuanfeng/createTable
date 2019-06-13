package com.example.demo.dto;

public class GrantDto {

    public static final String REPLACE_OWNER = "{owner}";

    public static final String REPLACE_TABLENAME = "{tableName}";

    public static final String REPLACE_OPERATEARR = "{operateArr}";

    public static final String REPLACE_ROLENAME = "{roleName}";

    private String owner;

    private String tableName;

    private String roleName;

    private String operateArr;

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOperateArr() {
        return operateArr;
    }

    public void setOperateArr(String operateArr) {
        this.operateArr = operateArr;
    }
}
