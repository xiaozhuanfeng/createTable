package com.example.demo.controller;

import com.example.demo.constant.FileConstant;
import com.example.demo.dto.TableDto;
import com.example.demo.service.OracleSqlBuilder;
import com.example.demo.service.TestService;
import com.example.demo.util.CommonCollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/export")
public class ExportController {

    private static final Logger logger = LoggerFactory.getLogger(ExportController.class);

    @Resource
    private TestService testService;

    @ResponseBody
    @RequestMapping(value = "/orcSql", method = RequestMethod.POST)
    public Map<String, Object> export(@RequestBody TableDto dto, HttpServletResponse response) throws IOException {

        logger.info("表数据>>>"+dto);

        System.out.println(dto);
        Map<String, Object> resultMap = CommonCollectionUtils.newHashMapInstance();
        resultMap.put("result", "1");
        resultMap.put("body", getBodyList(dto));
        return resultMap;
    }

    private String getFileName(TableDto dto, String fileTemplate) {
        Map<String, String> keyValMap = CommonCollectionUtils.newHashMapInstance();
        keyValMap.put(TableDto.REPLACE_OWNER, dto.getOwner());
        keyValMap.put(TableDto.REPLACE_TABLENAME, dto.getTableName());
        return OracleSqlBuilder.replaceAllKeyVal(fileTemplate, keyValMap);
    }

    private List<Map<String, String>> getBodyList(TableDto dto) {
        List<Map<String, String>> bodyList = new ArrayList<Map<String, String>>();
        //生成crt文件
        Map<String, String> fileMap1 = CommonCollectionUtils.newHashMapInstance();
        fileMap1.put("fileName", getFileName(dto, FileConstant.CRT_FILENAME));
        String crtSql = OracleSqlBuilder.buildCrtSql(dto);
        fileMap1.put("content", testService.exportExcelFileBase64Str(crtSql));

        //生成inx文件
        Map<String, String> fileMap2 = CommonCollectionUtils.newHashMapInstance();
        fileMap2.put("fileName", getFileName(dto, FileConstant.IDX_FILENAME));
        String inxSql = OracleSqlBuilder.buildInxSql(dto.getInxList());
        fileMap2.put("content", testService.exportExcelFileBase64Str(inxSql));

        //生成syn文件
        Map<String, String> fileMap3 = CommonCollectionUtils.newHashMapInstance();
        fileMap3.put("fileName", getFileName(dto, FileConstant.SYN_FILENAME));
        String synSql = OracleSqlBuilder.buildSynonymSql(dto);
        fileMap3.put("content", testService.exportExcelFileBase64Str(synSql));

        //生成grt文件
        Map<String, String> fileMap4 = CommonCollectionUtils.newHashMapInstance();
        fileMap4.put("fileName", getFileName(dto, FileConstant.GRT_FILENAME));
        String grtSql = OracleSqlBuilder.buildGrtSql(dto.getGrtList());
        fileMap4.put("content", testService.exportExcelFileBase64Str(grtSql));

        bodyList.add(fileMap1);
        bodyList.add(fileMap2);
        bodyList.add(fileMap3);
        bodyList.add(fileMap4);

        if (StringUtils.equalsIgnoreCase(dto.getAutoFlag(), TableDto.AUTOFLAG_YES)) {
            //生成tri文件
            Map<String, String> fileMap5 = CommonCollectionUtils.newHashMapInstance();
            fileMap5.put("fileName", getFileName(dto, FileConstant.TRI_FILENAME_BI));
            String triBiSql = OracleSqlBuilder.buildBiTriSql(dto);
            fileMap5.put("content", testService.exportExcelFileBase64Str(triBiSql));

            Map<String, String> fileMap6 = CommonCollectionUtils.newHashMapInstance();
            fileMap6.put("fileName", getFileName(dto, FileConstant.TRI_FILENAME_BU));
            String triBuSql = OracleSqlBuilder.buildBuTriSql(dto);
            fileMap6.put("content", testService.exportExcelFileBase64Str(triBuSql));

            bodyList.add(fileMap5);
            bodyList.add(fileMap6);
        }

        return bodyList;
    }

    @PostMapping(value = "/ajaxExportFileTest")
    @ResponseBody
    public String ajaxExportFileTest(HttpServletResponse response) {
        response.setStatus(HttpStatus.OK.value());
        return testService.exportExcelFileBase64Str("Hello,Heoo!");
    }
}
