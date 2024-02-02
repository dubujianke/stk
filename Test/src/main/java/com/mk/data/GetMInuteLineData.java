package com.mk.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.tool.stock.*;
import com.mk.tool.stock.AbsStragety;
import com.mk.util.FileUtil;
import com.mk.util.HttpsUtils;
import com.mk.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//http://d.10jqka.com.cn/v4/line/bk_881170/11/last.js  week
//http://d.10jqka.com.cn/v4/line/bk_881170/21/last.js month
//http://q.10jqka.com.cn/thshy/
public class GetMInuteLineData {

    static String mode = "99";

    static String getFile(String code) {
        return "res/data/" + code + "_" + mode + ".txt";
    }

    static String getCode2(String code) {
        if (code.startsWith("0")) {
            code = "sz" + code;
        } else if (code.startsWith("6")) {
            code = "sh" + code;
        }
        return code;
    }


    static String getCode(String code) {
        code = code.replace(".txt", "");
        return code;
    }


    public static StockDayMinuteLine saveAndGet(SingleContext context, String code) {
        try {
            List<Kline> days = context.getDays();

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
            if(code.startsWith("6")) {
                mkUrl.append("secid", "1."+code);
            }else {
                mkUrl.append("secid", "0."+code);
            }
            mkUrl.append("ndays", "1");
            mkUrl.append("iscr", "0");
            mkUrl.append("iscca", "0");

            String aurl = mkUrl.get();
            String str = HttpsUtils.sendByHttpDFCFMinutes(aurl);
            str = str.replace(cb + "(", "");
            str = str.trim().substring(0, str.length() - 2);
            JSONObject jsonObject2 = JSONObject.parseObject(str);
            JSONArray data = jsonObject2.getJSONObject("data").getJSONArray("trends");
            Log.log(code+""+data.size());
            String curDate = "";
            StockDayMinuteLine stockDayMinuteLine = null;
            for (int i = 0; i < data.size(); i++) {
                String item = data.getString(i);
                MinuteLine minuteLine = new MinuteLine(item, 0);

                int idxDate = AbsStragety.getIdx(days, minuteLine.getDate());
                Kline kline = days.get(idxDate);
                if (stockDayMinuteLine == null) {
                    stockDayMinuteLine = new StockDayMinuteLine();
                    stockDayMinuteLine.setDate(minuteLine.getDate());
                    stockDayMinuteLine.setKline(kline);
                } else {
                    int flag = stockDayMinuteLine.isSameDate(minuteLine);
                    if (flag == 0) {
                        stockDayMinuteLine = new StockDayMinuteLine();
                        stockDayMinuteLine.setDate(minuteLine.getDate());
                        stockDayMinuteLine.setKline(kline);
                    }
                }
                if (data.size() - 1 == i) {
                    if(AbsStragety.printMinute) {
                        Log.log(context.getsIdx()+" "+code + " " + i + " " + minuteLine);
                    }
                    if(minuteLine.getTime().equalsIgnoreCase("1459")) {
                        Log.logEnd(code + " " + i + " " + minuteLine);
                    }
                }
                stockDayMinuteLine.add(minuteLine);
            }
            PrsLog.log(stockDayMinuteLine.allLineList.size()+" idx:");
            return stockDayMinuteLine;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static StockDayMinuteLine saveAndGet_(SingleContext context, String code) {
        try {
            List<Kline> days = context.getDays();
            code = getCode(code);
            JSONObject jsonObject = new JSONObject();
            String url = "https://quotes.sina.cn/cn/api/openapi.php/CN_MinlineService.getMinlineData?symbol=" + code + "&callback=var%20t1" + code + "=&dpc=1";
            String str = HttpsUtils.sendByHttpMinute(jsonObject, url);
            String head = "var t1" + code + "=(";
            int idx = str.indexOf(head);

            str = str.substring(idx + head.length(), str.length() - 2);
            JSONObject jsonObject2 = JSONObject.parseObject(str);
            JSONArray data = jsonObject2.getJSONObject("result").getJSONArray("data");

            StockDayMinuteLine stockDayMinuteLine = null;
            for (int i = 0; i < data.size(); i++) {
                MinuteLine minuteLine = new MinuteLine();
                JSONObject json = data.getJSONObject(i);
                String time = json.getString("m");
                float p = json.getFloat("p");
                int v = json.getInteger("v");
                float avg_p = json.getFloat("avg_p");
                String date = DateUtil.dateToString3(new Date());

                String time2 = DateUtil.trans(time, "HH:mm:ss", "HHmm");
                minuteLine.setTime(time2);
                minuteLine.setGlobalIdx(i);
                minuteLine.setPrice(p);
                minuteLine.setVol(v / 100);
                minuteLine.setAvgPrice(avg_p);
                minuteLine.setDate(date);

                int idxDate = AbsStragety.getIdx(days, minuteLine.getDate());
                Kline kline = days.get(idxDate);
                if (stockDayMinuteLine == null) {
                    stockDayMinuteLine = new StockDayMinuteLine();
                    stockDayMinuteLine.setDate(minuteLine.getDate());
                    stockDayMinuteLine.setKline(kline);
                } else {
                    int flag = stockDayMinuteLine.isSameDate(minuteLine);
                    if (flag == 0) {
                        stockDayMinuteLine = new StockDayMinuteLine();
                        stockDayMinuteLine.setDate(minuteLine.getDate());
                        stockDayMinuteLine.setKline(kline);
                    }
                }
                if (data.size() - 1 == i) {
                    if(AbsStragety.printMinute) {
                        Log.log(context.getsIdx()+" "+code + " " + i + " " + minuteLine);
                    }
                    if(minuteLine.getTime().equalsIgnoreCase("1459")) {
                        Log.logEnd(code + " " + i + " " + minuteLine);
                    }
                }
                stockDayMinuteLine.add(minuteLine);
            }
            return stockDayMinuteLine;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void addIDX(SingleContext context) {
        if(AbsStragety.IDX_PROXY==-1) {
            context.idxProxy = 240;
            return;
        }
        context.idxProxy++;
    }

    public static StockDayMinuteLine saveAndGetProxyLocal(SingleContext context, String code, String date) {
        try {
            StockAllMinuteLine stockAllMinuteLine = context.stockAllMinuteLine;
            StockDayMinuteLine stockDayMinuteLine_ = stockAllMinuteLine.getStockDayMinuteLineProxy(date);
            if(stockDayMinuteLine_ == null) {
                int a = 0;
            }
            List<MinuteLine> proxyAllLineList = stockDayMinuteLine_.allLineList;
            List<Kline> days = context.getDays();
            code = getCode(code);

            StockDayMinuteLine stockDayMinuteLine = null;
            addIDX(context);
            if (context.idxProxy > proxyAllLineList.size()) {
                context.idxProxy = proxyAllLineList.size();
            }

            for (int i = 0; i < context.idxProxy; i++) {
                MinuteLine minuteLine = proxyAllLineList.get(i);
                int idxDate = AbsStragety.getIdx(days, minuteLine.getDate());
                Kline kline = days.get(idxDate);
                if (stockDayMinuteLine == null) {
                    stockDayMinuteLine = new StockDayMinuteLine();
                    stockDayMinuteLine.setDate(minuteLine.getDate());
                    stockDayMinuteLine.setKline(kline);
                } else {
                    int flag = stockDayMinuteLine.isSameDate(minuteLine);
                    if (flag == 0) {
                        stockDayMinuteLine = new StockDayMinuteLine();
                        stockDayMinuteLine.setDate(minuteLine.getDate());
                        stockDayMinuteLine.setKline(kline);
                    }
                }
                if (context.idxProxy - 1 == i) {
                    if(AbsStragety.printMinute) {
                        Log.log(code + " " + i + " " + minuteLine);
                    }
                    if(minuteLine.getTime().equalsIgnoreCase("1459")) {
                        Log.logEnd(code + " " + i + " " + minuteLine);
                    }
                }
                stockDayMinuteLine.add(minuteLine);
            }
            return stockDayMinuteLine;
        } catch (Exception e) {
//            e.printStackTrace();

        }
        return null;
    }

    public static StockDayMinuteLine saveAndGetProxy(SingleContext context, String code) {
        try {
            List<Kline> days = context.getDays();
            code = getCode(code);
//        if (FileManager.exist(getFile(code))) {
//            return;
//        }
            if (context.dataProxy == null) {
                JSONObject jsonObject = new JSONObject();
                String url = "https://quotes.sina.cn/cn/api/openapi.php/CN_MinlineService.getMinlineData?symbol=" + code + "&callback=var%20t1" + code + "=&dpc=1";
                String str = HttpsUtils.sendByHttpMinute(jsonObject, url);
                String head = "var t1" + code + "=(";
                int idx = str.indexOf(head);

                str = str.substring(idx + head.length(), str.length() - 2);
                JSONObject jsonObject2 = JSONObject.parseObject(str);
                JSONArray data = jsonObject2.getJSONObject("result").getJSONArray("data");
                context.dataProxy = data;
            }

            StockDayMinuteLine stockDayMinuteLine = null;
            addIDX(context);
            if (context.idxProxy > context.dataProxy.size()) {
                context.idxProxy = context.dataProxy.size();
            }
            for (int i = 0; i < context.idxProxy; i++) {
                MinuteLine minuteLine = new MinuteLine();
                JSONObject json = context.dataProxy.getJSONObject(i);
                String time = json.getString("m");
                float p = json.getFloat("p");
                int v = json.getInteger("v");
                float avg_p = json.getFloat("avg_p");
                String date = DateUtil.dateToString3(new Date());

                String time2 = DateUtil.trans(time, "HH:mm:ss", "HHmm");
                minuteLine.setTime(time2);
                minuteLine.setGlobalIdx(i);
                minuteLine.setPrice(p);
                minuteLine.setVol(v / 100);
                minuteLine.setAvgPrice(avg_p);
                minuteLine.setDate(date);

                int idxDate = AbsStragety.getIdx(days, minuteLine.getDate());
                Kline kline = days.get(idxDate);
                if (stockDayMinuteLine == null) {
                    stockDayMinuteLine = new StockDayMinuteLine();
                    stockDayMinuteLine.setDate(minuteLine.getDate());
                    stockDayMinuteLine.setKline(kline);
                } else {
                    int flag = stockDayMinuteLine.isSameDate(minuteLine);
                    if (flag == 0) {
                        stockDayMinuteLine = new StockDayMinuteLine();
                        stockDayMinuteLine.setDate(minuteLine.getDate());
                        stockDayMinuteLine.setKline(kline);
                    }
                }
                if (context.idxProxy - 1 == i) {
//                    Log.log("------------");
//                    Log.log(""+minuteLine);
                }
                stockDayMinuteLine.add(minuteLine);
            }
            return stockDayMinuteLine;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
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
            i++;
        }
        return null;
    }


    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        while (true) {
//            long t1 = System.currentTimeMillis();
//            StockDayMinuteLine stockDayMinuteLine = GetMInuteLineData.saveAndGet("600547");
//            long t2 = System.currentTimeMillis();
//            Log.log("dlt:" + (t2 - t1));
//            Thread.currentThread().sleep(60 * 1000);
//        }

//        log(""+AllModel.nameMap);
    }
}
