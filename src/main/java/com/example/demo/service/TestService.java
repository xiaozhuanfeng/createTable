package com.example.demo.service;

import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class TestService {
    /**
     * 根据byte 数组获取BASE64字符串
     * @param bytes
     * @return
     */
    public String getFileBase64Str(byte [] bytes){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encodeBuffer(bytes);
    }

    /**
     *
     * @return
     */
    public  String exportExcelFileBase64Str(String content){
        //创建输出流
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            output.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getFileBase64Str(output.toByteArray());
    }
}
