package com.alading.tool.stock;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.alading.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KNStragety {

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
        int idx = getIdx(days, date);
        if (idx < 200) {
            return;
        }

        Kline kline0 = days.get(idx);
        if (kline0.getOpen() < kline0.getMA250() && kline0.getClose() > kline0.getMA250()
                && KLineUtil.compareMax(kline0.getClose(), kline0.getMA250()) > 3
                && KLineUtil.compareMax(kline0.getOpen(), kline0.getMA250()) > 3) {
            return;
        }
        Kline prev = kline0.prev();
//        if (prev.getZhangfu() < ) {
//            return;
//        }
        Kline next = kline0.next();
        if (next.isShadownUp(50)) {
            return;
        }
        double ma120 = kline0.getMA120();
        double ma30 = kline0.getMA30();
        double ma60 = kline0.getMA60();
        if (ma30 > kline0.getOpen()) {
            double tmp1 = KLineUtil.compareMax(ma30, kline0.getOpen());
            if (tmp1 < 3) {

            } else {
                return;
            }
        }
        if (ma60 > kline0.getOpen()) {
            double tmp1 = KLineUtil.compareMax(ma60, kline0.getOpen());
            if (tmp1 < 3) {

            } else {
                return;
            }
        }

        double frac = KLineUtil.compareMax(kline0.getOpen(), ma120);
        double frac30 = KLineUtil.compareMax(kline0.getOpen(), ma30);
        double frac60 = KLineUtil.compareMax(kline0.getOpen(), ma60);
        double fracmin = KLineUtil.min(frac30, frac60);
        double fracMa30 = KLineUtil.prevAndMa30(kline0);
//        Log.log(file + "=================>" + frac30 + "  " + fracmin);

        if (ma30 > kline0.getOpen() && frac30 > 1) {
            return;
        }
        if (fracMa30 < 1) {

        } else {
            if (frac30 > 7.5) {
                int fromDate = days.get(0).getIdx();
                int toDate = days.get(days.size() - 1).getIdx();

                List<MonthKline> months = DateUtil.initANdGetAllMonthKLines(days, fromDate, toDate);
                if (months.size() < 100) {
                    return;
                }
                double mopen = kline0.getMopen();
                MonthKline monthKline = kline0.monthKline;
                MonthKline prevMonth = monthKline.prev();
//                int len = kline0.isMonthEary();
//                if (len > 4) {
//                    return;
//                }
                if (prevMonth.getZhangfu() > 10 && prevMonth.getMax() > prevMonth.getMA60()) {
                    if (KLineUtil.abousNearest(monthKline.getOpen(), kline0.getMonthMA60(), 2)) {
                        int a = 0;
                        a++;


                        Kline kline1 = kline0.next();
                        if (kline1 == null) {
                            return;
                        }
                        Kline kline2 = kline1.next();
                        if (kline2 == null) {
                            return;
                        }
                        double zhangfu = kline0.getZhangfu();
                        double min = kline2.getMin();
                        if (zhangfu > 9.6 && kline1.getZhangfu() < 0 && kline2.getZhangfu() < 0) {
                            if (KLineUtil.compareMax(min, kline0.getMax()) > 6.5 && min > kline0.getMin()) {
                                Log.log("OK" + file + "	" + date + "  " + days.get(idx).getZhangfu() + " mamin:" + fracmin + " ma120:" + frac);
                            }
                        }


                    }
                }
                double open = kline0.getOpen();
//                return;
            } else if (frac30 > 5) {
                //有动力
                if (KLineUtil.compareMax(ma30, ma60) < 2 && KLineUtil.compareMax(ma60, ma120) < 4) {

                } else {
//                Log.log("prsIsN==========>N 30~60 60~120 " + file + "	" + date + "  " + days.get(idx).getZhangfu()+"  MA30:"+frac30);
                    return;
                }
            } else {
                if (KLineUtil.compareMax(ma30, ma60) < 4 && KLineUtil.compareMax(ma60, ma120) < 8) {

                } else {
//                Log.log("prsIsN==========>N 30~60 60~120 " + file + "	" + date + "  " + days.get(idx).getZhangfu()+"  MA30:"+frac30);
                    return;
                }
            }
        }
//        if (frac > 16) {
////            Log.log("prsIsN==========>N " + file + "	" + date + "  " + days.get(idx).getZhangfu()+"  MA120:"+frac);
//            return;
//        }
        if (kline0.is30(60)) {
            return;
        }
        Kline kline1 = kline0.next();
        if (kline1 == null) {
            return;
        }
        Kline kline2 = kline1.next();
        if (kline2 == null) {
            return;
        }
        double zhangfu = KLineUtil.prevNZhangfu(kline0, 2);
        Kline touchMA10 = KLineUtil.nextNTouchMA10(kline0, 5);
        double min = touchMA10.getMin();
        if (touchMA10 == null) {
            return;
        }
        if (kline0.getZhangfu() > 6 && zhangfu > 9.6 && touchMA10 != null) {
            double AFRAC =  (5.6 * (kline0.getZhangfu() / 10.0));
            if (KLineUtil.compareMax(min, kline0.getMax()) > AFRAC) {
                Log.log("" + file + "	" + date + "  " + days.get(idx).getZhangfu() + " mamin:" + fracmin + " ma120:" + frac);
//                if(ma30>ma60 && ma60>ma120) {
//                    Log.log("30~60: "+KLineUtil.compareMax(ma30, ma60));
//                    Log.log("60~120: "+KLineUtil.compareMax(ma60, ma120));
//                    Log.log("30~120: "+KLineUtil.compareMax(ma30, ma120));
//                }
            }
        }
    }


    static String FILE = "D:\\new_tdx\\T0002\\export\\";

    public static void prs(String file, String singleDate) {
//        SINGLE_DATE = singleDate;
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
        int LEN = 100;
        if (isTest) {
            if (days.size() < LEN) {
                LEN = days.size() - 50;
            }
            for (int j = 0; j < LEN; j++) {
                Date adate = DateUtil.stringToDate3(DATE);
                adate = DateUtil.getBeforeDate(adate, j);
                String dateStr = DateUtil.dateToString3(adate);
                if (dateStr.equalsIgnoreCase("2023/06/01")) {
                    int a = 0;
                    a++;
                }
                prsIsN(file, days, dateStr);
            }
        } else {
            if (isSingle) {
                prsIsN(file, days, singleDate);
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


    static final String DATE = "2023/06/01";
//    static String SINGLE_DATE = "";
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
                if (item.toString().contains("000756")) {
                    int a = 0;
                    Log.log(item.toString());
                }
                prs(item.getName(), DATE);
            }
        } else {
            prs(code, date);//2023/01/30
        }

    }

    public static void main(String[] args) {
//        mainProcess("SH#603999.txt", "2023/04/24");//Y  30~60=3
//        mainProcess("SH#601138.txt", "2023/03/03");//Y  30~60=3
//        mainProcess("SH#600959.txt", "2023/04/24");//Y 跨月攻击，所以第二个月会完全调整， 所以遇到上影线就跑 10个点跑
//        mainProcess("SH#600685.txt", "2023/05/08");//Y 周线仙人指路 加 周线N, 月初都有回调 Y
//        mainProcess("SH#600482.txt", "2023/05/08");//Y 自回调 先突破月线均线再回抽
//        mainProcess("SH#600310.txt", "2023/05/17");//Y
//        mainProcess("SH#603097.txt", "2023/05/25");//Y
//        mainProcess("002846.txt", "2023/06/13");//Y
//        mainProcess("002315.txt", "2023/04/13");//Y
//        mainProcess("002315.txt", "2023/04/19");//Y

//        mainProcess("002846.txt", "2023/06/27");//Y

        mainProcess("SH#600339.txt", "2023/03/17");//E 月线压制
        mainProcess("SH#600377.txt", "2023/05/08");//E MA 16  周线MA250压制
        mainProcess("SH#600468.txt", "2023/05/17");//E MA>6
        mainProcess("SH#600428.txt", "2023/05/09");//E 2023/01/30
        mainProcess("SH#600827.txt", "2023/05/26");//E 时间不对
        mainProcess("SH#601106.txt", "2023/05/18");//市盈率220 ERR

        mainProcess("003010.txt", "2023/05/17");//Y  function2
        mainProcess("SH#600468.txt", "2023/05/17");//E
        mainProcess("002301.txt", "2023/04/14");//E
        mainProcess("SH#600756.txt", "2023/03/14");//E
        mainProcess("002638.txt", "2023/03/13");//E 没有阳线规避掉
        mainProcess("002377.txt", "2023/03/22");//E
        mainProcess("003037.txt", "2023/03/02");//E
        mainProcess("002292.txt", "2023/04/03");//E

        //SH#600310.txt	2023/05/17  10.031345 mamin:5.3535905
        //002122	2023/03/29  10.144925 mamin:0.79326195003027
        //000050	2023/03/10  10.051277 mamin:1.3191551  O

//        mainProcess("SH#603999.txt", "");//Y

        //            main2("003010.txt");//2023/05/17
    }

}
