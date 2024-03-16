package com.mk.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mk.MKNode;
import com.mk.model.AllModel;
import com.mk.model.Bankuai;
import com.mk.util.FileUtil;
import com.mk.util.JsonUtil;
import com.mk.util.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;


public class GetAllBankuaiCode {
    public static final String FILE = "res/bankuai.txt";
    public static final String FILE_SELECT = "bankuai_select.txt";
    public static  List<String> bankuaiSelectList;
    public static String getCode(String str) {
        String[] strs = str.split("/");
        return strs[strs.length-1];
    }

    public static void save(int page) throws IOException {
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StringBuffer stringBuffer = new StringBuffer();
        Document doc = Jsoup.connect("https://q.10jqka.com.cn/thshy/index/field/199112/order/desc/page/"+page+"/ajax/1/")
                .cookie("Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1", "1686045943")
                .cookie("v", "A4Y0zcfNHjTf-8pQtn1HM4xE13cL58qhnCv-BXCvcqmEcygh2HcasWy7ThRD")
                .get();
        Elements newsHeadlines = doc.select("table.m-table").select("tbody").select("tr");
        for(Element element : newsHeadlines) {
            Elements tds = element.select("td");
            Element td1 = tds.get(1).select("a").get(0);
            Bankuai bankuai = new Bankuai();
            String name  = td1.text();
            String url = td1.attr("href");
            String aCode = getCode(url);
            bankuai.setName(name);
            bankuai.setUrl(url);
            bankuai.setCode(aCode);
//            Log.log(name + " "+aCode);
            stringBuffer.append(name + " "+aCode).append("\r\n");
            AllModel.add(aCode, bankuai);
        }
        FileUtil.writeAppend("./res/sks.txt", stringBuffer.toString());
    }

    public static void read() throws IOException {
        List<String> strs = FileUtil.readLines(FILE);
        for(String element : strs) {
            Bankuai bankuai = new Bankuai();
            String items[] = element.split(" ");
            String name  = items[0];
            String url = items[1];
            String aCode = getCode(url);
            bankuai.setName(name);
            bankuai.setUrl(url);
            bankuai.setCode(aCode);
//            Log.log(name + " "+aCode);
            AllModel.add(aCode, bankuai);
        }
    }

    public static List<String> readSelect() throws IOException {
        List<String> strs = FileUtil.readLines(Config.ROOT2+FILE_SELECT);
//        for(String element : strs) {
//            strs.add(element);
//        }
        bankuaiSelectList = strs;
        return strs;
    }

    public static boolean containBankuai(String name) {
        boolean flag = bankuaiSelectList.stream().anyMatch(v -> name.contains(v));
        return flag;
    }

    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException {
        save(1);
//        read();
    }
}
