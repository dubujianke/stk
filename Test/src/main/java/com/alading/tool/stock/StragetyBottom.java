package com.alading.tool.stock;

import com.huaien.core.util.DateUtil;
import com.alading.data.GetAllBankuaiCode;
import com.alading.data.eastmoney.GetConceptDFCF;
import com.alading.model.ConceptDFCF;
import com.alading.model.ScoreConcept;
import com.alading.model.Table;
import com.alading.tool.stock.model.KModel;
import com.alading.util.ExcelWrite2007Test;
import com.alading.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

// FMWMQ-H8N8X-98WYT-GQVM8-TQ8DP
public class StragetyBottom extends AbsStragety {
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

    public static int step = 0;
    public static boolean flag = true;
    //    public static String method = "IsBottom";
//    public static String method = "IsBottomDeep";
//    public static String method = "IsBottomHor";
    public static String method = "IsBottomGetNextZT";
//    public static String method = "IsBottomHorXRZL";

    public static void main(String[] args) throws IOException, InterruptedException {
        StragetyBottom.usecmf = true;
        GetConceptDFCF.initAll();
        List<ScoreConcept> ret = ConceptDFCF.getList("002049");
        if (ASTEP == 0) {
            step = 0;
            flag = true;
        } else if (ASTEP == 1) {
            step = 1;
            flag = false;
        }

        AbsStragety.useSingleCode = false;
        AbsStragety.singleCode = "002253";
        if (flag) {
            List<String> dates = new ArrayList<>();
            int year = 2024;
            for (int m = 3; m <= 3; m++) {
                int month = m;
                int maxMonth = DateUtil.getDaysOfMonth(year, m);
                for (int i = 1; i <= 22; i++) {
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
        } else {
            List<String> dirs = getDirs("");
            for (String absPath : dirs) {
                if (!absPath.endsWith("_2")) {
                    continue;
                }
                BOTTOM_PATH = absPath + "\\";
                System.out.println(absPath);
                ExcelWrite2007Test.PATH = absPath + "\\";
                List<String> dates = new ArrayList<>();
                dates = getDates(BOTTOM_PATH);
                for (int i = 0; i < dates.size(); i++) {
                    if (i < dates.size() - 1) {
                        continue;
                    }
                    String date = dates.get(i);
                    if (date.endsWith("_")) {
                        break;
                    }
                    retx = date;
                    useReport = true;
                    main(false, retx);
                }
            }
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
        if (datesFlag) {
            kn = 4;
            DATE = aDate;
            String save = DateUtil.getNextWorkDate(DATE);
            absPath = AbsStragety.resDir + "res/bottom/" + save + ".txt";
        } else {
            kn = 1;
            if (kn == 1) {
            } else {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                Scanner scanner = new Scanner(System.in);
                System.out.print("DATE:2023/");
                String tmp = scanner.nextLine().trim();
                if (tmp.length() == 4) {
                    tmp = tmp.substring(0, 2) + "/" + tmp.substring(2);
                }
                if (!StringUtil.isNull(tmp)) {
                    DATE = "2023/" + tmp.trim();
                }
                String save = DateUtil.getNextWorkDate(DATE);
                absPath = AbsStragety.resDir + "res/bottom/" + save + ".txt";
            }
        }
        try {
            GetAllBankuaiCode.read();
            GetAllBankuaiCode.readSelect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LineContext context = new LineContext();
        Stragety.isResult = true;
        if (kn == 1) {
            String apath = AbsStragety.BOTTOM_PATH + retx + ".xlsx";
            Table table = ExcelWrite2007Test.read(apath);
            table.initIndex();
            int rows = table.rows.size();
            boolean filterFlag = false;
            for (int i = 1; i < rows; i++) {
                String acode = table.rows.get(i).getCol(0).data;
                if (StringUtil.isNull(acode)) {
                    continue;
                }
                String adate = table.rows.get(i).getCol(2).data;
                String test = table.rows.get(i).getCol(5).data;
                if (StringUtil.isNull(adate)) {
                    continue;
                }
                if (StringUtil.eq(test.trim(), "1.0")) {
                    filterFlag = true;
                }
            }

            for (int i = 1; i < rows; i++) {
                String acode = table.rows.get(i).getCol(0).data;
                if (StringUtil.isNull(acode)) {
                    continue;
                }
                acode = acode.substring(0, acode.indexOf(" "));
                String adate = table.rows.get(i).getCol(2).data;
                String test = table.rows.get(i).getCol(5).data;
                if (StringUtil.isNull(adate)) {
                    continue;
                }
                if (filterFlag && !StringUtil.eq(test.trim(), "1.0")) {
                    continue;
                }
                Log.log(acode + " " + adate);

                KModel kModel = new KModel();
                kModel.setRow(table.rows.get(i));
                context.setkModel(kModel);
                context.setUseMinute(1);
                context.setUseMinuteLen(5);
                mainProcess(acode, adate, method, 0, 0, context);
            }
        } else if (kn == 3) {
            mainProcess("", "2023/08/10~2023/08/20", method, 0, 0, context);
        } else if (kn == 4) {
            mainProcess("", "", method, 1, 1, context);
        }

        if (!Stragety.isResult) {
            mainResult();
        }

        if (kn == 1 || kn == 5) {
            absPath = AbsStragety.BOTTOM_PATH + retx + "_.xlsx";
        } else {
            String save = DateUtil.getNextWorkDate(DATE);
            absPath = AbsStragety.resDir + "res/bottom/" + save.replaceAll("/", "-") + ".xlsx";
        }
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


