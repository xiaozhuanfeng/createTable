package com.example.demo.constant;

import org.apache.commons.lang3.StringUtils;

public class IndexTemplateConstant {

    /**
     * 普通索引
     */
    public static final String INDEXTYPE_COMMON = "0";

    /**
     * 主键标识
     */
    public static final String INDEXTYPE_PK = "1";

    /**
     * 唯一键索引
     */
    public static final String INDEXTYPE_UQ = "2";

    /**
     * 主键索引
     */
    private static final String PK_INDEX = "create unique index {owner}.{indexName} on {owner}.{tableName}" +
            "({inxColumnNames}) initrans 16;\nalter table {owner}.{tableName} add constraint {indexName} primary key" +
            "({inxColumnNames}) using index {owner}.{indexName};";

    /**
     * 普通索引
     */
    private static final String COMMON_INDEX = "create index {owner}.{indexName} on {owner}.{tableName}" +
            "({inxColumnNames}) initrans 16 online;";

    /**
     * Unique索引
     */
    private static final String UNIQUE_INDEX = "create unique index {owner}.{indexName} on {owner}.{tableName}" +
            "({inxColumnNames}) initrans 16;";


    private IndexTemplateConstant() {
        throw new AssertionError("The Class cannot be instance....");
    }

    /**
     * 获取索引模板
     * @param indexType
     * @return
     */
    public static String getIndexTemplate(String indexType){
        String indexTemplate = COMMON_INDEX;
        if(StringUtils.equals(INDEXTYPE_PK,indexType)){
            indexTemplate = PK_INDEX;
        }else if(StringUtils.equals(INDEXTYPE_UQ,indexType)){
            indexTemplate = UNIQUE_INDEX;
        }
        return indexTemplate;
    }
}
