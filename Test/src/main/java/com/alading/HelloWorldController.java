package com.alading;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public class HelloWorldController {
    @RequestMapping("/hello")
    public String showMsg(MultipartFile file, HttpServletRequest request){
        return "123";
    }
}
