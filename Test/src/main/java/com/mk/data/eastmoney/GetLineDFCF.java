package com.mk.data.eastmoney;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.util.FileUtil;
import com.mk.util.HttpsUtils;
import com.mk.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetLineDFCF {

    static String mode = "11";

    static String getFile(String code) {
        return "res/data/" + code + "_" + mode + ".txt";
    }

    static String getCode(String code) {
        code = "bk_" + code;
        return code;
    }


    public static void save(int pn) throws IOException {
        JSONObject jsonObject = new JSONObject();
        String cb = "jQuery112408751154292648391_1693092934833";
        String url = "https://84.push2.eastmoney.com/api/qt/clist/get?pz=20";
        url = append(url, "po", "" + 1);
        url = append(url, "np", "" + 1);
        url = append(url, "ut", "bd1d9ddb04089700cf9c27f6f7426281");
        url = append(url, "fltt", "" + 2);
        url = append(url, "invt", "" + 2);
        url = append(url, "cb", cb);
        url = append(url, "pn", "" + pn);
        url = append(url, "wbp2u", "wbp2u=|0|0|0|web");
        url = append(url, "fid", "f3");
        url = append(url, "fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152");
        url = append(url, "_", "1693092934870");
        url = append(url, "fs", "m:0 t:6,m:0 t:80,m:1 t:2,m:1 t:23,m:0 t:81 s:2048");
        String str = HttpsUtils.sendByHttpDFCF(url);
        str = str.replace(cb + "(", "");
        str = str.trim().substring(0, str.length() - 2);
        JSONObject jsonObject2 = JSONObject.parseObject(str);
        JSONArray ret = jsonObject2.getJSONObject("data").getJSONArray("diff");
        Date date = new Date();
        int dayIdx = DateUtil.dayForWeek(date);
        if (dayIdx == 1) {
            date = DateUtil.getBeforeDate(date, 2);
        }
        if (dayIdx == 7) {
            date = DateUtil.getBeforeDate(date, 1);
        }
        dayIdx = DateUtil.dayForWeek(date) - 1;
        String strDate = DateUtil.dateToString3(date);
        for (int i = 0; i < ret.size(); i++) {
            try {
                JSONObject item = ret.getJSONObject(i);
                String acode = item.getString("f12");
                String name = item.getString("f14");
                double  open = 0;
                open = item.getFloat("f17");
                double close = item.getFloat("f2");
                double max = item.getFloat("f15");
                double min = item.getFloat("f16");
                long vol = 100 * item.getLong("f5");
                double changeHand = item.getFloat("f8");
                Kline kline = new Kline();
                kline.setDate(strDate);
                kline.setOpen(open);
                kline.setClose(close);
                kline.setMax(max);
                kline.setMin(min);
                kline.setVolume(vol);
                kline.setHand(changeHand);

                List<String> list = AbsStragety.readList(acode + ".txt");
                boolean flagContein = false;
                for (int i0 = list.size() - 1; i0 >= list.size() - 5; i0--) {
                    String line = list.get(i0);
                    Kline lineKline = new Kline(line);
                    if (kline.getDate().equalsIgnoreCase(lineKline.getDate())) {
                        flagContein = true;
                        break;
                    }
                }
                if (flagContein) {
                    continue;
                }
                String line = kline.getFormatLine();
                list.add(line);
                appendToFile("D:\\111\\" + acode, list);

            } catch (Exception e) {

            }
        }

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
        //System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 45; i <= 276; i++) {
            Thread.currentThread().sleep(30);
            Log.log("page:" + i);
            GetLineDFCF.save(i);
        }

//        List<String> list = GetLineDFCF.read("881162");
//        AllModel.add("sh600547", list);
//        log(""+AllModel.nameMap);
    }
}
