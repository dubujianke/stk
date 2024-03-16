package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.data.GetSanhu;
import com.mk.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SanhuNumStragety {

    public static int getIdx(List<Kline> days, String date) {
        int i = 0;
        for (Kline line : days) {
            if (line.getDate().equals(date)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public static int searchZhangting(List<Kline> days, int offset) {
        for (int i = 0; i < 30; i++) {
            Kline kline = days.get(offset - i);
            if (kline.isZhanging()) {
                return offset - i;
            }
        }
        return -1;
    }


    public static boolean isN(List<Kline> days, int idx, int toOffset) {
        if (idx == -1) {
            return false;
        }
        Kline kline = days.get(idx);
        if (kline.getZhangfu() < 7) {
            return false;
        }
        boolean flag = true;
        if (!kline.isWrapAfter(toOffset)) {
            flag = false;
        }
        if (!kline.isVolumStepDownAfter(toOffset)) {
            flag = false;
        }

        if (flag) {
            double max = kline.getMaxBefore(20);
            if (max > kline.getOpen()) {
                if (Math.abs(max - kline.getOpen()) / kline.getOpen() * 100 > 3) {
                    flag = false;
                }
            }
        }
        return flag;
    }


    public static double getMax(List<Kline> days, int offset, int dayNum) {
        double max = 0;
        for (int i = offset; i >= offset - dayNum; i--) {
            Kline kline = days.get(i);
            if (kline.getMax() > max) {
                max = kline.getMax();
            }
        }
        return max;
    }

    public static double compareFraction(double src, double dst) {
        double v = 0;
        v = 100 * (src - dst) / dst;
        return v;
    }


    public static boolean isIn(double src, double v1, double v2) {
        return src > v1 && src < v2;
    }


    public static void prsIsN(String file, List<Kline> days, String date) {
        if (file.contains("000063")) {
            int a = 0;
        }
        String endDate = date.split("/")[2];
        boolean fflag = false;
        prsIsN1(file, days, date);
    }


    static boolean isST() {
        return name.contains("ST");
    }

    static Set<String> set = new HashSet<>();
    static String FILE = "D:\\new_tdx\\T0002\\export\\";
    //      static String FILE = "D:\\new_tdx\\T0001\\export\\";
    static String name;

    public static void prs(String file, String singleDate) {
        SINGLE_DATE = singleDate;
        if (file.startsWith("30")) {
            return;
        }
        List<Kline> days = new ArrayList();
        List<String> list = FileManager.readListGB(FILE + file);
        int i = 0;
        for (String line : list) {
            i++;
            if (i == 1) {
                name = line.split("\\s+")[1];
            }
            if (i < 3) {
                continue;
            }
            if (line.startsWith("数据来源:通达信")) {
                break;
            }
            int idx = i - 3;
            Kline dayLine = new Kline(line.trim());
            dayLine.allLineList = days;
            dayLine.setIdx(idx);
            days.add(dayLine);
        }
        int LEN = 1;
        if (isTest) {
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < LEN; j++) {
                Date adate = DateUtil.stringToDate3(DATE);
                adate = DateUtil.getBeforeDate(adate, j);
                String dateStr = DateUtil.dateToString3(adate);
                prsIsN(file, days, dateStr);
            }
        } else {
            if (isSingle) {
                prsIsN(file, days, SINGLE_DATE);
            } else {
                if (days.size() < LEN) {
                    LEN = days.size() - 50;
                }
                for (int j = 0; j < LEN; j++) {
                    Date adate = DateUtil.stringToDate3(DATE);
                    adate = DateUtil.getBeforeDate(adate, j);
                    String dateStr = DateUtil.dateToString3(adate);
                    prsIsN(file, days, dateStr);
                }
            }
        }
    }


    static final String DATE = "2023/06/16";
    static String SINGLE_DATE = "";
    static boolean isSingle = false;
    static boolean isTest = true;

    public static void mainProcess(String code, String date) {
        if (!StringUtil.isNull(date)) {
            if (date.equalsIgnoreCase("-")) {
                isTest = false;
                isSingle = false;
            } else {
                isTest = false;
                isSingle = true;
            }

        } else {
            isTest = true;
            isSingle = false;
        }

        if (isTest) {
            File file = new File(FILE);
            File fs[] = file.listFiles();
            for (File item : fs) {
                prs(item.getName(), DATE);
            }
        } else {
            prs(code, date);//2023/01/30
        }
    }

    public static void prsIsN1(String file, List<Kline> days, String date) {
        if (set.contains(file)) {
            return;
        }
        int idx = getIdx(days, date);
        if (isST()) {
            return;
        }
        GetSanhu.DV dv = null;
        try {
            dv = GetSanhu.get(Kline.getCode(file), 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dv == null) {
            return;
        }
        double ret = dv.v;
        if (ret <= -6) {
            Log.log(Kline.getCode(file) + "   " + name + "    " + date + " " + ret);
        }
    }

    public static void main(String[] args) {
//        mainProcess("SH#600113.txt", "2023/03/21");
//        mainProcess("000063.txt", "");
        mainProcess("000628.txt", " 2023/03/31");
    }

}
