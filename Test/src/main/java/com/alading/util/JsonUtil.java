package com.alading.util;

import com.alibaba.fastjson.JSONArray;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    public static List getList(JSONArray array) {
        List list = new ArrayList();
        for(int i=0; i<array.size(); i++) {
            list.add(array.get(i));
        }
        return list;
    }

    public static void main(String[] args) {
        String txt = FileUtil.read("res/1.html");
        String templateString = txt;
        Velocity.init();
        VelocityContext ctx = new VelocityContext();
        ctx.put("email", "nguyen@vietnam.com");
        StringWriter out = new StringWriter();
        Velocity.evaluate(ctx, out, "test", templateString);
        //System.out.println(out.toString());
    }

}
