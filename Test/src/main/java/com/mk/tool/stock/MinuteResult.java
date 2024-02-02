package com.mk.tool.stock;

import java.util.ArrayList;
import java.util.List;

public class MinuteResult {
    private MinuteLine minuteLine;

    private List<MinuteLine> upMAs = new ArrayList<>();

    public MinuteLine getMinuteLine() {
        return minuteLine;
    }

    public void setMinuteLine(MinuteLine minuteLine) {
        this.minuteLine = minuteLine;
    }
    public void add(MinuteLine minuteLine) {
        getUpMAs().add(minuteLine);
    }

    public List<MinuteLine> getUpMAs() {
        return upMAs;
    }

    public void setUpMAs(List<MinuteLine> upMAs) {
        this.upMAs = upMAs;
    }
}
