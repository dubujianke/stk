package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.mk.data.eastmoney.GetGuben;
import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.report.LineReport;
import com.mk.util.StringUtil;

import java.util.List;

/**
 * 大底
 */
public class IsBottom extends Stragety {
    public static Table table;
    public static Table ztTable;

    public static void initTable() {
        table = new Table();
        ztTable = new Table();
        ztTable.addFirst(StragetyZTBottom.headerRow);
    }

    public static String getINFO(String INFO) {
        int idx = INFO.indexOf(" ");
        String str1 = INFO.substring(0, idx);
        String str2 = INFO.substring(idx);
        str2 = str2.replaceAll(" ", "").trim();
        if (str2.length() == 3) {
            str2 = "  " + str2;
        }
        return str1 + " " + str2;
    }


    public static Table getTable(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, LineContext context) {
        Row row = IsZhangting.getRow(file, INFO, date, kline0, minuteLine, context);
        Table table = new Table();
        table.add(StragetyZTBottom.headerRow);
        table.add(row);
        return table;
    }

    /**
     * 上月z幅>12.5  上上月z幅>15 放行
     *
     * @param table
     * @param row
     * @return
     */
    public static boolean filter0(Table table, Row row) {
        boolean flag1 = false;
        if (row.getFloat(table.getColumn("上月z幅")) > 12.5) {
            flag1 = true;
        }
        if (row.getFloat(table.getColumn("上上月z幅")) > 15) {
            flag1 = true;
        }
        if (flag1) {
            return true;
        }

        if (row.getFloat(table.getColumn("gap120")) < 0 && row.getFloat(table.getColumn("gap120")) > -1) {
            if (row.getFloat(table.getColumn("gap250")) < -1 && row.getFloat(table.getColumn("gap250")) > -2) {
                return true;
            }
        }
        if (row.getFloat(table.getColumn("gap120")) < 0 && row.getFloat(table.getColumn("gap120")) > -3.5) {
            return false;
        }

        if (row.getInt(table.getColumn("月上吊线")) == 0) {
            return false;
        }
        if (row.getInt(table.getColumn("k250")) < -20) {
            return false;
        }
        if (row.getInt(table.getColumn("30d60")) >= 0 && row.getInt(table.getColumn("30d60")) < 25) {
            return false;
        }
        if (row.getInt(table.getColumn("w60d120")) >= 0 && row.getInt(table.getColumn("w60d120")) < 17) {
            return false;
        }
        if (row.getInt(table.getColumn("w120d250")) >= 0 && row.getInt(table.getColumn("w120d250")) < 48) {
            return false;
        }
        if (row.getInt(table.getColumn("120d250")) >= 0 && row.getInt(table.getColumn("120d250")) < 17) {
            return false;
        }

        return true;
    }

