package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.mk.tool.stock.model.KModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mk.tool.stock.AbsStragety.minuteDayLen;
import static com.mk.tool.stock.StragetyZTBottom.MIN_3MINUTES_LARGEHAND_ZF;
import static com.mk.tool.stock.StragetyZTBottom.MIN_3MINUTES_SMALLHAND_ZF;

public class MinuteLine implements Serializable {
    public List<MinuteLine> allLineList = new ArrayList();
    public List<MinuteLine> dayLineList = new ArrayList();
    public int globalIdx;
    private int idx;

    private String time;
    public float price;
    public int vol;
    public float avgPrice;
    private String date;
    private float chanage;

    public MinuteLine prev() {
        if (getIdx() - 1 < 0) {
            return null;
        }
        return dayLineList.get(getIdx() - 1);
    }

    public MinuteLine prev(int i) {
        if (getIdx() - i < 0) {
            return null;
        }
        return dayLineList.get(getIdx() - i);
    }

    public MinuteLine next() {
        return dayLineList.get(getIdx() + 1);
    }

    public MinuteLine next(int i) {
//        if (getIdx() - i < 0) {
//            return null;
//        }
        if(getIdx() + i >= dayLineList.size()) {
            return null;
        }
        return dayLineList.get(getIdx() + i);
    }

    public List<MinuteLine> getAllLineList() {
        return allLineList;
    }

    public void setAllLineList(List<MinuteLine> allLineList) {
        this.allLineList = allLineList;
    }

    public int getGlobalIdx() {
        return globalIdx;
    }

