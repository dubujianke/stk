package com.alading.report;

import com.alading.util.FileUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class LineReports {
    private static List<LineReport> list = new ArrayList<>();

    public static void add(LineReport report) {
        list.add(report);
    }

    public static List<String> getCategory() {
        LineReport lineReport = list.get(0);
        return lineReport.getCategory();
    }

    public static void report() {
        try {
            int len = list.size();
            String txt = FileUtil.read("res/report/1.html");
            String templateString = txt;
            Velocity.init();
            VelocityContext ctx = new VelocityContext();
            ctx.put("list", list);
            ctx.put("categoryes", getCategory());
            StringWriter out = new StringWriter();
            Velocity.evaluate(ctx, out, "test", templateString);
//        //System.out.println(out.toString());
            FileUtil.write("./res/report/reportLines.html", out.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
