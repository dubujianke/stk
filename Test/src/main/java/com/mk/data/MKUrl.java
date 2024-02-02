package com.mk.data;

import java.net.URLEncoder;

public class MKUrl {
    public MKUrl(String url) {
        this.url = url;
    }

    private String url;
    StringBuffer stringBuffer = new StringBuffer();
    int idx;

    public MKUrl append(String param, String v) {
        if(idx == 0) {
            stringBuffer.append(param + "=" + URLEncoder.encode(v));
        }else {
            stringBuffer.append("&" + param + "=" + URLEncoder.encode(v));
        }
        idx++;
        return this;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String get() {
        return url+"?"+stringBuffer.toString();
    }
}
