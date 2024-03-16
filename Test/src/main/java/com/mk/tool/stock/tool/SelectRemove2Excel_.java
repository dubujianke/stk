package com.mk.tool.stock.tool;

import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.tree.TreeGraph;
import com.mk.util.ExcelWrite2007Test;
import com.mk.util.StringUtil;
import org.springframework.boot.autoconfigure.social.FacebookAutoConfiguration;

import java.io.IOException;


public class SelectRemove2Excel_ {

    public static int getCNT(String str, int v) {
        str = str.substring(1, str.length() - 1);
        String[] items = str.split(",");
        int flag = 0;
        for (String item : items) {
            if (Double.parseDouble(item) >= v) {
                flag++;
            }
        }
        return flag;
    }

    public static double[] getCNTS(String str) {
        str = str.substring(1, str.length() - 1);
        String[] items = str.split(",");
        double vs[] = new double[3];
        int idx = 0;
        for (String item : items) {
            vs[idx] = Double.parseDouble(item);
            idx++;
        }
        return vs;
    }


//    public static void main(String[] args) throws IOException, InterruptedException {
//        String apath = ExcelWrite2007Test.PATH + "root/node1/zt_bak__" + ".xlsx";
//        Table table = ExcelWrite2007Test.read(apath);
//        try {
//            table.initIndex();
//            ExcelWrite2007Test.main(table, new Table.Filter() {
//                @Override
//                public boolean filter(int rowNumber, Row row) {
//                    if (rowNumber == 0) {
//                        return true;
//                    }
//
//                    if (row.isNull()) {
//                        return false;
//                    }
//
//                    String acode = row.getCol(0).data;
//                    acode = acode.substring(0, acode.indexOf(" "));
//                    String adate = row.getCol(3).data;
//                    Boolean[] flags = TreeGraph.instance.process(table, rowNumber);
//                    Log.log(acode + " " + adate + " " + flags[0] + " " + flags[1]);
//                    return flag;
//                }
//            }, "root/node1/zt_bak_2", true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}


