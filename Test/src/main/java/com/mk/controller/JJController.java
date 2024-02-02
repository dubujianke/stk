package com.mk.controller;

import com.mk.report.JJResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class JJController {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        String msg  = JJResults.getInfo();
        return "ret: "+msg;
    }
}
