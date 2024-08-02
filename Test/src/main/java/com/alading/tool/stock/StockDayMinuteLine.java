package com.alading.tool.stock;

import com.alading.model.GlobalData;
import com.alading.model.Stock;
import com.alading.tool.stock.model.MinuteModel;
import com.alading.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alading.tool.stock.IsZhangting.filterZF;

public class StockDayMinuteLine {
    private String date;
    private Kline kline;

    private Map<String, LineType> lineMap = new HashMap<>();
    private List<LineType> lineTypeList = new ArrayList<>();
    public List<MinuteLine> allLineList = new ArrayList();

    public double getFitstZF(Kline kline) {
        return allLineList.get(0).getZF(kline);
    }

    private LineType currentLineType;


    public double total;
    public int num;

    public LineType getLineType(int miunteLineIdx) {
        for (LineType lineType : lineTypeList) {
            MinuteLine minuteLine = lineType.get(miunteLineIdx);
            if (minuteLine != null) {
                return lineType;
            }
        }
        return null;
    }

    public LineType getLineType(String miunteTime) {
        for (LineType lineType : lineTypeList) {
            MinuteLine minuteLine = lineType.get(miunteTime);
            if (minuteLine != null) {
                return lineType;
            }
        }
        return null;
    }

    public void log() {
        for (LineType lineType : lineTypeList) {
            Log.log(lineType.toString());
        }
    }

    public void logRightAngle() {
        StringBuffer stringBuffer = new StringBuffer();
        for (LineType lineType : lineTypeList) {
            String info = lineType.getInfo(78, 116);
            if (StringUtil.isNull(info)) {
                continue;
            }
            stringBuffer.append(info + "\r\n");
        }
        if (StringUtil.isNull(stringBuffer.toString())) {
            return;
        }
        Log.log("--->" + kline.getDate());
        Log.log(stringBuffer.toString());
    }

    public TypeResult filterRightAngle() {
        TypeResult typeResult = new TypeResult();
        for (LineType lineType : lineTypeList) {
            int vol = lineType.caculateRVol();
            if (lineType.getTime().equalsIgnoreCase(StragetyZTBottom.monitorMinute)) {
                int a = 0;
            }
//            Log.log("vol:"+lineType.getTime()+" "+vol);
            String info = lineType.getInfo(78, 116);
            if (StringUtil.isNull(info)) {
                continue;
            }
            typeResult.add(lineType);
        }
        return typeResult;
    }

    public void logTringle() {
        Grid grid = new Grid();
        grid.setLineTypeList(lineTypeList);
        grid.kline = kline;
        grid.setPrev(kline.prev().getClose());
        grid.prsTringle("0930", "0941");
        grid.logTringle();
        Log.log("-------------\r\n");
    }

    public int getNum2(String fromStr, String toStr, int min) {
        int total = 0;
        for (int i = 0; i <= lineTypeList.size(); i++) {
            LineType lineType = lineTypeList.get(i);
            int level = lineType.getPriceLevel();
            if (!lineType.isBetween(fromStr, toStr)) {
                break;
            }
            if (!lineType.isEndUpAVG()) {
                continue;
            }

            if (level > min) {
                total++;
            }
        }
        return total;
    }

    public int getNum(String fromStr, String toStr, int min, boolean usABS) {
        int total = 0;
        for (int i = 0; i <= lineTypeList.size(); i++) {
            LineType lineType = lineTypeList.get(i);
            int level = lineType.getPriceLevel();
            if (!lineType.isBetween(fromStr, toStr)) {
                break;
            }
            if (usABS) {
                level = Math.abs(level);
            }
            if (level > min) {
                total++;
            }
        }
        return total;
    }

    public void add(MinuteLine minuteLine) {
        if (StringUtil.isNull(date)) {
            date = minuteLine.getDate();
        }
        minuteLine.setIdx(num);
        total += minuteLine.price;
        minuteLine.dayLineList = allLineList;

        num++;
        double avg = total / num;
        minuteLine.setAvgPrice(avg);
        allLineList.add(minuteLine);

        minuteLine.setAllLineList(allLineList);

        addLineType(kline, minuteLine);
    }


