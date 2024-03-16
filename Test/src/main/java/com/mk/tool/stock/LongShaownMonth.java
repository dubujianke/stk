package com.mk.tool.stock;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;

public class LongShaownMonth {

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

    public static List<Integer> searchDazhang30(List<Kline> days, int offset) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Kline kline = days.get(offset - i);
            if (kline.isZhanging()) {
                list.add(offset - i);
            }
        }
        return list;
    }


    public static int searchDazhang30_(List<Kline> days, int offset, List<Integer> list) {
        Kline maxKline = null;
        if (list.size() == 0) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            int idx = list.get(i);
            Kline kline = days.get(idx);
            if (maxKline == null) {
                maxKline = kline;
            }
            if (kline.contain(maxKline)) {
                maxKline = kline;
            }
        }
        return maxKline.getIdx();
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


//	public static boolean testCode(String file) {
//		return file.contains("688220");
//	}

    public static void prsAll(String file, List<Kline> days, String date) {
        int idx = getIdx(days, date);
        Kline kline = days.get(idx);
        if (idx < 30) {
            return;
        }
        int fromDate = days.get(0).getIdx();
        int toDate = days.get(days.size() - 1).getIdx();

        List<MonthKline> months =DateUtil.initANdGetAllMonthKLines(days, fromDate, toDate);
        List<Weekline> weeks = DateUtil.initANdGetAllWeekKLines(days, date, toDate);
        Weekline last = weeks.get(weeks.size()-1);
        for(int i=0; i<5; i++) {
            Log.log("CLOSE:"+weeks.get(weeks.size()-1-i).key+" "+ weeks.get(weeks.size() - 1 - i).getClose());
        }
        double ma30 = last.getMA60();
        double zhangfu = last.getZhangfu();
        double zf = last.getZhenfu();
        boolean flag = kline.isShadownMonthDownNoUp();
        double mma120 = kline.monthKline.getMA120();
        if (flag && kline.monthKline.getOpen() > mma120) {
            Log.log("==========#>" + file + "	" + date);
        }


    }

    static boolean isTest = false;
    static String FILE = "D:\\new_tdx\\T0002\\export\\";

    public static void prs(String file) {
        if (file.startsWith("30")) {
            return;
        }
        List<Kline> days = new ArrayList();
        List<String> list = FileManager.readListGB(FILE + file);
        int i = 0;
        for (String line : list) {
            i++;
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

        if (isTest) {
            int LEN = 100;
            if (days.size() < LEN) {
                LEN = days.size() - 50;
            }
            for (int j = 0; j < LEN; j++) {
                Date adate = DateUtil.stringToDate3(DATE);
                adate = DateUtil.getBeforeDate(adate, j);
                String dateStr = DateUtil.dateToString3(adate);
                prsAll(file, days, dateStr);
            }
        } else {
            prsAll(file, days, "2023/05/19");
        }
    }

    static final String DATE = "2023/05/19";

    public static void main(String[] args) {
        if (isTest) {
            File file = new File(FILE);
            File fs[] = file.listFiles();
            for (File item : fs) {
                prs(item.getName());
            }
        } else {
            prs("SH#600345.txt");
        }

    }

}
