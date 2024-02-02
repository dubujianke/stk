package com.mk.tool.stock.tool;

import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.KLineUtil;
import com.mk.util.ExcelWrite2007Test;
import com.mk.util.StringUtil;

import java.io.IOException;


public class SelectRemove2Excel {

    public static int getCNT(String str, int v) {
        str = str.substring(1, str.length() - 1);
        String[] items = str.split(",");
        int flag = 0;
        for (String item : items) {
            if (Float.parseFloat(item) >= v) {
                flag++;
            }
        }
        return flag;
    }

    public static float[] getCNTS(String str) {
        str = str.substring(1, str.length() - 1);
        String[] items = str.split(",");
        float vs[] = new float[3];
        int idx = 0;
        for (String item : items) {
            vs[idx] = Float.parseFloat(item);
            idx++;
        }
        return vs;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = ExcelWrite2007Test.PATH + "zt_bak" + ".xlsx";
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
                    if (KLineUtil.testWithError("股本 >230 ", null, table, rowNumber, false)) {
                        return false;
                    }

                    if (KLineUtil.testWithError("大涨幅度 >= 5.0", null, table, rowNumber, false)) {
                        return false;
                    }

                    if (KLineUtil.testWithError("MIN涨幅 <-1.5 ", null, table, rowNumber, false)) {
                        int tkidx = row.getInt(table.getColumn("跳空下跌Idx"));
                        if (tkidx >= 0) {
                            return false;
                        }
                        float gb = row.getFloat(table.getColumn("月MIN涨幅"));
                        if (gb < -2.435) {
                            return false;
                        }
                        float gb2 = row.getFloat(table.getColumn("上上月涨幅"));
                        if (gb2 > 0) {
                            return false;
                        }
                        return true;
                    }
                    if (KLineUtil.testWithError("月MIN涨幅 <-10 ", null, table, rowNumber, false)) {

                        if (KLineUtil.testWithError("MIN涨幅 < -0.1 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("MIN涨幅 > 0.9 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("周Crash >= 0 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("月Crash >= 0 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("振幅 > 3.1 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("gap250 > -20 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("gap120250 < -9 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("gap120250 > -8 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        return true;
                    }
                    String str = row.getStr(table.getColumn("换手")).trim();
                    str = str.substring(1, str.length() - 1);
                    String[] items = str.split(",");
                    int flag = 0;
                    for (String item : items) {
                        if (Float.parseFloat(item) >= 2) {
                            flag++;
                        }
                    }
//                    big hole
                    if (flag > 0) {
                        //hole
                        if (KLineUtil.testWithError("30d60 < 9 & 30d60 > 0 ", null, table, rowNumber, false)) {
                            if (KLineUtil.testWithError("30d60 < 7", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("120金叉250数 > 15", null, table, rowNumber, false)) {
                                return false;
                            }
//                            return true;
                        }

                        if (KLineUtil.testWithError("上月上影线 == true & 上月z幅>10", null, table, rowNumber, false)) {
                            return false;
                        }


                        if (KLineUtil.testWithError("gap250 < 0 & gap250 < 0", null, table, rowNumber, false)) {

                            if (KLineUtil.testWithError("gap250 < -8 & gap250 > -11", null, table, rowNumber, false)) {
                                if (KLineUtil.testWithError("gap120 > -3 ", null, table, rowNumber, false)) {
                                    return false;
                                }
                            }

                            if (KLineUtil.testWithError("gap120 < -3", null, table, rowNumber, false)) {
                                if (KLineUtil.testWithError("gap250 > -5.5 ", null, table, rowNumber, false)) {
                                    return false;
                                }
                            }

                            if (KLineUtil.testWithError("120死叉250数 < 16 & 120死叉250数 >=0 ", null, table, rowNumber, false)) {
                                return false;
                            }

                            if (KLineUtil.testWithError("60金叉120数 > 5", null, table, rowNumber, false)) {
                                return false;
                            }

                            if (KLineUtil.testWithError("跳空横盘2 == true", null, table, rowNumber, false)) {
                                return false;
                            }

                            if (KLineUtil.testWithError("gap250W>0 & gap120W < 0", null, table, rowNumber, false)) {
                                return false;
                            }

                            if (KLineUtil.testWithError("gap250MCur>0 & gap250MCur<100", null, table, rowNumber, false)) {
                                return false;
                            }

                            if (KLineUtil.testWithError("zhenf(1)>7 || zhenf(0)>6", null, table, rowNumber, false)) {
                                return false;
                            }

                            if (KLineUtil.testWithError("月压力数 > 1", null, table, rowNumber, false)) {
                                return false;
                            }
                        }
                        if (flag == 2) {
                            if (KLineUtil.testWithError("振幅 > 5.6", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("30d60 < 18 & 30d60 >=0", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("gap250 > 10 & gap120 >10", null, table, rowNumber, false)) {
                                return true;
                            }
                            if (KLineUtil.testWithError("gap250 < 10 & gap120 < 10", null, table, rowNumber, false)) {
                                if (KLineUtil.testWithError("gap120 > 3", null, table, rowNumber, false)) {
                                    return false;
                                }
//                        return true;
                            }

                            if (KLineUtil.testWithError("大涨幅度 > 3 & 大涨Idx < 3", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("cur < -2 & cur > -3 &  跳空下跌Idx = -1", null, table, rowNumber, false)) {
                                return true;
                            }
                            if (KLineUtil.testWithError("cur < -1", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("跳空下跌幅度 > 6", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("上月涨幅 > 1", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("gap250MOpen < -20", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("w30_250guaili == true", null, table, rowNumber, false)) {
                                return false;
                            }
                        }
                        if (flag == 3) {
                            if (KLineUtil.testWithError("上上月上影线 == true", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("上月上影线 == true & 上月z幅>5", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("跳空下跌Crash == true", null, table, rowNumber, false)) {
                                return false;
                            }
//                        return true;
                        }
                    }


                    int flagOne = getCNT(row.getStr(table.getColumn("换手")).trim(), 1);
                    if (rowNumber == 32) {
                        int a = 0;
                    }
                    if (flagOne == 0) {
                        if (KLineUtil.testWithError(" 跳空横盘 == true", null, table, rowNumber, false) || KLineUtil.testWithError(" 跳空横盘2 == true", null, table, rowNumber, false)) {
                            if (KLineUtil.testWithError(" 振幅 < 2.5", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" 横盘MA2 != 60", null, table, rowNumber, false)) {
                                return false;
                            }
//                            return true;
                        }
                        if (KLineUtil.testWithError(" 股本 > 150", null, table, rowNumber, false)) {
                            return false;
                        }

                        if (KLineUtil.testWithError(" 30d60 < 10 & 30d60 >= 0 ", null, table, rowNumber, false)) {
                            if (KLineUtil.testWithError(" 大涨Idx < 13", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" 大涨Idx2 < 9", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" 周Crash > 0", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" gap250 < -7", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" 30d60 == 1", null, table, rowNumber, false)) {
                                return false;
                            }
                        }
                        if (KLineUtil.testWithError(" 大涨幅度 = 0", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError(" 120金叉250数 < 32 & 120金叉250数 >=0 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError(" 60金叉120数 < 18 & 60金叉120数 >=0 ", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError(" 60死叉120数 = 0", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError(" cur > 1.9", null, table, rowNumber, false)) {
                            return false;
                        }

                        //3 hand<1
                        if (KLineUtil.testWithError(" 上月换手 > 20", null, table, rowNumber, false)) {
                            if (KLineUtil.testWithError(" 上月z幅 > 15 & 上月z幅 < 25", null, table, rowNumber, false)) {
                                return false;
                            }

                            if (KLineUtil.testWithError(" MIN涨幅 > 1", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" 上月涨幅 > 0", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" gap250 < -8 &  gap250 > -11", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" gap250 < -21", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError(" gap120 < -8 &  gap120 > -11", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("月压力数 > 1", null, table, rowNumber, false)) {
                                return false;
                            }

                            String wStr = row.getStr(table.getColumn("周MINMA压力")).trim();
                            wStr = wStr.trim().substring(1, wStr.length() - 1);
                            String ws[] = wStr.split("\\s+");
                            if (ws[0].equalsIgnoreCase("250")) {
                                return false;
                            }

                            if (KLineUtil.testWithError("gap120W < -38", null, table, rowNumber, false)) {
                                return false;
                            }
                            if (KLineUtil.testWithError("gap120W > 0", null, table, rowNumber, false)) {
                                return false;
                            }
                            return true;
                        }
                    }//end flag one

                    if (flagOne == 3) {
//                    月压力
                        float hands[] = getCNTS(str);
                        String wStr2 = row.getStr(table.getColumn("月压力")).trim();
                        if (!StringUtil.isNull(wStr2)) {
                            String wss[] = wStr2.split(",");
                            boolean contain250Or120 = false;
                            for (String wStr : wss) {
                                wStr = wStr.trim();
                                wStr = wStr.trim().substring(1, wStr.length() - 1);
                                String ws[] = wStr.split("\\s+");
                                if (ws[0].equalsIgnoreCase("250")) {
                                    float v = Float.parseFloat(ws[1]);
                                    if (v > -8) {
                                        contain250Or120 = true;
                                    }
                                }
                                if (ws[0].equalsIgnoreCase("120")) {
                                    float v = Float.parseFloat(ws[1]);
                                    contain250Or120 = true;
                                }
                            }
                            if (contain250Or120) {
                                return false;
                            }
                        }

                        //周压力
                        String wStr3 = row.getStr(table.getColumn("周压力")).trim();
                        if (!StringUtil.isNull(wStr3)) {
                            String wss[] = wStr3.split(",");
                            boolean errFlag = false;
                            for (String wStr : wss) {
                                wStr = wStr.trim();
                                wStr = wStr.substring(1, wStr.length() - 1);
                                String ws[] = wStr.split("\\s+");
                                if (hands[0] < 4 && hands[2] < 4 && ws[0].equalsIgnoreCase("250")) {
                                    float v = Float.parseFloat(ws[1]);
                                    if (v > -5.0) {
                                        errFlag = true;
                                    }
                                }
                            }
                            if (errFlag) {
                                return false;
                            }
                        }

                        boolean guaili = false;
                        if (KLineUtil.testWithError("30_250guaili == true", null, table, rowNumber, false)) {
                            guaili = true;
                        }
                        if (KLineUtil.testWithError("60_120guaili == true", null, table, rowNumber, false)) {
                            guaili = true;
                        }
                        if (KLineUtil.testWithError("跳空下跌幅度 >5.4", null, table, rowNumber, false)) {
                            if (!guaili) {
                                return false;
                            }
                        }

                        if (KLineUtil.testWithError("振幅 < 1.15", null, table, rowNumber, false)) {
                            return false;
                        }

                        if (KLineUtil.testWithError("120金叉250数 < 50 & 120金叉250数 >= 0", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("跳空涨幅 < -1", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("月Crash > -1", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("横盘MA = 30", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("gap250W> 24", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("30d60 = 1", null, table, rowNumber, false)) {
                            return false;
                        }
                        if (KLineUtil.testWithError("wbottom == true", null, table, rowNumber, false)) {
                            return false;
                        }

                        //wcur
                        String wcur = row.getStr(table.getColumn("wcur")).trim();
                        if (!StringUtil.isNull(wcur)) {
                            String wcurs[] = wcur.split("/");
                            boolean contain250Or120 = false;
                            float v = Float.parseFloat(wcurs[0].trim());
                            if (v < -3.3) {
                                contain250Or120 = true;
                            }
                            if (contain250Or120) {
                                return false;
                            }
                        }

                        //wprev(1)
                        String wprev1 = row.getStr(table.getColumn("wprev(1)")).trim();
                        if (!StringUtil.isNull(wprev1)) {
                            String wcurs[] = wprev1.split("/");
                            boolean contain250Or120 = false;
                            float v = Float.parseFloat(wcurs[0].trim());
                            if (v > 6.9) {
                                contain250Or120 = true;
                            }
                            if (contain250Or120) {
                                return false;
                            }
                        }

                        //wprev(2)
                        String wprev2 = row.getStr(table.getColumn("wprev(2)")).trim();
                        if (!StringUtil.isNull(wprev2)) {
                            String wcurs[] = wprev2.split("/");
                            boolean contain250Or120 = false;
                            float v = Float.parseFloat(wcurs[0].trim());
                            if (v > 5) {
                                contain250Or120 = true;
                            }
                            if (contain250Or120) {
                                return false;
                            }
                        }


                    }


                    return true;
                }
            }, "zt_bak_", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


