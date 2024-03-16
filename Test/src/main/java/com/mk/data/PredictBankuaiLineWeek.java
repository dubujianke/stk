package com.mk.data;

import com.mk.tool.stock.*;
import com.mk.util.Constant;

import java.io.IOException;
import java.util.List;

public class PredictBankuaiLineWeek {


    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void prsIsN(String code, String weekDate, Kline kline, Kline mkline, List<Kline> weeks, List<Kline> moths) {
        boolean flag = false;
        int offsetLen = 0;
        if (weeks != null) {
            int monIdx = kline.getIdx();
            Kline current = weeks.get(kline.getIdx());
            double  prevZf = current.getPrevZF(6);
            double  ma60 = current.getMA60();
            double  temp2 = current.getMAXMA60Frac();
            int atype = 0;
            Kline prevMonth = mkline.prev();
            int trendType10 = prevMonth.getMA10TrendType(10);
            int trendType30 = prevMonth.getMA30TrendType(10);
            int trendType60 = prevMonth.getMA60TrendType(10);


            boolean isFirstWeek = KLineUtil.isFirstWeek(weekDate);

            if (prevMonth.getZhangfu() > 3 && isFirstWeek) {
                Kline prevMonth2 = prevMonth.prev();
                double  ma10Prev2 = prevMonth2.getMA10();
                double  ma10Prev = prevMonth.getMA10();
                double  dlt = ma10Prev - ma10Prev2;
                //hengpan
                if (trendType10 == 0 && trendType30 == 2) {
                    double  offset = KLineUtil.compareMax(prevMonth.getClose(), prevMonth.getMA30());
                    if (offset > 8) {
                        atype = Constant.MA10HOR_MA30FAR;
                        Log.log(code + " " + "MA10HOR_MA30FAR");
                        return;
                    }
                }

//                double  fracD = 100 * dlt / mkline.close;
//                if (fracD < 1) {
//                    atype = Constant.MA10FAR;
//                    Log.log(code + " " + "MA10FAR");
//                    return;
//                }
                double  curMA10 = mkline.getMA10();
                double  dlt2 = curMA10;
                double  dlt3 = mkline.getMA60();
                if (prevMonth.getClose() > curMA10 && prevMonth.getClose() > mkline.getMA60()) {
                    double  minDlr = Math.max(dlt2, dlt3);
                    double  dlt4 = KLineUtil.compareMax(prevMonth.getClose(), minDlr);
                    if (dlt4 > 3) {
                        atype = Constant.MON_MA10FAR;
                        Log.log(code + " " + "MA10FAR");
                        return;
                    }
                }
            }

            if (prevMonth.getZhangfu() > 3 && isFirstWeek) {

            }

            if (temp2 < 3) {
                if (prevZf > 7 && current.isShadownUp() && current.szWuli()) {
                    atype = Constant.WEEK_MA60_PREVENT;
                    Log.log(code + " " + "WEEK_MA60_PREVENT");
                    return;
                }
            }
            if (current.getEntityMax() < ma60) {
                atype = Constant.WEEK_MA60_PREVENT;
                Log.log(code + " " + "WEEK_MA60_PREVENT");
                return;
            }


            Kline aprevLine = current.prev();
            if (current.isShadownUp(50) && current.szWuli()) {
                if (aprevLine.isTupoMA60(0.2f)) {
                    double  temp = current.getMA60Frac();

                    if (temp > 1.5 && prevZf > 7) {
                        atype = Constant.WEEK_STANDMA60_SHADOWUP__PREVENT;
                        Log.log(code + " " + "WEEK_UPMA60_SHADOWUP__PREVENT");
                        return;
                    }
                }
            }
            if (current.getZhangfu() > 2 && current.szWuli()) {
                if (aprevLine.isTupoMA60(0.2f)) {
                    double  temp = current.getMA60Frac();

                    if (temp > 1.5 && prevZf > 7) {
                        atype = Constant.WEEK_UPMA60_SHADOWUP__PREVENT;
                        Log.log(code + " " + "WEEK_UPMA60_SHADOWUP__PREVENT");
                        return;
                    }
                }
            }

            if (aprevLine.isShadownUp(60) && current.getZhangfu() > 2) {
//                double  frac = KLineUtil.compareMax(current.open, aprevLine.close);
                if (current.touchClose(aprevLine.getMax(), 0.3f)) {
                    atype = Constant.PREVUPSHADOW_CURSZ;
                    Log.log(code + " " + "WEEK_PREVUPSHADOW_CURSZ");
                    return;
                }
            }

            double temp = current.getMA60Frac();

            if (temp > 3 && aprevLine.getPrevZF(2) > 2 && current.getZhangfu() < 2 && prevZf > 7) {
//                if(current.szWuli()) {
                atype = Constant.WEEK_UPMA60_UP_DOWN;
                Log.log(code + " " + "WEEK_UPMA60_UP_DOWN");
//                }
                return;
            }

            if (current.getPrevZhenF(4) < 6 && current.getZhangfu() < 0.5) {
                double prevZf2 = current.prev().getPrevZF(6);
                double frac = KLineUtil.compareMaxSign(current.getClose(), current.getMA10());
                if (prevZf2 > 7 && frac > 1) {
                    atype = Constant.UP_AND_HOR;
                    Log.log(code + " " + "UP_AND_HOR");
                    return;
                }
            }

            Kline.ZFModel zfModel = current.getPrevZhenFContinus(4);
            if (zfModel.isHor()) {
                double prevZf2 = current.prev().getPrevZF(zfModel.n + 4);
                double prevDf2 = current.prev().getPrevDF(zfModel.n + 4);
                if (prevZf2 > prevDf2 && (prevZf2 - prevDf2) > prevDf2) {
                    int cnt = KLineUtil.getNIsDownMAI(current, 6);
                    if (cnt > 0) {
                        double frac = KLineUtil.compareMaxSign(current.getClose(), current.getMA10());
                        if (prevZf2 > 18 && frac > 3) {
                            Log.log(code + " " + "UP_AND_HOR");
                            return;
                        }
                    }
                }
                double frac = KLineUtil.compareMaxSign(current.getClose(), current.getMA10());
                if (prevZf2 > 18 && frac > 3) {
                    //MA60横盘 乖离减少到0后， 距离MA10还有3个点距离
                    atype = Constant.UP_AND_HOR;
                    Log.log(code + " " + "UP_AND_HOR");
                    return;
                }
            }

            if (zfModel.isHor()) {
                double prevZf2 = current.prev().getPrevZF(zfModel.n + 4);
                double prevDf2 = current.prev().getPrevDF(zfModel.n + 4);
                if (prevDf2 > prevZf2 && (prevDf2 - prevZf2) > prevZf2) {
                    int cnt = KLineUtil.getNIsDownMAI(current, 6);
                    if (cnt >= 2) {
                        //MA60横盘 乖离减少到0后， 距离MA10还有3个点距离
                        atype = Constant.DOWN_AND_HOR;
                        Log.log(code + " " + "DOWN_AND_HOR");
                        return;
                    }
                }
            }

            MaxSection maxSection60 = new MaxSection(30);
            maxSection60.initDay(weeks, 0, monIdx);
            List<MaxPoint> pointsAll = maxSection60.points;
            List<MaxPoint> points = maxSection60.getRangeAllMAX(monIdx - 60, monIdx);
            KLineUtil.sortDescMonthlinePoint(points);

            double frac = KLineUtil.compareMaxSign(current.getClose(), current.getMA10());
            if (frac > 5) {
                if (points.size() > 0) {
                    MaxPoint point = points.get(0);
                    double frac45 = KLineUtil.compareMax(point.kline.getMax(), current.getMax());
                    if (frac45 > 5) {
                        atype = Constant.MON_MA10FAR;
                        Log.log(code + " " + "MA10FAR");
                        return;
                    } else {
                        atype = Constant.MON_MA10FAR;
                        Log.log(code + " " + "MA10FAR");
                        return;
                    }
                }

            }

            if (trendType10 == 0 && trendType30 == 0 && trendType60 == 2) {
                double offset = KLineUtil.compareMax(prevMonth.getClose(), prevMonth.getMA30());
                if (offset > 4) {
                    atype = Constant.MA10HOR_MA30FAR;
                    Log.log(code + " " + "MA10HOR_MA30FAR");
                    return;
                }
            }else if (trendType60 == 0 && trendType30 == 1 && trendType60 == 0) {
                double tmp = KLineUtil.compareMax(prevMonth.getClose(), prevMonth.getMA30());
                if(prevMonth.isShadownUp(60)  && prevMonth.getClose() <prevMonth.getMA30()) {
                    atype = Constant.MA30_PREVENT;
                    Log.log(code + " " + "MA30_PREVENT");
                    return;
                }
            }else if (trendType60 == 0 && trendType30 == 0 && trendType10 == 0) {
                boolean isGuaili0 = prevMonth.isGuaili0(10);
                double offset = KLineUtil.compareMax(prevMonth.getClose(), prevMonth.getMA60());
                if (isGuaili0 && offset > 4) {
                    atype = Constant.MA10HOR_MA30FAR;
                    Log.log(code + " " + "MA10HOR_MA30FAR");
                    return;
                }
            }

            boolean ff = true;
            if (ff) {
                Log.log(code + " " + "");
            }

            if (points.size() == 0) {
                int a = 0;
                a++;
                return;
            }


            int atype2 = current.getTrendType();
            if (points.size() == 1) {
                MaxPoint point = points.get(0);
                if (point.flag2 == MPoint.MIN) {
                    double frac3 = KLineUtil.compareMax(current.getMax(), point.kline.getMin());
                    if (frac3 > 60) {
                        //too  high
                        int a2 = 0;
                        a2++;

                    }
                }
                return;
            }
            boolean isBottom = KLineUtil.isRecentBottom(code, weekDate, kline, weeks, weeks);
            MaxPoint currentPoint = pointsAll.get(pointsAll.size() - 1);

            MaxPoint mp = currentPoint.prevMax();
            Kline currentMonth = currentPoint.kline;
            double mfrac = KLineUtil.compareMax(currentPoint.kline.getMax(), mp.kline.getMax());
            boolean flagQG = false;

            int type = MPoint.HOR;
            //eq prev high
            if (mfrac < 2) {
                flagQG = true;
            }

            double gailv = 0.5f;
            //zg > 30
            double zf = KLineUtil.prevNZhangfu(kline, 3, true);
            //上涨无量
            boolean isSZWuliang = kline.isSZWuliang();

            //MA30
            boolean ma30Zuli = kline.isMAIZuli30();
            boolean ma60Zuli = kline.isMAIZuli60();
            if (ma30Zuli) {
                gailv += 0.1;
            }
            if (ma60Zuli) {
                gailv += 0.1;
            }
            if (zf > 30) {
                gailv += 0.3;
                if (flagQG) {
                    gailv += 0.2;
                    if (isSZWuliang) {
                        gailv += 0.2;
                    }
                }
                type = MPoint.DOWN;
                Log.log("prs==========>Y " + code + "	" + weekDate + "   type:" + type + " gailv:" + gailv);
            } else if (zf > 20) {
                gailv += 0.2;
                if (flagQG) {
                    gailv += 0.2;
                    if (isSZWuliang) {
                        gailv += 0.2;
                    }
                }
                type = MPoint.DOWN;
                Log.log("prs==========>Y " + code + "	" + weekDate + "   type:" + type + " gailv:" + gailv);
            }

//            KLineUtil.sortAescMonthlinePoint(points);
//            if(points.size() == 0) {
//                int a = 0;
//                a++;
//                return;
//            }
//            MonthKline minMonth = (MonthKline) points.get(0).kline;
//            if(monIdx == minMonth.idx) {
//                flag = true;
//            }
//            com.mk.tool.stock.Log.log(file+"=====================>"+minMonth.key);
        }
        if (!flag) {
            return;
        }
    }

