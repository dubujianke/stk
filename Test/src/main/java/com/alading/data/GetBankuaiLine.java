package com.alading.data;

import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.FileManager;
import com.alading.model.AllModel;
import com.alading.model.Bankuai;
import com.alading.tool.stock.Kline;
import com.alading.util.FileUtil;
import com.alading.util.HttpsUtils;
import com.alading.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//http://d.10jqka.com.cn/v4/line/bk_881170/11/last.js  week
//http://d.10jqka.com.cn/v4/line/bk_881170/21/last.js month
//http://q.10jqka.com.cn/thshy/
public class GetBankuaiLine {

    public static String DAY = "01";
    public static String WEEK = "11";
    public static String MONTH = "21";

    static String getFile(String code, String mode) {
        return "res/data/" + mode+"/" + code + "_"+mode+".txt";
    }

    static String getCode(String code) {
        if(code.startsWith("1A")) {
            code = "zs_"+code;
        }
        if(code.startsWith("88")) {
            code = "bk_"+code;
        }
        if(code.startsWith("0")) {
            code = "sz_"+code;
        }
        if(code.startsWith("6")) {
            code = "sh_"+code;
        }

        return code;
    }

    //http://d.10jqka.com.cn/v4/line/zs_1A0001/01/last.js
    //http://d.10jqka.com.cn/v4/line/zs_1A0001/01/today.js
    //http://d.10jqka.com.cn/v4/line/zs_1A0001/11/last.js
    //http://d.10jqka.com.cn/v4/line/zs_399001/11/last.js

    public static String sendByHttp(JSONObject jsonObject, String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Host", "d.10jqka.com.cn");
            httpPost.setHeader("Referer", "http://q.10jqka.com.cn/");
            httpPost.setHeader("Cookie", "__bid_n=1863b4c6eee48d7f304207; FPTOKEN=sMt7wpi3ZHPxhFl3U+rBQpW3xvj2LSS73Uk786mKYBq3+kVr7PdSrI83DnjwxEgN+vOPZylQv2gkGDekJuG1ZBuQTMPJG3Myuzx/KokIovPpUyW3AbBu6WbAQ4m9RfDGAq6wEOaoSNdTv+gzQ1bmZf7IgXJbUvIbe6BogH6G2Q4T/qziIlwhF4RLAUrpSr2jN2pwwaYJnLq/Vx/q8qKzu+pOPrKVzFA1Vb0NaR9BH3RaSGcXBwbBGNWO66CcC68c6cl2CExOSPLFbAVrRCTU4jo1h7HVqU9LDaNJ8gmHfyAHJgr55KrpHQI8x+Fj+NGa6P2LqHmyGfx+BbuMAgUhw4roZtlgEIMQMXNTiv2KQQ67Q3YuOu0y0ZnQFfYBLPplneZ/S0SOgFM80IrCApjw3Q==|xVDBvZBOw9UZtEM+IxZllu3ssEYgVpL9kVZ19dKVVsY=|10|df7573dbc0114bd2965f9645bf242f17; spversion=20130314; historystock=000063%7C*%7C000060%7C*%7C300548; cid=f6a575aac6de5adb5bf4c6bbe74e04a11687047431; searchGuide=sg; Hm_lvt_722143063e4892925903024537075d0d=1687047188,1688043221,1688684642,1688772698; Hm_lvt_929f8b362150b1f77b477230541dbbc2=1687047188,1688043221,1688684643,1688772698; Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1=1687047188,1688043221,1688684643,1688772698; u_dpass=Po06CJEsPkBOYV4uUA8bshMkhFQyJ2T1uOVXoaYezfceUGdfHBNdtwytFjtudTfC%2FsBAGfA5tlbuzYBqqcUNFA%3D%3D; u_did=96331274AB2F42FDAB7B997182770AED; u_ttype=WEB; u_ukey=A10702B8689642C6BE607730E11E6E4A; u_uver=1.0.0; user=MDptb181ODQ0MDc2NDg6Ok5vbmU6NTAwOjU5NDQwNzY0ODo3LDExMTExMTExMTExLDQwOzQ0LDExLDQwOzYsMSw0MDs1LDEsNDA7MSwxMDEsNDA7MiwxLDQwOzMsMSw0MDs1LDEsNDA7OCwwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMSw0MDsxMDIsMSw0MDoyNDo6OjU4NDQwNzY0ODoxNjg4NzcyODQ3Ojo6MTYyMzE1ODM0MDo2MDQ4MDA6MDoxYmQ4MmFjMTM4MDBmOTFiNTNhMjZkYzQyYjcxZGMwMTg6ZGVmYXVsdF80OjE%3D; userid=584407648; u_name=mo_584407648; escapename=mo_584407648; ticket=a3e9d1e75593f38681d113ba55dfd4e9; user_status=0; utk=1471ee6858e5436499986c7bf3c06b68; log=; Hm_lpvt_722143063e4892925903024537075d0d=1688776934; Hm_lpvt_929f8b362150b1f77b477230541dbbc2=1688776934; Hm_lpvt_78c58f01938e4d85eaf619eae71b4ed1=1688776934; v=A0sQb9clq7p4DPduGYzpZvKl2uQw4F9i2fQjFr1IJwrh3GWaRbDvsunEs2TO");
            httpPost.setHeader("content-type", "text/*");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                return jsObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpResponse.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save(String code, String mode) throws IOException {
        code =  getCode(code);
        if (FileManager.exist(getFile(code, mode))) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        String str = sendByHttp(jsonObject, "http://d.10jqka.com.cn/v4/line/" + code + "/"+mode+"/last.js");
        str = str.replace("quotebridge_v4_line_"+code+"_"+mode+"_last(", "");
        str = str.trim().substring(0, str.length() - 1);
        JSONObject jsonObject2 = JSONObject.parseObject(str);
        FileUtil.write(getFile(code, mode), jsonObject2.toJSONString());
        //System.out.println(jsonObject2.getInteger("total"));
    }

    public static List<Kline> read(String code, String mode, boolean log) throws IOException {
        code =  getCode(code);
        List<Kline> days = new ArrayList();
        String str = FileUtil.read(getFile(code, mode));
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
            i++;
            if(log) {
                Log.log(dayLine.date+" "+ dayLine.getOpen() +" "+ dayLine.getMax());
            }
        }
        return days;
    }


    private static void log(String msg, String... vals) {
        System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException {
        GetAllBankuaiCode.read();
        Map<String, Bankuai> infoMap = AllModel.infoMap;
//        for(Bankuai info : infoMap.values()) {
//            try {
//                Thread.currentThread().sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            String code = info.getCode();
//            GetBankuaiLine.save(code, WEEK);
//            List<Kline> list = GetBankuaiLine.read(code, WEEK, false);
//            Log.log("================>"+list.size());
//        }
        GetBankuaiLine.save("1A0001", MONTH);
//        List<Kline> list = GetBankuaiLine.read("1A0001", WEEK);

    }
}