    /**
     * 月z幅<12.5  上上月z幅<15 放行
     *
     * @param table
     * @param row
     * @return
     */
    public static boolean filter(Table table, Row row) {
        boolean flag1 = false;
        if (row.getFloat(table.getColumn("上月z幅")) > 12.5) {
            flag1 = true;
        }
        if (row.getFloat(table.getColumn("上上月z幅")) > 15) {
            flag1 = true;
        }
        if (flag1) {
            if (row.getFloat(table.getColumn("周MIN涨幅")) > 2.4) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(9)")) > (3.9 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(8)")) > (3.6 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(7)")) > (3.4 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(6)")) > (3.4 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(5)")) > (3.8 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(4)")) > (2.4 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(3)")) > (3.0 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(2)")) > (5.6 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("zhenf(1)")) > (2.6 + 0.01)) {
                return false;
            }
            if (row.getFloat(table.getColumn("周MINMA压力30")) < (-7.0) && row.getFloat(table.getColumn("周MINMA压力30")) > (-10.0)) {
                return false;
            }
            if (row.getFloat(table.getColumn("周MINMA压力60")) < (999.0)) {
                return false;
            }
            if (row.getFloat(table.getColumn("周MINMA压力250")) < (999.0)) {
                return false;
            }
            if (row.getFloat(table.getColumn("日MINMA压力250")) < (-3.0)) {
                return false;
            }
            if (row.getFloat(table.getColumn("gap30")) > (5.0)) {
                return false;
            }
        }
        return true;
    }


    public static boolean filterRow(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, String msg, LineContext context) {
        if (StragetyBottom.step == 0) {
            return filterRow0(file, INFO, date, kline0, minuteLine, msg, context);
        } else if (StragetyBottom.step == 1) {
            return filterRow1(file, INFO, date, kline0, minuteLine, msg, context);
        }
        return false;
    }

    public static boolean filterRow0(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, String msg, LineContext context) {
        boolean flag1 = kline0.hasPrevZT(20);
        boolean flag2 = kline0.hasPrevZT2(20);
        if (flag1 || flag2) {
            return false;
        }

        //for safe
        if (kline0.getZhangfu() < -3.0) {
            return false;
        }

        Table table = getTable(file, INFO, date, kline0, minuteLine, context);
        table.initIndex();

        double v5 = table.rows.get(1).getFloat(table.getColumn("大涨幅度"));
        int v5Idx = table.rows.get(1).getInt(table.getColumn("大涨Idx"));
        int type5 = 0;
        if (v5 < 5) {
            type5 = 1;
        } else if (v5 < 7 && v5Idx > 20) {
            type5 = 1;
        }
        context.setType05(type5);
        return true;
    }

    public static boolean catchFlag(Table table, Row row) {
        return false;
    }
    public static boolean catchFlag_(Table table, Row row) {
        boolean catchF = false;

//        if (row.getFloat(table.getColumn("上月z幅")) > 12.5) {
//            catchF = true;
//        }

        if (row.getFloat(table.getColumn("上上月z幅")) > 15) {
            catchF = true;
        }
        //4500
        boolean tt1 = false;
        boolean tt2 = false;
        if (row.getInt(table.getColumn("加速天量")) > 5000) {
            tt1 = true;
            catchF = true;
        }
        if (row.getInt(table.getColumn("减速天量")) > 8000) {
            tt2 = true;
            catchF = true;
        }

        if (row.getInt(table.getColumn("加速天量")) > 3500) {
            tt1 = true;
        }
        if (row.getInt(table.getColumn("减速天量")) > 5000) {
            tt2 = true;
        }
        if (row.getFloat(table.getColumn("gap250")) > 8.5) {
            catchF = true;
        }
        if (row.getFloat(table.getColumn("gap30")) > 8) {
            catchF = true;
        }
        if (row.getFloat(table.getColumn("cur")) > 5) {
            catchF = true;
        }
        if (row.getFloat(table.getColumn("prev(1)")) > 4) {
            catchF = true;
        }


        if (!catchF) {
            if (tt1 && tt2) {
                catchF = true;
            }
        }
        return catchF;
    }

    public static boolean filterRow1(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, String msg, LineContext context) {
        boolean flag1 = kline0.hasPrevZT(20);
        boolean flag2 = kline0.hasPrevZT2(20);
        if (flag1 || flag2) {
            return false;
        }

        //for safe
        if (kline0.getZhangfu() < -3.0) {
            return false;
        }

        if (file.contains("000720")) {
            int a = 0;
        }

        double df = kline0.getPrevDF3(10);
        if (df > 5) {
            return false;
        }

        Table table = getTable(file, INFO, date, kline0, minuteLine, context);
        table.initIndex();
        Row row = table.rows.get(1);
        if (catchFlag(table, row)) {
            return false;
        }

        return true;
    }



    public static void ok(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
        boolean flag = filterRow(file, INFO, date, kline0, null, msg, context);
        if (context.getkModel() != null && context.getkModel().getRow() != null) {
            double mtZf = context.getkModel().getRow().getFloat("mtZf");
            if (!StragetyBottom.isBottom) {
                if (mtZf < 1.5) {
                    return;
                }
            }
        }
        if (flag) {
            okReal(table, file, INFO, date, kline0, weekline, nextN, msg, context);
        }
    }

    public static void okReal(Table table, String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
        String dateCur = date;
        date = DateUtil.getNextWorkDate(date);
        double zt = kline0.getClose() * 1.1f;
        try {
            Row row = IsZhangting.getRow(file, INFO, date, kline0, null, context);
            Row rowRaw = null;
            if (context.getkModel() != null && context.getkModel().getRow() != null) {
                rowRaw = context.getkModel().getRow();
                row.setCol(rowRaw.getTable().getColumn("涨幅"), "" + rowRaw.getFloat("涨幅"));
                row.setCol(rowRaw.getTable().getColumn("分钟"), "" + rowRaw.getStr("分钟"));
            }

            try {
                LineReport lineReport = report(file, nextN, context, context.isRealBottomFlag());
                //加速天序号	加速天量	减速天序号	减速天量
                Object[] objsU = lineReport.isFirstSpeedUpWithVOLInDays(context);
                Object[] objsD = lineReport.isFirstSpeedDownWithVOLInDays(context);

                List<Integer> list = lineReport.getSeries(0);
                row.setCol(rowRaw.getTable().getColumn("v900(0)"), "" + list.get(0));
                row.setCol(rowRaw.getTable().getColumn("v900(1)"), "" + list.get(1));
                row.setCol(rowRaw.getTable().getColumn("v900(2)"), "" + list.get(2));
                row.setCol(rowRaw.getTable().getColumn("v900(3)"), "" + list.get(3));
                row.setCol(rowRaw.getTable().getColumn("v900(4)"), "" + list.get(4));

                row.setCol(rowRaw.getTable().getColumn("加速天序号"), "" + objsU[0]);
                row.setCol(rowRaw.getTable().getColumn("加速天量"), "" + objsU[1]);
                row.setCol(rowRaw.getTable().getColumn("减速天序号"), "" + objsD[0]);
                row.setCol(rowRaw.getTable().getColumn("减速天量"), "" + objsD[1]);
            } catch (Exception e) {
//                e.printStackTrace();
            }
            try {
                boolean bottomUp = reportBottomUp(file, nextN, context, context.isRealBottomFlag());
                int a = 0;
                row.setCol(rowRaw.getTable().getColumn("bottomup"), "" + StringUtil.getInt(bottomUp));
            } catch (Exception e) {
//                e.printStackTrace();
            }


            table.add(row);

//            Log.logString(StragetyBottom.resultBuffer,
//                    "" + IsBottom.getINFO(INFO) + " " + IsBottom.class.getSimpleName() + " " + dateCur + " " + date
//                            + "	Stguben:" + StringUtil.spaceString(StringUtil.format3(guben, 0), 3)
//                            + "	type:" + 1
//                            + "	LFT:" + 0
//                            + "	RGT:" + 0
//            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void error(String file, String date, String msg) {
        int a = 0;
        Log.log(file + "  " + date + " " + msg);
    }


    public static void filter(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, Kline kline, LineContext context) {
        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
        Kline lineMin = kline.getMin(60);
        double frac = KLineUtil.compareMax(lineMin.getClose(), kline.getClose());
        boolean realBottomFlag = false;
        if (frac < 3) {
            realBottomFlag = true;
        }

        if (context.isIs3MonthSmal()) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }

        context.setRealBottomFlag(realBottomFlag);

        //2.0
        {
            boolean isBottom = IsBottomUtil.prsIsNDF(file, kline, days, date, 60, 50);
            if (isBottom) {
                ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
                return;
            }
        }

        if (context.isDayStandMA250()) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }

        IsBottomUtil.NUM = 60;
        boolean isBottom = IsBottomUtil.prsIsN2(file, kline, days, date, weeks, moths, usemonth, useweek);
        if (isBottom) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }
        IsBottomUtil.NUM = 130;
        isBottom = IsBottomUtil.prsIsN2(file, kline, days, date, weeks, moths, usemonth, useweek);
        if (isBottom) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }
        isBottom = IsBottomUtil.prsIsN3(file, kline, days, date, weeks, moths, usemonth, useweek);
        if (isBottom) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }

        isBottom = IsBottomUtil.prsIsN4(file, kline, days, date, weeks, moths, usemonth, useweek);
        if (isBottom) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }
        if (realBottomFlag) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }
        if (context.isLocalHorFlag()) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }
        if (context.isLocalBottomFlag()) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }
        if (context.isLocalWBottomFlag()) {
            ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
            return;
        }
    }

    /**
     * 当日20分钟从底部-2点以上拉上4个点以上，-2->4 放量
     * 之前的1个小时没有量，
     * 之前一直在水下1个小时 -1.8
     */
    public static boolean reportBottomUp(String file, Kline kline, LineContext context, boolean realBottomFlag) {
        try {
            StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
            LineReport lineReport = new LineReport();
            lineReport.setCode(INFO);
            lineReport.setFile(file);
            lineReport.setStockAllMinuteLine(stockAllMinuteLine);
            lineReport.setBottomFlag(realBottomFlag ? 2 : 0);
            Kline item = kline.prev(0);
            return IsZhangting.printDaysMinutesBottomUpTJ(file, item, context);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

    public static LineReport report(String file, Kline kline, LineContext context, boolean realBottomFlag) {
        try {
            StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
            LineReport lineReport = new LineReport();
            lineReport.setCode(INFO);
            lineReport.setFile(file);
            lineReport.setStockAllMinuteLine(stockAllMinuteLine);
            lineReport.setBottomFlag(realBottomFlag ? 2 : 0);
            for (int i = 0; i < 5; i++) {
                Kline item = kline.prev(i);
                Grid grid = IsZhangting.printDaysMinutesTJ(file, item, context);
                lineReport.add(grid);
            }
//            lineReport.getReportCross();
            lineReport.getSppedUpWithVOL(context);
            if (lineReport.isDeadCross() == 0) {
//                LineReports.add(lineReport);
            }
            return lineReport;
//            lineReport.clear();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }


    public void prs(String file, List<Kline> days, String date, String stragetyName, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
        try {
            prsIsN(file, days, date, weeks, moths, usemonth, useweek, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean importantShizhiFlag = true;
    public static boolean importantMonthFlag = true;
    public static boolean importantMAFlag = true;
    public static boolean importantGoingDownFlag = true;
    public static boolean importantFlag = false;


    public void prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
        if (file.contains("603348") && date.equalsIgnoreCase("2023/09/11")) {
            int a = 0;
        }

        if (INFO.contains("ST")) {
            return;
        }

        int idx = getIdx(days, date);
        if (idx < 141) {
            return;
        }

        ////////////////////////////////////////////////////////////////////////
        Kline kline0 = days.get(idx);
//        if (kline0.getZhangfu()>9) {
//            okReal(ztTable, file, INFO, date, kline0, kline0.weekline, null, "", context);
//            return;
//        }
        if (kline0.isZT(30)) {
            return;
        }

        //IMPORTANT 1
        double v = GetGuben.retriveOrGetShizhi(AbsStragety.getCode(file), kline0.getClose());
        if (v >= 210 && importantShizhiFlag) {
            error(file, date, "guben < 210");
            return;
        }

        //IMPORTANT 2
        boolean monFlag = true;
        boolean allFlag = true;
        int aweekIdx = kline0.getWeekDay();
        Weekline prev1Week = KLineUtil.prevWeekline(kline0, 1);
        Weekline prev2Week = KLineUtil.prevWeekline(kline0, 2);

        double prevW1ZF = prev1Week.getZhangfu2();
        double prevW2ZF = prev2Week.getZhangfu2();
        double prevM1ZF = kline0.monthKline.prev(1).getZhangfu2();
        double prevM2ZF = kline0.monthKline.prev(2).getZhangfu2();
        double totalAbsM = Math.abs(prevM1ZF + prevM2ZF);
        double minAll = Integer.MAX_VALUE;
        double minWeek = Integer.MAX_VALUE;
        double minMon = Integer.MAX_VALUE;
        Kline Kline = kline0;
        double hand = kline0.getHand(context.getTotalV());
        double handPrev = kline0.prev().getHand(context.getTotalV());
        double handPrev2 = kline0.prev(2).getHand(context.getTotalV());
        double hands[] = new double[]{handPrev2, handPrev, hand};

        if (Math.abs(Kline.monthKline.prev(1).getZhangfu2()) < Math.abs(minMon)) {
            minMon = Kline.monthKline.prev(1).getZhangfu2();
        }
        if (Math.abs(Kline.monthKline.prev(1).getZhangfu2() + Kline.monthKline.prev(2).getZhangfu2()) < Math.abs(minMon)) {
            minMon = Kline.monthKline.prev(1).getZhangfu2() + Kline.monthKline.prev(2).getZhangfu2();
        }

        if (Math.abs(prev1Week.getZhangfu2()) < Math.abs(minWeek)) {
            minWeek = prev1Week.getZhangfu2();
        }
        if (Math.abs(Kline.weekline.prev(2).getZhangfu2()) < Math.abs(minWeek)) {
            minWeek = Kline.weekline.prev(2).getZhangfu2();
        }
        if (Math.abs(prev1Week.getZhangfu2() + prev2Week.getZhangfu2()) < Math.abs(minWeek)) {
            minWeek = prev1Week.getZhangfu2() + prev2Week.getZhangfu2();
        }

        if (Math.abs(minWeek) < Math.abs(minAll)) {
            minAll = minWeek;
        }
        if (Math.abs(minMon) < Math.abs(minAll)) {
            minAll = minMon;
        }
        double minAllAbs = Math.abs(minAll);
        if (minAllAbs < 2.5f) {
        } else if (minAllAbs > 2.5f && minAllAbs <= 3.0f) {
            if (hand > 1 || handPrev > 1 || handPrev2 > 1) {
                return;
            }
        } else if (minAllAbs < 3.3f) {
            if (StringUtil.getSmallThanCount(hand, handPrev, handPrev2, 2.6f) < 2) {
                return;
            }
            if (hand >= 1.7f) {
                return;
            }
        } else if (minAllAbs < 4.3f) {
            if (hand > 2 || handPrev > 2 || handPrev2 > 2) {
                return;
            }
        } else {
            return;
        }
/////////////////////////////////////////////////////////////////////////////
        //IMPORTANT 3 zhangfu
        double[] zfms = kline0.monthKline.prev().getPrevZhangFu(34);
        if (zfms == null) {
            zfms = kline0.monthKline.prev().getPrevZhangFu(18);
        }
        int monthLen = 0;
        double monzhf = 0;
        if (zfms != null) {
            monzhf = zfms[0];
            monthLen = (int) zfms[1];
            if (monzhf > 250) {
                if (monzhf < 300) {//>250 < 300
                    //必须消化9个月以上， 且换手大多数小于2
                    if (monthLen >= 9 && StringUtil.getSmallThanCount(hand, handPrev, handPrev2, 2) >= 2) {
                    } else {
                        monFlag = false;
                    }
                } else {//>300
                    if (monthLen < 12) {
                        //必须消化12个月以上， 否则换手不能大于1
                        if (hand > 1 || handPrev > 1 || handPrev2 > 1) {
                            monFlag = false;
                        }
                    }
                }

                //消化完毕后，涨幅要小
                if (minAll > 0 && minAll > 1.8) {
                    return;
                }
                if (minAll < 0 && Math.abs(minAll) > 2) {
                    return;
                }

                if (Math.abs(minMon) < 10) {
                } else {
                    return;
                }
            } else {
                //<=250
                if (Math.abs(minAll) <= 3.01) {
                } else if (Math.abs(minAll) > 3.0) {
                    if (Math.abs(minMon) < 5.0) {
                    } else if (Math.abs(minMon) < 8.0 && StringUtil.getSmallThanCount(hand, handPrev, handPrev2, 0.6f) >= 3) {
                    } else {
                        return;
                    }
                }

                //涨幅<=250 月最大涨幅为10（-） 12（+）
                if (minMon < 0) {
                    if (Math.abs(minMon) < 10) {
                    } else {
                        if (StringUtil.getSmallThanCount(hand, handPrev, handPrev2, 1f) > 3) {
                            return;
                        }
                    }
                } else {
                    if (Math.abs(minMon) >= 12) {
                        return;
                    }
                }
            }
        }
        if (!allFlag) {
            return;
        }
        if (!monFlag) {
            return;
        }

        //IMPORTANT 5:isGongDownToMAAndUpMAPrevent
        boolean isGongDown = kline0.monthKline.isGongDownToMAAndUpMAPrevent();
        if (isGongDown && importantGoingDownFlag) {
            error(file, date, "isGoingDown to MA");
            return;
        }

        //IMPORTANT
        boolean isWeekCrash = prev1Week.isCrashDownMA120250();
        if (isWeekCrash) {
            return;
        }
        ///////////////////////////////////////////////////////////////////////
        boolean zfFlag = false;
        //local bottom
        boolean localBottomFlag = false;
        try {
            com.mk.tool.stock.Kline.LocalBottom localBottom = kline0.getLocalBottom();
            if (localBottom.flag) {
                localBottomFlag = true;
            }
        } catch (Exception e) {
        }
        context.setLocalBottomFlag(localBottomFlag);


        context.setLocalWBottomFlag(false);
        Kline prev = kline0.prev();


        boolean prevMonFlag = kline0.monthKline.prev().getZhangfu() < 3.5;
        boolean localHorFlag = false;
        double vs[] = null;
        if (kline0.getWeekDay() == 5) {
            double vs2[] = new double[]{kline0.weekline.prev().getZhangfu(), kline0.weekline.prev(2).getZhangfu(), kline0.weekline.prev(3).getZhangfu(), kline0.weekline.prev(4).getZhangfu(), kline0.weekline.prev(5).getZhangfu()};
            vs = vs2;
        } else {
            double vs2[] = new double[]{kline0.weekline.prev().getZhangfu(), kline0.weekline.prev(2).getZhangfu(), kline0.weekline.prev(3).getZhangfu(), kline0.weekline.prev(4).getZhangfu(), kline0.weekline.prev(5).getZhangfu()};
            vs = vs2;
        }
        if (StringUtil.getSmallThanCountAbs(vs, 2f) >= 3) {
            localHorFlag = true;
            zfFlag = true;
        }
        if (StringUtil.getSmallThanCountAbs(vs, 3f) >= 4) {
            localHorFlag = true;
            zfFlag = true;
        }
        vs = kline0.getZhanfus(5);
        if (StringUtil.getSmallThanCountAbs(vs, 1f) >= 5) {
            localHorFlag = true;
            zfFlag = true;
        }
        if (Math.abs(kline0.monthKline.getZhangfu()) < 3) {
            localHorFlag = true;
            zfFlag = true;
        }

        double twoZf = kline0.getZhangfu() + prev.getZhangfu();
        if (twoZf > 4 && prev.getZhangfu() > 4.1) {
            if (localHorFlag || localBottomFlag) {
            } else {
                return;
            }
        }

        boolean handFlag = KLineUtil.isAnySmall(hands, 2.0f);
        boolean handFlag2 = StringUtil.getSmallThanCountAbs(hands, 3f) >= 2;
        if (!handFlag) {
            if (kline0.allMA103060120OK()) {

            } else {
                double frac250 = KLineUtil.compareMaxSign(kline0.getClose(), kline0.getMA250());
                double frac2502 = KLineUtil.compareMaxSign(kline0.getMin(), kline0.getMA250());
                if (Math.abs(frac250) < 2.5f || Math.abs(frac2502) < 2.5f) {
                } else {
                    if (localHorFlag && StringUtil.getSmallThanCountAbs(kline0.getZhanfus(4), 2) >= 3) {
                    } else if (localBottomFlag) {

                    } else {
                        return;
                    }

                }
            }
        }
        if (kline0.getZhangfu() < -2.6) {
            if (!localHorFlag) {
                return;
            }
        }

        context.setLocalHorFlag(localHorFlag);

        //3.0
        {
            boolean flag = false;
            Weekline weekline = kline0.weekline;
            List<Kline.MAI> alist = weekline.prev().isStandMAI(1);
            double prevZF = weekline.prev().getZhangfu();
            if (alist.size() > 0) {
                Kline.MAI mai = alist.get(0);
                if (prevZF < 2.9) {
                    flag = true;
                }
                if (prevZF >= 2.9 && weekline.prev().isCross2(10)) {
                    flag = true;
                } else {
                    if (mai.space < 2.8) {
                        flag = true;
                    }
                }
            } else {
                flag = true;
            }
            if (!flag) {
                return;
            }
        }

        IsBottomUtil.NUM = 130;
        boolean isBottom = IsBottomWeekUtil.prsIsN(file, days, date, weeks, moths, usemonth, useweek, 220 * 5);
        boolean isBottom2 = IsBottomWeekUtil.prsIsN(file, days, date, weeks, moths, usemonth, useweek, 220 * 10);
        if (!isBottom && !isBottom2) {
            return;
        }


        //small zhangfu
        if (kline0.getZhangfu() <= 0) {
            double azf0 = kline0.getSmallZhangu();
            if (azf0 < 0.5) {
                zfFlag = true;
            }
            double zf0 = kline0.getZhangfu();
            double zf1 = kline0.prev().getZhangfu();
            if (Math.abs(zf0) < 2 && Math.abs(zf1) < 2 && zf1 > 0 && (zf1 + zf0) > 0) {
                zfFlag = true;
            }
            if (Math.abs(zf0) < 1.3f && Math.abs(zf1) < 1.3f) {
                zfFlag = true;
            }

            if (kline0.isStandMA120()) {
                zfFlag = true;
            }

            //local bottom
            if (localBottomFlag) {
                zfFlag = true;
            }
        } else {
            double zf0 = kline0.getZhangfu();
            if (localBottomFlag) {
                zfFlag = true;
            }
            if (zf0 < 2.65) {
                zfFlag = true;
            } else {
            }
        }
//        if (!zfFlag && !localHorFlag) {
//            error(file, date, "!zfFlag");
//            return;
//        }

        Kline next = kline0.next();
        if (StragetyBottom.isBottomTest) {
            if (next != null && !next.isZhanging()) {
                return;
            }
        }

        Weekline weekline = null;
        if (useweek == 1) {
            weekline = kline0.weekline;
        }
        //WEEK stand ma250
        boolean isWeekStandMA250 = false;
        if (useweek == 1) {
            if (weekline.isStandMA250__(3)) {
                isWeekStandMA250 = true;
            }
        }
        context.setWeekStandMA250(isWeekStandMA250);

        boolean mwFlag = true;
        boolean aboutMA = false;
        boolean allAboutM250 = false;
        ///////////////////////////////////////////////////////////////////////////////
        double v3060[] = kline0.isStandMA3060();

        boolean dflag = false;
        boolean wflag = false;
        boolean mflag = false;
        if (v3060[0] > 0 && v3060[0] < 3.0) {
            dflag = true;
        } else if (v3060[1] > 0 && v3060[1] < 2.7) {
            dflag = true;
        } else if (v3060[2] > 0 && v3060[2] < 2.7) {
            dflag = true;
        } else if (v3060[3] > 0 && v3060[3] < 2.7) {
            dflag = true;
        }
        if (!dflag) {
            double ms[] = kline0.monthKline.isStandMA60120250();
            double vWeek[] = kline0.weekline.isStandMA60120250Fix();
            if (vWeek[0] > 0 && vWeek[0] < 2.7) {
                wflag = true;
            } else if (vWeek[1] > 0 && vWeek[1] < 3.0) {
                wflag = true;
            } else if (vWeek[2] > 0 && vWeek[2] < 3.0) {
                wflag = true;
            } else if (vWeek[3] > 0 && vWeek[3] < 3.0) {
                wflag = true;
            } else if (vWeek[4] > 0 && vWeek[4] < 3.0) {
                wflag = true;
            } else if (vWeek[5] > 0 && vWeek[5] < 3.0) {
                wflag = true;
            }
            if (kline0.weekline.prev().getZhangfu() < 1 && kline0.weekline.prev(2).getZhangfu() < 2) {
                if (vWeek[0] > 0 && vWeek[0] < 8.0) {
                    wflag = true;
                } else if (vWeek[1] > 0 && vWeek[1] < 8.0) {
                    wflag = true;
                } else if (vWeek[2] > 0 && vWeek[2] < 8.0) {
                    wflag = true;
                } else if (vWeek[3] > 0 && vWeek[3] < 8.0) {
                    wflag = true;
                }
            }

            if (ms[0] > 0 && ms[0] < 2.7) {
                mflag = true;
            } else if (ms[1] > 0 && ms[1] < 5.0) {
                mflag = true;
            } else if (ms[2] > 0 && ms[2] < 5.0) {
                mflag = true;
            } else if (ms[3] > 0 && ms[3] < 5.0) {
                mflag = true;
            }

            if (wflag || mflag) {
                allAboutM250 = true;
                aboutMA = true;
                context.setDayStandMA250(true);
            }
        }

        if (prevMonFlag || localHorFlag || localBottomFlag || dflag || wflag || mflag) {
        } else {
            error(file, date, "!localHorFlag");
            return;
        }


        //last m
        if (kline0.weekline.isOpenStandMA120()) {
            int a = 0;
            allAboutM250 = true;
            aboutMA = true;
            context.setDayStandMA250(true);
        } else {
            double lastMonth = kline0.monthKline.prev().getEntityZhangfu();
            if (lastMonth < 0) {
                if (isWeekStandMA250) {
                    if (lastMonth < -9.5f) {
                        mwFlag = false;
                    }
                } else {
                    if (lastMonth < -9.3f) {
                        mwFlag = false;
                    }
                }
            } else if (lastMonth > 0) {
                if (lastMonth >= 11) {
                    mwFlag = false;
                }
            }
        }


        double ret[] = kline0.weekline.spaceDownTouchMAI250();
        if (ret[1] == 250 && ret[0] > 6) {
            double ret2[] = kline0.monthKline.spaceDownTouchMAI250();
            if (ret2[1] == 250 && ret2[0] <= 6) {
                aboutMA = true;
            } else if (ret2[1] == 120 && ret2[0] <= 6) {
                aboutMA = true;
            } else {
                boolean ff = false;
                for (int i = 0; i < 5; i++) {
                    Weekline tmp = kline0.weekline.prev(i + 1);
                    double ret3[] = tmp.spaceDownTouchMAI250();
                    if (ret2[0] <= 8 && ret3[1] == 250 && ret3[0] <= 3) {
                        if (tmp.getOpen() > tmp.getMA10()) {
                            ff = true;
                        }
                    }
                }
                if (ff) {
                    aboutMA = true;
                }
            }
        } else {
            aboutMA = true;
        }
        if (ret[1] == 120 && ret[0] > 6) {
            double ret2[] = kline0.monthKline.spaceDownTouchMAI250();
            if (ret2[1] == 250 && ret2[0] <= 6) {
                aboutMA = true;
            } else if (ret2[1] == 120 && ret2[0] <= 6) {
                aboutMA = true;
            } else {
                boolean ff = false;
                for (int i = 0; i < 5; i++) {
                    Weekline tmp = kline0.weekline.prev(i + 1);
                    double ret3[] = tmp.spaceDownTouchMAI250();
                    if (ret2[0] <= 9 && ret3[1] == 250 && ret3[0] <= 3) {
                        if (tmp.getOpen() > tmp.getMA10()) {
                            ff = true;
                        }
                    }
                }
                if (ff) {
                    aboutMA = true;
                }
            }
        } else {
            aboutMA = true;
        }

        if (kline0.weekline.prev().getEntityZhangfu() < -2.5f) {
            aboutMA = false;
        }
        if (kline0.weekline.prev().getZhangfu() < -3.5f) {
            aboutMA = false;
        }
        if (kline0.weekline.prev(2).getZhangfu() < -3.5f) {
            aboutMA = false;
        }

        double ret0[] = kline0.weekline.spaceDownTouchMAI250();
        if (ret0[1] == 60 && ret[0] < 2.5f) {
            aboutMA = true;
        }


        if (!allAboutM250) {
            double retw250[] = kline0.weekline.spaceDownTouchMAI2502();
            double retw60[] = kline0.weekline.spaceDownTouchMAI_(60);
            double retw120[] = kline0.spaceDownTouchMAI_(120);
            boolean deadCrpss = kline0.isDeadCrpsses(30, 60, 0.25f);
            if (deadCrpss) {
                boolean downFlag = false;
                if (retw250[0] < 1.9f && retw250[1] == 250) {
                    downFlag = true;
                }
                if (retw120[0] < 1.5f && retw120[1] == 120) {
                    downFlag = true;
                }
                if (retw60[0] < 1.9f && retw60[1] == 60) {
                    downFlag = true;
                }
                if (!downFlag) {
                    error(file, date, "isDeadCrpsses");
                    return;
                } else {
                    allAboutM250 = true;
                    aboutMA = true;
                    context.setDayStandMA250(true);
                }
            } else {
                boolean downFlag = false;
                if (retw250[0] < 1.9f && retw250[1] == 250) {
                    downFlag = true;
                }
                if (retw60[0] < 1.9f && retw60[1] == 60) {
                    downFlag = true;
                }
                if (retw120[0] < 1.9f && retw120[1] == 120) {
                    downFlag = true;
                }
                if (downFlag) {
                    aboutMA = true;
                    allAboutM250 = true;
                    context.setDayStandMA250(true);
                }
            }
        }


        double ret2[] = kline0.weekline.spaceDownTouchMAI250ByEntityMin();
        double retd[] = kline0.spaceDownTouchMAI250ByEntityMin();
        if (ret2[1] == 250 && ret2[0] < 2) {
            if (retd[1] == 250 && retd[0] < 2) {
                int weekIdx = DateUtil.getWeek(DateUtil.stringToDate3(date));
                if (weekIdx == 5) {
                    aboutMA = true;
                    allAboutM250 = true;
                    context.setDayStandMA250(true);
                    context.setWeekStandMA250(true);
                }
            }
        }
        if ((retd[1] == 250 || retd[1] == 120) && retd[0] < 3) {
            double retw[] = kline0.weekline.spaceALlDownTouchMAI250ByEntityMin();
            boolean fflag = false;
            if (retw[1] == 250 && retw[0] < 2) {
                fflag = true;
            } else if (retw[1] == 60 && retw[0] < 2) {
                double frac = KLineUtil.compareMax(kline0.weekline.getMA60(), kline0.weekline.getMA250());
                if (frac < 4.5f) {
                    fflag = true;
                }
            }
            if (fflag) {
                int weekIdx = DateUtil.getWeek(DateUtil.stringToDate3(date));
                if (weekIdx == 5) {
                    aboutMA = true;
                    allAboutM250 = true;
                    context.setDayStandMA250(true);
                    context.setWeekStandMA250(true);
                }
            }
        }


        {
            boolean fflag63 = kline0.monthKline.allIsLessAbsThanExceptNoSelf(3, 2, 2.0f, 0, 0.0f, 0, 0.0f, 3.0f);
            if (fflag63) {
                context.setIs3MonthSmal(true);
                context.setMsg("MHor(3,0,3)~2(" + 2 + ")");
                addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
                return;
            }
        }

        if (!aboutMA) {
            boolean deadCrpss = kline0.isDeadCrpsses(30, 60, 0.25f);
            if (deadCrpss) {
                error(file, date, "isDeadCrpsses");
                return;
            }
        }


        if (localBottomFlag || aboutMA || allAboutM250) {
        } else {
            double df = kline0.getMaxDF(15);
            if (isWeekStandMA250) {
                if (df > 4.9f) {
                    return;
                }
            } else {
                if (df <= 3.3f) {
                } else {
                    double df2 = kline0.getMaxDF(9);
                    if (df2 > 1.5) {
                        return;
                    }
                }
            }
        }


        if (kline0.prevHasZF(3, 4.5f, true)) {
            if (localHorFlag || localBottomFlag) {

            } else {
                error(file, date, "prevHasZF");
                return;
            }
        }

        if (kline0.isZhanging()) {
            return;
        }

        {
            //603389
            boolean fflag63 = kline0.allIsLessAbsThanExcept(4, 0, 2.0f, 0, 0.0f, 0, 0.0f, 1.5f);
            if (fflag63) {
                context.setMsg("Hor(9,2,3)~2(" + 2 + ")");
                addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
                return;
            }
        }
        {
            //3 603348
            boolean fflag63 = kline0.allIsLessAbsThanExcept(9, 2, 3.0f, 0, 1.0f, 2, 3.0f, 3);
            double zf2 = kline0.getPrevZhenFExcept(9, 1, 5);
            if (fflag63 && zf2 < 4) {
                context.setMsg("Hor(9,2,3)~2(" + zf2 + ")");
                addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
                return;
            }
        }
        {
            //3 002693
            boolean fflag63 = kline0.allIsLessAbsThanExcept(14, 2, 3.0f, 0, 1.0f, 2, 3.0f, 4);
            double zf2 = kline0.getPrevZhenFExcept(7, 1, 5);
            if (fflag63 && zf2 < 2) {
                context.setMsg("Hor(7,3,2)~2(" + zf2 + ")");
                addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
                return;
            }
        }
        //1 000801
        if (isWeekStandMA250) {
            boolean fflag5 = kline0.allIsLessAbsThan(7, 1, 2.0f);
            if (fflag5) {
                double zf2 = kline0.getPrevZhenF(7);
                if (zf2 < 7) {
                    context.setMsg("isWeekStandMA250  Hor(7,1,2)~2(" + zf2 + ")");
                    addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
                    return;
                }
            }
        }

        //2 000536
        boolean fflag61 = kline0.allIsLessAbsThanExcept(10, 3, 2.0f, 0, 1.0f, 0, 3.0f, 0);
        boolean fflag62 = true;
        if (usemonth == 1) {
            MonthKline monthKline = kline0.monthKline;
            fflag62 = monthKline.allIsLessAbsThanExcept(4, 2, 5.0f, 0, 1.0f, 0, 3.0f, 11);
        }
        if (fflag61 && fflag62) {
            context.setMsg("Hor(10,3,2) MHor(4,2,5)");
            addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
            return;
        }

        {
            //3 000851
            boolean fflag63 = kline0.allIsLessAbsThanExcept(14, 2, 3.0f, 0, 1.0f, 2, 3.0f, 4);
            double zf2 = kline0.getPrevZhenF(14);
            if (fflag63 && zf2 < 10) {
                context.setMsg("Hor(10,3,2)-10");
                addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
                return;
            }
        }
        {
            //4 000518
            boolean fflag65 = kline0.allIsLessAbsThanExcept(7, 2, 3.0f, 0, 1.0f, 0, 3.0f, 3);
            boolean fflag66 = true;
            if (useweek == 1) {
                fflag66 = weekline.allIsLessAbsThansNoSelf(5, 1, 3.0f, 2);
            }
            if (fflag65 && fflag66) {
                context.setMsg("Hor(7,2,3) WHor(5,2,2)");
                addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
                return;
            }
        }
        {
            //002906
            boolean fflag65 = kline0.allIsLessAbsThanExcept(4, 0, 3.0f, 0, 1.0f, 0, 3.0f, 3);
            boolean fflag66 = true;
            if (useweek == 1) {
                fflag66 = weekline.allIsLessAbsThansNoSelf(5, 1, 3.0f, 2);
            }
            if (fflag65 && fflag66) {
                context.setMsg("Hor(4,0,3) WHor(5,2,3)");
                addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
                return;
            }
        }

    }

    private boolean addOK(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context, Kline kline0) {
        if (!Stragety.isResult) {
            Result result = new Result(file, date);
            result.row = context.getkModel().row;
            result.stragety = IsBottom.class.getSimpleName();
            if (file.contains("000016")) {
                int a = 0;
            }

            StragetyBottom.addLine(result, date);
            String key = AbsStragety.getCode(file) + " " + date;
            GlobalContext.map.put(key, null);
            return false;
        } else {
            if (file.contains("600322") && date.equalsIgnoreCase("2023/07/20")) {
                int a = 0;
            }
            filter(file, days, date, weeks, moths, usemonth, useweek, kline0, context);
            String key = AbsStragety.getCode(file) + " " + date;
            GlobalContext.map.put(key, null);
            return true;
        }

    }

    static String getString(String nane, Object v) {
        return " " + nane + " " + v;
    }


}
