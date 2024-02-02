package com.mk.tool.stock;

public class Pos {

    public Pos(double x, double y) {
        x = x*0.1f;
        this.x = x;
        this.y = y;
    }

    public Pos() {
    }

    double x;
    double y;
    int idx;

    public Pos dlt(Pos v) {
        return new Pos(x - v.x, y - v.y);
    }

}
