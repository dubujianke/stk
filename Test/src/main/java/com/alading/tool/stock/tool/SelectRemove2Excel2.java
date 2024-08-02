package com.alading.tool.stock.tool;

import com.alading.model.Col;
import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.AbsStragety;
import com.alading.tool.stock.Kline;
import com.alading.tool.stock.Log;
import com.alading.tool.stock.SingleContext;
import com.alading.util.ExcelWrite2007Test;
import com.alading.util.StringUtil;

import java.io.IOException;


public class SelectRemove2Excel2 {

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


    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = ExcelWrite2007Test.PATH + "root\\node1\\zt_bak__" + ".xlsx";
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
//                    for (String s : new String[]{"30d60", "30d120", "30d250", "60d120", "60d250", "120d250"}) {
//                        if(row.getInt(table.getColumn(s))<10 && row.getInt(table.getColumn(s))>0) {
//                            row.getCol(table.getColumn("code")).isBold = true;
//                            row.getCol(table.getColumn(s)).isBold = true;
//                            return true;
//                        }
//                    }

                    String acode = row.getCol(0).data;
                    acode = acode.substring(0, acode.indexOf(" "));
                    String adate = row.getCol(3).data;
                    SingleContext singleContext = AbsStragety.getContext(acode + ".txt", adate);
                    singleContext.getWeeks();
                    singleContext.getMoths();


                    int idx = AbsStragety.getIdx(singleContext.getDays(), adate);
                    if (idx == -1) {
                        return true;
                    }
                    Kline kline0 = singleContext.getDays().get(idx);

                    if(acode.equalsIgnoreCase("600613")) {
                        int a = 0;
                    }
                    Log.log(acode);
                    com.alading.tool.stock.Kline.LocalBottom localBottom = kline0.prev().getLocalBottom();
                    row.add(new Col("" + localBottom.flag));
                    row.add(new Col("" + localBottom.num));
                    row.add(new Col(StringUtil.format(localBottom.frac)));



                    return true;
                }
            }, "root\\node1\\zt_bak___", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


