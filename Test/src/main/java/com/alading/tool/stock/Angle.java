package com.alading.tool.stock;

public class Angle {
    private double theata;
    private double leftLen;
    private double rightLen;
    private int lvol;
    private int rvol;
    private double price;
    private double prevClose;
    private double avgPrice;

    public double getTheata() {
        return theata;
    }

    public void setTheata(double theata) {
        this.theata = theata;
    }

    public double getLeftLen() {
        return leftLen;
    }

    public void setLeftLen(double leftLen) {
        this.leftLen = leftLen;
    }

    public double getRightLen() {
        return rightLen;
    }

    public void setRightLen(double rightLen) {
        this.rightLen = rightLen;
    }

    public void mergeLeft(Angle angle) {
        leftLen = leftLen + angle.getLeftLen();
        lvol += angle.getLvol();

    }

    public void mergeRight(Angle angle) {
        rightLen = rightLen + angle.getRightLen();
        setRvol(getRvol() + angle.getRvol());
    }


    public int getLvol() {
        return lvol;
    }

    public void setLvol(int lvol) {
        this.lvol = lvol;
    }

    public int getRvol() {
        return rvol;
    }

    public void setRvol(int rvol) {
        this.rvol = rvol;
    }

    public int getToalVol() {
        return lvol+ getRvol();
    }

    public double getTotalLen() {
        return leftLen+rightLen;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(double prevClose) {
        this.prevClose = prevClose;
    }

    public boolean isUnderClose() {
        return price<avgPrice;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }
}
