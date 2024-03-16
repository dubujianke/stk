package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.mk.model.Table;
import com.mk.util.StringUtil;

import java.util.*;

public class KLineUtil {

    //idx>=1
    public static Weekline prevWeekline(Kline kline0, int idx) {
        Weekline prev1Week = null;
        int aweekIdx = kline0.getWeekDay();
        prev1Week = kline0.weekline;
        if (aweekIdx == 5) {
            prev1Week = prev1Week.prev(idx-1);
        } else {
            prev1Week = prev1Week.prev(idx);
        }
        return prev1Week;
    }



    public static Kline getMaxBetween(List<Kline> days, int idx, int idx2) {
        int maxIdx = 0;
        double max = 0;
        for (int i = idx; i < idx2; i++) {
            Kline kline = days.get(i);
            if (kline.getMax() > max) {
                max = kline.getMax();
                maxIdx = i;
            }
        }
        return days.get(maxIdx);
    }

    public static Kline getMinBetween(List<Kline> days, int idx, int idx2) {
        int aidx = 0;
        double min = 99999;
        for (int i = idx; i < idx2; i++) {
            Kline kline = days.get(i);
            if (kline.getMin() < min) {
                min = kline.getMin();
                aidx = i;
            }
        }
        return days.get(aidx);
    }

    public static double compareMax(double v1, double v2) {
        if (Math.abs(v2) < 0.01) {
            return 0.0001f;
        }
        return Math.abs(100.0f * (v1 - v2) / v2);
    }

    public static boolean prevIsDouble(MonthKline monthKline) {
        boolean flag = false;
        MonthKline prev = monthKline.prev();
        MonthKline prev2 = prev.prev();
        double frac = 70;
        if (prev.isShadownUp(frac) && prev2.isShadownDown(frac)) {
            return true;
        }
        if (prev2.isShadownUp(frac) && prev.isShadownDown(frac)) {
            return true;
        }
        return false;
    }

    public static double compareMaxSign5(double v1, double v2) {
        return Grid.BEISHU * v1 / v2;
    }


    public static double compareMaxSign(double v1, double v2) {
        return 100.0f * (v1 - v2) / v2;
    }

    public static double compareMaxSign(double v1, double v2, double basse) {
        return 100.0f * (v1 - v2) / basse;
    }

    public static String compareMaxSignStr(double v1, double v2, double basse) {
        return StringUtil.spaceString(StringUtil.format(compareMaxSign(v1, v2, basse), 1), 6);
    }


    public static double getMin(double v1, double v2, double v3, double v4) {
        double min = 999;
        if (v1 > 0 && v1 < min) {
            min = v1;
        }
        if (v2 > 0 && v2 < min) {
            min = v2;
        }
        if (v3 > 0 && v3 < min) {
            min = v3;
        }
        if (v4 > 0 && v4 < min) {
            min = v4;
        }
        return min;
    }


    public static double compareMaxSign3(double v1, double v2) {
        return 100.0f * (v2 - v1) / v1;
    }


    public static double compareMaxSign2(double v1, double v2) {
        return 100.0f * (v1 - v2) / v1;
    }

    public static double compareMaxSign1(double v1, double v2) {
        return 100.0f * (v1) / v2;
    }


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

    public static Kline getLineByDate(List<Kline> days, String date) {
        int i = 0;
        for (Kline line : days) {
            if (line.getDate().equals(date)) {
                return line;
            }
            i++;
        }
        return null;
    }

    public static Kline getMonthLineByDate(List<Kline> days, String date) {
        int i = 0;
        String mDateStr = DateUtil.dateToStringYM(DateUtil.strToDate4(date));
        for (Kline line : days) {
            if (line.getMonthDate().equals(mDateStr)) {
                return line;
            }
            i++;
        }
        return null;
    }

    public static int getKlineIdxFromDate(List<Kline> days, String dateStr) {
        for (int i = 0; i < days.size(); i++) {
            Kline kline = days.get(i);
            Date cmpDate = DateUtil.stringToDate3(dateStr);
            Date date = kline.getDateObj();
            if (date.getTime() > cmpDate.getTime()) {
                return i;
            }
        }
        return 0;
    }

