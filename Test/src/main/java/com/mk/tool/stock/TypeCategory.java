package com.mk.tool.stock;

public class TypeCategory {
    public TypeCategory() {
    }

    String name;
    private LineType fromLineType;
    private LineType endLineType;

    public void add() {
        total++;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    int total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeCategory(String name) {
        this.name = name;
    }

    public String toString() {
        return name + " " + total;
    }


    public LineType getFromLineType() {
        return fromLineType;
    }

    public void setFromLineType(LineType fromLineType) {
        this.fromLineType = fromLineType;
    }

    public LineType getEndLineType() {
        return endLineType;
    }

    public void setEndLineType(LineType endLineType) {
        this.endLineType = endLineType;
    }
}
