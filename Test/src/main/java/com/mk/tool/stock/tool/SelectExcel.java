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


    public static void print(float[] vs, boolean abs) {
        for (float v : vs) {
            //System.out.println(v);
        }
    }

    public static void filter(float[] vs, float[] vs2, boolean flag, boolean flag2) {
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

    public static float min(float[] vs, boolean abs) {
        float min = Float.MAX_VALUE;
        for (float v : vs) {
            if (abs) {
                v = Math.abs(v);
            }
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public static float count(float[] vs) {
        return vs.length;
    }

    public static int countFlag(float[] vs, boolean flag) {
        int num = 0;
        for (float v : vs) {
            if (flag && v > 0) {
                num++;
            }
            if (!flag && v < 0) {
                num++;
            }
        }
        return num;
    }

    public static float minFlag(float[] vs, boolean flag, boolean abs) {
        float min = Float.MAX_VALUE;
        for (float v : vs) {
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

    public static float maxFlag(float[] vs, boolean flag, boolean abs) {
        float min = Float.MIN_VALUE;
        for (float v : vs) {
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

    public static float max(float[] vs, boolean abs) {
        float min = Float.MIN_VALUE;
        for (float v : vs) {
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

    public static void printV(float[] vs) {
//        //System.out.println("min:"+min(vs, false));
//        //System.out.println("max:"+max(vs, false));
//        //System.out.println("amin:"+min(vs, true));
//        //System.out.println("amax:"+max(vs, true));

        //System.out.println("fmin:" + countFlag(vs, false));

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String apath = AbsStragety.REPORT_PATH + "ret" + ".xlsx";
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

                    if (row.getFloat(table.getColumn("上月z幅"))>12.5) {
                        return false;
                    }
                    if (row.getFloat(table.getColumn("上上月z幅"))>15) {
                        return false;
                    }


                    return true;
                }
            }, "ret_", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


