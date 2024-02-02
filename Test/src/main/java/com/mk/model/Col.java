package com.mk.model;

public class Col {
    public Col() {
    }

    public Col(String data) {
        this.data = data;
    }

    public String colName = "";

    public boolean isBold;
    public boolean isItalic;
    public String data;

    public String toString() {
        return colName+":"+data;
    }

}
