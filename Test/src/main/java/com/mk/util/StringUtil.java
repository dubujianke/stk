package com.mk.util;

import com.mk.tool.stock.Kline;

import java.util.List;

public class StringUtil {
    public static String STR1 = "日线 前复权";
    public static String STR2 = "数据来源:通达信";

    public static int getInt(boolean flag) {
        return flag?1:0;
    }

    public static boolean isNull(String v) {
        if (v == null) {
            return true;
        }
        if (v.trim().equalsIgnoreCase("")) {
            return true;
        }
        if (v.trim().equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    public static boolean eq(String v, String v2) {
        if (v == null) {
            return true;
        }
        if (v2 == null) {
            return true;
        }
        if (v.trim().equalsIgnoreCase(v2)) {
            return true;
        }
        return false;
    }

    public static String format(double v, int len) {
        if (Math.abs(v) > 6) {
            return "N";
        }
        return String.format("%." + len + "f", v);
    }

    public static String format3(double v, int len) {
        return String.format("%." + len + "f", v);
    }

    public static String format(float v) {
        if (v > 0) {
            return String.format("+%.1f", v);
        }
        return String.format("%.1f", v);
    }

    public static String format2_(float v) {
        if (v > 0) {
            return String.format("+%.2f", v);
        }
        return String.format("%.2f", v);
    }

    public static String format(double v) {
        if (v > 0) {
            return String.format("+%.1f", v);
        }
        return String.format("%.1f", v);
    }

    public static String format2(float v) {
        return String.format("%.2f", v);
    }

    public static String format3(float v, int len) {
        return spaceString(String.format("%.2f", v), len);
    }

    public static String spaceString(String v, int len) {
        int dlt = len - v.length();
        for (int i = 0; i < dlt; i++) {
            v = " " + v;
        }
        return v;
    }

    public static String format(float v, int len) {
        return String.format("%." + len + "f", v);
    }

    public static String format4(float v, int len, int len2) {
        return spaceString(String.format("%." + len + "f", v), len2);
    }

    public static String format(int v) {
        return String.format("%02d", v);
    }

    public static String formatVs(float[] fs) {
        StringBuffer sbf = new StringBuffer();
        sbf.append("[");
        int i = 0;
        for (float v : fs) {
            i++;
            if (i > 1) {
                sbf.append(", ");
            }
            sbf.append(StringUtil.format(v));
        }
        sbf.append("]");
        return sbf.toString();
    }

    public static String formatVs(List<Kline.StandResult> fs) {
        StringBuffer sbf = new StringBuffer();
        sbf.append("[");
        int i = 0;
        for (Kline.StandResult v : fs) {
            i++;
            if (i > 1) {
                sbf.append(", ");
            }
            sbf.append("(" + v.getPeriod() + " " + StringUtil.format(v.getMai()) + ")");
        }
        sbf.append("]");
        return sbf.toString();
    }

    public static boolean isSameStandResult(List<Kline.StandResult> fs) {
        if (fs.size() == 2) {
            if (StringUtil.eq(StringUtil.format(fs.get(0).getMai()), StringUtil.format(fs.get(1).getMai()))) {
                return true;
            }
        }
        return false;
    }

    public static String formatVs2(List<Kline.StandResult> fs) {
        StringBuffer sbf = new StringBuffer();
        int i = 0;
        if (fs.size() == 2) {
            if (isSameStandResult(fs)) {
                sbf.append("(" + fs.get(0).getPeriod() + " " + StringUtil.format(fs.get(0).getMai()) + ")");
                return sbf.toString();
            }
        }
        for (Kline.StandResult v : fs) {
            i++;
            if (i > 1) {
                sbf.append(", ");
            }
            sbf.append("(" + v.getPeriod() + " " + StringUtil.format(v.getMai()) + ")");
        }
        return sbf.toString();
    }

    public static int getSmallThanCount(float v1, float v2, float v3, float v) {
        return getSmallThanCount(new float[]{v1, v2, v3}, v);
    }

    public static int getSmallThanCount(float[] vs, float v) {
        int num = 0;
        for (float item : vs) {
            if (item < v) {
                num++;
            }
        }
        return num;
    }

    public static int getSmallThanCountAbs(float[] vs, float v) {
        int num = 0;
        for (float item : vs) {
            if (Math.abs(item) < v) {
                num++;
            }
        }
        return num;
    }

}
