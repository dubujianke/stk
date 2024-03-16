package com.mk.tool.stock;

import java.util.List;
import java.util.regex.Pattern;

public class MaxPoint {
    public int idx;
    public int pIdx;
    public double value;
    public Kline kline;
    public List<MaxPoint> points;

    //0 min 1 max
    public int flag;

    public MaxPoint pre;
    public MaxPoint next;

    public static final int UD = 0;
    public static final int DU = 1;
    public static final int MAX = 2;
    public static final int MIN = 3;

    public int flag2;

    public String getType() {
        if(flag2==MPoint.UP_DOWN) {
            return "UD";
        }
        if(flag2==MPoint.DOWN_UP) {
            return "DU";
        }
        if(flag2==MPoint.MAX) {
            return "MAX";
        }
        if(flag2==MPoint.MIN) {
            return "MIN";
        }
        if(flag2==MPoint.UP) {
            return "U";
        }
        if(flag2==MPoint.DOWN) {
            return "D";
        }
        return "";
    }

    public String toString() {
        return kline.toString()+"  "+getType();
    }

    public MaxPoint next() {
        return  next(1);
    }

    public MaxPoint next(int i) {
        if(pIdx+i >= points.size()-1) {
            return null;
        }
        return points.get(pIdx+i);
    }

    public MaxPoint prev() {
        return prev(1);
    }
    public MaxPoint prev(int i) {
        if(pIdx-i < 0) {
            return null;
        }
        return points.get(pIdx-i);
    }

    public MaxPoint prevMax() {
        for(int i=1; i<points.size(); i++) {
            MaxPoint cur = prev(i);
            if(cur == null) {
                break;
            }
            int type = cur.flag2;
            if(type == MPoint.MAX) {
                return cur;
            }
        }
        return null;
    }

}
