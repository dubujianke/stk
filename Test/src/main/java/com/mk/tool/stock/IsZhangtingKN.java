//package com.mk.tool.stock;
//
//import com.mk.report.JJResult;
//import com.mk.report.JJResults;
//
//import java.util.List;
//
///**
// * 大底
// */
//public class IsZhangtingKN extends Stragety {
//
//
//    public static void ok(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg) {
//        Log.log("OK:" + INFO + " " + msg);
//        JJResult jjResult = new JJResult();
//        jjResult.setMsg("" + INFO + " " + msg);
//        JJResults.add(jjResult);
//    }
//
//    public static String getSpeedUp(Kline kline, MinuteLine minuteLine) {
//        float open = kline.prev().getClose();
//        try {
//            float prev3 = KLineUtil.compareMaxSign(minuteLine.prev(3).price, open);
//            float prev2 = KLineUtil.compareMaxSign(minuteLine.prev(2).price, open);
//            float prev1 = KLineUtil.compareMaxSign(minuteLine.prev(1).price, open);
//            float cur = KLineUtil.compareMaxSign(minuteLine.price, open);
//
//            return kline.getDate() + "      " + minuteLine.getTime() + " " +
//                    minuteLine.prev(3).getVol() + "/- " +
//                    minuteLine.prev(2).getVol() + "/" + format(prev2 - prev3) + " " +
//                    minuteLine.prev(1).getVol() + "/" + format(prev1 - prev2) + " " +
//                    minuteLine.getVol() + "/" + format(cur - prev1) + " totalB:" + ((prev2 - prev3) + (prev1 - prev2) + (cur - prev1)) + "   fraction:" + format(cur - prev3) + "  ";
//        }catch (Exception e){
//
//        }
//        return "";
//    }
//
//    public static String getSpeedUpFrac(Kline kline, MinuteLine minuteLine) {
//        float open = kline.prev().getClose();
//        float prev3 = KLineUtil.compareMaxSign(minuteLine.prev(3).price, open);
//        float cur = KLineUtil.compareMaxSign(minuteLine.price, open);
//
//        return format(cur-prev3);
//    }
//
//
//
//
//    public static String format(float v) {
//        return String.format("%.2f", v);
//    }
//
//    public static void filterZT1(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, Kline kline, LineContext context) {
//    }
//    public static void filter(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, Kline kline, LineContext context) {
//        IsBottomUtil.NUM = 60;
//        boolean flag = kline.isGuailiChange();
////        Log.log(file);
//        //right angle
//        int len = 1;
//        for (int i = 0; i < len; i++) {
//            TypeResult typeResult = realRightAngleFilter(file, kline.prev(i), context);
//            if (StragetyZTBottom.get(file) != null) {
//
//            } else {
//                if (typeResult.size() > 0) {
//                    StragetyZTBottom.add(file, typeResult);
//                    Log.log(typeResult.getInfo());
//                }
//            }
//        }
//
//        MinuteLine minuteLine = realTimeFilter(file, days, date, weeks, moths, usemonth, useweek, kline, context);
//        if (minuteLine != null) {
//            if(AbsStragety.isMonitor) {
//                if (StragetyZTKN.get(file) != null) {
//                } else {
//                    StragetyZTKN.add(file, minuteLine);
//                    String info = getCode(file);
//                    ok(file, INFO, date, kline, null, kline, "" + minuteLine.getTime() + "  " + getSpeedUp(kline, minuteLine));
//                }
//            }else {
//                ok(file, INFO, date, kline, null, kline, "" + minuteLine.getTime() + "  " + " totalE:" + minuteLine.getMsg());
//            }
//
//            for (int i = 0; i < 6; i++) {
////                printDaysMinutes(file, kline.prev(i+1), context);
//            }
//            int alen = 11;
//            for (int i = 0; i < alen; i++) {
////                printDaysTringleMinutes(file, kline.prev(i), context);
//            }
////            Log.log("-----------------------------------------------------------------\r\n");
//        }
//    }
//
//    public static MinuteLine realTimeFilter(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, Kline kline, LineContext context) {
//        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
//        StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(date);
////        Log.log("--->"+kline.getDate());
////        stockDayMinuteLine.log();
////        stockDayMinuteLine.logTringle();//just log
//
////        LineType lineType = stockDayMinuteLine.getLineType("0933");
////        double theata = lineType.caculate();
////        double lenRet = lineType.caculateLength();
////        Log.log("lenRets:"+theata+" "+String.format("%.2f", lenRet));
//
//        MinuteLine minuteLine = StockAllMinuteLine.getFirstSpeedUpKN(kline, stockAllMinuteLine, getCode(file), date, context);
//        if (minuteLine != null) {
//            return minuteLine;
//        }
//        return null;
//    }
//
//    public static Grid printDaysMinutesTJ(String file, Kline kline, LineContext context) {
//        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
//        MinuteLine minuteLine = StockAllMinuteLine.getFirstSpeedUp(kline, stockAllMinuteLine, getCode(file), kline.getDate(), context);
//        StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(kline.getDate());
//        Grid grid = stockDayMinuteLine.logVOLTJ(kline);
//        return grid;
//    }
//
//    public static boolean isDaysMinutesTJ(String file, Kline kline, LineContext context) {
//        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
//        MinuteLine minuteLine = StockAllMinuteLine.getFirstSpeedUp(kline, stockAllMinuteLine, getCode(file), kline.getDate(), context);
//        StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(kline.getDate());
//        boolean flag = stockDayMinuteLine.isPrsLines(kline);
//        return flag;
//    }
//
//    public static String printDaysMinutes(String file, Kline kline, LineContext context) {
//        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
//        MinuteLine minuteLine = StockAllMinuteLine.getFirstSpeedUp(kline, stockAllMinuteLine, getCode(file), kline.getDate(), context);
//        if (minuteLine != null) {
//            Log.log(getSpeedUp(kline, minuteLine));
//        } else {
////            Log.log(""+kline.getDate()+" null");
//        }
//        return null;
//    }
//
//    public static String printDaysTringleMinutes(String file, Kline kline, LineContext context) {
//        StockDayMinuteLine stockDayMinuteLine = kline.getStockDayMinuteLine();
//        if (stockDayMinuteLine != null) {
//            stockDayMinuteLine.logRightAngle();
//        }
//        return null;
//    }
//
//    public static TypeResult realRightAngleFilter(String file, Kline kline, LineContext context) {
//        StockDayMinuteLine stockDayMinuteLine = kline.getStockDayMinuteLine();
//        TypeResult typeResult = null;
//        if (stockDayMinuteLine != null) {
//            typeResult = stockDayMinuteLine.filterRightAngle();
//        }
//        return typeResult;
//    }
//
//
//    public void prs(String file, List<Kline> days, String date, String stragetyName, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
//        //dynamic
//        prsIsN(file, days, date, weeks, moths, usemonth, useweek, context);
//    }
//
//    public void prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
//        int NUM = 4;
//        int idx = getIdx(days, date);
//        if (!AbsStragety.isMonitor) {
//            if (idx < 141) {
//                return;
//            }
//        }
//        Kline kline0 = days.get(idx);
//        if (Stragety.isMonitor) {
//            filter(file, days, date, weeks, moths, usemonth, useweek, kline0, context);
//        }
//        else {
//            if (!Stragety.isResult) {
//                Result result = new Result(file, date);
//                result.stragety = IsZhangtingKN.class.getSimpleName();
//                StragetyZTBottom.addLine(result);
//            } else {
//                filter(file, days, date, weeks, moths, usemonth, useweek, kline0, context);
//            }
//        }
//    }
//
//    static String getString(String nane, Object v) {
//        return " " + nane + " " + v;
//    }
//
//}
