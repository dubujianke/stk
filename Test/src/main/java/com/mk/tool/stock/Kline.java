package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.mk.data.GetGuben;
import com.mk.util.StringUtil;

import java.util.*;

public class Kline {

    public List<Kline> allLineList = new ArrayList();
    public MonthKline monthKline;
    public Weekline weekline;

    private StockDayMinuteLine stockDayMinuteLine = null;

    public void clear() {
        allLineList = null;
        stockDayMinuteLine = null;
        weekline = null;
        monthKline = null;
    }

    private int idx;
    public float open;
    public float close;
    public float max;
    public float min;
    public String date;
    private long volume;
    private float hand;

    public static int getHOR() {
        return HOR;
    }

    public static void setHOR(int HOR) {
        Kline.HOR = HOR;
    }

    public static int getDOWN() {
        return DOWN;
    }

    public static void setDOWN(int DOWN) {
        Kline.DOWN = DOWN;
    }

    public static int getUP() {
        return UP;
    }

    public static void setUP(int UP) {
        Kline.UP = UP;
    }

    public static int getDeepV() {
        return DeepV;
    }

    public static void setDeepV(int deepV) {
        DeepV = deepV;
    }

    public Date getDateObj() {
        return DateUtil.stringToDate3(date);
    }


    private float mopen;
    private float mclose;
    private float mmax;
    private float mmin;

    private float wopen;
    private float wclose;
    private float wmax;
    private float wmin;


    public Kline() {

    }

    public Kline(String line, int mode) {
        if (mode == 0) {
            String items[] = line.split(",");
            String date = DateUtil.dateToString3(DateUtil.strToDate(items[0]));
            setDate(date);
            setOpen(Float.parseFloat(items[1]));
            setClose(Float.parseFloat(items[2]));
            setMax(Float.parseFloat(items[3]));
            setMin(Float.parseFloat(items[4]));
            setVolume(Long.parseLong(items[5]));
            setHand(Float.parseFloat(items[10]));
        } else {
            String items[] = line.split(",");
            String date = DateUtil.dateToString3(DateUtil.strToDate2(items[0]));
            setDate(date);
            setOpen(Float.parseFloat(items[1]));
            setClose(Float.parseFloat(items[2]));
            setMax(Float.parseFloat(items[3]));
            setMin(Float.parseFloat(items[4]));
            setVolume(100 * Long.parseLong(items[5]));
            setHand(Float.parseFloat(items[10]));
        }
    }

    public Kline(String line) {
        // 1997/11/18 21.81 24.10 20.75 20.80 39718400 877249024.00
        String items[] = line.split("\\s+");
        setDate(items[0]);
        setOpen(Float.parseFloat(items[1]));
        setClose(Float.parseFloat(items[4]));
        setMax(Float.parseFloat(items[2]));
        setMin(Float.parseFloat(items[3]));
        setVolume(Long.parseLong(items[5]));
    }

    public float getZhangfuAbs() {
        Kline day0 = allLineList.get(getIdx());
        Kline day1 = allLineList.get(getIdx() - 1);
        float ret = 100 * (day0.getClose() - day1.getClose()) / day1.getClose();
        return Math.abs(ret);
    }

    public float getUpZhangfu() {
        try {
            Kline day0 = allLineList.get(getIdx());
            Kline day1 = allLineList.get(getIdx() - 1);
            float ret = 100 * (day0.getMax() - day1.getClose()) / day1.getClose();
            return ret;
        } catch (Exception e) {

        }
        return 0;
    }

    public float getZhangfu() {
        try {
            Kline day0 = allLineList.get(getIdx());
            Kline day1 = allLineList.get(getIdx() - 1);
            float ret = 100 * (day0.getClose() - day1.getClose()) / day1.getClose();
            return ret;
        } catch (Exception e) {

        }
        return 0;
    }

    public float getMAXZhangfu2() {
        try {
            Kline day0 = allLineList.get(getIdx());
            Kline day1 = allLineList.get(getIdx() - 1);
            float ret = 100 * (day0.max - day1.getClose()) / day1.getClose();
            return ret;
        } catch (Exception e) {

        }
        return 0;
    }


    public float getSmallZhangu() {
        float zf = getZhangfu();
        float zf1 = prev().getZhangfu();
        float ezf = getEntityZhangfu();
        float ezf1 = prev().getEntityZhangfu();

        float azf = Math.abs(zf);
        float azf1 = Math.abs(zf1);
        float aezf = Math.abs(ezf);
        float aezf1 = Math.abs(ezf1);
        float min = Integer.MAX_VALUE;
        if (azf < min) {
            min = azf;
        }
        if (azf1 < min) {
            min = azf1;
        }
        if (aezf < min) {
            min = aezf;
        }
        if (aezf1 < min) {
            min = aezf1;
        }
        return min;
    }


    public float getMAXZhangfu() {
        try {
            Kline day0 = allLineList.get(getIdx());
            Kline day1 = allLineList.get(getIdx() - 1);
            float ret = 100 * (day0.getMax() - day1.getClose()) / day1.getClose();
            return ret;
        } catch (Exception e) {

        }
        return 0;
    }


    public boolean isTouchBottomMAI(int period, float minZF, float percent) {
        if (getZhangfu() < minZF) {
            return false;
        }
        float mai = getMAI(period);
        if (max <= mai) {
            return false;
        }

        float all = getMax() - getMin();
        float upLen = getMax() - mai;
        float downLen = mai - getMin();
        float fraction = 100 * upLen / all;
        if (fraction > percent) {
            return true;
        }

        return false;
    }

    public float getEntityZhangfu() {
        float ret = 100 * (getClose() - getOpen()) / getOpen();
        return ret;
    }

    public float getEntityZhangfu2() {
        float ret = 100 * (getClose() - getOpen()) / prev().getClose();
        return ret;
    }


    public boolean isBlack() {
        if (getClose() < getOpen()) {
            return true;
        }
        return false;
    }

    public float getMonthEntityZhenfu() {
        Kline day0 = allLineList.get(getIdx());
        float ret = Math.abs(100 * (day0.getClose() - day0.getOpen()) / day0.getOpen());
        return ret;
    }

    public float getAvg20() {
        float total = 0;
        for (int i = 0; i < 20; i++) {
            Kline day = allLineList.get(getIdx());
            total += day.getClose();
        }
        total = total / 20;
        return total;
    }

    public Kline getMaxBefore2(int num) {
        float max = 0;
        Kline kline = null;
        for (int i = getIdx(); i >= getIdx() - num; i--) {
            Kline day = allLineList.get(i);
            if (day.getMax() > max) {
                max = day.getMax();
                kline = day;
            }
        }
        return kline;
    }

    public float getMaxBefore(int num) {
        float max = 0;
        int maxIdx = 0;
        for (int i = getIdx() - 2; i >= getIdx() - num; i--) {
            Kline day = allLineList.get(i);
            if (day.getEntityMax() > max) {
                max = day.getEntityMax();
                maxIdx = i;
            }
        }
        return max;
    }

    public boolean isWrapAfter(int toOffset) {
        return isWrapAfter(getMin(), getMax(), toOffset);
    }


    public boolean isWrapAfter(float min, float max, int toOffset) {
        int len = toOffset - getIdx();
        if (len < 5) {
            return false;
        }
        int cnt = 0;
        for (int i = getIdx() + 1; i <= toOffset; i++) {
            Kline day = allLineList.get(i);
            if (this.contain(day)) {
                cnt++;
            }
        }
        int fraction = (int) (1.0f * cnt / len * 100);
        if (fraction > 80) {
            return true;
        }
        return false;
    }


    public boolean isVolumStepDownAfter(int toOffset) {
        int len = toOffset - getIdx();
        if (len < 5) {
            return false;
        }
        int cnt = 0;
        for (int i = getIdx() + 1; i <= toOffset; i++) {
            Kline day = allLineList.get(i);
            if (this.containVolume(day)) {
                cnt++;
            }
        }
        int fraction = (int) (1.0f * cnt / len * 100);
        if (fraction > 90) {
            return true;
        }
        return false;
    }


    public float getZhangfu(int num) {
        Kline day0 = allLineList.get(getIdx());
        Kline day1 = allLineList.get(getIdx() - num);
        float ret = 100 * (day0.getClose() - day1.getClose()) / day1.getClose();
        return ret;
    }

    public float getVolFraction() {
        try {
            Kline day0 = allLineList.get(getIdx());
            Kline day1 = allLineList.get(getIdx() - 1);
            float ret = day0.getVolume() / day1.getVolume();
            return ret;
        } catch (Exception e) {

        }
        return 0.1f;
    }

//	public boolean isLianzhang(int num) {
//		try {
//			for(int i=idx-num; i<=idx; i++) {
//				Kline day = allLineList.get(i);
//				if(day.getZhangfu()<0) {
//					return false;
//				}
//			}
//			float zf = getZhangfu(num);
//			if(zf<10) {
//				return false;
//			}
//		} catch (Exception e) {
//
//		}
//		return true;
//	}

    public float getZhenfu() {
        if (prev() == null) {
            return 1000;
        }
        return Math.abs(100 * (getMax() - getMin()) / prev().close);
    }


    public float getShitiZhenfu() {
        return Math.abs(100 * (getOpen() - getClose()) / getOpen());
    }

    public boolean zhangfuBetween(float v1, float v2) {
        float ret = getZhangfu();
        if (ret > v1 && ret <= v2) {
            return true;
        }
        return false;
    }

    public boolean isShadownDown() {
        if (getOpen() > getClose()) {
            boolean flag = false;
            float all = getMax() - getMin();
            float upLen = getMax() - getOpen();
            float midLen = getOpen() - getClose();
            float downLen = getClose() - getMin();
            float fraction = 100 * downLen / all;
            return fraction > 60;
        }

        boolean flag = false;
        float all = getMax() - getMin();
        float upLen = getMax() - getClose();
        float downLen = getOpen() - getMin();
        float fraction = 100 * downLen / all;
        return fraction > 60;
    }

    public boolean isShadownDown(float vfraction) {
        if (getOpen() > getClose()) {
            boolean flag = false;
            float all = getMax() - getMin();
            float upLen = getMax() - getOpen();
            float midLen = getOpen() - getClose();
            float downLen = getClose() - getMin();
            float fraction = 100 * downLen / all;
            return fraction > vfraction;
        }

        boolean flag = false;
        float all = getMax() - getMin();
        float upLen = getMax() - getClose();
        float downLen = getOpen() - getMin();
        float fraction = 100 * downLen / all;
        return fraction > vfraction;
    }

//    public int getType() {
//        boolean isCross = isCross(10);
//        if (isCross) {
//            return MPoint.HOR;
//        }
//        if (getZhangfu() < -1) {
//            return MPoint.DOWN;
//        }
//        if (getZhangfu() > 1) {
//            return MPoint.UP;
//        }
//        return MPoint.HOR;
//    }

    public boolean isUpCrossBottom(float vfraction, float vv) {

        if (getOpen() > getClose()) {
            if (getOpen() < vfraction || getClose() > vfraction) {
                return false;
            }
            boolean flag = false;
            float all = getMax() - getMin();
            float upLen = getMax() - vfraction;
            float midLen = getOpen() - getClose();
            float downLen = vfraction - getMin();
            float fraction = upLen / downLen;
            return fraction >= vv;
        }

        boolean flag = false;
        float upLen = getEntityMax() - vfraction;
        float downLen = vfraction - getEntityMin();
        float fraction = upLen / downLen;
        return fraction >= vv;
    }

    public boolean isUpCrossTop(float vfraction, float vv) {

        if (getOpen() > getClose()) {
            if (getOpen() < vfraction || getClose() > vfraction) {
                return false;
            }
            boolean flag = false;
            float all = getMax() - getMin();
            float upLen = getMax() - vfraction;
            float midLen = getOpen() - getClose();
            float downLen = vfraction - getMin();
            float fraction = upLen / downLen;
            return fraction >= 0.2;
        }

        boolean flag = false;
        float upLen = getEntityMax() - vfraction;
        float downLen = vfraction - getEntityMin();
        float fraction = Math.abs(upLen / downLen);
        return fraction <= vv;
    }

    public boolean isCross(float vfraction) {
        if (getOpen() > getClose()) {
            boolean flag = false;
            float all = getMax() - getMin();
            float upLen = getMax() - getOpen();
            float midLen = getOpen() - getClose();
            float downLen = getClose() - getMin();
            float fraction = 100 * midLen / all;
            return fraction <= vfraction;
        }

        boolean flag = false;
        float all = getMax() - getMin();
        float upLen = getMax() - getClose();
        float midLen = getClose() - getOpen();
        float downLen = getOpen() - getMin();
        float fraction = 100 * midLen / all;

        if (fraction < 10 && getZhangfu() < 0.2) {
            return true;
        }
        return fraction < vfraction;
    }

    public boolean isMiddleeShadownDown() {
        return isShadownDown(50);
    }

    public boolean isShadownMonthDown() {
        return isShadownMonthDown_(70);
    }

    public boolean isShadownMonthDownNoUp() {
        return isShadownMonthDown_(70);
    }

    public boolean isShadownMonthUp(int percent) {
        if (getMopen() > getMclose()) {
            boolean flag = false;
            float all = getMmax() - getMmin();
            float upLen = getMmax() - getMopen();
            float midLen = getMopen() - getMclose();
            float fraction = 100 * upLen / all;
            return fraction > percent;
        }

        float all = getMmax() - getMmin();
        float upLen = getMmax() - getMclose();
        float fraction = 100 * upLen / all;
        return fraction > percent;
    }

    public boolean isShadownMonthDown_(int percent) {
        if (getMopen() > getMclose()) {
            boolean flag = false;
            float all = getMmax() - getMmin();
            float upLen = getMmax() - getMopen();
            float midLen = getMopen() - getMclose();
            float downLen = getMclose() - getMmin();
            float fraction = 100 * downLen / all;
            return fraction > 60;
        }

        float all = getMmax() - getMmin();
        float upLen = getMmax() - getMclose();
        float downLen = getMopen() - getMmin();
        float fraction = 100 * downLen / all;
        return fraction > percent;
    }

    public boolean isZhanging() {
        return getZhangfu() > 9.9;
    }


    public boolean isZhanging(float v) {
        return getZhangfu() > v;
    }

    public boolean isShadownUp() {
        if (getOpen() > getClose()) {
            boolean flag = false;
            float all = getMax() - getMin();
            float upLen = getMax() - getOpen();
            float midLen = getOpen() - getClose();
            float downLen = getClose() - getMin();
            float fraction = 100 * upLen / all;
            return upLen > downLen;
        }

        boolean flag = false;
        float all = getMax() - getMin();
        float upLen = getMax() - getClose();
        float downLen = getOpen() - getMin();
        float fraction = 100 * downLen / all;
        return upLen > downLen;
    }

    public boolean isShadownUp(float percent) {
        if (getOpen() > getClose()) {
            float all = getMax() - getMin();
            float upLen = getMax() - getOpen();
            float fraction = 100 * upLen / all;
            if (fraction > percent) {
                return true;
            }
            return false;
        }

        boolean flag = false;
        float all = getMax() - getMin();
        float upLen = getMax() - getClose();
        float downLen = getOpen() - getMin();
        float fraction = 100 * upLen / all;
        if (fraction > percent) {
            return true;
        }
        return false;
    }

    public boolean contain(Kline line) {
        if (getMax() > line.getMax() && getMin() < line.getMin()) {
            return true;
        }
        return false;
    }

    public boolean containVolume(Kline line) {
        if (getVolume() > line.getVolume()) {
            return true;
        }
        return false;
    }

    public float getEntityMax() {
        if (getOpen() > getClose()) {
            return getOpen();
        }
        return getClose();
    }

    public float getEntityMin() {
        if (getOpen() > getClose()) {
            return getClose();
        }
        return getOpen();
    }

    public float getEntityLen() {
        return getMax() - getMin();
    }

    public String toString() {
        return getDate() + " open:" + getOpen() + " close:" + getClose() + " max:" + getMax() + " min" + getMin() + " frc" + getZhangfu();
    }

    public Kline prev() {
        if (getIdx() - 1 < 0) {
            int a = 0;
            return null;
        }
        return allLineList.get(getIdx() - 1);
    }

    public boolean isPrevTouchMA250(int days) {
        for (int i = 0; i < days; i++) {
            Kline prev = prev(i + 1);
            float ma250 = prev.getMA250();
            float dlt250 = KLineUtil.compareMax(prev.getMax(), ma250);
            if (prev.max > ma250) {
                return true;
            }
            if (dlt250 < 2) {
                return true;
            }
        }
        return false;
    }

    public boolean isPrevTouchMA120(int days) {
        for (int i = 0; i < days; i++) {
            Kline prev = prev(i + 1);
            float ma120 = prev.getMA120();
            float dlt250 = KLineUtil.compareMax(prev.getMax(), ma120);
            if (prev.max > ma120) {
                return true;
            }
            if (dlt250 < 2) {
                return true;
            }
        }
        return false;
    }

    public boolean isZT(int days) {
        for (int i = 0; i < days; i++) {
            Kline prev = prev(i);
            float v = prev.getZhangfu();
            if (v > 8.0) {
                return true;
            }
        }
        return false;
    }

    public Kline prev(int i) {
        if (getIdx() - i < 0) {
            int a = 0;
            return null;
        }
        return allLineList.get(getIdx() - i);
    }

    public MonthKline prevMonth(int i) {
        MonthKline thisMonth = this.monthKline;
        return thisMonth.prev(i);
    }

    public MonthKline nextMonth(int i) {
        MonthKline thisMonth = this.monthKline;
        return thisMonth.next(i);
    }

    public Kline next() {
        try {
            int idx = getIdx() + 1;
            if (idx > allLineList.size() - 1) {
                return null;
            }
            return allLineList.get(idx);
        } catch (Exception e) {
            return null;
        }
    }

    public Kline next(int i) {
        try {
            return allLineList.get(getIdx() + i);
        } catch (Exception e) {
            return null;
        }
    }


    public float getMAVI(int num) {
        float avg = 0;
        int minusNum = 0;
        for (int i = 0; i < num; i++) {
            Kline item = prev(i);
            if (item == null) {
                avg += 0;
                minusNum++;
            } else {
                avg += item.getVolume();
            }

        }
        if (minusNum > 10) {
            return 0;
        }
        return avg / (num - minusNum);
    }

    public float getNextMAI(int num, float v) {
        float avg = 0;
        for (int i = 0; i < num - 1; i++) {
            Kline item = prev(i);
            avg += item.getClose();
        }
        avg += v;
        return avg / (num);
    }


