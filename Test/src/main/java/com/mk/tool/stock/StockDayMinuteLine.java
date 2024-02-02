package com.mk.tool.stock;

import com.mk.tool.stock.model.MA;
import com.mk.tool.stock.model.MinuteModel;
import com.mk.util.StringUtil;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mk.tool.stock.IsZhangting.filterZF;

public class StockDayMinuteLine {
    private String date;
    private Kline kline;

    private Map<String, LineType> lineMap = new HashMap<>();
    private List<LineType> lineTypeList = new ArrayList<>();
    public List<MinuteLine> allLineList = new ArrayList();

    public float getFitstZF(Kline kline) {
        return allLineList.get(0).getZF(kline);
    }

    private LineType currentLineType;


    public float total;
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
            if(lineType.getTime().equalsIgnoreCase(StragetyZTBottom.monitorMinute)) {
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
        float avg = total / num;
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


    public MinuteLine getFirstSpeedUp(Kline kline, LineContext context) {
        MinuteLine minuteLine = null;
        int specialHor = 0;
        specialHor = context.getInt("specialHor");
        for (int i = 0; i < num; i++) {
            try {
                minuteLine = allLineList.get(i);
                if (minuteLine.getTime().equals("1034")) {
                    int a = 0;
                }

                MinuteModel minuteModel = new MinuteModel();
                minuteModel.kline = kline.prev();
                minuteModel.process(minuteLine);
                boolean isAllOK = minuteModel.allIsOK();
                if(isAllOK) {
                    return minuteLine;
                }
                if (minuteLine.getTime().equals(StragetyZTBottom.monitorMinute)) {
                    int a = 0;
                }
                float open = kline.prev().getClose();
                float curV = KLineUtil.compareMaxSign(minuteLine.price, open);
                if(curV<0 && Math.abs(curV)>3.3) {
                    return null;
                }

                MinuteLine ret = minuteLine.getFirstSpeedUp(kline, 950, 1000, 1000, context);
                if(specialHor==1 && ret!=null) {
                    return ret;
                }
                if (!filterZF(kline, minuteLine)) {
                    continue;
                }
                if (ret != null) {
                    return ret;
                }
                ret = minuteLine.getFirstSpeedUpFirst(kline, context);
                if (ret != null) {
                    return ret;
                }
                ret = minuteLine.getFirstSpeedUpFirst4(kline, context);
                if (ret != null) {
                    return ret;
                }

                if (!filterZF(kline, minuteLine)) {
                    continue;
                }

            }catch (Exception e) {
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


    public boolean isPrsLines(Kline kline) {
        Grid grid = new Grid();
        grid.kline = kline;
        grid.setAllLineList(allLineList);
        return grid.isPrsLines(kline);
    }

}