    public static Kline getMinKlineIdxFromDate(List<Kline> days, String dateStr) {
        int idx = getKlineIdxFromDate(days, dateStr);
        Kline kline = getMinBetween(days, idx, days.size() - 1);
        return kline;
    }

    public static Kline getMinKlineIdxFromDate(List<Kline> days) {
        String dateStr = "2016/01/01";
        int idx = getKlineIdxFromDate(days, dateStr);
        Kline kline = getMinBetween(days, idx, days.size() - 1);
        return kline;
    }


    public static List<Kline> reverse(List<Kline> lines) {
        List<Kline> days = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            days.add(lines.get(lines.size() - 1 - i));
        }
        return days;
    }

    public static List<Kline> removeInterscction(List<Kline> days, List<Kline> endList) {
        List<Kline> removeList = new ArrayList<>();
        int len = days.size();
        Kline zhangting = endList.get(0);
        if (!zhangting.isZhanging()) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            Kline kline = days.get(i);
            if (zhangting.isIntersection(kline)) {
                removeList.add(kline);
            }
        }
        return removeList;
    }

    public static List<Integer> searchDazhang30(List<Kline> days, int offset) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 70; i++) {
            Kline kline = days.get(offset - i);
            if (kline.isZhanging()) {
                list.add(offset - i);
            }
        }
        return list;
    }

    public static List<Kline> searchDazhang30Kline2(List<Kline> days, int offset) {
        List<Kline> list = new ArrayList<>();
        for (int i = 0; i < 70; i++) {
            Kline kline = days.get(offset - i);
            if (kline.getDate().equalsIgnoreCase("2022/11/03")) {
                int a = 0;
            }
            if (kline.isZhanging(6)) {
                if (kline.getMax() < kline.getMA120() && KLineUtil.compareMax(kline.getMax(), kline.getMA120()) > 1) {
                    break;
                }
                list.add(kline);
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

    public static void sortAsc(List<Kline> klines) {
        klines.sort(new Comparator<Kline>() {
            @Override
            public int compare(Kline o1, Kline o2) {
                return (int) (o1.getDateObj().getTime() - o2.getDateObj().getTime());
            }
        });
    }

    public static List<Kline> searchDazhang30Kline(List<Kline> days, int offset) {
        List<Kline> list = new ArrayList<>();
        int flagCount = 0;
        int flagIndex = 0;
        boolean FLAG = false;
        boolean FLAG2 = false;
        for (int i = 0; i < 70; i++) {
            Kline kline = days.get(offset - i);
            if (kline.getDate().equalsIgnoreCase("2022/11/03")) {
                int a = 0;
            }
            if (kline.getMin() > kline.getMA120() && KLineUtil.compareMax(kline.getMin(), kline.getMA120()) > 0.5) {
                if (kline.getIdx() == flagIndex - 1) {
                    flagCount++;
                    flagIndex = kline.getIdx();
                } else {
                    flagCount = 0;
                    flagIndex = kline.getIdx();
                }
            }
            if (flagCount >= 5) {
                FLAG = true;
            }

            if (FLAG) {
                if (kline.getZhenfu() > 3) {
                    if (kline.getMax() < kline.getMA120() && KLineUtil.compareMax(kline.getMax(), kline.getMA120()) > 1) {
                        FLAG2 = true;
                        break;
                    }
                    list.add(kline);
                }
            } else {
                if (kline.getZhenfu() > 3) {
                    list.add(kline);
                }
            }
        }
        if (!FLAG2) {
            return new ArrayList<>();
        }
        return list;
    }

    public static void sortAescWeekline(List<Weekline> klines) {
        klines.sort(new Comparator<Weekline>() {
            @Override
            public int compare(Weekline o1, Weekline o2) {
                if (o1 == null || o2 == null) {
                    int a = 0;
                    a++;
                }
                return (int) ((int) 100 * (o1.getMin() - o2.getMin()));
            }
        });
    }

    public static void sortAescMonthline(List<MonthKline> klines) {
        klines.sort(new Comparator<MonthKline>() {
            @Override
            public int compare(MonthKline o1, MonthKline o2) {
                if (o1 == null || o2 == null) {
                    int a = 0;
                    a++;
                }
                return (int) ((int) 100 * (o1.getMin() - o2.getMin()));
            }
        });
    }

    public static void sortAescMonthlinePoint(List<MaxPoint> klines) {
        klines.sort(new Comparator<MaxPoint>() {
            @Override
            public int compare(MaxPoint o1, MaxPoint o2) {
                if (o1 == null || o2 == null) {
                    int a = 0;
                    a++;
                }
                return (int) ((int) 100 * (o1.kline.getMin() - o2.kline.getMin()));
            }
        });
    }

    public static void sortDescMonthlinePoint(List<MaxPoint> klines) {
        klines.sort(new Comparator<MaxPoint>() {
            @Override
            public int compare(MaxPoint o1, MaxPoint o2) {
                if (o1 == null || o2 == null) {
                    int a = 0;
                    a++;
                }
                return (int) ((int) 100 * -(o1.kline.getMin() - o2.kline.getMin()));
            }
        });
    }


    public static void sortDescWeekline(List<Weekline> klines) {
        klines.sort(new Comparator<Weekline>() {
            @Override
            public int compare(Weekline o1, Weekline o2) {
                return (int) ((o2.getDateObj().getTime() - o1.getDateObj().getTime()) / (24 * 1000 * 3600));
            }
        });
    }

    public static void sortDesc(List<Kline> klines) {
        klines.sort(new Comparator<Kline>() {
            @Override
            public int compare(Kline o1, Kline o2) {
                return (int) ((o2.getDateObj().getTime() - o1.getDateObj().getTime()) / (24 * 1000 * 3600));
            }
        });
    }

    public static double getHigher(double v, double fr) {
        return v * (100 + fr) / 100.0f;
    }

    public static double min(double v1, double v2) {
        if (v1 > v2) {
            return v2;
        }
        return v1;
    }

    public static boolean abousNearest(double v1, double v2, double vv) {
        double f = Math.abs(100.0f * (v1 - v2) / v2);
        if (f <= vv) {
            return true;
        }
        return false;
    }

    public static boolean isBetween(int from, int to, List<Weekline> list, double min, double max) {
        for (int i = from + 1; i < to; i++) {
            Weekline weekline = list.get(i);
            if (!weekline.touchEntity(min, max)) {
                return false;
            }
        }
        return true;
    }

    public static List<Weekline> copy(List<Weekline> klines) {
        List<Weekline> copy = new ArrayList<>();
        copy.addAll(klines);
        return copy;
    }

    static Weekline getRecent(Weekline w1, Weekline w2) {
        if (w1.getIdx() > w2.getIdx()) {
            return w1;
        }
        return w2;
    }

    static int getLen(Weekline w1, Weekline w2) {
        return Math.abs(w1.getIdx() - w2.getIdx());
    }

    static class Bottom {
        int idx;
        double value;
        Weekline line1;
        Weekline line2;

    }

    public static int getTochCnt(List<MaxPoint> points, int idx) {
        int cnt = 0;
        for (int i = idx; i < points.size(); i++) {
            MaxPoint weekline = points.get(i);
            Weekline item = (Weekline) weekline.kline;
            if (weekline.flag2 == 0) {
                cnt++;
            }
        }
        return cnt;
    }

    public static double prevAndMa30(Kline kline) {
        Kline item = kline.prev();
        double frac = KLineUtil.compareMax(item.getOpen(), item.getMA30());
        return frac;
    }

    public static double prevNZhangfu(Kline kline, int n) {
        n = n - 1;
        double max = kline.getClose();
        double min = kline.prev().getClose();
        for (int i = 0; i < n; i++) {
            Kline item = kline.prev(i + 1);
            if (item.getZhangfu() < 0) {
                break;
            }
            min = item.prev().getClose();
        }
        double frac = KLineUtil.compareMax(max, min);
        return frac;
    }

    public static double prevNZhangfu(Kline kline, int n, boolean isFirstMax) {
        n = n - 1;
        double max = kline.getClose();
        if (isFirstMax) {
            max = kline.getMax();
        }
        double min = kline.prev().getClose();
        for (int i = 0; i < n; i++) {
            Kline item = kline.prev(i + 1);
            if (item.getZhangfu() < 0) {
                break;
            }
            min = item.prev().getClose();
        }
        double frac = KLineUtil.compareMax(max, min);
        return frac;
    }

    public static double prevNZhangfu2(Kline kline, int n) {
        n = n - 1;
        double max = kline.getClose();
        double min = kline.prev().getClose();
        for (int i = 0; i < n; i++) {
            Kline item = kline.prev(i + 1);
            if (item.getZhangfu() < 0) {
                continue;
            }
            if (item.getClose() < min) {
                min = item.getClose();
            }
        }
        double frac = KLineUtil.compareMax(max, min);
        return frac;
    }

    public static Kline nextNTouchMA10(Kline kline, int n) {
        double min = kline.getClose();
        boolean flag = false;
        for (int i = 0; i < n; i++) {
            Kline item = kline.next(i + 1);
            min = item.getClose();
            if (i == 0) {

            } else {
                if (item.touch(item.getMA10(), 0.5f)) {
                    return item;
                }
            }

        }
        for (int i = 0; i < n; i++) {
            Kline item = kline.next(i + 1);
            min = item.getClose();
            if (item.touch(item.getMA10(), 2.1f)) {
                return item;
            }
        }
        return null;
    }


    public static boolean standOn(Kline kline, double ma, double upFrac) {
        double vma = ma;
        double max = kline.getMax();
        double min = kline.getMin();
        double upF = KLineUtil.compareMaxSign(max, vma);
        double downF = KLineUtil.compareMaxSign(min, vma);
        double downFAbs = KLineUtil.compareMax(min, vma);
        if (downFAbs < 1) {
            return false;
        }
        if (kline.getZhenfu() < 3) {
            return false;
        }
        if (kline.getClose() >= ma && kline.getOpen() <= ma) {
            return true;
        }
        if (kline.getZhangfu() > 2.3 && kline.touch(ma)) {
            return true;
        }
        return false;
    }

    public static double getMAX(double a, double b, double c) {
        double max = 0;
        if (a > max) {
            max = a;
        }
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }
        return max;
    }

    public static double getMIN(double a, double b, double c) {
        double v = 99999;
        if (a < v) {
            v = a;
        }
        if (b < v) {
            v = b;
        }
        if (c < v) {
            v = c;
        }
        return v;
    }

    public static double getMaxSpace(double a, double b, double c) {
        double v1 = KLineUtil.compareMax(a, b);
        double v2 = KLineUtil.compareMax(a, c);
        double v3 = KLineUtil.compareMax(c, b);
        return getMAX(v1, v2, v3);
    }

    public static boolean isExist(List<Kline> list, int from, int to, double zf) {
        for (int i = from; i <= to; i++) {
            Kline item = list.get(i);
            if (item.getZhangfu() > zf) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExistNoShadow(List<Kline> list, int from, int to, double zf) {
        for (int i = from; i <= to; i++) {
            Kline item = list.get(i);
            boolean isShadow = item.isShadownDown(60) || item.isShadownUp(60);
            double zfv = item.getZhangfu();
            if (zfv > zf && !isShadow) {
                return true;
            }
        }
        return false;
    }

    public static Kline standOnAndNextNFallDown(Kline kline, double ma, int ma2, int n, double upFrac) {
        boolean flag = standOn(kline, ma, upFrac);
        if (!flag) {
            return null;
        }
        int num = 0;
        for (int i = 0; i < n; i++) {
            Kline item = kline.next(i + 1);
            if (item == null) {
                break;
            }
            boolean flag2 = item.touch(item.getMAI(ma2));
            num++;
            if (flag2) {
                if (num > 5) {
                    double ma10 = item.getMA10();
                    double ma30 = item.getMA30();
                    double ma60 = item.getMA60();
                    double mSpace = getMaxSpace(ma10, ma30, ma60);

                    double touchSpace = item.getTouch(ma60);
                    return item;
                }
            }
        }

        return null;
    }

    public static double standOnSpance(Kline kline, double ma) {
        double vma = ma;
        double min = kline.getMin();
        double downFAbs = KLineUtil.compareMax(min, vma);
        return downFAbs;
    }

    public static boolean isMonthBottom(MonthKline monthKline, double v) {
        double min = monthKline.getMin();
        double ma60 = monthKline.getMA60();
        double ma120 = monthKline.getMA120();
        double frac60 = compareMax(min, ma60);
        double frac120 = compareMax(min, ma120);
        double frac = min(frac60, frac120);
        if (frac > v) {
            return false;
        }
        return true;
    }

    public static boolean isRecentMin(Kline kline, int num) {
        int monNum = 50;
        List<Kline> recents = new ArrayList<>();
        for (int i = 0; i < monNum; i++) {
            recents.add(kline.prev(i));
        }
        KLineUtil.sortDesc(recents);
        double min = Integer.MAX_VALUE;
        int minIdx = 0;
        for (int i = 0; i < num; i++) {
            Kline item = kline.prev(i);
            if (item.getMin() < min) {
                min = item.getMin();
                minIdx = item.getIdx();
            }
        }
        if (minIdx == kline.getIdx()) {
            return true;
        } else {
            double tmp = KLineUtil.compareMax(min, kline.getMin());
            if (num <= 4 && tmp < 2) {
                return true;
            }
            if (num < 20 && tmp < 11.5) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPrevIsShadow(Kline kline, int num) {
        double min = Integer.MAX_VALUE;
        int minIdx = 0;
        Kline item = kline.prev(1);
        if (item.getZhenfu() > 0 && item.isShadownDown(70)) {
            return true;
        }
        return false;
    }




    public static boolean isRecentBottom(String code, String date, Kline kline, List<Kline> weeks, List<Kline> moths) {
        boolean flag = false;
        int offsetLen = 0;
        if (moths != null) {
            int monIdx = kline.getIdx();
            Kline current = moths.get(kline.getIdx());
            MaxSection maxSection60 = new MaxSection(30);
            maxSection60.initDay(moths, 0, monIdx);
            List<MaxPoint> pointsAll = maxSection60.points;

            List<MaxPoint> points = maxSection60.getRange(monIdx - 60, monIdx);
            KLineUtil.sortAescMonthlinePoint(points);
            if (points.size() == 0) {
                return flag;
            }

            Kline minMonth = (Kline) points.get(0).kline;
            if (monIdx == minMonth.getIdx()) {
                flag = true;
            }
            if (points.size() > 1) {
                Kline minMonth2 = (Kline) points.get(1).kline;
                offsetLen = Math.abs(minMonth2.getIdx() - minMonth.getIdx() + 1);
                if (offsetLen > 12) {
                    flag = true;
                }
            }
//            if(points.size()>1) {
//                Kline minMonth2 = (Kline) points.get(1).kline;
//                if(monIdx == minMonth2.idx) {
//                    double frac = KLineUtil.compareMax(minMonth.min, minMonth2.min);
//                    if(frac<10) {
//                        flag = true;
//                    }
//                    if(minMonth2.touch(minMonth2.getMA60())) {
//                        flag = true;
//                    }
//                    double frac2 = KLineUtil.compareMax(minMonth2.min, minMonth2.getMA60());
//                    if(minMonth2.min<minMonth2.getMA60() && frac2<18) {
//                        flag = true;
//                    }
//
//                    if(minMonth2.idx > minMonth.idx) {
//                        double tfrac = KLineUtil.compareMax(minMonth.min, minMonth2.min);
//                        if(frac<20) {
//                            flag = true;
//                        }
//                    }
//                }
//            }
        }
        return flag;
    }


    public static int getNIsDownMAI(Kline kline, int n) {
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            Kline item = kline.prev(i);
            if (item.isTouchOnMAI(30)) {
                cnt++;
            }
        }
        return cnt;
    }


    public static int getDateNum(String weekDate) {
        String str = weekDate.substring(8);
        return Integer.parseInt(str);
    }

    public static boolean isFirstWeek(String date) {
        int num = getDateNum(date);
        return num < 10;
    }

    public static final String flag_cross = "" +
            "dopen < dma120 & dclose > dma120 && dopen < dma250 & dclose < dma250";

    public static String getCrossMA(int i) {
        return String.format("dopen < dma%d & dclose > dma%d", i);
    }

    public static final String upcrossbottom = "day upcrossbottom dma120";
    public static final String upcrosstop60 = "day upcrosstop dma60";

    public static final String space = "space(dmax, dma250) > 8";
    public static final String space2 = "day upcrosstop dma120";

    public static final String wupcrossbottom = "week upcrossbottom dma120";

    public static final String wupcrosstop250 = "week upcrosstop dma250";
    public static final String wupcrosstop120 = "week upcrosstop dma120";

    public static final String dmax_dma250_space = "((dma250 - dmax)/dmax*100) >7 & ((dma250 - dmax)/dmax*100) < 18";

    public static final String flag1 = "" +
            "dopen < dma120 & dclose > dma120 && dopen < dma250 & dclose < dma250  && " +
            "wopen > wma120 & wclose > wma120 && wopen > wma250 & wclose > wma250";
    public static final String w_30_60__120_250 = "" +
            "wopen > wma120 & wclose > wma120 && wopen > wma250 & wclose > wma250 && " +
            "wopen < wma30 & wclose < wma30 && wopen < wma60 & wclose < wma60";

    public static final String flag3 = "" +
            "wopen > wma120 & wclose > wma120 && wopen > wma250 & wclose > wma250 && wopen < wma30";

    public static final int ZERO = 0;//
    public static final int ZERO_MA250 = 0;
    public static final int ZERO_MA120 = 101;
    public static final int ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_UPENTITY_LARGER = 1;//
    public static final int ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_DOWNENTITY_LARGER = 2;//
    public static final int ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_ENTITY_EQUAL = 3;//
    public static final int ZERO_BOTTOM_STANDMA120_TOP_DOWNMA250_SPACE_LARGER = 4;//
    public static final int ZERO_BOTTOM_STANDMA120_TOP_DOWNMA250_SPACE_SMALLER = 5;//
    public static final int ZERO_STAND_MA250 = 6;//
    public static final int ZERO_STAND_MA250_UPENTITY_LARGER = 8;
    public static final int ZERO_STAND_MA250_DOWNENTITY_LARGER = 9;

    public static final int DOWNMA250_DOWN_MA120LARGER = 100;
    public static final int DOWNMA250_DOWN_MA120SMALLER = 11;

    public static final int UP_MA120_DOWN_MA250LARGER = 12;
    public static final int UP_MA120_DOWN_MA250SMALLER = 13;

    public static final int ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_UPENTITY_LARGER = 21;//
    public static final int ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_DOWNENTITY_LARGER = 22;//
    public static final int ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_ENTITY_EQUAL = 23;//

    public static final int ZERO_BOTTOM_STANDMA250_TOP_DOWNMA120_SPACE_LARGER = 24;//
    public static final int ZERO_BOTTOM_STANDMA250_TOP_DOWNMA120_SPACE_SMALLER = 25;//

    public static final int ZERO_STAND_MA120 = 26;//

    public static final int ZERO_STAND_MA120_UPENTITY_LARGER = 28;
    public static final int ZERO_STAND_MA120_DOWNENTITY_LARGER = 29;

    public static final int DOWNMA120_DOWNMA250LARGER = 30;
    public static final int DOWNMA120_DOWNMA250SMALLER = 31;
    public static final int UPMA250_DOWNMA120LARGER = 32;
    public static final int UPMA250_DOWNMA120SMALLER = 33;


    public static final int ZERO_DOWN = -8;

    public static final int ONE = 10;

    public static final int ONE_ = -10;

    public static final int HIGHG = 100;
    public static final int LOW = -100;

    public static boolean isOne(int v) {
        if (v == ONE) {
            return true;
        }
        return false;
    }


    public static boolean isStandMA250OrMa120(int v) {
        if (v == ZERO_MA250 || v == ZERO_MA120) {
            return true;
        }
        return false;
    }

    public static boolean isZeroBottomUpEntityLarge(int v) {
        if (v == ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_UPENTITY_LARGER || v == ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_UPENTITY_LARGER) {
            return true;
        }
        if (v == ZERO_STAND_MA120_UPENTITY_LARGER || v == ZERO_STAND_MA250_UPENTITY_LARGER) {
            return true;
        }
        if (v == ZERO_STAND_MA250 || v == ZERO_STAND_MA120) {
            return true;
        }


        return false;
    }


    public static boolean testWithError(String code, Kline kLine, Table table, int rowIdx, boolean ret) {
        try {
            Command command = new Command();
            command.code = code;
            command.parse();
            boolean flag = command.process(kLine, table, rowIdx);
            return flag;
        } catch (Exception e) {
            return ret;
        }
    }

    public static boolean test(String code, Kline kLine, Table table, int rowIdx) {
        Command command = new Command();
        command.code = code;
        command.parse();
        boolean flag = command.process(kLine, table, rowIdx);
        return flag;
    }

    public static void getStockState_250LARGE(Kline kLine, StockState stockState, double matop, double mabottom) {
        if (kLine.getOpen() >= matop) {
            double fraction = KLineUtil.compareMax(kLine.getOpen(), matop);
            if (fraction > 3) {
                stockState.dayPos = ONE;
            } else {
                stockState.dayPos = ZERO_STAND_MA250;
            }
            return;
        }

        boolean flag = kLine.isTouchUp(mabottom, 1);//stand mabottom or cross
        if (flag) {
            if (kLine.getOpen() < matop && kLine.getClose() > matop) {
                double downLen = matop - kLine.getOpen();
                double upLen = kLine.getClose() - matop;
                if (upLen / downLen >= 2) {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_UPENTITY_LARGER;
                } else if (downLen / upLen >= 2) {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_DOWNENTITY_LARGER;
                } else {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_ENTITY_EQUAL;
                }
            } else if (kLine.getOpen() < matop && kLine.getClose() <= matop) {
                double fraction = KLineUtil.compareMax(kLine.getClose(), matop);
                if (fraction > 7) {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA120_TOP_DOWNMA250_SPACE_LARGER;
                } else {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA120_TOP_DOWNMA250_SPACE_SMALLER;
                }
            } else {
                stockState.dayPos = ONE;
            }
        } else if (kLine.getOpen() <= matop && kLine.getClose() > matop) {
            double downLen = matop - kLine.getOpen();
            double upLen = kLine.getClose() - matop;
            if (upLen / downLen >= 2) {
                stockState.dayPos = ZERO_STAND_MA250_UPENTITY_LARGER;
            } else if (downLen / upLen >= 2) {
                stockState.dayPos = ZERO_STAND_MA250_DOWNENTITY_LARGER;
            } else {
                stockState.dayPos = ZERO_MA250;
            }
        } else if (kLine.getClose() < mabottom) {
            double fraction = KLineUtil.compareMax(kLine.getClose(), mabottom);
            if (fraction > 5) {
                stockState.dayPos = DOWNMA250_DOWN_MA120LARGER;
            } else {
                stockState.dayPos = DOWNMA250_DOWN_MA120SMALLER;
            }
        } else if (kLine.getOpen() > mabottom) {
            double fraction = KLineUtil.compareMax(kLine.getClose(), mabottom);
            if (fraction > 5) {
                stockState.dayPos = UP_MA120_DOWN_MA250LARGER;
            } else {
                stockState.dayPos = UP_MA120_DOWN_MA250SMALLER;
            }
        } else {
            //no touch mabottom
        }
    }

    public static void getStockState_120LARGE(Kline kLine, StockState stockState, double matop, double mabottom) {
        if (kLine.getOpen() >= matop) {
            double fraction = KLineUtil.compareMax(kLine.getOpen(), matop);
            if (fraction > 3) {
                stockState.dayPos = ONE;
            } else {
                stockState.dayPos = ZERO_STAND_MA120;
            }
            return;
        }

        boolean flag = kLine.isTouchUp(mabottom, 1);//stand mabottom or cross
        if (flag) {
            if (kLine.getOpen() < matop && kLine.getClose() > matop) {
                double downLen = matop - kLine.getOpen();
                double upLen = kLine.getClose() - matop;
                if (upLen / downLen >= 2) {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_UPENTITY_LARGER;
                } else if (downLen / upLen >= 2) {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_DOWNENTITY_LARGER;
                    stockState.space = KLineUtil.compareMax(matop, mabottom);
                } else {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_ENTITY_EQUAL;
                }
            } else if (kLine.getOpen() < matop && kLine.getClose() <= matop) {
                double fraction = KLineUtil.compareMax(kLine.getClose(), matop);
                if (fraction > 5) {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA250_TOP_DOWNMA120_SPACE_LARGER;
                } else {
                    stockState.dayPos = ZERO_BOTTOM_STANDMA250_TOP_DOWNMA120_SPACE_SMALLER;
                }
            } else {
                stockState.dayPos = ONE;
            }
        } else if (kLine.getOpen() <= matop && kLine.getClose() > matop) {
            double downLen = matop - kLine.getOpen();
            double upLen = kLine.getClose() - matop;
            if (upLen / downLen >= 2) {
                stockState.dayPos = ZERO_STAND_MA120_UPENTITY_LARGER;
            } else if (downLen / upLen >= 2) {
                stockState.dayPos = ZERO_STAND_MA120_DOWNENTITY_LARGER;
            } else {
                stockState.dayPos = ZERO_MA120;
            }
        } else if (kLine.getClose() < mabottom) {
            double fraction = KLineUtil.compareMax(kLine.getClose(), mabottom);
            if (fraction > 5) {
                stockState.dayPos = DOWNMA120_DOWNMA250LARGER;
            } else {
                stockState.dayPos = DOWNMA120_DOWNMA250SMALLER;
            }
        } else if (kLine.getOpen() > mabottom) {
            double fraction = KLineUtil.compareMax(kLine.getClose(), mabottom);
            if (fraction > 5) {
                stockState.dayPos = UPMA250_DOWNMA120LARGER;
            } else {
                stockState.dayPos = UPMA250_DOWNMA120SMALLER;
            }
        } else {
            //no touch mabottom
        }
    }

    public static void getStockState(Kline kLine, StockState stockState, double matop, double mabottom, boolean ma250Large) {
        if (ma250Large) {
            getStockState_250LARGE(kLine, stockState, matop, mabottom);
        } else {
            getStockState_120LARGE(kLine, stockState, matop, mabottom);
        }
    }


    public static double min(double a, double b, double c, double d) {
        double v = 999999;
        if (a != 0) {
            if (a < v) {
                v = a;
            }
        }

        if (b != 0) {
            if (b < v) {
                v = b;
            }
        }

        if (c != 0) {
            if (c < v) {
                v = c;
            }
        }


        if (d != 0) {
            if (d < v) {
                v = d;
            }
        }

        return v;

    }

    public static double min(double a, double b, double c, double d, double e) {
        double v = 999999;
        if (a != 0) {
            if (a < v) {
                v = a;
            }
        }

        if (b != 0) {
            if (b < v) {
                v = b;
            }
        }

        if (c != 0) {
            if (c < v) {
                v = c;
            }
        }


        if (d != 0) {
            if (d < v) {
                v = d;
            }
        }

        if (e != 0) {
            if (e < v) {
                v = e;
            }
        }

        return v;

    }

    public static boolean isAllLarge(List<Double> vs, double v) {
        for (int i = 0; i < vs.size(); i++) {
            if (vs.get(i) < v) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnySmall(List<Double> vs, double v) {
        for (int i = 0; i < vs.size(); i++) {
            if (vs.get(i) < v) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnySmall(double[] vs, double v) {
        for (int i = 0; i < vs.length; i++) {
            if (vs[i] < v) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnySmallAbs(double[] vs, double v) {
        for (int i = 0; i < vs.length; i++) {
            if (Math.abs(vs[i]) < v) {
                return true;
            }
        }
        return false;
    }

}
