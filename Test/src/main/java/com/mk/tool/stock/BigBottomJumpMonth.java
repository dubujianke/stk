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
//public class BigBottomJumpMonth {
//
//    public static  Set set = new HashSet<>();
//    public static boolean add(String v) {
//        boolean flag = set.contains(v);
//        if(flag) {
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
//    public static void prsAll(String file, List<Kline> days, String date) {
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
//            MaxPoint maxPoint = MaxSection.getMax(maxSection30.points);
//            MaxPoint minPoint = MaxSection.getMinPoint(maxSection30.points);
//            MaxPoint latest = maxSection30.points.get(maxSection30.points.size()-1);
//            boolean flag3 = false;
//            if(DateUtil.getDayLen(latest.kline.getDateObj(), kline.getDateObj())>1*12*30) {
//                flag3 = true;
//            }
//            MaxSection maxSection10 = new MaxSection(10);
//            maxSection10.init(months);
//            boolean flag = maxSection10.isBigBottom();
//            boolean flag2 = false;
//            if (maxPoint == null) {
//                return;
//            }
////            int fraction = (int) maxPoint.kline.beishuMaxMin(minPoint.kline);
////            if (fraction > 4) {
////                flag2 = true;
////            }
//
//            double ma5 = kline.monthKline.getMA5();
//            double ma10 = kline.monthKline.getMA10();
//            double ma20 = kline.monthKline.getMA20();
//            double ma30 = kline.monthKline.getMA30();
//            double ma60 = kline.monthKline.getMA60();
//            double ma120 = kline.monthKline.getMA120();
//            double open = kline.monthKline.getOpen();
//            double close = kline.monthKline.getClose();
//            boolean f5 = close>ma5 && open <ma5;
//            boolean f10 = close>ma10 && open <ma10;
//            boolean f20 = close>ma20 && open <ma20;
//
//            boolean f30 = close>ma30 && open <ma30;
//            boolean f60 = close>ma60 && open <ma60;
//            boolean f120 = close>ma120 && open <ma120;
//            boolean hasMa120 = kline.monthKline.hasMA120();
//            if(hasMa120) {
//                double frac = KLineUtil.compareMax(ma120, ma60);
//                if(frac>30) {
//                    if (f5 && f10 && f20 && f30 && f60 && kline.monthKline.getShitiZhenfu()>10) {
//                        if(add(file)) {
//                            Log.log("==========#>" + file + "	" + date);
//                        }
//                    }
//                }
//
//            }else {
//                if (f5 && f10 && f20 && f30 && f60 && kline.monthKline.getShitiZhenfu()>10) {
//                    if(add(file)) {
//                        Log.log("==========#>" + file + "	" + date);
//                    }
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
//        for (String line : list) {
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
//            int LEN = 100;
//            if (days.size() < LEN) {
//                LEN = days.size() - 50;
//            }
//            for (int j = 0; j < LEN; j++) {
//                Date adate = DateUtil.stringToDate3(DATE);
//                adate = DateUtil.getBeforeDate(adate, j);
//                String dateStr = DateUtil.dateToString3(adate);
//                prsAll(file, days, dateStr);
//            }
//        } else {
//            prsAll(file, days, "2023/05/31");
//        }
//    }
//
//    static final String DATE = "2023/05/31";
//    static boolean isTest = true;
//    public static void main(String[] args) {
//        if (isTest) {
//            File file = new File(FILE);
//            File fs[] = file.listFiles();
//            for (File item : fs) {
//                prs(item.getName());
//            }
//        } else {
//            prs("SH#603618.txt");
//        }
//
//    }
//
//}
