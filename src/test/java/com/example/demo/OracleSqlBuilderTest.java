package com.example.demo;

import com.example.demo.constant.DefualtValConstant;
import com.example.demo.constant.IndexTemplateConstant;
import com.example.demo.dto.ColumnDto;
import com.example.demo.dto.GrantDto;
import com.example.demo.dto.IndexDto;
import com.example.demo.dto.TableDto;
import com.example.demo.service.OracleSqlBuilder;
import com.example.demo.util.ProjectPathUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
public class OracleSqlBuilderTest {
    @Test
    public void test1() {
        String content = "VARCHAR2(2)";
        String pattern = "^(varchar2\\()([1-9])\\d*\\)$";
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println(isMatch);

        String content1 = "char(1)";
        String pattern1 = "^(CHAR\\()([1-9])\\d*\\)$";
        boolean isMatch1 = Pattern.matches(pattern1, content1);
        System.out.println(isMatch1);
    }

    @Test
    public void test2() {
        String content = "1234567";
        System.out.println(StringUtils.substring(content, 0, 4));
    }

    @Test
    public void crt() {
        TableDto tabDto = new TableDto();
        tabDto.setOwner("CHENJIANLONG");
        tabDto.setTableName("L_USER_INFO_TBL");
        tabDto.setTableDescribe("用户测试表");
        tabDto.setColumnList(getColumnList());
        tabDto.setAutoFlag(TableDto.AUTOFLAG_YES);
        String sql = OracleSqlBuilder.buildCrtSql(tabDto);

        System.out.println(sql);
    }

    @Test
    public void buildInxSql() {
        List<IndexDto> inxList = getIndexDtoList();
        String sql = OracleSqlBuilder.buildInxSql(inxList);
        System.out.println(sql);
    }

    @Test
    public void buildSynonymSql() {
        TableDto tabDto = new TableDto();
        tabDto.setOwner("CHENJIANLONG");
        tabDto.setTableName("L_USER_INFO_TBL");
        tabDto.setTableDescribe("用户测试表");
        tabDto.setColumnList(getColumnList());
        tabDto.setAutoFlag(TableDto.AUTOFLAG_YES);

        String sql = OracleSqlBuilder.buildSynonymSql(tabDto);
        System.out.println(sql);
    }

    @Test
    public void buildGrtSql() {
        List<GrantDto> grantDtoList = getGrtDtoList();
        String sql = OracleSqlBuilder.buildGrtSql(grantDtoList);
        System.out.println(sql);
    }

    @Test
    public void buildTriBiSql() {
        TableDto tabDto = new TableDto();
        tabDto.setOwner("CHENJIANLONG");
        tabDto.setTableName("L_USER_INFO_TBL");
        tabDto.setTableDescribe("用户测试表");
        tabDto.setColumnList(getColumnList());
        tabDto.setAutoFlag(TableDto.AUTOFLAG_YES);

        String sql = OracleSqlBuilder.buildBiTriSql(tabDto);
        System.out.println(sql);
    }

