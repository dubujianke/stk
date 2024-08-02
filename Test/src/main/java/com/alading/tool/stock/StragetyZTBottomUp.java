package com.alading.tool.stock;

import com.alading.MusicSearchActivity;
import com.alading.model.GlobalData;
import com.alading.model.Row;
import com.alading.model.Stock;
import com.alading.model.Table;
import com.alading.tool.stock.model.KModel;
import com.alading.util.ExcelWrite2007Test;
import com.alading.util.StringUtil;
import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * minute time stragety
 */
public class StragetyZTBottomUp extends StragetyZTBottom {
    public static String stragety = "IsZhangtingHorUp";
    public static boolean useGlobalDataflag = true;
    public static void main(String[] args) throws Exception {
        init();
        step = 0;
        DATE = "2024-04-15";
        ExcelWrite2007Test.PATH = absPath + "\\";
        FileManager.mkdirs(ExcelWrite2007Test.PATH);
        StragetyZTBottomUp.clear();
        GlobalContext.clear();
        IsZhangting.initTable();
        AbsStragety.isMonitor = true;
        AbsStragety.isNetProxy = false;
        AbsStragety.isNetLocalProxy = false;
        StockAllMinuteLine.useSingle = false;
        AbsStragety.MONITOR_LEN = -1;
        AbsStragety.minuteDayLen = -1;
        AbsStragety.minuteSingleDate = 4;
        AbsStragety.printMinute = true;
        AbsStragety.IDX_PROXY = -1;
        StragetyZTBottom.isTDX = false;
        useGlobalDataflag = true;

        //实际监控
        step = 1;
        AbsStragety.IDX_PROXY = 0;
        timeSkip = true;
        MONITOR_DATE = DATE.replaceAll("-", "/");
        mainInstance(DATE);
        try {
            System.out.println("END");
            IsZhangting.table.sort();
            ExcelWrite2007Test.main(IsZhangting.table, DATE + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mainInstance(String adate) throws Exception {
        int idx = 0;
        FILE = "D:\\new_tdx\\T0001\\export\\";
        CTROL_LEN = 1;
        AbsStragety.CURDAY_OPEN = true;
        StragetyZTBottomUp.clear();
        List<String> dates = new ArrayList<>();
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        DATE = adate;

        Table table = ExcelWrite2007Test.read(AbsStragety.BOTTOM_PATH + DATE + ".xlsx");
        table.initIndex();
        List<String> list = new ArrayList<>();
        for (int i = 1; i < table.rows.size(); i++) {
            Row row = table.rows.get(i);
            String acode = row.getCol(0).data;
            list.add(acode);
            table.rowMap.put(acode.substring(0, 6), row);
        }
        MusicSearchActivity.list = list;
        MusicSearchActivity musicSearchActivity = new MusicSearchActivity();
        musicSearchActivity.startTime();

        while (true) {
            if (timeSkip) {
                long time = DateUtil.getCurMinute(new Date());
                long amStartTime = DateUtil.getCurMinute(DateUtil.getStartTime());
                long amEndTime = DateUtil.getCurMinute(DateUtil.getAMEndTime());
                long pmStartTime = DateUtil.getCurMinute(DateUtil.getPMStartTime());
                long pmEndTime = DateUtil.getCurMinute(DateUtil.getPMEndTime());
                if (time < amStartTime) {
                    StringUtil.sleep(1000);
                    continue;
                }
                if (time > amEndTime && time < pmStartTime) {
                    StringUtil.sleep(1000);
                    continue;
                }
                if (time > pmEndTime) {
                    IsZhangting.table.initIndex();
                    break;
                }
            }
            idx++;
            StragetyZTBottomUp stragetyZTBottom = new StragetyZTBottomUp();
            stragetyZTBottom.mainPrs(table);
            if (AbsStragety.isNetProxy) {
            } else {
                Thread.currentThread().sleep(10 * 1000);
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

    public void mainPrs(Table table) throws Exception {
        LineContext context = new LineContext();
        context.setUseMinute(1);
        int rows = table.rows.size();
        boolean filterFlag = false;
        PrsLog.total = rows;
        List<Stock> ret = GlobalData.getRet();
        List<Row> rets = new ArrayList<>();
        if (useGlobalDataflag) {
            for (Stock stock : ret) {
                Row row = table.rowMap.get(stock.getCode().replace("sh", "").replace("sz", ""));
                rets.add(row);
            }
        } else {
            rets = table.rows;
        }
        for (int i = 1; i < rets.size(); i++) {
            Row row = rets.get(i);
            String acode = row.getCol(0).data;
            if (StringUtil.isNull(acode)) {
                continue;
            }
            acode = acode.substring(0, acode.indexOf(" "));
            String adate = row.getCol(2 + 1).data;
            String test = row.getCol(5).data;
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
            mainProcess(acode + ".txt", adate, stragety, 1, 1, context);
        }

//        absPath = AbsStragety.BOTTOM_PATH + DATE + "__.xlsx";
//        IsZhangting.table.sort();
//        ExcelWrite2007Test.mainABS(IsZhangting.table, absPath);
        StragetyZTBottom.clear();
        GlobalContext.clear();

    }


}
