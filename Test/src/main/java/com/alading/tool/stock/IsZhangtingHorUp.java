package com.alading.tool.stock;

import com.alading.data.GetBankuai;
import com.alading.data.eastmoney.GetGuben;
import com.alading.data.eastmoney.GetLInes;
import com.alading.model.*;
import com.alading.msg.MsgSend;
import com.alading.report.LineReport;
import com.alading.tool.stock.cyq.CYQCalculator;
import com.alading.tool.stock.cyq.Cmf;
import com.alading.tool.stock.decision.DecisionZT;
import com.alading.tool.stock.model.PeriodPressure;
import com.alading.tool.stock.tool.SelectAddColExcel;
import com.alading.util.StringUtil;
import com.huaien.core.util.DateUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大底
 */
public class IsZhangtingHorUp extends Stragety {
    public static double minZF = 0.9f;


    public static double getMtZF(double open, MinuteLine minuteLine) {
        try {
            if (minuteLine.getTime().equalsIgnoreCase("0930")) {
                double minPrice = KLineUtil.compareMaxSign(minuteLine.price, open);
                return minPrice;
            }
            double prev3 = 0;
            String p3Str = "";
            String p3 = "";
            String p2 = "";
            String p1 = "";
            String p0 = "";
            String mZf = "";
            MinuteLine minMinuteLine = minuteLine.getMin();
            double minPrice = KLineUtil.compareMaxSign(minMinuteLine.price, open);
            String minuteLineStr = "" + minPrice;
            MinuteLine mPrev3 = minuteLine.prev(3);
            MinuteLine mPrev2 = minuteLine.prev(2);
            MinuteLine mPrev1 = minuteLine.prev(1);
            if (mPrev3 != null) {
                prev3 = KLineUtil.compareMaxSign(mPrev3.price, open);
                p3Str = minuteLine.prev(3).getVolStr() + " ";
            }
            double prev2 = KLineUtil.compareMaxSign(mPrev2.price, open);
            double prev1 = KLineUtil.compareMaxSign(mPrev1.price, open);
            double cur = KLineUtil.compareMaxSign(minuteLine.price, open);
            p3 = p3Str;
            p2 = minuteLine.prev(2).getVolStr() + "/" + format(prev2 - prev3);
            p1 = minuteLine.prev(1).getVolStr() + "/" + format(prev1 - prev2);
            p0 = minuteLine.getVolStr() + "/" + format(cur - prev1);
            mZf = format(cur - prev3);
            return Double.parseDouble(mZf);
        } catch (Exception e) {
        }
        return 0;
    }


    public static double getFloat(LineContext context, String name) {
        try {
            double v = context.getkModel().getRow().getFloat(name);
            return v;
        } catch (Exception e) {

        }
        return -99;
    }


