package com.mk.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mk.MKNode;
import com.mk.model.AllModel;
import com.mk.util.FileUtil;
import com.mk.util.JsonUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import org.apache.http.entity.StringEntity;

public class GetPrice {

    public static void get(String code) throws IOException {
        Connection.Response response =Jsoup.connect("https://hq.sinajs.cn/rn=pjx0m&list=sz300817,sz301192,sz300657,sz301488,sz301141,sz300475,sz301186,sz300270,sz301007,sz300008,sz000656,sz002146,sz002426,sz000691,sz000980,sz002654,sz000020,sz002636,sz002703,sz301357,sz000004,sz002976,sz002261,sz002818,sz001309,sz002428,sz002892,sz001380,sz002134,sz002403,sz300128,sz000961,sz000838,sz301163,sz300283,sz301068,sz301282,sz301359,sz300620,sz002527")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("Host", "hq.sinajs.cn")
                .header("Referer", "https://vip.stock.finance.sina.com.cn/")
                .header("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"")
                .header("Sec-Ch-Ua-Mobile", "?0")
                .header("Sec-Ch-Ua-Platform", "\"Windows\"")
                .header("Accept", "text/*")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")


                .execute();
        String body = response.body();
        log("1", body);
    }

    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException {
        GetPrice.get("sh600547");
    }

}
