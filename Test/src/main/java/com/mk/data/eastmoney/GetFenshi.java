package com.mk.data.eastmoney;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.data.MKUrl;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.tool.stock.MinuteLine;
import com.mk.tool.stock.StockAllMinuteLine;
import com.mk.util.FileUtil;
import com.mk.util.HttpsUtils;
import com.mk.util.Log;
import com.mk.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetFenshi {

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
        if (dayIdx == 7) {
            date = DateUtil.getBeforeDate(date, 1);
        }

        String cb = "jsonp1693129622356";
        String url = "https://push2his.eastmoney.com/api/qt/stock/trends2/get";
        MKUrl mkUrl = new MKUrl(url);
        mkUrl.append("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        mkUrl.append("cb", cb);
        mkUrl.append("fields1", "f1,f2-,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13&fields2=f51,f52,f53,f54,f55,f56,f57,f58");
        mkUrl.append("fields2", "f51,f52,f53,f54,f55,f56,f57,f58");
        if (code.startsWith("6")) {
            mkUrl.append("secid", "1." + code);
        } else {
            mkUrl.append("secid", "0." + code);
        }
        mkUrl.append("ndays", "3");
        mkUrl.append("iscr", "0");
        mkUrl.append("iscca", "0");

        String aurl = mkUrl.get();
        String str = HttpsUtils.sendByHttpDFCFMinutes(aurl);
        str = str.replace(cb + "(", "");
        str = str.trim().substring(0, str.length() - 2);
        JSONObject jsonObject2 = JSONObject.parseObject(str);
        JSONArray ret = jsonObject2.getJSONObject("data").getJSONArray("trends");
        Log.log(code + "" + ret.size());
        String curDate = "";
        List<List<MinuteLine>> daysMinuteLines = new ArrayList<>();
        List<MinuteLine> minuteLines = null;
        for (int i = 0; i < ret.size(); i++) {
            String item = ret.getString(i);
            MinuteLine minuteLine = new MinuteLine(item, 0);
            if (!minuteLine.getDate().equalsIgnoreCase(curDate)) {
                curDate = minuteLine.getDate();
                minuteLines = new ArrayList<>();
                daysMinuteLines.add(minuteLines);
            }
            minuteLines.add(minuteLine);
        }

        for (int i = 0; i < daysMinuteLines.size(); i++) {
            List<MinuteLine> tmpMinuteLines = daysMinuteLines.get(i);
            MinuteLine first = tmpMinuteLines.get(0);
            String adate = first.getDateGNG();

            String firstDate = StockAllMinuteLine.getFirstDate();
            if (!StringUtil.isNull(firstDate)) {
                int dlt = StockAllMinuteLine.getBetweenDayLen(adate, firstDate);
                if (dlt < 0) {
                    continue;
                }
            }
            String absPath2 = (getMinutePath(adate, code));
            if (FileManager.exist(absPath2)) {
                continue;
            }

            FileManager.mkdirs(AbsStragety.FILE_MINUTE_DATE + "/" + adate);
            List<String> list = new ArrayList<>();
            for (MinuteLine minuteLine : tmpMinuteLines) {
                list.add(minuteLine.getLine());
            }
            appendToFile(absPath2, list);
        }
    }


    public static String getMinutePath(String date, String code) {
        return AbsStragety.FILE_MINUTE_DATE + "" + date + "/" + code + ".txt";
    }

    public static void appendToFile(String cdde, List<String> list) {
        try {
            String file = "";
            if (cdde.endsWith(".txt")) {
                file = cdde;
            } else {
                file = cdde + ".txt";
            }
            FileManager.write(file, list);
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
        File file = new File(AbsStragety.FILE);
        File fs[] = file.listFiles();
        for (File item : fs) {
            String code = getCode(item.getName());
            if (code.startsWith("688")) {
                continue;
            }
            if (code.startsWith("300")) {
                continue;
            }
            Log.log(code);
            try {
//                Thread.currentThread().sleep(50);
                GetFenshi.save(code);
            } catch (Exception e) {
            }
        }
    }
}
