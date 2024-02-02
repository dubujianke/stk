package com.mk.tool.stock.tool;

import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.util.ExcelWrite2007Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MergeDirExcel {
    public static StringBuffer resultBuffer = new StringBuffer();

    public static List<String> getDirs(String dir) {
        List<String> dates = new ArrayList<>();
        File file = new File(dir);
        File[] fs = file.listFiles();
        for(File fle: fs) {
            if(fle.getName().startsWith("ret_2023")) {
                dates.add(fle.getAbsolutePath());
            }
        }
        return dates;
    }

    public static List<String> getDates_(String dir) {
        List<String> dates = new ArrayList<>();
        File file = new File(dir);
        File[] fs = file.listFiles();
        for(File fle: fs) {
            if(fle.getName().endsWith("_.xlsx")) {
                dates.add(fle.getName().replace(".xlsx", "").replace("ret_", ""));
            }
        }
        return dates;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean sort = true;
        String str = "a1";
        Table ftable = null;
        String dir = "D:\\stock\\Test\\res\\bottom\\";
        List<String> dirs = getDirs(dir);
        for(String absPath: dirs) {
            System.out.print("2024-");
            if (!absPath.endsWith("_2")) {
                continue;
            }
            List<String> dates = new ArrayList<>();
            dates = getDates_(absPath);
//            String path = dates.get(0);
            for(String path:dates) {
                Table table = ExcelWrite2007Test.read(absPath+"\\"+path+".xlsx");
                if (ftable == null) {
                    ftable = table;
                    ftable.initIndex();
                } else {
                    ftable.add(table);
                }
            }
        }
        try {
            ExcelWrite2007Test.PATH = "res/bottom/";
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
                    return true;
                }
            }, str, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
//        //System.out.println("amin:"+min(vs, true));
//        //System.out.println("amax:"+max(vs, true));

        //System.out.println("fmin:" + countFlag(vs, false));
    }


}


