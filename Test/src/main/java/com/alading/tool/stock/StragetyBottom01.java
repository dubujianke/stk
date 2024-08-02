package com.alading.tool.stock;

import com.alading.data.GetAllBankuaiCode;
import com.alading.data.eastmoney.GetConceptDFCF;
import com.alading.model.ConceptDFCF;
import com.alading.model.ScoreConcept;
import com.alading.model.Table;
import com.alading.tool.stock.model.KModel;
import com.alading.util.ExcelWrite2007Test;
import com.alading.util.StringUtil;
import com.huaien.core.util.DateUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

// FMWMQ-H8N8X-98WYT-GQVM8-TQ8DP
public class StragetyBottom01 extends AbsStragety {
    public static StringBuffer resultBuffer = new StringBuffer();
    public static boolean isBottom;
    public static boolean useReport;

    public void prs(String file, List<Kline> days, String date, String stagetyName, List<Weekline> weeks, List<MonthKline> moths, boolean usem, boolean usew, LineContext context) {
    }

    public static List<Result> retLines = new ArrayList<>();
    static Map<String, Result> map = new HashMap();

    public static int kn = 4;
    public static boolean allIsOK = false;
    public static String retx = "";


    public static List<String> getDates(String dir) {
        List<String> dates = new ArrayList<>();
        File file = new File(dir);
        File[] fs = file.listFiles();
        for (File fle : fs) {
            if (fle.getName().contains("~")) {
                continue;
            }
            dates.add(fle.getName().replace(".xlsx", "").replace("ret_", ""));
        }
        return dates;
    }

    public static List<String> getDirs(String dir) {
        dir = "D:\\stock\\Test\\res\\bottom\\";
        List<String> dates = new ArrayList<>();
        File file = new File(dir);
        File[] fs = file.listFiles();
        for (File fle : fs) {
            if (fle.getName().startsWith("ret_202")) {
                dates.add(fle.getAbsolutePath());
            }
        }
        return dates;
    }


    //modify 0, 1
    public static int ASTEP = 0;

    //    public static String method = "IsBottom";
//    public static String method = "IsBottomDeep";
//    public static String method = "IsBottomGetNextZT";
public static String method = "IsBottomHor";
//    public static String method = "IsBottomHorXRZL";

    public static void main(String[] args) throws IOException, InterruptedException {
        StragetyBottom01.usecmf = true;
        GetConceptDFCF.initAll();
//        List<ScoreConcept> ret = ConceptDFCF.getList("002253");

        AbsStragety.useSingleCode = false;
        AbsStragety.singleCode = "001255";
        List<String> dates = new ArrayList<>();
        int year = 2024;
        int mon = 4;
        int aday = 12;
        for (int m = mon; m <= mon; m++) {
            int month = m;
            int maxMonth = DateUtil.getDaysOfMonth(year, m);
            for (int i = aday; i <= aday; i++) {
                Date beginDate = DateUtil.stringToDate(year + "-" + (m < 10 ? "0" + m : "" + m) + "-" + (i < 10 ? "0" + i : "" + i));
                int weekIdx = DateUtil.getWeek(beginDate);
                if (weekIdx == 6 || weekIdx == 0) {
                    continue;
                }
                String date = String.format(year + "/" + (month < 10 ? "0" + month : "" + month) + "/%s", i < 10 ? "0" + i : "" + i);
                dates.add(date);
            }
        }

        for (String date : dates) {
            int dateStr = DateUtil.getWeek(DateUtil.stringToDate(date));
            if (dateStr == 6 || dateStr == 0) {
                continue;
            }
            main(true, date);
        }
    }

    public static void mainResult() {
        Stragety.isResult = true;
        for (Result result : retLines) {
            LineContext context = new LineContext();
            KModel kModel = new KModel();
            kModel.setRow(result.row);
            context.setkModel(kModel);
            context.setUseMinute(0);
            mainProcess(result.code, result.date, result.stragety, 1, 1, context);
        }
    }

    public static void main(boolean datesFlag, String aDate) throws IOException, InterruptedException {
        usecmf = false;
        FILE = "D:\\new_tdx\\T0001\\export\\";
        DATE = "2023/09/11";
        isBottom = true;
        IsBottom.initTable();
        AbsStragety.isMonitor = false;
        AbsStragety.use600 = true;
        AbsStragety.use000 = true;
        AbsStragety.MONITOR_LEN = -1;
        IsBottom.importantFlag = false;

        CTROL_LEN = 1;
        String absPath = "";
        kn = 4;
        DATE = aDate;
        String save = DateUtil.getNextWorkDate(DATE);
        absPath = AbsStragety.resDir + "res/bottom/" + save + ".txt";
        try {
            GetAllBankuaiCode.read();
            GetAllBankuaiCode.readSelect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LineContext context = new LineContext();
        Stragety.isResult = true;

        File file = new File(FILE);
        File fs[] = file.listFiles();
        int fIdx = 0;
        PrsLog.total = fs.length;
        for (File item : fs) {
            fIdx++;
//            Log.log(item.getName() + " " + aDate);
            KModel kModel = new KModel();
            context.setkModel(kModel);
            context.setUseMinute(0);
            context.setUseMinuteLen(5);
            if(!useSingleCode) {
                mainProcess(item.getName(), aDate, method, 1, 1, context);
            }else {
                mainProcess(singleCode, aDate, method, 1, 1, context);
                break;
            }

        }
        if (!Stragety.isResult) {
            mainResult();
        }

        save = DateUtil.getNextWorkDate(DATE);
        absPath = AbsStragety.resDir + "res/bottom/" + save.replaceAll("/", "-") + "_"+method+".xlsx";
        export(absPath);
        GlobalContext.clear();
    }

    public static void export(String absPath) {
        IsBottom.table.addFirst(StragetyZTBottom.headerRow);
        IsBottom.table.sort();
        try {
            ExcelWrite2007Test.mainABS(IsBottom.table, absPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addLine(Result line, String date) {
        String key = AbsStragety.getCode(line.code) + " " + date;
        if (map.get(key) != null) {
            return;
        }
        retLines.add(line);
        map.put(key, line);
        before_num++;
    }


}


