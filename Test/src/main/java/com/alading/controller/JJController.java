package com.alading.controller;

import com.alading.report.JJResults;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;


@Controller
public class JJController {
    @ResponseBody
    @RequestMapping("/hello3")
    public String hello(MultipartFile file, String OSSAccessKeyId, HttpServletRequest request) {
        try {
            String type = request.getHeader("Content-Type");
            InputStream inputStream = file.getInputStream();
            OutputStream outputStream = new FileOutputStream("d:/test/output1111.jpg");
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
            System.out.println("Data has been written to output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String msg = JJResults.getInfo();
        return "ret: " + msg;
    }
}