    @Test
    public void buildTriBuSql() {
        TableDto tabDto = new TableDto();
        tabDto.setOwner("CHENJIANLONG");
        tabDto.setTableName("L_USER_INFO_TBLCxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        tabDto.setTableDescribe("用户测试表");
        tabDto.setColumnList(getColumnList());
        tabDto.setAutoFlag(TableDto.AUTOFLAG_YES);

        String sql = OracleSqlBuilder.buildBuTriSql(tabDto);
        System.out.println(sql);
    }

    private List<GrantDto> getGrtDtoList() {
        List<GrantDto> grtList = new ArrayList<GrantDto>();
        GrantDto dto1 = new GrantDto();
        dto1.setOwner("CHENJIANLONG");
        dto1.setTableName("L_USER_INFO_TBL");
        dto1.setOperateArr("select");
        dto1.setRoleName("CBITSOPR");

        GrantDto dto2 = new GrantDto();
        dto2.setOwner("CHENJIANLONG");
        dto2.setTableName("L_USER_INFO_TBL");
        dto2.setOperateArr("select,update,insert,delete");
        dto2.setRoleName("CBITSDATA");

        GrantDto dto3 = new GrantDto();
        dto3.setOwner("CHENJIANLONG");
        dto3.setTableName("L_USER_INFO_TBL");
        dto3.setOperateArr("select,update,insert,delete");
        dto3.setRoleName("PHSSPOPR");

        grtList.add(dto1);
        grtList.add(dto2);
        grtList.add(dto3);
        return grtList;
    }

    @Test
    public void getTempLateFilePath() {
        String filePath = ProjectPathUtils.getRootPath("");

        int index = filePath.indexOf("target");
        System.out.println(filePath);
        System.out.println(index);
        System.out.println(StringUtils.substring(filePath, 0, index));
    }

    private List<IndexDto> getIndexDtoList() {
        List<IndexDto> inxList = new ArrayList<IndexDto>();

        IndexDto dto1 = new IndexDto();
        dto1.setOwner("CHENJIANLONG");
        dto1.setTableName("L_USER_INFO_TBL");
        dto1.setIndexType(IndexTemplateConstant.INDEXTYPE_PK);
        dto1.setInxColumnNames("sno");

        IndexDto dto2 = new IndexDto();
        dto2.setOwner("CHENJIANLONG");
        dto2.setTableName("L_USER_INFO_TBL");
        dto2.setIndexType(IndexTemplateConstant.INDEXTYPE_COMMON);
        dto2.setInxColumnNames("sname,sage");

        IndexDto dto3 = new IndexDto();
        dto3.setOwner("CHENJIANLONG");
        dto3.setTableName("L_USER_INFO_TBL");
        dto3.setIndexType(IndexTemplateConstant.INDEXTYPE_UQ);
        dto3.setInxColumnNames("semail,smobileddddddddddddddddddddddddddddddddddd");

        inxList.add(dto1);
        inxList.add(dto2);
        inxList.add(dto3);

        return inxList;
    }

    private List<ColumnDto> getColumnList() {
        List<ColumnDto> columnList = new ArrayList<ColumnDto>();

        ColumnDto colDto1 = new ColumnDto();
        colDto1.setColumnName("sno");
        colDto1.setColumnType("VARCHAR2(36)");
        colDto1.setDefaultVal(DefualtValConstant.SYS_GUID);
        colDto1.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_NO);
        colDto1.setColumnDescribe("学生号");

        ColumnDto colDto2 = new ColumnDto();
        colDto2.setColumnName("sname");
        colDto2.setColumnType("VARCHAR2(30)");
        colDto2.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_NO);
        colDto2.setColumnDescribe("姓名");

        ColumnDto colDto3 = new ColumnDto();
        colDto3.setColumnName("sage");
        colDto3.setColumnType("NUMBER(2)");
        colDto3.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_NO);
        colDto3.setColumnDescribe("年龄");

        ColumnDto colDto4 = new ColumnDto();
        colDto4.setColumnName("sgender");
        colDto4.setColumnType("VARCHAR2(10)");
        colDto4.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_NO);
        colDto4.setColumnDescribe("性别");

        ColumnDto colDto5 = new ColumnDto();
        colDto5.setColumnName("sclassno");
        colDto5.setColumnType("VARCHAR2(10)");
        colDto5.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_NO);
        colDto5.setColumnDescribe("班级");

        ColumnDto colDto6 = new ColumnDto();
        colDto6.setColumnName("smobile");
        colDto6.setColumnType("VARCHAR2(11)");
        colDto6.setDefaultVal("18795462855");
        colDto6.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_YES);
        colDto6.setColumnDescribe("手机号");

        ColumnDto colDto7 = new ColumnDto();
        colDto7.setColumnName("semail");
        colDto7.setColumnType("VARCHAR2(100)");
        colDto7.setColumnNullFlag(ColumnDto.COLUMNNULLFLAG_YES);
        colDto7.setColumnDescribe("邮箱");

        columnList.add(colDto1);
        columnList.add(colDto2);
        columnList.add(colDto3);
        columnList.add(colDto4);
        columnList.add(colDto5);
        columnList.add(colDto6);
        columnList.add(colDto7);
        return columnList;
    }


}
