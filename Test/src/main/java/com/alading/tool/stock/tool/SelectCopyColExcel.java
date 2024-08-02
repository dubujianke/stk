package com.alading.tool.stock.tool;

import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.AbsStragety;
import com.alading.tool.stock.Log;
import com.alading.util.ExcelWrite2007Test;
import com.alading.util.StringUtil;
import java.io.*;


public class SelectCopyColExcel {
    public static StringBuffer resultBuffer = new StringBuffer();

    public static String FILE = "root/node1/zt_bak_";

    public static String defalutValue = "1000";
    public static String defalutValue_null = "";

    public static void ff(Table table, Row row, String prefix) {
        {
            String yueyali = row.getStr(table.getColumn(prefix)).trim();
            if (StringUtil.isNull(yueyali)) {
                row.setCol(table.getColumn(prefix + "30"), defalutValue);
                row.setCol(table.getColumn(prefix + "60"), defalutValue);
                row.setCol(table.getColumn(prefix + "120"), defalutValue);
                row.setCol(table.getColumn(prefix + "250"), defalutValue);
            } else {
                String yl[] = yueyali.split(",");
                boolean contain30 = false;
                boolean contain60 = false;
                boolean contain120 = false;
                boolean contain250 = false;
                for (String v : yl) {
                    String[] vs = v.trim().replace("(", "").replace(")", "").split("\\s+");
                    Log.log(v + " " + vs[0]);
                    int period = Integer.parseInt(vs[0]);
                    if (period == 0) {
                        continue;
                    }
                    if (period == 30) {
                        contain30 = true;
                    }
                    if (period == 60) {
                        contain60 = true;
                    }
                    if (period == 120) {
                        contain120 = true;
                    }
                    if (period == 250) {
                        contain250 = true;
                    }
                    double vv = Double.parseDouble(vs[1]);
                    row.setCol(table.getColumn(prefix + period), "" + vv);
                }
                if (!contain30) {
                    row.setCol(table.getColumn(prefix + "30"), defalutValue);
                }
                if (!contain60) {
                    row.setCol(table.getColumn(prefix + "60"), defalutValue);
                }
                if (!contain120) {
                    row.setCol(table.getColumn(prefix + "120"), defalutValue);
                }
                if (!contain250) {
                    row.setCol(table.getColumn(prefix + "250"), defalutValue);
                }
            }
        }
    }

    public static void copy(String src, String dst) throws IOException, InterruptedException {
        Table tableSrc = ExcelWrite2007Test.read(src);
        Table table = ExcelWrite2007Test.read(dst);
        File file = new File(dst);
        String PATH = file.getParentFile().getAbsolutePath()+"/";
        try {
            tableSrc.initIndex();
            table.initIndex();
            ExcelWrite2007Test.mainNoSortPATH(table, new Table.Filter() {
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
                    Row rowSrc = tableSrc.getRow(acode);
                    try {
                        row.setCol(table.getColumn("LFT"), "" + rowSrc.getInt(tableSrc.getColumn("LFT")));
                        row.setCol(table.getColumn("RGT"), "" + rowSrc.getInt(tableSrc.getColumn("RGT")));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
            }, PATH,file.getName().replace(".xlsx", ""), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            File file = new File(AbsStragety.BOTTOM_PATH);
            for(File item: file.listFiles()) {
                String apathSrc = item.getAbsolutePath();
                String apath = AbsStragety.REPORT_PATH + item.getName();
                Log.log(apathSrc +": "+apath);
                copy(apathSrc, apath);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


