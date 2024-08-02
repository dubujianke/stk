package com.alading.tool.stock.decision;

import cn.hutool.core.util.StrUtil;
import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.Command;
import com.alading.tool.stock.KLineUtil;
import com.alading.tool.stock.Log;
import com.alading.util.ExcelWrite2007Test;

import java.io.IOException;


/**
 * OK
 * 大涨幅度在5到7之间
 */
public class Decision57 {
    public static StringBuffer resultBuffer = new StringBuffer();
    static int num = 0;
    static int total;
    static Table table;

    public static boolean testWithError(String code, int rowIdx) {
        try {
            Command command = new Command();
            command.code = code;
            command.parse();
            boolean flag = command.process(null, table, rowIdx);
            return flag;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean judge(Table table, int rowNumber) {
        if (rowNumber == 0) {
            return true;
        }

        Row row = table.rows.get(rowNumber);
        if (row.isNull()) {
            return false;
        }

        if(rowNumber == 25) {
            int a = 0;
        }
        if (KLineUtil.testWithError("大涨幅度 < 5.0", null, table, rowNumber, false)) {
            return false;
        }
        if (KLineUtil.testWithError("大涨幅度 >= 7.0", null, table, rowNumber, false)) {
            return false;
        }
//        if (KLineUtil.testWithError("大涨幅度 > 5.0", null, table, rowNumber, false)) {
//            return false;
//        }

        if (KLineUtil.testWithError("振幅 > 5.4 & 振幅 < 6.4", null, table, rowNumber, false)) {
            if (KLineUtil.testWithError("gap250 >= -11.0 & gap250 < -8.0", null, table, rowNumber, false)) {
                return true;
            } else {
                Log.log("=========>e:" + rowNumber);
                return false;
            }
        }
        if (KLineUtil.testWithError("振幅 > 6.4", null, table, rowNumber, false)) {
            return false;
        }

        double vw = row.getFloat(table.getColumn("大涨幅度"));
        if (vw >= 7) {
            if (row.getFloat(table.getColumn("大涨Idx")) < 10) {
                return false;
            }
        }

        double v = row.getFloat(table.getColumn("涨幅"));
///////////////////////////////////////////////////////////
        //(大涨幅度 > 5  大涨幅度<=7)
        int p1 = row.getInt(table.getColumn("大涨Idx"));
        if (KLineUtil.testWithError("大涨Idx < 3", null, table, rowNumber, false)) {
            return false;
        }

        if (KLineUtil.testWithError("股本 >= 95", null, table, rowNumber, false)) {
            return false;
        }

        if (KLineUtil.testWithError("大涨Idx == 3", null, table, rowNumber, false)) {
//                        if(KLineUtil.testWithError("gap250 < -11.0", null, table, rowNumber, false)) {
//                            return false;
//                        }
//                        if(KLineUtil.testWithError("gap120 < -6.8", null, table, rowNumber, false)) {
//                            return false;
//                        }
        }

        if (testWithError("大涨Idx >= 4", rowNumber)) {
            if (testWithError("gap250 < -10.0", rowNumber)) {
                if (testWithError("gap120 > -11.0", rowNumber) && testWithError("gap120 < -8", rowNumber)) {
                } else {
                    return false;
                }
            }
        }
        if (p1 >= 4) {
            if (testWithError("gap120 < -10.0", rowNumber)) {
                return false;
            }
        }

        if (p1 >= 4) {
            if (testWithError("gap60 < -10.0", rowNumber)) {
                return false;
            }
        }
        if (p1 >= 4) {
            if (testWithError("跳空涨幅 < 0", rowNumber)
                    && testWithError("跳空涨幅 > -7", rowNumber)) {
                return false;
            }

            if (testWithError("跳空涨幅 < 0", rowNumber)
                    && testWithError("gap250 > 0", rowNumber)
                    && testWithError("gap120 > 0", rowNumber)
                    && testWithError("gap60 < 0", rowNumber)
                    && testWithError("gap30 < 0", rowNumber)
                    && testWithError("跳空涨幅 > -7", rowNumber)) {
                return true;
            }
        }

        String p2 = row.getCol(table.getColumn("上月上影线")).data.trim();
        if (StrUtil.equals(p2, "true")) {
            return false;
        }

        if (KLineUtil.testWithError("cur <= -2", null, table, rowNumber, false)) {
            return false;
        }

        String p3[] = row.getCol(table.getColumn("换手")).data.replace("[", "").replace("]", "").split(",");
        boolean handFlag = false;
        for (String item : p3) {
            if (Double.parseDouble(item) <= 5) {
                handFlag = true;
            }
        }
        if (!handFlag) {
            return false;
        }


        if (KLineUtil.testWithError("两月涨幅 > 19", null, table, rowNumber, false)) {
            return false;
        }
        if (KLineUtil.testWithError("月压力数 > 1", null, table, rowNumber, false)) {
            return false;
        }

        String p4[] = row.getCol(table.getColumn("wprev(1)")).data.split("/");
        if (Double.parseDouble(p4[1]) > 13.2) {
            return false;
        }

        if (KLineUtil.testWithError("gap60 < 0", null, table, rowNumber, false)
                && KLineUtil.testWithError("gap30 > 0", null, table, rowNumber, false)) {
            return false;
        }


        if (KLineUtil.testWithError("加速天量 < 减速天量", null, table, rowNumber, false) &&
                KLineUtil.testWithError("加速天序号 >= 0", null, table, rowNumber, false) &&
                KLineUtil.testWithError("减速天序号 >= 0", null, table, rowNumber, false)) {
            return false;
        }

        boolean handFlag2 = false;
        for (String item : p3) {
            if (Double.parseDouble(item) <= 2) {
                handFlag2 = true;
            }
        }
        if (!handFlag2) {
            if (KLineUtil.testWithError("大涨Idx < 12", null, table, rowNumber, false)) {
                return false;
            }
        }

        int cnt_ = 0;
        for (String item : p3) {
            if (Double.parseDouble(item) > 1.9) {
                cnt_++;
            }
        }
        if (cnt_ >= 2) {
            if (KLineUtil.testWithError("prev(2) <= 0", null, table, rowNumber, false)
                    && KLineUtil.testWithError("prev(1) <= 0", null, table, rowNumber, false)
                    && KLineUtil.testWithError("cur <= 0", null, table, rowNumber, false)) {
                return false;
            }
        }


        double zf2 = row.getFloat(table.getColumn("zhenf(2)"));
        double zf1 = row.getFloat(table.getColumn("zhenf(1)"));
        double zf0 = row.getFloat(table.getColumn("zhenf(0)"));

        if (zf2 < 3 && zf1 < 3 && zf0 < 3) {
            double cur = row.getFloat(table.getColumn("cur"));
            double totalF = zf0 + zf1 + zf2;
            if (zf0 < 1.9 && !(Math.abs(zf0 - 1.9) < 0.01)) {
                return false;
            }
            if (Math.abs(cur) > 0.65) {
                return false;
            }
            if (totalF < 7.0f) {
                double v2 = row.getFloat(table.getColumn("gap250"));
                if (v2 >= -11.0 && v2 < -8.0) {
                } else {
                    return false;
                }
            }
        }

        int d3060 = row.getInt(table.getColumn("30d60"));
        if (d3060 < 10 && d3060 > -1) {
            double gap60 = row.getFloat(table.getColumn("gap60"));
            double gap30 = row.getFloat(table.getColumn("gap30"));
            if (gap60 < 0 && gap60 > -6) {
                return false;
            }
            if (gap30 < 0 && gap30 > -5) {
                return false;
            }
        }

        double gap250MOpen = row.getFloat(table.getColumn("gap250MOpen"));
        if (gap250MOpen < -7) {
            return false;
        }

        double p5 = row.getFloat(table.getColumn("上上周涨幅"));
        if (p5 > 8) {
            return false;
        }

        if (row.getFloat(table.getColumn("MIN涨幅")) < -1.7 &&
                row.getFloat(table.getColumn("月MIN涨幅")) < -7) {
            return false;
        }
        if (rowNumber == 23) {
            int a = 0;
        }
        if (row.getFloat(table.getColumn("gap60")) < 0 && row.getFloat(table.getColumn("gap60")) > -1) {
//            String p6 = row.getCol(table.getColumn("bottom")).data;
                return false;
        }

        if (row.getFloat0(table.getColumn("wprev(2)")) >9
                && row.getFloat0(table.getColumn("wprev(1)")) > -0.6
                && row.getFloat0(table.getColumn("wprev(1)")) < 0.6) {
            return false;
        }


        return true;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = ExcelWrite2007Test.PATH + "zt_bak" + ".xlsx";
        table = ExcelWrite2007Test.read(apath);
        try {
            table.initIndex();
            ExcelWrite2007Test.main(table, new Table.Filter() {
                @Override
                public boolean filter(int rowNumber, Row row) {
                    boolean flag = judge(table, rowNumber);
                    if(flag) {
                        num++;
                    }
                    total++;
                    return flag;
                }
            }, "zt_bak_", true);

            Log.log("=========>22:" + num + "/" + total + " percent:" + (100.0 * (num-1) / (total-1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


