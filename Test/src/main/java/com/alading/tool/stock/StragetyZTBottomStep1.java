package com.alading.tool.stock;

import com.alading.data.GetAllBankuaiCode;
import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.filter.GetNextZT;
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
 */
public class StragetyZTBottomStep1 extends StragetyZTBottom {

    public static void main(String[] args) throws InterruptedException, IOException {
        init();
        int kn = 9;//validate xls
        DATE = "2024-02-20";
        MONITOR_DATE = DATE.replaceAll("-", "/");
        AbsStragety.isNetProxy = true;
        AbsStragety.BOTTOM_PATH = String.format("D:\\stock\\Test\\res\\bottom\\ret_%s_2\\", DATE + "");
        absPath = "D:\\stock\\Test\\res\\bottom\\ret_" + DATE + "_2\\" + DATE + "__.xlsx";

        StragetyZTBottomStep1.clear();
        GlobalContext.clear();
        IsZhangting.initTable();
        AbsStragety.isMonitor = true;
        AbsStragety.isNetProxy = true;
        AbsStragety.isNetLocalProxy = true;
        mainInstance(DATE);
        try {
            System.out.println("END");
            if (kn == 10) {
                absPath = AbsStragety.BOTTOM_PATH + DATE + "__.xlsx";
                IsZhangting.table.sort();
                ExcelWrite2007Test.mainABS(IsZhangting.table, absPath);

                StragetyZTBottomStep1.clear();
                GlobalContext.clear();
            } else {
                IsZhangting.table.sort();
                ExcelWrite2007Test.mainABS(IsZhangting.table, absPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void mainInstance(String adate) throws InterruptedException, IOException {
        int idx = 0;

        FILE = "D:\\new_tdx\\T0001\\export\\";
        CTROL_LEN = 1;
        AbsStragety.CURDAY_OPEN = true;
        StragetyZTBottomStep1.clear();
        List<String> dates = new ArrayList<>();
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        DATE = adate;
        if (!AbsStragety.isMonitor) {
            StragetyZTBottomStep1 stragetyZTBottom = new StragetyZTBottomStep1();
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
                StragetyZTBottomStep1 stragetyZTBottom = new StragetyZTBottomStep1();
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
        StockAllMinuteLine.useSingle = true;
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
        mainResult();
//        Log.log("TOTAL_OK:" + KN.CNT);
    }


}
