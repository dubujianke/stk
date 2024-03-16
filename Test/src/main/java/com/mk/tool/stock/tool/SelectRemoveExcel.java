package com.mk.tool.stock.tool;

import cn.hutool.core.util.StrUtil;
import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.AbsStragety;
import com.mk.util.ExcelWrite2007Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


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


