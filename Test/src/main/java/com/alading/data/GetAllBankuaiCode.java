package com.alading.data;

import com.alading.model.AllModel;
import com.alading.model.Bankuai;
import com.alading.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;


public class GetAllBankuaiCode {
    public static final String FILE = "res/bankuai.txt";
    public static final String FILE_SELECT = "bankuai_select.txt";
    public static List<String> bankuaiSelectList;

    public static String getCode(String str) {
        String[] strs = str.split("/");
        return strs[strs.length - 1];
    }

    public static void save(int page) throws IOException {
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StringBuffer stringBuffer = new StringBuffer();
        Document doc = Jsoup.connect("https://q.10jqka.com.cn/thshy/index/field/199112/order/desc/page/" + page + "/ajax/1/")
                .cookie("historystock", "301326")
                .cookie("spversion", "20130314")
                .cookie("searchGuide", "sg")
                .cookie("Hm_lvt_722143063e4892925903024537075d0d", "1712752685,1713105513")
                .cookie("Hm_lpvt_722143063e4892925903024537075d0d", "1713105513")
                .cookie("log", "")
                .cookie("Hm_lvt_929f8b362150b1f77b477230541dbbc2", "1712752685,1713105513")
                .cookie("Hm_lpvt_929f8b362150b1f77b477230541dbbc2", "1713105513")
                .cookie("Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1", "1712752685,1713105513")
                .cookie("Hm_lpvt_78c58f01938e4d85eaf619eae71b4ed1", "1713105542")
                .cookie("v", "A7oehtFhiCtrjATHegbudaDgC-vZaz5FsO-y6cSzZs0Yt1RVrPuOVYB_AviX")
                .get();
        Elements newsHeadlines = doc.select("table.m-table").select("tbody").select("tr");
        for (Element element : newsHeadlines) {
            Elements tds = element.select("td");
            Element td1 = tds.get(1).select("a").get(0);
            Bankuai bankuai = new Bankuai();
            String name = td1.text();
            String url = td1.attr("href");
            String aCode = getCode(url);
            bankuai.setName(name);
            bankuai.setUrl(url);
            bankuai.setCode(aCode);
//            Log.log(name + " "+aCode);
            stringBuffer.append(name + " " + aCode).append("\r\n");
            AllModel.add(aCode, bankuai);
        }
        FileUtil.writeAppend("./res/sks.txt", stringBuffer.toString());
    }

    public static void read() throws IOException {
        List<String> strs = FileUtil.readLines(FILE);
        for (String element : strs) {
            Bankuai bankuai = new Bankuai();
            String items[] = element.split(" ");
            String name = items[0];
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
        List<String> strs = FileUtil.readLines(Config.ROOT2 + FILE_SELECT);
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
        save(2);
//        read();
    }
}