    public static void sendMsg(Row row, ZTReason reason) {
        try {
            MsgSend.sendMsg(row, reason);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void ff(Row row, String yueyali) {
        {
            if (StringUtil.isNull(yueyali)) {
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
            } else {
                Map<String, PeriodPressure> yalis = new HashMap<>();
                yalis.put("30", new PeriodPressure(SelectAddColExcel.defalutValue));
                yalis.put("60", new PeriodPressure(SelectAddColExcel.defalutValue));
                yalis.put("120", new PeriodPressure(SelectAddColExcel.defalutValue));
                yalis.put("250", new PeriodPressure(SelectAddColExcel.defalutValue));
                String yl[] = yueyali.split(",");
                for (String v : yl) {
                    String[] vs = v.trim().replace("(", "").replace(")", "").split("\\s+");
                    int period = Integer.parseInt(vs[0]);
                    double vv = Double.parseDouble(vs[1]);
                    yalis.put("" + period, new PeriodPressure("" + vv));
                }
                row.add(new Col(yalis.get("30").value));
                row.add(new Col(yalis.get("60").value));
                row.add(new Col(yalis.get("120").value));
                row.add(new Col(yalis.get("250").value));
            }
        }
//        row.add(new Col(yueyali));
    }

    static int IIDX;

    public static int filterRowSpecialHorAfter(Kline kline0) {
        boolean flag1 = kline0.existHor(6);
        double f1 = kline0.getSpace250();
        if (f1 > 0 && f1 < 2) {
            flag1 = true;
        }
        boolean horFrac = kline0.allIsLessAbsThanExcept(5, 0, 1.0f, 0, 0.0, 0, 0.0, 1.5f);
        if (horFrac) {
            return 1;
        }

        boolean horFrac2 = kline0.allIsLessAbsThanExcept(8, 3, 1.0f, 1, 2.0f, 1, 2.7f, 3.0f);
        if (horFrac2) {
            return 1;
        }

        boolean horFrac3 = kline0.allIsLessAbsThanExcept(10, 4, 2.5f, 2, 2.0f, 1, 2.7f, 3.0f);
        if (horFrac3 && kline0.weekline.prev().getZhenfu() < 3 && kline0.weekline.prev().getZhangfu() < 1) {
            return 1;
        }

        if (kline0.weekline.prev().getZhangfu() < 1 && kline0.weekline.prev().getZhangfu() < 2.5) {
            return 1;
        }

        double m1 = kline0.monthKline.getZhangfu();
        double z1 = kline0.monthKline.getZhenfu();

        double m2 = kline0.monthKline.prev().getZhangfu();
        double z2 = kline0.monthKline.prev().getZhenfu();
        if (m1 > 7 || m2 > 7) {
            return 0;
        }

        if (z1 < 2 || z2 < 2) {
            return 1;
        }
        if (m1 < 3 || m2 < 3) {
            return 1;
        }
        return 0;
    }


    public static void ok(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, ZTReason reason, Weekline weekline, Kline nextN, String msg, LineContext context) {
        SingleContext.add(file);
        if (INFO.length() == 11) {
            INFO = INFO + "  ";
        }

        Row aRow = context.getkModel().getRow();
        aRow.getTable().initIndex();
        int rgt = (int) aRow.getInt("股本");
        if (rgt > 150) {
            return;
        }
        double open = kline0.prev().getClose();
        double mtzf = IsZhangtingHorUp.getMtZF(open, minuteLine);
        boolean ret = true;
        double firstMinuteZF = minuteLine.allLineList.get(0).getZF(kline0.prev());
        if (firstMinuteZF <= -0.8f) {
            ret = false;
        }

        double zf = minuteLine.getZF(kline0.prev());
        if (zf < StragetyZTBottom.pulsezf) {
            ret = false;
        }
        if (!minuteLine.hasMax(kline0.prev(), StragetyZTBottom.maxzf)) {
            ret = false;
        }
        if (!ret) {
            return;
        }


        if (ret) {
            if (mtzf > minZF) {
                Row row = IsZhangting.getRowZT(file, INFO, date, kline0.prev(), minuteLine, context);
                boolean fflag = IsBottom.catchFlag(IsZhangting.table, row);
                if (!fflag) {

                    if (StragetyZTBottom.useTree) {
                        Table table = getTable(file, INFO, date, kline0.prev(), minuteLine, context);
                        table.initIndex();
                        boolean ztFlag = DecisionZT.judge(table, 1);
                        if (!ztFlag) {
                            return;
                        }
                    }
                    sendMsg(row, reason);
                    IsZhangting.table.add(row);
                    Log.logString(StragetyBottom.resultBuffer, IsBottom.getINFO(INFO) + " " + date + " " + minuteLine.getTime());
                }

            } else if (mtzf > 0.75 && zf > 2) {
                Row row = IsZhangting.getRowZT(file, INFO, date, kline0.prev(), minuteLine, context);
                boolean fflag = IsBottom.catchFlag(IsZhangting.table, row);
                if (!fflag) {
                    if (StragetyZTBottom.useTree) {
                        Table table = getTable(file, INFO, date, kline0.prev(), minuteLine, context);
                        table.initIndex();
                        boolean ztFlag = DecisionZT.judge(table, 1);
                        if (!ztFlag) {
                            return;
                        }
                    }
                    sendMsg(row, reason);
                    IsZhangting.table.add(row);
                    Log.logString(StragetyBottom.resultBuffer, IsBottom.getINFO(INFO) + " " + date + " " + minuteLine.getTime());
                }
            }
        }
    }

    public static Table getTable(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, LineContext context) {
        Row row = IsZhangting.getRowZT(file, INFO, date, kline0, minuteLine, context);
        Table table = new Table();
        table.add(StragetyZTBottom.headerRow);
        table.add(row);
        return table;
    }


    public static boolean filterZF(Kline kline, MinuteLine minuteLine) {
        if (minuteLine.prev(3) == null) {
            return true;
        }
        double open = kline.prev().getClose();
        double prev3 = KLineUtil.compareMaxSign(minuteLine.prev(3).price, open);
        double cur = KLineUtil.compareMaxSign(minuteLine.price, open);
        if (cur - prev3 > StragetyZTBottom.MIN_3MINUTES_ZF) {
            return true;
        }
        boolean flag1 = cur - prev3 > StragetyZTBottom.MIN_3MINUTES_ZF2;
        boolean flag2 = cur > StragetyZTBottom.MIN_ZF2;
        if (flag1 && flag2) {
            return true;
        }
        return false;
    }

    public static String getSpeedUp(Kline kline, MinuteLine minuteLine) {
        try {
            double open = kline.prev().getClose();
            double prev3 = 0;
            String p3Str = "-";
            if (minuteLine.prev(3) != null) {
                prev3 = KLineUtil.compareMaxSign(minuteLine.prev(3).price, open);
                p3Str = minuteLine.prev(3).getVolStr() + " ";
            }
            double prev2 = KLineUtil.compareMaxSign(minuteLine.prev(2).price, open);
            double prev1 = KLineUtil.compareMaxSign(minuteLine.prev(1).price, open);
            double cur = KLineUtil.compareMaxSign(minuteLine.price, open);


            return kline.getDate() + " " + minuteLine.getTime() + " " +
                    "p3:" + p3Str + " " +
                    "p2:" + minuteLine.prev(2).getVolStr() + "/" + format(prev2 - prev3) + " " +
                    "p1:" + minuteLine.prev(1).getVolStr() + "/" + format(prev1 - prev2) + " " +
                    "p0:" + minuteLine.getVolStr() + "/" + format(cur - prev1)
                    + " zf:" + format(cur - prev3) + "  ";
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "";
    }


    public static String format(double v) {
        return String.format("%.2f", v);
    }

    public static String getKey(String file, String date) {
        return file + "_" + date;
    }


    public static void filter(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, Kline kline, LineContext context) {
        IsBottomUtil.NUM = 60;
//        Log.log(file);
        int len = 1;
        String key = getKey(file, date);
        for (int i = 0; i < len; i++) {
            StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
            StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(date);
            TypeResult typeResult = realRightAngleFilter(file, kline.prev(i), stockDayMinuteLine, context);
            if (StragetyZTBottom.get(key) != null) {
                int a = 0;
            } else {
                if (typeResult.size() > 0) {
                    MinuteLine minuteLine = typeResult.lineTypeList.get(0).allLineList.get(0);
                    context.setLastMinuteLine(minuteLine);
                    typeResult.setMinuteLine(minuteLine);
                    StragetyZTBottom.add(key, typeResult);
                    try {
                        ZTReason reason = new ZTReason();
                        reason.reason = "90angle";
                        reason.minuteLine = minuteLine;
                        ok(file, context.getInfo(), date, kline, minuteLine, reason, null, kline, getSpeedUp(kline, minuteLine) + " isStandMA250:" + kline.isStandMA250() + " vol:" + context.getLastMinuteLine().getVolStr(), context);
                    } catch (Exception e) {
                        com.alading.util.Log.log("ERR:" + context.getInfo());
                    }
                }
            }
        }

        ZTReason ztReason = realTimeFilter(file, date, kline, context);
        if (ztReason != null && ztReason.minuteLine != null) {
            MinuteLine minuteLine = ztReason.minuteLine;
            double retFrac = KLineUtil.compareMaxSign(minuteLine.price, kline.prev().close);
            if (AbsStragety.isMonitor) {
                Object keyObj = StragetyZTBottom.get(key);
                boolean needReplace = true;
                if (keyObj != null) {
                    if (keyObj instanceof TypeResult) {
                        MinuteLine minuteLine1 = ((TypeResult) keyObj).getMinuteLine();
                        String time1 = minuteLine1.getTime();
                        String timeCur = minuteLine.getTime();
                        if (Integer.parseInt(timeCur) > Integer.parseInt(time1)) {
                            needReplace = false;
                        }
                    }
                }
                if (needReplace) {
                    context.setLastMinuteLine(minuteLine);
                    StragetyZTBottom.add(key, minuteLine);
                    ok(file, context.getInfo(), date, kline, minuteLine, ztReason, null, kline, getSpeedUp(kline, minuteLine) + " isStandMA250:" + kline.isStandMA250() + " vol:" + context.getLastMinuteLine().getVolStr(), context);
                }
            } else {
                context.setLastMinuteLine(minuteLine);
                ok(file, context.getInfo(), date, kline, minuteLine, ztReason, null, kline, "" + minuteLine.getTime() + "  " + " totalF:" + minuteLine.getMsg() + " isStandMA250:" + kline.isStandMA250() + " vol:" + context.getLastMinuteLine().getVolStr(), context);
            }
        }

        MinuteLine minuteLine = realTimeFilterFirstJump(file, date, kline, context);
        if (minuteLine != null) {
            context.setLastMinuteLine(minuteLine);
            ZTReason reason = new ZTReason();
            reason.reason = "--";
            reason.minuteLine = minuteLine;
            ok(file, context.getInfo(), date, kline, minuteLine, reason, null, kline, "" + minuteLine.getTime() + "  " + " totalF:" + minuteLine.getMsg() + " isStandMA250:" + kline.isStandMA250() + " vol:" + context.getLastMinuteLine().getVolStr(), context);
        }

    }

    public static ZTReason realTimeFilter(String file, String date, Kline kline, LineContext context) {
        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
        MinuteLine minuteLine = StockAllMinuteLine.getFirstSpeedUp(kline, stockAllMinuteLine, getCode(file), date, context);
        if (minuteLine != null) {
            ZTReason ztReason = new ZTReason();
            ztReason.minuteLine = minuteLine;
            return ztReason;
        }
        return null;
    }

    public static MinuteLine realTimeFilterFirstJump(String file, String date, Kline kline, LineContext context) {
        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
        MinuteLine minuteLine = StockAllMinuteLine.realTimeFilterFirstJump(kline, stockAllMinuteLine, getCode(file), date, context);
        if (minuteLine != null) {
            return minuteLine;
        }
        return null;
    }

    public static Grid printDaysMinutesTJ(String file, Kline kline, LineContext context) {
        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
        StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(kline.getDate());
        if (stockDayMinuteLine == null) {
            int a = 0;
        }
        Grid grid = stockDayMinuteLine.logVOLTJ(kline);
        return grid;
    }

    public static boolean printDaysMinutesBottomUpTJ(String file, Kline kline, LineContext context) {
        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
        StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(kline.getDate());
        if (stockDayMinuteLine == null) {
            int a = 0;
        }
        return stockDayMinuteLine.logBottomUpFlag(kline, context);
    }


    public static TypeResult realRightAngleFilter(String file, Kline kline, StockDayMinuteLine stockDayMinuteLine, LineContext context) {
        TypeResult typeResult = null;
        if (stockDayMinuteLine != null) {
            typeResult = stockDayMinuteLine.filterRightAngle();
        }
        return typeResult;
    }


    public void prs(String file, List<Kline> days, String date, String stragetyName, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
        //dynamic
        prsIsN(file, days, date, weeks, moths, usemonth, useweek, context);
    }

    public void prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
        int NUM = 4;
        int idx = getIdx(days, date);
        if (!AbsStragety.isMonitor) {
            if (idx < 141) {
                return;
            }
        }

        Kline kline0 = days.get(idx);
        if (Stragety.isMonitor) {
            filter(file, days, date, weeks, moths, usemonth, useweek, kline0, context);
        }
    }

    static String getString(String nane, Object v) {
        return " " + nane + " " + v;
    }


}
