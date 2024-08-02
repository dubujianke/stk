package com.alading.tool.stock;

import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.model.KModel;
import com.alading.util.ExcelWrite2007Test;
import com.alading.util.StringUtil;
import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * minute time stragety
 * int kn = 1;//validate xls
 * int kn1sub = 0;
 */
public class StragetyZTBottomStep0 extends StragetyZTBottom {
//    public static String stragety = "IsZhangting";
public static String stragety = "IsZhangtingHorUp";
    public static void main(String[] args) throws InterruptedException, IOException {
        init();
        step = 0;
        List<String> dirs = getDirs("");
        for (String absPath : dirs) {
            if (absPath.endsWith("_2")) {
                continue;
            }
            BOTTOM_PATH = absPath + "\\";
            ExcelWrite2007Test.PATH = absPath + "_2" + "\\";
//            FileManager.mkdirs(ExcelWrite2007Test.PATH);
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
                StragetyZTBottomStep0.clear();
                GlobalContext.clear();
                IsZhangting.initTable();
                AbsStragety.isMonitor = true;
                AbsStragety.isNetProxy = true;
                AbsStragety.isNetLocalProxy = true;
                StockAllMinuteLine.useSingle = false;
                AbsStragety.MONITOR_LEN = -1;
                AbsStragety.minuteDayLen = -1;
                AbsStragety.minuteSingleDate = 4;
                AbsStragety.printMinute = true;
                AbsStragety.IDX_PROXY = -1;
                mainInstance(DATE);
                try {
                    System.out.println("END");
                    IsZhangting.table.sort();
                    ExcelWrite2007Test.main(IsZhangting.table, DATE + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void mainInstance(String adate) throws InterruptedException, IOException {
        int idx = 0;
        FILE = "D:\\new_tdx\\T0001\\export\\";
        CTROL_LEN = 1;
        AbsStragety.CURDAY_OPEN = true;
        StragetyZTBottomStep0.clear();
        List<String> dates = new ArrayList<>();
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        DATE = adate;
        if (!AbsStragety.isMonitor) {
            StragetyZTBottomStep0 stragetyZTBottom = new StragetyZTBottomStep0();
            stragetyZTBottom.mainPrs();
        } else {
            while (true) {
                if (timeSkip) {
                    long time = DateUtil.getCurMinute(new Date());
                    long amStartTime = DateUtil.getCurMinute(DateUtil.getStartTime());
                    long amEndTime = DateUtil.getCurMinute(DateUtil.getAMEndTime());
                    if (time > amEndTime) {
                        IsZhangting.table.initIndex();
                        break;
                    }
                    if (time < amStartTime) {
                        StringUtil.sleep(1000);
                        continue;
                    }
                }
                idx++;
                StragetyZTBottomStep0 stragetyZTBottom = new StragetyZTBottomStep0();
                stragetyZTBottom.mainPrs();
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

    public void mainPrs() {
        LineContext context = new LineContext();
        context.setUseMinute(1);
        String dateMonitor = DATE;
        Table table = ExcelWrite2007Test.read(AbsStragety.BOTTOM_PATH + dateMonitor + ".xlsx");
        table.initIndex();
        int rows = table.rows.size();
        boolean filterFlag = false;
        PrsLog.total = rows;
        boolean containFilter = false;
        for (int i = 1; i < rows; i++) {
            Row row = table.rows.get(i);
            String isTest = row.getCol(5).data.trim();
            if(isTest.equals("1") || isTest.equals("1.0")) {
                containFilter = true;
            }
        }
        for (int i = 1; i < rows; i++) {
            if (i == 8) {
                int a = 0;
            }
            Row row = table.rows.get(i);
            String test = row.getCol(5).data;
            if(containFilter && !test.equals("1.0")) {
                continue;
            }
            String acode = row.getCol(0).data;
            if (StringUtil.isNull(acode)) {
                continue;
            }
            acode = acode.substring(0, acode.indexOf(" "));
            String adate = table.rows.get(i).getCol(2 + 1).data;
            if (StringUtil.isNull(adate)) {
                continue;
            }

            KModel kModel = new KModel();
            kModel.setRow(row);
            context.setkModel(kModel);
            context.table = table;
            mainProcess(acode + ".txt", adate, stragety, 1, 1, context);
        }

        mainResult();
//        Log.log("TOTAL_OK:" + KN.CNT);
    }


}
