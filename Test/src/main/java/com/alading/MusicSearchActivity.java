package com.alading;

import com.alading.data.GetPrice;
import com.alading.model.GlobalData;
import com.alading.model.Stock;
import com.alading.tool.stock.AbsStragety;
import com.alading.tool.stock.Log;
import com.alading.tool.stock.SingleContext;
import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import java.io.IOException;
import java.util.*;

public class MusicSearchActivity {
    public static List<String> list = null;
    public static void main(String[] args) {
//        list = FileManager.readList("d:/stks.txt");
//        int len = list.size();
        new MusicSearchActivity().startTime();
    }

    static int idx = 0;

    public static String getParam() {
        String ret = "";
        int len = 100;
        int from = idx * len;
        if (from >= list.size()) {
            idx = 0;
            from = idx * len;
        }
        StringBuffer sbf = new StringBuffer();
        int id = 0;
        for (int i = from; i < from + len; i++) {
            if (i >= list.size()) {
                break;
            }
            String line = list.get(i).split("\\s+")[0].trim();
            String item = getCodeType(line) + line;
            if (id > 0) {
                sbf.append(",");
            }
            sbf.append(item);
            id++;
        }
        idx++;
        return sbf.toString();
    }

    public void startTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        initTask();
                        Thread.currentThread().sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void log(String msg) {
//        Log.i("getprice", msg);
    }

    public static void process() {
        final List<Stock> tempList = new ArrayList<Stock>();
        Stock[] stocks = GlobalData.get();
        for (Stock stock : stocks) {
            double zf = stock.zf;
            if (zf < 1.9) {
                continue;
            }
            if (GlobalData.isNoOK(stock)) {
                continue;
            }
            tempList.add(stock);
        }

        tempList.sort(new Comparator<Stock>() {
            @Override
            public int compare(Stock stock, Stock t1) {
                return (int) (100 * (stock.zf - t1.zf));
            }
        });
//        System.out.println("---------------------->"+tempList.get(0).getCode()+" "+tempList.get(0).zf);
        GlobalData.setRet(tempList);
    }

    static long dlt = 0;

    class InitDataTask implements Runnable {
        @Override
        public void run() {
            try {
                String url = "https://hq.sinajs.cn/rn=1710776893472&list=" + getParam();

                String dt = DateUtil.dateToString(new Date());
                Date beginTime = DateUtil.strToTime(dt + " 09:30:00");
                dlt = DateUtil.BetweenMinutes(new Date(), beginTime);

                if (dlt < 0) {
                    Log.log("time no happed:" + dlt);
                    return;
                }
                //10:08
                if (dlt > 90 && dlt<210) {
                    Log.log("time no happed:" + dlt);
                    return;
                }


                String content = GetPrice.sendByHttp(url);
                String[] lines = content.split("\n");
                List<Stock> stks = new ArrayList<>();
//                Log.d("getptice", "len:" + lines.length);
                int i = 0;
                for (String line : lines) {
                    i++;
                    if(i==1) {
                        Log.logFile(line);
                    }
                    String[] items = line.split("=");
                    String item0 = items[0];
                    String[] codes = item0.split("_");
                    String code = codes[codes.length - 1];
                    String item1 = items[1];
                    String[] vs = item1.split(",");
                    Stock stock = new Stock();
                    stock.setCode(code);
                    stock.name = vs[0];
                    stock.setTime(DateUtil.dateTimeToString(new Date()));
                    stock.zf = Double.parseDouble(vs[3]);
                    if (stock.zf < -1.5) {
                        GlobalData.addNoOK(stock);
                        continue;
                    }
                    stks.add(stock);
                }

                for (Stock stock : stks) {
                    if (stock.zf >= 2.6) {
                        GlobalData.add(stock);
                    }
                }
                process();
                log(content);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    void initTask() {
        new InitDataTask().run();
    }


    public static String getCodeType(String code) {
        String codeType = "";
        if (code.startsWith("6")) {
            codeType = "s_sh";
        } else {
            codeType = "s_sz";
        }
        return codeType;
    }


}