    public void setGlobalIdx(int globalIdx) {
        this.globalIdx = globalIdx;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getVol() {
        return vol;
    }

    public String getVolStr() {
        String v = "" + vol;
        if (v.length() == 3) {
            v = "  " + v;
        }
        if (v.length() == 4) {
            v = " " + v;
        }
        return v;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public float getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(float avgPrice) {
        this.avgPrice = avgPrice;
    }

    public MinuteLine() {

    }

    public String transTime(String time) {
        Date date = DateUtil.strToDateTime2(time);
        date = DateUtil.getBeforeMinute(date, 1);
        time = DateUtil.dateTimeToTimeString(date);
        return time;
    }

    public String transTime2(String time) {
        Date date = DateUtil.strToDateTime(time, DateUtil.SLANTMAO);
        date = DateUtil.getBeforeMinute(date, 0);
        time = DateUtil.dateTimeToTimeString(date);
        return time;
    }

    public MinuteLine(String line, int flag) {
        // 2023-08-25 09:31,34.19,32.97,34.24,32.90,32477,108942348.00,33.606
        String items[] = line.split(",");
        String[] dt = items[0].split("\\s+");
        setDate(dt[0]);
        String time = transTime2(getDate() + " " + dt[1] + ":00");
        setTime(time);
        setPrice(Float.parseFloat(items[2]));
        setAvgPrice(Float.parseFloat(items[7]));
        setVol(Integer.parseInt(items[5]));
    }

    public MinuteLine(String line, int flag, int f) {
        // 2023-08-25 09:31,34.19,32.97,34.24,32.90,32477,108942348.00,33.606
        String items[] = line.split(",");
        String[] dt = items[0].split("\\s+");
        setDate(dt[0]);
        String time = transTime2(getDate() + " " + dt[1].substring(0, 2) + ":" + dt[1].substring(2) + ":00");
        setTime(time);
        setPrice(Float.parseFloat(items[1]));
        setAvgPrice(Float.parseFloat(items[2]));
        setVol(Integer.parseInt(items[3]));
    }

    public MinuteLine(String line) {
        // 2023/05/08	0931	10.84	10.96	10.74	10.94	286800	3135409.00
        String items[] = line.split("\\s+");
        setDate(items[0]);
        String time = transTime(getDate() + " " + items[1] + "00");
        setTime(time);
        setPrice(Float.parseFloat(items[5]));
        setVol(Integer.parseInt(items[6]) / 100);
    }

    public String toString() {
        float dltPrice = 0;
        if (prev() != null) {
            dltPrice = price - prev().price;
        }
        return globalIdx + " " + idx + " " + getDate() + "   " + getTime() + " price:" + price + "  vol:" + vol + " dltPrice:" + dltPrice;
    }

    public String getDate() {
        return date;
    }

    public String getDateGNG() {
        String dateStr = DateUtil.trans(date, DateUtil.SLANT, DateUtil.GANG);
        return dateStr;
    }

    public void setDate(String date) {
        if (date.contains("-")) {
            date = DateUtil.trans(date, DateUtil.GANG, DateUtil.SLANT);
        }
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public boolean isVOLBetween(int v1, int v2) {
        int v = getVol();
        if (v >= v1 && v <= v2) {
            return true;
        }
        return false;
    }

    public boolean isVOLLarger(int v1) {
        int v = getVol();
        if (v >= v1) {
            return true;
        }
        return false;
    }

    public boolean isUPAVG(Kline kline, float frac, int i) {
        MinuteLine prev1 = prev(i);
        float open = kline.prev().getClose();
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(this.price, open);
        float dlt0 = curV - prev1V;
        if (prev1.price < prev1.avgPrice && this.price > avgPrice) {
            if (dlt0 > frac) {
                return true;
            }
        }
        return false;
    }

    public boolean isUPAVG(Kline kline, float frac) {
        float prevClose = kline.prev().getClose();
        float prev1V = KLineUtil.compareMaxSign(prev().price, prevClose);
        float curV = KLineUtil.compareMaxSign(this.price, prevClose);
        float dlt0 = curV - prev1V;
        if (prev().price < prev().avgPrice && this.price > avgPrice) {
            if (dlt0 > frac) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEq(float v1, float v2) {
        float v = Math.abs(v1 - v2);
        return v <= 0.01f;
    }

    public MinuteLine isBottomSppedUpFlag(Kline kline, LineContext context) {
        MinuteLine first = getFirst();
        MinuteLine cur = this;
        if(cur.getTime().equalsIgnoreCase("1338")) {
            int a = 0;
        }
        int time = Integer.parseInt(cur.getTime());
        if(time<1300) {
            return null;
        }
        //当前大于1
        float frac = this.getZF(kline);
        if(frac<1) {
            return null;
        }
        //是否最高
        boolean flag = cur.isABSMax();
        if(!flag) {
            return null;
        }
        MinuteLine min = getMinPrice(20);
        MinuteLine temp = min.getMazPrice(20);

        int vol = getTotalVol(20);

        float fracMin = min.getZF(kline);
        float fracTempMax = temp.getZF(kline);
        float dltTotal = frac - fracMin;
        float dltTotal2 = frac - fracTempMax;
        int num = getIdx() - min.getIdx();
        if(dltTotal2<2.5) {
            return null;
        }
        if(dltTotal<4) {
            return null;
        }
        int volPrev = min.getTotalVol(60);
        if(vol<volPrev*1.6) {
            return null;
        }
        return this;
    }

    public MinuteLine getMinPrice(int num) {
        MinuteLine min = null;
        float price = Integer.MAX_VALUE;
        for (int i = 0; i < num; i++) {
            MinuteLine minuteLine = prev(i);
            if (minuteLine == null) {
                break;
            }
            if (minuteLine.price < price) {
                price = minuteLine.price;
                min = minuteLine;
            }
        }
        return min;
    }

    public MinuteLine getMazPrice(int num) {
        MinuteLine min = null;
        float price = -Integer.MAX_VALUE;
        for (int i = 0; i < num; i++) {
            MinuteLine minuteLine = prev(i);
            if (minuteLine == null) {
                break;
            }
            if (minuteLine.price > price) {
                price = minuteLine.price;
                min = minuteLine;
            }
        }
        return min;
    }

    public int getTotalVol(int num) {
        int vol = 0;
        for (int i = 0; i < num; i++) {
            MinuteLine minuteLine = prev(i);
            if (minuteLine == null) {
                break;
            }
            vol+=minuteLine.getVol();
        }
        return vol;
    }


    public MinuteLine getFirstSpeedUp(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        MinuteLine first = getFirst();
        int specialHor = context.getInt("specialHor");
        if (specialHor == 1 && first.getInfo(kline).frac > 1) {
            return this;
        }

        if (specialHor == 1 && first.getInfo(kline).frac > 1.5) {
            return this;
        }

        MinuteLine minuteLine = getFirstSpeedUpVOL(kline, fracVolNum1, fracVolNum2, fracVolNum3, context);
        if (minuteLine != null) {
            return minuteLine;
        }


        //连续6根上涨
        minuteLine = getFirstSpeedCotiniusUpOne(kline, fracVolNum1, fracVolNum2, fracVolNum3, context);
        if (minuteLine != null) {
            return minuteLine;
        }


        minuteLine = getFirstSpeedUpVOLPROCE_IN4MInutes(kline, fracVolNum1, fracVolNum2, fracVolNum3);
        if (minuteLine != null) {
            return minuteLine;
        }

        minuteLine = getFirstUp3In3MInute(kline, 950, 1000, 1000, context);
        if (minuteLine != null) {
            return minuteLine;
        }

        return null;
    }


    public MinuteLine getFirstSpeedUpFirst5(Kline kline, LineContext context) {
        MinuteLine minuteLine = null;
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 5) {
            return null;
        }
//        if (idx > 8) {
//            return null;
//        }
        if (cur.getTime().equalsIgnoreCase("0935")) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);
        MinuteLine prev4 = cur.prev(4);
        MinuteLine prev5 = cur.prev(5);

        float open = kline.prev().getClose();
        float prev5V = KLineUtil.compareMaxSign(prev5.price, open);
        float prev4V = KLineUtil.compareMaxSign(prev4.price, open);
        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt4 = prev4V - prev5V;
        float dlt3 = prev3V - prev4V;
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 936) {
            return null;
        }

        //+++
        if (dlt2 < 0 || dlt1 < 0 || dlt0 < 0 || dlt3 < 0 || dlt4 < 0) {
            return null;
        }
        int MCNT = 0;
        if (dlt0 == 0) {
            MCNT++;
        }
        if (dlt1 == 0) {
            MCNT++;
        }
        if (dlt2 == 0) {
            MCNT++;
        }
        if (dlt3 == 0) {
            MCNT++;
        }
        if (dlt4 == 0) {
            MCNT++;
        }
        if (MCNT > 1) {
            return null;
        }
        float total = dlt0 + dlt1 + dlt2 + dlt3 + dlt4;
        if (total < 1.2) {
            return null;
        }

        int VCNT = 0;
        if (cur.getVol() > 1000) {
            VCNT++;
        }

        if (prev1.getVol() > 1000) {
            VCNT++;
        }
        if (prev2.getVol() > 1000) {
            VCNT++;
        }
        if (prev3.getVol() > 1000) {
            VCNT++;
        }
        if (prev4.getVol() > 1000) {
            VCNT++;
        }
        if (prev5.getVol() > 1000) {
            VCNT++;
        }
        if (VCNT < 2) {
            return null;
        }
        return cur;
    }


    public MinuteLine getFirstSpeedUpFirst4(Kline kline, LineContext context) {
        MinuteLine minuteLine = null;
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 4) {
            return null;
        }
        if (idx > 5) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase("0934")) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);
        MinuteLine prev4 = cur.prev(4);

