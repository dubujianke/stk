//package com.mk.tool.stock;
//
//import com.mk.data.eastmoney.GetGuben;
//import com.mk.report.LineReport;
//import com.mk.report.LineReports;
//
//import java.util.List;
//
///**
// * 大底
// */
//public class IsJustZT extends Stragety {
//
//
//    public static void ok(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg) {
//        Log.log("mainProcess(\"" + file + "\", \"" + kline0.date + "\", \"IsZhangting\", 1, 1, context);");
//    }
//
//    public static void filter(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, Kline kline, LineContext context) {
//        IsBottomUtil.NUM = 60;
//        boolean flag = kline.isGuailiChange();
//        ok(file, INFO, date, kline, null, kline, "");
//    }
//
//
//    public static void report(String file, Kline kline, LineContext context, boolean realBottomFlag) {
//        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
//        LineReport lineReport = new LineReport();
//        lineReport.setCode(INFO);
//        lineReport.setFile(file);
//        lineReport.setStockAllMinuteLine(stockAllMinuteLine);
//        lineReport.setBottomFlag(realBottomFlag ? 2 : 0);
//        for (int i = 0; i < 15; i++) {
//            Kline item = kline.prev(i);
//            Grid grid = IsZhangting.printDaysMinutesTJ(file, item, context);
//            lineReport.add(grid);
//        }
//        lineReport.getReportCross();
//        lineReport.getSppedUpWithVOL(context);
//        if (lineReport.isDeadCross() == 0) {
//            LineReports.add(lineReport);
//        }
//    }
//
//    public void prs(String file, List<Kline> days, String date, String stragetyName, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
//        try {
//            prsIsN(file, days, date, weeks, moths, usemonth, useweek, context);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
//        double v = GetGuben.retriveOrGet(AbsStragety.getCode(file));
//        if (v > 80) {
//            return;
//        }
//        int idx = getIdx(days, date);
//        if (idx < 141) {
//            return;
//        }
//        Kline kline0 = days.get(idx);
////        if (kline0.hasDazhang(15, 5)) {
////            double zf = kline0.getMinPrevEntityZhenFSelf(4, 0);
////            if (zf > 2) {
////                return;
////            }
////        }
//
////        if (kline0.hasDadie(10, 2.98f)) {
////            return;
////        }
//        if (!kline0.isZhanging()) {
//            return;
//        }
//        //////////////////////////////////////////////////
//
//        IsBottomUtil.NUM = 130;
////        boolean isBottom = IsBottomWeekUtil.prsIsN(file, days, date, weeks, moths, usemonth, useweek);
////        if (!isBottom) {
////            return;
////        }
//
//        if (file.contains("600322") && date.equalsIgnoreCase("2023/07/20")) {
//            int a = 0;
//        }
//        filter(file, days, date, weeks, moths, usemonth, useweek, kline0, context);
//
////        GlobalContext.map.put(file, null);
//    }
//
//    static String getString(String nane, Object v) {
//        return " " + nane + " " + v;
//    }
//
//
//}
