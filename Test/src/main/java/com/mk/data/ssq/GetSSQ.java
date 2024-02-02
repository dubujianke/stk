package com.mk.data.ssq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.DateUtil;
import com.mk.data.Config;
import com.mk.util.FileUtil;
import com.mk.util.JsonUtil;
import com.mk.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetSSQ {
    public static class DV {
        public String date;
        public float v;
    }



    public static void get(String code, int len) throws IOException {
        Document doc = Jsoup.connect("https://caipiao.eastmoney.com/pub/Result/History/ssq?page=62").get();
        Elements newsHeadlines = doc.select("table[class=table table-bordered table-history]");
        Elements trs2 = newsHeadlines.select("tr");
        List<String> titles = new ArrayList<>();
        for(int i=2; i<trs2.size(); i++) {
            Element element = trs2.get(i);
            Element idx = element.children().first();
            Element ret = element.children().get(3);
            String content = idx.text();
            Log.log(content);
            titles.add(content);
        }

    }


    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException {
        GetSSQ.get("", 9);
    }

}
