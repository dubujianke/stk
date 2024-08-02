package com.alading.tool.stock.tool;

import com.alading.model.Col;
import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.*;
import com.alading.util.ExcelWrite2007Test;

import java.io.IOException;


public class SelectAddColExcel {
    public static StringBuffer resultBuffer = new StringBuffer();

    public static String FILE = "root/node1/zt_bak_";

    public static String defalutValue = "1000";
    public static String defalutValue_null = "";


    public static void monthMa120(Kline kline0, Table table, Row row) {
        Kline mklinePrev0 = kline0.monthKline;
        Kline mklinePrev1 = mklinePrev0.prev();
        Kline mklinePrev2 = mklinePrev0.prev(2);
        int dir0 = 0;
        int dir1 = 0;
        int dir2 = 0;

        double ma120Prev0 = mklinePrev0.getMA120();
        double fracMa120Prev0 = KLineUtil.compareMaxSign(kline0.getClose(), ma120Prev0);
        if (mklinePrev0.close > ma120Prev0 && mklinePrev0.open < ma120Prev0) {
            dir0 = 1;
        } else if (mklinePrev0.open > ma120Prev0 && mklinePrev0.close < ma120Prev0) {
            dir0 = 2;
        }
        row.setCol(table.getColumn("PrevMA120Month(0)"), "" + fracMa120Prev0 + " (" + dir0 + ")");

        double ma120Prev1 = mklinePrev1.getMA120();
        double fracMa120Prev1 = KLineUtil.compareMaxSign(mklinePrev1.getClose(), ma120Prev1);
        if (mklinePrev1.close > ma120Prev1 && mklinePrev1.open < ma120Prev1) {
            dir1 = 1;
        } else if (mklinePrev1.open > ma120Prev1 && mklinePrev1.close < ma120Prev1) {
            dir1 = 2;
        }
        row.setCol(table.getColumn("PrevMA120Month(1)"), "" + fracMa120Prev1 + " (" + dir1 + ")");

        double ma120Prev2 = mklinePrev2.getMA120();
        double fracMa120Prev2 = KLineUtil.compareMaxSign(mklinePrev2.getClose(), ma120Prev2);
        if (mklinePrev2.close > ma120Prev2 && mklinePrev2.open < ma120Prev2) {
            dir2 = 1;
        } else if (mklinePrev2.open > ma120Prev2 && mklinePrev2.close < ma120Prev2) {
            dir2 = 2;
        }
        row.setCol(table.getColumn("PrevMA120Month(2)"), "" + fracMa120Prev2 + " (" + dir2 + ")");
    }

    public static void monthMa60(Kline kline0, Table table, Row row) {
        Kline mklinePrev0 = kline0.monthKline;
        Kline mklinePrev1 = mklinePrev0.prev();
        Kline mklinePrev2 = mklinePrev0.prev(2);
        int dir0 = 0;
        int dir1 = 0;
        int dir2 = 0;

        double ma60Prev0 = mklinePrev0.getMA60();
        double fracMa60Prev0 = KLineUtil.compareMaxSign(kline0.getClose(), ma60Prev0);
        if (mklinePrev0.close > ma60Prev0 && mklinePrev0.open < ma60Prev0) {
            dir0 = 1;
        } else if (mklinePrev0.open > ma60Prev0 && mklinePrev0.close < ma60Prev0) {
            dir0 = 2;
        }
        row.setCol(table.getColumn("PrevMA60Month(0)"), "" + fracMa60Prev0 + " (" + dir0 + ")");

        double ma60Prev1 = mklinePrev1.getMA60();
        double fracMa60Prev1 = KLineUtil.compareMaxSign(mklinePrev1.getClose(), ma60Prev1);
        if (mklinePrev1.close > ma60Prev1 && mklinePrev1.open < ma60Prev1) {
            dir1 = 1;
        } else if (mklinePrev1.open > ma60Prev1 && mklinePrev1.close < ma60Prev1) {
            dir1 = 2;
        }
        row.setCol(table.getColumn("PrevMA60Month(1)"), "" + fracMa60Prev1 + " (" + dir1 + ")");

        double ma60Prev2 = mklinePrev2.getMA60();
        double fracMa60Prev2 = KLineUtil.compareMaxSign(mklinePrev2.getClose(), ma60Prev2);
        if (mklinePrev2.close > ma60Prev2 && mklinePrev2.open < ma60Prev2) {
            dir2 = 1;
        } else if (mklinePrev2.open > ma60Prev2 && mklinePrev2.close < ma60Prev2) {
            dir2 = 2;
        }
        row.setCol(table.getColumn("PrevMA60Month(2)"), "" + fracMa60Prev2 + " (" + dir2 + ")");
    }


