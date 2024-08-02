package com.alading;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * A simple example, used on the jsoup website.
 */
public class Wikipedia {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://finance.sina.com.cn/realstock/company/sh600547/nc.shtml").get();
        log(doc.title());

        Connection.Response response =Jsoup.connect("https://cmake.org/").execute();
        String body = response.body();


        Elements newsHeadlines = doc.select("div.com_overview");
        Element aElement = MKNode.getChildByContent(newsHeadlines.get(0), "所属板块");
        List<String> list = MKNode.getListStr(aElement, 1);
        JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));

        String a = "{}";

        JSONObject jsonObject = JSONObject.parseObject(a);
        log(jsonObject.toJSONString());
    }

    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }
}
