package com.mk.tool.stock.tool;

import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.tool.stock.Log;
import com.mk.tool.stock.SingleContext;
import com.mk.util.ExcelWrite2007Test;

import java.io.IOException;


public class SelectFilterExcel {
    public static StringBuffer resultBuffer = new StringBuffer();


    public static void print(float[] vs, boolean abs) {
        for (float v : vs) {
            //System.out.println(v);
        }
    }

    public static void filter(float[] vs, float[] vs2, boolean flag, boolean flag2) {
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

    public static float min(float[] vs, boolean abs) {
        float min = Float.MAX_VALUE;
        for (float v : vs) {
            if (abs) {
                v = Math.abs(v);
            }
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public static float count(float[] vs) {
        return vs.length;
    }



    public static float max(float[] vs, boolean abs) {
        float min = Float.MIN_VALUE;
        for (float v : vs) {
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
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = AbsStragety.BOTTOM_PATH + "a11_" + ".xlsx";
        Table table = ExcelWrite2007Test.read(apath);
        try {
            table.initIndex();
            table.sort = false;
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

                    if(row.getInt(table.getColumn("10g30"))==-1 && row.getInt(table.getColumn("10g60"))==-1) {
                        row.setCol(table.getColumn("10ga"), "" +  "1");
                    }else {
                        row.setCol(table.getColumn("10ga"), "" +  "0");
                    }

                    Kline kline0 = singleContext.getDays().get(idx);
                    row.setCol(table.getColumn("Test"), "" +  kline0.getNextZF(2));
                    return true;
                }
            }, "a11_", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


