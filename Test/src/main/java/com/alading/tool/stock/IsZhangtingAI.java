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
public class IsZhangtingAI extends Stragety {
    public static Table table;
    public static double  minZF = 0.9f;

    public static void initTable() {
        table = new Table();
        table.addFirst(StragetyZTBottom.headerRow);
        table.initIndex();
    }

    public static double  getMtZF(double  open, MinuteLine minuteLine) {
        try {
            double  prev3 = 0;
            String p3Str = "";
            String p3 = "";
            String p2 = "";
            String p1 = "";
            String p0 = "";
            String mZf = "";
            MinuteLine minMinuteLine = minuteLine.getMin();
            double  minPrice = KLineUtil.compareMaxSign(minMinuteLine.price, open);
            String minuteLineStr = "" + minPrice;
            MinuteLine mPrev3 = minuteLine.prev(3);
            MinuteLine mPrev2 = minuteLine.prev(2);
            MinuteLine mPrev1 = minuteLine.prev(1);
            if (mPrev3 != null) {
                prev3 = KLineUtil.compareMaxSign(mPrev3.price, open);
                p3Str = minuteLine.prev(3).getVolStr() + " ";
            }
            double  prev2 = KLineUtil.compareMaxSign(mPrev2.price, open);
            double  prev1 = KLineUtil.compareMaxSign(mPrev1.price, open);
            double  cur = KLineUtil.compareMaxSign(minuteLine.price, open);
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

    public static Row getRowZT(String file, String INFO, String date, Kline kline0Prev, MinuteLine minuteLine, LineContext context) {
        Kline Kline = kline0Prev.next();
        Kline KlinePrev = kline0Prev;
        double  open = KlinePrev.getClose();
        double  prev3 = 0;
        String p3Str = "-";

        String p3 = "";
        String p2 = "";
        String p1 = "";
        String p0 = "";
        String mZf = "";
        if (minuteLine == null || minuteLine.prev(1) == null) {
            if (context.getkModel() != null) {
                mZf = context.getkModel().getRow().getStr("mtZf");
            }
        } else {
            MinuteLine minMinuteLine = minuteLine.getMin();
            double  minPrice = KLineUtil.compareMaxSign(minMinuteLine.price, open);
            MinuteLine mPrev3 = minuteLine.prev(3);
            MinuteLine mPrev2 = minuteLine.prev(2);
            MinuteLine mPrev1 = minuteLine.prev(1);
            if (mPrev3 != null) {
                prev3 = KLineUtil.compareMaxSign(mPrev3.price, open);
                p3Str = minuteLine.prev(3).getVolStr() + " ";
            }
            double  prev2 = KLineUtil.compareMaxSign(mPrev2.price, open);
            double  prev1 = KLineUtil.compareMaxSign(mPrev1.price, open);
            double  cur = KLineUtil.compareMaxSign(minuteLine.price, open);
            p3 = p3Str;
            p2 = minuteLine.prev(2).getVolStr() + "/" + format(prev2 - prev3);
            p1 = minuteLine.prev(1).getVolStr() + "/" + format(prev1 - prev2);
            p0 = minuteLine.getVolStr() + "/" + format(cur - prev1);
            mZf = format(cur - prev3);
        }

        double  zt = KlinePrev.getClose() * 1.1f;
        List<Kline.StandResult> list = KlinePrev.monthKline.spaceUpTouchMA(zt);
        String zfStr = "";
        if (Kline != null) {
            if (minuteLine != null) {
                MinuteLine lastMinuteLine = minuteLine.allLineList.get(minuteLine.allLineList.size() - 1);
                double  frac = KLineUtil.compareMaxSign(lastMinuteLine.price, KlinePrev.getClose());
                zfStr = "" + frac;
            } else {
                zfStr = "" + Kline.getZhangfu();
            }
        }

        Row row = context.getkModel().getRow();
        Table table = row.getTable();
        row.setCol(table.getColumn("分钟"), "" + (minuteLine != null ? minuteLine.getTime() : ""));
        row.setCol(table.getColumn("涨幅"), zfStr);
        row.setCol(table.getColumn("mtP3"), p3);
        row.setCol(table.getColumn("mtP2"), p2);
        row.setCol(table.getColumn("mtP1"), p1);
        row.setCol(table.getColumn("mtP0"), p0);
        row.setCol(table.getColumn("mtZf"), mZf);

        String fistMinuteStr = "0";
        if (minuteLine != null) {
            fistMinuteStr = "" + minuteLine.allLineList.get(0).getZF(kline0Prev);
        } else {
            if (context.getkModel() != null && context.getkModel().getRow() != null) {
                context.getkModel().getRow().getTable().initIndex();
                double  fistMinute = context.getkModel().getRow().getFloat("fistMinute");
                fistMinuteStr = "" + fistMinute;
            } else {
            }
        }


        row.setCol(table.getColumn("fistMinute"), fistMinuteStr);
        if (minuteLine != null && minuteLine.prev(1) != null) {
            row.setCol(table.getColumn("mVPrev2"), "" + minuteLine.prev(2).getVol());
            row.setCol(table.getColumn("mVPrev1"), "" + minuteLine.prev(1).getVol());
            row.setCol(table.getColumn("mVPrev0"), "" + minuteLine.getVol());
        }

        if (minuteLine != null) {
            double  cur = KLineUtil.compareMaxSign(minuteLine.price, open);
            row.setCol(table.getColumn("maxzf"), "" + cur);

            MinuteLine first = minuteLine.getFirst();
            double  firstV = KLineUtil.compareMaxSign(first.price, open);
            double ma120 = kline0Prev.getMA120();
            int ma120Flag = 0;
            if (first.price > ma120 && kline0Prev.close < ma120) {
                ma120Flag = 1;
            }
            row.setCol(table.getColumn("是否跳空"), "" + ma120Flag);
        } else {
            row.setCol(table.getColumn("maxzf"), "-99");
        }

        if (minuteLine != null) {
            double firstV = KLineUtil.compareMaxSign(minuteLine.price, open);
            row.setCol(table.getColumn("curMinute"), ""+firstV);
        }


        return row;
    }

    public static double getFloat(LineContext context, String name) {
        try {
            double v = context.getkModel().getRow().getFloat(name);
            return v;
        } catch (Exception e) {

        }
        return -99;
    }

    /**
     * bottom
     */
    public static Row getRow(String file, String INFO, String date, Kline kline0Prev, MinuteLine minuteLine, LineContext context) {
        Kline Kline = kline0Prev.next();
        Kline KlinePrev = kline0Prev;

        String prevDate = DateUtil.getPrevWorkDate(date);
        String firstSpeedDown = "";
        double open = KlinePrev.getClose();
        double prev3 = 0;
        String p3Str = "-";

        StockDayMinuteLine stockDayMinuteLine = KlinePrev.getStockDayMinuteLine();
        if (stockDayMinuteLine != null) {
            MinuteLine sppedDownMinuteLine = stockDayMinuteLine.getFirstSpeedDown(KlinePrev);
            if (sppedDownMinuteLine != null) {
                firstSpeedDown = "" + sppedDownMinuteLine.getTime();
            }
        }

        String p3 = "";
        String p2 = "";
        String p1 = "";
        String p0 = "";
        String mZf = "";
        String minuteLineStr = "";
        if (minuteLine == null) {
            if (context.getkModel() != null) {
                mZf = context.getkModel().getRow().getStr("mtZf");
            }
        } else {
            MinuteLine minMinuteLine = minuteLine.getMin();
            double minPrice = KLineUtil.compareMaxSign(minMinuteLine.price, open);
            minuteLineStr = "" + minPrice;
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
        }


        double guben = GetGuben.retriveOrGetShizhi(AbsStragety.getCode(file), KlinePrev.getClose());
        String isWeekCrash = "" + KLineUtil.prevWeekline(KlinePrev, 1).isCrashDownMA120250();

        String space10 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace10(), 1), 4);
        String space30 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace30(), 1), 4);
        String space60 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace60(), 1), 4);

        String space250 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace250(), 1), 4);
        String space120 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace120(), 1), 4);

        String space1030 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace1030(), 1), 4);
        String space3060 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace3060(), 1), 4);
        String space30120 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace30120(), 1), 4);
        String space30250 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace30250(), 1), 4);
        String space60120 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace60120(), 1), 4);
        String space60250 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace60250(), 1), 4);

        String space120250 = StringUtil.spaceString(StringUtil.format(KlinePrev.getSpace120250(), 1), 4);

        String space250W = StringUtil.spaceString(StringUtil.format(KlinePrev.weekline.getSpace250(KlinePrev.getClose()), 1), 4);
        String space120W = StringUtil.spaceString(StringUtil.format(KlinePrev.weekline.getSpace120(KlinePrev.getClose()), 1), 4);

        String space250MOpen = StringUtil.spaceString(StringUtil.format(KlinePrev.monthKline.getSpace250(KlinePrev.monthKline.getOpen()), 1), 4);
        String space120MOpen = StringUtil.spaceString(StringUtil.format(KlinePrev.monthKline.getSpace120(KlinePrev.monthKline.getOpen()), 1), 4);

        String space250MCur = StringUtil.spaceString(StringUtil.format(KlinePrev.monthKline.getSpace250(KlinePrev.getClose()), 1), 4);
        String space120MCur = StringUtil.spaceString(StringUtil.format(KlinePrev.monthKline.getSpace120(KlinePrev.getClose()), 1), 4);

        double[] zfs = KLineUtil.prevWeekline(KlinePrev, 1).getPrevZhangFu(17);
        double[] zfms = KlinePrev.monthKline.prev().getPrevZhangFu(34);
        if (zfms == null) {
            zfms = KlinePrev.monthKline.prev().getPrevZhangFu(18);
        }

        if (zfs == null) {
            zfs = new double[]{1000, 1000};
        }
        String weekzf = StringUtil.format3(zfs[0], 5) + " " + StringUtil.format((int) zfs[1]);
        weekzf = weekzf.trim();
        if (!StringUtil.isNull(weekzf)) {
            String[] vs2 = weekzf.split(" ");
            double v1 = Double.parseDouble(vs2[0]);
            int v2 = Integer.parseInt(vs2[1]);
            if (v2 < 8 && v1 > 19) {
                weekzf = "" + vs2[0];
            } else {
                weekzf = "" + 0;
            }
        } else {
            weekzf = "" + 0;
        }


        String monzhf_ = "";
        int monthLen = 0;
        double meanMon = 0;
        if (zfms != null) {
            monthLen = (int) zfms[1];
            monzhf_ = StringUtil.format(zfms[0], 1);
            meanMon = zfms[0] / monthLen;
        }
        String minStr = "";
        double hand = KlinePrev.getHand(context.getTotalV());
        double handPrev = KlinePrev.prev().getHand(context.getTotalV());
        double handPrev2 = KlinePrev.prev(2).getHand(context.getTotalV());
        double hands[] = new double[]{handPrev2, handPrev, hand};

        double zt = KlinePrev.getClose() * 1.1f;
        List<Kline.StandResult> list = KlinePrev.monthKline.spaceUpTouchMA(zt);
        List<Kline.StandResult> listWeek = KlinePrev.weekline.spaceUpTouchMA(zt);
        List<Kline.StandResult> listDay = KlinePrev.spaceUpTouchMA(zt);
        List<Kline.StandResult> listMASpaceMonth = KlinePrev.monthKline.spaceDownTouchMA();
        List<Kline.StandResult> listMASpaceWeek = KlinePrev.weekline.spaceDownTouchMA();

        List<Kline.StandResult> listMASpaceMonth2 = KlinePrev.monthKline.spaceMinDownTouchMA();
        List<Kline.StandResult> listMASpaceWeek2 = KlinePrev.weekline.spaceMinDownTouchMA();
        List<Kline.StandResult> listMASpaceDay2 = KlinePrev.spaceMinDownTouchMA();

        List<Kline.StandResult> listMASpaceMonth2_ = KlinePrev.monthKline.spaceMinUpTouchMA();
        List<Kline.StandResult> listMASpaceWeek2_ = KlinePrev.weekline.spaceMinUpTouchMA();
        List<Kline.StandResult> listMASpaceDay2_ = KlinePrev.spaceMinUpTouchMA();

        if (list.size() >= 2) {
            int a = 0;
        }

        String type = IsZhangtingAI.class.getSimpleName();
        String zfStr = "";
        if (Kline != null) {
            if (minuteLine != null) {
                MinuteLine lastMinuteLine = minuteLine.allLineList.get(minuteLine.allLineList.size() - 1);
                double frac = KLineUtil.compareMaxSign(lastMinuteLine.price, KlinePrev.getClose());
                zfStr = "" + frac;
            } else {
                zfStr = "" + Kline.getZhangfu();
            }
        }
        double zhenfu = KlinePrev.getZhenfu();
        String gubenStr = StringUtil.spaceString(StringUtil.format3(guben, 0), 3);
        String space250Str = "" + space250;
        String weekzfStr = "" + weekzf;
        //最小minStr
        String min = StringUtil.spaceString(minStr, 5);
        String chgHands = StringUtil.formatVs(hands);
        String prevmon1 = KlinePrev.monthKline.prev(1).getZhangfuStr2();
        String prevmon2 = KlinePrev.monthKline.prev(2).getZhangfuStr2();
        if (file.contains("600691")) {
            int a = 0;
        }

        String monTotal = StringUtil.spaceString(StringUtil.format(KlinePrev.monthKline.prev(1).getZhangfu2() + KlinePrev.monthKline.prev(2).getZhangfu2(), 1), 5);

        String prevweek1 = KLineUtil.prevWeekline(KlinePrev, 1).getZhangfuStr2();
        String prevweek2 = KLineUtil.prevWeekline(KlinePrev, 2).getZhangfuStr2();
        String weekTotal = StringUtil.spaceString(StringUtil.format(KLineUtil.prevWeekline(KlinePrev, 1).getZhangfu2() + KLineUtil.prevWeekline(KlinePrev, 2).getZhangfu2(), 1), 5);
        double minAll = Integer.MAX_VALUE;
        double minWeek = Integer.MAX_VALUE;
        double minMon = Integer.MAX_VALUE;
        if (Math.abs(KlinePrev.monthKline.prev(1).getZhangfu2()) < Math.abs(minMon)) {
            minMon = KlinePrev.monthKline.prev(1).getZhangfu2();
        }
        if (Math.abs(KlinePrev.monthKline.prev(1).getZhangfu2() + KlinePrev.monthKline.prev(2).getZhangfu2()) < Math.abs(minMon)) {
            minMon = KlinePrev.monthKline.prev(1).getZhangfu2() + KlinePrev.monthKline.prev(2).getZhangfu2();
        }

        if (Math.abs(KLineUtil.prevWeekline(KlinePrev, 1).getZhangfu2()) < Math.abs(minWeek)) {
            minWeek = KLineUtil.prevWeekline(KlinePrev, 1).getZhangfu2();
        }
        if (Math.abs(KLineUtil.prevWeekline(KlinePrev, 2).getZhangfu2()) < Math.abs(minWeek)) {
            minWeek = KLineUtil.prevWeekline(KlinePrev, 2).getZhangfu2();
        }
        if (Math.abs(KLineUtil.prevWeekline(KlinePrev, 1).getZhangfu2() + KLineUtil.prevWeekline(KlinePrev, 2).getZhangfu2()) < Math.abs(minWeek)) {
            minWeek = KLineUtil.prevWeekline(KlinePrev, 1).getZhangfu2() + KLineUtil.prevWeekline(KlinePrev, 2).getZhangfu2();
        }

        if (Math.abs(minWeek) < Math.abs(minAll)) {
            minAll = minWeek;
        }
        if (Math.abs(minMon) < Math.abs(minAll)) {
            minAll = minMon;
        }

        String isCross = "" + KlinePrev.isCross();

        boolean isGongDown = KlinePrev.monthKline.isGongDownToMAAndUpMAPrevent();
        Row row = new Row();
        row.add(new Col(INFO));
        row.add(new Col(GetBankuai.get(getCode(file))));
        row.add(new Col(prevDate));
        row.add(new Col(date));
        row.add(new Col("" + (minuteLine != null ? minuteLine.getTime() : "")));
        row.add(new Col(""));
        row.add(new Col(type));
        row.add(new Col(zfStr));
        row.add(new Col("" + zhenfu));

        row.add(new Col("" + KlinePrev.monthKline.prev().getZhenfu()));
        row.add(new Col("" + KlinePrev.monthKline.prev(2).getZhenfu()));

        com.alading.tool.stock.Kline.SortEntity[] aobjs = KlinePrev.getDZNum(0);
        row.add(new Col("" + aobjs[0].v));
        row.add(new Col("" + aobjs[0].num));
        row.add(new Col("" + aobjs[1].v));
        row.add(new Col("" + aobjs[1].num));
        row.add(new Col("" + aobjs[2].v));
        row.add(new Col("" + aobjs[2].num));
        row.add(new Col(gubenStr));


        row.add(new Col("" + KlinePrev.isCrashDownMA(3)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 1).isCrashDownMA(3)));
        row.add(new Col("" + KlinePrev.monthKline.prev().isCrashDownMA(3)));

        row.add(new Col("" + KlinePrev.isSDLine(6)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 1).isSDLine(5)));
        row.add(new Col("" + KlinePrev.monthKline.prev().isSDLine(3)));

        row.add(new Col(isWeekCrash));

        //gap250	gap120	gap60	gap30 gap10
        row.add(new Col(space250Str));
        row.add(new Col(space120));
        row.add(new Col(space60));
        row.add(new Col(space30));//gap30
        row.add(new Col(space10));//gap10

        double price = KlinePrev.close;
        row.add(new Col("" + KlinePrev.getPrevKR(price, 250, 5)));//k250
        row.add(new Col("" + KlinePrev.getPrevKR(price, 120, 5)));//
        row.add(new Col("" + KlinePrev.getPrevKR(price, 60, 5)));//
        row.add(new Col("" + KlinePrev.getPrevKR(price, 30, 5)));//


