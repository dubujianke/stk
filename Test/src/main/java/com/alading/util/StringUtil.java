package com.alading.util;

import com.alading.tool.stock.Kline;

import java.text.DecimalFormat;
import java.util.List;

public class StringUtil {
    public static String STR1 = "日线 前复权";
    public static String STR2 = "数据来源:通达信";

    public static int getInt(boolean flag) {
        return flag ? 1 : 0;
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
//        if (Math.abs(v) > 6) {
//            return "N";
//        }
        return String.format("%." + len + "f", v);
    }

    public static String format3(double v, int len) {
        return String.format("%." + len + "f", v);
    }

    public static String format(double v) {
        if (v > 0) {
            return String.format("+%.2f", v);
        }
        return String.format("%.2f", v);
    }

    public static String format2_(double v) {
        if (v > 0) {
            return String.format("+%.2f", v);
        }
        return String.format("%.2f", v);
    }

//    public static String format(double v) {
//        if (v > 0) {
//            return String.format("+%.1f", v);
//        }
//        return String.format("%.1f", v);
//    }

    public static String format2(double v) {
        return String.format("%.2f", v);
    }

//    public static String format3(double v, int len) {
//        return spaceString(String.format("%.2f", v), len);
//    }

    public static String spaceString(String v, int len) {
        int dlt = len - v.length();
        for (int i = 0; i < dlt; i++) {
            v = " " + v;
        }
        return v;
    }

//    public static String format(double v, int len) {
//        return String.format("%." + len + "f", v);
//    }

    public static String format4(double v, int len, int len2) {
        return spaceString(String.format("%." + len + "f", v), len2);
    }

    public static String format(int v) {
        return String.format("%02d", v);
    }

    public static String formatVs(double[] fs) {
        StringBuffer sbf = new StringBuffer();
        sbf.append("[");
        int i = 0;
        for (double v : fs) {
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

    public static int getSmallThanCount(double v1, double v2, double v3, double v) {
        return getSmallThanCount(new double[]{v1, v2, v3}, v);
    }

    public static int getSmallThanCount(double[] vs, double v) {
        int num = 0;
        for (double item : vs) {
            if (item < v) {
                num++;
            }
        }
        return num;
    }

    public static int getSmallThanCountAbs(double[] vs, double v) {
        int num = 0;
        for (double item : vs) {
            if (Math.abs(item) < v) {
                num++;
            }
        }
        return num;
    }

    public static void sleep(int v) {
        try {
            Thread.currentThread().sleep(v);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static double toFix(double v) {
        if(v==Double.POSITIVE_INFINITY) {
            return 10000;
        }
        if(v==Double.NEGATIVE_INFINITY) {
            return -10000;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String retStr = String.format("%.2f", v);
        if(retStr.contains("N")) {
            return 100000;
        }
        return Double.parseDouble(retStr);
    }

    public static double toFix4(double v) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(String.format("%.4f", v));
    }

    public static String getCNFromU(String unicodeString) {
        StringBuffer stringBuffer = new StringBuffer();
        String chineseString = unicodeString.replace("\\u", "").replace("", "");
        for (int i = 0; i < chineseString.length(); i++) {
            char ch = (char) Integer.parseInt(chineseString.substring(i, i + 4), 16);
            stringBuffer.append(ch);
            System.out.print(ch);
        }
        return stringBuffer.toString();
    }
}
