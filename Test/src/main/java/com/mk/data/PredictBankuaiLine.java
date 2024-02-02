package com.mk.data;

import com.mk.tool.stock.*;

import java.io.IOException;
import java.util.List;

public class PredictBankuaiLine {


    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void prsIsN(String code, String date, Kline kline, List<Kline> weeks, List<Kline> moths) {
        boolean flag = false;
        int offsetLen = 0;
        if(moths != null) {
            int monIdx = kline.getIdx();
            Kline current = moths.get(kline.getIdx());
            MaxSection maxSection60 = new MaxSection(30);
            maxSection60.initDay(moths, 0, monIdx);
            List<MaxPoint> pointsAll = maxSection60.points;

            List<MaxPoint> points = maxSection60.getRange(monIdx-60, monIdx);
            KLineUtil.sortAescMonthlinePoint(points);
            if(points.size() == 0) {
                int a = 0;
                a++;
                return;
            }

            int atype = current.getTrendType();
            if(points.size() == 1) {
                MaxPoint point = points.get(0);
                if(point.flag2 == MPoint.MIN) {
                    float frac = KLineUtil.compareMax(current.getMax(), point.kline.getMin());
                    //
                    if(frac>60) {
                        //too  high
                        int a2= 0;
                        a2++;

                    }
                }
                return;
            }
            boolean isBottom = KLineUtil.isRecentBottom(code, date, kline, weeks, moths);
            MaxPoint currentPoint = pointsAll.get(pointsAll.size()-1);

            MaxPoint mp = currentPoint.prevMax();
            Kline currentMonth = currentPoint.kline;
            float mfrac = KLineUtil.compareMax(currentPoint.kline.getMax(), mp.kline.getMax());
            boolean flagQG =false;

            int type = MPoint.HOR;
            //eq prev high
            if(mfrac<2) {
                flagQG = true;
            }

            float gailv = 0.5f;
            //zg > 30
            float zf = KLineUtil.prevNZhangfu(kline, 3, true);
            //上涨无量
            boolean isSZWuliang = kline.isSZWuliang();

            //MA30
            boolean ma30Zuli = kline.isMAIZuli30();
            boolean ma60Zuli = kline.isMAIZuli60();
            if(ma30Zuli) {
                gailv+=0.1;
            }
            if(ma60Zuli) {
                gailv+=0.1;
            }
            if(zf>30) {
                gailv+=0.3;
                if(flagQG) {
                    gailv+=0.2;
                    if(isSZWuliang) {
                        gailv+=0.2;
                    }
                }
                type = MPoint.DOWN;
                Log.log("prs==========>Y " + code + "	" + date + "   type:"+type+ " gailv:"+gailv);
            }else if(zf>20) {
                gailv+=0.2;
                if(flagQG) {
                    gailv+=0.2;
                    if(isSZWuliang) {
                        gailv+=0.2;
                    }
                }
                type = MPoint.DOWN;
                Log.log("prs==========>Y " + code + "	" + date + "   type:"+type+ " gailv:"+gailv);
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
        if(!flag) {
            return;
        }
    }

    public static void pred(String code, String date) throws IOException {
        String mode = GetBankuaiLine.MONTH;
        List<Kline> months = GetBankuaiLine.read(code, mode, false);
        Kline kline = KLineUtil.getLineByDate(months, date);
        prsIsN(code, date, kline, null,  months);
    }

    public static void main(String[] args) throws IOException {
       pred("881128", "2022/06/30");
//        pred("881129", "2022/06/30");
//        pred("881130", "2022/06/30");
//        pred("881173", "2022/08/31");//X down
//        pred("881117", "2022/08/31");
    }

}
