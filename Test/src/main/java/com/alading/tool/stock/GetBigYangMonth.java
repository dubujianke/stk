//package com.mk.tool.stock;
//
//import com.huaien.core.util.DateUtil;
//import com.huaien.core.util.FileManager;
//
//import java.io.File;
//import java.util.*;
//
///**
// * 月线在大底部，与前低持平 1
// */
//public class GetBigYangMonth {
//
//    public static Set set = new HashSet<>();
//
//    public static boolean add(String v) {
//        boolean flag = set.contains(v);
//        if (flag) {
//            return false;
//        }
//        set.add(v);
//        return true;
//    }
//
//    public static int getIdx(List<Kline> days, String date) {
//        int i = 0;
//        for (Kline line : days) {
//            if (line.getDate().equals(date)) {
//                return i;
//            }
//            i++;
//        }
//        return 0;
//    }
//
//    public static int searchZhangting(List<Kline> days, int offset) {
//        for (int i = 0; i < 30; i++) {
//            Kline kline = days.get(offset - i);
//            if (kline.isZhanging()) {
//                return offset - i;
//            }
//        }
//        return -1;
//    }
//
//    public static List<Integer> searchDazhang30(List<Kline> days, int offset) {
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            Kline kline = days.get(offset - i);
//            if (kline.isZhanging()) {
//                list.add(offset - i);
//            }
//        }
//        return list;
//    }
//
//
//    public static int searchDazhang30_(List<Kline> days, int offset, List<Integer> list) {
//        Kline maxKline = null;
//        if (list.size() == 0) {
//            return -1;
//        }
//        for (int i = 0; i < list.size(); i++) {
//            int idx = list.get(i);
//            Kline kline = days.get(idx);
//            if (maxKline == null) {
//                maxKline = kline;
//            }
//            if (kline.contain(maxKline)) {
//                maxKline = kline;
//            }
//        }
//        return maxKline.getIdx();
//    }
//
//    public static boolean isN(List<Kline> days, int idx, int toOffset) {
//        if (idx == -1) {
//            return false;
//        }
//        Kline kline = days.get(idx);
//        if (kline.getZhangfu() < 7) {
//            return false;
//        }
//        boolean flag = true;
//        if (!kline.isWrapAfter(toOffset)) {
//            flag = false;
//        }
//        if (!kline.isVolumStepDownAfter(toOffset)) {
//            flag = false;
//        }
//
//        if (flag) {
//            double max = kline.getMaxBefore(20);
//            if (max > kline.getOpen()) {
//                if (Math.abs(max - kline.getOpen()) / kline.getOpen() * 100 > 3) {
//                    flag = false;
//                }
//            }
//        }
//        return flag;
//    }
//
//    public static double getMax(List<Kline> days, int offset, int dayNum) {
//        double max = 0;
//        for (int i = offset; i >= offset - dayNum; i--) {
//            Kline kline = days.get(i);
//            if (kline.getMax() > max) {
//                max = kline.getMax();
//            }
//        }
//        return max;
//    }
//
//    public static double compareFraction(double src, double dst) {
//        double v = 0;
//        v = 100 * (src - dst) / dst;
//        return v;
//    }
//
//
//    public static boolean isIn(double src, double v1, double v2) {
//        return src > v1 && src < v2;
//    }
//
//
////	public static boolean testCode(String file) {
////		return file.contains("688220");
////	}
//
//    public static void prsAll(String name, String code, String file, List<Kline> days, String date) {
//        try {
//            int idx = getIdx(days, date);
//            Kline kline = days.get(idx);
//            if (idx < 30) {
//                return;
//            }
//            int fromDate = days.get(0).getIdx();
//            int toDate = days.get(days.size() - 1).getIdx();
//
//            List<MonthKline> months = DateUtil.initANdGetAllMonthKLines(days, fromDate, toDate);
//            MaxSection maxSection30 = new MaxSection(30);
//            maxSection30.init(months);
//            MaxPoint latest = maxSection30.points.get(maxSection30.points.size() - 1);
//
//
//            double fraction = kline.monthKline.getZhangfu();
//            if (fraction > 45) {
//                if (add(file)) {
//                    if(code.startsWith("300")) {
//                        return;
//                    }
//                    Log.log("==========#>" + name + "    " + file + "	" + fraction + "     " + date);
//                }
//            }
//
//        } catch (Exception e) {
//
//        }
//
//    }
//
//
//    static String FILE = "D:\\new_tdx\\T0002\\export\\";
//
//    public static void prs(String file) {
//        if (file.startsWith("30")) {
//            return;
//        }
//        List<Kline> days = new ArrayList();
//        List<String> list = FileManager.readListGB(FILE + file);
//        int i = 0;
//        String name = "";
//        String code = "";
//        for (String line : list) {
//            if (i == 0) {
//                String items[] = line.split("\\s+");
//                name = items[1];
//                code = items[0];
//            }
//            i++;
//            if (i < 3) {
//                continue;
//            }
//            if (line.startsWith("数据来源:通达信")) {
//                break;
//            }
//            int idx = i - 3;
//            Kline dayLine = new Kline(line.trim());
//            dayLine.allLineList = days;
//            dayLine.setIdx(idx);
//            days.add(dayLine);
//        }
//
//        if (isTest) {
//            int LEN = 500;
//            if (days.size() < LEN) {
//                LEN = days.size() - 50;
//            }
//            for (int j = 0; j < LEN; j++) {
//                Date adate = DateUtil.stringToDate3(DATE);
//                adate = DateUtil.getBeforeDate(adate, j);
//                String dateStr = DateUtil.dateToString3(adate);
//                prsAll(name, code, file, days, dateStr);
//            }
//        } else {
//            prsAll(name, code, file, days, "2023/04/28");
//        }
//    }
//
//    static final String DATE = "2023/04/28";
//    static boolean isTest = true;
//    static boolean isTestAll = true;
//
//    public static void main(String[] args) {
//        if (isTestAll) {
//            File file = new File(FILE);
//            File fs[] = file.listFiles();
//            for (File item : fs) {
//                prs(item.getName());
//            }
//        } else {
//            prs("000957.txt");
//        }
//
//    }
//
//}