    public static void monthZf(Kline kline0, Table table, Row row) {
        Kline mklinePrev0 = kline0.monthKline;
        Kline mklinePrev1 = mklinePrev0.prev();
        Kline mklinePrev2 = mklinePrev0.prev(2);
        row.setCol(table.getColumn("mprev(0)"), "" + mklinePrev0.getZhangfu() + " (" + mklinePrev0.getEntityZhangfu() + ")");
        row.setCol(table.getColumn("mprev(1)"), "" + mklinePrev1.getZhangfu() + " (" + mklinePrev1.getEntityZhangfu() + ")");
        row.setCol(table.getColumn("mprev(2)"), "" + mklinePrev2.getZhangfu() + " (" + mklinePrev2.getEntityZhangfu() + ")");
    }


    public static void monthMa250Add(Kline kline0, Row row) {
        Kline mklinePrev0 = kline0.monthKline;
        Kline mklinePrev1 = mklinePrev0.prev();
        Kline mklinePrev2 = mklinePrev0.prev(2);
        int dir0 = 0;
        int dir1 = 0;
        int dir2 = 0;

        double ma250Prev0 = mklinePrev0.getMA250();
        double fracMa250Prev0 = KLineUtil.compareMaxSign(kline0.getClose(), ma250Prev0);
        if (mklinePrev0.close > ma250Prev0 && mklinePrev0.open < ma250Prev0) {
            dir0 = 1;
        } else if (mklinePrev0.open > ma250Prev0 && mklinePrev0.close < ma250Prev0) {
            dir0 = 2;
        }


        double ma250Prev1 = mklinePrev1.getMA250();
        double fracMa250Prev1 = KLineUtil.compareMaxSign(mklinePrev1.getClose(), ma250Prev1);
        if (mklinePrev1.close > ma250Prev1 && mklinePrev1.open < ma250Prev1) {
            dir1 = 1;
        } else if (mklinePrev1.open > ma250Prev1 && mklinePrev1.close < ma250Prev1) {
            dir1 = 2;
        }



        double ma250Prev2 = mklinePrev2.getMA250();
        double fracMa250Prev2 = KLineUtil.compareMaxSign(mklinePrev2.getClose(), ma250Prev2);
        if (mklinePrev2.close > ma250Prev2 && mklinePrev2.open < ma250Prev2) {
            dir2 = 1;
        } else if (mklinePrev2.open > ma250Prev2 && mklinePrev2.close < ma250Prev2) {
            dir2 = 2;
        }

        row.add(new Col("" + fracMa250Prev2 + " (" + dir2 + ")"));
        row.add(new Col("" + fracMa250Prev1 + " (" + dir1 + ")"));
        row.add(new Col("" + fracMa250Prev0 + " (" + dir0 + ")"));
    }

    public static void monthMa120Add(Kline kline0, Row row) {
        Kline mklinePrev0 = kline0.monthKline;
        Kline mklinePrev1 = mklinePrev0.prev();
        Kline mklinePrev2 = mklinePrev0.prev(2);
        int dir0 = 0;
        int dir1 = 0;
        int dir2 = 0;

        double ma120Prev0 = mklinePrev0.getMA120();
        double fracMa120Prev0 = KLineUtil.compareMaxSign(kline0.getClose(), ma120Prev0);
        if (mklinePrev0.close > ma120Prev0 && mklinePrev0.open < ma120Prev0) {
            dir0 = 1;
        } else if (mklinePrev0.open > ma120Prev0 && mklinePrev0.close < ma120Prev0) {
            dir0 = 2;
        }



        double ma120Prev1 = mklinePrev1.getMA120();
        double fracMa120Prev1 = KLineUtil.compareMaxSign(mklinePrev1.getClose(), ma120Prev1);
        if (mklinePrev1.close > ma120Prev1 && mklinePrev1.open < ma120Prev1) {
            dir1 = 1;
        } else if (mklinePrev1.open > ma120Prev1 && mklinePrev1.close < ma120Prev1) {
            dir1 = 2;
        }



        double ma120Prev2 = mklinePrev2.getMA120();
        double fracMa120Prev2 = KLineUtil.compareMaxSign(mklinePrev2.getClose(), ma120Prev2);
        if (mklinePrev2.close > ma120Prev2 && mklinePrev2.open < ma120Prev2) {
            dir2 = 1;
        } else if (mklinePrev2.open > ma120Prev2 && mklinePrev2.close < ma120Prev2) {
            dir2 = 2;
        }
        row.add(new Col("" + fracMa120Prev2 + " (" + dir2 + ")"));
        row.add(new Col("" + fracMa120Prev1 + " (" + dir1 + ")"));
        row.add(new Col("" + fracMa120Prev0 + " (" + dir0 + ")"));
    }

