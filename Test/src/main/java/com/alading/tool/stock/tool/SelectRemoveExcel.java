package com.alading.tool.stock.tool;

import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.AbsStragety;
import com.alading.util.ExcelWrite2007Test;

import java.io.IOException;


public class SelectRemoveExcel {
    public static StringBuffer resultBuffer = new StringBuffer();


    public static void print(double[] vs, boolean abs) {
        for (double v : vs) {
            //System.out.println(v);
        }
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = AbsStragety.BOTTOM_PATH + "ztbasic09" + ".xlsx";
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
                    if (row.getFloat(table.getColumn("大涨幅度"))>=5) {
                        return false;
                    }

                    return true;
                }
            }, "ztbasic09_", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


