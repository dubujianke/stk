package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.mk.data.GetAllBankuaiCode;
import com.mk.data.GetBankuai;
import com.mk.data.eastmoney.GetGuben;
import com.mk.model.*;
import com.mk.report.LineReport;
import com.mk.util.StringUtil;

import java.util.List;

/**
 * 大底
 */
public class IsBottomDeep extends Stragety {


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


    public static void ok(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
        boolean flag = true;
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

    public boolean filter1030(String file, Kline kline0, LineContext context) {
        if (!kline0.isZT(30)) {
            return false;
        }
        //8 天内有zf大于8个点
        double prevDZ = kline0.getPrevDZ(8);
        if (prevDZ < 8) {
            return false;
        }

        boolean flag1030 = kline0.isBetween1030();
        if (!flag1030) {
            return false;
        }
        double space30 = kline0.getAssumeSpace30();
        if (space30 >= -9.5) {
            return false;
        }
        return true;
    }

    public boolean filter3060(String file, Kline kline0, LineContext context) {
        if (!kline0.isZT(10)) {
            return false;
        }
        //8 天内有zf大于8个点
        double prevDZ = kline0.getPrevDZ(8);
        if (prevDZ < 8) {
            return false;
        }

        boolean flag = kline0.isBetween3060();
        if (!flag) {
            return false;
        }


        double space60 = kline0.getAssumeSpace60();
        if (space60 < 0 && Math.abs(space60) <= 6.2) {
            return false;
        }

        //站在30线上
        double space30 = kline0.getAssumeSpace30();
        if (space30 > 0 && Math.abs(space30) < 2) {
            return true;
        }

        boolean isGold = kline0.isGoldOrComingGold(10, 30);

        //顶到60线
        double zt = kline0.getZTSelf();
        double ztma = kline0.getNextSupposeMA60(kline0.getClose() * 1.1f);
        if (zt < ztma) {
            double gap = KLineUtil.compareMaxSign(ztma, zt);
            if (gap < 3.3 && isGold) {
                return true;
            }
        } else if (zt > ztma) {
            double gap = KLineUtil.compareMaxSign(zt, ztma);
            if (gap < 3.1 && isGold) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public boolean filter60120(String file, Kline kline0, LineContext context) {
        if (!kline0.isZT2(10)) {
            return false;
        }

        //8 天内有zf大于8个点
        double prevDZ = kline0.getPrevDZ(8);
        if (prevDZ < 7) {
            return false;
        }


        boolean flag = kline0.isBetween60120();
        if (!flag) {
            return false;
        }

        double space120 = kline0.getAssumeSpace120();
        if (space120 < 0 && Math.abs(space120) <= 6.2) {
            return false;
        }

        //站在60线上
        double space = kline0.getAssumeSpace60();
        if (space > 0 && Math.abs(space) < 2) {
            return true;
        }

        boolean isGold = kline0.isGoldOrComingGold(10, 120);

        //顶到120线
        double zt = kline0.getZTSelf();
        double ztma = kline0.getNextSupposeMA120(kline0.getClose() * 1.1f);
        if (zt < ztma) {
            double gap = KLineUtil.compareMaxSign(ztma, zt);
            if (gap < 3.3 && isGold) {
                return true;
            }
        } else if (zt > ztma) {
            double gap = KLineUtil.compareMaxSign(zt, ztma);
            if (gap < 3.1 && isGold) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }


    public void prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
        if (file.contains("000957") && date.equalsIgnoreCase("2024/02/20")) {
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


        //IMPORTANT 1
//        double v = GetGuben.retriveOrGetShizhi(AbsStragety.getCode(file), kline0.getClose());
//        if (v >= 120 && importantShizhiFlag) {
//            error(file, date, "guben < 210");
//            return;
//        }

        String bankuai = GetBankuai.get(getCode(file));
        boolean flag = GetAllBankuaiCode.containBankuai(bankuai);
        if (!flag) {
            return;
        }



//        boolean flag1 = filter1030(file, kline0, context);
//        if (!flag1) {
//            return;
//        }
//
//        boolean flag2 = filter3060(file, kline0, context);
//        if (!flag2) {
//            return;
//        }

        boolean flag2 = filter60120(file, kline0, context);
        if (!flag2) {
            return;
        }



        List<ScoreConcept> retConcept = ConceptDFCF.getList(AbsStragety.getCode(file));
        if (retConcept.size() == 0) {
            return;
        }
        Kline minLine = kline0.getMin(30);
        double minSpace250 = minLine.getSpace250();
        if (minSpace250 > -40) {
            return;
        }


        int aweekIdx = kline0.getWeekDay();
        double dadie = kline0.getAlreadyDF(30, 250);
        int dadieOffset = kline0.getAlreadyDFIdx(30, 250);
        double dadieRecent10 = kline0.getAlreadyDFFromMinLine(30, 30);
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