//        w30d60	w30d120	w30d250	w60d120	w60d250	w120d250
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 1).getWDeadCrossNum(30, 60)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 1).getWDeadCrossNum(30, 120)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 1).getWDeadCrossNum(30, 250)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 1).getWDeadCrossNum(60, 120)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 1).getWDeadCrossNum(60, 250)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 1).getWDeadCrossNum(120, 250)));

        //30d60	30d120	30d250	60d120	60d250	120d250
        row.add(new Col("" + KlinePrev.getDeadCrossNum(10, 30)));
        row.add(new Col("" + KlinePrev.getDeadCrossNum(10, 60)));
        row.add(new Col("" + KlinePrev.getDeadCrossNum(30, 60)));
        row.add(new Col("" + KlinePrev.getDeadCrossNum(30, 120)));
        row.add(new Col("" + KlinePrev.getDeadCrossNum(30, 250)));
        row.add(new Col("" + KlinePrev.getDeadCrossNum(60, 120)));
        row.add(new Col("" + KlinePrev.getDeadCrossNum(60, 250)));
        row.add(new Col("" + KlinePrev.getDeadCrossNum(120, 250)));


        row.add(new Col("" + KlinePrev.getGoldCrossNum(10, 30)));
        row.add(new Col("" + KlinePrev.getGoldCrossNum(10, 60)));
        row.add(new Col("" + KlinePrev.getGoldCrossNum(30, 60)));
        row.add(new Col("" + KlinePrev.getGoldCrossNum(30, 120)));
        row.add(new Col("" + KlinePrev.getGoldCrossNum(30, 250)));
        row.add(new Col("" + KlinePrev.getGoldCrossNum(60, 120)));
        row.add(new Col("" + KlinePrev.getGoldCrossNum(60, 250)));
        row.add(new Col("" + KlinePrev.getGoldCrossNum(120, 250)));


        //60金叉120数	120金叉250数
        row.add(new Col(""));
        row.add(new Col(""));

        row.add(new Col("" + KlinePrev.prev(1).isGuailiChange(30, 60)));
        row.add(new Col("" + KlinePrev.prev(1).isGuailiChange(30, 120)));
        row.add(new Col("" + KlinePrev.prev(1).isGuailiChange(30, 250)));
        row.add(new Col("" + KlinePrev.prev(1).isGuailiChange(60, 120)));
        row.add(new Col("" + KlinePrev.prev(1).isGuailiChange(60, 250)));
        row.add(new Col("" + KlinePrev.prev(1).isGuailiChange(120, 250)));

        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 2).isGuailiChange(30, 60)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 2).isGuailiChange(30, 120)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 2).isGuailiChange(30, 250)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 2).isGuailiChange(60, 120)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 2).isGuailiChange(60, 250)));
        row.add(new Col("" + KLineUtil.prevWeekline(KlinePrev, 2).isGuailiChange(120, 250)));

        //gap1030 gap3060	gap30120	gap30250	gap60120	gap60250
        row.add(new Col(space1030));
        row.add(new Col(space3060));
        row.add(new Col(space30120));
        row.add(new Col(space30250));
        row.add(new Col(space60120));
        row.add(new Col(space60250));

        //gap250W	gap120W	gap250MOpen	gap120MOpen	gap250MCur	gap120MCur
        row.add(new Col(space250W));
        row.add(new Col(space120W));
        row.add(new Col(space250MOpen));
        row.add(new Col(space120MOpen));
        row.add(new Col(space250MCur));
        row.add(new Col(space120MCur));

        //gap120(9)	gap120(8)	gap120(7)	gap120(6)	gap120(5)	gap120(4)	gap120(3)	gap120(2)	gap120(1)	gap120(0)
        for (int i = 9; i >= 0; i--) {
            String frac = StringUtil.format2_(KLineUtil.compareMaxSign(KlinePrev.prev(i * 2 + 1).close, KlinePrev.prev(i * 2 + 1).getMA120()));
            row.add(new Col(frac));
        }

        // gap250(9)	gap250(8)	gap250(7)	gap250(6)	gap250(5)	gap250(4)	gap250(3)	gap250(2)	gap250(1)	gap250(0)
        for (int i = 9; i >= 0; i--) {
            String frac = StringUtil.format2_(KLineUtil.compareMaxSign(KlinePrev.prev(i * 2 + 1).close, KlinePrev.prev(i * 2 + 1).getMA250()));
            row.add(new Col(frac));
        }

        Object[] ret = KlinePrev.getMinPointMA(250);
        //maxPointMA250_	minPointMA250_
        row.add(new Col("" + Integer.parseInt("" + ret[2]) * 2));
        row.add(new Col("" + ret[0]));
        //maxPointMA250	minPointMA250
        row.add(new Col("" + ret[3]));
        row.add(new Col("" + ret[1]));


        //zhenf(9)	zhenf(8)	zhenf(7)	zhenf(6)	zhenf(5)	zhenf(4)	zhenf(3)	zhenf(2)	zhenf(1)	zhenf(0)
        row.add(new Col(StringUtil.format(KlinePrev.prev(9).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(8).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(7).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(6).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(5).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(4).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(3).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(2).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(1).getZhenfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(0).getZhenfu())));

        //guaili price
        //price_(9)	price_(8)	price_(7)	price_(6)	price_(5)	price_(4)	price_(3)	price_(2)	price_(1)	price_(0)
        double minPriceGuaili = 999;
        for (int i = 9; i >= 0; i--) {
            String aret = KlinePrev.prev(i).getGuaili(4);
            double ret2 = Double.parseDouble(aret);
            if (i <= 4 && minPriceGuaili > ret2) {
                minPriceGuaili = ret2;
            }
            row.add(new Col(StringUtil.format(KlinePrev.prev(i).getClose())));
        }

        for (int i = 9; i >= 0; i--) {
            row.add(new Col(KlinePrev.prev(i).getGuaili(4)));
        }

        //gold3060	gold30120	gold30250	gold60120	gold60250	gold120250
        row.add(new Col("" + kline0Prev.prev().isGoldOrComingGold(4, 30, 60)));
        row.add(new Col("" + kline0Prev.prev().isGoldOrComingGold(4, 30, 120)));
        row.add(new Col("" + kline0Prev.prev().isGoldOrComingGold(4, 30, 250)));
        row.add(new Col("" + kline0Prev.prev().isGoldOrComingGold(4, 60, 120)));
        row.add(new Col("" + kline0Prev.prev().isGoldOrComingGold(4, 60, 250)));
        row.add(new Col("" + kline0Prev.prev().isGoldOrComingGold(4, 120, 250)));

        //daytouch250	daytouch120	10dayma250	10dayma120
        int offset250 = kline0Prev.dayTouchMA250(20, 250);
        int offset120 = kline0Prev.dayTouchMA250(15, 120);
        row.add(new Col("" + offset250));
        row.add(new Col("" + offset120));
        row.add(new Col(""));
        row.add(new Col(""));

        row.add(new Col("" + StringUtil.format2_(minPriceGuaili)));

        //prev(9)	prev(8)	prev(7)	prev(6)	prev(5)	prev(4)	prev(3)	prev(2)	prev(1)	cur
        row.add(new Col(StringUtil.format(KlinePrev.prev(9).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(8).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(7).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(6).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(5).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(4).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(3).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(2).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(1).getZhangfu())));
        row.add(new Col(StringUtil.format(KlinePrev.prev(0).getZhangfu())));


        //wprev_(4)	wprev_(3)	wprev_(2)	wprev_(1)	wcur_	wprev(4)	wprev(3)	wprev(2)	wprev(1)	wcur
        double minw = 999;
        for (int IDX = 5; IDX >= 1; IDX--) {
            String wps = StringUtil.format(KLineUtil.prevWeekline(KlinePrev, IDX).getZhangfu2()) + "/" + StringUtil.format(KLineUtil.prevWeekline(KlinePrev, IDX).getChangeHand(context));
            String wpss[] = wps.split("/");
            row.add(new Col("" + wpss[1]));
        }

        for (int IDX = 5; IDX >= 1; IDX--) {
            String wps = StringUtil.format(KLineUtil.prevWeekline(KlinePrev, IDX).getZhangfu2()) + "/" + StringUtil.format(KLineUtil.prevWeekline(KlinePrev, IDX).getChangeHand(context));
            String wpss[] = wps.split("/");
            double ttt = Math.abs(Double.parseDouble(wpss[0]));
            if (Math.abs(minw) >= ttt) {
                minw = ttt;
            }
            row.add(new Col("" + wpss[0]));
        }

        row.add(new Col("" + minw));
        Object[] jumps = KlinePrev.getJumpIndays(10, false);
        Object[] jumps2 = KlinePrev.getJumpIndays(10, true);
        row.add(new Col(StringUtil.format((Double) jumps[0])));
        row.add(new Col("" + jumps[1]));
        row.add(new Col("" + jumps[5]));
        row.add(new Col("" + jumps[2]));
        row.add(new Col("" + StringUtil.format((Double) jumps[3])));

        row.add(new Col(StringUtil.format((Double) jumps2[0])));
        row.add(new Col("" + jumps2[1]));
        row.add(new Col("" + jumps2[2]));

        Object[] horFlag = KlinePrev.isJumpHorMAIs(10, false);
        Object[] horFlag2 = KlinePrev.isJumpHorMAIs2(10, false);
        row.add(new Col("" + horFlag[0]));
        row.add(new Col("" + horFlag[1]));
        row.add(new Col("" + StringUtil.format((Double) horFlag[2])));

        row.add(new Col("" + horFlag2[0]));
        row.add(new Col("" + horFlag2[1]));
        row.add(new Col("" + StringUtil.format((Double) horFlag2[2])));

        //放量下跌	上下压力	周涨幅	月长度	月涨幅	月均涨	分时最小	上上周涨幅	上周涨幅	两周涨幅	上上月涨幅	上月涨幅
        row.add(new Col(firstSpeedDown));
        row.add(new Col("" + isGongDown));
        row.add(new Col(weekzfStr));
        row.add(new Col("" + monthLen));
        row.add(new Col(monzhf_));
