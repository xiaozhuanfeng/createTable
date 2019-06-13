package com.example.demo.service;

import com.example.demo.constant.*;
import com.example.demo.dto.ColumnDto;
import com.example.demo.dto.GrantDto;
import com.example.demo.dto.IndexDto;
import com.example.demo.dto.TableDto;
import com.example.demo.util.CommonCollectionUtils;
import com.example.demo.util.CommonFileUtils;
import com.example.demo.util.ProjectPathUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.example.demo.dto.ColumnDto.COLUMNNULLFLAG_YES;

public class OracleSqlBuilder {
    private static final Logger logger = LoggerFactory.getLogger(OracleSqlBuilder.class);

    /**
     * 数据库表名、列名、索引名最大命名长度
     */
    public static final int MAX_ORACLE_LENGTH = 30;

    /**
     * 触发器模板允许长度：30 <= 6 + (tableName)
     */
    public static final int TRI_NAME_EXSITS_LENGTH = 24;

    public static final String PAK_TARGET = "target";
    public static final String PAK_TEMPLATE_ORACLE = "template\\oracle\\";
    public static final String PAK_TEMPLATE_EXPORT = "template\\export\\";
    public static final String CRT_FILE_TEMPLATE = "tab_crt_template";
    public static final String TRI_BI_FILE_TEMPLATE = "tab_tri_bi_template";
    public static final String TRI_BU_FILE_TEMPLATE = "tab_tri_bu_template";

    /**
     * varchar2正则
     */
    private static final String VARCHAR_PATTERN = "^(VARCHAR2\\()([1-9])\\d*\\)$";

    /**
     * char 正则
     */
    private static final String CHAR_PATTERN = "^(CHAR\\()([1-9])\\d*\\)$";

    private OracleSqlBuilder() {
        throw new AssertionError();
    }

    /**
     * 指定文件模板
     *
     * @param fileName
     * @return
     */
    private static String getFilePath(String fileName) {
        return getFilePath(fileName,PAK_TEMPLATE_ORACLE);
    }

    /**
     * 指定文件模板
     *
     * @param fileName
     * @return
     */
    public static String getFilePath(String fileName,String pakagePath) {
        String filePath = ProjectPathUtils.getRootPath("");
        int index = filePath.indexOf(PAK_TARGET);
        filePath = StringUtils.substring(filePath, 0, index);

        filePath = filePath + pakagePath + fileName;

        System.out.println(">>>>filePath = " + filePath);
        return filePath;
    }


   /* public static String buildOrcSql(TableDto dto){

    }*/

    /**
     * create table sql
     *
     * @param dto
     * @return
     */
    public static String buildCrtSql(TableDto dto) {
        String filePath = getFilePath(CRT_FILE_TEMPLATE);
        String sqlTemplate = CommonFileUtils.scannerReadFile(filePath, "utf-8");
        String result = "";

        if (StringUtils.isNotBlank(sqlTemplate)) {
            Map<String, String> keyValMap = CommonCollectionUtils.newHashMapInstance();
            keyValMap.put(TableDto.REPLACE_OWNER, dto.getOwner());
            keyValMap.put(TableDto.REPLACE_TABLENAME, dto.getTableName());
            keyValMap.put(TableDto.REPLACE_TABLEDESCRIBE, dto.getTableDescribe());

            String columns = buildColumns(dto.getColumnList(), dto.getAutoFlag());
            keyValMap.put(TableDto.REPLACE_COLUMNS, columns);

            String comments = buildColumnComments(dto.getColumnList(), dto.getOwner(), dto.getTableName());
            keyValMap.put(TableDto.REPLACE_COMMENTS, comments);

            result = replaceAllKeyVal(sqlTemplate, keyValMap);
        } else {
            logger.error("建表模板为空>>>>>>>");
        }

        return result;
    }

    /**
     * create index sql
     *
     * @param indexDtoList
     * @return
     */
    public static String buildInxSql(List<IndexDto> indexDtoList) {

        StringBuilder idxBuilder = new StringBuilder();

        if (!CollectionUtils.isEmpty(indexDtoList)) {

            for (IndexDto dto : indexDtoList) {
                //索引命名
                dto.setIndexName(getIndexName(dto));

                //获取索引模板
                String indexTemplate = IndexTemplateConstant.getIndexTemplate(dto.getIndexType());
                Map<String, String> keyValMap = CommonCollectionUtils.newHashMapInstance();
                keyValMap.put(IndexDto.REPLACE_OWNER, dto.getOwner());
                keyValMap.put(IndexDto.REPLACE_TABLENAME, dto.getTableName());
                keyValMap.put(IndexDto.REPLACE_INDEXNAME, dto.getIndexName());
                keyValMap.put(IndexDto.REPLACE_INXCOLUMNNAMES, dto.getInxColumnNames());

                idxBuilder.append(replaceAllKeyVal(indexTemplate, keyValMap)).append("\n");
            }
        }

        return idxBuilder.toString();
    }

