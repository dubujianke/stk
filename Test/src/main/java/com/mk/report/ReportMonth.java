package com.mk.report;

import com.mk.data.GetAllBankuaiCode;
import com.mk.data.GetBankuaiLine;
import com.mk.model.*;
import com.mk.tool.stock.*;
import com.mk.util.ExcelWrite2007Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportMonth {
    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }
    static Table table = new Table();
    public static void prsIsN(String code, String weekDate, Kline kline, Kline mkline, List<Kline> weeks, List<Kline> moths) {
        boolean flag = false;
        if (weeks != null) {
            int monIdx = kline.getIdx();
            Kline current = weeks.get(kline.getIdx());
            double prevZf = current.getPrevZF(6);
            double ma60 = current.getMA60();
            double temp2 = current.getMAXMA60Frac();
            int atype = 0;
            Kline prevMonth = mkline.prev();
            int trendType10 = prevMonth.getMA10TrendType(10);
            int trendType30 = prevMonth.getMA30TrendType(10);
            int trendType60 = prevMonth.getMA60TrendType(10);
            boolean isFirstWeek = KLineUtil.isFirstWeek(weekDate);
            MaxSection maxSection60 = new MaxSection(30);
            maxSection60.initDay(weeks, 0, monIdx);
            List<MaxPoint> pointsAll = maxSection60.points;
            List<MaxPoint> points = maxSection60.getRangeAllMAX(monIdx - 60, monIdx);
            KLineUtil.sortDescMonthlinePoint(points);

        }
    }

    public static List<String> getDates() {
        List<String> list = new ArrayList<>();
        list.add("");
//        for (int i = 0; i < 5; i++) {
//            for (int j = 1; j <= 12; j++) {
//                String year = "" + (2018 + i);
//                String month = String.format("%02d", j);
//                String date = year + "-" + month;
//                Col col = new Col();
//                list.add(date);
//            }
//        }
        for (int j = 5; j <= 5; j++) {
            String year = "" + (2023);
            String month = String.format("%02d", j);
            String date = year + "-" + month;
            Col col = new Col();
            list.add(date);
        }
        return list;
    }
    public static List<String> getDates2() {
        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            for (int j = 1; j <= 12; j++) {
//                String year = "" + (2018 + i);
//                String month = String.format("%02d", j);
//                String date = year + "/" + month+"/01";
//                Col col = new Col();
//                list.add(date);
//            }
//        }
        for (int j = 5; j <= 5; j++) {
            String year = "" + (2023);
            String month = String.format("%02d", j);
            String date = year + "/" + month+"/01";
            Col col = new Col();
            list.add(date);
        }
        return list;
    }

    public static int getZSLevel(double v) {
        if (v < -10) {
            return 2;
        }
        if (v < -3) {
            return 1;
        }
        return 0;
    }

    public static int getBKLevel2(double v) {
        if (v > 5) {
            return 2;
        }
        if (v > 2) {
            return 1;
        }
        return 0;
    }

    public static int getBKLevel(double v) {
        if (v < -5) {
            return 2;
        }
        if (v < -2 ) {
            return 1;
        }
        return 0;
    }

    public static Col pred(String code, String date) throws IOException {
//        List<Kline> weeks = GetBankuaiLine.read(code, GetBankuaiLine.WEEK, false);
        List<Kline> months = GetBankuaiLine.read(code, GetBankuaiLine.MONTH, false);
//        Kline kline = KLineUtil.getLineByDate(weeks, date);
        Kline mkline = KLineUtil.getMonthLineByDate(months, date);
//        prsIsN(code, date, kline, mkline, weeks, months);
        double f = mkline.getZhangfu();
        int flag = 0;
        if (code.startsWith("1A")) {
            flag = getZSLevel(f);
        } else {
            flag = getBKLevel(f);
        }
//        Log.log(code + " " + date + " ---->" + String.format("%.2f", f) + " " + flag);
        Col col = new Col();
        if(flag == 0) {
            col.data = "";
        }else {
            col.data = "" + flag;
        }

        return col;
    }

    public static void header(Table table) throws IOException {
        Row row = new Row();
        List<String> list = getDates();
        for (int j = 0; j < list.size(); j++) {
            String date = list.get(j);
            Col col = new Col();
            col.data = "" + date;
            row.add(col);
        }
        table.add(row);
    }

    public static Table bk(Table table) throws IOException {
        Map<String, Bankuai> infoMap = AllModel.infoMap;
        for (Bankuai info : infoMap.values()) {
            String code = info.getCode();
            if(code.equalsIgnoreCase("881119")) {
                int a = 0;
            }
            Row row = new Row();
            Col col0 = new Col(code);
            row.add(col0);
            List<String> list = getDates2();
            for (int j = 0; j < list.size(); j++) {
                String date = list.get(j);
                Col col = pred(code, date);
                String zs = table.getCell(1, j+1);
                if(!zs.equals("1")) {
                    col.data = "";
                }else {
                }
                row.add(col);
            }
            table.add(row);
        }
        return table;
    }

    public static void zs(Table table) throws IOException {
        Row row = new Row();
        Col col0 = new Col("1A0001");
        row.add(col0);
        List<String> list = getDates2();
        for (int j = 0; j < list.size(); j++) {
            String date = list.get(j);
            Col col = pred("1A0001", date);
            row.add(col);
        }
        table.add(row);
    }




    public static void main(String[] args) throws IOException {
        GetAllBankuaiCode.read();
        header(table);
        zs(table);
        bk(table);
        try {
            ExcelWrite2007Test.main(table, "test");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