        float open = kline.prev().getClose();
        float prev4V = KLineUtil.compareMaxSign(prev4.price, open);
        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt3 = prev3V - prev4V;
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 938) {
            return null;
        }

        //+++
        if (dlt2 <= 0 || dlt1 <= 0 || dlt0 <= 0 || dlt3 <= 0) {
            return null;
        }
        float total = dlt0 + dlt1 + dlt2 + dlt3;
        if (total < 2.0) {
            return null;
        }
        float frac = KLineUtil.compareMax(cur.price, kline.prev().getClose());
        if (frac < 2) {
            return null;
        }

        boolean flag1000 = false;
        boolean flag500 = true;
        if (cur.getVol() < 500) {
            return null;
        }
        if (prev1.getVol() <= 500) {
            return null;
        }
        if (prev2.getVol() <= 500) {
            return null;
        }
        if (prev3.getVol() <= 500) {
            return null;
        }
        if (prev4.getVol() <= 500) {
            return null;
        }
        if (cur.getVol() > 950) {
            flag1000 = true;
        }
        if (prev1.getVol() > 950) {
            flag1000 = true;
        }
        if (prev2.getVol() > 950) {
            flag1000 = true;
        }
        if (prev3.getVol() > 950) {
            flag1000 = true;
        }
        if (!flag1000) {
            return null;
        }
        return cur;
    }

    public MinuteLine getFirstSpeedUpFirst(Kline kline, LineContext context) {
        MinuteLine minuteLine = null;
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (idx > 5) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase("0934")) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);

        float open = kline.prev().getClose();
        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 938) {
            return null;
        }

        //+++
        if (dlt2 <= 0 || dlt1 <= 0 || dlt0 <= 0) {
            return null;
        }
        float total = dlt0 + dlt1 + dlt2;
        if (total < 2.0) {
            return null;
        }
        float frac = KLineUtil.compareMax(cur.price, kline.prev().getClose());
        if (frac < 2) {
            return null;
        }

        boolean flag1000 = false;
        boolean flag500 = true;
        if (cur.getVol() < 500) {
            return null;
        }
        if (prev1.getVol() <= 500) {
            return null;
        }
        if (prev2.getVol() <= 500) {
            return null;
        }
        if (prev3.getVol() <= 500) {
            return null;
        }
        if (cur.getVol() > 1000) {
            flag1000 = true;
        }
        if (prev1.getVol() > 1000) {
            flag1000 = true;
        }
        if (prev2.getVol() > 1000) {
            flag1000 = true;
        }
        if (prev3.getVol() > 1000) {
            flag1000 = true;
        }

        if (!flag1000) {
            return null;
        }

        return cur;
    }

    public MinuteLine getFirstUpOpen(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        MinuteLine minuteLine = null;
        minuteLine = getFirstSpeedUpOpen(kline, fracVolNum1, fracVolNum2, fracVolNum3, context);
        return minuteLine;
    }

    public MinuteLine getFirstUp3In3MInute(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        MinuteLine minuteLine = null;
        minuteLine = getFirstSpeedUpVOLPROCE_IN2MInutes(kline, fracVolNum1, fracVolNum2, fracVolNum3, context);
        return minuteLine;
    }


    public MinuteLine getFirstUp3In2MInute(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3) {
        MinuteLine minuteLine = null;
        minuteLine = getFirstSpeedUpVOLPROCE_IN1MInutes(kline, fracVolNum1, fracVolNum2, fracVolNum3);
        return minuteLine;
    }

    public MinuteLine getFirstSpeedDown(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3) {
        MinuteLine minuteLine = getFirstSpeedDownVOL(kline, fracVolNum1, fracVolNum2, fracVolNum3);
        if (minuteLine != null) {
            return minuteLine;
        }
        return null;
    }

    public MinuteLine getFirstSpeedUpVOLPROCE_IN1MInutes(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3) {
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 1) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase("0931")) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();

        float open = kline.prev().getClose();
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt0 = curV - prev1V;

        boolean volFlag = false;
        int timeSecond = Integer.parseInt(getTime());
        if (curV > 3) {
            return null;
        }

        if (timeSecond >= 933) {
            return null;
        }

        if (cur.getVol() < 9000) {
            return null;
        }
        if (prev1.getVol() < 10000) {
            if (prev1.getVol() < 7000) {
                return null;
            } else {
                if ((curV >= 2 || prev1V >= 2)) {
                    return cur;
                }

            }
            return null;
        }


        if (curV > 2.0 && prev1V > 0.7) {
            return cur;
        }


        return cur;
    }

    public MinuteLine getFirstSpeedUpOpen(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        float open = kline.prev().getClose();
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        if (curV > 2) {
            if (context.getkModel() != null) {
                if (context.getkModel().getSpace250() < 1) {
                    return cur;
                }
            }
        }
        return null;
    }


    public MinuteLine getFirstSpeedUpVOLPROCE_IN2MInutes(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 2) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase("0933")) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);

        float open = kline.prev().getClose();
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;

        boolean volFlag = false;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 933) {
            return null;
        }

        if (dlt1 < 1) {
            return null;
        }

        float total = dlt0 + dlt1;
        if (timeSecond == 932) {
            int specialHor = context.getInt("specialHor");
            if (specialHor == 1 && StragetyZTBottom.isTDX && prev2V > 1.0 && getVol() > 1000 && prev1.getVol() > 1000 && prev2.getVol() > 1000) {
                return cur;
            }
        }

        if (dlt0 < 1) {
            return null;
        }

        if (total < 2.5) {
            return null;
        }
        float frac = KLineUtil.compareMax(cur.price, kline.prev().getClose());
        if (frac < 3) {
            return null;
        }

        if (prev1.getVol() <= 1000) {
            return null;
        }

        if (cur.getVol() < 1000) {
            return null;
        }

        int total2 = cur.getVol() + prev1.getVol();
        if (total2 < 3000) {
            return null;
        }
        return cur;
    }

    public MinuteLine getFirstSpeedUpVOLPROCE_IN4MInutesKN(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3) {
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase("0933")) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);

        float open = kline.prev().getClose();
        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;

        boolean volFlag = false;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 938) {
            return null;
        }

        if (dlt2 > dlt1 || dlt1 > dlt0) {
            return null;
        }
        float total = dlt0 + dlt1 + dlt2;
        if (total < 1.0) {
            return null;
        }
        float frac = KLineUtil.compareMax(cur.price, kline.prev().getClose());
        if (frac < 2) {
            return null;
        }

        if (prev1.getVol() <= 500) {
            return null;
        }
        if (prev2.getVol() <= 500) {
            return null;
        }
        if (prev3.getVol() <= 500) {
            return null;
        }

        if (cur.getVol() < 1500) {
            return null;
        }
        return cur;
    }

    public MinuteLine getFirstSpeedUpVOLPROCE_IN4MInutes(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3) {
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase("0932")) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);

        float open = kline.prev().getClose();
        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;

        boolean volFlag = false;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 938) {
            return null;
        }


        //+++
        if (dlt2 > dlt1 || dlt1 > dlt0) {
            return null;
        }
        float total = dlt0 + dlt1 + dlt2;
        if (total < 1.0) {
            return null;
        }
        float frac = KLineUtil.compareMax(cur.price, kline.prev().getClose());
        if (frac < 2) {
            return null;
        }

        if (cur.getVol() < 4000) {
            return null;
        }

        if (prev1.getVol() <= 2000) {
            return null;
        }
        if (prev2.getVol() <= 500) {
            return null;
        }
        if (prev3.getVol() <= 500) {
            return null;
        }


        if (total < 3.1) {
            return null;
        } else if (dlt0 < 2.1f) {
            return null;
        }

        return cur;
    }


    public MinuteLine getFirstSpeedDownVOL(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3) {
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase("1006")) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);

        float open = kline.prev().getClose();
        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;

        int timeSecond = Integer.parseInt(getTime());

        if (dlt0 < 0 && dlt1 < 0 && dlt2 < 0) {
            if (cur.getVol() > 1000 && prev1.getVol() > 1000 && prev2.getVol() > 1000) {
                return cur;
            }
        }
        return null;
    }

    public MinuteLine getFirstSpeedUpVOLKN2(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        if (!context.getType().equalsIgnoreCase("FIRST_ZT")) {
            return null;
        }
        Kline prevKline = kline.prev();
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        float price = getPrice();
        float zf = KLineUtil.compareMax(price, prevKline.getClose());
        if (cur.getTime().equalsIgnoreCase("0930")) {
            if (zf > 9) {
                context.setFirsetIsZT(true);
                return cur;
            }
        } else {
            if (context.isFirsetIsZT()) {
                if (zf < 9) {
                    return cur;
                }
            }
        }

        return null;
    }

    public MinuteLine getFirstSpeedUpVOLKN(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;

        //open is jump to MA
        if (cur.getTime().equalsIgnoreCase("0930")) {
            Kline prevKline = kline.prev();
            float ma250 = prevKline.getMA250();
            float ma30 = prevKline.getMA30();
            if (prevKline.getClose() < ma250 && prevKline.getClose() < ma30) {
                if (price > ma30 && price > ma250) {
                    float frac = KLineUtil.compareMax(price, prevKline.getClose());
                    if (frac > 3) {
                        return null;
                    }
                    if (frac > 2) {
                        return cur;
                    }
                }
            }
        }
        //open is stand MA250
        if (cur.getTime().equalsIgnoreCase("0930")) {
            Kline prev3 = kline.prev(3);
            Kline prev2 = kline.prev(2);
            Kline prev1 = kline.prev(1);
            if (prev3.getZhangfu() > 9.5) {
                if (prev2.getZhangfu() < -3 && prev1.getZhangfu() < -1) {
                    float jf = prev2.getZhangfu() + prev1.getZhangfu();
                    if (jf < -6) {
                        float ma250 = prev1.getMA250();
                        if (prev1.getMin() <= ma250 && prev1.getClose() >= ma250) {
                            if (price > prev1.getMA250() && price > prev1.getMA10()) {
                                float ma10 = prev1.getNextSupposeMA10(kline.getOpen());
                                float frac = KLineUtil.compareMax(price, ma10);
                                if (frac < 2.5) {
                                    return cur;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (cur.getTime().equalsIgnoreCase("1324")) {
            int a = 0;
        }
        if (idx < 1) {
            return null;
        }

        float open = kline.prev().getClose();
        //in 2minutes >=3.9
        if (cur.getTime().equalsIgnoreCase("0931")) {
            MinuteLine prev1 = cur.prev();
            float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
            float curV = KLineUtil.compareMaxSign(cur.price, open);
            if (prev1V > 1 && curV > 3.9) {
                if (getVol() > 2000) {
                    return cur;
                }
            }
            return null;
        }

        if (idx < 3) {
            return null;
        }

        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);


        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;

        if (dlt0 > 1.9) {
            int timeSecond = Integer.parseInt(cur.getTime());
            if (timeSecond >= 939) {
                if (cur.getVol() > 2000) {
                    MinuteLine max = getMaxBefore();
                    float dlt = cur.price - max.price;
                    if (dlt > 0) {
                        float frac = KLineUtil.compareMaxSign1(dlt, kline.prev().getClose());
                        if (frac > 1) {
                            return cur;
                        }
                    }
                }
            }
        }

        boolean volFlag = false;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 1300 && timeSecond <= 1304) {
            return null;
        }

//        if (dlt2 > dlt1 && dlt2 > dlt0) {
//            return null;
//        }

        if (prev3V - curV == 0) {
            return null;
        }
        if (isEq(dlt1, 0) && isEq(dlt2, 0)) {
            return null;
        }
        if (isEq(dlt1, 0) && isEq(dlt0, 0)) {
            return null;
        }
        if (isEq(dlt0, 0) && isEq(dlt2, 0)) {
            return null;
        }
        if (dlt0 <= 0.7f && dlt1 <= 0.7f && dlt2 <= 0.7f) {
            if (cur.getVol() > 2000 && prev1.getVol() > 2000 && prev2.getVol() > 2000) {
                int total = cur.getVol() + prev1.getVol() + prev2.getVol();
                if (total > 9000) {
                    volFlag = true;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

//        if (dlt0 < dlt1 && dlt1 < dlt2) {
//            return null;
//        }

        float total = dlt0 + dlt1 + dlt2;
        if (volFlag) {
            if (total < 0.7) {
                return null;
            }
        } else {
            if (total < StragetyZTBottom.MIN_3MINUTES_ZF) {
                return null;
            }
        }


        msg = "" + total;

        if (cur.isVOLLarger(fracVolNum1) && cur.getPrice() >= prev1.getPrice()) {
            if (prev1.isVOLLarger(fracVolNum2) && prev1.getPrice() >= prev2.getPrice()) {
                if (prev2.isVOLLarger(fracVolNum3) && prev2.getPrice() >= prev3.getPrice()) {
                    return cur;
                }
            }
        }
        return null;
    }

    public MinuteLine getFirst() {
        MinuteLine minuteLine = this;
        while (minuteLine.prev() != null) {
            minuteLine = minuteLine.prev();
        }
        return minuteLine;
    }

    public MinuteLine getFirstSpeedUpVOLUpDownUp(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        if (getTime().equalsIgnoreCase("0953")) {
            int a = 0;
        }
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase(StragetyZTBottom.monitorMinute)) {
            int a = 0;
        }

        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);

        Info prev2Info = prev2.getInfo(kline);
        Info prev1Info = prev1.getInfo(kline);
        Info prev0Info = getInfo(kline);
        int specialHor = context.getInt("specialHor");
        if (prev2Info.dlt > 0.7 && prev0Info.dlt > 0.7 && prev1Info.frac > -0.2) {
            if (prev0Info.vol > 1000 && prev2Info.vol > 1000 && prev1Info.vol > 1000) {
                if (specialHor == 1 && prev0Info.frac > 2) {
                    return cur;
                }
            }
        }

        if (kline.prev() == null) {
            int a = 0;
        }
        float open = kline.prev().getClose();
        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;
        float total = dlt0 + dlt1 + dlt2;

        boolean volFlag = false;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 1300 && timeSecond <= 1304) {
            return null;
        }

        msg = "" + total;

        return null;
    }

    public float getZF2(Kline kline) {
        return KLineUtil.compareMaxSign(price, kline.prev().close);
    }

    public MinuteLine getFirstSpeedCotiniusUpOne(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        if (getTime().equalsIgnoreCase("1017")) {
            int a = 0;
        }
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase(StragetyZTBottom.monitorMinute)) {
            int a = 0;
        }

        int upNum = 0;
        int horNum = 0;
        int downNum = 0;
        int volNum = 0;
        float downFrac = 0;
        float dltTotal = 0;
        for (int i = 0; i < 3; i++) {
            MinuteLine prev = cur.prev(i);
            if (prev == null) {
                return null;
            }
            Info prevInfo = prev.getInfo(kline);
            dltTotal += prevInfo.dlt;
            if (prevInfo.dlt > 0.0) {
                if (prevInfo.vol > 1000) {
                    volNum++;
                }
                upNum++;
            } else if (prevInfo.dlt == 0.0) {
                horNum++;
            } else {
                downNum++;
                float dFrac = prevInfo.dlt;
                if (downFrac > dFrac) {
                    downFrac = dFrac;
                }
            }
        }
        float zf = cur.getZF(kline.prev());
        boolean flag = cur.isABSMax(kline.prev(), 1);
        if (dltTotal < 1.5 || cur.getVol() < 1000 || zf < 2.8 || !flag) {
            return null;
        }
        int specialHor = context.getInt("specialHor");
        if (specialHor == 1) {
            return cur;
        }
        return null;
    }

    public MinuteLine getFirstSpeedCotiniusUp(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        if (getTime().equalsIgnoreCase("1017")) {
            int a = 0;
        }
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase(StragetyZTBottom.monitorMinute)) {
            int a = 0;
        }

        int upNum = 0;
        int horNum = 0;
        int downNum = 0;
        int volNum = 0;
        float downFrac = 0;
        float dltTotal = 0;
        for (int i = 0; i < 7; i++) {
            MinuteLine prev = cur.prev(i);
            if (prev == null) {
                return null;
            }
            Info prevInfo = prev.getInfo(kline);
            dltTotal += prevInfo.dlt;
            if (prevInfo.dlt > 0.0) {
                if (prevInfo.vol > 1000) {
                    volNum++;
                }
                upNum++;
            } else if (prevInfo.dlt == 0.0) {
                horNum++;
            } else {
                downNum++;
                float dFrac = prevInfo.dlt;
                if (downFrac > dFrac) {
                    downFrac = dFrac;
                }
            }
        }
        if (dltTotal < 1.5) {
            return null;
        }
        int specialHor = context.getInt("specialHor");
        if (specialHor == 1 && upNum >= 6 && downNum <= 1) {
            return cur;
        }
        return null;
    }

    public MinuteLine getFirstSpeedCotiniusUp10(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        if (getTime().equalsIgnoreCase("0941")) {
            int a = 0;
        }
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 9) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase(StragetyZTBottom.monitorMinute)) {
            int a = 0;
        }

        int upNum = 0;
        int horNum = 0;
        int downNum = 0;
        int volNum = 0;
        float downFrac = 0;
        float dltTotal = 0;
        for (int i = 0; i < 9; i++) {
            MinuteLine prev = cur.prev(i);
            if (prev == null) {
                return null;
            }
            Info prevInfo = prev.getInfo(kline);
            dltTotal += prevInfo.dlt;
            if (prevInfo.dlt > 0.0) {
                if (prevInfo.vol > 1000) {
                    volNum++;
                }
                upNum++;
            } else if (prevInfo.dlt == 0.0) {
                horNum++;
            } else {
                downNum++;
                float dFrac = prevInfo.dlt;
                if (downFrac > dFrac) {
                    downFrac = dFrac;
                }
            }
        }
        if (dltTotal < 1.5) {
            return null;
        }
        int specialHor = context.getInt("specialHor");
        if (specialHor == 1 && upNum >= 6 && downNum <= 1) {
            return cur;
        }
        return null;
    }

    public MinuteLine getFirstSpeedUpVOLUpDownUp2(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        if (getTime().equalsIgnoreCase("0953")) {
            int a = 0;
        }
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase(StragetyZTBottom.monitorMinute)) {
            int a = 0;
        }

        int upNum = 0;
        int horNum = 0;
        int downNum = 0;
        int volNum = 0;
        float downFrac = 0;
        for (int i = 0; i < 5; i++) {
            MinuteLine prev = cur.prev(i);
            if (prev == null) {
                return null;
            }
            Info prevInfo = prev.getInfo(kline);
            if (prevInfo.dlt > 0.0) {
                if (prevInfo.vol > 1000) {
                    volNum++;
                }
                upNum++;
            } else if (prevInfo.dlt == 0.0) {
                horNum++;
            } else {
                downNum++;
                float dFrac = prevInfo.dlt;
                if (downFrac > dFrac) {
                    downFrac = dFrac;
                }
            }
        }
        int specialHor = context.getInt("specialHor");
        if (specialHor == 1 && upNum >= 2 && downNum <= 1 && volNum >= 2) {
            return cur;
        }


        return null;
    }

    public MinuteLine getFirstSpeedUpVOL(Kline kline, int fracVolNum1, int fracVolNum2, int fracVolNum3, LineContext context) {
        int specialHor = context.getInt("specialHor");
        MinuteResult minuteResult = new MinuteResult();
        MinuteLine cur = this;
        if (idx < 3) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase(StragetyZTBottom.monitorMinute)) {
            int a = 0;
        }
        MinuteLine prev1 = cur.prev();
        MinuteLine prev2 = cur.prev(2);
        MinuteLine prev3 = cur.prev(3);
        if (kline.prev() == null) {
            int a = 0;
        }
        float open = kline.prev().getClose();
        float prev3V = KLineUtil.compareMaxSign(prev3.price, open);
        float prev2V = KLineUtil.compareMaxSign(prev2.price, open);
        float prev1V = KLineUtil.compareMaxSign(prev1.price, open);
        float curV = KLineUtil.compareMaxSign(cur.price, open);
        float dlt2 = prev2V - prev3V;
        float dlt1 = prev1V - prev2V;
        float dlt0 = curV - prev1V;
        float total = dlt0 + dlt1 + dlt2;

        boolean volFlag = false;
        int timeSecond = Integer.parseInt(getTime());

        if (timeSecond >= 1300 && timeSecond <= 1304) {
            return null;
        }
        if (cur.getTime().equalsIgnoreCase("0954")) {
            int a = 0;
        }


        if (prev3V - curV == 0) {
            return null;
        }
        if (isEq(dlt1, 0) && isEq(dlt2, 0)) {
            return null;
        }
        if (isEq(dlt1, 0) && isEq(dlt0, 0)) {
            return null;
        }
        if (isEq(dlt0, 0) && isEq(dlt2, 0)) {
            return null;
        }
        int totalVol = cur.getVol() + prev1.getVol() + prev2.getVol();
        if (dlt0 <= 0.7f && dlt1 <= 0.7f && dlt2 <= 0.7f) {
            if (cur.getVol() > 4000 && prev1.getVol() > 4000 && prev2.getVol() > 3000) {
                volFlag = true;
            } else if (cur.getVol() > 1000 && prev1.getVol() > 1000 && prev2.getVol() > 1000 && totalVol > 10000) {
                volFlag = true;
            }
            //space250
            else if (cur.getVol() > 1000 && prev1.getVol() > 1000 && prev2.getVol() > 500) {
                float space250 = Integer.MAX_VALUE;
                if (context.getkModel() != null) {
                    space250 = context.getkModel().getSpace250();
                }
                if (space250 < 1.0f && total > 1.0f) {
                    return cur;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        boolean specialFlag = false;
        if (cur.getVol() > 1000 && prev1.getVol() > 1000 && prev2.getVol() < 5 && prev3.getVol() < 5) {
            volFlag = true;
            specialFlag = true;
        }
        if (cur.getVol() > 1000 && prev1.getVol() > 780 && prev2.getVol() > 1000 && prev3.getVol() < 300) {
            volFlag = true;
            specialFlag = true;
        }

        if (volFlag) {
            if (total < 0.7) {
                return null;
            }
        } else {
            if (total > MIN_3MINUTES_LARGEHAND_ZF) {
            } else if (dlt1 < 0 && dlt0 > 2.0f) {
            } else {
                return null;
            }
            if (total < StragetyZTBottom.MIN_3MINUTES_LARGEHAND_ZF) {
                return null;
            }
        }

        msg = "" + total;
        if (cur.isVOLLarger(fracVolNum1) && cur.getPrice() >= prev1.getPrice()) {
            if (prev1.isVOLLarger(fracVolNum2) && prev1.getPrice() >= prev2.getPrice()) {
                if (prev2.isVOLLarger(fracVolNum3) && prev2.getPrice() >= prev3.getPrice()) {
                    return cur;
                } else if (specialFlag) {
                    return cur;
                }
            } else {
                if (specialFlag) {
                    //603178.txt  0944  2023/09/18 should stand ma250
                    return cur;
                }
            }
        }


//        if (specialHor == 1 && curV > 2) {
//            return cur;
//        }

        return null;
    }

    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getAvgSlant(Kline kline) {
        double slant = 0;
        if (prev() == null) {
            return 0;
        }
        float prev = kline.prev().getClose();
        double m0x = prev().idx;
        double m0y = LineType.getLevel(prev().getAvgPrice(), prev);
        double m1x = idx;
        double m1y = LineType.getLevel(getAvgPrice(), prev);

        m0x = Grid.getPixelX(m0x);
        m0y = Grid.getPixelY(m0y);
        m1x = Grid.getPixelX(m1x);
        m1y = Grid.getPixelY(m1y);
        slant = (m0y - m1y) / (m0x - m1x);
        double theata = 180 * Math.atan(slant) / Math.PI;
        return (int) theata;
    }

    public boolean isMax() {
        int idx = 0;
        int timeSecond = Integer.parseInt(getTime());
        if (timeSecond < 1000) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            MinuteLine minuteLine = prev(i + 1);
            if (minuteLine.price < price) {
                idx = i;
                break;
            }
        }
        for (int i = idx; i < 240; i++) {
            MinuteLine minuteLine = prev(i + 1);
            if (minuteLine == null) {
                break;
            }
            if (minuteLine.price > price) {
                return false;
            }
        }
        return true;
    }

    public boolean isABSMax() {
        int idx = 0;
        int timeSecond = Integer.parseInt(getTime());
        if (timeSecond < 945) {
            return false;
        }
        for (int i = idx; i < 240; i++) {
            MinuteLine minuteLine = prev(i + 1);
            if (minuteLine == null) {
                break;
            }
            if (minuteLine.price > price) {
                return false;
            }
        }
        return true;
    }

    public boolean isABSMax(Kline kline, float dlt) {
        int idx = 0;
        int timeSecond = Integer.parseInt(getTime());
        if (timeSecond < 945) {
            return false;
        }
        for (int i = idx; i < 240; i++) {
            MinuteLine minuteLine = prev(i + 1);
            if (minuteLine == null) {
                break;
            }

            float frac = KLineUtil.compareMaxSign(price, kline.close) - KLineUtil.compareMaxSign(minuteLine.price, kline.close);
            if (frac < dlt) {
                return false;
            }
        }
        return true;
    }

    public MinuteLine getMin() {
        int timeSecond = Integer.parseInt(getTime());
        MinuteLine min = null;
        float price = Integer.MAX_VALUE;
        for (int i = 0; i < 240; i++) {
            MinuteLine minuteLine = prev(i);
            if (minuteLine == null) {
                break;
            }
            if (minuteLine.price < price) {
                price = minuteLine.price;
                min = minuteLine;
            }
        }
        return min;
    }


    public String getLine() {
        return "" + date + " " + time + "," + price + "," + avgPrice + "," + vol;
    }

    public float getChanage() {
        return chanage;
    }

    public void setChanage(float chanage) {
        this.chanage = chanage;
    }


    public MinuteLine getMaxBefore() {
        int idx = 0;
        float max = 0;
        MinuteLine maxLine = null;
        int timeSecond = Integer.parseInt(getTime());
        for (int i = idx; i < 240; i++) {
            MinuteLine minuteLine = prev(i + 1);
            if (minuteLine == null) {
                break;
            }
            if (minuteLine.price > max) {
                maxLine = minuteLine;
                max = maxLine.price;
            }
        }
        return maxLine;
    }

    public boolean isMax2() {
        int idx = 0;
        int timeSecond = Integer.parseInt(getTime());
        for (int i = idx; i < 240; i++) {
            MinuteLine minuteLine = prev(i + 1);
            if (minuteLine == null) {
                break;
            }
            if (minuteLine.price > price) {
                return false;
            }
        }
        return true;
    }

    public float getZF(Kline kline) {
        float ret = KLineUtil.compareMaxSign(price, kline.getClose());
        return ret;
    }

    public boolean hasMax(Kline kline, float minZf) {
        for(int i=0; i<dayLineList.size(); i++) {
            if(this.getTime().equalsIgnoreCase("0941") && i==12) {
                int a = 0;
            }
            MinuteLine minuteLine = this.next(i);
            if(minuteLine==null) {
                return false;
            }
            float ret = KLineUtil.compareMaxSign(minuteLine.price, kline.getClose());
            if(ret>minZf) {
                return true;
            }
        }
        return false;
    }


    public boolean isPriceUp() {
        return prev().getPrice() < getPrice();
    }

    public Info getInfo(Kline kline1) {
        Kline kline = kline1.prev();
        Info info = new Info();
        info.vol = getVol();
        info.price = getPrice();
        info.frac = KLineUtil.compareMaxSign(this.getPrice(), kline.close);
        float prev = 0;//KLineUtil.compareMaxSign(prev().getPrice(), kline.close);
        if (prev() != null) {
            prev = KLineUtil.compareMaxSign(prev().getPrice(), kline.close);
        }
        info.dlt = info.frac - prev;
        return info;
    }

    public List<Info> getInfos(Kline kline) {
        MinuteLine prev1 = prev(1);
        MinuteLine prev2 = prev(2);
        List<Info> list = new ArrayList<>();
        list.add(prev2.getInfo(kline));
        list.add(prev1.getInfo(kline));
        list.add(getInfo(kline));
        return list;
    }

    static class Info {
        public float price;
        public float frac;
        public int vol;
        public float dlt;
    }
}