    /**
     * create synonym sql
     *
     * @param dto
     * @return
     */
    public static String buildSynonymSql(TableDto dto) {
        Map<String, String> keyValMap = CommonCollectionUtils.newHashMapInstance();
        keyValMap.put(TableDto.REPLACE_OWNER, dto.getOwner());
        keyValMap.put(TableDto.REPLACE_TABLENAME, dto.getTableName());

        return replaceAllKeyVal(SynonymTemplateConstant.SYNONYM_TEMPLATE, keyValMap);
    }

    /**
     * create grant sql
     *
     * @param grantDtoList
     * @return
     */
    public static String buildGrtSql(List<GrantDto> grantDtoList) {

        StringBuilder grtBuilder = new StringBuilder();

        if (!CollectionUtils.isEmpty(grantDtoList)) {
            for (GrantDto dto : grantDtoList) {
                Map<String, String> keyValMap = CommonCollectionUtils.newHashMapInstance();
                keyValMap.put(GrantDto.REPLACE_OWNER, dto.getOwner());
                keyValMap.put(GrantDto.REPLACE_TABLENAME, dto.getTableName());
                keyValMap.put(GrantDto.REPLACE_OPERATEARR, dto.getOperateArr());
                keyValMap.put(GrantDto.REPLACE_ROLENAME, dto.getRoleName());
                grtBuilder.append(replaceAllKeyVal(GrantTemplateConstant.GRANT_TEMPLATE, keyValMap)).append("\n");
            }

        } else {
            logger.info("无其他角色需要授权");
        }
        return grtBuilder.toString();
    }

    /**
     * create bi trigger sql
     *
     * @param dto
     * @return
     */
    public static String buildBiTriSql(TableDto dto) {
        return buildTriSql(dto, TRI_BI_FILE_TEMPLATE);
    }

    /**
     * create bu trigger sql
     *
     * @param dto
     * @return
     */
    public static String buildBuTriSql(TableDto dto) {
        return buildTriSql(dto, TRI_BU_FILE_TEMPLATE);
    }

    /**
     * create trigger sql
     *
     * @param dto
     * @return
     */
    private static String buildTriSql(TableDto dto, String fileTemplatePath) {
        String triSql = null;
        if (StringUtils.equalsIgnoreCase(TableDto.AUTOFLAG_YES, dto.getAutoFlag())) {
            //created_date,updated_date 自动生成
            String filePath = getFilePath(fileTemplatePath);
            String sqlTemplate = CommonFileUtils.scannerReadFile(filePath, "utf-8");
            Map<String, String> keyValMap = CommonCollectionUtils.newHashMapInstance();
            keyValMap.put(TableDto.REPLACE_OWNER, StringUtils.upperCase(dto.getOwner()));

            //Oracle  长度最大命名规则<=30
            String tableName = StringUtils.upperCase(dto.getTableName());
            if (TRI_NAME_EXSITS_LENGTH < tableName.length()) {
                tableName = StringUtils.substring(tableName, 0, TRI_NAME_EXSITS_LENGTH);
            }
            keyValMap.put(TableDto.REPLACE_TABLENAME, tableName);
            triSql = replaceAllKeyVal(sqlTemplate, keyValMap);
        } else {
            logger.info("created_date,updated_date 不自动生成，不创建触发器.....");
        }
        return triSql;
    }

    private static String getIndexName(IndexDto dto) throws RuntimeException {

        String indexName = "";
        String inxColumnNames = dto.getInxColumnNames();

        if (StringUtils.isNotBlank(inxColumnNames)) {
            String indexType = dto.getIndexType();

            String handleInxColumnNames = handleInxColumnNames(inxColumnNames);

            if (StringUtils.equals(IndexTemplateConstant.INDEXTYPE_PK, indexType)) {
                //主键
                indexName = "PK_" + handleInxColumnNames;

            } else if (StringUtils.equals(IndexTemplateConstant.INDEXTYPE_UQ, indexType)) {
                //唯一键
                indexName = "UK_" + handleInxColumnNames;
            } else {
                indexName = "IX_" + handleInxColumnNames;
            }

            //数据库默认索引命名最大长度为30
            if (MAX_ORACLE_LENGTH < indexName.length()) {
                indexName = StringUtils.substring(indexName, 0, MAX_ORACLE_LENGTH);
            }
        } else {
            throw new RuntimeException(">>>>>>index need columns...");
        }
        return indexName;
    }

