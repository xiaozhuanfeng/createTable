package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "/orc")
public class OrcController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/createTbl")
    private String toOrcCrtPage(HttpServletRequest request, Map<String, Object> map) {

        return "export_orc";
    }

   /* @ResponseBody
    @RequestMapping(value = "/export",method = RequestMethod.POST)
    public ResponseEntity<byte[]>  export(@RequestBody TableDto dto, HttpServletResponse response) throws IOException {
        System.out.println(dto);

        String content = "Hello,Hero";

        HttpHeaders headers = new HttpHeaders();
        String fileName=new String("1111.sql".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //设置文件类型
        headers.add("Content-Disposition", "attchement;filename=" + fileName);

        return new ResponseEntity<byte[]>(content.getBytes(),headers, HttpStatus.CREATED);
    }*/

}
