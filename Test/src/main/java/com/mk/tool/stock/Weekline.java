package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.mk.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Weekline extends Kline {

    private String date;
    public long volume;
    private int year;
    private int month;
    public boolean isBottom;
    private int curDayIdx = -1;
    public String key;
    public List<Kline> mdays = new ArrayList<>();
    public List<Weekline> allLineList = new ArrayList();

    public Weekline(String line) {
        super(line);
    }

    public Weekline() {
    }

    public void clear() {
        mdays = null;
        allLineList = null;
    }

    public void add(Kline kline) {
        mdays.add(kline);
    }

    public int minIdx = 0;
    public int maxIdx = 0;

    public void init(int idx) {
        float max = 0;
        float min = 99999;
        if (mdays.size() == 0) {
            return;
        }
        if (key.equalsIgnoreCase("2023-05") && idx == mdays.size() - 1) {
            int a = 0;
        }
        Kline ckline = mdays.get(idx);
        ckline.setWopen(mdays.get(0).getOpen());
        ckline.setWclose(mdays.get(idx).getClose());
        for (int i = 0; i <= idx; i++) {
            Kline kline = mdays.get(i);
            if (kline.getMin() < min) {
                min = kline.getMin();
            }
            if (kline.getMax() > max) {
                max = kline.getMax();
            }
        }
        ckline.setWmin(min);
        ckline.setWmax(max);

    }

    public void init() {
        float max = 0;
        float min = 99999;
        if (mdays.size() == 0) {
            return;
        }
        if (key.equalsIgnoreCase("2022-07")) {
            int a = 0;
        }
        setOpen(mdays.get(0).getOpen());
        setClose(mdays.get(mdays.size() - 1).getClose());
        for (int i = 0; i < mdays.size(); i++) {
            Kline kline = mdays.get(i);
            if (kline.getMin() < min) {
                min = kline.getMin();
                minIdx = i;
            }
            if (kline.getMax() > max) {
                max = kline.getMax();
                maxIdx = i;
            }
        }

        this.setMin(min);
        this.setMax(max);

        for (int i = 0; i < mdays.size(); i++) {
            init(i);
        }

        this.key = mdays.get(mdays.size() - 1).date;
        for (int i = 0; i < mdays.size(); i++) {
            Kline kline = mdays.get(i);
            kline.weekline = this;
        }
    }

    public int getByIdx(int idx) {
        for (int i = 0; i < mdays.size(); i++) {
            Kline kline1 = mdays.get(i);
            if (kline1.getIdx() == idx) {
                return i;
            }
        }
        return -1;
    }

    public float getMin() {
        if (AbsStragety.CURDAY_OPEN) {
            return getOpen();
        }
        if (curDayIdx == -1) {
            return super.getMin();
        }
        float max = 0;
        float min = 99999;
        if (mdays.size() == 0) {
            return 0;
        }
        if (key.equalsIgnoreCase("2023-05")) {
            int a = 0;
        }
        int idx = getByIdx(curDayIdx);
        Kline ckline = mdays.get(idx);
        ckline.setWopen(mdays.get(0).getOpen());
        ckline.setWclose(mdays.get(idx).getClose());
        for (int i = 0; i <= idx; i++) {
            Kline kline = mdays.get(i);
            if (AbsStragety.CURDAY_OPEN) {
                if (i == idx) {
                    if (kline.getOpen() < min) {
                        min = kline.getMin();
                    }
                } else {
                    if (kline.getMin() < min) {
                        min = kline.getMin();
                    }
                }
            } else {
                if (kline.getMin() < min) {
                    min = kline.getMin();
                }
            }
        }
        return min;
    }

    public float getMax() {
        if (curDayIdx == -1) {
            return super.getMax();
        }

        float max = 0;
        if (mdays.size() == 0) {
            return 0;
        }
        if (key.equalsIgnoreCase("2023-05")) {
            int a = 0;
        }
        int idx = getByIdx(curDayIdx);
        for (int i = 0; i <= idx; i++) {
            Kline kline = mdays.get(i);
            if (AbsStragety.CURDAY_OPEN) {
                if (i == idx) {
                    if (kline.getOpen() > max) {
                        max = kline.getMax();
                    }
                } else {
                    if (kline.getMax() > max) {
                        max = kline.getMax();
                    }
                }
            } else {
                if (kline.getMax() > max) {
                    max = kline.getMax();
                }
            }
        }
        return max;
    }

    public float getOpen() {
//        if (curDayIdx == -1) {
//            return super.getOpen();
//        }
//        int idx = getByIdx(curDayIdx);
//        float open = mdays.get(idx).getOpen();
        return open;
    }

    public float getClose() {
//        if (AbsStragety.CURDAY_OPEN) {
//            return getOpen();
//        }
//        if (curDayIdx == -1) {
//            return super.getClose();
//        }
//        int idx = getByIdx(curDayIdx);
//        Kline ckline = mdays.get(idx);
//        ckline.setWopen(mdays.get(0).getOpen());
//        ckline.setWclose(mdays.get(idx).getClose());
//        return ckline.getClose();
        return close;
    }

    public long getVolume() {
        if (curDayIdx == -1) {
            return super.getVolume();
        }
        int idx = getByIdx(curDayIdx);
        Kline ckline = mdays.get(idx);
        ckline.setWopen(mdays.get(0).getOpen());
        ckline.setWclose(mdays.get(idx).getClose());
        return ckline.getVolume();
    }


    public float getZhangfu() {
        Kline day0 = allLineList.get(getIdx());
        Kline day1 = allLineList.get(getIdx() - 1);
        float ret = 100 * (day0.getClose() - day1.getClose()) / day1.getClose();
        return ret;
    }

    public float getZhangfu2() {
        Weekline day0 = prev();
        Weekline day1 = this;
        float ret = 100 * (day1.close - day0.close) / day0.close;
        return ret;
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

    public Weekline prev() {
        return allLineList.get(getIdx() - 1);
    }

    public Weekline prev(int i) {
        if (getIdx() - i < 0) {
            return null;
        }
        return allLineList.get(getIdx() - i);
    }

    public Weekline next() {
        if (getIdx() + 1 >= allLineList.size() - 1) {
            return null;
        }
        return allLineList.get(getIdx() + 1);
    }

    public Weekline next(int i) {
        if (getIdx() + 1 >= allLineList.size()) {
            return null;
        }
        return allLineList.get(getIdx() + i);
    }

    public String getDate() {
        return key;
    }

    public String toString() {
        return key + " open:" + getOpen() + " close:" + getClose() + " max:" + getMax() + " min" + getMin() + " frc" + getZhangfu();
    }


    public Weekline getPrevUpShadow(int n, float percent) {
        for (int i = 1; i <= n; i++) {
            Weekline item = prev(i);
            if (item.isShadownUp(percent)) {
                return item;
            }
        }
        return null;
    }


    public Weekline getPrevUpShadowWIthMATouch(int n, float percent, float minZF) {
        for (int i = 1; i <= n; i++) {
            Weekline item = prev(i);
            if (item.isShadownUp(percent)) {
                float ma120 = item.getMA120();
                if (item.touch(ma120)) {
                    if (minZF <= 0 || item.getZhenfu() >= minZF) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public Date getDateObj() {
        return DateUtil.stringToDate3(key);
    }

    public String getDateYM() {
        String monStr = DateUtil.dateToStringYM(DateUtil.strToDate4(key));
        return monStr;
    }

    public float getZhangfuAbs() {
        Kline day0 = allLineList.get(getIdx());
        Kline day1 = allLineList.get(getIdx() - 1);
        float ret = 100 * (day0.getClose() - day1.getClose()) / day1.getClose();
        return Math.abs(ret);
    }

    public boolean hasDZ(int num, float frac) {
        float fracV = this.getPrevZF2(num);
        if (fracV > frac) {
            return true;
        }
        return false;
    }


    public int getCurDayIdx() {
        return curDayIdx;
    }

    public void setCurDayIdx(int curDayIdx) {
        if (curDayIdx == -1) {
            return;
        }
        if (AbsStragety.CURDAY_OPEN) {
        } else {
            if (curDayIdx == mdays.get(mdays.size() - 1).getIdx()) {
                return;
            }
        }

        this.curDayIdx = curDayIdx;
    }

    public String getZhangfuStr() {
        float zf = getZhangfu();
        return StringUtil.spaceString(StringUtil.format(zf,1),5);
    }

    public String getZhangfuStr2() {
        float zf = getZhangfu2();
        return StringUtil.spaceString(StringUtil.format(zf,2),5);
    }

    public double getChangeHand(LineContext context) {
        float total = 0;
        for (int i = 0; i < mdays.size(); i++) {
            Kline kline = mdays.get(i);
            double hand = kline.getHandD(context.getTotalV());
            total+= hand;
        }
        return total;
    }

    public boolean isCrashDownMAI2(int period) {
        float ma120 = getMAI(period);
        if (open > ma120 && close < ma120  && KLineUtil.compareMax(close, open)>5 && KLineUtil.compareMax(close, min)<0.8f && KLineUtil.compareMax(close, ma120)>=0.5f) {
            return true;
        }
        return false;
    }



}
