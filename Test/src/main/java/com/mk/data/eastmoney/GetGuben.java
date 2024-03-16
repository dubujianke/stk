package com.mk.data.eastmoney;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.FileManager;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.util.FileUtil;
import com.mk.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//https://84.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112408751154292648391_1693092934833&pn=3&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&wbp2u=|0|0|0|web&fid=f3&fs=m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1693092934870
public class GetGuben {
    static String mode = "11";

    static String getFile(String code) {
        return "res/data/" + code + "_" + mode + ".txt";
    }

    static String getCode(String code) {
        code = "bk_" + code;
        return code;
    }

    public static double retriveOrGetShizhi(String code, double v)  {
        return retriveOrGet(code)*v;
    }

    public static double retriveOrGet(String code)  {
//        try {
            try {
                String v = FileManager.read(AbsStragety.resDir+"/res/code_shizhi/" + code + ".txt");
                if (!StrUtil.isEmpty(v)) {
                    return Double.parseDouble(v);
                }
            } catch (Exception e) {

            }

//            JSONObject jsonObject = new JSONObject();
//            String cb = "jQuery112408751154292648391_1693092934833";
//            String url = "https://push2.eastmoney.com/api/qt/stock/get?invt=2&fltt=1";
//            url = append(url, "ut", "fa5fd1943c7b386f172d6893dbfba10b");
//            url = append(url, "cb", cb);
//            if (code.startsWith("6")) {
//                url = append(url, "secid", "1." + code);
//            } else {
//                url = append(url, "secid", "0." + code);
//            }
//            url = append(url, "wbp2u", "wbp2u=|0|0|0|web");
//            url = append(url, "invt", "2");
//            url = append(url, "fltt", "1");
//            url = append(url, "fid", "f3");
//            url = append(url, "fields", "f58,f734,f107,f57,f43,f59,f169,f301,f60,f170,f152,f177,f111,f46,f44,f45,f47,f260,f48,f261,f279,f277,f278,f288,f19,f17,f531,f15,f13,f11,f20,f18,f16,f14,f12,f39,f37,f35,f33,f31,f40,f38,f36,f34,f32,f211,f212,f213,f214,f215,f210,f209,f208,f207,f206,f161,f49,f171,f50,f86,f84,f85,f168,f108,f116,f167,f164,f162,f163,f92,f71,f117,f292,f51,f52,f191,f192,f262,f294,f295,f269,f270,f256,f257,f285,f286,f748,f747");
//            url = append(url, "wbp2u", "wbp2u=|0|0|0|web");
//            url = append(url, "_", "1693146159830");
//            String str = HttpsUtils.sendByHttpDFCF(url);
//            str = str.replace(cb + "(", "");
//            str = str.trim().substring(0, str.length() - 2);
//            JSONObject jsonObject2 = JSONObject.parseObject(str);
//            JSONObject ret = jsonObject2.getJSONObject("data");
//            Date date = new Date();
//            int dayIdx = DateUtil.dayForWeek(date);
//            if (dayIdx == 1) {
//                date = DateUtil.getBeforeDate(date, 2);
//            }
//            if (dayIdx == 7) {
//                date = DateUtil.getBeforeDate(date, 1);
//            }
//            dayIdx = DateUtil.dayForWeek(date) - 1;
//            String strDate = DateUtil.dateToString3(date);
////        long ltgb = ret.getLong("f85");
//            double ltsz = ret.getDouble("f117");
//            ltsz = ltsz / 100000000;
////            Log.log("shizi" + code + " " + ltsz);
//
//            FileManager.write(AbsStragety.resDir+"/res/code_shizhi/" + code + ".txt", ""+ltsz);
//            return (float) ltsz;
//        } catch (Exception e) {
//        }
        return 0.0f;
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

    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File(AbsStragety.FILE);
        File fs[] = file.listFiles();
        for (File item : fs) {
            Thread.currentThread().sleep(10);
            String name = item.getName().replace(".txt", "");
//            if (item.toString().contains("002269")) {
//                int a = 0;
//                com.mk.tool.stock.Log.log(item.toString());
//            }
            double v = GetGuben.retriveOrGet(name);
            FileManager.write("./res/code_shizhi/" + name + ".txt", "" + v);
        }

//        List<String> list = GetGuben.read("881162");
    }
}