//        月均涨
        row.add(new Col("" + meanMon));

        row.add(new Col(minuteLineStr));
        row.add(new Col(prevweek2));
        row.add(new Col(prevweek1));
        row.add(new Col(weekTotal));
        row.add(new Col(prevmon2));
        row.add(new Col(prevmon1));
        row.add(new Col("" + KlinePrev.monthKline.prev(2).getChangeHand(context)));
        row.add(new Col("" + KlinePrev.monthKline.prev().getChangeHand(context)));
        row.add(new Col("" + KlinePrev.monthKline.prev(2).isShadownUp(60)));
        row.add(new Col("" + KlinePrev.monthKline.prev().isShadownUp(60)));
        row.add(new Col(monTotal));
        row.add(new Col(format(minWeek)));
        row.add(new Col(format(minMon)));
        row.add(new Col(format(minAll)));


        row.add(new Col(isCross));

        //mtP3	mtP2	mtP1	mtP0	mtZf
        row.add(new Col(p3));
        row.add(new Col(p2));
        row.add(new Col(p1));
        row.add(new Col(p0));
        row.add(new Col(mZf));

        //换手2	换手1	换手0
        String p3s[] = chgHands.replace("[", "").replace("]", "").split(",");
        row.add(new Col(p3s[0]));
        row.add(new Col(p3s[1]));
        row.add(new Col(p3s[2]));
