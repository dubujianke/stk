package com.alading.msg;

import com.alading.model.Row;
import com.alading.tool.stock.ZTReason;
import com.alading.util.HttpsUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MsgSend {
    public static void sendMsg(Row row, ZTReason reason) throws UnsupportedEncodingException {
        String code = row.getStr("code").trim();
        String date = row.getStr("日期").trim();
        String time = row.getStr("分钟").trim();
        String price = row.getStr("涨幅").trim();
        String param = "code=" + URLEncoder.encode(code, "utf-8") + "&"
                + "date=" + URLEncoder.encode(date, "utf-8") + "&"
                + "time=" + URLEncoder.encode(time, "utf-8") + "&"
                + "price=" + URLEncoder.encode(price, "utf-8") + "&"
                + "reason=" + URLEncoder.encode(reason.reason, "utf-8");
        HttpsUtils.sendMsgByHttp("http://jes.xthea.com:7180/xuexue/addStock.do", param);
    }
}
