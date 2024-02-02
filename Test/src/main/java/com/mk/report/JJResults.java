package com.mk.report;

import com.mk.util.FileUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class JJResults {
    static List<JJResult> list = new ArrayList<>();

    public static void add(JJResult report) {
        list.add(report);
    }

    public static String getInfo() {
        if(list.size()==0) {
            return "";
        }
        return list.get(list.size()-1).getInfo();
    }

    public static void cleaar() {
        list.clear();
    }

}