//        row.add(new Col(chgHands));

        //月压力数
        if (StringUtil.isSameStandResult(list)) {
            row.add(new Col("" + 1));
        } else {
            row.add(new Col("" + list.size()));
        }
        {
            String yueyali = StringUtil.formatVs2(list);
            if (StringUtil.isNull(yueyali)) {
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
            } else {
                Map<String, PeriodPressure> yalis = new HashMap<>();
                yalis.put("30", new PeriodPressure());
                yalis.put("60", new PeriodPressure());
                yalis.put("120", new PeriodPressure());
                yalis.put("250", new PeriodPressure());
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
//        row.add(new Col(StringUtil.formatVs2(list)));

        //            周压力30	周压力60	周压力120	周压力250
        row.add(new Col("" + listWeek.size()));
        {
            String yueyali = StringUtil.formatVs2(listWeek);
            if (StringUtil.isNull(yueyali)) {
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
            } else {
                Map<String, PeriodPressure> yalis = new HashMap<>();
                yalis.put("30", new PeriodPressure());
                yalis.put("60", new PeriodPressure());
                yalis.put("120", new PeriodPressure());
                yalis.put("250", new PeriodPressure());
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
//        row.add(new Col(StringUtil.formatVs2(listWeek)));

        //日压力数	日压力30	 日压力60	日压力120	日压力250	日压力
        row.add(new Col("" + listDay.size()));
        {
            String yueyali = StringUtil.formatVs2(listDay);
            if (StringUtil.isNull(yueyali)) {
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
                row.add(new Col(SelectAddColExcel.defalutValue));
            } else {
                Map<String, PeriodPressure> yalis = new HashMap<>();
                yalis.put("30", new PeriodPressure());
                yalis.put("60", new PeriodPressure());
                yalis.put("120", new PeriodPressure());
                yalis.put("250", new PeriodPressure());
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
//        row.add(new Col(StringUtil.formatVs2(listDay)));


        row.add(new Col("" + listMASpaceMonth.size()));
        {
            String yueyali = StringUtil.formatVs2(listMASpaceMonth);
            if (StringUtil.isNull(yueyali)) {
                row.add(new Col(SelectAddColExcel.defalutValue_null));
                row.add(new Col(SelectAddColExcel.defalutValue_null));
                row.add(new Col(SelectAddColExcel.defalutValue_null));
                row.add(new Col(SelectAddColExcel.defalutValue_null));
            } else {
                Map<String, PeriodPressure> yalis = new HashMap<>();
                yalis.put("30", new PeriodPressure(SelectAddColExcel.defalutValue_null));
                yalis.put("60", new PeriodPressure(SelectAddColExcel.defalutValue_null));
                yalis.put("120", new PeriodPressure(SelectAddColExcel.defalutValue_null));
                yalis.put("250", new PeriodPressure(SelectAddColExcel.defalutValue_null));
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
//        row.add(new Col(StringUtil.formatVs2(listMASpaceMonth)));

        row.add(new Col("" + listMASpaceWeek.size()));
        {
            String yueyali = StringUtil.formatVs2(listMASpaceWeek);
            if (StringUtil.isNull(yueyali)) {
                row.add(new Col(SelectAddColExcel.defalutValue_null));
                row.add(new Col(SelectAddColExcel.defalutValue_null));
                row.add(new Col(SelectAddColExcel.defalutValue_null));
                row.add(new Col(SelectAddColExcel.defalutValue_null));
            } else {
                Map<String, PeriodPressure> yalis = new HashMap<>();
                yalis.put("30", new PeriodPressure(SelectAddColExcel.defalutValue_null));
                yalis.put("60", new PeriodPressure(SelectAddColExcel.defalutValue_null));
                yalis.put("120", new PeriodPressure(SelectAddColExcel.defalutValue_null));
                yalis.put("250", new PeriodPressure(SelectAddColExcel.defalutValue_null));
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
//        row.add(new Col(StringUtil.formatVs2(listMASpaceWeek)));

        //月MINMA支撑30	月MINMA支撑60	月MINMA支撑120	月MINMA支撑250	月MINMA支撑
        ff(row, StringUtil.formatVs2(listMASpaceMonth2));
        ff(row, StringUtil.formatVs2(listMASpaceWeek2));
        ff(row, StringUtil.formatVs2(listMASpaceDay2));
        ff(row, StringUtil.formatVs2(listMASpaceMonth2_));
        ff(row, StringUtil.formatVs2(listMASpaceWeek2_));
        ff(row, StringUtil.formatVs2(listMASpaceDay2_));


        //50日跌幅	avgk120	avgk250	10hor3day 20hor4day	20hor5day	20hor6day  10hor3day 20hor4day	20hor5day	20hor6day
        row.add(new Col("" + kline0Prev.prev().getPrevDF(50)));
        row.add(new Col("" + kline0Prev.getPrevK_(120, 5)));
        row.add(new Col("" + kline0Prev.getPrevK_(250, 5)));

        row.add(new Col("" + kline0Prev.isHorNum(10, 3)));
        row.add(new Col("" + kline0Prev.isHorNum(20, 4)));
        row.add(new Col("" + kline0Prev.isHorNum(20, 5)));
        row.add(new Col("" + kline0Prev.isHorNum(20, 6)));

        row.add(new Col("" + kline0Prev.isHorNumFraction(10, 3)));
        row.add(new Col("" + kline0Prev.isHorNumFraction(20, 4)));
        row.add(new Col("" + kline0Prev.isHorNumFraction(20, 5)));
        row.add(new Col("" + kline0Prev.isHorNumFraction(20, 6)));


        com.alading.tool.stock.Kline.LocalBottom localBottom = KlinePrev.getLocalBottom();
        row.add(new Col("" + localBottom.flag));
        row.add(new Col("" + localBottom.num));
        row.add(new Col(StringUtil.format(localBottom.frac)));

        com.alading.tool.stock.Kline.LocalBottom localWBottom1 = KlinePrev.getWLocalBottomW(2.6f, 10);
        row.add(new Col("" + localWBottom1.flag));
        row.add(new Col("" + localWBottom1.num));
        row.add(new Col(StringUtil.format(localWBottom1.frac)));

        com.alading.tool.stock.Kline.LocalBottom localWBottom = KlinePrev.getWLocalBottomWBZ(3f, 10);
        row.add(new Col("" + localWBottom.flag));
        row.add(new Col("" + localWBottom.num));
        row.add(new Col(StringUtil.format(localWBottom.frac)));

        int specialHor = filterRowSpecialHorAfter(kline0Prev);

//        context.add("specialHor", specialHor);
//        SingleContext singleContext = getContext(file, date);
//        context.setStockAllMinuteLine(singleContext.getStockAllMinuteLine(date, false, 4));
        //prev0分时	prev1分时	prev2分时	prev3分时	prev4分时
        LineReport lineReport = null;
        if (StragetyBottom.useReport) {
            lineReport = IsBottom.report(file, KlinePrev, context, context.isRealBottomFlag());
        }
        if (lineReport != null) {
            List<Integer> vlist = lineReport.getSeries(0);
            for (int i = 0; i < 5; i++) {
                row.add(new Col("" + vlist.get(i)));
            }
            //加速天序号	加速天量	减速天序号	减速天量
            Object[] objs = lineReport.isFirstSpeedUpWithVOLInDays(context);
            Object[] objs2 = lineReport.isFirstSpeedDownWithVOLInDays(context);
            row.add(new Col("" + objs[0]));
            row.add(new Col("" + objs[1]));
            row.add(new Col("" + objs2[0]));
            row.add(new Col("" + objs2[1]));
//            lineReport.clear();
        } else {
            for (int i = 0; i < 5; i++) {
                row.add(new Col("" + -1));
            }
            row.add(new Col("" + -1));
            row.add(new Col("" + -1));
            row.add(new Col("" + -1));
            row.add(new Col("" + -1));
        }


        if (minuteLine != null) {
            row.add(new Col("" + minuteLine.allLineList.get(0).getZF(kline0Prev)));
        } else {
            if (context.getkModel() != null && context.getkModel().getRow() != null) {
                context.getkModel().getRow().getTable().initIndex();
                double fistMinute = getFloat(context, "fistMinute");
                row.add(new Col("" + fistMinute));
            } else {
                row.add(new Col(""));
            }
        }

        row.add(new Col("" + context.getType05()));
        row.add(new Col("" + context.getLft()));
        row.add(new Col("" + context.getRgt()));
        row.add(new Col("" + StringUtil.format(kline0Prev.getMA120())));
        //是否跳空
        row.add(new Col("0"));
        if (minuteLine != null) {
            row.add(new Col("" + minuteLine.prev(2).getVol()));
            row.add(new Col("" + minuteLine.prev(1).getVol()));
            row.add(new Col("" + minuteLine.getVol()));
        } else {
            row.add(new Col("-1"));
            row.add(new Col("-1"));
            row.add(new Col("-1"));
        }


        row.add(new Col("" + specialHor));
        if (minuteLine != null) {
            double cur = KLineUtil.compareMaxSign(minuteLine.price, open);
            row.add(new Col("" + cur));
        } else {
            row.add(new Col("-99"));
        }

        context.add("specialHor", specialHor);
//        context.setStockAllMinuteLine(singleContext.getStockAllMinuteLine(date, false, 4));

        try {
            if (lineReport != null) {
                List<Integer> alist = lineReport.getSeries(0);
                row.add(new Col("" + alist.get(0)));
                row.add(new Col("" + alist.get(1)));
                row.add(new Col("" + alist.get(2)));
                row.add(new Col("" + alist.get(3)));
                row.add(new Col("" + alist.get(4)));
            } else {
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
            }
        } catch (Exception e) {
            row.add(new Col("" + -1));
            row.add(new Col("" + -1));
            row.add(new Col("" + -1));
            row.add(new Col("" + -1));
            row.add(new Col("" + -1));
//                e.printStackTrace();
        }

        //max
        if(Kline!= null) {
            double max  = Kline.getUpZhangfu();
            row.add(new Col("" + max));
        }else {
            row.add(new Col("" + -1));
        }

        //curMinute
        row.add(new Col("" + -1));
        try {
            if(StragetyBottom.step == 1) {
                SelectAddColExcel.monthZfAdd(kline0Prev, row);
                SelectAddColExcel.monthMa250Add(kline0Prev, row);
                SelectAddColExcel.monthMa120Add(kline0Prev, row);
                SelectAddColExcel.monthMa60Add(kline0Prev, row);
                row.add(new Col("" + -1));
            }else {
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                row.add(new Col("" + -1));
                //bottomUp
                row.add(new Col("" + -1));
            }
        }catch (Exception e) {
        }

        double prevDZ = kline0Prev.getPrevDZ(8);
        row.add(new Col(""+prevDZ));
        List<ScoreConcept> retConcept = ConceptDFCF.getList(AbsStragety.getCode(file));
        row.add(new Col(""+retConcept.size()));
        row.add(new Col(""+ConceptDFCF.getListStr(AbsStragety.getCode(file))));
        Kline minLine = kline0Prev.getMin(30);
        String minSpace250 = StringUtil.spaceString(StringUtil.format(minLine.getSpace250(), 1), 4);
        row.add(new Col(""+minSpace250));
        int prevDZOffset = kline0Prev.getPrevDZOffset(8);
        row.add(new Col(""+prevDZOffset));

        double upSpace = KLineUtil.compareMaxSign(kline0Prev.getClose(), minLine.min);
        row.add(new Col(""+upSpace));
        if (lineReport != null) {
            lineReport.clear();
        }
        if(kline0Prev.next(2)!=null) {
            row.add(new Col(""+kline0Prev.next(2).getZhangfu()));
        }else {
            row.add(new Col(""+1000));
        }
        if(kline0Prev.next(3)!=null) {
            row.add(new Col(""+kline0Prev.next(3).getZhangfu()));
        }else {
            row.add(new Col(""+1000));
        }
        if(kline0Prev.next(4)!=null) {
            row.add(new Col(""+kline0Prev.next(4).getZhangfu()));
        }else {
            row.add(new Col(""+1000));
        }
        if(kline0Prev.next(4)!=null) {
            row.add(new Col(""+KLineUtil.compareMaxSign(+kline0Prev.next(4).max, +kline0Prev.next(1).close)));
        }else {
            row.add(new Col(""+1000));
        }
        if (file.contains("600006")) {
            int a = 0;
        }

        List<Kline> lines = null;
        if(StragetyBottom.usecmf) {
            try {
                String code = getCode(file);
                if(code.equalsIgnoreCase("002340")) {
                    int a= 0;
                }
                lines = GetLInes.getFromNet(code);
                CYQCalculator.CYQData cyqData = Cmf.get(code, lines, prevDate);
                row.add(new Col(""+cyqData.percentChips.get("70").get("concentration")));
                row.add(new Col(""+cyqData.percentChips.get("90").get("concentration")));
                row.add(new Col(""+cyqData.fracZt));
                row.add(new Col(""+cyqData.fracZt));
                row.add(new Col(""+cyqData.fracZt2));
                row.add(new Col(""+cyqData.fracProfit));
                row.add(new Col(""+cyqData.fracTrapInProfit));
                row.add(new Col(""+cyqData.fracTrapInZtUp));
                row.add(new Col(""+cyqData.minLine.get("minLine")));
                row.add(new Col(""+cyqData.minLine.get("priceZf")));
            } catch (IOException e) {
                e.printStackTrace();

                row.add(new Col(""+0));
                row.add(new Col(""+0));
                row.add(new Col(""+0));
                row.add(new Col(""+0));
                row.add(new Col(""+0));
                row.add(new Col(""+0));
                row.add(new Col(""+0));
                row.add(new Col(""+0));
                row.add(new Col(""+0));
                row.add(new Col(""+0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            row.add(new Col(""+0));
            row.add(new Col(""+0));
            row.add(new Col(""+0));
            row.add(new Col(""+0));
            row.add(new Col(""+0));
            row.add(new Col(""+0));
            row.add(new Col(""+0));
            row.add(new Col(""+0));
            row.add(new Col(""+0));
            row.add(new Col(""+0));
        }





//        singleContext.clear();
        return row;
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

    public static void sendMsg(Row row) {
//        try {
//            MsgSend.sendMsg(row);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }
    public static void ok(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, Weekline weekline, Kline nextN, String msg, LineContext context) {
        SingleContext.add(file);
        if (INFO.length() == 11) {
            INFO = INFO + "  ";
        }

        Row aRow = context.getkModel().getRow();
        aRow.getTable().initIndex();
        int rgt = (int) aRow.getInt("股本");
        if(rgt>150) {
            return;
        }
        double open = kline0.prev().getOpen();
        double mtzf = IsZhangtingAI.getMtZF(open, minuteLine);
        boolean ret = true;
        double firstMinuteZF = minuteLine.allLineList.get(0).getZF(kline0.prev());
        if (firstMinuteZF <= -0.8f) {
            ret = false;
        }

        double zf = minuteLine.getZF(kline0.prev());
        if (zf < StragetyZTBottom.pulsezf) {
            ret = false;
        }
        if(!minuteLine.hasMax(kline0.prev(), StragetyZTBottom.maxzf)) {
            ret = false;
        }
        if(!ret) {
            return;
        }


        if (ret) {
            if (mtzf > minZF) {
                Row row = IsZhangtingAI.getRowZT(file, INFO, date, kline0.prev(), minuteLine, context);
                boolean fflag = IsBottom.catchFlag(table, row);
                if(!fflag) {

                    if(StragetyZTBottom.useTree) {
                        Table table = getTable(file, INFO, date, kline0.prev(), minuteLine, context);
                        table.initIndex();
                        boolean ztFlag = DecisionZT.judge(table, 1);
                        if(!ztFlag) {
                            return;
                        }
                    }
                    sendMsg(row);
                    table.add(row);
                    Log.logString(StragetyBottom.resultBuffer, IsBottom.getINFO(INFO) + " " + date + " " + minuteLine.getTime());
                }

            } else if (mtzf > 0.9 && zf > 2) {
                Row row = IsZhangtingAI.getRowZT(file, INFO, date, kline0.prev(), minuteLine, context);
                boolean fflag = IsBottom.catchFlag(table, row);
                if(!fflag) {
                    if(StragetyZTBottom.useTree) {
                        Table table = getTable(file, INFO, date, kline0.prev(), minuteLine, context);
                        table.initIndex();
                        boolean ztFlag = DecisionZT.judge(table, 1);
                        if(!ztFlag) {
                            return;
                        }
                    }
                    sendMsg(row);
                    table.add(row);
                    Log.logString(StragetyBottom.resultBuffer, IsBottom.getINFO(INFO) + " " + date + " " + minuteLine.getTime());
                }
            }
        }
    }

    public static Table getTable(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, LineContext context) {
        Row row = IsZhangtingAI.getRowZT(file, INFO, date, kline0, minuteLine, context);
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
                        ok(file, context.getInfo(), date, kline, minuteLine, null, kline, getSpeedUp(kline, minuteLine) + " isStandMA250:" + kline.isStandMA250() + " vol:" + context.getLastMinuteLine().getVolStr(), context);
                    } catch (Exception e) {
                        com.alading.util.Log.log("ERR:" + context.getInfo());
                    }
                }
            }
        }

        MinuteLine minuteLine = realTimeFilter(file, date, kline, context);
        if (minuteLine != null) {
            double retFrac = KLineUtil.compareMaxSign(minuteLine.price, kline.prev().close);
            if (retFrac > 3.6) {
//                return;
            }
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
                    ok(file, context.getInfo(), date, kline, minuteLine, null, kline, getSpeedUp(kline, minuteLine) + " isStandMA250:" + kline.isStandMA250() + " vol:" + context.getLastMinuteLine().getVolStr(), context);
                }
            } else {
                context.setLastMinuteLine(minuteLine);
                ok(file, context.getInfo(), date, kline, minuteLine, null, kline, "" + minuteLine.getTime() + "  " + " totalF:" + minuteLine.getMsg() + " isStandMA250:" + kline.isStandMA250() + " vol:" + context.getLastMinuteLine().getVolStr(), context);
            }
        }
    }

    public static MinuteLine realTimeFilter(String file, String date, Kline kline, LineContext context) {
        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
        MinuteLine minuteLine = StockAllMinuteLine.getFirstSpeedUp(kline, stockAllMinuteLine, getCode(file), date, context);
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
