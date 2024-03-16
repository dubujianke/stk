package com.mk.tool.stock.tool;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.data.GetAllBankuaiCode;
import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.report.LineReports;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.tool.stock.SingleContext;
import com.mk.util.ExcelWrite2007Test;
import com.mk.util.StringUtil;

import java.io.IOException;
import java.util.*;


public class SelectExcel {
    public static StringBuffer resultBuffer = new StringBuffer();


    public static void print(double[] vs, boolean abs) {
        for (double v : vs) {
            //System.out.println(v);
        }
    }

    public static void filter(double[] vs, double[] vs2, boolean flag, boolean flag2) {
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

    public static double min(double[] vs, boolean abs) {
        double min = Float.MAX_VALUE;
        for (double v : vs) {
            if (abs) {
                v = Math.abs(v);
            }
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public static double count(double[] vs) {
        return vs.length;
    }

    public static int countFlag(double[] vs, boolean flag) {
        int num = 0;
        for (double v : vs) {
            if (flag && v > 0) {
                num++;
            }
            if (!flag && v < 0) {
                num++;
            }
        }
        return num;
    }

    public static double minFlag(double[] vs, boolean flag, boolean abs) {
        double min = Float.MAX_VALUE;
        for (double v : vs) {
            if (flag && v >= 0) {
                if (abs) {
                    v = Math.abs(v);
                }
                if (v < min) {
                    min = v;
                }
            }
            if (!flag && v <= 0) {
                if (abs) {
                    v = Math.abs(v);
                }
                if (v < min) {
                    min = v;
                }
            }
        }
        return min;
    }

    public static double maxFlag(double[] vs, boolean flag, boolean abs) {
        double min = Float.MIN_VALUE;
        for (double v : vs) {
            if (flag && v <= 0) {
                if (abs) {
                    v = Math.abs(v);
                }
                if (v > min) {
                    min = v;
                }
            }
            if (!flag && v <= 0) {
                if (abs) {
                    v = Math.abs(v);
                }
                if (v > min) {
                    min = v;
                }
            }
        }
        return min;
    }

    public static double max(double[] vs, boolean abs) {
        double min = Float.MIN_VALUE;
        for (double v : vs) {
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
        //System.out.println("-------------------------");
    }

    public static void printV(double[] vs) {
//        //System.out.println("min:"+min(vs, false));
//        //System.out.println("max:"+max(vs, false));
//        //System.out.println("amin:"+min(vs, true));
//        //System.out.println("amax:"+max(vs, true));

        //System.out.println("fmin:" + countFlag(vs, false));

    }

    public static boolean isIn(double v, double negmin, double negmax) {
        if (v > negmax && v <= negmin) {
            return true;
        }
        return false;
    }

    public static double getMin(double v1, double v2, double v3, double v4) {
        double min = 999;
        if(v1<min) {
            min = v1;
        }
        if(v2<min) {
            min = v2;
        }
        if(v3<min) {
            min = v3;
        }
        if(v4<min) {
            min = v4;
        }
        return min;
    }

    public static boolean isRealOK(Table table, Row row) {
        if (row.getFloat(table.getColumn("gap250")) > 0) {
            return false;
        }
        double gap250 = row.getFloat(table.getColumn("gap250"));
        double gap120 = row.getFloat(table.getColumn("gap120"));
        double gap60 = row.getFloat(table.getColumn("gap60"));
        double gap30 = row.getFloat(table.getColumn("gap30"));
        boolean flag30 = isIn(gap30, -7.5f, -11);
        boolean flag60 = isIn(gap60, -7.2f, -13);
        boolean flag120 = isIn(gap120, -7.2f, -13);
        boolean flag250 = isIn(gap250, -7.2f, -13);
        boolean matchGap = false;
        if(flag30 || flag60 || flag120 || flag250) {
            matchGap = true;
        }
        if(!matchGap) {
            return false;
        }

        boolean matchGap2 = false;
        double cur = row.getFloat(table.getColumn("cur"));
        double prev1 = row.getFloat(table.getColumn("prev(1)"));
        double prev2 = row.getFloat(table.getColumn("prev(2)"));
        double prev3 = row.getFloat(table.getColumn("prev(3)"));
        if(Math.abs(cur)<1 || Math.abs(prev1)<1 || Math.abs(prev2)<1 || Math.abs(prev3)<1) {
            matchGap2 = true;
        }
        if(!matchGap2) {
            return false;
        }


        double k250 = row.getFloat(table.getColumn("k250"));
        double k120 = row.getFloat(table.getColumn("k120"));
        double k60 = row.getFloat(table.getColumn("k60"));
        double k30 = row.getFloat(table.getColumn("k30"));
        double min = getMin(k30, k60, k120, k250);
        if(min>-14.5) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = AbsStragety.BOTTOM_PATH + "ret" + ".xlsx";
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

                    boolean flag = isRealOK(table, row);
                    return flag;
                }
            }, "ret_", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


