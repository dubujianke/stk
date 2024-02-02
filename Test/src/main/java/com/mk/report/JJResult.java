package com.mk.report;

import com.mk.tool.stock.MinuteLine;

public class JJResult {
    private String code;
    private MinuteLine minuteLine;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MinuteLine getMinuteLine() {
        return minuteLine;
    }

    public void setMinuteLine(MinuteLine minuteLine) {
        this.minuteLine = minuteLine;
    }

    public String getInfo() {
        return msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
