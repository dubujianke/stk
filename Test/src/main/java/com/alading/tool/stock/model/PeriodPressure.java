package com.alading.tool.stock.model;

import com.alading.tool.stock.tool.SelectAddColExcel;

public class PeriodPressure {
    public int period;

    public PeriodPressure(String value) {
        this.value = value;
    }

    public PeriodPressure() {
    }

    public String value  = SelectAddColExcel.defalutValue;
}