    public float getMAI(int num) {
        double avg = 0;
        int minusNum = 0;
        for (int i = 0; i < num; i++) {
            Kline item = prev(i);
            if (item == null) {
                int a = 0;
                a++;
                avg += 0;
                minusNum++;
            } else {
                avg += item.getClose();
//                System.out.println(i+" "+item.getDate()+" "+item.getClose());
            }

        }
        if (minusNum > 10) {
            return 0;
        }
        return (float) (avg / (num - minusNum));
    }

    //猜测下一根线某个价格时 均线位置
    public float getNextSupposeMAI(float close, int anum) {
        float avg = 0;
        int minusNum = 0;
        int num = anum - 1;
        for (int i = 0; i < num; i++) {
            Kline item = prev(i);
            if (item == null) {
                avg += 0;
                minusNum++;
            } else {
                avg += item.getClose();
            }
        }
        avg += close;
        return avg / (anum - minusNum);
    }

    public float getNextSupposeMAI_(float close, int anum) {
        float avg = 0;
        int minusNum = 0;
        int num = anum - 1;
        for (int i = 0; i < num; i++) {
            Kline item = prev(i);
            if (item == null) {
                avg += 0;
                minusNum++;
            } else {
                avg += item.getClose();
            }
        }
        avg += close;
        if (minusNum > 0) {
            return 0;
        }
        return avg / (anum - minusNum);
    }

    public boolean hasMAI(int num) {
        for (int i = 0; i < num; i++) {
            Kline item = prev(i);
            if (item == null) {
                return false;
            } else {
            }

        }
        return true;
    }


    public float getMonthMAI(int num) {
        float avg = 0;
        int minusNum = 0;
        MonthKline thisMonth = this.monthKline;
        for (int i = 1; i < num; i++) {
            MonthKline item = prevMonth(i);
            if (item == null) {
                avg += 0;
                minusNum++;
            } else {
                avg += item.getClose();
            }
        }
        avg += this.getMclose();
        return avg / (num - minusNum);
    }

    public float getMA5() {
        return getMAI(5);
    }

    public float getMA10() {
        return getMAI(10);
    }

    public float getMA20() {
        return getMAI(20);
    }

    public float getMA30() {
        return getMAI(30);
    }

    public float getMA60() {
        return getMAI(60);
    }

    public float getMonthMA60() {
        return getMonthMAI(60);
    }

    public float getMA120() {
        return getMAI(120);
    }


    public float getNextSupposeMA5(float nextClose) {
        return getNextSupposeMAI(nextClose, 5);
    }

    public float getNextSupposeMA10(float nextClose) {
        return getNextSupposeMAI(nextClose, 10);
    }

    public float getNextSupposeMA20(float nextClose) {
        return getNextSupposeMAI(nextClose, 20);
    }

    public float getNextSupposeMA30(float nextClose) {
        return getNextSupposeMAI(nextClose, 30);
    }

    public float getNextSupposeMA60(float nextClose) {
        return getNextSupposeMAI(nextClose, 60);
    }

    public float getNextSupposeMA30_(float nextClose) {
        return getNextSupposeMAI_(nextClose, 30);
    }

    public float getNextSupposeMA60_(float nextClose) {
        return getNextSupposeMAI_(nextClose, 60);
    }

    public float getNextSupposeMA120(float nextClose) {
        return getNextSupposeMAI(nextClose, 120);
    }

    public float getNextSupposeMA120_(float nextClose) {
        return getNextSupposeMAI_(nextClose, 120);
    }


    public float getNextSupposeMA250(float nextClose) {
        return getNextSupposeMAI(nextClose, 250);
    }

    public float getNextSupposeMA250_(float nextClose) {
        return getNextSupposeMAI_(nextClose, 250);
    }

    public boolean isNextZTOnMAI() {
        float zt = getClose() * 1.1f;
        float v = getNextSupposeMA120(zt);
        float frac = KLineUtil.compareMaxSign(zt, v, getClose());
        if (frac > 3) {
            return false;
        }
        if (frac < 2) {
            return false;
        }
        return true;
    }

    public boolean hasMA120() {
        return hasMAI(120);
    }

    public float getMA250() {
        return getMAI(250);
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public float getOpen() {
        return open;
    }

    public float getSpace250() {
        float v1 = KLineUtil.compareMaxSign(getClose(), getMA250());
        return v1;
    }

    public float getSpace120(float v) {
        float v1 = KLineUtil.compareMaxSign(v, getMA120());
        return v1;
    }

    public float getSpace250(float v) {
        float v1 = KLineUtil.compareMaxSign(v, getMA250());
        return v1;
    }


    public float getSpace120() {
        float v1 = KLineUtil.compareMaxSign(getClose(), getMA120());
        return v1;
    }

    public float getSpace60() {
        float v1 = KLineUtil.compareMaxSign(getClose(), getMA60());
        return v1;
    }


    public float getSpace30() {
        float v1 = KLineUtil.compareMaxSign(getClose(), getMA30());
        return v1;
    }

    public float getSpace3060() {
        float v1 = getSpace60() - getSpace30();
        return v1;
    }

    public float getSpace30120() {
        float v1 = getSpace120() - getSpace30();
        return v1;
    }

    public float getSpace30250() {
        float v1 = getSpace250() - getSpace30();
        return v1;
    }

    public float getSpace60120() {
        float v1 = getSpace120() - getSpace60();
        return v1;
    }

    public float getSpace60250() {
        float v1 = getSpace250() - getSpace60();
        return v1;
    }

    public float getSpace120250() {
        float v1 = getSpace250() - getSpace120();
        return v1;
    }

    public int getWDeadCrossNum(int period1, int period2) {
        int num = 0;
        float vperiod1 = getMAI(period1);
        float vperiod2 = getMAI(period2);
        if (vperiod2 < vperiod1) {
            return -1;
        }

        for (int i = 0; i < 50; i++) {
            float v1 = prev(i).getMAI(period1);
            float v2 = prev(i).getMAI(period2);
            if (v2 > v1) {
                num++;
            } else {
                break;
            }
        }
        return num;
    }

    public int getDeadCrossNum(int period1, int period2) {
        int num = 0;
        float vperiod1 = getMAI(period1);
        float vperiod2 = getMAI(period2);
        if (vperiod2 < vperiod1) {
            return -1;
        }

        for (int i = 0; i < 100; i++) {
            float v1 = prev(i).getMAI(period1);
            float v2 = prev(i).getMAI(period2);
            if (v2 > v1) {
                num++;
            } else {
                break;
            }
        }
        return num;
    }

    public int getGoldCrossNum(int period1, int period2) {
        int num = 0;
        float vperiod1 = getMAI(period1);
        float vperiod2 = getMAI(period2);
        if (vperiod2 > vperiod1) {
            return -1;
        }

        for (int i = 0; i < 50; i++) {
            float v1 = prev(i).getMAI(period1);
            float v2 = prev(i).getMAI(period2);
            if (v2 < v1) {
                num++;
            } else {
                break;
            }
        }
        return num;
    }

    public boolean isCrashDownMA120250() {
        float ma120 = getMA120();
        float ma250 = getMA250();
        if (open >= ma120 && open >= ma250 && close <= ma120 && close <= ma250) {
            return true;
        }
        return false;
    }

    public float getMaxOpenClose() {
        return getOpen() > getClose() ? getOpen() : getClose();
    }

    public float getMinOpenClose() {
        return getOpen() < getClose() ? getOpen() : getClose();
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    //n 日平均乖离 SUM(close[i]-avg)/num
    public String getGuaili(int num) {
        float total = 0;
        float total2 = 0;
        for (int i = 0; i < num; i++) {
            total += prev(i).getClose();
        }
        float avg = total / num;

        for (int i = 0; i < num; i++) {
            total2 += Math.abs(prev(i).getClose() - avg);
        }
        float dlt = total2 / num;
        dlt = 100.0f * dlt / getClose();
        return StringUtil.format2_(dlt);
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public String getDate() {
        return date;
    }

    public String getMonthDate() {
        String mDateStr = DateUtil.dateToStringYM(DateUtil.strToDate4(date));
        return mDateStr;
    }

    public String getDateYM() {
        String monStr = DateUtil.dateToStringYM(DateUtil.strToDate4(date));
        return monStr;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public float getMopen() {
        return mopen;
    }

    public void setMopen(float mopen) {
        this.mopen = mopen;
    }

    public float getMclose() {
        return mclose;
    }

    public void setMclose(float mclose) {
        this.mclose = mclose;
    }

    public float getWopen() {
        return wopen;
    }

    public void setWopen(float wopen) {
        this.wopen = wopen;
    }

    public float getWclose() {
        return wclose;
    }

    public void setWclose(float wclose) {
        this.wclose = wclose;
    }

    public float getWmax() {
        return wmax;
    }

    public void setWmax(float wmax) {
        this.wmax = wmax;
    }

    public float getWmin() {
        return wmin;
    }

    public void setWmin(float wmin) {
        this.wmin = wmin;
    }


    public float compare(Kline kline) {
        float ret = 100 * (getMin() - kline.getMin()) / kline.getMin();
        return ret;
    }

    public float compareMax(Kline kline) {
        float ret = 100 * (getMax() - kline.getMax()) / kline.getMax();
        return ret;
    }


    public float beishuMaxMin(Kline kline) {
        float ret = getMax() / kline.getMin();
        return ret;
    }

    public boolean isWrap(Kline kline2) {
        Kline kline1 = this;
        if (kline1.getMax() >= kline2.getMax() && kline1.getMin() <= kline2.getMin()) {
            return true;
        }
        return false;
    }

    public boolean isIntersection(Kline kline2) {
        Kline kline1 = this;
        if (kline1.getMax() >= kline2.getMax() && kline1.getMin() <= kline2.getMin()) {
            return true;
        }

        if (kline2.getMax() >= kline1.getMax() && kline2.getMin() <= kline1.getMin()) {
            return true;
        }

        if (kline1.getMax() <= kline2.getMin()) {
            return false;
        }

        if (kline1.getOpen() >= kline2.getMax()) {
            return false;
        }

        if (kline1.getOpen() >= kline2.getMin() && kline1.getOpen() <= kline2.getMax()) {
            float tempLen = kline2.getMax() - kline1.getOpen();
            float fraction = 100 * tempLen / kline2.getEntityLen();
            return fraction > 10;
        }

        if (kline1.getClose() >= kline2.getMin() && kline1.getClose() <= kline2.getMax()) {
            float tempLen = kline1.getClose() - kline2.getMin();
            float fraction = 100 * tempLen / kline2.getEntityLen();
            return fraction > 10;
        }
        return false;
    }


    public static float getIntersection(float min, float max, float min2, float max2) {
        if (max >= max2 && min <= min2) {
            return 100;
        }

        if (max <= max2 && min >= min2) {
            float tempLen = max - min;
            float tempLen2 = max2 - min2;
            float fraction = 100 * tempLen / tempLen2;
            return fraction;
        }

        if (max <= min2) {
            return 0;
        }

        if (min >= max2) {
            return 0;
        }

        if (min >= min2 && min <= max2) {
            float tempLen = max2 - min;
            float tempLen2 = max2 - min2;
            float fraction = 100 * tempLen / tempLen2;
            return fraction;
        }

        if (max >= min2 && max <= max2) {
            float tempLen = max - min2;
            float tempLen2 = max2 - min2;
            float fraction = 100 * tempLen / tempLen2;
            return fraction;
        }

        return 0;
    }

    public boolean is30(float v) {
        Kline kline = prev(60);
        if (kline == null) {
            return false;
        }
        float min = kline.getMin();
        if (min < this.getMax() && KLineUtil.compareMax(getMax(), min) > v) {
            return true;
        }
        return false;
    }

    public boolean is60Fanbei() {
        Kline kline = prev(60);
        if (kline == null) {
            return false;
        }
        float min = kline.getMin();
        if (min < this.getMax() && KLineUtil.compareMax(getMax(), min) > 200) {
            return true;
        }
        return false;
    }


    public int isMonthEary() {
        //0000/00/00
        String str = this.date.substring(0, 7) + "/01";
        Date beginDate = DateUtil.stringToDate3(str);
        int days = DateUtil.getDayLen(beginDate, getDateObj());
        return days;

    }

    public static String getCode(String code) {
        return code.replace("SH#", "").replace("SZ#", "")
                .replace(".txt", "");
    }


    public static float getMax(float v1, float v2, float v3) {
        float v = 0;
        if (v1 > v) {
            v = v1;
        }
        if (v2 > v) {
            v = v2;
        }
        if (v3 > v) {
            v = v3;
        }
        return v;
    }

    public static float getMin(float v1, float v2, float v3) {
        float v = 100000;
        if (v1 < v) {
            v = v1;
        }
        if (v2 < v) {
            v = v2;
        }
        if (v3 < v) {
            v = v3;
        }
        return v;
    }


    public static float getMaxGuaili(float v1, float v2, float v3) {
        float max = getMax(v1, v2, v3);
        float min = getMin(v1, v2, v3);
        float v = KLineUtil.compareMax(max, min);
        return v;
    }

    public float getTouch(float v) {
        float max = Math.max(this.getMin(), this.getMax());
        float min = Math.min(this.getMin(), this.getMax());
        if (max >= v && min <= v) {
            return 0;
        } else {
            float maxF = KLineUtil.compareMax(max, v);
            float minF = KLineUtil.compareMax(min, v);
            return Math.min(maxF, minF);
        }
    }

    public boolean touch(float v) {
        float max = Math.max(this.getMin(), this.getMax());
        float min = Math.min(this.getMin(), this.getMax());
        if (max >= v && min <= v) {
            return true;
        } else {
            float maxF = KLineUtil.compareMax(max, v);
            float minF = KLineUtil.compareMax(min, v);
            if (maxF <= 1 || minF <= 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isTouchUp(float v, float fraction) {
        if (getClose() <= getOpen()) {
            return false;
        }
        if (getClose() <= v) {
            return false;
        }
        return touch(v, fraction);
    }

    public boolean touch(float v, float fraction) {
        float max = Math.max(this.getMin(), this.getMax());
        float min = Math.min(this.getMin(), this.getMax());
        if (max >= v && min <= v) {
            return true;
        } else {
            float maxF = KLineUtil.compareMax(max, v);
            float minF = KLineUtil.compareMax(min, v);
            if (maxF <= fraction || minF <= fraction) {
                return true;
            }
        }
        return false;
    }

    public boolean touchClose(float v, float fraction) {
        float max = getClose();
        float maxF = KLineUtil.compareMax(max, v);
        if (maxF <= fraction) {
            return true;
        }
        return false;
    }

    public boolean touchEntity(float v) {
        float max = Math.max(getOpen(), getClose());
        float min = Math.min(getOpen(), getClose());

        if (max >= v && min <= v) {
            return true;
        } else {
            float maxF = KLineUtil.compareMax(max, v);
            float minF = KLineUtil.compareMax(min, v);
            if (maxF <= 2 || minF <= 2) {
                return true;
            }
        }

        return false;
    }

    public boolean touchEntity(float vmin, float vmax) {
        float max = Math.max(getOpen(), getClose());
        float min = Math.min(getOpen(), getClose());
        if (min >= vmin && max <= vmax) {
            return true;
        } else {
            float minfrac = KLineUtil.compareMax(min, vmin);
            float minfrac2 = KLineUtil.compareMax(min, vmax);

            float maxfrac = KLineUtil.compareMax(max, vmin);
            float maxfrac2 = KLineUtil.compareMax(max, vmax);
            if (minfrac < 2 || minfrac2 < 2) {
                return true;
            }
            if (maxfrac < 2 || maxfrac2 < 2) {
                return true;
            }
        }

        return false;
    }


    public float getNextZF(int n) {
        float max = 0;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = next(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMax();
            float v = KLineUtil.compareMaxSign(close, open);
            if (v > max) {
                max = v;
            }
        }
        return max;
    }


    public float getPrevZFLH(int n) {
        float max = 0;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            float v = KLineUtil.compareMaxSign(open, close);
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public float getPrevZF2(int n) {
        float max = 0;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float v = item.getZhangfu();
            if (v > max) {
                max = v;
            }
        }
        return max;
    }


    public float getPrevZF(int n) {
        float max = 0;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getClose();
            float v = KLineUtil.compareMaxSign(open, close);
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public float getPrevZF3(int n) {
        float max = 0;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            float v = KLineUtil.compareMaxSign(open, close);
            if (v > max) {
                max = v;
            }
        }
        return max;
    }


    public float[] getPrevZhangFu(int n) {
        float max = 0;
        float open = this.getClose();
        Kline item = prev(n);
        if (item == null) {
            return null;
        }
        Kline maxLine = getMax(n);
        int dlt = getIdx() - maxLine.getIdx();
        Kline minLine = maxLine.getMin(n - dlt);
        float frac = KLineUtil.compareMaxSign(maxLine.getMax(), minLine.getMin());
        int dltLne = maxLine.getIdx() - minLine.getIdx() + 1;
        int dltLne2 = getIdx() - maxLine.getIdx() + 1;
        return new float[]{frac, dltLne2};
    }


    public float getPrevZFLHNoSelf(int n) {
        float max = 0;
        float min = 99999;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float amax = item.getMax();
            float amin = item.getMin();
            if (amax > max) {
                max = amax;
            }
            if (amin < min) {
                min = amin;
            }
        }
        float v = KLineUtil.compareMaxSign(max, min);
        return v;
    }

    public float getPrevZFLHIncludeSelf(int n) {
        float max = 0;
        float min = 99999;
        int maxIdx = 0;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float amax = item.getMax();
            if (amax > max) {
                max = amax;
                maxIdx = item.getIdx();
            }

        }
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            if (item.getIdx() <= maxIdx) {
                continue;
            }
            float amin = item.getMin();
            if (amin < min) {
                min = amin;
            }
        }
        float v = KLineUtil.compareMaxSign(max, min);
        return v;
    }

    public float getPrevZFLHIncludeSelfOpen(int n) {
        float max = 0;
        float min = 99999;
        int maxIdx = 0;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            if (item.getIdx() <= maxIdx) {
                continue;
            }
            float amin = item.getMin();
            if (amin < min) {
                min = amin;
            }
        }
        float v = KLineUtil.compareMaxSign(open, min);
        return v;
    }

    public float getPrevDF2(int num) {
        float min = Integer.MAX_VALUE;
        float max = Integer.MIN_VALUE;
        for (int i = 0; i < num; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float aMin = item.getMin();
            float aMax = item.getMax();
            if (aMax > max) {
                max = aMax;
            }
            if (aMin < min) {
                min = aMin;
            }
        }
        float ret = KLineUtil.compareMaxSign(min, max);
        return ret;
    }

    public float getPrevDF(int n) {
        float min = 0;
        float open = this.getMin();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float aMax = item.getMax();
            float v = KLineUtil.compareMaxSign(open, aMax);
            if (v < min) {
                min = v;
            }
        }
        min = Math.abs(min);
        return min;
    }


    public float getPrevDF3(int n) {
        float min = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float v = item.getZhangfu();
            if (v<0 && v < min) {
                min = v;
            }
        }
        min = Math.abs(min);
        return min;
    }

    public float getPrevKR(float price, int ma, int n) {
        float pixPerPrice = price * 0.25f * 2;
        pixPerPrice = 600 / pixPerPrice;
        float unitW = 12;
        float min = 0;
        float v1 = this.getMAI(ma);
        float v2 = prev(n).getMAI(ma);
        float dlt = (v1 - v2) * pixPerPrice;
        float dltW = unitW * n;
        float k = (float) Math.atan2(dlt, dltW);
        return (float) ((float) 180 * (k / Math.PI));
    }

    public float getPrevK(float price, int ma, int n) {
        float pixPerPrice = price * 0.25f * 2;
        pixPerPrice = 600 / pixPerPrice;
        float unitW = 12;
        float min = 0;
        float v1 = this.prev().getMAI(ma);
        float v2 = prev(1 + n).getMAI(ma);
        float dlt = (v1 - v2) * pixPerPrice;
        float dltW = unitW * n;
        float k = (float) Math.atan2(dlt, dltW);
        return (float) ((float) 180 * (k / Math.PI));
    }

    public float getPrevK(int ma, int n) {
        float price = prev().getClose();
        return getPrevK(price, ma, n);
    }

    public float getPrevKR(int ma, int n) {
        float price = getClose();
        return getPrevKR(price, ma, n);
    }

    public float getPrevKR_(int ma, int n) {
        float total = 0;
        for (int i = 0; i < 5; i++) {
            float v = prev(i * 2 + 4).getPrevKR(ma, n);
            total += v;
        }
        return total / 5;
    }

    public float getPrevK_(int ma, int n) {
        float total = 0;
        for (int i = 0; i < 5; i++) {
            float v = prev(i * 2 + 5).getPrevK(ma, n);
            total += v;
        }
        return total / 5;
    }

    public boolean isHor3(int n) {
        int total = 0;
        List<Integer> list = new ArrayList();
        for (int i = 0; i < n; i++) {
            float v = prev(i).getZhangfu();
            if (v >= 1) {
                list.add(total);
                total = 0;
                continue;
            }
            total++;
        }
        for (int ret : list) {
            if (ret >= 3) {
                return true;
            }
        }
        return false;
    }

    public boolean existHor(int num) {
        int total = 0;
        for (int i = 0; i < num; i++) {
            float v = Math.abs(prev(i).getZhangfu());
            if (v >= 1) {
                continue;
            }
            total++;
        }
        if (total >= num) {
            return true;
        }
        return false;
    }

    public float isHorNum(int n, int num) {
        for (int i = 0; i < n; i++) {
            boolean flag = prev(i).existHor(num);
            if (flag) {
                return i;
            }
        }
        return -1;
    }


    public float isHorNumFraction(int n, int num) {
        for (int i = 0; i < n; i++) {
            boolean flag = prev(i).existHor(num);
            if (flag) {
                return KLineUtil.compareMaxSign(close, prev(i).close);
            }
        }
        return -99;
    }


    public float getPrevDFSelf(int n) {
        float max = 0;
        float min = this.getMin();
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float amax = item.getMax();
            float v = KLineUtil.compareMaxSign2(amax, min);
            if (v > max) {
                max = v;
            }
        }
        max = Math.abs(max);
        return max;
    }

