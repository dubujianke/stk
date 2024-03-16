package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.mk.data.GetAllBankuaiCode;
import com.mk.data.GetBankuai;
import com.mk.model.ConceptDFCF;
import com.mk.model.Row;
import com.mk.model.ScoreConcept;
import com.mk.model.Table;
import com.mk.report.LineReport;
import com.mk.tool.stock.tool.SelectExcel;
import com.mk.util.StringUtil;

import java.util.List;

/**
 * 大底
 */
public class IsBottomHor extends Stragety {


    public static String getINFO(String INFO) {
        int idx = INFO.indexOf(" ");
        String str1 = INFO.substring(0, idx);
        String str2 = INFO.substring(idx);
        str2 = str2.replaceAll(" ", "").trim();
        if (str2.length() == 3) {
            str2 = "  " + str2;
        }
        return str1 + " " + str2;
    }

    public static Table getTable(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, LineContext context) {
        Row row = IsZhangting.getRow(file, INFO, date, kline0, minuteLine, context);
        Table table = new Table();
        table.add(StragetyZTBottom.headerRow);
        table.add(row);
        return table;
    }


    public static boolean filterRow(String file, String INFO, String date, Kline kline0, MinuteLine minuteLine, String msg, LineContext context) {
        boolean flag1 = kline0.hasPrevZT(20);
        boolean flag2 = kline0.hasPrevZT2(20);
        if (flag1 || flag2) {
            return false;
        }

        //for safe
        if (kline0.getZhangfu() < -3.0) {
            return false;
        }

        Table table = getTable(file, INFO, date, kline0, minuteLine, context);
        table.initIndex();
        boolean flag = SelectExcel.isRealOK(table,  table.rows.get(1));
        context.setType05(1);
        return flag;
    }

    public static void ok(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
        boolean flag = filterRow(file, INFO, date, kline0, null, msg, context);
        if (flag) {
            okReal(IsBottom.table, file, INFO, date, kline0, weekline, nextN, msg, context);
        }
    }

    public static void okReal(Table table, String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
        String dateCur = date;
        date = DateUtil.getNextWorkDate(date);
        double zt = kline0.getClose() * 1.1f;
        try {
            Row row = IsZhangting.getRow(file, INFO, date, kline0, null, context);
            Row rowRaw = null;
            if (context.getkModel() != null && context.getkModel().getRow() != null) {
                rowRaw = context.getkModel().getRow();
                row.setCol(rowRaw.getTable().getColumn("涨幅"), "" + rowRaw.getFloat("涨幅"));
                row.setCol(rowRaw.getTable().getColumn("分钟"), "" + rowRaw.getStr("分钟"));
            }

            try {
                LineReport lineReport = IsBottom.report(file, nextN, context, context.isRealBottomFlag());
                //加速天序号	加速天量	减速天序号	减速天量
                Object[] objsU = lineReport.isFirstSpeedUpWithVOLInDays(context);
                Object[] objsD = lineReport.isFirstSpeedDownWithVOLInDays(context);

                List<Integer> list = lineReport.getSeries(0);
                row.setCol(rowRaw.getTable().getColumn("v900(0)"), "" + list.get(0));
                row.setCol(rowRaw.getTable().getColumn("v900(1)"), "" + list.get(1));
                row.setCol(rowRaw.getTable().getColumn("v900(2)"), "" + list.get(2));
                row.setCol(rowRaw.getTable().getColumn("v900(3)"), "" + list.get(3));
                row.setCol(rowRaw.getTable().getColumn("v900(4)"), "" + list.get(4));

                row.setCol(rowRaw.getTable().getColumn("加速天序号"), "" + objsU[0]);
                row.setCol(rowRaw.getTable().getColumn("加速天量"), "" + objsU[1]);
                row.setCol(rowRaw.getTable().getColumn("减速天序号"), "" + objsD[0]);
                row.setCol(rowRaw.getTable().getColumn("减速天量"), "" + objsD[1]);
            } catch (Exception e) {
//                e.printStackTrace();
            }
            try {
                boolean bottomUp = IsBottom.reportBottomUp(file, nextN, context, context.isRealBottomFlag());
                int a = 0;
                row.setCol(rowRaw.getTable().getColumn("bottomup"), "" + StringUtil.getInt(bottomUp));
            } catch (Exception e) {
//                e.printStackTrace();
            }


            table.add(row);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void error(String file, String date, String msg) {
        int a = 0;
        Log.log(file + "  " + date + " " + msg);
    }


    public void prs(String file, List<Kline> days, String date, String stragetyName, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
        try {
            prsIsN(file, days, date, weeks, moths, usemonth, useweek, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean importantShizhiFlag = true;



    public void prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
        if (file.contains("000008") && date.equalsIgnoreCase("2024/03/06")) {
            int a = 0;
        }
        if (file.contains("000506")) {
            int a = 0;
        }
        if (INFO.contains("ST")) {
            return;
        }
        int idx = getIdx(days, date);
        if (idx < 141) {
            return;
        }

        ////////////////////////////////////////////////////////////////////////
        Kline kline0 = days.get(idx);


//        String bankuai = GetBankuai.get(getCode(file));
//        boolean flag = GetAllBankuaiCode.containBankuai(bankuai);
//        if (!flag) {
//            return;
//        }


//        List<ScoreConcept> retConcept = ConceptDFCF.getList(AbsStragety.getCode(file));
//        if (retConcept.size() == 0) {
//            return;
//        }
        Kline minLine = kline0.getMin(30);


        addOK(file, days, date, weeks, moths, usemonth, useweek, context, kline0);
    }

    public static void filter(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, Kline kline, LineContext context) {
        StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
        Kline lineMin = kline.getMin(60);
        double frac = KLineUtil.compareMax(lineMin.getClose(), kline.getClose());
        boolean realBottomFlag = false;
        if (frac < 3) {
            realBottomFlag = true;
        }
        ok(file, INFO, date, kline, null, kline, context.getMsg(), context);
    }

    private boolean addOK(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context, Kline kline0) {

        if (file.contains("600322") && date.equalsIgnoreCase("2023/07/20")) {
            int a = 0;
        }
        filter(file, days, date, weeks, moths, usemonth, useweek, kline0, context);
        String key = AbsStragety.getCode(file) + " " + date;
        GlobalContext.map.put(key, null);
        return true;

    }

    static String getString(String nane, Object v) {
        return " " + nane + " " + v;
    }


}
