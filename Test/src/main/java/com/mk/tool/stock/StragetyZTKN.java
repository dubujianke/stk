package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.data.GetAllBankuaiCode;
import com.mk.report.JJResults;
import com.mk.util.StringUtil;

import java.io.IOException;
import java.util.*;

/**
 * minute time stragety
 */
public class StragetyZTKN extends AbsStragety {

    public void prs(String file, List<Kline> days, String date, String stagetyName, List<Weekline> weeks, List<MonthKline> moths, boolean usem, boolean usew) {
    }


    public static Map<String, Object> resultMap = new HashMap<>();

    public static void add(String code, Object typeResult) {
        resultMap.put(code, typeResult);
    }

    public static Object get(String code) {
        return resultMap.get(code);
    }

    public static List<Result> retLines = new ArrayList<>();
    public static boolean useMinute;

    public static void addLine(Result line) {
        retLines.add(line);
        before_num++;
    }

    public static void clear() {
        resultMap.clear();
        retLines.clear();
        before_num = 0;
    }

    public static void mainResult() {
        Stragety.isResult = true;
        LineContext context = new LineContext();
        context.setUseMinute(1);
//        Log.log("================================================================================================================" + before_num);
        for (Result result : retLines) {
            mainProcess(result.code, result.date, result.stragety, 1, 1, context);
        }
//        Log.log(">>" + after_num);
    }

    public static int kn = 1;

    public static void main(String[] args) throws InterruptedException {
        try {
            AbsStragety.isMonitor = true;
            AbsStragety.isNetProxy = false;
            AbsStragety.isNetLocalProxy = false;
            useMinute = true;
            GetAllBankuaiCode.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int idx = 0;
        StragetyZTKN.clear();
        if (!AbsStragety.isMonitor) {
            mainPrs();
        } else {
            kn = 9;
            while (true) {
                idx++;
//                Log.d("" + idx);
                mainPrs();
                boolean flag = GlobalContext.isFinish();
                if(flag) {
                    break;
                }
                String info = JJResults.getInfo();
                if(!StringUtil.isNull(info)) {
//                   Log.log(info);
                }
                if (AbsStragety.isNetProxy) {
                } else {
                    Thread.currentThread().sleep(30 * 1000);
                }
            }
        }
    }

    public static void mainPrs() {
        kn = 9;
        FILE = "D:\\new_tdx\\T0001\\export\\";
        DATE = "2023/08/03";
        CTROL_LEN = 1;
        AbsStragety.CURDAY_OPEN = true;
        LineContext context = new LineContext();
        context.setUseMinute(0);
        if (kn == 1) {
            context.setUseMinute(1);
//            mainProcess("601086.txt", "2023/07/26", "IsZhangtingKN", 1, 1, context);
//            mainProcess("002211.txt", "2023/07/03", "IsZhangtingKN", 1, 1, context);
//            mainProcess("003010.txt", "2023/05/23", "IsZhangtingKN", 1, 1, context);//Y  function2
//            mainProcess("600685.txt", "2023/05/15", "IsZhangtingKN", 1, 1, context);//Y week30
//            mainProcess("600482.txt", "2023/05/15", "IsZhangtingKN", 1, 1, context);//Y week10
//            mainProcess("605133.txt", "2023/06/09", "IsZhangtingKN", 1, 1, context);//week30 +9
//            mainProcess("603097.txt", "2023/06/05", "IsZhangtingKN", 1, 1, context);//Y week30
//            mainProcess("002211.txt", "2023/07/03", "IsZhangtingKN", 1, 1, context);//Y week120
//            mainProcess("600310.txt", "2023/05/22", "IsZhangtingKN", 1, 1, context);//Y week60

            mainProcess("002719.txt", "2023/07/21", "IsZhangtingKN", 1, 1, context);//Y week120
        } else if (kn == 9) {
            context.setUseMinute(1);
            String dateMonitor = DateUtil.dateToString(new Date());
            dateMonitor = "2023-09-12";
            List<String> list = FileManager.readList("res/kn/"+dateMonitor+".txt");
            for(String line:list) {
                String items[] = line.split("\\s+");
                String code = items[0].trim();
                String type = items[2].trim();
                String date = items[3].trim();
                context.setType(type);
                mainProcess(code+".txt", date, "IsZhangtingKN", 1, 1, context);
            }
        } else if (kn == 2) {
            context.setUseMinute(1);
            mainProcess("000950.txt", "2023/08/11", "IsZhangtingKN", 1, 1, context);//ERROR
        } else if (kn == 3) {
            mainProcess("", "", "IsZhangtingKN", 0, 0, context);
        } else if (kn == 4) {
            mainProcess("", "", "IsZhangtingKN", 1, 1, context);
        }
        mainResult();
//        Log.log("TOTAL_OK:" + KN.CNT);
    }

}
