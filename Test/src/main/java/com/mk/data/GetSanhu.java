package com.mk.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.DateUtil;
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

public class GetSanhu {
    public static class DV {
        public String date;
        public float v;
    }

    private static int getRowIdx(List<String> titles) {
        for(int i=0; i<titles.size(); i++) {
            String element = titles.get(i);
            if(element.equalsIgnoreCase("A股股东数变化")) {
                return i;
            }
            if(element.equalsIgnoreCase("较上期变化")) {
                return i;
            }
        }
        return -1;
    }

    public static DV get(String code, int len) throws IOException {
        code = code.replace("sh", "");
        code = code.replace("sz", "");
        Document doc = Jsoup.connect("http://basic.10jqka.com.cn/"+code+"/holder.html").get();

        Elements newsHeadlines = doc.select("div.table_data");
        Elements left_thead = newsHeadlines.select("div.left_thead");
        left_thead = left_thead.select("table.tbody");
        Elements trs2 = left_thead.select("tr");
        Elements tds2 = trs2.select("th");
        List<String> titles = new ArrayList<>();
        for(int i=0; i<tds2.size(); i++) {
            Element element = tds2.get(i);
            String content = element.text();
            titles.add(content);
        }
        int rowIdx = getRowIdx(titles);
        if(rowIdx<0) {
            return null;
        }
        Elements es = newsHeadlines.select("div.data_tbody");
        Elements top_thead = es.select("table.top_thead");
        Elements ths = top_thead.select("th div.td_w");
        List<String> dates = new ArrayList<>();
        for(int i=0; i<ths.size(); i++) {
            Element element = ths.get(i);
            String content = element.text();
            dates.add(content);
        }
        String date0 = dates.get(0);
        int days = DateUtil.getDayLen(DateUtil.stringToDate4(date0), new Date());
        if(days<= len) {
            DV dv = new DV();
            Elements es2 = es.select("table.tbody");
            Elements trs = es2.select("tr");
            Element tr = trs.get(rowIdx);
            Elements tds = tr.select("td");
            String v = tds.get(0).text().replace("%", "");
            v = v.trim();
            try{
                if(v.equalsIgnoreCase("-")) {
                    return null;
                }
                float vv = Float.parseFloat(v);
                int a = 0;
                dv.date = date0;
                dv.v = vv;
                return dv;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<String> read(String code) throws IOException {
        String str = FileUtil.read(Config.ROOT+code+".txt");
        JSONArray jsonObject = JSONObject.parseArray(str);
        List<String> list = JsonUtil.getList(jsonObject);
        return list;
    }

    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException {
        DV dv = GetSanhu.get("000063", 9);
        log(dv.date+" "+dv.v);
    }

}
