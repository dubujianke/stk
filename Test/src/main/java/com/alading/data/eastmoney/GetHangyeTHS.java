package com.alading.data.eastmoney;

import cn.hutool.core.util.StrUtil;
import com.alading.model.*;
import com.alading.tool.stock.AbsStragety;
import com.alading.tool.stock.Kline;
import com.alading.util.FileUtil;
import com.alading.util.HttpsUtils;
import com.alading.util.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.FileManager;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GetHangyeTHS {
    static String mode = "11";

    static String getFile(String code) {
        return "res/data/" + code + "_" + mode + ".txt";
    }

    static String getCode(String code) {
        code = "bk_" + code;
        return code;
    }

    public static String[] retriveOrGetShizhi(String code, double v) {
        return retriveOrGet(code, 1);
    }

    public static String[] retriveOrGet(String code, int page) {
        try {
            Thread.currentThread().sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StringBuffer stringBuffer = new StringBuffer();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://q.10jqka.com.cn/thshy/detail/field/199112/order/desc/page/" + page + "/ajax/1/code/" + code)
                    .cookie("historystock", "301326")
                    .cookie("spversion", "20130314")
                    .cookie("searchGuide", "sg")
                    .cookie("Hm_lvt_722143063e4892925903024537075d0d", "1712752685,1713105513")
                    .cookie("log", "")
                    .cookie("Hm_lvt_929f8b362150b1f77b477230541dbbc2", "1712752685,1713105513")
                    .cookie("Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1", "1712752685,1713105513")
                    .cookie("v", "Axi84IdLyuUeTeZhkNJsi14a6U2vAXyL3mVQD1IJZNMG7bZz-hFMGy51IJ6h")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements newsHeadlines = doc.select("table.m-table").select("tbody").select("tr");
        String pageInfo = doc.select("span.page_info").text();

        for (Element element : newsHeadlines) {
            if (element.text().contains("暂无成份股数据")) {
                break;
            }
            Elements tds = element.select("td");
            Element td1 = tds.get(1).select("a").get(0);
            String name1 = tds.get(2).select("a").get(0).text();
            String price = tds.get(3).text();
            String zf = tds.get(4).text();
            Bankuai bankuai = new Bankuai();
            String name = td1.text();
            String url = td1.attr("href");
            String aCode = getCode(url);
            bankuai.setName(name);
            bankuai.setUrl(url);
            bankuai.setCode(aCode);
            stringBuffer.append(name + " " + name1).append("\r\n");

        }

        String apage = "1";
        if (!pageInfo.trim().equals("")) {
            apage = pageInfo.split("/")[1];
        }
        return new String[]{stringBuffer.toString(), apage};
    }

    public static void appendToFile(String cdde, List<String> list) {
        try {
            String file = cdde + ".txt";
            FileManager.write(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String append(String url, String param, String v) {
        return url + "&" + param + "=" + URLEncoder.encode(v);
    }

    public static List<String> read(String code) throws IOException {
        code = getCode(code);
        List<Kline> days = new ArrayList();
        String str = FileUtil.read(getFile(code));
        JSONObject jsonObject = JSONObject.parseObject(str);
        String data = jsonObject.getString("data");
        String[] lines = data.split(";");
        int i = 0;
        for (String line : lines) {
            int idx = i;
            Kline dayLine = new Kline(line.trim(), 0);
            dayLine.allLineList = days;
            dayLine.setIdx(idx);
            days.add(dayLine);
//            Log.log(dayLine.date+" "+dayLine.open+" "+dayLine.max);

            i++;
        }
        Log.log("" + days.size());
        return null;
    }

    private static void log(String msg, String... vals) {
        ////System.out.println(String.format(msg, vals));
    }

    public static void initConcept() {
        List<String> lines = FileManager.readList("D:\\stock\\Test\\res\\code_hy\\0.txt");
        for (String line : lines) {
            line = line.trim().split("\\s+")[1].trim();
            String name = line;
            int page = Integer.parseInt(retriveOrGet(name, 1)[1]);
            for (int i = 0; i < page; i++) {
                String[] v = retriveOrGet(name, i + 1);
                if (v[0].trim().equals("")) {
                    return;
                }
                try {
                    FileManager.append("D:\\stock\\Test\\res\\code_hy\\" + name + ".txt", v[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        initConcept();

//        File file = new File(AbsStragety.FILE);
//        File fs[] = file.listFiles();
//        for (File item : fs) {
//            Thread.currentThread().sleep(10);
//            String name = item.getName().replace(".txt", "");
//            String v = retriveOrGet(name);
//        }

    }
}