    /**
     * @param inxColumnNames
     * @return
     */
    private static String handleInxColumnNames(String inxColumnNames) {
        String handleInxColumnNames = StringUtils.upperCase(inxColumnNames);
        return handleInxColumnNames.replaceAll(",", "_");
    }

    /**
     * 获取表所有列注释
     *
     * @param columnList
     * @param owner
     * @param tableName
     * @return
     */
    public static String buildColumnComments(List<ColumnDto> columnList, String owner, String tableName) {
        String comments = "";
        if (!CollectionUtils.isEmpty(columnList)) {
            StringBuilder columnComments = new StringBuilder();
            //要替换的key-val
            Map<String, String> keyValMap = CommonCollectionUtils.newHashMapInstance();
            keyValMap.put(TableDto.REPLACE_OWNER, owner);
            keyValMap.put(TableDto.REPLACE_TABLENAME, tableName);

            //属主和表名替换
            String columnComentTemplate = replaceAllKeyVal(ColumnTemplateConstant.COLUMN_COMMENT, keyValMap);

            //清空key-val
            keyValMap.clear();

            for (ColumnDto dto : columnList) {
                if (StringUtils.isNotBlank(dto.getColumnDescribe())) {
                    keyValMap.put(ColumnDto.REPLACE_COLUMNNAME, dto.getColumnName());
                    keyValMap.put(ColumnDto.REPLACE_COLUMNDESCRIBE, dto.getColumnDescribe());

                    columnComments.append(replaceAllKeyVal(columnComentTemplate, keyValMap)).append("\n");
                }
            }

            comments = columnComments.toString();

        }

        return comments;
    }

    /**
     * 创建列
     *
     * @param columnList
     * @param autoFlag
     * @return
     */
    public static String buildColumns(List<ColumnDto> columnList, String autoFlag) {
        String columns = "";
        if (!CollectionUtils.isEmpty(columnList)) {
            StringBuilder columnComments = new StringBuilder();

            if (StringUtils.equalsIgnoreCase(TableDto.AUTOFLAG_YES, autoFlag)) {
                /**
                 * 是否自动生成
                 *  created_by   VARCHAR2(30),
                 *  created_date DATE default sysdate,
                 *  updated_by   VARCHAR2(30),
                 *  updated_date DATE default sysdate,
                 */
                addAutoColum(columnList);
            }


            for (ColumnDto dto : columnList) {
                Map<String, Object> columnTemplateKeyMap = getColumnTemplateAndKeyMap(dto);
                String columnTemplate = (String) columnTemplateKeyMap.get("columnKey");
                Map<String, String> keyMap = (Map<String, String>) columnTemplateKeyMap.get("columnVal");
                columnComments.append(replaceAllKeyVal(columnTemplate, keyMap)).append(",").append("\n");
            }
            int endIndex = columnComments.toString().lastIndexOf(",");
            columns = StringUtils.substring(columnComments.toString(), 0, endIndex);
        }

        return columns;
    }

    /**
     * 自动添加默认列
     *
     * @param columnList
     */
    public static void addAutoColum(List<ColumnDto> columnList) {
        ColumnDto colDto1 = new ColumnDto();
        colDto1.setColumnName("created_by");
        colDto1.setColumnType("VARCHAR2(30)");
        colDto1.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_YES);
        colDto1.setColumnDescribe("创建人");

