package com.alading.tool.stock.tool;

import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.*;
import com.alading.util.ExcelWrite2007Test;

import java.io.IOException;


public class SelectExcel {
    public static StringBuffer resultBuffer = new StringBuffer();


    public static void print(double[] vs, boolean abs) {
        for (double v : vs) {
            //System.out.println(v);
        }
    }

    public static void filter(double[] vs, double[] vs2, boolean flag, boolean flag2) {
        for (int i = 0; i < vs.length; i++) {
            if (flag && vs[i] > 0 && flag2 && vs2[i] > 0) {
                //System.out.println("" + vs[i] + " " + vs2[i]);
            }
            if (flag && vs[i] > 0 && !flag2 && vs2[i] < 0) {
                //System.out.println("" + vs[i] + " " + vs2[i]);
            }
            if (!flag && vs[i] < 0 && flag2 && vs2[i] > 0) {
                //System.out.println("" + vs[i] + " " + vs2[i]);
            }
            if (!flag && vs[i] < 0 && !flag2 && vs2[i] < 0) {
                //System.out.println("" + vs[i] + " " + vs2[i]);
            }
        }
    }

    public static double min(double[] vs, boolean abs) {
        double min = Float.MAX_VALUE;
        for (double v : vs) {
            if (abs) {
                v = Math.abs(v);
            }
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public static double count(double[] vs) {
        return vs.length;
    }

    public static int countFlag(double[] vs, boolean flag) {
        int num = 0;
        for (double v : vs) {
            if (flag && v > 0) {
                num++;
            }
            if (!flag && v < 0) {
                num++;
            }
        }
        return num;
    }

    public static double minFlag(double[] vs, boolean flag, boolean abs) {
        double min = Float.MAX_VALUE;
        for (double v : vs) {
            if (flag && v >= 0) {
                if (abs) {
                    v = Math.abs(v);
                }
                if (v < min) {
                    min = v;
                }
            }
            if (!flag && v <= 0) {
                if (abs) {
                    v = Math.abs(v);
                }
                if (v < min) {
                    min = v;
                }
            }
        }
        return min;
    }

    public static double maxFlag(double[] vs, boolean flag, boolean abs) {
        double min = Float.MIN_VALUE;
        for (double v : vs) {
            if (flag && v <= 0) {
                if (abs) {
                    v = Math.abs(v);
                }
                if (v > min) {
                    min = v;
                }
            }
            if (!flag && v <= 0) {
                if (abs) {
                    v = Math.abs(v);
                }
                if (v > min) {
                    min = v;
                }
            }
        }
        return min;
    }

    public static double max(double[] vs, boolean abs) {
        double min = Float.MIN_VALUE;
        for (double v : vs) {
            if (abs) {
                v = Math.abs(v);
            }
            if (v > min) {
                min = v;
            }
        }
        return min;
    }

    public static void line() {
        //System.out.println("-------------------------");
    }

    public static void printV(double[] vs) {
//        //System.out.println("min:"+min(vs, false));
//        //System.out.println("max:"+max(vs, false));
//        //System.out.println("amin:"+min(vs, true));
//        //System.out.println("amax:"+max(vs, true));

        //System.out.println("fmin:" + countFlag(vs, false));

    }

    public static boolean isIn(double v, double negmin, double negmax) {
        if (v > negmax && v <= negmin) {
            return true;
        }
        return false;
    }

    public static double getMin(double v1, double v2, double v3, double v4) {
        double min = 999;
        if (v1 < min) {
            min = v1;
        }
        if (v2 < min) {
            min = v2;
        }
        if (v3 < min) {
            min = v3;
        }
        if (v4 < min) {
            min = v4;
        }
        return min;
    }

    public static boolean isRealOK2(Kline kline0, Table table, Row row) {
        //for safe
        if (kline0.getZhangfu() < -2.5) {
            return false;
        }
        if (kline0.getZhangfu() > 5) {
            return false;
        }
        if (kline0.getZhangfu() < 0 || kline0.getZhangfu() > 2.5) {
            return false;
        }

        if (!kline0.isShadownUp(50)) {
            return false;
        }
        double upZf = kline0.getUpZhangfu();
        if (upZf < 4.5) {
            return false;
        }

        boolean flag1 = kline0.hasPrevZT(11);
        boolean flag2 = kline0.hasPrevZT2(11);
        if (flag1 || flag2) {
            return false;
        }

        if (row.getFloat(table.getColumn("gap250")) > 0) {
            return false;
        }
        double gap250 = row.getFloat(table.getColumn("gap250"));
        double gap120 = row.getFloat(table.getColumn("gap120"));
        double gap60 = row.getFloat(table.getColumn("gap60"));
        double gap30 = row.getFloat(table.getColumn("gap30"));
        boolean gapFlag = false;
        if (gap60 > gap120 && gap60 > gap250) {
            gapFlag = true;
        }
        if (!gapFlag) {
            return false;
        }
        boolean flag30 = isIn(gap30, -7.5, -11);
        boolean flag60 = isIn(gap60, -7.2, -14);
        boolean flag120 = isIn(gap120, -8, -14);
        boolean flag250 = isIn(gap250, -9.0, -13);
        boolean matchGap = false;
        if (flag30 || flag60 || flag120 || flag250) {
            matchGap = true;
        }
        if (!matchGap) {
            return false;
        }


        //flag120 下方不允许60
        if (flag250) {
            if (gap120 > 0) {
                if (gap120 > 4) {
                    return false;
                }
            } else if (gap120 < 0) {
                double abs = Math.abs(gap120);
            }
        }

        boolean matchGap2 = false;
        double cur = row.getFloat(table.getColumn("cur"));
        double prev1 = row.getFloat(table.getColumn("prev(1)"));
        double prev2 = row.getFloat(table.getColumn("prev(2)"));
        double prev3 = row.getFloat(table.getColumn("prev(3)"));
        double prev4 = row.getFloat(table.getColumn("prev(4)"));
        if (Math.abs(cur) < 1 || Math.abs(prev1) < 1 || Math.abs(prev2) < 1 || Math.abs(prev3) < 1 || Math.abs(prev4) < 1) {
            matchGap2 = true;
        }
        if (!matchGap2) {
            return false;
        }


        double k250 = row.getFloat(table.getColumn("k250"));
        double k120 = row.getFloat(table.getColumn("k120"));
        double k60 = row.getFloat(table.getColumn("k60"));
        double k30 = row.getFloat(table.getColumn("k30"));
        double min = getMin(k30, k60, k120, k250);
        if (min > -9) {
            return false;
        }


        //cmf
        double cmf70 = row.getFloat(table.getColumn("70%cmf"));
        if (cmf70 > 0.23) {
            return false;
        }

        //fracTrapInProfit < 200 ok
        //fracTrapInProfit > 200 & < 250 priceZf>12 ok
        //fracTrapInProfit > 250 err
        double fracTrapInProfit = row.getFloat(table.getColumn("fracTrapInProfit"));
        double priceZf = row.getFloat(table.getColumn("priceZf"));
        double minLine = row.getFloat(table.getColumn("minLine"));
        if (fracTrapInProfit > 250) {
            return false;
        }
        if (fracTrapInProfit > 200 && priceZf < 12) {
            return false;
        }
        if (minLine > 0.5) {
            return false;
        }

        return true;
    }


    public static boolean isRealOK(Kline kline0, Table table, Row row) {
        MonthKline monthKline = kline0.monthKline;
        MonthKline preMon = monthKline.prev();
        MonthKline preMon2 = monthKline.prev(2);
        double zf = preMon2.getZhangfu();
        boolean flagDown = preMon2.isCrashDownMAI(120);
        Weekline weekline = kline0.weekline;

        boolean dCrossFlag2 = false;
        int dcrossNum2502 = weekline.getDeadCrossNum(10, 250);
        if (dcrossNum2502 > -1 && dcrossNum2502 >= 0 && dcrossNum2502 <= 6) {
            dCrossFlag2 = true;
        }
        if (dCrossFlag2) {
            return false;
        }

        //for safe
        if (kline0.getZhangfu() < -2.5) {
            return false;
        }
        if (kline0.getZhangfu() > 5) {
            return false;
        }
        boolean flag1 = kline0.hasPrevZT(9);
        boolean flag2 = kline0.hasPrevZT2(9);
        if (flag1 || flag2) {
            return false;
        }

        if (row.getFloat(table.getColumn("gap250")) > 0) {
            return false;
        }
        double gap250 = row.getFloat(table.getColumn("gap250"));
        double gap120 = row.getFloat(table.getColumn("gap120"));
        double gap60 = row.getFloat(table.getColumn("gap60"));
        double gap30 = row.getFloat(table.getColumn("gap30"));
        boolean gapFlag = false;
        if (gap60 > gap120 && gap60 > gap250) {
            gapFlag = true;
        }
        if (!gapFlag) {
            return false;
        }

        //股价触碰均线
        boolean flag30 = isIn(gap30, -7.5, -11);
        boolean flag60 = isIn(gap60, -7.2, -14.5);
        boolean flag120 = isIn(gap120, -7.6, -16);
        boolean flag250 = isIn(gap250, -9.0, -13);
        boolean matchGap = false;
        if (flag30 || flag60 || flag120 || flag250) {
            matchGap = true;
        }
        if (!matchGap) {
            return false;
        }

        //5天内必须有一个短线小于1
        boolean matchGap2 = false;
        double cur = row.getFloat(table.getColumn("cur"));
        double prev1 = row.getFloat(table.getColumn("prev(1)"));
        double prev2 = row.getFloat(table.getColumn("prev(2)"));
        double prev3 = row.getFloat(table.getColumn("prev(3)"));
        double prev4 = row.getFloat(table.getColumn("prev(4)"));
        if (Math.abs(cur) < 1 || Math.abs(prev1) < 1 || Math.abs(prev2) < 1 || Math.abs(prev3) < 1 || Math.abs(prev4) < 1) {
            matchGap2 = true;
        }
        if (!matchGap2) {
            return false;
        }

        //三日内有一个涨幅>8.5 skip
        double maxZf = kline0.getMaxZhangfu(3);
        if(maxZf>8.5) {
            return false;
        }

        //10日线与30日线即将死叉 skip
        boolean isWillDCross = kline0.isWillDeadCross(10, 30);
        if(isWillDCross) {
            return false;
        }

        double k250 = row.getFloat(table.getColumn("k250"));
        double k120 = row.getFloat(table.getColumn("k120"));
        double k60 = row.getFloat(table.getColumn("k60"));
        double k30 = row.getFloat(table.getColumn("k30"));
        double min = getMin(k30, k60, k120, k250);
//        if (min > -9) {
//            return false;
//        }


        //cmf 筹码峰
        double cmf70 = row.getFloat(table.getColumn("70%cmf"));
        if (cmf70 > 0.23) {
            return false;
        }

        //fracTrapInProfit < 200 ok
        //fracTrapInProfit > 200 & < 250 priceZf>12 ok
        //fracTrapInProfit > 250 err
        double fracTrapInProfit = row.getFloat(table.getColumn("fracTrapInProfit"));
        double priceZf = row.getFloat(table.getColumn("priceZf"));
        double minLine = row.getFloat(table.getColumn("minLine"));
        if (fracTrapInProfit > 250) {
            return false;
        }
        if (fracTrapInProfit > 200 && priceZf < 12) {
            return false;
        }
        if (minLine > 0.5) {
            return false;
        }

        if (cur < -0 && prev1 < 0) {
            if (cur < -3 && prev1 < -2) {
                return false;
            }
        }

        //当月不能触碰均线
        monthKline.setCurDayIdx(kline0.getIdx());
        if (monthKline.isTouchOnMAI(10) && monthKline.getOpen() < monthKline.getMA10()) {
            return false;
        }
        if (monthKline.isTouchOnMAI(30) && monthKline.getOpen() < monthKline.getMA30()) {
            return false;
        }
        if (monthKline.isTouchOnMAI(60) && monthKline.getOpen() < monthKline.getMA60()) {
            return false;
        }
        if (monthKline.isTouchOnMAI(120) && monthKline.getOpen() < monthKline.getMA120()) {
            return false;
        }
        if (monthKline.isTouchOnMAI(250) && monthKline.getOpen() < monthKline.getMA250()) {
            return false;
        }

        if (monthKline.getOpen() > monthKline.getMA10()) {
            return false;
        }
        if (monthKline.getMA30() > 0 && monthKline.getOpen() > monthKline.getMA30()) {
            return false;
        }
        if (monthKline.getMA60() > 0 && monthKline.getOpen() > monthKline.getMA60()) {
            return false;
        }
        if (monthKline.getMA120() > 0 && monthKline.getOpen() > monthKline.getMA120()) {
            return false;
        }
        if (monthKline.getOpen() > monthKline.getMA250() && monthKline.getMA250() > 0) {
            return false;
        }

        double nextZT = kline0.getZT();
//        if (KLineUtil.compareMaxSign(nextZT, monthKline.getMA10())>0) {
//            return false;
//        }

//        if (nextZT > monthKline.getMA30()) {
//            return false;
//        }
//        if (nextZT > monthKline.getMA60()) {
//            return false;
//        }
//        if (nextZT > monthKline.getMA120()) {
//            return false;
//        }
//        if (nextZT > monthKline.getMA250() && monthKline.getMA250()>0) {
//            return false;
//        }


        double max = weekline.getMax();
        if (weekline.isTouchOnMAI(10) && weekline.getOpen() < weekline.getMA10()) {
            boolean frac = weekline.prev().isShadownUp(50);
            if (!frac && !weekline.prev().isShadownDown(3)) {
                return false;
            }
        }
        if (weekline.isTouchOnMAI(30) && weekline.getOpen() < weekline.getMA30()) {
            return false;
        }
        if (weekline.isTouchOnMAI(60) && weekline.getOpen() < weekline.getMA60()) {
            return false;
        }
        if (weekline.isTouchOnMAI(120) && weekline.getOpen() < weekline.getMA120()) {
            return false;
        }
        if (weekline.isTouchOnMAI(250) && weekline.getOpen() < weekline.getMA250()) {
            return false;
        }

        boolean gCrossFlag = false;
        int gcrossNum3060 = kline0.getGoldCrossNum(30, 60);
        if (gcrossNum3060 >= 0 && gcrossNum3060 <= 30) {
            gCrossFlag = true;
        }
        if (gCrossFlag) {
            return false;
        }

        //死叉
        boolean dCrossFlag = false;
        int dcrossNum30 = kline0.getDeadCrossNum(10, 30);
        if (dcrossNum30 >= 0 && dcrossNum30 <= 10) {
            dCrossFlag = true;
        }

        int dcrossNum60 = kline0.getDeadCrossNum(10, 60);
        if (dcrossNum60 >= 0 && dcrossNum60 <= 10) {
            dCrossFlag = true;
        }

        int dcrossNum120 = kline0.getDeadCrossNum(10, 120);
        if (dcrossNum120 >= 0 && dcrossNum120 <= 20) {
            dCrossFlag = true;
        }

        int dcrossNum250 = kline0.getDeadCrossNum(10, 250);
        if (dcrossNum250 >= 0 && dcrossNum250 <= 20) {
            dCrossFlag = true;
        }
        if (dCrossFlag) {
            return false;
        }
        return true;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = AbsStragety.BOTTOM_PATH + "2024-03-15_" + ".xlsx";
        Table table = ExcelWrite2007Test.read(apath);

        try {
            table.initIndex();
            ExcelWrite2007Test.main(table, new Table.Filter() {
                @Override
                public boolean filter(int rowNumber, Row row) {
                    if (rowNumber == 0) {
                        return true;
                    }
                    if (row.isNull()) {
                        return false;
                    }

                    String acode = row.getCol(0).data;
                    acode = acode.substring(0, acode.indexOf(" "));
                    String adate = row.getCol(3).data;
                    SingleContext singleContext = AbsStragety.getContext(acode + ".txt", adate);
                    singleContext.getWeeks();
                    singleContext.getMoths();
                    Log.log(acode);
                    int idx = AbsStragety.getIdx(singleContext.getDays(), adate);
                    if (idx == -1) {
                        return true;
                    }
                    Kline kline0 = singleContext.getDays().get(idx).prev();

                    boolean flag = isRealOK(kline0, table, row);
                    return flag;
                }
            }, "ret_", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


