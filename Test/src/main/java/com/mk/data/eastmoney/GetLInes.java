package com.mk.data.eastmoney;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.data.MKUrl;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.util.FileUtil;
import com.mk.util.HttpsUtils;
import com.mk.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mk.tool.stock.AbsStragety.FILE;

public class GetLInes {
    static String mode = "11";
    static String getFile(String code) {
        return "res/data/" + code + "_" + mode + ".txt";
    }

    static String getCode(String code) {
        code = code.replace(".txt", "");
        return code;
    }

    public static void save(String code) throws IOException {
        Date date = new Date();
        int dayIdx = DateUtil.dayForWeek(date);
        if (dayIdx == 1) {
            date = DateUtil.getBeforeDate(date, 2);
        }
        String strDate = DateUtil.dateToString(date);

        File file = new File(FILE + code + ".txt");
        Date modifiedDate = new Date(file.lastModified());
        String strDate3 = DateUtil.dateToString3(modifiedDate);
        String curDate = DateUtil.dateToString3(new Date());
        if(curDate.equalsIgnoreCase(strDate3)) {
            return;
        }


        String cb = "jQuery351004319807009174559_1693351665609";
        String url = "https://push2his.eastmoney.com/api/qt/stock/kline/get";
        MKUrl mkUrl = new MKUrl(url);
        mkUrl.append("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        mkUrl.append("cb", cb);
        mkUrl.append("fields1", "f1,f2,f3,f4,f5,f6");
        mkUrl.append("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61");
        if(code.startsWith("6")) {
            mkUrl.append("secid", "1."+code);
        }else {
            mkUrl.append("secid", "0."+code);
        }
        mkUrl.append("klt", "101");
        mkUrl.append("fqt", "1");
        mkUrl.append("end", "20500101");
        mkUrl.append("lmt", "120");
        mkUrl.append("_", "1693351665622");

        String aurl = mkUrl.get();
        String str = HttpsUtils.sendByHttpDFCFLines(aurl);
        str = str.replace(cb + "(", "");
        str = str.trim().substring(0, str.length() - 2);
        JSONObject jsonObject2 = JSONObject.parseObject(str);
        JSONArray ret = jsonObject2.getJSONObject("data").getJSONArray("klines");

        List<Kline> klines = new ArrayList<>();
        for (int i = 0; i < ret.size(); i++) {
            String item = ret.getString(i);
            Kline minuteLine = new Kline(item, 1);
            klines.add(minuteLine);
        }


        List<String> list = AbsStragety.readList(code + ".txt");
        String item = list.get(list.size()-1);
        Kline kline = new Kline(item);

        int idx = getFromIdx(klines, kline);
        for(int i=idx+1; i<klines.size(); i++) {
            list.add(klines.get(i).getFormatLine());
        }

        String absPath = FILE + code+".txt";
        appendToFile(absPath, list);
    }


    public static int getFromIdx(List<Kline> list, Kline kline) {
        for (int i0 = list.size() - 1; i0 >= 0; i0--) {
            Kline lineKline = list.get(i0);
            if (kline.getDate().equalsIgnoreCase(lineKline.getDate())) {
                return i0;
            }
        }
        return -1;
    }

    public static String getMinutePath(String date, String code) {
        return AbsStragety.FILE_MINUTE_DATE + ""+date+"/"+code + ".txt";
    }

    public static void appendToFile(String cdde, List<String> list) {
        try {
            String file = "";
            if(cdde.endsWith(".txt")) {
                file = cdde;
            }else {
                file = cdde + ".txt";
            }
            FileManager.writeGB(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) throws IOException {
        File file = new File(FILE);
        File fs[] = file.listFiles();
        for (File item : fs) {
            String code = getCode(item.getName());
            if(code.startsWith("68")) {
                continue;
            }
            if(code.startsWith("30")) {
                continue;
            }
            Log.log(code);
//            if (item.toString().contains("600000")) {
//                com.mk.tool.stock.Log.log(item.toString());
//            }
            try {
                GetLInes.save(code);
            }catch (Exception e) {}
        }

    }
}
