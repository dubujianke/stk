package com.mk.tool.stock;

import java.io.*;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.data.GetAllBankuaiCode;
import com.mk.model.Col;
import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.filter.GetNextZT;
import com.mk.tool.stock.model.KModel;
import com.mk.util.ExcelWrite2007Test;
import com.mk.util.Log;
import com.mk.util.StringUtil;

import java.io.IOException;
import java.util.*;

/**
 * minute time stragety
 */
public class StragetyZTBottom extends AbsStragety {

    public static String monitorMinute = "1051";
    public static float MIN_3MINUTES_SMALLHAND_ZF = 1.21f;
    public static float MIN_3MINUTES_LARGEHAND_ZF = 1.21f;
    public static float MIN_3MINUTES_ZF = 1.21f;
    public static float MIN_3MINUTES_ZF2 = 1.0f;
    public static float MIN_ZF2 = 1.4f;
    public static boolean isTDX = true;
    public static String absPath = "";

    public static String YEAR = "2024";

    public static List<String> getDates(String dir) {
        List<String> dates = new ArrayList<>();
        File file = new File(dir);
        File[] fs = file.listFiles();
        for (File fle : fs) {
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
        retLines.clear();
        before_num = 0;
        resultMap.clear();
    }

    public static void mainResult() {
        Stragety.isResult = true;
        LineContext context = new LineContext();
        context.setUseMinute(1);
        for (Result result : retLines) {
            mainProcess(result.code, result.date, result.stragety, 1, 1, context);
        }
    }


    public static float maxzf = 2.0f;
    public static float pulsezf = 1.8f;
    public static int step = 1;
    public static boolean useTree = true;//last step
    public static String MONITOR_DATE = "";

    public static void main(String[] args) throws InterruptedException, IOException {
        int kn = 1;//validate xls
        int kn1sub = 0;
        if (step == 0) {
            kn = 1;//validate xls
            kn1sub = 0;
        } else {
            kn = 9;
            DATE = "2024-02-08";
            MONITOR_DATE = DATE.replaceAll("-", "/");
            AbsStragety.isNetProxy = true;
            AbsStragety.BOTTOM_PATH = String.format("D:\\stock\\Test\\res\\bottom\\ret_%s_2\\", DATE+"");
            absPath = "D:\\stock\\Test\\res\\bottom\\ret_" + DATE + "_2\\" + DATE + "__.xlsx";
        }
        if (kn == 1) {
            List<String> dirs = getDirs("");
            for (String absPath : dirs) {
                if (absPath.endsWith("_2")) {
                    continue;
                }
                BOTTOM_PATH = absPath + "\\";
                ExcelWrite2007Test.PATH = absPath + "_2" + "\\";
                FileManager.mkdirs(ExcelWrite2007Test.PATH);
                List<String> dates = getDates(BOTTOM_PATH);
                for (int i = 0; i < dates.size(); i++) {
                    if (i < dates.size() - 1) {
                        continue;
                    }
                    String date = dates.get(i);
                    DATE = date;//dates.get(0);
                    File file = new File(ExcelWrite2007Test.PATH + DATE + ".xlsx");
                    if (file.exists()) {
                        continue;
                    }
                    StragetyZTBottom.clear();
                    GlobalContext.clear();
                    IsZhangting.initTable();
                    AbsStragety.isMonitor = true;
                    AbsStragety.isNetProxy = true;
                    AbsStragety.isNetLocalProxy = true;
                    mainInstance(DATE, kn, kn1sub);
                    try {
                        System.out.println("END");
                        IsZhangting.table.sort();
                        ExcelWrite2007Test.main(IsZhangting.table, DATE + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            StragetyZTBottom.clear();
            GlobalContext.clear();
            IsZhangting.initTable();
            AbsStragety.isMonitor = true;
            AbsStragety.isNetProxy = true;
            AbsStragety.isNetLocalProxy = true;
            mainInstance(DATE, kn, kn1sub);
            try {
                System.out.println("END");
                if (kn == 10) {
                    absPath = AbsStragety.BOTTOM_PATH + DATE + "__.xlsx";
                    IsZhangting.table.sort();
                    ExcelWrite2007Test.mainABS(IsZhangting.table, absPath);

                    StragetyZTBottom.clear();
                    GlobalContext.clear();
                } else {
                    IsZhangting.table.sort();
                    ExcelWrite2007Test.mainABS(IsZhangting.table, absPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void mainPrs(int from, int to, int kn, int kn1sub) {
        StockAllMinuteLine.useSingle = true;
        if (kn == 1) {
            StockAllMinuteLine.useSingle = false;
            AbsStragety.MONITOR_LEN = -1;
            AbsStragety.minuteDayLen = -1;
            AbsStragety.minuteSingleDate = 4;
            AbsStragety.printMinute = true;
            AbsStragety.IDX_PROXY = -1;
            LineContext context = new LineContext();
            context.setUseMinute(0);
            context.setUseMinute(1);

            String dateMonitor = DATE;
            Table table = ExcelWrite2007Test.read(AbsStragety.BOTTOM_PATH + dateMonitor + ".xlsx");
            table.initIndex();
            int rows = table.rows.size();
            boolean filterFlag = false;
            PrsLog.total = rows;
            for (int i = 1; i < rows; i++) {
                if (i == 8) {
                    int a = 0;
                }
                Row row = table.rows.get(i);
                String acode = row.getCol(0).data;
                if (StringUtil.isNull(acode)) {
                    continue;
                }
                acode = acode.substring(0, acode.indexOf(" "));
                String adate = table.rows.get(i).getCol(2 + 1).data;
                String test = table.rows.get(i).getCol(5).data;
                if (StringUtil.isNull(adate)) {
                    continue;
                }
                if (filterFlag && !StringUtil.eq(test.trim(), "1.0")) {
                    continue;
                }

                KModel kModel = new KModel();
                kModel.setRow(row);
                context.setkModel(kModel);
                context.table = table;
//                if(!acode.contains("603506")) {
//                    return;
//                }
                mainProcess(acode + ".txt", adate, "IsZhangting", 1, 1, context);
            }

        } else if (kn == 9) {
            AbsStragety.IDX_PROXY = -1;
            AbsStragety.isMonitor = true;
            AbsStragety.isNetProxy = false;
            AbsStragety.isNetLocalProxy = false;
            AbsStragety.printMinute = true;
            AbsStragety.minuteDayLen = 1;
            useMinute = true;
            AbsStragety.use000 = true;
            AbsStragety.use600 = true;
            String dateMonitor = DATE;//"2023-12-04";
            Table table = ExcelWrite2007Test.read(AbsStragety.BOTTOM_PATH + dateMonitor + ".xlsx");
            table.initIndex();
            int rows = table.rows.size();
            boolean filterFlag = false;
            PrsLog.total = rows;
            for (int i = 1; i < rows; i++) {
                Row row = table.rows.get(i);
                String acode = row.getCol(0).data;
                if (StringUtil.isNull(acode)) {
                    continue;
                }
                acode = acode.substring(0, acode.indexOf(" "));
                String adate = table.rows.get(i).getCol(2 + 1).data;
                String test = table.rows.get(i).getCol(5).data;
                if (StringUtil.isNull(adate)) {
                    continue;
                }
                if (filterFlag && !StringUtil.eq(test.trim(), "1.0")) {
                    continue;
                }

                LineContext context = new LineContext();
                context.setUseMinute(0);
                context.setUseMinute(1);
                KModel kModel = new KModel();
                kModel.setRow(row);
                context.setkModel(kModel);
                mainProcess(acode + ".txt", adate, "IsZhangting", 1, 1, context);
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                }
            }
        } else if (kn == 10) {
            StockAllMinuteLine.useSingle = true;
            AbsStragety.IDX_PROXY = -1;
            AbsStragety.MONITOR_LEN = -1;
            AbsStragety.isMonitor = true;
            AbsStragety.isNetProxy = true;
            AbsStragety.isNetLocalProxy = true;
            AbsStragety.printMinute = false;
            useMinute = true;
            AbsStragety.use000 = true;
            AbsStragety.use600 = true;
            String dateMonitor = DATE;

            Table table = ExcelWrite2007Test.read(AbsStragety.BOTTOM_PATH + dateMonitor + ".xlsx");
            table.initIndex();
            int rows = table.rows.size();
            boolean filterFlag = false;
            PrsLog.total = rows;
            for (int i = 1; i < rows; i++) {
                Row row = table.rows.get(i);
                String acode = row.getCol(0).data;
                if (StringUtil.isNull(acode)) {
                    continue;
                }
                acode = acode.substring(0, acode.indexOf(" "));
                String adate = table.rows.get(i).getCol(2 + 1).data;
                String test = table.rows.get(i).getCol(5).data;
                if (StringUtil.isNull(adate)) {
                    continue;
                }
                if (filterFlag && !StringUtil.eq(test.trim(), "1.0")) {
                    continue;
                }

                LineContext context = new LineContext();
                context.setUseMinute(0);
                context.setUseMinute(1);
                KModel kModel = new KModel();
                kModel.setRow(row);
                context.setkModel(kModel);

                mainProcess(acode + ".txt", adate, "IsZhangting", 1, 1, context);
            }
        }
        mainResult();
//        Log.log("TOTAL_OK:" + KN.CNT);
    }


    public static void mainInstance(String adate, int kn, int kn1sub) throws InterruptedException, IOException {
        try {
            AbsStragety.printMinute = false;
            AbsStragety.MONITOR_LEN = 50;
            useMinute = true;
            AbsStragety.use000 = true;
            AbsStragety.use600 = true;
            GetAllBankuaiCode.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int idx = 0;

        FILE = "D:\\new_tdx\\T0001\\export\\";
        CTROL_LEN = 1;
        AbsStragety.CURDAY_OPEN = true;
        StragetyZTBottom.clear();
        List<String> dates = new ArrayList<>();
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        DATE = adate;
        if (!AbsStragety.isMonitor) {
            StragetyZTBottom stragetyZTBottom = new StragetyZTBottom();
            stragetyZTBottom.mainPrs(0, -1, kn, kn1sub);
        } else {
            while (true) {
                long time = DateUtil.getCurMinute(new Date());
                long amStartTime = DateUtil.getCurMinute(DateUtil.getStartTime());
                long amEndTime = DateUtil.getCurMinute(DateUtil.getAMEndTime());
                if(time>amEndTime) {
                    IsZhangting.table.initIndex();
                    break;
                }
                if(time<amStartTime) {
                    StringUtil.sleep(1000);
                    continue;
                }
                idx++;
                StragetyZTBottom stragetyZTBottom = new StragetyZTBottom();
                stragetyZTBottom.mainPrs(0, -1, kn, kn1sub);
                if (AbsStragety.isNetProxy) {
                } else {
                    Thread.currentThread().sleep(30 * 1000);
                }
                if (idx == 240 || AbsStragety.IDX_PROXY == -1) {
                    try {
                        IsZhangting.table.initIndex();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    //////////////////////////////////////////////////////////////////
    public static Row headerRow = TableConst.getHeader();


    public static void parseAttr(String str, KModel kModel) {
        String[] vs = str.split(":");
        String name = vs[0].trim();
        String value = vs[1].trim();
        if (name.equalsIgnoreCase("guben")) {
            kModel.setGuben(Float.parseFloat(value));
        }
        if (name.equalsIgnoreCase("space250")) {
            kModel.setSpace250(Float.parseFloat(value));
        }
        if (name.equalsIgnoreCase("chgHands")) {
            String[] cvs = value.replace("[", "").replace("]", "").split(",");
            for (String cv : cvs) {
                kModel.getChgHands().add(Float.parseFloat(cv));
            }
        }
        if (name.equalsIgnoreCase("prevmon1")) {
            kModel.setPrevmon1(Float.parseFloat(value));
        }
        if (name.equalsIgnoreCase("prevmon2")) {
            kModel.setPrevmon2(Float.parseFloat(value));
        }
        if (name.equalsIgnoreCase("monTotal")) {
        }
        if (name.equalsIgnoreCase("isCross")) {
            kModel.setCross(Boolean.parseBoolean(value));
        }
        if (name.equalsIgnoreCase("upMAs")) {

        }
    }

    public static KModel assembleMap(String items[]) {
        KModel model = new KModel();
        String code = items[0].trim();
        String type = items[2].trim();
        String cdate = items[3].trim();
        String ndate = items[4].trim();
        model.setCode(code);
        model.setStragety(type);
        model.setDate(cdate);
        model.setNextDate(ndate);
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            if (item.contains(":")) {
                int idx = GetNextZT.nextMaohao(items, i);
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = i; j < idx; j++) {
                    stringBuffer.append(" " + items[j]);
                }
                parseAttr(stringBuffer.toString(), model);
                i = idx;
                i--;
            }
        }
        return model;
    }


}
