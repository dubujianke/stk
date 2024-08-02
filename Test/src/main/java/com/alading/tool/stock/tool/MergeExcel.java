package com.alading.tool.stock.tool;

import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.AbsStragety;
import com.alading.tool.stock.Kline;
import com.alading.tool.stock.SingleContext;
import com.alading.util.ExcelWrite2007Test;

import java.io.IOException;
import java.io.*;

public class MergeExcel {
    public static StringBuffer resultBuffer = new StringBuffer();

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean sort = true;
        String str = "a1";
        String apath = "D:\\stock\\Test\\res\\bottom\\1\\";
        java.io.File file = new java.io.File(apath);
        Table ftable = null;
        File[] fss = file.listFiles();
        for (File fs : fss) {
            String path = fs.getAbsolutePath();
            Table table = ExcelWrite2007Test.read(path);
            if (ftable == null) {
                ftable = table;
                ftable.initIndex();
            } else {
                ftable.add(table);
            }
        }
        try {
            ExcelWrite2007Test.PATH = "D:/stock/Test/res/bottom/";
            ftable.sort = sort;
            ExcelWrite2007Test.main(ftable, new Table.Filter() {
                @Override
                public boolean filter(int rowNumber, Row row) {
                    if (rowNumber == 0) {
                        return true;
                    }
                    if (row.isNull()) {
                        return false;
                    }


//                    String acode = row.getCol(0).data;
//                    acode = acode.substring(0, acode.indexOf(" "));
//                    String adate = row.getCol(3).data;
//                    SingleContext singleContext = AbsStragety.getContext(acode + ".txt", adate);
//                    singleContext.getWeeks();
//                    singleContext.getMoths();
//                    int idx = AbsStragety.getIdx(singleContext.getDays(), adate);
//                    Kline kline0 = singleContext.getDays().get(idx);
//                    if(kline0.getMax()<2.6) {
//                        return false;
//                    }


//                    String data = row.getStr("周涨幅").trim();
//                    String[] vs = data.split(" ");
//                    double v1 = Double.parseDouble(vs[0]);
//                    int v2 = Integer.parseInt(vs[1]);
//                    if (v2 < 8 && v1 > 19) {
//                        row.setCol(row.getTable().getColumn("周涨幅"), "" + vs[0]);
//                    } else {
//                        row.setCol(row.getTable().getColumn("周涨幅"), "" + 0);
//                    }

                    return true;
                }
            }, str, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
//        //System.out.println("amin:"+min(vs, true));
//        //System.out.println("amax:"+max(vs, true));

        //System.out.println("fmin:" + countFlag(vs, false));
    }


}


