package com.mk.msg;

import com.mk.model.Row;
import com.mk.util.HttpsUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MsgSend {
    public static void sendMsg(Row row) throws UnsupportedEncodingException {
        String code = row.getStr("code").trim();
        String date = "2024-02-14";//row.getStr("日期").trim();
        String time = row.getStr("分钟").trim();
        String price = row.getStr("涨幅").trim();
        String param = "code="+URLEncoder.encode(code, "utf-8")+"&"+  "date="+ URLEncoder.encode(date, "utf-8")+"&"+  "time="+URLEncoder.encode(time, "utf-8")+"&"+  "price="+URLEncoder.encode(price, "utf-8");
        HttpsUtils.sendMsgByHttp("http://jes.xthea.com:7180/xuexue/addStock.do", param);
    }
}