    public float getNextDF(int n) {
        float min = 0;
        float open = this.getClose();
        for (int i = 0; i < n; i++) {
            Kline item = next(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            float v = KLineUtil.compareMaxSign(close, open);
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public float getPrevEntityMAXSelf(int n) {
        float max = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float close = item.getEntityMax();
            if (close > max) {
                max = close;
            }
        }
        return max;
    }

    public float getPrevEntityMAX(int n) {
        float max = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getEntityMax();
            if (close > max) {
                max = close;
            }
        }
        return max;
    }

    public float getPrevEntityMINSelf(int n) {
        float min = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float close = item.getEntityMin();
            if (close < min) {
                min = close;
            }
        }
        return min;
    }


    public float getPrevEntityMIN(int n) {
        float min = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getEntityMin();
            if (close < min) {
                min = close;
            }
        }
        return min;
    }

    public float getMaxPrevZDFExcludeN(int n, int excludeN, float absFracton) {
        float max = 0;
        int idx = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
//            float zf = item.getEntityZhangfu();
            float frac = item.getZhangfu();
            if (Math.abs(frac) > absFracton) {
                if (idx < excludeN) {
                    idx++;
                    continue;
                }
            }
            float v = Math.abs(frac);
            if (v > max) {
                max = v;
            }
        }
        max = Math.abs(max);
        return max;
    }


    public float getPrevMAMAX(int n, int period) {

        float max = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float v = item.getMAI(period);
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public float getPrevMAX(int n) {
        float max = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMax();
            if (close > max) {
                max = close;
            }
        }
        return max;
    }

    public float getPrevMAXExcept(int n, int num, float exceptZF) {
        float max = 0;
        int tNum = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            if (item.getZhenfu() > exceptZF) {
                tNum++;
                if (tNum <= num) {
                    continue;
                }
            }
            float close = item.getMax();
            if (close > max) {
                max = close;
            }
        }
        return max;
    }

    public float getPrevMINExcept(int n, int num, float exceptZF) {
        float min = 999;
        int tNum = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            if (item.getZhenfu() > exceptZF) {
                tNum++;
                if (tNum <= num) {
                    continue;
                }
            }
            float close = item.getMax();
            if (close < min) {
                min = close;
            }
        }
        return min;
    }


    public Kline getPrevMAXKline(int n) {
        float min = Integer.MIN_VALUE;
        Kline ret = null;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMax();
            if (close > min) {
                min = close;
                ret = item;
            }
        }
        return ret;
    }

    public Kline getPrevMINKline(int n) {
        float min = Integer.MAX_VALUE;
        Kline ret = null;
        for (int i = 0; i <= n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            if (i > 5 && close < min) {
                min = close;
                ret = item;
            }
        }
        return ret;
    }

    public Kline getPrevMINKlineSelf(int n) {
        float min = Integer.MAX_VALUE;
        Kline ret = null;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            if (close < min) {
                min = close;
                ret = item;
            }
        }
        return ret;
    }

    public Kline getPrevMINKlineSelf2(int n) {
        float min = Integer.MAX_VALUE;
        Kline ret = null;
        int curIdx = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            if (close <= min) {
                min = close;
                ret = item;
                curIdx = i;
            }
        }
        return ret;
    }


    public Kline getPrevMINKlineSelf2_(int n) {
        float min = Integer.MAX_VALUE;
        Kline ret = null;
        int curIdx = 0;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            if (close <= min) {
                min = close;
                ret = item;
                curIdx = i;
            } else if ((i - curIdx) >= 5 && KLineUtil.compareMax(close, min) <= 2) {
                if (item.isMinPrev(3) && item.isMinAfter(3)) {
                    item = item.getPrevMINKlineSelf(5);
                    min = item.getClose();
                    ret = item;
                }
            }
        }
        return ret;
    }


    public int getPrevMINKlineSelfOffset(int n) {
        float min = Integer.MAX_VALUE;
        Kline ret = null;
        int retIdx = -1;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            if (close < min) {
                min = close;
                ret = item;
                retIdx = i;
            }
        }
        return retIdx;
    }

    public Kline getPrevMINKlineSelf(int n, int offset) {
        float min = Integer.MAX_VALUE;
        Kline ret = null;
        for (int i = offset; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            if (close < min) {
                min = close;
                ret = item;
            }
        }
        return ret;
    }

    public int getPrevMINKlineSelfOffset(int n, int offset) {
        float min = Integer.MAX_VALUE;
        int ret = -1;
        for (int i = offset; i < n; i++) {
            Kline item = prev(i);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            if (close < min) {
                min = close;
                ret = i;
            }
        }
        return ret;
    }

    public float getPrevMIN(int n) {
        float min = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMin();
            if (close < min) {
                min = close;
            }
        }
        return min;
    }

    public float getMinPrevEntityZhenFSelf(int n, int num) {
        float min = 9999;
        for (int i = 0; i <= num; i++) {
            float v = prev(i).getPrevEntityZhenFSelf(n);
            if (v < min) {
                min = v;
            }
        }
        return min;
    }


    public float getMinPrevEntityZhenF(int n, int num) {
        float min = 9999;
        for (int i = 0; i <= num; i++) {
            float v = prev(i).getPrevEntityZhenF(n);
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public float getMinPrevEntityZhanF(int n, int num) {
        float min = 9999;
        for (int i = 0; i <= num; i++) {
            float v = prev(i).getMaxZF(n);
            if (v < min) {
                min = v;
            }
        }
        return min;
    }


    public float getMinPrevZDFExcludeNSelf(int n, int num, int excludeN, float abs) {
        float min = 9999;
        for (int i = 0; i <= num; i++) {
            float v = prev(i).getMaxPrevZDFExcludeN(n, excludeN, abs);
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public float getMinPrevEntityZhanFSelf(int n, int num) {
        float min = 9999;
        for (int i = 0; i <= num; i++) {
            float v = prev(i).getMaxEntityZFSelf(n);
            if (v < min) {
                min = v;
            }
        }
        return min;
    }


    public float getMaxEntityZFSelf(int num) {
        float max = 0;
        for (int i = 0; i <= num; i++) {
            float v = prev(i).getEntityZhangfu();
            if (v > max) {
                max = v;
            }
        }
        return max;
    }


    public float getZhenfu(int num) {
        float max = 0;
        for (int i = 0; i <= num; i++) {
            float v = prev(i).getZhenfu();
            if (v > max) {
                max = v;
            }
        }
        return max;
    }


    public float getMaxZF(int num) {
        float max = 0;
        for (int i = 0; i <= num; i++) {
            float v = prev(i + 1).getEntityZhangfu();
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public float getPrevEntityZhenF(int n) {
        float zf = getPrevEntityMAX(n);
        float df = getPrevEntityMIN(n);
        float fv = KLineUtil.compareMax(zf, df);
        return fv;
    }

    public float getPrevEntityZhenFSelf(int n) {
        float zf = getPrevEntityMAXSelf(n);
        float df = getPrevEntityMINSelf(n);
        float fv = KLineUtil.compareMax(zf, df);
        return fv;
    }

    public float getPrevZhenF(int n) {
        float zf = getPrevMAX(n);
        float df = getPrevMIN(n);
        float fv = KLineUtil.compareMax(zf, df);
        return fv;
    }

    public float getPrevZhenFExcept(int n, int exceptNum, float azf) {
        float zf = getPrevMAXExcept(n, exceptNum, azf);
        float df = getPrevMINExcept(n, exceptNum, azf);
        float fv = KLineUtil.compareMax(zf, df);
        return fv;
    }

    public float getMmax() {
        return mmax;
    }

    public void setMmax(float mmax) {
        this.mmax = mmax;
    }

    public float getMmin() {
        return mmin;
    }

    public void setMmin(float mmin) {
        this.mmin = mmin;
    }

    public StockDayMinuteLine getStockDayMinuteLine() {
        return stockDayMinuteLine;
    }

    public void setStockDayMinuteLine(StockDayMinuteLine stockDayMinuteLine) {
        if (this.stockDayMinuteLine != null) {
            return;
        }
        this.stockDayMinuteLine = stockDayMinuteLine;
    }

    //    public float getHand() {
//        return hand;
//    }
    public float getHand(String file) {
        float totalV = GetGuben.retriveOrGet(getCode(file));
        float vv = volume / (totalV * 100000000) * 100;
        return vv;
    }

    public float getHand(float totalV) {
        float vv = volume / (totalV * 100000000) * 100;
        return vv;
    }

    public double getHandD(float totalV) {
        double vv = volume / (totalV * 100000000) * 100;
        return vv;
    }

    public void setHand(float hand) {
        this.hand = hand;
    }

    public static class ZFModel {
        public int n;
        public float v;

        public boolean isHor() {
            if (n == 4 && v < 9) {
                return true;
            }
            if (n > 6 && v < 12) {
                return true;
            }
            return false;
        }

    }

    public ZFModel getPrevZhenFContinus(int n) {
        float zfOld = 0;
        ZFModel aZFModel = new ZFModel();
        for (int i = 0; i < 5; i++) {
            float zf = getPrevZhenF(n + i);
            float frac = Math.abs(zf - zfOld);
            if (zfOld != 0) {
                if (frac < 2) {
                    aZFModel.n = n + i;
                    aZFModel.v = zf;
                }
                if (frac > 4) {
                    break;
                }
            } else {
                aZFModel.n = n + i;
                aZFModel.v = zf;
            }
            zfOld = zf;
        }
        return aZFModel;
    }


    public boolean isTouchOnMAI(int x) {
        float ma = getMAI(x);
        if (touch(ma)) {
            return true;
        }
        return false;
    }

    public boolean isTouchOnMAI10_30_60(int x) {
        boolean flag1 = isTouchOnMAI(10);
        boolean flag2 = isTouchOnMAI(30);
        boolean flag3 = isTouchOnMAI(60);
        return flag1 || flag2 || flag3;
    }

    public boolean isSZWuliang() {
        Kline prev = prev();
        float zf = getZhangfu();
        float zf2 = prev.getZhangfu();
        if (zf > 0 && zf2 > 0) {
            if (zf2 > zf && getVolume() > prev.getVolume()) {
                return true;
            }
        }
        return false;
    }

    public boolean isMAIZuli(int period) {
        float mai = getMAI(period);
        float frac = KLineUtil.compareMax(getMax(), mai);
        if (frac < 1.5f && getMax() < mai) {
            return true;
        }
        return false;
    }

    public boolean isMAIZuli30() {
        int period = 30;
        return isMAIZuli(period);
    }

    public boolean isMAIZuli60() {
        int period = 60;
        return isMAIZuli(period);
    }


    public Kline getMin(int len) {
        float min = Integer.MAX_VALUE;
        Kline minLine = null;
        for (int i = 0; i < len; i++) {
            Kline kline = prev(i);
            if (kline.getMin() < min) {
                min = kline.getMin();
                minLine = kline;
            }
        }
        return minLine;
    }

    public int getMinIdx(int fromIdx, int len) {
        float min = Integer.MAX_VALUE;
        int minLine = -1;
        for (int i = 0; i < len - fromIdx; i++) {
            Kline kline = prev(fromIdx + i);
            if (kline.getMin() < min) {
                min = kline.getMin();
                minLine = i;
            }
        }
        return minLine;
    }

    public int getMaxIdx(int fromIdx, int len) {
        float max = Integer.MAX_VALUE;
        int minLine = -1;
        for (int i = 0; i < len - fromIdx; i++) {
            Kline kline = prev(fromIdx + i);
            if (kline.getMax() > max) {
                max = kline.getMax();
                minLine = i;
            }
        }
        return minLine;
    }

    public Kline getMax(int len) {
        float v = Integer.MIN_VALUE;
        Kline maxLine = null;
        for (int i = 0; i < len; i++) {
            Kline kline = prev(i);
            if (kline.getMax() > v) {
                v = kline.getMax();
                maxLine = kline;
            }
        }
        return maxLine;
    }

    public int getMaxIdx(int len) {
        float v = Integer.MIN_VALUE;
        int maxLine = -1;
        for (int i = 0; i < len; i++) {
            Kline kline = prev(i);
            if (kline.getMax() > v) {
                v = kline.getMax();
                maxLine = i;
            }
        }
        return maxLine;
    }

    public Kline getMax2(int len, int skipIdx) {
        float v = Integer.MIN_VALUE;
        Kline maxLine = null;
        len = getIdx() - skipIdx + 1;
        for (int i = 0; i < len; i++) {
            Kline kline = prev(i);
            if (kline.getIdx() == skipIdx) {
                continue;
            }
            if (kline.getMax() > v) {
                v = kline.getEntityMax();
                maxLine = kline;
            }
        }
        return maxLine;
    }

    private static int HOR = 0;
    private static int DOWN = 1;
    private static int UP = 2;
    private static int DeepV = 3;

    public int getTrendType() {
        int flag = 0;
        int len = 10;
        Kline maxLine = getMax(len);
        Kline minLine = getMin(len);

        Kline max2 = getMax2(len, maxLine.getIdx());
        if (maxLine.getIdx() == getIdx()) {

        } else if (minLine.getIdx() == getIdx()) {

        } else if (minLine.getIdx() > maxLine.getIdx()) {
            if (max2.getIdx() > minLine.getIdx()) {

            } else {
                float lenFraction = KLineUtil.compareMax(maxLine.getMax(), minLine.getMin());
                //deep v
                if (lenFraction >= 20) {
                    float lenFraction2 = KLineUtil.compareMax(getClose(), minLine.getMin());
                    float upPercent = 100 * lenFraction2 / lenFraction;
                    float dlt = lenFraction - lenFraction2;
                    if (upPercent < 70) {
                        return getDeepV();
                    }
                    if (dlt > 2) {
                        return getDeepV();
                    }
                }
            }

        } else if (minLine.getIdx() < maxLine.getIdx()) {
            float lenFraction = KLineUtil.compareMax(minLine.getMin(), maxLine.getMax());
            if (lenFraction >= 20) {

            }
        }
        return flag;
    }

    public boolean isTupo(float v, float v2) {
        float frac = KLineUtil.compareMax(getEntityMax(), v);
        if (getEntityMax() > v) {
            if (frac < v2) {
                return true;
            }
            return false;
        }

        return false;
    }

    public boolean isTupoMA60(float v) {
        return isTupo(getMA60(), v);
    }

    public float getUpFrac(float v) {
        float frac = KLineUtil.compareMax(getEntityMax(), v);
        return frac;
    }

    public float getMA60Frac() {
        return getUpFrac(getMA60());
    }

    public float getMAXUpFrac(float v) {
        float frac = KLineUtil.compareMax(getMax(), v);
        return frac;
    }

    public float getMAXMA60Frac() {
        return getMAXUpFrac(getMA60());
    }

    public boolean szWuli() {
        Kline prev = prev();
        if (getZhangfu() < prev.getZhangfu()) {
            if (getVolume() >= prev.getVolume()) {
                return true;
            }
        }
        if (isShadownUp()) {
            float fracv = 100.0f * getVolume() / prev.getVolume();
            if (fracv > 9.0f) {
                return true;
            }
        }
        return false;
    }

    public int getMA10TrendTypeStrict(int n) {
        int up = 0;
        int down = 0;
        int hor = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i);
            float ma10 = kline.getMA10();
            float center = kline.getEntityCenter();
            if (center > ma10) {
                up++;
            } else if (center < ma10) {
                down++;
            } else {
                hor++;
            }
        }
        if (up >= 2 && down >= 2) {
            return 0;
        }
        if (down >= n - 1) {
            return 1;
        }
        if (up >= n - 1) {
            return 2;
        }
        return 0;
    }

    public int getMA10TrendType(int n) {
        int up = 0;
        int down = 0;
        int hor = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i);
            float ma10 = kline.getMA10();
            float center = kline.getEntityCenter();
            if (center > ma10) {
                up++;
            } else if (center < ma10) {
                down++;
            } else {
                hor++;
            }
        }
        if (up >= 2 && down >= 2) {
            return 0;
        }
        if (down >= n - 1) {
            return 1;
        }
        if (up >= n - 1) {
            return 2;
        }
        return 0;
    }

    public int getMA30TrendTypeStrict(int n) {
        int up = 0;
        int down = 0;
        int hor = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i);
            float ma30 = kline.getMA30();
            float center = kline.getEntityMin();
            if (center > ma30) {
                up++;
            } else if (center < ma30) {
                down++;
            } else {
                hor++;
            }
        }
        if (up > 2 && down > 2) {
            return 0;
        }
        if (down >= n - 2) {
            return 1;
        }
        if (up >= n - 2) {
            return 2;
        }
        return 2;
    }

    public int getMA30TrendType(int n) {
        int up = 0;
        int down = 0;
        int hor = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i);
            float ma30 = kline.getMA30();
            float center = kline.getEntityCenter();
            if (center > ma30) {
                up++;
            } else if (center < ma30) {
                down++;
            } else {
                hor++;
            }
        }
        if (up > 2 && down > 2) {
            return 0;
        }
        if (down >= n - 2) {
            return 1;
        }
        if (up >= n - 2) {
            return 2;
        }
        return 2;
    }

    public int getMA60TrendTypeStrict(int n) {
        int up = 0;
        int down = 0;
        int hor = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i);
            float ma60 = kline.getMA60();
            float center = kline.getEntityCenter();
            if (center > ma60) {
                up++;
            } else if (center < ma60) {
                down++;
            } else {
                hor++;
            }
        }
        if (up > 2 && down > 2) {
            return 0;
        }
        if (down >= n - 1) {
            return 1;
        }
        if (up >= n - 1) {
            return 2;
        }
        return 2;
    }

    public int getMA60TrendType(int n) {
        int up = 0;
        int down = 0;
        int hor = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i);
            float ma60 = kline.getMA60();
            float center = kline.getEntityCenter();
            if (center > ma60) {
                up++;
            } else if (center < ma60) {
                down++;
            } else {
                hor++;
            }
        }
        if (up > 2 && down > 2) {
            return 0;
        }
        if (down >= n - 1) {
            return 1;
        }
        if (up >= n - 1) {
            return 2;
        }
        return 2;
    }

    public boolean isGuaili0(int n) {
        int up = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i);
            float ma10 = kline.getMA10();
            float ma30 = kline.getMA30();
            float ma60 = kline.getMA60();
            float max = KLineUtil.getMAX(ma10, ma30, ma60);
            float min = KLineUtil.getMIN(ma10, ma30, ma60);
            float offset = KLineUtil.compareMax(max, min);
            if (offset < 1) {
                up++;
            }
        }

        if (up >= 4) {
            return true;
        }
        return false;
    }

    public float getEntityCenter() {
        float max = getEntityMax();
        float min = getEntityMin();
        return (max + min) / 2;
    }

    public int getPrevZTNum(int n) {
        int num = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i + 1);
            float zf = kline.getZhangfu();
            if (zf > 9.6) {
                num++;
            }
        }
        return num;
    }

    public int getPrevZTNumSelf(int n) {
        int num = 0;
        for (int i = 0; i < n; i++) {
            Kline kline = prev(i);
            float zf = kline.getZhangfu();
            if (zf > 9.6) {
                num++;
            }
        }
        return num;
    }


    public StockState getPOS() {
        Kline kLine = this;
        Weekline weekline = this.weekline;

        int flag = 0;
        StockState stockState = new StockState();
        float dma10 = kLine.getMA10();
        float dma30 = kLine.getMA30();
        float dma60 = kLine.getMA60();
        float dma120 = kLine.getMA120();
        float dma250 = kLine.getMA250();
        if (KLineUtil.compareMax(dma120, dma250) < 2) {
            if (dma120 > dma250) {
                KLineUtil.getStockState(kLine, stockState, dma120, dma250, false);
            } else if (dma120 < dma250) {
                KLineUtil.getStockState(kLine, stockState, dma250, dma120, true);
            }
        } else if (dma120 > dma250) {
            KLineUtil.getStockState(kLine, stockState, dma120, dma250, false);
        } else if (dma120 < dma250) {
            KLineUtil.getStockState(kLine, stockState, dma250, dma120, true);
        }


        float ma10 = weekline.getMA10();
        float ma30 = weekline.getMA30();
        float ma60 = weekline.getMA60();
        float ma120 = weekline.getMA120();
        float ma250 = weekline.getMA250();
        float open = weekline.getOpen();

        float highest = weekline.getPrevMAX(50);
        float diefu = KLineUtil.compareMax(open, highest);
        if (open < highest && diefu > 35) {
            float guaili120_250 = KLineUtil.compareMax(ma120, ma250);
            if (guaili120_250 < 1) {
                if (open > ma120 && open > ma250) {
                    float mai = Math.max(ma120, ma250);
                    float frac = KLineUtil.compareMax(open, mai);
                    if (frac > 15) {
                        stockState.weekPos = KLineUtil.ONE;
                    }
                }
            } else {

            }
        }

        stockState.enable = true;
        return stockState;
    }

    public boolean prevHasDF(int num, float v, boolean self) {
        int offset = self ? 0 : 1;
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i + offset);
            if (kline.getZhangfu() < 0 && Math.abs(kline.getZhangfu()) >= v) {
                return true;
            }
        }
        return false;
    }

    public boolean prevHasZF(int num, float v, boolean self) {
        int offset = self ? 0 : 1;
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i + offset);
            if (kline.getZhangfu() > 0 && Math.abs(kline.getZhangfu()) >= v) {
                return true;
            }
        }
        return false;
    }


    public int fallWithNoUp() {
        boolean flag = false;
        flag = prevHasDF(3, 4, false);
        boolean flag2 = !prevHasZF(3, 5, false);
        if (flag && flag2) {
            return 1;
        }
        return 0;
    }

    public boolean isMonthEnd(int num) {
        int mnum = DateUtil.getDaysOfMonth(date);
        int adate = DateUtil.getDate(DateUtil.stringToDate3(date));
        if (mnum - adate <= num) {
            return true;
        }
        return false;
    }

    public boolean equal(float v1, float v2) {
        float ret = KLineUtil.compareMax(v1, v2);
        return ret < 0.3;
    }

    public boolean bigger(float v1, float v2) {
        boolean flag = equal(v1, v2);
        if (flag) {
            return false;
        }
        float ret = KLineUtil.compareMax(v1, v2);
        if (v1 > v2) {
            return ret > 0.3;
        }
        return false;
    }

    public boolean smaller(float v1, float v2) {
        boolean flag = equal(v1, v2);
        if (flag) {
            return false;
        }
        float ret = KLineUtil.compareMax(v1, v2);
        if (v1 < v2) {
            return ret > 0.3;
        }
        return false;
    }

    public boolean isGuailiChange(List<Float> vs, float frac) {
        float lastFraction = vs.get(vs.size() - 1);
        float lastDlt = 0;
        List<Float> vs2 = new ArrayList<>();
        for (int i = 0; i < vs.size() - 1; i++) {
            float dlt = (vs.get(i + 1) - vs.get(i));
            lastDlt = dlt;
            vs2.add(dlt);
        }
        if (vs.size() == 3) {
            if (vs.get(1) < 0.5) {
                if (vs2.get(0) < 0 && vs2.get(1) > 0) {
                    return true;
                }
            }
        }

        int sign = 1;
        List<Integer> vs3 = new ArrayList<>();
        for (int i = 0; i < vs2.size(); i++) {
            float dlt = vs2.get(i);
            if (dlt < 0) {
                sign = -1;
            } else if (dlt > 0) {
                sign = 1;
            }
            vs3.add(sign);
        }
        int negCount = 0;
        int objCount = 0;
        int eqCount = 0;
        for (int i = 0; i < vs3.size(); i++) {
            int asign = vs3.get(i);
            if (asign < 0) {
                negCount++;
            }
            if (asign > 0) {
                objCount++;
            }
            if (asign == 0) {
                eqCount++;
            }
        }
        if (negCount == vs3.size()) {
            float nextFraction = lastFraction - Math.abs(lastDlt);
            if (nextFraction < frac) {
                return true;
            }
            return false;
        }
        if (eqCount == vs3.size()) {
            return true;
        }
        if (objCount == vs3.size()) {
            return false;
        }
        return false;
    }

    public int isStandardNextGuailiChange() {
        boolean flag = false;
        flag = isNextGuailiChange(30, 60);
        if (flag) {
            return 5;
        }
        flag = isNextGuailiChange(10, 60);
        if (flag) {
            return 2;
        }
        flag = isNextGuailiChange(10, 120);
        if (flag) {
            return 2;
        }
        flag = isNextGuailiChange(10, 30);
        if (flag) {
            return 2;
        }
        return 0;
    }

    public int isNextGuailiChange() {
        boolean flag = false;
        if (date.equalsIgnoreCase("2023/07/20")) {
            int a = 0;
        }

//        if(isDeadCrossAll(30, 60)) {
//            return  0;
//        }

        flag = isNextGuailiChange(30, 60);
        if (flag) {
            return 10;
        }
        flag = isNextGuailiChange(10, 30);
        if (flag) {
            return 4;
        }
        flag = isNextGuailiChange(10, 60);
        if (flag) {
            if (isNextZTStandMA120()) {
                return 6;
            }
            if (isNextZTStandMA250()) {
                return 6;
            }
            return 4;
        }
        flag = isNextGuailiChange(10, 120);
        if (flag) {
            boolean flag2 = isRealGuailiChange(30, 60);
            if (flag2) {
                return 2;
            }
            flag2 = prev().isRealGuailiChange(30, 60);
            if (flag2) {
                return 2;
            }
            flag2 = prev(2).isRealGuailiChange(30, 60);
            if (flag2) {
                return 2;
            }
            return 0;
        }


        if (isNextZTStandMA250()) {
            for (int i = 0; i < 4; i++) {
                Kline kline = prev(i);
                float frac = kline.getMA1MA2Guaili(30, 60);
                boolean flag3 = kline.isRealGuailiChange(30, 60);
                if (flag3) {
                    return 2;
                }
                flag3 = kline.isRealGuailiChange(10, 60);
                if (flag3) {
                    return 2;
                }
            }
        }

        float zf = getMinPrevEntityZhanFSelf(3, 0);
        if (zf < 0.5) {
            for (int i = 0; i < 4; i++) {
                Kline kline = prev(i);
                boolean flag3 = kline.isRealGuailiChange(30, 60);
                if (flag3) {
                    return 2;
                }
                flag3 = kline.isRealGuailiChange(10, 60);
                if (flag3) {
                    return 2;
                }
                flag3 = kline.isRealGuailiChange(10, 30);
                if (flag3) {
                    return 2;
                }
            }
        }


        return 0;
    }

    public float getMA1MA2Guaili(int ma1, int ma2) {
        float v1ma1 = getMAI(ma1);
        float v1ma2 = getMAI(ma2);
        float frac = KLineUtil.compareMax(v1ma1, v1ma2);
        return frac;
    }

    public boolean isRealGuailiChange(int ma1, int ma2) {
        float v1ma1 = getMAI(ma1);
        float v1ma2 = getMAI(ma2);
        float frac = KLineUtil.compareMax(v1ma1, v1ma2);
        float v0ma1 = prev().getMAI(ma1);
        float v0ma2 = prev().getMAI(ma2);
        float dlt = (v1ma1 - v1ma2) / v1ma2 * 100;
        float dltPrev = (v0ma1 - v0ma2) / v0ma2 * 100;
        if (dltPrev <= 0 && (dlt >= 0 || Math.abs(dlt) < 0.15)) {
            return true;
        }
        return false;
    }

    public boolean isNextGuailiChange(int ma1, int ma2) {
        float ztPrice = getClose() * 1.1f;
        float v1ma1 = getNextMAI(ma1, ztPrice);
        float v1ma2 = getNextMAI(ma2, ztPrice);

        float v0ma1 = getMAI(ma1);
        float v0ma2 = getMAI(ma2);
        float dlt = (v1ma1 - v1ma2) / v1ma2 * 100;
        float dltPrev = (v0ma1 - v0ma2) / v0ma2 * 100;
        if (dltPrev <= 0 && (dlt >= 0 || Math.abs(dlt) < 0.15)) {
            return true;
        }
        return false;
    }

    public int isTouchEndMAUpOrDown(float v, int offset) {
        boolean flag = false;
        for (int i = 0; i < 10; i++) {
            if (prev(i + offset + 1).getMax() > v && KLineUtil.compareMax(prev(i + offset + 1).getMax(), v) > 5) {
                return -1;
            }
            if (prev(i + offset + 1).getMax() < v && KLineUtil.compareMax(prev(i + offset + 1).getMax(), v) > 5) {
                return 1;
            }
        }
        return 0;
    }


    public boolean isGoldOrComingGold(int num, int ma1, int ma2) {
        float v0ma1 = getMAI(ma1);
        float v0ma2 = getMAI(ma2);
        boolean flag = false;
        for (int i = 0; i < 5; i++) {
            float v0ma1_ = prev(i + 1).getMAI(ma1);
            float v0ma2_ = prev(i + 1).getMAI(ma2);
            if (KLineUtil.compareMax(v0ma1_, v0ma2_) > 0.5f) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return false;
        }
        int offset2 = 99;
        if (v0ma1 < v0ma2) {
            for (int i = 0; i < 20; i++) {
                float v0ma1_ = prev(i + 1).getMAI(ma1);
                float v0ma2_ = prev(i + 1).getMAI(ma2);
                if (v0ma1_ >= v0ma2_) {
                    offset2 = i;
                    break;
                }
            }
        }
        if (offset2 <= 10) {
            return false;
        }

        if (v0ma1 > v0ma2) {
            int offset = 999;
            for (int i = 0; i < 20; i++) {
                float v0ma1_ = prev(i + 1).getMAI(ma1);
                float v0ma2_ = prev(i + 1).getMAI(ma2);
                if (v0ma1_ <= v0ma2_) {
                    offset = i;
                    break;
                }
            }
            if (offset < num) {
                return true;
            } else {
                return false;
            }
        }
        if (v0ma1 > v0ma2 && KLineUtil.compareMax(v0ma1, v0ma2) < 0.5f) {
            return true;
        }
        float ztPrice = getClose() * 1.1f;
        float v1ma1Zt = getNextMAI(ma1, ztPrice);
        float v1ma2Zt = getNextMAI(ma2, ztPrice);
        if (v1ma1Zt >= v1ma2Zt) {
            return true;
        }
        if (KLineUtil.compareMax(v1ma1Zt, v1ma2Zt) < 0.5f) {
            return true;
        }
        return false;
    }

    public Object[] getMinPointMA(int ma) {
        float min = 99;
        String minReal = "";
        int offset = -1;
        for (int i = 0; i < 10; i++) {
            float v0ma1_ = prev(i + 1).getMAI(ma);
            if (KLineUtil.compareMax(prev(i + 1).close, v0ma1_) < min) {
                offset = i;
                min = KLineUtil.compareMax(prev(i + 1).close, v0ma1_);
                minReal = StringUtil.format2_(KLineUtil.compareMaxSign(prev(i + 1).close, v0ma1_));
            }
        }
        int offset2 = -1;
        float max = 0;
        String maxReal = "";
        for (int i = 0; i < 10; i++) {
            float v0ma1_ = prev(i + 1).getMAI(ma);
            if (KLineUtil.compareMax(prev(i * 2 + 1).close, v0ma1_) > max) {
                offset2 = i;
                max = KLineUtil.compareMax(prev(i * 2 + 1).close, v0ma1_);
                maxReal = StringUtil.format2_(KLineUtil.compareMaxSign(prev(i * 2 + 1).close, v0ma1_));
            }
        }

        return new Object[]{offset, minReal, offset2, maxReal};
    }

    public boolean isDeadCrossAll(int ma1, int ma2) {
        for (int i = 0; i < 3; i++) {
            if (prev(i).isDeadCross(ma1, ma2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeadCross(int ma1, int ma2) {
        float v1ma1 = getMAI(ma1);
        float v1ma2 = getMAI(ma2);
        float v0ma1 = prev().getMAI(ma1);
        float v0ma2 = prev().getMAI(ma2);
        //v0 is prev
        float dltPrev = (v0ma1 - v0ma2) / v0ma2 * 100;
        float dlt = (v1ma1 - v1ma2) / v1ma2 * 100;
        boolean flag1 = dltPrev > 0 && (dlt <= 0 || Math.abs(dlt) < 0.15);
        boolean flag2 = dltPrev >= 0 && dlt < 0;
        if (flag1 || flag2) {
            return true;
        }
        return false;
    }

    public boolean isDeadCrpssesAll(int ma1, int ma2, int ma3, int ma4, float dltFrac) {

        return false;
    }

    public boolean isDeadCrpsses(int ma1, int ma2, float dltFrac) {
        List<Float> vs = new ArrayList<>();
        List<Float> dltVs = new ArrayList<>();
        float retFrac = 1;
        Kline prev1 = this;
        Kline prev2 = prev();

        for (int i = -2; i <= 0; i++) {
            Kline next = next(i);
            if (next == null) {
                return false;
            }
            float dlt = (next.getMAI(ma1) - next.getMAI(ma2)) / next.getMAI(ma2) * 100;
            vs.add(dlt);
        }

        float cur = vs.get(vs.size() - 1);
        if (cur > 3) {
            return false;
        }
        if (cur < -3) {
            return false;
        }

        for (int i = 0; i < vs.size() - 1; i++) {
            float dlt = vs.get(i + 1) - vs.get(i);
            dltVs.add(dlt);
        }

        boolean flag = true;
        for (int i = 0; i < dltVs.size() - 1; i++) {
            float dlt = dltVs.get(i);
            if (dlt > 0) {
                return false;
            }
            if (dlt < 0 && Math.abs(dlt) < dltFrac) {
                flag = false;
            }

            if (-dlt * 4 < vs.get(vs.size() - 1)) {
                return false;
            }
        }

        return flag;
    }

    public boolean isGuailiChange(int ma1, int ma2) {
        List<Float> vs = new ArrayList<>();
        float retFrac = 1;
        Kline prev1 = this;
        Kline prev2 = prev();
        if (prev1.getZhangfuAbs() < 1 && prev2.getZhangfuAbs() < 1) {
            retFrac = 2;
        }
        for (int i = -1; i <= 1; i++) {
            Kline next = next(i);
            if (next == null) {
                return false;
            }
            float dlt = Math.abs(next.getMAI(ma1) - next.getMAI(ma2)) / next.getMAI(ma2) * 100;
            vs.add(dlt);
        }
        boolean aflag = true;
        for (int i = 0; i < vs.size(); i++) {
            float dlt = vs.get(i);
            if (dlt > 0.5) {
                aflag = false;
            }
        }
        if (Math.abs(vs.get(0)) < 0.3) {
            if (Math.abs(vs.get(vs.size() - 1)) > 0.3) {
                aflag = true;
            }
        }
        if (aflag) {
            return true;
        }
        boolean flag = isGuailiChange(vs, retFrac);
        return flag;
    }

    public boolean isGuailiChange_m10_m30() {
        return isGuailiChange(10, 30);
    }


    public boolean isGuailiChange_m10_m120() {
        return isGuailiChange(10, 120);
    }

    public boolean isGuailiChange_m120_m250() {
        return isGuailiChange(120, 250);
    }

    public boolean isGuailiChange_m10_m60() {
        return isGuailiChange(10, 60);
    }

    public boolean isGuailiChange_m60_m250() {
        return isGuailiChange(60, 250);
    }

    public boolean isGuailiChange_m60_m120() {
        return isGuailiChange(60, 120);
    }

    public boolean isGuailiChange_m30_m250() {
        return isGuailiChange(30, 250);
    }

    public boolean isGuailiChange_m30_m60() {
        return isGuailiChange(30, 60);
    }


    public boolean isGuailiChange() {
        if (isGuailiChange_m10_m120()) {
            return true;
        }
        if (isGuailiChange_m120_m250()) {
            return true;
        }
        if (isGuailiChange_m10_m60()) {
            return true;
        }
        if (isGuailiChange_m60_m250()) {
            return true;
        }
        if (isGuailiChange_m60_m120()) {
            return true;
        }
        if (isGuailiChange_m30_m250()) {
            return true;
        }
        if (isGuailiChange_m30_m60()) {
            return true;
        }
        return false;
    }


    public boolean hasHigherForAfter(int num) {
        try {


            boolean flag = true;
            int cnt = 0;
            int cnt2 = 0;

            for (int i = 0; i < 2; i++) {
                Kline kline = next(i + 1);
                if (kline == null) {
                    break;
                }
                float min = kline.getEntityMax();
                if (min >= getMax()) {
                    cnt++;
                }
            }
            if (cnt >= 2) {
                return true;
            }

            Kline next1 = next(1);
            Kline next2 = next(2);
            Kline next3 = next(3);
            if (next1.getZhangfu() < 0) {
                cnt2++;
            }
            if (next2.getZhangfu() < 0) {
                cnt2++;
            }
            if (next3.getZhangfu() < 0) {
                cnt2++;
            }
            if (cnt2 >= 2) {
                return false;
            }

            for (int i = 0; i < 6; i++) {
                Kline kline = next(i + 1);
                if (kline == null) {
                    break;
                }
                float min = kline.getEntityMin();
                if (min >= getMax()) {
                    cnt++;
                }
            }
            if (cnt >= num) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 6根线之内 乖离减少到最低， 寻找是否即将金叉
     */
    public boolean nextMa30LagerThanOther(int period, int num, float fracv) {
        boolean flag = true;
        int cnt = 0;
        int cnt2 = 0;
        float ma30_ = getMAI(30);
        float mai_ = getMAI(period);
        if (ma30_ > mai_) {
            float frac = KLineUtil.compareMax(ma30_, mai_);
            return false;
        }
        for (int i = 0; i < 8; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float ma30 = kline.getMAI(30);
            float mai = kline.getMAI(period);
            float frac = KLineUtil.compareMax(ma30, mai);
            if (frac < fracv) {
                return true;
            }
        }
        return false;
    }

    /**
     * MA30是否即将与其他线金叉
     */
    public boolean nextMa30LagerThanOther(int num) {
        boolean flag = true;
        int cnt = 0;
        int cnt2 = 0;
        float gFrac = 0.6f;
        boolean flag1 = nextMa30LagerThanOther(60, num, gFrac);
        boolean flag2 = nextMa30LagerThanOther(120, num, gFrac);
        boolean flag3 = nextMa30LagerThanOther(250, num, gFrac);
        if (flag1) {
            return flag1;
        }
        if (flag2) {
            return flag2;
        }
        if (flag3) {
            return flag3;
        }
        return false;
    }

    public boolean nextMa10LagerThanOther(int num) {
        boolean flag = true;
        int cnt = 0;
        int cnt2 = 0;
        float gFrac = 0.6f;
        boolean flag1 = nextMa30LagerThanOther(10, num, gFrac);
        boolean flag2 = nextMa30LagerThanOther(30, num, gFrac);
        boolean flag3 = nextMa30LagerThanOther(60, num, gFrac);
        if (flag1) {
            return flag1;
        }
        if (flag2) {
            return flag2;
        }
        if (flag3) {
            return flag3;
        }
        return false;
    }

    public boolean hasPrevZT(int num) {
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i + 1);
            if (kline == null) {
                break;
            }
            float frac = kline.getZhangfu();
            if (frac > 7.5) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPrevZT2(int num) {
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i + 1);
            if (kline == null) {
                break;
            }
            float frac = kline.getMAXZhangfu2();
            if (frac > 9) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNextZT(int num) {
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float frac = kline.getZhangfu();
            if (frac > 9.94) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNextZFLarge(int num) {
        float open = this.getOpen();
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float max = kline.getMax();
            float frac = KLineUtil.compareMax(max, open);
            if (frac > 9.94) {
                return true;
            }
        }
        return false;
    }


    public boolean hasNextZF(int num, float frac) {
        float v0 = getMin();
        float v2 = getClose();
        for (int i = 0; i <= num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float frac2 = kline.getZhangfu();
            if (frac2 > 0) {
                v2 = kline.getMax();
            } else {
                break;
            }
        }

        float ret = KLineUtil.compareMax(v2, v0);
        return ret >= frac;
    }

    public boolean hasNextZF(int num, int subNum, float frac) {
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            boolean flag = kline.hasNextZF(subNum, frac);
            if (flag) {
                return flag;
            }
        }
        return false;
    }

    public boolean hasNextFall(int num, float fracv) {
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }

            float frac = KLineUtil.compareMaxSign(kline.getMin(), getClose());
            if (frac < -fracv) {
                return true;
            }
        }
        return false;
    }

    static class RET {
        public boolean flag;
        public Kline kline;
    }

    public RET hasUpToMAI(int num, int period, float fracv) {
        RET ret = new RET();
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float ma = kline.getMAI(period);
            if (kline.prev().getZhangfu() > 0 && kline.getEntityMax() > ma) {
                ret.flag = true;
                ret.kline = kline;
                return ret;
            }
        }
        return ret;
    }

    public RET hasNextFallToMA250(int num, float fracv) {
        RET ret = new RET();
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float ma = kline.getMA250();
            float frac = KLineUtil.compareMax(kline.getMin(), ma);
            if (frac < fracv) {
                ret.flag = true;
                ret.kline = kline;
                return ret;
            }
        }
        return ret;
    }

    public boolean hasNextFallToMA30(int num, float fracv) {
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float ma30 = kline.getMA30();
            float frac = KLineUtil.compareMax(kline.getMin(), ma30);
            if (frac < fracv) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFallDownMAI(int period, int num, float fracv) {
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float mai = kline.getMAI(period);
            float frac = KLineUtil.compareMax(kline.getMin(), mai);
            if (frac < fracv) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFallDown(int period, int num, float fracv) {
        boolean flag = false;
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float mai = kline.getMAI(period);
            float frac = KLineUtil.compareMax(kline.getMin(), this.getClose());
            if (kline.getMin() >= getClose()) {
                continue;
            }
            if (frac > fracv) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPrevUpMAI(int period, int num, float fraction) {
        for (int i = 0; i < num; i++) {
            Kline kline = next(i + 1);
            if (kline == null) {
                break;
            }
            float mai = kline.getMAI(period);
            if (kline.getMax() < mai) {
                continue;
            }
            float frac = KLineUtil.compareMax(kline.getMax(), mai);
            if (frac < fraction) {
                return true;
            }
        }
        return false;
    }

    public boolean isVolumnLarge() {
        Kline prevLine = prev();
        long v0 = prevLine.getVolume();
        long v1 = getVolume();
        if (v1 > v0) {
            return true;
        }
        return false;
    }

    public boolean isVolumnLarge2() {
        Kline prevLine = prev();
        long v0 = prevLine.getVolume() / 10000;
        long v1 = getVolume() / 10000;
        float v3 = (float) (1.0f * v1 / v0);
        if (v3 > 1.5) {
            return true;
        }
        return false;
    }

    public boolean isMa30SLantDown() {
        Kline prevLine = next(0);
        Kline curLine = next(1);
        Kline nextLine = next(2);
        float prevMA = prevLine.getMA30();
        float nextMA = nextLine.getMA30();
        float curMA = curLine.getMA30();
        float slant1 = curMA - prevMA;
        float slant2 = nextMA - curMA;
        float fv = slant2 / slant1;
        if (slant1 < 0.002 && slant2 < 0.001) {
            return false;
        }
        if (fv < 0.6) {
            return true;
        }
        return false;
    }

    public float getGuaili(int period, int period2) {
        float v1 = getMAI(period);
        float v2 = getMAI(period2);
        float ret = KLineUtil.compareMax(v1, v2);
        return ret;
    }

    public float getJumpZF() {
        float min = getMin();
        float prev = prev().getEntityMax();
        return KLineUtil.compareMaxSign(min, prev);
    }

    public float getCDPercent(Kline kline) {
        if (kline.getMin() > getMax()) {
            return 0;
        }
        if (kline.getMmax() < getMin()) {
            return 0;
        }
        float min = Math.max(this.getMin(), kline.getMin());
        float max = Math.min(this.getMax(), kline.getMax());
        float len = max - min;
        float frac = len / (max - min);
        return frac;
    }

    public boolean preHasZT(int num) {
        for (int i = 0; i < num; i++) {
            Kline item = prev(i + 1);
            if (item.getZhangfu() >= 9.5) {
                float cd = getCDPercent(item);
                if (cd >= 1.0) {
                    return true;
                }
            }
        }
        return false;
    }

    public float getMinspanceMA() {
        float ma30space = 0;
        float ma60space = 0;
        float ma120space = 0;
        float ma250space = 0;
        if (getOpen() < getMA30()) {
            ma30space = KLineUtil.compareMax(getOpen(), getMA30());
        }
        if (getOpen() < getMA60()) {
            ma60space = KLineUtil.compareMax(getOpen(), getMA60());
        }
        if (getOpen() < getMA120()) {
            ma120space = KLineUtil.compareMax(getOpen(), getMA120());
        }
        if (getOpen() < getMA250()) {
            ma250space = KLineUtil.compareMax(getOpen(), getMA250());
        }

        float minspace = KLineUtil.min(ma30space, ma60space, ma120space, ma250space);
        return minspace;
    }


    public float getMinspanceDownMA() {
        float ma10space = 99;
        float ma30space = 99;
        float ma60space = 99;
        float ma120space = 99;
        float ma250space = 99;
        boolean flag = false;
        if (KLineUtil.compareMax(min, getMA10()) < 0.5 && getClose() > getMA10()) {
            flag = true;
            ma10space = KLineUtil.compareMax(getClose(), getMA10());
        }
        if (KLineUtil.compareMax(min, getMA30()) < 0.5 && getClose() > getMA30()) {
            flag = true;
            ma30space = KLineUtil.compareMax(getClose(), getMA30());
        }
        if (KLineUtil.compareMax(min, getMA60()) < 0.5 && getClose() > getMA60()) {
            flag = true;
            ma60space = KLineUtil.compareMax(getClose(), getMA60());
        }
        if (KLineUtil.compareMax(min, getMA120()) < 0.5 && getClose() > getMA120()) {
            flag = true;
            ma120space = KLineUtil.compareMax(getClose(), getMA120());
        }
        if (KLineUtil.compareMax(min, getMA250()) < 0.5 && getClose() > getMA250()) {
            flag = true;
            ma250space = KLineUtil.compareMax(getClose(), getMA250());
        }
        if (!flag) {
            return -1;
        }
        float minspace = KLineUtil.min(ma10space, ma30space, ma60space, ma120space, ma250space);
        return minspace;
    }

    public boolean allMAOK() {
        float ma10 = getMA10();
        float ma30 = getMA30();
        float ma60 = getMA60();
        float ma120 = getMA120();
        if (ma10 <= ma30) {
            return false;
        }
        if (ma10 <= ma60) {
            return false;
        }
        if (ma30 <= ma60) {
            return false;
        }
        if (ma30 <= ma120) {
            return false;
        }
        return true;
    }

    public boolean allMA103060120OK() {
        float ma10 = getMA10();
        float ma30 = getMA30();
        float ma60 = getMA60();
        float ma120 = getMA120();
        if (ma10 <= ma30) {
            return false;
        }
        if (ma10 <= ma60) {
            return false;
        }
        if (ma30 <= ma60) {
            return false;
        }
        if (ma30 <= ma120) {
            return false;
        }
        if (ma60 <= ma120) {
            return false;
        }
        return true;
    }


    public boolean isALlLineOK() {
        List<Float> vs = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            Kline prev = prev(i + 1);
            if (prev == null) {
                return false;
            }
            if (!prev.allMAOK()) {
                return false;
            }
        }
        return true;
    }


    public boolean allMAERR() {
        float ma10 = getMA10();
        float ma30 = getMA30();
        float ma60 = getMA60();
        float ma120 = getMA120();
        float ma250 = getMA250();
        if (ma10 >= ma30) {
            return false;
        }
        if (ma10 >= ma60) {
            return false;
        }
        if (ma30 >= ma60) {
            return false;
        }
        if (ma30 >= ma120) {
            return false;
        }
        if (ma120 >= ma250) {
            return false;
        }

        return true;
    }

    public boolean allMA103060120ERR() {
        float ma10 = getMA10();
        float ma30 = getMA30();
        float ma60 = getMA60();
        float ma120 = getMA120();
        if (ma10 >= ma30) {
            return false;
        }
        if (ma10 >= ma60) {
            return false;
        }
        if (ma30 >= ma60) {
            return false;
        }
        if (ma30 >= ma120) {
            return false;
        }
        if (ma60 >= ma120) {
            return false;
        }
        return true;
    }

    public boolean isALlLineERR() {
        for (int i = 0; i <= 3; i++) {
            Kline prev = prev(i + 1);
            if (prev == null) {
                return false;
            }
            if (!prev.allMAERR()) {
                return false;
            }
        }
        return true;
    }


    public boolean isALlLineOKAndOneCross() {
        List<Float> vs = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            Kline prev = prev(i + 1);
            if (prev == null) {
                return false;
            }
            if (!prev.allMA103060120OK()) {
                return false;
            }
            if (prev(3).getMA120() > prev(3).getMA250()) {
                return false;
            }
            if (Math.abs(KLineUtil.compareMaxSign(getMA120(), getMA250())) > 0.2) {
                return false;
            }
            return true;
        }
        return true;
    }

    public boolean hasDZ(int num, float frac) {
        float fracV = this.getPrevZF(num);
        if (fracV > frac) {
            return true;
        }
        return false;
    }

    public boolean nextTouchUPMA(int num, int period) {
        for (int i = 0; i < num; i++) {
            Kline item = next(i + 1);
            if (item == null) {
                return false;
            }
            if (item.getClose() > item.getMAI(period)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUPTouchToMA(int num, int period) {
        for (int i = 0; i < num; i++) {
            Kline item = next(i + 1);
            if (!item.isShadownUp(50)) {
                continue;
            }
            if (item.getMin() < item.getMA10()) {
                continue;
            }
            if (item.getMin() < item.getMA30()) {
                continue;
            }
            if (item.getMin() < item.getMA60()) {
                continue;
            }
            if ((KLineUtil.compareMax(item.getMax(), item.getMAI(period)) < 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean nextIsBlackSun() {
        float ma120 = getMA120();
        float ma250 = getMA250();
        if (getClose() < ma120) {
            if (KLineUtil.compareMax(ma120, getClose()) < 3) {
                Kline next = next();
                if ((next.isShadownUp(30)) && next.getEntityMin() > next.getMA120()) {
                    return true;
                }
            }
        }
        if (getClose() < ma250) {
            if (KLineUtil.compareMax(ma250, getClose()) < 3) {
                Kline next = next();
                if ((next.getZhangfu() < 0) && next.getEntityMin() > next.getMA250()) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean hasTopLines(int num11, int num) {
        try {
            int cnt = 0;
            for (int i = 0; i < num11; i++) {
                Kline item = next(i + 1);
                if (item == null) {
                    int a = 0;
                }
                if (item.getClose() > getClose()) {
                    cnt++;
                }
            }
            if (cnt >= num) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public float nextSpaceSingle(int num, int period) {
        Kline item = next(num + 1);
        return KLineUtil.compareMax(item.getMin(), item.getMAI(period));
    }

    public float nextSpace(int num, int period) {
        float minV = 999;
        for (int i = 0; i < num; i++) {
            float tmp = nextSpaceSingle(i, period);
            if (tmp < minV) {
                minV = tmp;
            }
        }
        return minV;
    }

    public float nextSpace(int num) {
        float minV = 999;
        float tmp = nextSpace(num, 30);
        if (tmp < minV) {
            minV = tmp;
        }

        tmp = nextSpace(num, 60);
        if (tmp < minV) {
            minV = tmp;
        }

        tmp = nextSpace(num, 120);
        if (tmp < minV) {
            minV = tmp;
        }

        tmp = nextSpace(num, 250);
        if (tmp < minV) {
            minV = tmp;
        }

        return minV;

    }


    public boolean hasNOmoreMax(int num) {
        float max = 0;
        for (int i = 0; i < num; i++) {
            Kline item = prev(i + 1);
            if (item == null) {
                break;
            }
            float close = item.getMax();
            if (close > max) {
                max = close;
            }
        }
        if (KLineUtil.compareMax(max, this.getMax()) < 20) {
            return true;
        }
        return false;
    }

    public float guailiMI(int period) {
        return KLineUtil.compareMax(getOpen(), this.getMAI(period));
    }

    public boolean isStandMA3060_() {
        float ma30 = getMAI(30);
        float ma60 = getMAI(60);
        float ma120 = getMAI(120);
        float v30 = KLineUtil.compareMaxSign(getClose(), ma30);
        float v60 = KLineUtil.compareMaxSign(getClose(), ma60);
        if (v30 > 0 && Math.abs(v30) < 1) {
            return true;
        }
        if (v60 > 0 && Math.abs(v60) < 1) {
            return true;
        }
        return false;
    }

    public float[] isStandMA3060() {
        float ma30 = getMAI(30);
        float ma60 = getMAI(60);
        float ma120 = getMAI(120);
        float v30 = KLineUtil.compareMaxSign(getClose(), ma30);
        float v60 = KLineUtil.compareMaxSign(getClose(), ma60);
        float v30_ = KLineUtil.compareMaxSign(getOpen(), ma30);
        float v60_ = KLineUtil.compareMaxSign(getOpen(), ma60);
        float v120_ = KLineUtil.compareMaxSign(getOpen(), ma120);
        return new float[]{v30_, v60_, v30, v60};
    }

    public float[] isStand120250() {
        float ma60 = getMAI(60);
        float ma120 = getMAI(120);
        float ma250 = getMAI(250);

        float v120 = KLineUtil.compareMaxSign(getClose(), ma120);
        float v250 = KLineUtil.compareMaxSign(getClose(), ma250);

        float v120_ = KLineUtil.compareMaxSign(getOpen(), ma120);
        float v250_ = KLineUtil.compareMaxSign(getOpen(), ma250);

        return new float[]{0, v120_, v250_, 0, v120, v250};
    }


    public float fixV(boolean isSZ, float v) {
        if (!isSZ) {
            return v;
        }
        if (v < 0 && Math.abs(v) < 0.5) {
            return 0.11f;
        }
        return v;
    }

    public float[] isStandMA60120250Fix() {
        float ma60 = getMAI(60);
        float ma120 = getMAI(120);
        float ma250 = getMAI(250);
        boolean isSZ = getZhangfu() > 0;
        float ma250_ = prev().getMAI(250);

        float v60 = fixV(isSZ, KLineUtil.compareMaxSign(getClose(), ma60));
        float v120 = fixV(isSZ, KLineUtil.compareMaxSign(getClose(), ma120));
        float v250 = fixV(isSZ, KLineUtil.compareMaxSign(getClose(), ma250));
        float v60_ = KLineUtil.compareMaxSign(getOpen(), ma60);
        float v120_ = KLineUtil.compareMaxSign(getOpen(), ma120);
        float v250_ = KLineUtil.compareMaxSign(getOpen(), ma250);

        float v250m_ = 99;
        if (prev().isShadownDown(50) && prev().getZhangfu() < 1) {
            v250m_ = KLineUtil.compareMaxSign(prev().getMin(), ma250_);
        }
        return new float[]{v60_, v120_, v250_, v60, v120, v250, v250m_};
    }


    public float[] isStandMA60120250() {
        float ma60 = getMAI(60);
        float ma120 = getMAI(120);
        float ma250 = getMAI(250);
        boolean isSZ = getZhangfu() > 0;
        float ma250_ = prev().getMAI(250);

        float v60 = KLineUtil.compareMaxSign(getClose(), ma60);
        float v120 = KLineUtil.compareMaxSign(getClose(), ma120);
        float v250 = KLineUtil.compareMaxSign(getClose(), ma250);

        float v60_ = KLineUtil.compareMaxSign(getOpen(), ma60);
        float v120_ = KLineUtil.compareMaxSign(getOpen(), ma120);
        float v250_ = KLineUtil.compareMaxSign(getOpen(), ma250);

        float v250m_ = 99;
        if (prev().isShadownDown(50) && prev().getZhangfu() < 1) {
            v250m_ = KLineUtil.compareMaxSign(prev().getMin(), ma250_);
        }
        return new float[]{v60_, v120_, v250_, v60, v120, v250, v250m_};
    }

    public float[] isStandMA120250() {
        float ma120 = getMAI(120);
        float ma250 = getMAI(250);

        float ma250_ = prev().getMAI(250);

        float v120 = KLineUtil.compareMaxSign(getClose(), ma120);
        float v250 = KLineUtil.compareMaxSign(getClose(), ma250);

        float v120_ = KLineUtil.compareMaxSign(getOpen(), ma120);
        float v250_ = KLineUtil.compareMaxSign(getOpen(), ma250);

        float v250m_ = 99;
        if (prev().isShadownDown(50) && prev().getZhangfu() < 1) {
            v250m_ = KLineUtil.compareMaxSign(prev().getMin(), ma250_);
        }
        return new float[]{v120_, v250_, v120, v250, v250m_};
    }

    class MAI {
        float space;
        int period;

        public MAI(float space, int period) {
            this.space = space;
            this.period = period;
        }
    }

    public boolean isCross(int period) {
        float ma = getMAI(period);
        if (getOpen() < ma && getClose() >= ma) {
            return true;
        }
        if (getOpen() < ma && KLineUtil.compareMax(getClose(), ma) < 1.0) {
            return true;
        }
        return false;
    }

    public boolean isCross2(int period) {
        float ma = getMAI(period);
        if (getOpen() < ma && getClose() >= ma) {
            return true;
        }
        if (getOpen() < ma && KLineUtil.compareMax(getClose(), ma) < 1.0) {
            return true;
        }
        return false;
    }

    public boolean isCross() {
        return isCross(120) && isCross(250);
    }

    public List<MAI> isStandMAI(int type) {
//        float ma10 = getMAI(10);
        float ma30 = getMAI(30);
        float ma60 = getMAI(60);
        float ma120 = getMAI(120);
        float ma250 = getMAI(250);

        float v = getOpen();
        if (type == 1) {
            v = getClose();
        }
//        MAI v10_ = new MAI(KLineUtil.compareMaxSign(v, ma10), 10);
        MAI v30_ = new MAI(KLineUtil.compareMaxSign(v, ma30), 30);
        MAI v60_ = new MAI(KLineUtil.compareMaxSign(v, ma60), 60);
        MAI v120_ = new MAI(KLineUtil.compareMaxSign(v, ma120), 120);
        MAI v250_ = new MAI(KLineUtil.compareMaxSign(v, ma250), 250);
        List<MAI> list = new ArrayList<>();
//        if (v10_.space >= 0) {
//            list.add(v10_);
//        }
        if (v30_.space >= 0) {
            list.add(v30_);
        }

        if (v60_.space >= 0) {
            list.add(v60_);
        }

        if (v120_.space >= 0) {
            list.add(v120_);
        }

        if (v250_.space >= 0) {
            list.add(v250_);
        }


        list.sort(new Comparator<MAI>() {
            @Override
            public int compare(MAI o1, MAI o2) {
                return (int) (o1.space - o2.space);
            }
        });
        return list;
    }

    public float spaceDownTouchMAI(float vv, int period) {
        try {
            Kline item = this;
            float tt = item.getMAI(period);
            float v = KLineUtil.compareMaxSign(vv, tt);
            return v;
        } catch (Exception e) {

        }
        return 999;
    }

    public float spaceDownTouchMAI(int period) {
        try {
            Kline item = this;
            float tt = item.getMAI(period);
            float v = KLineUtil.compareMaxSign(item.getMin(), tt);
            return v;
        } catch (Exception e) {

        }
        return 999;
    }


    public int dayTouchMA250(int len, int period){
        int dayLen = -1;
        for(int i=0; i<len; i++) {
            Kline kline = prev(i);
            float periodV = kline.getMAI(period);
            float frac = Math.abs(KLineUtil.compareMaxSign(kline.getMax(), periodV));
            if(kline.max>=periodV && kline.min<=periodV) {
                return i;
            }
            if(frac<0.7f) {
                return i;
            }

        }
        return -1;
    }

    public float spaceDownTouchMAIByEntityMin(int period) {
        try {
            Kline item = this;
            float tt = item.getMAI(period);
            float v = KLineUtil.compareMaxSign(item.getEntityMin(), tt);
            return v;
        } catch (Exception e) {

        }
        return 999;
    }

    public boolean allSpaceDownTouchMAI250() {
        float dspace = spaceDownTouchMAI(250);
        float wspace = weekline.spaceDownTouchMAI(250);
        if (dspace < 2 && wspace < 2) {
            return true;
        }
        return false;
    }

    public float[] spaceDownTouchMAI250_() {
        boolean flag = false;
        float[] vs = spaceDownTouchMAI250();
        float v = vs[0];
        int v2 = (int) vs[1];
        if (v2 == 120 || v2 == 250) {
            if (v < 5) {
                return vs;
            }
        }

        vs = prev().spaceDownTouchMAI250();
        v = vs[0];
        v2 = (int) vs[1];
        if (v2 == 120 || v2 == 250) {
            if (v < 5) {
                return vs;
            }
        }

        vs = prev(2).spaceDownTouchMAI250();
        v = vs[0];
        v2 = (int) vs[1];
        if (v2 == 120 || v2 == 250) {
            if (v < 5) {
                return vs;
            }
        }

        return vs;

    }

    public float[] spaceDownTouchMAI250ByEntityMin() {
        float v250 = spaceDownTouchMAIByEntityMin(250);
        float v120 = spaceDownTouchMAIByEntityMin(120);
//        float v60 = spaceDownTouchMAIByOpen(60);
//        float v30 = spaceDownTouchMAIByOpen(30);
        float v = 99;
        int period = 0;
//        if (v30 > 0 && v30 < v) {
//            v = v30;
//            period = 30;
//        }
//        if (v60 > 0 && v60 < v) {
//            v = v60;
//            period = 60;
//        }
        if (v120 > 0 && v120 < v) {
            v = v120;
            period = 120;
        }
        if (v250 > 0 && v250 < v) {
            v = v250;
            period = 250;
        }

        return new float[]{v, period};
    }

    public float[] spaceALlDownTouchMAI250ByEntityMin() {
        float v250 = spaceDownTouchMAIByEntityMin(250);
        float v120 = spaceDownTouchMAIByEntityMin(120);
        float v60 = spaceDownTouchMAIByEntityMin(60);
//        float v30 = spaceDownTouchMAIByOpen(30);
        float v = 99;
        int period = 0;
//        if (v30 > 0 && v30 < v) {
//            v = v30;
//            period = 30;
//        }
        if (v60 > 0 && v60 < v) {
            v = v60;
            period = 60;
        }
        if (v120 > 0 && v120 < v) {
            v = v120;
            period = 120;
        }
        if (v250 > 0 && v250 < v) {
            v = v250;
            period = 250;
        }

        return new float[]{v, period};
    }

    public float[] spaceDownTouchMAI250() {
        float v250_ = spaceDownTouchMAI(getEntityMax(), 250);
        float v120_ = spaceDownTouchMAI(getEntityMax(), 120);
        float v60_ = spaceDownTouchMAI(getEntityMax(), 60);
        float v30_ = spaceDownTouchMAI(getEntityMax(), 30);


        float v250 = spaceDownTouchMAI(250);
        float v120 = spaceDownTouchMAI(120);
        float v60 = spaceDownTouchMAI(60);
        float v30 = spaceDownTouchMAI(30);
        float v = 99;
        int period = 0;

        if (v30_ > -0.3f && v30_ < v) {
            v = v30_;
            period = 30;
        }
        if (v60_ > -0.3f && v60_ < v) {
            v = v60_;
            period = 60;
        }
        if (v120_ > -0.3f && v120_ < v) {
            v = v120_;
            period = 120;
        }
        if (v250_ > -0.3f && v250_ < v) {
            v = v250_;
            period = 250;
        }

        if (v30 > 0 && v30 < v) {
            v = v30;
            period = 30;
        }
        if (v60 > 0 && v60 < v) {
            v = v60;
            period = 60;
        }
        if (v120 > 0 && v120 < v) {
            v = v120;
            period = 120;
        }
        if (v250 > 0 && v250 < v) {
            v = v250;
            period = 250;
        }

        return new float[]{v, period};
    }

    public boolean isSupposeNextCrossPrice(float v, float nextSupposeClose) {
        if (getOpen() < v && nextSupposeClose >= v) {
            return true;
        }
        return false;
    }

    public class StandResult {
        public StandResult(int period, float mai) {
            this.period = period;
            this.mai = mai;
        }

        private int period;
        private float mai;

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public float getMai() {
            return mai;
        }

        public void setMai(float mai) {
            this.mai = mai;
        }

        public String toString() {
            return "" + period + " " + mai;
        }
    }

    public boolean containsMA120250UP(float zt) {
        List<StandResult> list = spaceUpTouchMA(zt);
        boolean flag = false;
        if (list.size() == 2) {
            for (StandResult standResult : list) {
                if (standResult.period == 120) {
                    flag = true;
                }
                if (standResult.period == 250) {
                    flag = true;
                }
            }
            float frac = 0;
            if (flag) {
                frac = Math.abs(list.get(0).getMai() - list.get(1).getMai());
                if (frac < 0.3) {
                    return false;
                }
            }
        }


        if (list.size() > 1) {
            for (StandResult standResult : list) {
                if (standResult.period == 120) {
                    return true;
                }
                if (standResult.period == 250) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<StandResult> spaceUpTouchMAWeek(float zt) {
        List<StandResult> list = new ArrayList<>();
        float v250 = prev().getNextSupposeMA250_(getOpen());
        if (isSupposeNextCrossPrice(v250, zt)) {
            list.add(new StandResult(250, v250));
        }

        float v120 = prev().getNextSupposeMA120_(getOpen());
        if (isSupposeNextCrossPrice(v120, zt)) {
            list.add(new StandResult(120, v120));
        }
        float v60 = prev().getNextSupposeMA60_(getOpen());
        if (isSupposeNextCrossPrice(v60, zt)) {
            list.add(new StandResult(60, v60));
        }
        list.sort(new Comparator<StandResult>() {
            @Override
            public int compare(StandResult o1, StandResult o2) {
                return (int) (o1.mai - o2.mai);
            }
        });
        return list;
    }

    public List<StandResult> spaceDownTouchMA() {
        List<StandResult> list = new ArrayList<>();
        float vOpen = getOpen();

        float v250 = prev().getNextSupposeMA250_(getOpen());
        list.add(new StandResult(250, KLineUtil.compareMaxSign(vOpen, v250)));

        float v120 = prev().getNextSupposeMA120_(getOpen());
        list.add(new StandResult(120, KLineUtil.compareMaxSign(vOpen, v120)));

        float v60 = prev().getNextSupposeMA60_(getOpen());
        list.add(new StandResult(60, KLineUtil.compareMaxSign(vOpen, v60)));

        float v30 = prev().getNextSupposeMA30_(getOpen());
        list.add(new StandResult(30, KLineUtil.compareMaxSign(vOpen, v30)));
        list.sort(new Comparator<StandResult>() {
            @Override
            public int compare(StandResult o1, StandResult o2) {
                return (int) (o1.mai - o2.mai);
            }
        });
        return list;
    }

    public List<StandResult> spaceMinUpTouchMA() {
        List<StandResult> list = new ArrayList<>();
        float vOpen = getOpen();

        float min = -99;
        int period = 0;

        float v250 = prev().getNextSupposeMA250_(getOpen());
        float v120 = prev().getNextSupposeMA120_(getOpen());
        float v60 = prev().getNextSupposeMA60_(getOpen());
        float v30 = prev().getNextSupposeMA30_(getOpen());

        float tmp = KLineUtil.compareMaxSign(vOpen, v250);
        if (tmp < 0 && tmp > min) {
            period = 250;
            min = tmp;
        }
        tmp = KLineUtil.compareMaxSign(vOpen, v120);
        if (tmp < 0 && tmp > min) {
            period = 120;
            min = tmp;
        }
        tmp = KLineUtil.compareMaxSign(vOpen, v60);
        if (tmp < 0 && tmp > min) {
            period = 60;
            min = tmp;
        }
        tmp = KLineUtil.compareMaxSign(vOpen, v30);
        if (tmp < 0 && tmp > min) {
            period = 30;
            min = tmp;
        }

        list.add(new StandResult(period, min));
        return list;
    }

    public List<StandResult> spaceMinDownTouchMA() {
        List<StandResult> list = new ArrayList<>();
        float vOpen = getOpen();

        float min = 99;
        int period = 0;

        float v250 = prev().getNextSupposeMA250_(getOpen());
        float v120 = prev().getNextSupposeMA120_(getOpen());
        float v60 = prev().getNextSupposeMA60_(getOpen());
        float v30 = prev().getNextSupposeMA30_(getOpen());

        float tmp = KLineUtil.compareMaxSign(vOpen, v250);
        if (tmp > 0 && tmp < min) {
            period = 250;
            min = tmp;
        }
        tmp = KLineUtil.compareMaxSign(vOpen, v120);
        if (tmp > 0 && tmp < min) {
            period = 120;
            min = tmp;
        }
        tmp = KLineUtil.compareMaxSign(vOpen, v60);
        if (tmp > 0 && tmp < min) {
            period = 60;
            min = tmp;
        }
        tmp = KLineUtil.compareMaxSign(vOpen, v30);
        if (tmp > 0 && tmp < min) {
            period = 30;
            min = tmp;
        }

        list.add(new StandResult(period, min));
        return list;
    }


    public List<StandResult> spaceUpTouchMA(float zt) {
        List<StandResult> list = new ArrayList<>();
        float v250 = prev().getNextSupposeMA250_(getOpen());
        float vOpen = getOpen();
        if (isSupposeNextCrossPrice(v250, zt)) {
            list.add(new StandResult(250, KLineUtil.compareMaxSign(vOpen, v250)));
        }

        float v120 = prev().getNextSupposeMA120_(getOpen());
        if (isSupposeNextCrossPrice(v120, zt)) {
            list.add(new StandResult(120, KLineUtil.compareMaxSign(vOpen, v120)));
        }
        float v60 = prev().getNextSupposeMA60_(getOpen());
        if (isSupposeNextCrossPrice(v60, zt)) {
            list.add(new StandResult(60, KLineUtil.compareMaxSign(vOpen, v60)));
        }
        float v30 = prev().getNextSupposeMA30_(getOpen());
        if (isSupposeNextCrossPrice(v30, zt)) {
            list.add(new StandResult(30, KLineUtil.compareMaxSign(vOpen, v30)));
        }
        list.sort(new Comparator<StandResult>() {
            @Override
            public int compare(StandResult o1, StandResult o2) {
                return (int) (o1.mai - o2.mai);
            }
        });
        return list;
    }


    public float[] spaceDownTouchMAI2502() {
        float v250 = spaceDownTouchMAI(250);
        float v = 99;
        int period = 0;
        if (v250 > 0 && v250 < v) {
            v = v250;
            period = 250;
        }

        float v250_ = spaceDownTouchMAI(getClose(), 250);
        if (v250_ > 0 && v250_ < v) {
            v = v250_;
            period = 250;
        }
        return new float[]{v, period};
    }

    public float[] spaceDownTouchMAI_(int aperiod) {
        float v250 = spaceDownTouchMAI(aperiod);
        float v = 99;
        int period = 0;
        if (v250 > 0 && v250 < v) {
            v = v250;
            period = aperiod;
        }

        float v250_ = spaceDownTouchMAI(getClose(), aperiod);
        if (v250_ > -0.2f && v250_ < v) {
            v = v250_;
            period = aperiod;
        }
        return new float[]{v, period};
    }

    public float spaceUpTouchMAI(int period) {
        try {
            Kline item = this;
            float tt = item.getMAI(period);
            float v = KLineUtil.compareMaxSign(item.getOpen(), tt);
            return v;
        } catch (Exception e) {

        }
        return 999;
    }

    public float spaceUpTouchMAI2(int period) {
        try {
            Kline item = this;
            float tt = item.getMAI(period);
            float v = KLineUtil.compareMaxSign(item.getMax(), tt);
            return v;
        } catch (Exception e) {

        }
        return 999;
    }


    public float[] spaceUpTouchMAI250() {
        float v250 = spaceUpTouchMAI(250);
        float v120 = spaceUpTouchMAI(120);
        float v60 = spaceUpTouchMAI(60);
        float v30 = spaceUpTouchMAI(30);
        float v = Integer.MIN_VALUE;
        int period = 0;
        if (v30 < 0 && v30 > v) {
            v = v30;
            period = 30;
        }
        if (v60 < 0 && v60 > v) {
            v = v60;
            period = 60;
        }
        if (v120 < 0 && v120 > v) {
            v = v120;
            period = 120;
        }
        if (v250 < 0 && v250 > v) {
            v = v250;
            period = 250;
        }
        return new float[]{Math.abs(v), period};
    }


    public boolean isGongDownToMAAndUpMAPrevent() {
        boolean flag = false;
        float ret[] = spaceDownTouchMAI250();
        if (ret[1] == 250 && ret[0] > 8) {
            flag = true;
        }

        boolean flag2 = false;
        float ret2[] = spaceUpTouchMAI250();
        if (ret2[1] == 120 && ret2[0] > 0 && ret2[0] < 10) {
            flag2 = true;
        }
        if (flag2 && flag) {
            return true;
        }
        return false;
    }

    public Kline nextDownTouchMAI(int num, int period) {
        try {
            Kline minLine = null;
            float minV = 999;
            for (int i = 0; i < num; i++) {
                Kline item = next(i + 1);
                float tt = item.getMAI(period);
                float v = KLineUtil.compareMax(item.getMin(), tt);
                if (v < minV) {
                    minV = v;
                    minLine = item;
                }
                if (item.getEntityMin() < tt && item.getEntityMax() > tt) {
                    minV = 0;
                    minLine = item;
                }
            }
            if (minV < 2) {
                return minLine;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public Kline getNextLineOK(int num) {
        Kline kline = null;
        List<Float> vs = new ArrayList<>();
        for (int i = 0; i <= num; i++) {
            Kline next = next(i + 1);
            if (next == null) {
                return null;
            }
            if (next.allMA103060120OK()) {
                return next;
            }
        }
        return kline;
    }

    public Kline getNextLineDownTouchMA(int num, int period) {
        Kline kline = null;
        List<Float> vs = new ArrayList<>();
        for (int i = 0; i <= num; i++) {
            Kline next = next(i + 1);
            if (next == null) {
                return null;
            }
            if (KLineUtil.compareMax(next.getMin(), next.getMAI(period)) < 1) {
                return next;
            }
        }
        return kline;
    }


//    public Kline getNextLineDownFirstUpMA250SecondOK2(int num) {
//        Kline kline = null;
//        List<Float> vs = new ArrayList<>();
//        for (int i = 0; i <= num; i++) {
//            Kline next = next(i + 1);
//            if (next == null) {
//                return null;
//            }
//
//            String space = "dPrevMa10(1) < dPrevMa120(1)  & dNextMa10(0) >= dNextMa120(0) && space(dma30, dma120) < 1 && dclose > dma120";
//            if (next.date.equalsIgnoreCase("2023/07/19")) {
//                int a = 0;
//            }
//            boolean flag11 = KLineUtil.testWithError(space, next, weekline, false);
//            if (flag11) {
//                return next.next();
//            }
//
//
//        }
//        return kline;
//    }
//
//    public Kline getNextLineDownFirstUpMA250SecondOK(int num) {
//        Kline kline = null;
//        List<Float> vs = new ArrayList<>();
//        for (int i = 0; i <= num; i++) {
//            Kline next = next(i + 1);
//            if (next == null) {
//                return null;
//            }
//
//            String space = "dPrevMa10(1) < dPrevMa30(1)  & dNextMa10(1) > dPrevMa30(1) && space(dma30, dma60) < 1 && dclose > dma250";
//            boolean flag11 = KLineUtil.testWithError(space, next, weekline, false);
//            if (flag11) {
//                return next.next();
//            }
//
//            String space2 = "dPrevMa10(1) <= dPrevMa30(1)  & dNextMa10(1) >= dPrevMa30(1) && space(dma30, dma60) < 1 && dclose > dma250";
//            boolean flag111 = KLineUtil.testWithError(space, next, weekline, false);
//            if (flag111) {
//                return next.next();
//            }
//        }
//        return kline;
//    }

    public boolean isGuailiMA120250ContiniusLarge(int num1, int num) {
        try {
            Kline kline = null;
            List<Float> vs = new ArrayList<>();
            for (int i = num1; i <= num; i++) {
                Kline next = next(i + 1);
                if (next == null) {
                    return false;
                }
                float ma250 = next.getMA250();
                float ma120 = next.getMA120();
                float dlt = KLineUtil.compareMax(ma120, ma250);
                vs.add(dlt);
            }
            float prevV = 0;
            if (vs.get(1) < vs.get(0)) {
                if (vs.get(vs.size() - 1) > vs.get(vs.size() - 2)) {
                    return false;
                }
            }
            for (int i = 0; i <= vs.size() - 1; i++) {
                float vv = vs.get(i);
                float vv2 = vs.get(i + 1);
                float dlt = vv2 - vv;
                if (dlt > 0) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public float getSpace(int p1, int p2) {
        return KLineUtil.compareMax(getMAI(p1), getMAI(p2));
    }

    public boolean isGuailiMA1MA2ContiniusLarge(int p1, int p2, int num1, int num) {
        try {
            Kline kline = null;
            List<Float> vs = new ArrayList<>();
            for (int i = num1; i <= num; i++) {
                Kline next = next(i + 1);
                if (next == null) {
                    return false;
                }
                float ma2 = next.getMAI(p2);
                float ma1 = next.getMAI(p1);
                float dlt = KLineUtil.compareMax(ma1, ma2);
                vs.add(dlt);
            }
            float prevV = 0;
            if (vs.get(1) < vs.get(0)) {
                if (vs.get(vs.size() - 1) > vs.get(vs.size() - 2)) {
                    return false;
                }
            }
            for (int i = 0; i <= vs.size() - 1; i++) {
                float vv = vs.get(i);
                float vv2 = vs.get(i + 1);
                float dlt = vv2 - vv;
                if (dlt > 0) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    //p1<p2
    public boolean testGoldCross(int p1, int p2, int off1, int off2) {
        float mav1 = prev(off1).getMAI(p1);
        float mav2 = prev(off1).getMAI(p2);

        float mav1_ = prev(off2).getMAI(p1);
        float mav2_ = prev(off2).getMAI(p2);

        if (mav1 < mav2 && mav1_ > mav2_) {
            return true;
        }
        return false;
    }

    public boolean testDeadGoldCross(int p1, int p2, int off1, int off2) {
        float mav1 = prev(off1).getMAI(p1);
        float mav2 = prev(off1).getMAI(p2);

        float mav1_ = prev(off2).getMAI(p1);
        float mav2_ = prev(off2).getMAI(p2);

        if (mav1 > mav2 && mav1_ < mav2_) {
            return true;
        }
        return false;
    }

    public float getZT() {
        return prev().getClose() * 1.1f;
    }

    public float getZTSelf() {
        return getClose() * 1.1f;
    }

    public float getZTZT() {
        return prev().getClose() * 1.1f * 1.1f;
    }

    public boolean isDownAndUpToMA(int num, int period) {
        Kline bottom = getPrevMINKline(num);
        float max60 = getPrevMAMAX(num, period);
        if (bottom.getMin() > max60) {
            return false;
        }
        float frac = KLineUtil.compareMax(bottom.getMin(), max60);
        if (frac < 20) {
            return false;
        }
        float ztzt = getZTZT();
        if (ztzt < max60) {
            return false;
        }
        float frac2 = KLineUtil.compareMax(ztzt, max60);
        if (frac2 < 0.5) {
            return false;
        }
        return true;
    }

    public float getDF(int num) {
        Kline bottom = getPrevMINKline(num);
        float max = getPrevMAX(num);
        if (bottom.getMin() >= max) {
            return 0;
        }
        float frac = KLineUtil.compareMax(bottom.getMin(), max);
        return frac;
    }

    public float getZF_(int num) {
        Kline bottom = getPrevMINKline(num);
        Kline max = getPrevMAXKline(num);
        if (max == null) {
            return 0;
        }
        if (bottom == null) {
            return 0;
        }
        float frac = KLineUtil.compareMax(max.getMax(), bottom.getMin());
        return frac;
    }

    public boolean hasDazhang(int num, float v) {
        Kline kline = null;
        for (int i = 0; i <= num; i++) {
            kline = prev(i + 1);
            if (kline.getZhangfu() > v) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDadie(int num, float v) {
        Kline kline = null;
        for (int i = 0; i <= num; i++) {
            kline = prev(i + 1);
            float frac = kline.getZhangfu();
            if (frac < 0 && Math.abs(frac) >= v) {
                return true;
            }
        }
        return false;
    }

    public boolean isJumpMA250() {
        if (getOpen() < getMA250()) {
            return false;
        }
        if (KLineUtil.compareMax(getOpen(), prev().getClose()) > 2) {
            return true;
        }
        return false;
    }

    public boolean isJumpMAI(int v, float jumpV) {
        if (getOpen() < getMAI(v)) {
            return false;
        }
        if (KLineUtil.compareMax(getOpen(), getMAI(v)) >= 0.5f) {
            return false;
        }
        if (KLineUtil.compareMax(getOpen(), prev().getClose()) >= jumpV) {
            return true;
        }
        return false;
    }

    public boolean isNextZTStandMA250() {
        float ztPrice = getClose() * 1.1f;
        float ztMA250 = getNextMAI(250, ztPrice);
        if (KLineUtil.compareMax(ztPrice, ztMA250) < 0.7f) {
            return true;
        }
        return false;
    }

    public boolean isNextZTStandMA120() {
        float ztPrice = getClose() * 1.1f;
        float ztMA250 = getNextMAI(120, ztPrice);
        if (KLineUtil.compareMax(ztPrice, ztMA250) < 0.5f) {
            return true;
        }
        return false;
    }

    public boolean isStandMA250() {
        float v1 = getMA250();
        if (getOpen() < v1) {
            return false;
        }
        if (KLineUtil.compareMax(getOpen(), v1) < 1) {
            return true;
        }
        return false;
    }

    public boolean isStandMA250__(float frac) {
        boolean flag = isStandMA250(frac);
        if (flag) {
            return flag;
        }
        flag = prev().isStandMA250(frac);
        if (flag) {
            return flag;
        }
        flag = prev(2).isStandMA250(frac);
        if (flag) {
            return flag;
        }
        return false;
    }

    public boolean isOpenStandMA120() {
        float v1 = getMA120();
        float v10 = getMA10();
        float v30 = getMA30();
        float v60 = getMA60();
        if (getZhangfu() > 0) {
            if (getOpen() > v1 && KLineUtil.compareMax(getOpen(), v1) < 1.5) {
                return true;
            }
            if (getClose() > v1 && KLineUtil.compareMax(getOpen(), v1) < 1.5) {
                return true;
            }
        } else {
            if (getOpen() > v1 && KLineUtil.compareMax(getOpen(), v1) < 1.5) {
                return true;
            }
            if (getClose() > v1 && KLineUtil.compareMax(getOpen(), v1) < 1.5) {
                return true;
            }
            if (getOpen() > v1 && getClose() < v1) {
                if (KLineUtil.compareMax(getClose(), v10) > 0 && KLineUtil.compareMax(getClose(), v10) < 2.5f) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isStandMA120() {
        float v1 = getMA120();
        if (getZhangfu() > 0) {
            if (getOpen() > v1 && KLineUtil.compareMax(getOpen(), v1) < 1.5) {
                return true;
            }
            if (getClose() > v1 && KLineUtil.compareMax(getOpen(), v1) < 1.5) {
                return true;
            }
        } else {
            if (getClose() > v1 && KLineUtil.compareMax(getClose(), v1) < 1.5) {
                return true;
            }
        }
        return false;
    }


    public boolean isStandMA250(float frac) {
        float v1 = getMA250();
        if (getOpen() < v1) {
            return false;
        }
        if (getClose() < v1) {
            return false;
        }

        if (KLineUtil.compareMax(getMin(), v1) < frac) {
            return true;
        }
        return false;
    }

    public boolean isStandMA120ZULI() {
        float v1 = getMA120();
        if (getOpen() > v1) {
            return false;
        }
        if (KLineUtil.compareMaxSign(getZT(), v1) <= -3) {
            return false;
        }
        if (KLineUtil.compareMax(v1, getZT()) < 4) {
            return true;
        }
        return false;
    }

    public String getFormatLine() {
        return String.format("%s\t%.2f\t%.2f\t%.2f\t%.2f\t%s\t%d\t%.2f", date, open, max, min, close, "" + volume, 0, hand);
    }

    public float getSlant(int period, int n) {
        float ma250 = getMA250();
        float nextN = KLineUtil.compareMaxSign3(ma250, next(n).getMAI(period));
        float prevN = KLineUtil.compareMaxSign3(ma250, prev(n).getMAI(period));
        float slant = (nextN - prevN) / (2 * n + 1);
        float angle = 3 * (float) (180 * Math.atan(slant) / Math.PI);
        return angle;
    }

    public float getABSSlant(int period, int n) {
        float ma250 = getMA250();
        float nextN = KLineUtil.compareMaxSign3(next(n).getMAI(period), ma250);
        float prevN = KLineUtil.compareMaxSign3(prev(n).getMAI(period), ma250);
        float slant = Math.abs((nextN - prevN) / (0.5f * (2 * n + 1)));
        float angle = (float) (180 * Math.atan(slant) / Math.PI);
        return angle;
    }


    public boolean allIsLessAbsThan(int num, int exceptNum, float fracv) {
        int tNum = 0;
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i);
            if (kline == null) {
                break;
            }
            float frac = Math.abs(kline.getZhangfu());
            if (frac > fracv) {
                tNum++;
            }
        }
        return tNum <= exceptNum;
    }

    /**
     * 排除exceptSZNum根上涨>szFrac  false
     * 排除exceptXDNum根下跌>xdFrac  false
     * 排除exceptNum根ABS 涨幅>fracv  false
     * 某一根跌幅达到 dfFrac  false
     *
     * @param num
     * @param exceptNum
     * @param fracv
     * @return
     */
    public boolean allIsLessAbsThanExcept(int num, int exceptNum, float fracv, int exceptSZNum, float szFrac, int exceptXDNum, float xdFrac, float maxDF) {
        int tNum = 0;
        int tNumSZ = 0;
        int tNumXD = 0;
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i);
            if (kline == null) {
                break;
            }
            float frac = kline.getZhangfu();
            float fracAbs = Math.abs(frac);
            if (exceptNum >= 0) {
                if (fracAbs > fracv) {
                    tNum++;
                }
            }
            if (exceptSZNum > 0) {
                if (frac > 0 && frac > szFrac) {
                    tNumSZ++;
                }
            }
            if (exceptXDNum > 0) {
                if (frac < 0 && fracAbs > xdFrac) {
                    tNumXD++;
                }
            }
            if (maxDF < 999) {
                if (fracAbs < 0 && Math.abs(fracAbs) > maxDF) {
                    return false;
                }
            }
        }
        if (tNumSZ > 0 && tNumSZ >= exceptSZNum) {
            return false;
        }
        if (tNumXD > 0 && tNumXD >= exceptXDNum) {
            return false;
        }
        if (tNum > 0 && tNum > exceptNum) {
            return false;
        }
        return true;
    }

    public boolean allIsLessAbsThanExceptNoSelf(int num, int exceptNum, float fracv, int exceptSZNum, float szFrac, int exceptXDNum, float xdFrac, float maxDF) {
        int tNum = 0;
        int tNumSZ = 0;
        int tNumXD = 0;
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i + 1);
            if (kline == null) {
                break;
            }
            float frac = kline.getZhangfu();
            float fracAbs = Math.abs(frac);
            if (exceptNum > 0) {
                if (fracAbs > fracv) {
                    tNum++;
                }
            }
            if (exceptSZNum > 0) {
                if (fracAbs > 0 && fracAbs > szFrac) {
                    tNumSZ++;
                }
            }
            if (exceptXDNum > 0) {
                if (frac < 0 && fracAbs > xdFrac) {
                    tNumXD++;
                }
            }
            if (maxDF < 999) {
                if (fracAbs < 0 && Math.abs(fracAbs) > maxDF) {
                    return false;
                }
            }
        }
        if (tNumSZ > 0 && tNumSZ >= exceptSZNum) {
            return false;
        }
        if (tNumXD > 0 && tNumXD >= exceptXDNum) {
            return false;
        }
        if (tNum > 0 && tNum >= exceptNum) {
            return false;
        }
        return true;
    }


    public boolean allIsLessAbsThans(int num, int exceptNum, float fracv, int days) {
        for (int i = 0; i < days; i++) {
            Kline kline = prev(i);
            boolean flag = kline.allIsLessAbsThan(num, exceptNum, fracv);
            if (flag) {
                return true;
            }
        }
        return false;
    }

    public boolean allIsLessAbsThansNoSelf(int num, int exceptNum, float fracv, int days) {
        for (int i = 0; i < days; i++) {
            Kline kline = prev(i + 1);
            boolean flag = kline.allIsLessAbsThan(num, exceptNum, fracv);
            if (flag) {
                return true;
            }
        }
        return false;
    }


    public boolean anyIsLessAbsThan(int num, float fracv) {
        int tNum = 0;
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i + 1);
            if (kline == null) {
                break;
            }
            float frac = Math.abs(kline.getZhangfu());
            if (frac < fracv) {
                return true;
            }
        }
        return false;
    }

    public boolean anyIsLessAbsThanMAI(int num, int period, float fracv) {
        int tNum = 0;

        for (int i = 0; i < num; i++) {
            Kline kline = prev(i + 1);
            if (kline == null) {
                break;
            }
            float mai = kline.getMAI(period);
            float frac = KLineUtil.compareMax(kline.getMin(), mai);
            if (frac < fracv && kline.getClose() > mai) {
                return true;
            }
        }
        return false;
    }

    public float getMaxDF(int num) {
        float min = 0;
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i);
            if (kline == null) {
                break;
            }
            float frac = kline.getZhangfu();
            if (frac < min) {
                min = frac;
            }
        }
        return Math.abs(min);
    }


    public int getWeekDay() {
        int idx = DateUtil.getWeek(DateUtil.stringToDate3(date));
        return idx;
    }

    public boolean isCrashDownMAI(int period) {
        float ma120 = getMAI(period);
        if (open >= ma120 && close <= ma120) {
            return true;
        }
        return false;
    }


    public boolean isCrashDownMAI2(int period) {
        float ma120 = getMAI(period);
        if (open > ma120 && close < ma120 && KLineUtil.compareMax(close, open) > 3 && KLineUtil.compareMax(close, min) < 0.5f && KLineUtil.compareMax(close, ma120) >= 0.5f) {
            return true;
        }
        return false;
    }

    public boolean isCrashDownMAIMAXMIN(int period) {
        float ma120 = getMAI(period);
        if (max >= ma120 && min <= ma120) {
            return true;
        }
        float frac1 = KLineUtil.compareMax(min, ma120);
        float frac2 = KLineUtil.compareMax(max, ma120);
        if (frac1 < 1 || frac2 < 1) {
            return true;
        }
        return false;
    }


    public Object[] getJumpIndays(int num, boolean isSZ) {
        float min = 1;
        Kline jumpLine = null;
        int idx = 0;
        for (int i = 0; i < num; i++) {
            Kline kline = prev(i);
            if (kline == null) {
                break;
            }
            float frac = KLineUtil.compareMaxSign(kline.getOpen(), kline.prev().getClose());
            float zf = kline.getEntityZhangfu();
            if (frac > min) {
                if (zf > 0 && isSZ) {
                    min = frac;
                    jumpLine = kline;
                    idx = i;
                }
                if (zf < 0 && !isSZ) {
                    min = frac;
                    jumpLine = kline;
                    idx = i;
                }
            }
        }

        int periodCrash = 0;
        if (jumpLine != null) {
            if (jumpLine.isCrashDownMAI(30)) {
                periodCrash = 30;
            } else if (jumpLine.isCrashDownMAI(60)) {
                periodCrash = 60;
            } else if (jumpLine.isCrashDownMAI(120)) {
                periodCrash = 120;
            } else if (jumpLine.isCrashDownMAI(250)) {
                periodCrash = 250;
            }
            return new Object[]{
                    min,
                    periodCrash > 0,
                    idx,
                    jumpLine.getZhangfu(),
                    periodCrash,
                    min >= 2.5
            };
        }
        return new Object[]{0.0f, false, -1, 0.0f, 0, false};
    }


    public boolean isHorMAI(int fromIdx, int period) {
        int total = fromIdx + 1;
        int num = 0;
        for (int i = fromIdx; i >= 0; i--) {
            Kline kline = prev(i);
            if (kline == null) {
                return false;
            }
            if (kline.isCrashDownMAIMAXMIN(period)) {
                num++;
            } else {
                continue;
            }
        }
        return num >= 4;
    }

    public float getMinMA120250(float v, float ma120, float ma250) {
        if (v < ma120 && v < ma250) {
            return -1;
        }
        float min = 99;
        float tmp = KLineUtil.compareMax(v, ma120);
        if (v > ma120 && tmp < min) {
            min = tmp;
        }
        tmp = KLineUtil.compareMax(v, ma250);
        if (v > ma250 && tmp < min) {
            min = tmp;
        }
        return min;
    }

    public Object[] isJumpHorMAIs(int num, boolean isSZ) {
        Object[] items = getJumpIndays(num, isSZ);
        if (items == null) {
            return new Object[]{false, 0, 0.0f};
        }
        if (!(Boolean) items[1]) {
            return new Object[]{false, 0, 0.0f};
        }

        int fromIdx = (int) items[2];
        boolean flag30 = isHorMAI(fromIdx, 30);
        boolean flag60 = isHorMAI(fromIdx, 60);
        boolean flag120 = isHorMAI(fromIdx, 120);
        boolean flag250 = isHorMAI(fromIdx, 250);

        float ma120 = getMAI(120);
        float ma250 = getMAI(250);
        float ma = 99;
        int period = 0;
        if (flag30) {
            float mav = getMAI(30);
            ma = getMinMA120250(mav, ma120, ma250);
            period = 30;
        } else if (flag60) {
            float mav = getMAI(60);
            ma = getMinMA120250(mav, ma120, ma250);
            period = 60;
        } else if (flag120) {
            ma = 0;
            period = 120;
        } else if (flag250) {
            ma = 0;
            period = 250;
        }

        return new Object[]{flag30 | flag60 | flag120 | flag250, period, ma};
    }

    public Object[] isJumpHorMAIs2(int num, boolean isSZ) {
        Object[] items = getJumpIndays(num, isSZ);
        if (items == null) {
            return new Object[]{false, 0, 0.0f};
        }
        if (!(Boolean) items[1] && !(Boolean) items[5]) {
            return new Object[]{false, 0, 0.0f};
        }

        int fromIdx = (int) items[2];
        boolean flag30 = isHorMAI(fromIdx, 30);
        boolean flag60 = isHorMAI(fromIdx, 60);
        boolean flag120 = isHorMAI(fromIdx, 120);
        boolean flag250 = isHorMAI(fromIdx, 250);

        float ma120 = getMAI(120);
        float ma250 = getMAI(250);
        float ma = 99;
        int period = 0;
        if (flag30) {
            float mav = getMAI(30);
            ma = getMinMA120250(mav, ma120, ma250);
            period = 30;
        } else if (flag60) {
            float mav = getMAI(60);
            ma = getMinMA120250(mav, ma120, ma250);
            period = 60;
        } else if (flag120) {
            ma = 0;
            period = 120;
        } else if (flag250) {
            ma = 0;
            period = 250;
        }

        return new Object[]{flag30 | flag60 | flag120 | flag250, period, ma};
    }


    public boolean isMinPrev(int num) {
        for (int i = 0; i < num; i++) {
            if (prev(i + 1) == null) {
                int a = 0;
            }
            if (prev(i + 1).getClose() < min) {
                return false;
            }
        }
        return true;
    }

    public boolean isMinAfter(int num) {
        for (int i = 0; i < num; i++) {
            if (next(i + 1).getClose() >= min) {
            } else if (next(i + 1).getClose() < min && KLineUtil.compareMax(next(i + 1).getClose(), min) < 2.6f) {
            } else {
                return false;
            }
        }
        return true;
    }

    public float getMaxAfter(int num) {
        float max = 0;
        for (int i = 0; i < num; i++) {
            if (next(i + 1).getMax() > max) {
                max = next(i + 1).getMax();
            }
        }
        return max;
    }

    public class LocalBottom {
        public boolean flag;
        public int num;
        public float frac;
    }

    public LocalBottom getLocalBottom(float fracV, int preNum, int df, float high) {
        LocalBottom localBottom = new LocalBottom();
        Kline klineMin = getPrevMINKline(preNum);
        float min = klineMin.getMin();
        if (!klineMin.isMinPrev(10)) {
            return localBottom;
        }
        if (klineMin.getPrevDF(10) < 8) {
            return localBottom;
        }

        if (KLineUtil.compareMax(min, getMin()) > fracV) {
            return localBottom;
        }
        int dltNum = getIdx() - klineMin.getIdx();
        if (!klineMin.isMinAfter(dltNum - 1)) {
            return localBottom;
        }
        float maxAfter = klineMin.getMaxAfter(dltNum - 1);
        float dltFrac = KLineUtil.compareMax(maxAfter, klineMin.getMin());
        if (dltFrac < high) {
            return localBottom;
        }
        localBottom.flag = true;
        localBottom.num = dltNum;
        localBottom.frac = dltFrac;
        return localBottom;
    }

    public LocalBottom getLocalBottom2(float fracV, int preNum, int df, float high) {
        LocalBottom localBottom = new LocalBottom();
        Kline klineMin = getPrevMINKline(preNum);
        float min = klineMin.getMin();
        if (!klineMin.isMinPrev(10)) {
            return localBottom;
        }
        if (klineMin.getPrevDF(10) < 6.6) {
            return localBottom;
        }

        int dltNum = getIdx() - klineMin.getIdx();
        if (!klineMin.isMinAfter(dltNum - 1)) {
            return localBottom;
        }
        float maxAfter = klineMin.getMaxAfter(dltNum - 1);
        float dltFrac = KLineUtil.compareMax(maxAfter, klineMin.getMin());
        if (dltFrac < high) {
            return localBottom;
        }
        localBottom.flag = true;
        localBottom.num = dltNum;
        localBottom.frac = dltFrac;
        return localBottom;
    }

    public LocalBottom getLocalBottomBZ(float fracV, int preNum, int df, float high) {
        LocalBottom localBottom = new LocalBottom();
        Kline klineMin = getPrevMINKline(preNum);
        float min = klineMin.getMin();
        if (!klineMin.isMinPrev(10)) {
            return localBottom;
        }
        if (klineMin.getPrevDF(10) < 6.5f) {
            return localBottom;
        }

        if (KLineUtil.compareMax(min, getMin()) > fracV) {
            return localBottom;
        }
        int dltNum = getIdx() - klineMin.getIdx();
        if (!klineMin.isMinAfter(dltNum - 1)) {
            return localBottom;
        }
        float maxAfter = klineMin.getMaxAfter(dltNum - 1);
        float dltFrac = KLineUtil.compareMax(maxAfter, klineMin.getMin());
        if (dltFrac < high) {
            return localBottom;
        }
        localBottom.flag = true;
        localBottom.num = dltNum;
        localBottom.frac = dltFrac;
        return localBottom;
    }


    public LocalBottom getNullLocalBottom() {
        com.mk.tool.stock.Kline.LocalBottom localBottom = new LocalBottom();
        return localBottom;
    }

    public LocalBottom getLocalBottom() {
        com.mk.tool.stock.Kline.LocalBottom localBottom = getNullLocalBottom();
        try {
            if (!localBottom.flag) {
                localBottom = getLocalBottom2(13, 3);
            }
            if (!localBottom.flag) {
                localBottom = getLocalBottom2(25, 4);
            }
        } catch (Exception e) {

        }
        return localBottom;
    }

    public LocalBottom getLocalBottom2(int dltNum, int loop) {
        com.mk.tool.stock.Kline.LocalBottom localBottom = getNullLocalBottom();
        Vector<Kline> list = new Vector<>();
        Set<Kline> set = new HashSet<>();
        Kline klineMin = this;
        klineMin = klineMin.getPrevMINKlineSelf(dltNum);
        Kline first = klineMin;
        list.add(klineMin);
        set.add(klineMin);
        int totalLoop = 0;
        for (int i = 0; i < loop; i++) {
            Kline klineMin_ = klineMin.getPrevMINKlineSelf2(dltNum);
            if (klineMin == klineMin_) {
                klineMin_ = klineMin.getPrevMINKlineSelf2_(dltNum);
            }
            totalLoop++;
            if (totalLoop > 6 || list.size() >= 5) {
                break;
            }
            if (klineMin == klineMin_) {
                dltNum = dltNum + 10;
                i--;
            }

            klineMin = klineMin_;
            if (!set.contains(klineMin)) {
                Kline last = list.get(list.size() - 1);
                if (last == first) {
                    if (KLineUtil.compareMaxSign(last.min, klineMin.getMin()) > 4) {
                        list.remove(first);
                    }
                }
                list.add(klineMin);
                set.add(klineMin);
            }
        }
        list.sort(new Comparator<Kline>() {
            @Override
            public int compare(Kline o1, Kline o2) {
                return (int) ((int) 100 * (o1.getMin() - o2.getMin()));
            }
        });


        Kline kline1 = list.get(0);
        if (list.size() <= 1) {
            return localBottom;
        }
        Kline kline2 = list.get(1);
        if (list.size() >= 3) {
            Kline kline3 = list.get(2);
            if ((kline1.getIdx() - kline2.getIdx()) <= 8 && KLineUtil.compareMax(kline2.min, kline3.min) < 1) {
                kline2 = kline3;
            }
        }
        if (!kline1.isMinPrev(5)) {
            return localBottom;
        }
        if (!kline2.isMinPrev(5)) {
            int offIdx = Math.abs(kline1.getIdx() - kline2.getIdx());
            if (offIdx <= 5) {
                return getNullLocalBottom();
            }
            return localBottom;
        }

        int offset1 = getIdx() - kline1.getIdx();
        int offset2 = getIdx() - kline2.getIdx();
        int offset = 99;
        Kline offLine = null;
        if (offset1 < offset) {
            offset = offset1;
            offLine = kline2;
        }
        if (offset2 < offset) {
            offset = offset2;
            offLine = kline1;
        }

        float max = 0;
        int len = 0;

        if (offset1 > offset2) {
            len = offset1 - offset2 + 1;
        } else {
            len = offset2 - offset1 + 1;
        }

        for (int i = 0; i < len; i++) {
            Kline kline = offLine.next(i);
            if (kline.getMax() > max) {
                max = kline.getMax();
            }
        }
        max = KLineUtil.compareMaxSign(max, offLine.getClose());
        localBottom.flag = true;
        localBottom.num = offset;
        localBottom.frac = max;

        return localBottom;
    }


    public LocalBottom getLocalBottomW(float fracV, int days) {
        Kline klineMin = getPrevMINKlineSelf(days);
        com.mk.tool.stock.Kline.LocalBottom localBottom = klineMin.getLocalBottom(fracV, 43, 11, 7);
        return localBottom;
    }

    public LocalBottom getLocalBottomWBZ(float fracV, int num) {
        Kline klineMin = getPrevMINKlineSelf(num);
        com.mk.tool.stock.Kline.LocalBottom localBottom = klineMin.getLocalBottomBZ(fracV, 33, 11, 7);
        return localBottom;
    }


    public LocalBottom getWLocalBottomW(float fracV, int num) {
        Kline klineMin = getPrevMINKlineSelf(num);
        com.mk.tool.stock.Kline.LocalBottom localBottom = klineMin.getLocalBottom(fracV, 50, 30, 30);
        return localBottom;
    }

    public LocalBottom getWLocalBottomWBZ(float fracV, int num) {
        Kline klineMin = getPrevMINKlineSelf(num);
        if (!klineMin.isStandMA3060_()) {
            return new LocalBottom();
        }
        com.mk.tool.stock.Kline.LocalBottom localBottom = klineMin.getLocalBottom2(fracV, 50, 15, 15);
        return localBottom;
    }


    public float[] getZhanfus(int num) {
        float vs[] = new float[num];
        float max = 0;
        for (int i = 0; i < num; i++) {
            vs[i] = prev(i).getZhangfu();
        }
        return vs;
    }

    public boolean isSDLine() {
        boolean flag = isShadownDown(60);
        float v = getMinspanceDownMA();
        if (!flag) {
            return false;
        }
        if (v < 6) {
            return false;
        }
        return true;
    }

    public int isSDLine(int num) {
        for (int i = 0; i < num; i++) {
            if (prev(i).isSDLine()) {
                return i;
            }
        }
        return -1;
    }

    public int isCrashDownMA() {
        Kline jumpLine = this;
        int periodCrash = 0;
        if (jumpLine != null) {
            if (jumpLine.isCrashDownMAI2(30)) {
                periodCrash = 30;
            } else if (jumpLine.isCrashDownMAI2(60)) {
                periodCrash = 60;
            } else if (jumpLine.isCrashDownMAI2(120)) {
                periodCrash = 120;
            } else if (jumpLine.isCrashDownMAI2(250)) {
                periodCrash = 250;
            }
        }
        return periodCrash;
    }

    public int isCrashDownMA(int num) {
        for (int i = 0; i < num; i++) {
            if (prev(i).isCrashDownMA() > 0) {
                return i;
            }
        }
        return -1;
    }

    static class SortEntity {
        float v;

        public SortEntity(float v, int num) {
            this.v = v;
            this.num = num;
        }

        int num;
    }

    public SortEntity[] getDZNum(int offset) {
        float max = -1;
        int num = -1;
        Vector<SortEntity> vector = new Vector<>();
        for (int i = 0; i < 30; i++) {
            int ioffset = i + offset;
            vector.add(new SortEntity(prev(ioffset).getZhangfu(), i + offset));
            if (prev(ioffset).getZhangfu() > max) {
                max = prev(ioffset).getZhangfu();
                num = i + offset;
            }
        }
        vector.sort(new Comparator<SortEntity>() {
            @Override
            public int compare(SortEntity o1, SortEntity o2) {
                int ret = (int) ((int) 100.0f * (o2.v - o1.v));
                return ret;
            }
        });

        return new SortEntity[]{vector.get(0), vector.get(1), vector.get(2)};
    }


}

