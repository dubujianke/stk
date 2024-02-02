package com.mk.tool.stock;

import cn.hutool.core.util.StrUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Function {
    public String name;
    public List<String> params = new ArrayList<>();
    public List<String> paramValues = new ArrayList<>();
    public String body;
    public String bodyValue;

    static Map<String, Function> funMap = new HashMap();
    static {
        funMap.put("space", new Function("a, b, (b-a)/a*100"));
    }

    public static boolean contain(String code) {
        int idx = code.indexOf("(");
        if(idx==-1) {
            return false;
        }
        String name = code.substring(0, idx).trim();
        return funMap.containsKey(name);
    }

    public Function(String code) {
        String items[] = code.split(",");
        String body = items[items.length-1].trim();
        for(int i=0; i<items.length-1; i++) {
            String param = items[i].trim();
            add(param);
            body = body.replaceAll(param, "{"+param+"}");
        }
        setBody(body);
    }

    public Function(String param1, String param2, String body) {
        add(param1);
        add(param2);
        body = body.replaceAll(param1, "{"+param1+"}");
        body = body.replaceAll(param2, "{"+param2+"}");
        setBody(body);
    }

    public Function(List<String> params, String body) {
        this.params = params;
        setBody(body);
    }

    public void add(String param) {
        params.add(param);
    }

    public void setBody(String body) {
        this.body = body;
    }


    public static Function parse(String code) throws Exception {
        List<String> paramValus = getParams(code);
        int idx = code.indexOf("(");
        String name = code.substring(0, idx).trim();
        Function function = funMap.get(name);
        function.paramValues = paramValus;
        //bodyValue

        Map mapParam = new HashMap<>();
        for(int i=0; i<function.params.size(); i++) {
            if(function.params.size() != paramValus.size()) {
                throw  new Exception("function error");
            }
            String paramValue = paramValus.get(i);
            mapParam.put(function.params.get(i), paramValue);
        }
        function.bodyValue = StrUtil.format(function.body, mapParam);
        return function;
    }

    public static List<String> getParams(String code) {
        int idx = code.indexOf("(");
        int idx2 = code.indexOf(")");
        String item = code.substring(idx + 1, idx2);
        String items[] = item.split(",");
        List<String> params = new ArrayList<>();
        for(String i:items) {
            params.add(i.trim());
        }
        return params;
    }
}