    public static void monthMa60Add(Kline kline0, Row row) {
        Kline mklinePrev0 = kline0.monthKline;
        Kline mklinePrev1 = mklinePrev0.prev();
        Kline mklinePrev2 = mklinePrev0.prev(2);
        int dir0 = 0;
        int dir1 = 0;
        int dir2 = 0;

        double ma60Prev0 = mklinePrev0.getMA60();
        double fracMa60Prev0 = KLineUtil.compareMaxSign(kline0.getClose(), ma60Prev0);
        if (mklinePrev0.close > ma60Prev0 && mklinePrev0.open < ma60Prev0) {
            dir0 = 1;
        } else if (mklinePrev0.open > ma60Prev0 && mklinePrev0.close < ma60Prev0) {
            dir0 = 2;
        }



        double ma60Prev1 = mklinePrev1.getMA60();
        double fracMa60Prev1 = KLineUtil.compareMaxSign(mklinePrev1.getClose(), ma60Prev1);
        if (mklinePrev1.close > ma60Prev1 && mklinePrev1.open < ma60Prev1) {
            dir1 = 1;
        } else if (mklinePrev1.open > ma60Prev1 && mklinePrev1.close < ma60Prev1) {
            dir1 = 2;
        }



        double ma60Prev2 = mklinePrev2.getMA60();
        double fracMa60Prev2 = KLineUtil.compareMaxSign(mklinePrev2.getClose(), ma60Prev2);
        if (mklinePrev2.close > ma60Prev2 && mklinePrev2.open < ma60Prev2) {
            dir2 = 1;
        } else if (mklinePrev2.open > ma60Prev2 && mklinePrev2.close < ma60Prev2) {
            dir2 = 2;
        }

        row.add(new Col("" + fracMa60Prev2 + " (" + dir2 + ")"));
        row.add(new Col("" + fracMa60Prev1 + " (" + dir1 + ")"));
        row.add(new Col("" + fracMa60Prev0 + " (" + dir0 + ")"));
    }


    public static void monthZfAdd(Kline kline0, Row row) {
        Kline mklinePrev0 = kline0.monthKline;
        Kline mklinePrev1 = mklinePrev0.prev();
        Kline mklinePrev2 = mklinePrev0.prev(2);

        row.add(new Col("" + mklinePrev2.getZhangfu() + " (" + mklinePrev2.getEntityZhangfu() + ")"));
        row.add(new Col("" + mklinePrev1.getZhangfu() + " (" + mklinePrev1.getEntityZhangfu() + ")"));
        row.add(new Col("" + mklinePrev0.getZhangfu() + " (" + mklinePrev0.getEntityZhangfu() + ")"));

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = AbsStragety.BOTTOM_PATH + "2024-03-07.xlsx";
        Table table = ExcelWrite2007Test.read(apath);
        try {
            table.initIndex();
            ExcelWrite2007Test.mainNoSort(table, new Table.Filter() {
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
                    if (acode.equalsIgnoreCase("002037")) {
                        int a = 0;
                    }
                    String adate = row.getCol(2).data;
                    SingleContext singleContext = AbsStragety.getContext(acode + ".txt", adate);
                    singleContext.getWeeks();
                    singleContext.getMoths();

                    Log.log(acode);
                    int idx = AbsStragety.getIdx(singleContext.getDays(), adate);
                    if (idx == -1) {
                        return true;
                    }
                    Kline kline0 = singleContext.getDays().get(idx);

                    row.setCol(table.getColumn("cur_"), ""+ kline0.getEntityZhangfu());
                    row.setCol(table.getColumn("prev_(1)"), ""+ kline0.prev(1).getEntityZhangfu());
                    row.setCol(table.getColumn("prev_(2)"), ""+ kline0.prev(2).getEntityZhangfu());
                    row.setCol(table.getColumn("prev_(3)"), ""+ kline0.prev(3).getEntityZhangfu());
                    return true;
                }
            }, "2024-03-07_", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


