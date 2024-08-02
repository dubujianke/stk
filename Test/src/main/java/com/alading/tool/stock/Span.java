package com.alading.tool.stock;

public class Span {
    public Span(int from, int to) {
        this.from = from;
        this.to = to;
    }

    private String id;

    private int from;
    private int to;
    private int num = 0;
    public void add() {
        setNum(getNum() + 1);
    }
    public boolean isIn(int vol) {
        return vol >= from && vol <to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return ""+from+"_"+to;
    }

    public void setId(String id) {
        this.id = id;
    }
}
