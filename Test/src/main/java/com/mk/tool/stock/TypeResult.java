package com.mk.tool.stock;

import java.util.ArrayList;
import java.util.List;

public class TypeResult {
    public MinuteLine getMinuteLine() {
        return minuteLine;
    }

    public void setMinuteLine(MinuteLine minuteLine) {
        this.minuteLine = minuteLine;
    }

    MinuteLine minuteLine;
    public List<LineType> lineTypeList = new ArrayList<>();

    public void add(LineType lineType) {
        lineTypeList.add(lineType);
    }

    public int size() {
        return lineTypeList.size();
    }

    public String getInfo() {
        LineType lineType = lineTypeList.get(lineTypeList.size()-1);
        return lineType.toString();
    }
}
