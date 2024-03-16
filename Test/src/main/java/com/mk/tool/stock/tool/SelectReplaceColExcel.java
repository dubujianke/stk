package com.mk.tool.stock.tool;

import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Log;
import com.mk.util.ExcelWrite2007Test;
import com.mk.util.StringUtil;

import java.io.IOException;


public class SelectReplaceColExcel {
    public static StringBuffer resultBuffer = new StringBuffer();

    public static String FILE = "root/node1/zt_bak_";


    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = AbsStragety.BOTTOM_PATH + "/step0基底/bottom" + ".xlsx";
        Table table = ExcelWrite2007Test.read(apath);
        try {
            table.initIndex();
            ExcelWrite2007Test.mainNoSortABS(table, new Table.Filter() {
                @Override
                public boolean filter(int rowNumber, Row row) {
                    if (rowNumber == 0) {
                        return true;
                    }
                    if (row.isNull()) {
                        return false;
                    }

                    String data = row.getStr("周涨幅").trim();
                    String[] vs = data.split(" ");
                    double v1 = Double.parseDouble(vs[0]);
                    int v2 = Integer.parseInt(vs[1]);
                    if (v2 < 8 && v1 > 19) {
                        row.setCol(row.getTable().getColumn("周涨幅"), "" + vs[0]);
                    } else {
                        row.setCol(row.getTable().getColumn("周涨幅"), "" + 0);
                    }



                    return true;
                }
            }, AbsStragety.BOTTOM_PATH+"/step0基底/bottom1", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


