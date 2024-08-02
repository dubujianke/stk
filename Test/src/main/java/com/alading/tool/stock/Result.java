package com.alading.tool.stock;

import com.alading.model.Row;

public class Result {
    public boolean flag;
    public String stragety;
    public String code = "";
    public String date;
    public Row row;

    public Result(String code, String date) {
        this.code = code;
        this.date = date;
    }

    public String getInfo() {
        return code+" "+date+" "+stragety;
    }
}
