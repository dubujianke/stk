package com.alading.tool.stock;

import com.huaien.core.util.DateUtil;
import com.alading.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {
    public static int BEISHU = 10000;//ZT=>1000 prev==>10000
    private List<MinuteLine> allLineList = new ArrayList();
    public Kline kline;
    private List<LineType> lineTypeList = new ArrayList<>();

    public boolean isBottomUpFlag() {
        return bottomUpFlag;
    }

    public void setBottomUpFlag(boolean bottomUpFlag) {
        this.bottomUpFlag = bottomUpFlag;
    }

    private  boolean bottomUpFlag;
    private double prev;
    private double unitPercent;//1分钱对应的百分比
    private int unit;//1分钱对应的值
    private List<TypeCategory> list = new ArrayList();
    private Map<String, TypeCategory> map = new HashMap<>();

    private List<Span> spanList = new ArrayList<>();

    
    public int getSpanSize() {
        return spanList.size();
    }

    public int getSpan(int idx) {
        return spanList.get(idx).getNum();
    }

    public Grid() {
        spanList.add(new Span(0, 950));
        spanList.add(new Span(950, Integer.MAX_VALUE));

        list.add(new TypeCategory("+1-1"));
        list.add(new TypeCategory("+1-2"));
        list.add(new TypeCategory("+1-3"));
        list.add(new TypeCategory("+2-1"));
        list.add(new TypeCategory("+2-2"));
        list.add(new TypeCategory("+2-3"));
        list.add(new TypeCategory("+3-1"));
        list.add(new TypeCategory("+3-2"));
        list.add(new TypeCategory("+3-3"));

        list.add(new TypeCategory("-1+1"));
        list.add(new TypeCategory("-1+2"));
        list.add(new TypeCategory("-1+3"));
        list.add(new TypeCategory("-2+1"));
        list.add(new TypeCategory("-2+2"));
        list.add(new TypeCategory("-2+3"));
        list.add(new TypeCategory("-3+1"));
        list.add(new TypeCategory("-3+2"));
        list.add(new TypeCategory("-3+3"));

        for (TypeCategory typeCategory : list) {
            map.put(typeCategory.name, typeCategory);
        }
    }

    public void logTringle() {
        for (TypeCategory typeCategory : list) {
            if (typeCategory.total > 0) {
                //System.out.println(typeCategory.toString());
            }
        }
    }

    public void add(String type) {
        if (map.get(type) == null) {
            return;
        }
        map.get(type).add();
    }

    public void addAllSpan(int vol) {
        for (Span span : spanList) {
            if (span.isIn(vol)) {
                span.add();
            }
        }
    }

    public boolean isPrsLines(Kline kline) {
        for (MinuteLine minuteLine : allLineList) {
            int vol = minuteLine.getVol();
            addAllSpan(vol);
        }

        int num = 0;
        for (Span span : spanList) {
            if (span.getFrom() == 0 && span.getTo() == 950 && span.getNum() < 190) {
//                Log.log("" + span.getFrom() + " " + span.getTo() + " " + span.getNum());
                return false;
            }
        }
        return true;
    }

    public void prsLines(Kline kline) {
        for (MinuteLine minuteLine : allLineList) {
            int vol = minuteLine.getVol();
            addAllSpan(vol);
        }

        int num = 0;
//        Log.log("prsLines:" + kline.getDate());
        for (Span span : spanList) {
//            if (span.getFrom() == 0 && span.getTo() == 950 && span.getNum() < 200) {
//                Log.log("" + span.getFrom() + " " + span.getTo() + " " + span.getNum());
                num++;
//            }
        }
        if(num>0) {
//            Log.log("prsLines:" + kline.getDate());
        }
    }

    //cur
    public boolean prsLinesBottomUp(Kline kline, LineContext context) {
        double dltTotal = 0;
        MinuteLine last = allLineList.get(allLineList.size()-1);
        if(last.getZF(kline.prev())<0) {
            return false;
        }
        for (MinuteLine minuteLine : allLineList) {
            MinuteLine cur = minuteLine;
            MinuteLine ret = cur.isBottomSppedUpFlag(kline, context);
            if(ret!=null) {
                return true;
            }
        }
        return false;
//        Log.log("prsLines:" + kline.getDate());
    }

    /**
     * 11 12 13
     * 21 22 23
     * 31 32 33
     */
    public String getUpTringleType(int left, int right) {
        if (left <= 0 || right >= 0) {
            return "";
        }
        int leftNumber = Math.abs(left / unit);
        int rightNumber = Math.abs(right / unit);
        return "+" + leftNumber + "-" + rightNumber;
    }

    public String getDownTringleType(int left, int right) {
        if (left >= 0 || right <= 0) {
            return "";
        }
        int leftNumber = Math.abs(left / unit);
        int rightNumber = Math.abs(right / unit);
        return "-" + leftNumber + "+" + rightNumber;
    }


    public String getType(int left, int right) {
        if (left > 0 && right < 0) {
            return getUpTringleType(left, right);
        }
        if (left < 0 && right > 0) {
            return getDownTringleType(left, right);
        }
        return "";
    }

    public String getType(LineType lineType) {
        try {
            LineType prevLineType = lineType.prev();
            int left = prevLineType.getPriceLevel();
            int right = lineType.getPriceLevel();
            return getType(left, right);
        } catch (Exception e) {
        }
        return "";
    }


    public static void main(String[] args) {
        Pos pos1 = new Pos(1, 0);
        Pos pos0 = new Pos(1, -100);
        double theata = LineType.caculate(pos1, pos0);
        //System.out.println(theata);
    }

    public int getLevelByPrice(double v) {
        double dlt = v - prev;
        return (int) KLineUtil.compareMaxSign5(dlt, prev);
    }

    public int getLevelByDltPrice(double dlt) {
        return (int) KLineUtil.compareMaxSign5(dlt, prev);
    }

    public double getPrev() {
        return prev;
    }


    public void setPrev(double prev) {
        this.prev = prev;
        double zt = 0.1f * prev;
        unitPercent = 0.01f / prev;
        unit = (int) (unitPercent * BEISHU);
    }

    public double getPriceByLevel(int level) {
        double ret = level / BEISHU * prev;
        return ret;
    }


    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public double getUnitPercent() {
        return unitPercent;
    }

    public void setUnitPercent(double unitPercent) {
        this.unitPercent = unitPercent;
    }

    public List<LineType> getLineTypeList() {
        return lineTypeList;
    }

    public void setLineTypeList(List<LineType> lineTypeList) {
        this.lineTypeList = lineTypeList;
    }

    public static boolean isBetween(String time, String fromTime, String endTime) {
        long timeV = DateUtil.strToTime3(time).getTime();
        long fromTimeV = DateUtil.strToTime3(fromTime).getTime();
        long endTimeV = DateUtil.strToTime3(endTime).getTime();
        if (timeV >= fromTimeV && timeV <= endTimeV) {
            return true;
        }
        return false;
    }

    public void prsTringle(String fromTime, String endTime) {
        int i = 0;
        for (LineType lineType : lineTypeList) {
            if (i == 61) {
                int a = 0;
                a++;
            }
            String time = lineType.getTime();
            if (!isBetween(time, fromTime, endTime)) {
                continue;
            }
            String type = getType(lineType);
            add(type);
            String atype = getType(lineType);
            if (!StringUtil.isNull(atype)) {
                Log.log(lineType.toString() + " -> " + atype);
            }
            i++;
        }
    }

    public static double getPixelX(double x) {
        return x * 1624.0f / 240;
    }


    public static double getPixelY(double y) {
        return y * 252.0f / 1000;
    }

    public List<MinuteLine> getAllLineList() {
        return allLineList;
    }

    public void setAllLineList(List<MinuteLine> allLineList) {
        this.allLineList = allLineList;
    }

    public int getCategorySize() {
        List<String> alist = new ArrayList<>();
        for(Span span:spanList)
        {
            alist.add(span.getId());
        }
        return alist.size();
    }

    public List<String> getCategory() {
        List<String> alist = new ArrayList<>();
        for(Span span:spanList)
        {
            alist.add(span.getId());
        }
        return alist;
    }

    public String getSerieName(int i) {
        return spanList.get(i).getId();
    }

    public int isGoldCross() {
        int flag = kline.isNextGuailiChange();
        return flag*10;
    }

    public int isDeadCrossAll() {
        if(kline.isDeadCrossAll(10, 30)) {
            return  1;
        }
        if(kline.isDeadCrossAll(10, 60)) {
            return  1;
        }
        if(kline.isDeadCrossAll(10, 120)) {
            return  1;
        }
        if(kline.isDeadCrossAll(10, 250)) {
            return  1;
        }

        if(kline.isDeadCrossAll(30, 60)) {
            return  1;
        }
        if(kline.isDeadCrossAll(30, 120)) {
            return  1;
        }
        if(kline.isDeadCrossAll(30, 250)) {
            return  1;
        }
        if(kline.isDeadCrossAll(60, 120)) {
            return  1;
        }
        if(kline.isDeadCrossAll(60, 250)) {
            return  1;
        }
        return 0;
    }



}
