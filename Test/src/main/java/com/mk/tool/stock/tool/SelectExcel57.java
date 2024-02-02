package com.mk.tool.stock.tool;

import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.KLineUtil;
import com.mk.tool.stock.Kline;
import com.mk.tool.stock.SingleContext;
import com.mk.util.ExcelWrite2007Test;

import java.io.IOException;


public class SelectExcel57 {
    public static StringBuffer resultBuffer = new StringBuffer();


    public static void print(float[] vs, boolean abs) {
        for (float v : vs) {
            System.out.println(v);
        }
    }

    public static void filter(float[] vs, float[] vs2, boolean flag, boolean flag2) {
        for (int i = 0; i < vs.length; i++) {
            if (flag && vs[i] > 0 && flag2 && vs2[i] > 0) {
                System.out.println("" + vs[i] + " " + vs2[i]);
            }
            if (flag && vs[i] > 0 && !flag2 && vs2[i] < 0) {
                System.out.println("" + vs[i] + " " + vs2[i]);
            }
            if (!flag && vs[i] < 0 && flag2 && vs2[i] > 0) {
                System.out.println("" + vs[i] + " " + vs2[i]);
            }
            if (!flag && vs[i] < 0 && !flag2 && vs2[i] < 0) {
                System.out.println("" + vs[i] + " " + vs2[i]);
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
        System.out.println("-------------------------");
    }

    public static void printV(float[] vs) {
//        System.out.println("min:"+min(vs, false));
//        System.out.println("max:"+max(vs, false));
//        System.out.println("amin:"+min(vs, true));
//        System.out.println("amax:"+max(vs, true));

        System.out.println("fmin:" + countFlag(vs, false));

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
                    if (rowNumber == 112) {
                        int a = 0;
                    }
                    if (row.isNull()) {
                        return false;
                    }
                    if (row.getCol(table.getColumn("上月涨幅")).data.trim().equalsIgnoreCase("null")) {
                        return false;
                    }

                    String acode = row.getCol(0).data;
                    acode = acode.substring(0, acode.indexOf(" "));
                    String adate = row.getCol(3).data;
                    SingleContext singleContext = AbsStragety.getContext(acode+".txt", adate);
                    int idx = AbsStragety.getIdx(singleContext.getDays(), adate);
                    Kline kline0 = singleContext.getDays().get(idx);
                    if(kline0.hasPrevZT(12)) {
                        return false;
                    }

                    float v5 = row.getFloat(table.getColumn("大涨幅度"));
                    int aidx = row.getInt(table.getColumn("大涨Idx"));

                    float v5_ = row.getFloat(table.getColumn("大涨幅度2"));
                    int aidx_ = row.getInt(table.getColumn("大涨Idx2"));

                    float v5__ = row.getFloat(table.getColumn("大涨幅度3"));

                    int gb = row.getInt(table.getColumn("股本"));

                    if(gb>200) {
                        return false;
                    }

                    if (KLineUtil.testWithError("大涨幅度 < 5.0", null, table, rowNumber, false)) {
                        return true;
                    }
//                    if (v5>5 && v5<=7) {
//                        return true;
//                    }
//                    if (v5_>5 && v5_<=7) {
//                        return true;
//                    }
//                    if (v5__>5 && v5__<=7) {
//                        return true;
//                    }
//                    if (v2.trim().equalsIgnoreCase("true") && v3 > 2.0 && v4<0) {
//                        return true;
//                    }
                    return false;
                }
            }, "zt_bak_", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


