package com.mk.tool.stock.model;

import com.mk.tool.stock.KLineUtil;
import com.mk.tool.stock.Kline;
import com.mk.tool.stock.MinuteLine;

import java.util.HashMap;
import java.util.Map;

public class MinuteModel {
    public Map<String, MinuteEntity> vMap = new HashMap<>();
    public int count;
    public boolean isMax;
    public MinuteLine maxLine;
    public boolean is3Dayu900;
    public float total4Frac;
    public Kline kline;
    MinuteLine aMinuteLine;
    private int idx;

    public boolean isMax() {
        return isMax;
    }

    public void setMax(boolean max) {
        isMax = max;
    }


    public boolean is4Up(MinuteLine minuteLine) {
        MinuteLine pointer0 = minuteLine.prev(4);
        MinuteLine pointer = minuteLine.prev(4);
        if (pointer == null) {
            return false;
        }
        int cnt = 0;
        int cntV900 = 0;
        while (pointer != null) {
            if (pointer.isPriceUp()) {
                cnt++;
            }
            if (pointer.getVol() >= 900) {
                cntV900++;
            }
            if (pointer == minuteLine) {
                break;
            }
            pointer = pointer.next();
        }

        float dlt = minuteLine.getPrice() - pointer0.getPrice();
        total4Frac = 100.0f * (dlt) / kline.getClose();
        is3Dayu900 = cntV900 >= 3;
        return cnt >= 4;
    }


    public MinuteModel() {
        vMap.put("100", new MinuteEntity());
        vMap.put("300", new MinuteEntity());
        vMap.put("900", new MinuteEntity());
    }

    public void process(MinuteLine minuteLine) {
        idx = minuteLine.getIdx();
        aMinuteLine = minuteLine;
        MinuteLine last = minuteLine;
        float max = 0;
        while (minuteLine != null) {
            int v = minuteLine.getVol();
            float price = minuteLine.getPrice();
            if (price > max) {
                max = price;
                maxLine = minuteLine;
            }
            add(v);
            minuteLine = minuteLine.prev();
        }
        isMax = maxLine == last;
    }


    public void add(int i) {
        count++;
        if (i < 100) {
            vMap.get("100").add();
        }
        if (i < 300) {
            vMap.get("300").add();
        }
        if (i < 900) {
            vMap.get("900").add();
        }
    }

    public int get100() {
        return vMap.get("100").v;
    }

    public int get300() {
        return vMap.get("300").v;
    }

    public int get900() {
        return vMap.get("900").v;
    }

    public float get100P() {
        return (1.0f * 100 * get100() / count);
    }

    public float get300P() {
        return (1.0f * 100 * get300() / count);
    }

    public float get900P() {
        return (1.0f * 100 * get900() / count);
    }

    public boolean matchLow() {
        if (count < 60) {
            return false;
        }
        float p100 = get100P();
        float p300 = get300P();
        float p900 = get900P();
        if (p300 > 80 && p900 > 90) {
            return true;
        }
        return false;
    }

    public boolean allIsOK() {
        if(idx<5) {
            return false;
        }
        boolean lowVolumnFlag = matchLow();//超低的量
        boolean isMax = isMax();
        boolean is4Up = is4Up(aMinuteLine);
        if (!lowVolumnFlag) {
            return false;
        }
        if (!isMax) {
            return false;
        }
        if (!is4Up) {
            return false;
        }
        if (total4Frac < 0.85) {
            return false;
        }
        return true;
    }

}
