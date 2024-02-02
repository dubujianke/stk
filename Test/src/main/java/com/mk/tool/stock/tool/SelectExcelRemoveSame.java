package com.mk.tool.stock.tool;

import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.Log;
import com.mk.util.ExcelWrite2007Test;
import com.mk.util.StringUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class SelectExcelRemoveSame {
    public static StringBuffer resultBuffer = new StringBuffer();
    static int num = 0;
    static Set<String> set = new HashSet();
     public static void main(String[] args) throws IOException, InterruptedException {
        String apath = ExcelWrite2007Test.PATH + "zt_basic" + ".xlsx";
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

                    if(rowNumber == 20) {
                        int a = 0;
                    }
                    if (StringUtil.isNull(row.getCol(table.getColumn("code")).data)) {
                        return false;
                    }

                    String val = row.getCol(table.getColumn("code")).data + row.getCol(table.getColumn("日期")).data;
                    if(set.contains(val)) {
                        return false;
                    }else {
                        set.add(val);
                    }

                    return true;
                }
            }, "zt_basic_", true);

            Log.log("=========>25:"+num);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


