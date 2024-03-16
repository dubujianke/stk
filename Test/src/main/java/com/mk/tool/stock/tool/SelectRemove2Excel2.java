package com.mk.tool.stock.tool;

import com.mk.model.Col;
import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.tool.stock.Log;
import com.mk.tool.stock.SingleContext;
import com.mk.util.ExcelWrite2007Test;
import com.mk.util.StringUtil;

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
                    com.mk.tool.stock.Kline.LocalBottom localBottom = kline0.prev().getLocalBottom();
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


