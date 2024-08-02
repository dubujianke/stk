package com.alading.tool.stock;

import java.util.ArrayList;
import java.util.List;

public class Section {
    public List<List<Kline>> maxPoints = new ArrayList<>();
    private int fromIdx;
    private int endIdx;


    public int getFromIdx() {
        return fromIdx;
    }

    public void setFromIdx(int fromIdx) {
        this.fromIdx = fromIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    public void setEndIdx(int endIdx) {
        this.endIdx = endIdx;
    }
}
