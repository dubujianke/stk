package com.mk.tool.stock;

public class Guaili {
    public boolean use3060;
    public boolean use60120;

    public double ma1030;
    public double ma3060;
    public double ma60120;

    public boolean isOK() {
        if(ma1030>40) {
            return false;
        }
        if(ma3060>30) {
            return false;
        }
        if(ma60120>30) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "ma10_30:"+ma1030 + " ma30_60:"+ma3060 +  " ma60_120:"+ma60120;
    }
}
