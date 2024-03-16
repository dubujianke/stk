package com.mk.tool.stock;

public class StockState {
    public int weekPos;
    public int dayPos;
    public boolean enable;
    public double space;

    public boolean isDayPosOne() {
        return KLineUtil.isOne(dayPos);
    }

    public boolean isDayPosZeroBottomUpEntityLarge() {
        return KLineUtil.isZeroBottomUpEntityLarge(dayPos);
    }


    public boolean isStandMA250OrMa120() {
        return KLineUtil.isStandMA250OrMa120(dayPos);
    }


}