    public int isSameDate(MinuteLine minuteLine) {
        if (StringUtil.isNull(date)) {
            return -1;
        }
        if (StringUtil.eq(date, minuteLine.getDate())) {
            return 1;
        }
        return 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    /**
     * 30 60 120 250
     */
    public static int hasNoPreventLine(Kline prev, Kline kline, double[] gaps) {
        boolean flag = false;
        double min = 9999;
        int idx = -1;
        if (Math.abs(gaps[0]) < min) {
            idx = 0;
            min = gaps[0];
        }
        if (Math.abs(gaps[1]) < min) {
            idx = 1;
            min = gaps[1];
        }
        if (Math.abs(gaps[2]) < min) {
            idx = 2;
            min = gaps[2];
        }
        if (Math.abs(gaps[3]) < min) {
            idx = 3;
            min = gaps[3];
        }
        if (idx == 0) {
            return idx;
        }
        if (idx == 1) {
            if (gaps[0] > 8.7) {
                return idx;
            }
        }
        if (idx == 2) {
            if (gaps[1] > 9.0) {
                return idx;
            }
            if (gaps[1] > 2 && gaps[1] < 7.5) {
                return -1;
            }
            return idx;
        }
        if (idx == 3) {
            if (gaps[2] > 9.0) {
                return idx;
            }
            if (gaps[2] > 2 && gaps[2] < 8.5) {
                return -1;
            }
            return idx;
        }
        return -1;
    }

    public ZTReason realTimeFilterFirstJump(Kline kline, LineContext context) {
        MinuteLine minuteLine = null;
        try {
            minuteLine = allLineList.get(0);
            //THS
//            if (minuteLine.getZF(kline.prev()) < 2.6) {
//                return null;
//            }
            if (minuteLine.getZF(kline.prev()) < 5) {
                return null;
            }
            ZTReason ztReason = new ZTReason();
            ztReason.minuteLine = minuteLine;
            ztReason.reason = "跳空高开:" + minuteLine.getZF(kline.prev());
            return ztReason;
        } catch (Exception e) {

        }
        return null;
    }

    public boolean isAllInGoldPoint(Kline kline, LineContext context) {
        MinuteLine minuteLine = null;
        boolean flag = false;
        for (int i = 0; i < num; i++) {
            minuteLine = allLineList.get(i);
            int a = 0;
            double zf = minuteLine.getZF(kline.prev());
            if (zf > 2.6) {
                flag = true;
                break;
            }
        }
        if (flag) {
            return true;
        }
        return false;
    }

    public ZTReason getFirstSpeedUp(Kline kline, LineContext context) {
        MinuteLine minuteLine = null;
        boolean flagGold = isAllInGoldPoint(kline, context);
        if (!flagGold) {
            return null;
        }
        for (int i = 0; i < num; i++) {
            try {
                int dlt = i;
                minuteLine = allLineList.get(i);
                if (minuteLine.getTime().equals(StragetyZTBottom.getTDX("09:33"))) {
                    int a = 0;
                }

//                同花顺<=5分钟内严格控制在3.0个点以内  通达信4分钟内
                if (dlt <= StragetyZTBottom.getDltTDX(5)) {
                    double zf = minuteLine.getZF(kline.prev());
                    if (zf > 2.8) {//002059 0401
                        return null;
                    }
                }


                MinuteModel minuteModel = new MinuteModel();
                minuteModel.kline = kline.prev();
                minuteModel.process(minuteLine);
                if (minuteLine.getTime().equals(StragetyZTBottom.monitorMinute)) {
                    int a = 0;
                }
                double open = kline.prev().getClose();
                double curV = KLineUtil.compareMaxSign(minuteLine.price, open);

//                if (curV < -1.4) {
//                    return null;
//                }
                if (curV < -2) {
                    return null;
                }

                Kline prev = kline.prev();
                String gap = "0:" + prev.getNextZTSpaceMAI(30)
                        + "   1:" + prev.getNextZTSpaceMAI(60)
                        + "   2:" + prev.getNextZTSpaceMAI(120) +
                        "    3:" + prev.getNextZTSpaceMAI(250);

                double[] gaps = new double[]{
                        prev.getNextZTSpaceMAI(30),
                        prev.getNextZTSpaceMAI(60),
                        prev.getNextZTSpaceMAI(120),
                        prev.getNextZTSpaceMAI(250)
                };

                int flag1 = hasNoPreventLine(prev, kline, gaps);
                if (flag1 == -1) {
                    Log.logErr("hasPreventLine:" + context.getInfo());
                    return null;
                }

                String gapStr = "";
                if (flag1 == 0) {
                    gapStr = "30:" + gaps[0];
                }
                if (flag1 == 1) {
                    gapStr = "30:" + gaps[0] + "    60:" + gaps[1];
                }
                if (flag1 == 2) {
                    gapStr = "60:" + gaps[1] + "    120:" + gaps[2];
                }
                if (flag1 == 3) {
                    gapStr = "120:" + gaps[2] + "    250:" + gaps[3];
                }

//                //9:33 ~ 09:40 curV < 1.5
//                if (dlt >= 3 && dlt <= 10) {
//                    if (curV > 1.5) {
//                        boolean gap60 = kline.isStandOnMA60();
//                        if (gap60) {
//                            if (prev.isNextZTSpace12(-1.5, 2)) {
//                                ZTReason ztReason = new ZTReason();
//                                ztReason.minuteLine = minuteLine;
//                                ztReason.reason = "站上60线, ZT严格 强 gap:" + gapStr;
//                                return ztReason;
//                            }
//                        }
//                        boolean gap30 = kline.isStandMA30();
//                        if (gap30) {
//                            if (prev.isNextZTSpace6(-1.5, 2)) {
//                                ZTReason ztReason = new ZTReason();
//                                ztReason.minuteLine = minuteLine;
//                                ztReason.reason = "站上30线, ZT严格 强 gap:" + gapStr;
//                                return ztReason;
//                            }
//                        }
//                        boolean gap10 = kline.isStandMA10();
//                        if (gap10) {
//                            if (prev.isNextZTSpace3(-1.5, 2)) {
//                                ZTReason ztReason = new ZTReason();
//                                ztReason.minuteLine = minuteLine;
//                                ztReason.reason = "站上10线, ZT严格 强 gap:" + gapStr;
//                                return ztReason;
//                            }
//                        }
//                        if (gap10) {
//                            if (prev.isNextZTSpace6(-1.5, 2)) {
//                                ZTReason ztReason = new ZTReason();
//                                ztReason.minuteLine = minuteLine;
//                                ztReason.reason = "站上10线, ZT严格 强 gap:" + gapStr;
//                                return ztReason;
//                            }
//                        }
//                        boolean isCross = kline.prev().isGoldOrComingGold(10, 30);
//                        if (isCross) {
//                            ZTReason ztReason = new ZTReason();
//                            ztReason.minuteLine = minuteLine;
//                            ztReason.reason = "金叉 强 gap:" + gapStr;
//                            return ztReason;
//                        }
//                        Log.logErr(context.getInfo() + " " + "9:33 ~ 09:40 curV < 1.5:");
//                        return null;
//                    }
//                }

                if (dlt >= StragetyZTBottom.getDltTDX(3)) {
                    if (curV > 2.6) {
                        ZTReason ztReason = new ZTReason();
                        ztReason.minuteLine = minuteLine;
                        ztReason.reason = "强 gap:" + gapStr;
                        return ztReason;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public MinuteLine getFirstSpeedDown(Kline kline) {
        MinuteLine minuteLine = null;
//        Log.d("getFirstSpeedUp:" + allLineList.get(num - 1));
        for (int i = 0; i < num; i++) {
            minuteLine = allLineList.get(i);
            if (minuteLine.getTime().equals("1303")) {
                int a = 0;
            }
            MinuteLine ret = minuteLine.getFirstSpeedDown(kline, 950, 1000, 1000);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    public String toString() {
        return date + " " + allLineList.size();
    }

    public void addLineType(LineType lineType) {
        lineTypeList.add(lineType);
    }

    MinuteLine prevMiniteLine;

    public void addLineType(Kline kline, MinuteLine line) {
        if (kline == null || kline.prev() == null) {
            return;
        }

        if (currentLineType == null) {
            currentLineType = new LineType();
            currentLineType.setLineTypeList(lineTypeList);
            currentLineType.setIdx(lineTypeList.size());
            currentLineType.setKline(kline);
            currentLineType.add(kline, prevMiniteLine, line);
        } else {
            boolean flag = currentLineType.add(kline, prevMiniteLine, line);
            if (!flag) {
                addLineType(currentLineType);
                currentLineType = new LineType();
                currentLineType.setLineTypeList(lineTypeList);
                currentLineType.setIdx(lineTypeList.size());
                currentLineType.setKline(kline);
                currentLineType.add(kline, prevMiniteLine, line);
            }
        }

        prevMiniteLine = line;

    }

    public Kline getKline() {
        return kline;
    }

    public void setKline(Kline kline) {
        this.kline = kline;
        kline.setStockDayMinuteLine(this);
    }

    public Grid logVOLTJ(Kline kline) {
        Grid grid = new Grid();
        grid.kline = kline;
        grid.setAllLineList(allLineList);
        grid.prsLines(kline);
        return grid;
    }

    public boolean logBottomUpFlag(Kline kline, LineContext context) {
        Grid grid = new Grid();
        grid.kline = kline;
        grid.setAllLineList(allLineList);
        boolean flag = grid.prsLinesBottomUp(kline, context);
        return flag;
    }


    public boolean isPrsLines(Kline kline) {
        Grid grid = new Grid();
        grid.kline = kline;
        grid.setAllLineList(allLineList);
        return grid.isPrsLines(kline);
    }

}