    public static void pred(String code, String date) throws IOException {
        List<Kline> weeks = GetBankuaiLine.read(code, GetBankuaiLine.WEEK, false);
        List<Kline> months = GetBankuaiLine.read(code, GetBankuaiLine.MONTH, false);
        Kline kline = KLineUtil.getLineByDate(weeks, date);
        Kline mkline = KLineUtil.getMonthLineByDate(months, date);
        prsIsN(code, date, kline, mkline, weeks, months);
    }

    public static void main(String[] args) throws IOException {

        pred("881156", "2022/12/09");//

//
        pred("881173", "2022/12/09");//MA60 down
        pred("881101", "2022/12/09");//UPMA60_SHADOWUP__PREVENT
        pred("881102", "2022/12/09");//
        pred("881145", "2022/12/09");//
        pred("881157", "2022/12/09");//
        pred("881158", "2022/12/09");//
        pred("881103", "2022/12/09");//
//        pred("881117", "2022/08/31");
//        pred("881128", "2022/12/09");//
//        pred("881173", "2022/12/09");//


        pred("881148", "2022/12/09");//
        pred("881135", "2022/12/09");//
        pred("881117", "2022/12/09");//
        pred("881144", "2022/12/09");//
        pred("881105", "2022/12/09");//
        pred("881142", "2022/12/09");//
        pred("881140", "2022/12/09");//
        pred("881133", "2022/12/09");//
        pred("881172", "2022/12/09");//
        pred("881169", "2022/12/09");//
          pred("881168", "2022/12/09");//
        pred("881129", "2022/12/09");//
        pred("881126", "2022/12/09");//
        pred("881120", "2022/12/09");//
        pred("881163", "2022/12/09");//
        pred("881119", "2022/12/09");//

        pred("881110", "2022/12/09");//
        pred("881153", "2022/12/09");//
        pred("881152", "2022/12/09");//
        pred("881151", "2022/12/09");//
        pred("881109", "2022/12/09");//
        pred("881108", "2022/12/09");//
        pred("881103", "2022/12/09");//
        pred("881102", "2022/12/09");//
        pred("881146", "2022/12/09");//
        pred("881145", "2022/12/09");//
        pred("881101", "2022/12/09");//
        pred("881107", "2022/12/09");//
        pred("881149", "2022/12/09");//
        pred("881104", "2022/12/09");//
        pred("881182", "2022/12/09");//
        pred("881181", "2022/12/09");//
        pred("881180", "2022/12/09");//
        pred("881143", "2022/12/09");//
        pred("881141", "2022/12/09");//
        pred("881136", "2022/12/09");//
        pred("881179", "2022/12/09");//
        pred("881134", "2022/12/09");//
        pred("881178", "2022/12/09");//
        pred("881177", "2022/12/09");//
        pred("881139", "2022/12/09");//
        pred("881138", "2022/12/09");//
        pred("881137", "2022/12/09");//

        pred("881171", "2022/12/09");//
        pred("881170", "2022/12/09");//
        pred("881132", "2022/12/09");//
        pred("881176", "2022/12/09");//
        pred("881175", "2022/12/09");//
        pred("881131", "2022/12/09");//
        pred("881130", "2022/12/09");//
        pred("881174", "2022/12/09");//
        pred("881125", "2022/12/09");//
        pred("881124", "2022/12/09");//
        pred("881167", "2022/12/09");//
        pred("881123", "2022/12/09");//
        pred("881166", "2022/12/09");//
        pred("881122", "2022/12/09");//
        pred("881127", "2022/12/09");//
        pred("881161", "2022/12/09");//
        pred("881160", "2022/12/09");//
        pred("881165", "2022/12/09");//
        pred("881121", "2022/12/09");//
        pred("881164", "2022/12/09");//
        pred("881162", "2022/12/09");//
        pred("881114", "2022/12/09");//
        pred("881158", "2022/12/09");//
        pred("881157", "2022/12/09");//
        pred("881112", "2022/12/09");//
        pred("881156", "2022/12/09");//
        pred("881155", "2022/12/09");//
        pred("881118", "2022/12/09");//
        pred("881116", "2022/12/09");//
        pred("881159", "2022/12/09");//
        pred("881115", "2022/12/09");//


    }

}