        ColumnDto colDto2 = new ColumnDto();
        colDto2.setColumnName("created_date");
        colDto2.setColumnType("DATE");
        colDto2.setDefaultVal(DefualtValConstant.SYSDATE);
        colDto2.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_YES);
        colDto2.setColumnDescribe("创建时间");

        ColumnDto colDto3 = new ColumnDto();
        colDto3.setColumnName("updated_by");
        colDto3.setColumnType("VARCHAR2(30)");
        colDto3.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_YES);
        colDto3.setColumnDescribe("更新人");

        ColumnDto colDto4 = new ColumnDto();
        colDto4.setColumnName("updated_date");
        colDto4.setColumnType("DATE");
        colDto4.setDefaultVal(DefualtValConstant.SYSDATE);
        colDto4.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_YES);
        colDto4.setColumnDescribe("更新时间");

        columnList.add(colDto1);
        columnList.add(colDto2);
        columnList.add(colDto3);
        columnList.add(colDto4);
    }


    public static Map<String, Object> getColumnTemplateAndKeyMap(ColumnDto dto) {
        Map<String, Object> resultMap = CommonCollectionUtils.newHashMapInstance();

        String result = "";
        Map<String, String> valMap = CommonCollectionUtils.newHashMapInstance();
        valMap.put(ColumnDto.REPLACE_COLUMNNAME, dto.getColumnName());

        String columnType = dto.getColumnType();
        valMap.put(ColumnDto.REPLACE_COLUMNTYPE, columnType);

        String columnNullFlag = dto.getColumnNullFlag();
        String defaultVal = dto.getDefaultVal();
        if (StringUtils.equals(columnNullFlag, COLUMNNULLFLAG_YES)) {
            //字段可为空
            if (StringUtils.isNotBlank(defaultVal)) {
                //有默认值
                result = getDefaultTemplate(columnType, defaultVal, true);

                valMap.put(ColumnDto.REPLACE_DEFAULTVAL, defaultVal);

            } else {
                result = ColumnTemplateConstant.COLUMN_NULL;
            }
        } else {
            //字段不可为空
            if (StringUtils.isNotBlank(defaultVal)) {
                //有默认值
                result = getDefaultTemplate(columnType, defaultVal, false);
                valMap.put(ColumnDto.REPLACE_DEFAULTVAL, defaultVal);
            } else {
                result = ColumnTemplateConstant.COLUMN_NOT_NULL;
            }
        }

        resultMap.put("columnKey", result);
        resultMap.put("columnVal", valMap);
        return resultMap;
    }

    private static String getDefaultTemplate(String columnType, String defaultVal, boolean isNull) {
        String result = "";

        boolean singleQuotes = !StringUtils.equalsIgnoreCase(defaultVal, DefualtValConstant.SYS_GUID);

        // !sys_guid && (varchar2 || char) 类型字段，默认值需要单引号
        singleQuotes = singleQuotes && (isMatch(VARCHAR_PATTERN, columnType) || isMatch(CHAR_PATTERN, columnType));

        if (isNull) {
            result = singleQuotes ? ColumnTemplateConstant.COLUMN_HAS_SINGLEQUOTES_DEFAULT_NULL :
                    ColumnTemplateConstant.COLUMN_HAS_DEFAULT_NULL;
        } else {
            result = singleQuotes ? ColumnTemplateConstant.COLUMN_HAS_SINGLEQUOTES_DEFAULT_NOT_NULL :
                    ColumnTemplateConstant.COLUMN_HAS_DEFAULT_NOT_NULL;
        }

        return result;
    }

    /**
     * 匹配类型
     *
     * @param pattern
     * @param content
     * @return
     */
    private static boolean isMatch(String pattern, String content) {
        String upContent = StringUtils.upperCase(content);
        return Pattern.matches(pattern, content);
    }

    /**
     * 替换字符串
     *
     * @param text
     * @param keyValMap
     * @return
     */
    public static String replaceAllKeyVal(String text, Map<String, String> keyValMap) {
        String tempText = text;

        for (Map.Entry<String, String> entry : keyValMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();

            while (true) {
                if (StringUtils.indexOf(tempText, key) == -1) {
                    break;
                } else {
                    tempText = StringUtils.replace(tempText, key, val);
                }
            }
        }

        //logger.info("preStr =  " + text);
        //logger.info("postStr = " + tempText);
        return tempText;
    }

   /* public static String replaceOwnerAndTblName(String text, String owner, String tableName) {

        Map<String, String> keyValMap = CommonCollectionUtils.newHashMapInstance();
        keyValMap.put(ColumnDto.REPLACE_COLUMNNAME, owner);
        keyValMap.put(ColumnDto.REPLACE_COLUMNDESCRIBE, tableName);
        return replaceAllKeyVal(text, keyValMap);
    }*/

    public static void main(String[] args) {
        /*String columnTemp = "comment {owner} on column {owner}.{tableName}.{columnName} is '{columnDescribe}'";
        System.out.println(replaceOwnerAndTblName(columnTemp, "chen", "tbl_user"));*/
    }
}
