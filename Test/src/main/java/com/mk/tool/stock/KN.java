//package com.mk.tool.stock;
//
//import com.huaien.core.util.DateUtil;
//import com.mk.model.AllModel;
//import com.mk.model.Bankuai;
//import com.mk.report.LineReport;
//import com.mk.report.LineReports;
//import org.jsoup.internal.StringUtil;
//
//import java.util.List;
//import java.util.Map;
//
//import static com.mk.tool.stock.KLineUtil.*;
//
///**
// * 涨停后回调
// */
//public class KN extends Stragety {
//
//    public static int CNT = 0;
//
//    public static void error(String file, String date, String msg) {
//        int a = 0;
//        Log.log(file + "  " + date + " " + msg);
//    }
//
//    public static void report(String file, Kline kline, LineContext context, boolean realBottomFlag) {
//        try {
//            StockAllMinuteLine stockAllMinuteLine = context.getStockAllMinuteLine();
//            LineReport lineReport = new LineReport();
//            lineReport.setCode(INFO);
//            lineReport.setFile(file);
//            lineReport.setNeedForce(context.needForce);
//            lineReport.setStockAllMinuteLine(stockAllMinuteLine);
//            lineReport.setBottomFlag(realBottomFlag ? 2 : 0);
//            for (int i = 0; i < 10; i++) {
//                Kline item = kline.next(i + 1);
//                Grid grid = IsZhangting.printDaysMinutesTJ(file, item, context);
//                lineReport.add(grid);
//            }
//            lineReport.getReportCross();
//            lineReport.getSppedUpWithVOL(context);
//            if (lineReport.isDeadCross() == 0) {
//                LineReports.add(lineReport);
//            }
//        } catch (Exception e) {
//        }
//
//    }
//
//    public static void ok_(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
//        Guaili aGuaili2 = monthguali(INFO, date, kline0);
//        Guaili guaili = weekguali(INFO, date, kline0);
//        CNT++;
//        Kline flag = null;
//        flag = needForce(file, INFO, date, kline0, weekline, nextN, msg, context);
//        if (Stragety.kn == 1 || Stragety.forceLog == 1) {
//            if (flag == null) {
//                Log.log("" + INFO + " " + " FORCE:N " + KN.class.getSimpleName() + " " + nextN.getDate() + "	" + date);
//            } else {
//                Log.log("" + INFO + " " + " FORCE:" + flag.getDate() + " " + KN.class.getSimpleName() + " " + nextN.getDate() + "	" + date);
//            }
//
//        }
//        try {
//            context.needForce = flag;
//            report(file, nextN, context, false);
//        } catch (Exception e) {
//
//        }
//    }
//
//    public static void ok2_(String file, String INFO, String date, Kline kline0, Kline flag, Weekline weekline, Kline nextN, String msg, LineContext context) {
//        Guaili aGuaili2 = monthguali(INFO, date, kline0);
//        Guaili guaili = weekguali(INFO, date, kline0);
//        CNT++;
//        if (Stragety.kn == 1 || Stragety.forceLog == 1) {
//            Log.log("" + INFO + " " + "" + "FIRST_ZT" + " " + KN.class.getSimpleName() + " nextDate:" + nextN.getDate() + "	" + date);
//        }
//        try {
//            context.needForce = flag;
//            report(file, nextN, context, false);
//        } catch (Exception e) {
//
//        }
//    }
//
//    public static Kline needForce(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
//        try {
//
//
//            for (int i = 0; i < 3; i++) {
//                Kline next = nextN.next(i);
//                if (next == null) {
//                    break;
//                }
//                float frac1 = KLineUtil.compareMax(next.getMin(), kline0.getMax());
//                if (frac1 > 7) {
//                    int dayLen = DateUtil.getDayLen(DateUtil.stringToDate3(date), DateUtil.stringToDate3(next.getDate()));
//                    if (dayLen < 3) {
//                        Kline next2 = next.next();
//                        if (next2 != null) {
//                            if (next.isShadownDown(50) && next2.isShadownUp(50)) {
//                                return next2;
//                            }
//                        }
//                    }
//                    continue;
//                }
//
//                float frac = KLineUtil.compareMax(next.getMax(), next.prev().getClose());
//                if (frac < 8) {
//                    continue;
//                }
//                int dayLen = DateUtil.getDayLen(DateUtil.stringToDate3(date), DateUtil.stringToDate3(next.getDate()));
//                if (dayLen > 6) {
//                    return null;
//                }
//                if (next.isShadownUp(70)) {
//                    return next;
//                }
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    public static void saveOK(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
//        if (!Stragety.isResult) {
//            Result result = new Result(file, date);
//            result.stragety = KN.class.getSimpleName();
//            Stragety.addLine(result);
//        } else {
//            ok(file, INFO, date, kline0, kline0.weekline, nextN, "", context);
//            return;
//        }
//
//    }
//
//    public static void ok(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
//        StockState astockState = null;
//        if (kline0.nextIsBlackSun()) {
//            error(file, date, "nextIsBlackSun");
//            return;
//        }
//
//        if (kline0.hasTopLines(4, 3)) {
//            if (kline0.nextSpace(4) > 2) {
//                error(file, date, "hasTopLines");
//                return;
//            }
//        }
//
//        if (kline0.next() != null) {
//            if (kline0.next().getZhangfu() > 4) {
//                return;
//            }
//        }
//        if (kline0.hasTopLines(4, 3)) {
//            if (kline0.nextSpace(2) > 3) {
//                error(file, date, "hasTopLines toHigh");
//                return;
//            }
//        }
//
//
//        if (kline0.getZhangfu() > 9 && kline0.getEntityZhangfu() < 5) {
//            error(file, date, "getEntityZhangfu < 5");
//            return;
//        }
//        if (kline0.isMonthEnd(2)) {
//            error(file, date, "isMonthEnd");
//            return;
//        }
//
//        Kline nextt = kline0.next();
//        float zf = nextt.getZhenfu();
//        if (zf > 7 && nextt.isShadownUp(30) && nextt.getZhangfu() > 0) {
//            error(file, date, "");
//            return;
//        }
//
//        float ma120 = kline0.getMA120();
//        float ma250 = kline0.getMA250();
//        if (ma120 > ma250) {
//            Kline kline_ = kline0.next(4);
//            if (kline_ != null) {
//                float ma120_ = kline_.getMA120();
//                float ma250_ = kline_.getMA250();
//                if (ma120_ < ma250_) {
//                    error(file, date, "");
//                    return;
//                }
//            }
//        }
//
//        if (kline0.weekline != null) {
//            astockState = kline0.getPOS();
//        }
//        if (kline0.weekline != null) {
//            if (astockState.dayPos == KLineUtil.ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_ENTITY_EQUAL) {
//                Kline kline = kline0.weekline;
//                if (kline.getOpen() < kline.getMA60()) {
//                    if (KLineUtil.compareMax(kline.getOpen(), kline.getMA60()) < 5) {
//                        error(file, date, "");
//                        return;
//                    }
//                }
//            }
//
//            if (astockState.dayPos == KLineUtil.ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_UPENTITY_LARGER) {
//                Kline nextOKLine = kline0.getNextLineOK(50);
//                if (nextOKLine != null && nextOKLine.hasNextZFLarge(6)) {
//                    ok_(file, INFO, date, kline0, weekline, nextN, msg, context);
//                    return;
//                }
//            }
//
//            if (astockState.dayPos == KLineUtil.DOWNMA250_DOWN_MA120LARGER) {
//                error(file, date, "");
//                return;
//            }
//
//            if (astockState.dayPos == KLineUtil.ONE) {
//                Kline kline = kline0.monthKline;
//                if (kline.getOpen() < kline.getMA30()) {
//                    float ffv = KLineUtil.compareMax(kline.getOpen(), kline.getMA30());
//                    if (ffv < 5) {
//                        error(file, date, "month is to close to MA30");
//                        return;
//                    }
//                }
//                if (kline0.testDeadGoldCross(120, 250, 10, 0)) {
//                    return;
//                }
//            }
//
//            if (astockState.dayPos == KLineUtil.ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_ENTITY_EQUAL) {
//                Kline nextOKLine = kline0.getNextLineDownTouchMA(30, 250);
//                if (nextOKLine != null && nextOKLine.hasNextZT(6)) {
//                    ok_(file, INFO, date, kline0, weekline, nextN, msg, context);
//                    return;
//                }
//            }
//
//            if (astockState.dayPos == KLineUtil.ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_DOWNENTITY_LARGER) {
//                //MA60 < MA120 dead cross
//                if (kline0.isGuailiMA1MA2ContiniusLarge(60, 120, 3, 5)) {
//                    if (kline0.testDeadGoldCross(60, 120, 16, 0)) {
//                        return;
//                    }
//                    return;
//                }
//            }
//
//
////            if (astockState.dayPos == KLineUtil.ZERO_STAND_MA120_DOWNENTITY_LARGER) {
////                Kline nextOKLine = kline0.getNextLineDownFirstUpMA250SecondOK(50);
////                if (nextOKLine != null && nextOKLine.hasNextZT(6)) {
////                    ok_(file, INFO, date, kline0, weekline, nextN, msg, context);
////                    return;
////                }
////            }
//
//            if (astockState.dayPos == ONE) {
//                if (kline0.isGuailiMA120250ContiniusLarge(1, 3)) {
//                    float ff = kline0.getSpace(120, 250);
//                    if (ff > 0.03 && ff < 0.5) {
//                        float ff2 = kline0.next().getSpace(120, 250);
//                        if (ff2 < ff) {
//
//                        } else {
//                            error(file, INFO, "MA120 MA250 space already gold");
//                        }
//                        return;
//                    }
//                }
//            }
//        }
//
//        if (kline0.monthKline != null) {
//            astockState = kline0.getPOS();
//            if (astockState.dayPos == KLineUtil.ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_ENTITY_EQUAL) {
//                Kline next250 = kline0.nextDownTouchMAI(36, 250);
//                if (next250 != null) {
//                    float vv = next250.getNextZF(10);
//                    if (vv > 10) {
//                        error(file, date, "");
//                        return;
//                    }
//                }
//            }
//            if (astockState.dayPos == KLineUtil.ZERO_BOTTOM_STANDMA120_TOP_DOWNMA250_SPACE_LARGER) {
//                Kline monthLine = kline0.monthKline;
//                float flagx = monthLine.getMinspanceMA();
//                if (flagx < 2) {
//                    error(file, date, "");
//                    return;
//                }
//            }
//            if (astockState.dayPos == KLineUtil.ZERO_STAND_MA120_UPENTITY_LARGER) {
//                Kline monthLine = kline0.monthKline;
//                float flagx = monthLine.getMinspanceMA();
//                if (flagx < 2) {
//                    error(file, date, "");
//                    return;
//                }
//            }
//            if (astockState.dayPos == KLineUtil.ZERO_STAND_MA250) {
//                Kline touchLine = kline0.nextDownTouchMAI(17, 250);
//                if (touchLine != null) {
//                    int dlt = touchLine.getIdx() - kline0.getIdx();
//                    if (dlt > 15) {
//                        error(file, date, "ZERO_STAND_MA250 next toucn ma250 days to long:" + dlt);
//                        return;
//                    }
//                }
//            }
//        }
//
//
//        Kline next = kline0.next();
//        if (next.getOpen() < kline0.getClose() && KLineUtil.compareMax(next.getOpen(), kline0.getClose()) >= 5) {
//            return;
//        }
//
//
//        //POS 10, no fall down to ma10
//        if (kline0.weekline != null) {
//
//            float ma30 = kline0.weekline.getMA30();
//            float ma60 = kline0.weekline.getMA60();
//            if (ma30 > kline0.weekline.getOpen()) {
//                if (KLineUtil.compareMax(ma30, kline0.weekline.getOpen()) < 7) {
//                    if (KLineUtil.compareMax(ma60, kline0.weekline.getOpen()) < 7) {
//                        if (KLineUtil.compareMax(ma60, ma30) < 0.7) {
//                            return;
//                        }
//                    }
//                }
//            }
//            //
//            if (astockState.dayPos == KLineUtil.ZERO_BOTTOM_STANDMA250_TOP_DOWNMA120_SPACE_LARGER) {
//                if (kline0.weekline.hasDZ(26, 55)) {
//                    if (!kline0.hasUPTouchToMA(14, 120)) {
//                        return;
//                    }
//                }
//            } else {
//                if (kline0.weekline.hasDZ(26, 55)) {
//                    return;
//                }
//            }
//
//            if (astockState.isDayPosOne()) {
//                float ma = kline0.guailiMI(120);
//                boolean flag = kline0.isALlLineOK();
//                if (!flag && ma > 10) {
//                    float ama60 = kline0.guailiMI(60);
//                    if (ama60 < 7.3) {
//                    } else {
//                        return;
//                    }
//                }
//
//                float ttt = KLineUtil.compareMax(kline0.getMA30(), kline0.getMA60());
//                boolean chg = kline0.isGuailiChange();
//                if (chg) {
//
//                } else {
//                    if (ttt > 4.5 && kline0.getMA30() > kline0.getMA60()) {
//                        error(file, date, "kline0.getMA30()> kline0.getMA60() space:" + ttt);
//                        return;
//                    }
//                }
//
//
//                int trendType10 = kline0.getMA10TrendTypeStrict(10);
//                int trendType30 = kline0.getMA30TrendTypeStrict(10);
//                int trendType60 = kline0.getMA60TrendTypeStrict(10);
//                if (trendType30 == 0) {
//                    if (trendType60 == 2) {
//                        Kline touchLine = kline0.nextDownTouchMAI(14, 60);
//                        if (touchLine != null) {
//                            boolean retFlag = touchLine.isGuailiChange();
//                            if (!retFlag) {
//                                error(INFO, date, "");
//                                return;
//                            }
//                            int dlt = touchLine.getIdx() - kline0.getIdx();
//                        }
//
//
//                    }
//                } else if (trendType30 == 2) {
//                    Kline touchLine = kline0.nextDownTouchMAI(14, 30);
//                }
//
//            }
//
//
//            int trendType10 = kline0.weekline.getMA10TrendType(10);
//            int trendType30 = kline0.weekline.getMA30TrendType(10);
//            int trendType60 = kline0.weekline.getMA60TrendType(10);
//            //hengpan
//            astockState = kline0.getPOS();
//            if (astockState.isDayPosOne()) {
//                boolean flagy = kline0.isVolumnLarge2();
//                boolean linesOK = kline0.isALlLineOK();
//                if (linesOK) {
//                    if (flagy) {
//                        boolean flag = kline0.weekline.isGuailiChange();
//                        if (!flag) {
//                            boolean flagx = kline0.hasFallDownMAI(10, 4, 2.1f);
//                            if (!flagx) {
//                                error(INFO, date, "");
//                                return;
//                            }
//                        } else {
//
//                        }
//                    } else {
//                        boolean flagx = kline0.hasFallDownMAI(10, 4, 1.0f);
//                        if (!flagx) {
//                            error(INFO, date, "");
//                            return;
//                        }
//                    }
//                } else {
//                    if (flagy) {
//                        boolean flag = kline0.weekline.isGuailiChange();
//                        if (!flag) {
//                            boolean flagx2 = kline0.hasFallDownMAI(10, 4, 2.1f);
//                            boolean flagx = kline0.hasFallDownMAI(30, 4, 2.1f);
//                            if (!flagx && !flagx2) {
//                                error(INFO, date, "");
//                                return;
//                            }
//
//                        } else {
//
//                        }
//                    } else {
//                        boolean flagx = kline0.hasFallDownMAI(10, 4, 1.0f);
//                        if (!flagx) {
//                            error(INFO, date, "");
//                            return;
//                        }
//                    }
//                }
//            }
//
//            if (astockState.isDayPosZeroBottomUpEntityLarge()) {
//                //POS = 2 MA30 >> MAI no gold cross
//                boolean flag = kline0.nextMa30LagerThanOther(10);
//                if (!flag) {
//                    error(INFO, date, "");
//                    return;
//                }
//                if (!kline0.isVolumnLarge()) {
//                    error(INFO, date, "");
//                    return;
//                }
//            }
//
//
//            if (astockState.dayPos == ZERO_STAND_MA120_DOWNENTITY_LARGER) {
//                float vv = KLineUtil.compareMax(kline0.getClose(), kline0.getMA60());
//                if (kline0.getMA60() > kline0.getClose()) {
//                    if (vv < 2) {
//                        error(INFO, date, "");
//                        return;
//                    }
//                }
//            }
//
//
//            if (astockState.dayPos == DOWNMA250_DOWN_MA120SMALLER) {
//                float vv = KLineUtil.compareMax(kline0.getMA120(), kline0.getMA250());
//                if (vv < 6) {
//                    error(INFO, date, "");
//                    return;
//                }
//            }
//            if (astockState.dayPos == DOWNMA120_DOWNMA250SMALLER) {
//                float vv = KLineUtil.compareMax(kline0.getMA120(), kline0.getMA250());
//                if (vv < 6) {
//                    error(INFO, date, "");
//                    return;
//                }
//            }
//
//
//            if (astockState.dayPos == ZERO_STAND_MA120_UPENTITY_LARGER) {
//                boolean flag = kline0.hasFallDown(30, 4, 5);
//                if (!flag) {
//                    error(INFO, date, "hasFallDown");
//                    return;
//                }
//            }
//
//
////            if (astockState.dayPos == ZERO_BOTTOM_STANDMA120_TOP_CROSSMA250_ENTITY_EQUAL) {
////                Kline nextOKLine = kline0.getNextLineDownFirstUpMA250SecondOK2(35);
////                if (nextOKLine != null && nextOKLine.hasNextZT(6)) {
////                    ok_(file, INFO, date, kline0, weekline, nextN, msg, context);
////                    return;
////                }
////            }
//            //ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_DOWNENTITY_LARGER
//            if (astockState.dayPos == ZERO_BOTTOM_STANDMA250_TOP_CROSSMA120_DOWNENTITY_LARGER) {
//                float spacee = astockState.space;
//                if (spacee > 5) {
//                    Kline.RET flag = kline0.hasNextFallToMA250(15, 1.5f);
//                    if (flag.flag) {
//                        //follow up tp ma120
//                        Kline aKline = flag.kline;
//                        Kline.RET flag2 = aKline.hasUpToMAI(3, 120, 0.5f);
//                        if (!flag2.flag) {
//                            return;
//                        }
//                        nextN = aKline;
//                        if (nextN.hasNextZF(5, 3, 10)) {
//                            ok_(file, INFO, date, kline0, weekline, nextN, msg, context);
//                            return;
//                        } else {
//                            error(INFO, date, "");
//                            return;
//                        }
//                    } else {
//                        error(INFO, date, "");
//                        return;
//                    }
//                }
//            }
//
//
//        }
//
//        if (kline0.monthKline != null) {
//            boolean tmp = kline0.monthKline.hasNOmoreMax(100);
//            if (tmp) {
//                error(INFO, date, "前高不久");
//                return;
//            }
//            if (kline0.monthKline.hasDZ(15, 60)) {
//                astockState = kline0.getPOS();
//                if (astockState.dayPos == ZERO_BOTTOM_STANDMA250_TOP_DOWNMA120_SPACE_LARGER) {
//
//                } else {
//                    error(INFO, date, "大涨15day 60");
//                    return;
//                }
//
//            }
//        }
//
//        //POS 0, no fall down to ma10
//        if (kline0.weekline != null) {
//            astockState = kline0.getPOS();
//            if (astockState.isStandMA250OrMa120()) {
//                boolean flagx = kline0.hasPrevUpMAI(10, 10, 1);
//                if (flagx) {
//                    return;
//                }
//            }
//            if (astockState.dayPos == ZERO_BOTTOM_STANDMA250_TOP_DOWNMA120_SPACE_SMALLER) {
//                return;
//            }
////            if (astockState.dayPos == ZERO_BOTTOM_STANDMA120_TOP_DOWNMA250_SPACE_SMALLER) {
////                return;
////            }
//        }
//
//        TOTAL_OK++;
//        String command = String.format("mainProcess(\"%s\", \"%s\", \"KN\", true, true);//N", file, date);
//        if (Stragety.kn == 3) {
////            Log.log(command);
//        }
//        ok_(file, INFO, date, kline0, weekline, nextN, msg, context);
//    }
//
//    public static Guaili monthguali(String INFO, String date, Kline kline0) {
//        Guaili aGuaili = new Guaili();
//        MonthKline monthKline = kline0.monthKline;
//        if (monthKline == null) {
//            int a = 0;
//        }
//        float nZFmonth = monthKline.getPrevZFLHIncludeSelf(11);
//        float guali = 0;
//        nZFmonth = monthKline.getPrevZFLHIncludeSelfOpen(24);
//
//        float ma120 = monthKline.getMA120();
//        float ma60 = monthKline.getMA60();
//        float ma30 = monthKline.getMA30();
//        float ma10 = monthKline.getMA10();
//        aGuaili.ma1030 = KLineUtil.compareMax(ma10, ma30);
//        aGuaili.ma3060 = KLineUtil.compareMax(ma30, ma60);
//        aGuaili.ma60120 = KLineUtil.compareMax(ma60, ma120);
//        return aGuaili;
//    }
//
//    public static Guaili weekguali(String INFO, String date, Kline kline0) {
//        Guaili aGuaili = new Guaili();
//        Weekline monthKline = kline0.weekline;
//        float guali = 0;
//        float ma120 = monthKline.getMA120();
//        float ma60 = monthKline.getMA60();
//        float ma30 = monthKline.getMA30();
//        float ma10 = monthKline.getMA10();
//        aGuaili.ma1030 = KLineUtil.compareMax(ma10, ma30);
//        aGuaili.ma3060 = KLineUtil.compareMax(ma30, ma60);
//        aGuaili.ma60120 = KLineUtil.compareMax(ma60, ma120);
//        return aGuaili;
//    }
//
//    public static boolean isJiaji(String INFO, String date, Kline next) {
//        Weekline monthKline = next.weekline;
//        float guali = 0;
//        float ma120 = monthKline.getMA120();
//        float ma60 = monthKline.getMA60();
//        float ma30 = monthKline.getMA30();
//        float ma10 = monthKline.getMA10();
//        if (KLineUtil.compareMax(ma30, ma60) < 6) {
//            if (next.getClose() > ma30 && next.getClose() < ma60) {
//                return true;
//            }
//            if (next.getClose() < ma30 && next.getClose() > ma60) {
//                return true;
//            }
//
//            if (next.getOpen() > ma30 && next.getOpen() < ma60) {
//                return true;
//            }
//            if (next.getOpen() < ma30 && next.getOpen() > ma60) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static int TOTAL_OK = 0;
//
//
//    public static boolean testGuaili(String INFO, String date, Kline kline0, Weekline weekline, Kline nextN) {
//        boolean guailiFlag0 = kline0.isGuailiChange_m120_m250();
//        boolean guailiFlag0_10_60 = kline0.isGuailiChange_m10_m60();
//        if (guailiFlag0) {
//            return true;
//        } else if (guailiFlag0_10_60) {
//            return true;
//        } else {
//            boolean guailiFlagNextN_m120_m250 = nextN.isGuailiChange_m120_m250();
//            boolean guailiFlagNextN_m60_m120 = nextN.isGuailiChange_m60_m120();
//            boolean guailiFlagNextN_m30_m250 = nextN.isGuailiChange_m30_m250();
//            boolean guailiFlagNextN_m30_m60 = nextN.isGuailiChange_m30_m60();
//            //week chance
//            if (weekline != null) {
//                boolean wguailiFlagNextN_m10_m120 = nextN.weekline.isGuailiChange_m10_m120();
//                boolean wguailiFlagNextN_m120_m250 = nextN.weekline.isGuailiChange_m120_m250();
//                boolean wguailiFlagNextN_m60_m120 = nextN.weekline.isGuailiChange_m60_m120();
//                boolean wguailiFlagNextN_m30_m250 = nextN.weekline.isGuailiChange_m30_m250();
//                boolean wguailiFlagNextN_m30_m60 = nextN.weekline.isGuailiChange_m30_m60();
//                if (wguailiFlagNextN_m120_m250) {
//                    return true;
//                } else if (wguailiFlagNextN_m60_m120) {
//                    return true;
//                } else if (wguailiFlagNextN_m30_m250) {
//                    return true;
//                } else if (wguailiFlagNextN_m10_m120) {
//                    return true;
//                } else if (wguailiFlagNextN_m30_m60) {
//                    return true;
//                }
//
//                //603999
//                StockState stockState = kline0.getPOS();
//                if (stockState.isDayPosOne()) {
//                    Weekline pre = weekline.prev();
//                    boolean wguailiFlagNextN_m10_m120_ = pre.isGuailiChange_m10_m120();
//                    boolean wguailiFlagNextN_m120_m250_ = pre.isGuailiChange_m120_m250();
//                    boolean wguailiFlagNextN_m60_m120_ = pre.isGuailiChange_m60_m120();
//                    boolean wguailiFlagNextN_m30_m250_ = pre.isGuailiChange_m30_m250();
//                    boolean wguailiFlagNextN_m30_m60_ = pre.isGuailiChange_m30_m60();
//                    boolean wguailiFlagNextN_m60_m250_ = pre.isGuailiChange_m60_m250();
//                    if (wguailiFlagNextN_m120_m250_) {
//                        return true;
//                    } else if (wguailiFlagNextN_m60_m120_) {
//                        return true;
//                    } else if (wguailiFlagNextN_m30_m250_) {
//                        return true;
//                    } else if (wguailiFlagNextN_m10_m120_) {
//                        return true;
//                    } else if (wguailiFlagNextN_m30_m60_) {
//                        return true;
//                    } else if (wguailiFlagNextN_m60_m250_) {
//                        float ma60 = kline0.getMA60();
//                        if (KLineUtil.compareMaxSign(kline0.getClose(), ma60) < 2) {
//                            return true;
//                        }
//                    }
//
//                }
//            }
//            if (guailiFlagNextN_m120_m250) {
//                return true;
//            } else if (guailiFlagNextN_m60_m120) {
//                return true;
//            } else if (guailiFlagNextN_m30_m250) {
//                return true;
//            } else if (guailiFlagNextN_m30_m60) {
//                float ma60 = nextN.getMA60();
//                if (KLineUtil.compareMaxSign(nextN.getClose(), ma60) < 2) {
//                    return true;
//                }
//            } else {
//                //last chance
//                for (int i = 0; i < 5; i++) {
//                    Kline nextN_ = nextN.next(i + 1);
//                    if (nextN_ == null) {
//                        break;
//                    }
//                    boolean guailiFlagNextN_m120_m250_ = nextN_.isGuailiChange_m120_m250();
//                    boolean guailiFlagNextN_m60_m120_ = nextN_.isGuailiChange_m60_m120();
//                    boolean guailiFlagNextN_m30_m250_ = nextN_.isGuailiChange_m30_m250();
//                    if (guailiFlagNextN_m120_m250_) {
//                        return true;
//                    } else if (guailiFlagNextN_m60_m120_) {
//                        return true;
//                    } else if (guailiFlagNextN_m30_m250_) {
//                        return true;
//                    }
//
//                    if (weekline != null) {
//                        boolean wguailiFlagNextN_m120_m250 = nextN.weekline.isGuailiChange_m120_m250();
//                        boolean wguailiFlagNextN_m60_m120 = nextN.weekline.isGuailiChange_m60_m120();
//                        boolean wguailiFlagNextN_m30_m250 = nextN.weekline.isGuailiChange_m30_m250();
//                        if (wguailiFlagNextN_m120_m250) {
//                            return true;
//                        } else if (wguailiFlagNextN_m60_m120) {
//                            return true;
//                        } else if (wguailiFlagNextN_m30_m250) {
//                            return true;
//                        }
//                    }
//                }
//
//                return false;
//            }
//        }
//        return false;
//    }
//
//
//    public static String testCode = "";
//    public static String testDate = "";
//
//    public static boolean isTouchWeek(String file, String date, Kline kline, Weekline weekline, int useweek) {
//        boolean weekFlag = false;
//        if (useweek == 1) {
//            float ma10 = weekline.getMA10();
//            float ma30 = weekline.getMA30();
//            float ma60 = weekline.getMA60();
//            float ma120 = weekline.getMA120();
//            float ma250 = weekline.getMA250();
//            float min = kline.getMin();
//
//            int trendType10 = weekline.getMA10TrendType(10);
//            int trendType30 = weekline.getMA30TrendType(10);
//            int trendType60 = weekline.getMA60TrendType(10);
//            //hengpan
//            if (trendType10 == 2) {
//                if (kline.getOpen() > ma10 && KLineUtil.compareMax(min, ma10) <= 5.5) {
//                    weekFlag = true;
//                }
//            }
//
//            if (kline.getOpen() > ma30 && KLineUtil.compareMax(min, ma30) < 2) {
//                float vv = KLineUtil.compareMax(min, ma30);
//                if (vv > 0.5) {
//                    weekFlag = true;
//                }
//            }
//            if (kline.getOpen() > ma60 && KLineUtil.compareMax(min, ma60) < 2) {
//                weekFlag = true;
//            }
//            if (kline.getOpen() > ma120 && KLineUtil.compareMax(min, ma120) < 2) {
//                weekFlag = true;
//            }
//            if (kline.getOpen() > ma250 && KLineUtil.compareMax(min, ma250) < 2) {
//                weekFlag = true;
//            }
//            if (weekFlag) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }
//
//    //    Kline kline0
//    public static boolean prsIsSmallN(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
//        float zf = kline0.getZhangfu();
//        if (zf > 9.5f) {
//            return false;
//        }
//        if (kline0.getMin() < kline0.getMA250()) {
//            return false;
//        }
//        if (kline0.getMin() < kline0.getMA120()) {
//            return false;
//        }
//        Kline next1 = kline0.next();
//        Kline next2 = kline0.next(2);
//        Kline next3 = kline0.next(3);
//        if (kline0.next() == null || next2 == null) {
//            return false;
//        }
//        if (zf < 7.5f || kline0.getMAXZhangfu() < 8.5f) {
//            return false;
//        }
//        boolean flag = kline0.isTouchBottomMAI(30, 7, 80);
//        if (!flag) {
//            return false;
//        }
//        if (next1.isShadownUp(50)) {
//            return false;
//        }
//        if (next2 != null && next2.isShadownUp(50)) {
//            return false;
//        }
//
//        float angle60 = kline0.getSlant(60, 2);
//        if (angle60 < 15) {
//            error(file, date, "pos:10, ma30 is hor");
//            return false;
//        }
//
//        boolean flag2 = next1.getMin() > next1.getMAI(30);
//        if (!flag2) {
//            return false;
//        }
//        if (next2 != null) {
//            boolean flag3 = next2.getMin() > next2.getMAI(30);
//            if (!flag3) {
//                return false;
//            }
//        }
//
//        if (kline0.weekline != null) {
//            StockState astockState = kline0.getPOS();
//            if (astockState.dayPos != 10 && astockState.dayPos != ZERO_STAND_MA120) {
//                return false;
//            }
//        }
//
//
//        if (next2.getMin() < next2.getMA30()) {
//            if (next3 != null && next3.getMin() < next3.getMA30()) {
//                return false;
//            }
//            return false;
//        }
//
//        if (KLineUtil.compareMaxSign(next2.getMin(), kline0.getMA30()) < 1) {
//            nextN = next2;
//            if (!Stragety.isResult) {
//                Result result = new Result(file, date);
//                result.stragety = KN.class.getSimpleName();
//                Stragety.addLine(result);
//            } else {
//                ok_(file, INFO, date, kline0, kline0.weekline, nextN, "", context);
//            }
//            return true;
//        }
//
//        if (next3 != null) {
//            boolean flag3 = next3.getMin() > next3.getMAI(30);
//            if (!flag3) {
//                return false;
//            }
//            if (KLineUtil.compareMaxSign(next3.getMin(), kline0.getMA30()) < 1) {
//                nextN = next3;
//                if (!Stragety.isResult) {
//                    Result result = new Result(file, date);
//                    result.stragety = KN.class.getSimpleName();
//                    Stragety.addLine(result);
//                } else {
//                    ok_(file, INFO, date, kline0, kline0.weekline, nextN, "", context);
//                }
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public static boolean prsIsZTS(String file, String INFO, String date, Kline kline0, Weekline weekline, Kline nextN, String msg, LineContext context) {
//        float zf = kline0.getZhangfu();
//        if (zf > 0 || zf < -2) {
//            return false;
//        }
//
//        Kline prev1 = kline0.prev();
//        Kline prev2 = kline0.prev(2);
//        if (prev1.getZhangfu() > 0 && prev1.getZhangfu() < -2) {
//            return false;
//        }
//        if (prev2.getZhangfu() > 0) {
//            return false;
//        }
//
//        float ma250 = kline0.getMA250();
//        if (kline0.getMax() * 1.08 < ma250) {
//            return false;
//        }
//        float nextZTV = kline0.getClose() * 1.1f;
//        boolean flag = kline0.isNextZTOnMAI();
//        if (!flag) {
//            return false;
//        }
//
//        if (!Stragety.isResult) {
//            Result result = new Result(file, date);
//            result.stragety = KN.class.getSimpleName();
//            Stragety.addLine(result);
//        } else {
//            nextN = kline0;
//            ok2_(file, INFO, date, kline0, nextN, kline0.weekline, nextN, "", context);
//        }
//        return true;
//
//    }
//
//    public static void prsIsN_(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
//        if (INFO.contains("ST") || file.startsWith("SH#688") || file.startsWith("SZ#300")) {
//            return;
//        }
//
//        int idx = getIdx(days, date);
//        if (idx < 200) {
//            return;
//        }
//
//        Kline kline0 = days.get(idx);
//        if (kline0.getZhangfu() > 9.5) {
//            Log.log("" + INFO + "  " + date);
//        }
//    }
//
//    public static void prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext context) {
//        if (Stragety.kn == 4) {
//            prsIsN_(file, days, date, weeks, moths, usemonth, useweek, context);
//            return;
//        }
//        if (INFO.contains("ST") || file.startsWith("SH#688") || file.startsWith("SZ#300")) {
//            return;
//        }
//
//        if (!StringUtil.isBlank(KN.testCode)) {
//            if (!file.contains(KN.testCode) || !date.equalsIgnoreCase(KN.testDate)) {
//                return;
//            }
//        }
//
//        int idx = getIdx(days, date);
//        if (idx < 200) {
//            return;
//        }
//
//        Kline kline0 = days.get(idx);
//        boolean shortFlag = prsIsSmallN(file, INFO, date, kline0, null, null, "", context);
//        if (shortFlag) {
//            return;
//        }
//        boolean shortFlag2 = prsIsZTS(file, INFO, date, kline0, null, null, "", context);
//        if (shortFlag2) {
//            return;
//        }
//
//        if (kline0.getZhangfu() < 9.5) {
//            return;
//        }
//
//        Kline nextLine = kline0.next();
////        if(nextLine.isShadownUp(50) && nextLine.getVolume()>kline0.getVolume()) {
////            error(INFO, date, "upline and vol");
////            return;
////        }
//
//
//        float nextMA250 = nextLine.getMA250();
//        float flag2 = KLineUtil.compareMaxSign(nextLine.getMin(), nextLine.getMA120());
//        if (flag2 >= 0 && flag2 < 0.5) {
//            if (nextLine.getOpen() > nextMA250 && nextLine.getClose() < nextMA250) {
//                if (kline0.weekline != null) {
//                    StockState astockState = kline0.getPOS();
//                    if (astockState.dayPos != 3) {
//                        return;
//                    }
//
//                    if (KLineUtil.compareMaxSign(nextLine.getMin(), kline0.getClose()) < 6) {
//                        if (!Stragety.isResult) {
//                            Result result = new Result(file, date);
//                            result.stragety = KN.class.getSimpleName();
//                            Stragety.addLine(result);
//                        } else {
//                            ok2_(file, INFO, date, kline0, kline0.next(), kline0.weekline, kline0.next(), "", context);
//                        }
//                        return;
//                    }
//                }
//            } else {
//                return;
//            }
//        } else {
//            if (nextLine.getOpen() > nextMA250 && nextLine.getClose() < nextMA250) {
//                Kline nextLine2 = kline0.next(2);
//                if (nextLine2 != null && nextLine2.getClose() > nextMA250) {
//                    error(INFO, date, "nextLine.getOpen()>nextMA250 && nextLine.getClose()<nextMA250");
//                    return;
//                }
//            }
//        }
//
//        if (nextLine.isShadownUp(50) && nextLine.getVolume() > kline0.getVolume() * 2) {
//            error(INFO, date, "next isShadownUp and volumn double");
//            return;
//        }
//        boolean ztFlag = kline0.hasPrevZT(10);
//        if (ztFlag) {
//            error(INFO, date, "kline0 hasPrevZT");
//            return;
//        }
//
//        float jump = kline0.getJumpZF();
//        if (jump > 4.5) {
//            error(INFO, date, "kline0 jump > 4.5");
//            return;
//        }
//
//        if (useweek == 1) {
//            StockState astockState = kline0.getPOS();
//            if (astockState.isDayPosZeroBottomUpEntityLarge()) {
//                boolean fflag = false;
//                boolean fflag2 = false;
//                boolean fflag3 = false;
//                boolean fflag4 = false;
//                if (!kline0.isMa30SLantDown()) {
//                    fflag = true;
//                }
//                //no volumn
//                if (kline0.isVolumnLarge()) {
//                    fflag2 = true;
//                }
//                boolean flag = kline0.nextMa10LagerThanOther(2);
//                if (flag) {
//                    fflag3 = true;
//                }
//                Kline anextLine = kline0.next();
//                if (anextLine.getZhangfu() < -5.5) {
//                    fflag4 = true;
//                }
//
//                if (fflag && fflag2 && fflag3 && fflag4) {
//                    if (anextLine.getMin() > anextLine.getMA250()) {
//                        if (KLineUtil.compareMaxSign(anextLine.getMin(), anextLine.getMA250()) < 1) {
//                            ok_(file, INFO, date, kline0, kline0.weekline, anextLine, "", context);
//                            return;
//                        }
//                    }
//                }
//            }
//
//            if (astockState.isDayPosOne()) {
//                float guailiMA10MA30 = kline0.getGuaili(10, 30);
//                if (guailiMA10MA30 > 4.5) {
//                    boolean fflag = false;
//                    if (kline0.hasFallDownMAI(30, 3, 2.2f)) {
//                        fflag = true;
//                    } else {
//
//                    }
//                    if (!fflag) {
//                        error(INFO, date, "");
//                        return;
//                    }
//                }
//                boolean fallFlag = kline0.hasNextFall(3, 5);
//                if (!fallFlag) {
//                    boolean fallFlag2 = kline0.hasNextFallToMA30(12, 1);
//                    if (!fallFlag2) {
//                        error(INFO, date, "!hasNextFallToMA30(3, 3) && !hasNextFallToMA30(12,1)");
//                        return;
//                    }
//                }
//                boolean flag = kline0.hasHigherForAfter(3);
//                if (flag) {
//                    error(INFO, date, "letter to many higher");
//                    return;
//                }
//            } else if (astockState.isDayPosZeroBottomUpEntityLarge()) {
//                if (useweek == 1) {
//
//                    if (kline0.isMa30SLantDown()) {
//                        error(INFO, date, "ma30 slant down");
//                        return;
//                    }
//
//                    //no volumn
//                    if (!kline0.isVolumnLarge()) {
//                        error(INFO, date, "no volumn larger");
//                        return;
//                    }
////                    POS = 2 MA30 >> MAI no gold cross
//                    boolean flag = kline0.nextMa30LagerThanOther(10);
//                    if (!flag) {
//                        error(INFO, date, "ma30 no gold cross");
//                        return;
//                    }
//
//
//                }
//            } else if (astockState.dayPos == KLineUtil.ZERO_DOWN) {
//                if (useweek == 1) {
//                    return;
//                }
//            }
//        }
//
//        if (kline0.isShadownDown(60)) {
//            error(file, date, "长下影线");
//            return;
//        }
//        Kline next = kline0.next();
//        if (kline0.isShadownUp(50f)) {
//            error(file, date, "");
//            return;
//        }
//
//        if (kline0.nextTouchUPMA(4, 120)) {
//        } else {
//            if (next.getZhangfu() > 0.1 && next.getClose() > next.getOpen()) {
//                if (next.next() != null) {
//                    if (next.next().getMin() > kline0.getMax()) {
//                        if (next.next(4) != null) {
//                            if (next.next(4).getMin() > kline0.getMax()) {
//                                return;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//
//        int num = kline0.prev().getPrevZTNumSelf(12);
//        if (num >= 2) {
//            error(file, date, "");
//            return;
//        }
//        float dma30 = kline0.getMA30();
//        float dma60 = kline0.getMA60();
//        float dma120 = kline0.getMA120();
//        float dma250 = kline0.getMA250();
//        if (kline0.getOpen() > dma120 && kline0.getOpen() > dma60) {
//            if (KLineUtil.compareMax(dma60, dma120) > 8) {
//                error(file, date, "");
//                return;
//            }
//        }
//
//        if (kline0.getOpen() < dma30 && kline0.getOpen() < dma60 && kline0.getOpen() < dma120 && kline0.getOpen() < dma250) {
//            if (dma30 < dma60 && dma60 < dma120 && dma120 < dma250) {
//            } else {
//                if (KLineUtil.compareMax(dma250, kline0.getClose()) < 2.0) {
//                    error(file, date, "");
//                    return;
//                }
//            }
//        }
//
//        num = kline0.getPrevZTNum(4);
//        if (num >= 1) {
//            error(file, date, "");
//            return;
//        }
//        float nZFmonth = 0;
//        if (usemonth == 1) {
//            MonthKline monthKline = kline0.monthKline;
//            nZFmonth = monthKline.getPrevZFLHIncludeSelf(11);
//            if (nZFmonth > 150) {
//                error(file, date, "");
//                return;
//            }
//            boolean isMonthEnd = kline0.isMonthEnd(2);
//            if (isMonthEnd) {
//                //月末涨幅大，次月回调
//                float zf = monthKline.getZhangfu();
//                if (zf > 15) {
//                    error(file, date, "月末涨幅大，次月回调:" + zf);
//                    return;
//                }
//            }
//            nZFmonth = monthKline.getPrevZFLHIncludeSelfOpen(24);
//            float ma120 = monthKline.getMA120();
//            float ma60 = monthKline.getMA60();
//            float ma30 = monthKline.getMA30();
//            float ma10 = monthKline.getMA10();
//            if (KLineUtil.compareMax(ma30, ma60) > 40) {
//                error(file, date, "compareMax(ma30, ma60)> 50");
//                return;
//            }
//            //guali to large
//        }
//
//        StockState stockState = null;
////        if (useweek == 1) {
////            Weekline weekline = kline0.weekline;
////            stockState = kline0.getPOS();
////            boolean flag1 = KLineUtil.test(KLineUtil.space2, kline0, weekline);
////            if (flag1) {
////                boolean flag11 = KLineUtil.test(KLineUtil.space, kline0, weekline);
////                if (!flag1) {
////                    error(file, date, "");
////                    return;
////                }
////            }
////            float nZFWeek = 0;
////            nZFWeek = weekline.getPrevZFLHIncludeSelf(7);
////            if (nZFWeek > 53) {
////                error(file, date, "");
////                return;
////            }
////            float ma10 = weekline.getMA10();
////            float ma30 = weekline.getMA30();
////            float ma60 = weekline.getMA60();
////            float ma120 = weekline.getMA120();
////            float ma250 = weekline.getMA250();
////            if (ma30 > 0) {
////                float zf = KLineUtil.compareMax(ma10, ma30);
////                if (zf > 13) {
////                    error(file, date, "");
////                    return;
////                }
////            }
////        }
//
//        float nzhangfu = kline0.getPrevZFLH(1);
//        if (nzhangfu > 10) {
//        } else {
//            error(file, date, "");
//            return;
//        }
//
//        boolean weekFlag = false;
//        for (int i = 1; i <= 16; i++) {
//            Kline nextN = kline0.next(i);
//            if (i == 1) {
//                if (nextN.getOpen() > nextN.getMA120() && nextN.getClose() < nextN.getMA120()) {
//                    return;
//                }
//            }
//            if (i <= 1) {
//                continue;
//            }
//            Kline linei = kline0.next(i);
//            if (linei == null) {
//                error(file, date, "");
//                return;
//            }
//
//
//            if (nextN.getDate().equalsIgnoreCase("2023/07/07")) {
//                int a = 0;
//            }
//            if (i == 1) {
//                //special condition
//                boolean flag3 = testGuaili(INFO, date, kline0, null, nextN);
//                //跌幅不能太小
//                float downFrac = KLineUtil.compareMaxSign(nextN.getClose(), kline0.getClose());
//                boolean downFlag = true;
//                if (downFrac >= 0) {
//                    downFlag = false;
//                }
//                if (Math.abs(downFrac) < 6) {
//                    downFlag = false;
//                }
//                if (flag3 && downFlag) {
//                    after_num++;
//                    if (!Stragety.isResult) {
//                        Result result = new Result(file, date);
//                        result.stragety = KN.class.getSimpleName();
//                        Stragety.addLine(result);
//                        return;
//                    } else {
//                        ok(file, INFO, date, kline0, null, nextN, "", context);
//                        return;
//                    }
//                }
//
//                continue;
//            }
//
//            float nZF = kline0.getNextDF(i);
//            if (nZF > 0) {
//                continue;
//            }
//
//
//            if (usemonth == 1) {
//                MonthKline monthKline = kline0.monthKline.prev();
//                MonthKline monthKline2 = monthKline.prev();
//                MonthKline monthKline3 = monthKline2.prev();
//                float ma120 = monthKline.getMA120();
//                float ma60 = monthKline.getMA60();
//                float ma30 = monthKline.getMA30();
//                float ma10 = monthKline.getMA10();
//                if (ma30 > 0) {
//                    float zf = KLineUtil.compareMax(ma10, ma30);
//                    if (zf > 30) {
//                        if (useweek == 1) {
//                            Weekline weekline = kline0.weekline;
//                            float wma10 = weekline.getMA10();
//                            float wma30 = weekline.getMA30();
//                            float wma60 = weekline.getMA60();
//                            float wma120 = weekline.getMA120();
//                            float wma250 = weekline.getMA250();
//                            if (wma30 > 0) {
//                                float wzf = KLineUtil.compareMax(wma10, wma30);
//                                if (wzf > 12) {
//                                    error(file, date, "");
//                                    return;
//                                }
//                            }
//                            if (wma60 > 0) {
//                                float wzf = KLineUtil.compareMax(wma10, wma60);
//                                if (wzf > 15) {
//                                    error(file, date, "");
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            if (useweek == 1) {
//                boolean fflag = isJiaji(INFO, date, nextN);
//                if (fflag) {
//                    error(file, date, "isJiaji");
//                    return;
//                }
//
//
//                if (nextN.getDate().equalsIgnoreCase("2023/04/26")) {
//                    int a = 0;
//                }
//                boolean flag6 = false;
//                if (stockState.dayPos == 10) {
//                    float angle = kline0.getABSSlant(30, 2);
//                    float angle60 = kline0.getABSSlant(60, 2);
//                    if (angle < 15 && angle60 < 15) {
//                        error(file, date, "pos:10, ma30 is hor");
//                        return;
//                    }
//                }
//                if (stockState.dayPos == 26) {
//                    if (i == 3 && nZF < -5.9) {
//                        float ftac = KLineUtil.compareMax(nextN.getMin(), nextN.getMA30());
//                        float ftac2 = KLineUtil.compareMax(nextN.getMin(), nextN.getMA60());
//                        weekFlag = isTouchWeek(file, date, nextN, nextN.weekline, useweek);
//                        if ((ftac < 3.1 || ftac2 < 2.5) && weekFlag) {
//                            flag6 = true;
//                        }
//                    }
//                    if (flag6) {
//                        saveOK(file, INFO, date, kline0, kline0.weekline, nextN, "", context);
//                        return;
//                    } else {
//                        if (i >= 5) {
//                            error(file, date, "daypos is middle high(10), touch line day is too many.");
//                            return;
//                        }
//                    }
//                }
//                if (stockState.dayPos == 10) {
//                    if (i < 3 && nZF < -7) {
//                        float frac = KLineUtil.compareMaxSign(nextN.getMA30(), nextN.getMA60());
//                        if (frac <= 0.7) {
//                            flag6 = true;
//                        }
//                    }
//
//                    //连续下跌7个点， 金叉
//                    if (stockState.dayPos == 10 && i <= 2 && nZF < -7) {
//                        boolean frac9 = nextN.isALlLineOKAndOneCross();
//                        weekFlag = isTouchWeek(file, date, nextN, nextN.weekline, useweek);
//                        if (frac9 && weekFlag) {
//                            flag6 = true;
//                        }
//                    }
//                    //连续下跌7个点， 金叉
//                    if (stockState.dayPos == 10 && i <= 2 && nZF < -7) {
//                        boolean frac9 = nextN.isALlLineOK();
//                        weekFlag = isTouchWeek(file, date, nextN, nextN.weekline, useweek);
//
//                        float ftac = KLineUtil.compareMax(nextN.getMin(), nextN.getMA30());
//                        float ftac2 = KLineUtil.compareMax(nextN.getMin(), nextN.getMA60());
//                        weekFlag = isTouchWeek(file, date, nextN, nextN.weekline, useweek);
//                        if (frac9 && (ftac < 3.1 || ftac2 < 2.5) && weekFlag) {
//                            flag6 = true;
//                        }
//                    }
//
//                    if (flag6) {
//                        saveOK(file, INFO, date, kline0, kline0.weekline, nextN, "", context);
//                        return;
//                    } else {
//                        if (i >= 5) {
//                            error(file, date, "daypos is middle high(10), touch line day is too many.");
//                            return;
//                        }
//                    }
//                }
//
//                weekFlag = isTouchWeek(file, date, nextN, nextN.weekline, useweek);
//                if (weekFlag) {
//                    if (!Stragety.isResult) {
//                        Result result = new Result(file, date);
//                        result.stragety = KN.class.getSimpleName();
//                        Stragety.addLine(result);
//                        return;
//                    } else {
//                        boolean fflag2 = isJiaji(INFO, date, nextN);
//                        if (fflag2) {
//                            error(file, date, "");
//                            return;
//                        }
//
//                        Weekline weekline = nextN.weekline;
//                        boolean flag3 = testGuaili(INFO, date, kline0, weekline, nextN);
//                        if (flag3) {
//                            after_num++;
//                        } else {
////                            error(INFO, date, "error guaili");
////                            return;
//                        }
//                    }
//                }
//            }
//            if (usemonth == 1 && moths.size() > 30) {
//                MonthKline monthKline = kline0.monthKline;
//                float ma120 = monthKline.getMA120();
//                float ma60 = monthKline.getMA60();
//                float ma30 = monthKline.getMA30();
//                float ma10 = monthKline.getMA10();
//                if (ma10 < ma30 && ma10 < ma60 && ma60 < ma120 && monthKline.getOpen() < ma10) {
//                    StockState stockState1 = kline0.getPOS();
//                    if (stockState1.dayPos == 11) {
//
//                    } else {
//                        float zhengfu = monthKline.getPrevZFLHNoSelf(4);
//                        if (zhengfu > 20) {
//                            error(file, date, "4months zhengfu(" + zhengfu + ") >20:");
//                            return;
//                        }
//                    }
//
//                }
//
//                if (ma120 > 0) {
//                    if (nextN.touch(ma120, 1.0f)) {
//                        error(file, date, "nextN.touch(ma120, 1.0f)");
//                        return;
//                    }
//                }
//            }
//
//            float ma250 = kline0.getMA250();
//            if (i > 0 && i < 3) {
//                float maxf = nextN.getMax();
//                float ff = KLineUtil.compareMax(maxf, ma250);
//                if (ff < 1) {
//                    error(file, date, "");
//                    return;
//                }
//            }
//
//            if (nZF < -8 && (i >= 2 && i <= 3)) {
//                Map<String, Bankuai> infoMap = AllModel.infoMap;
//                if (!Stragety.isResult) {
//                    Result result = new Result(file, date);
//                    result.stragety = KN.class.getSimpleName();
//                    Stragety.addLine(result);
//                } else {
//                    after_num++;
//                    if (useweek == 1) {
//                        Weekline weekline = nextN.weekline;
//                        boolean flag3 = testGuaili(INFO, date, kline0, weekline, nextN);
//                        //跌幅不能太小
//                        float downFrac = KLineUtil.compareMaxSign(nextN.getClose(), kline0.getClose());
//                        boolean downFlag = true;
//                        if (downFrac >= 0) {
//                            downFlag = false;
//                        }
//                        if (Math.abs(downFrac) < 6) {
//                            downFlag = false;
//                        }
//                        if (flag3 && downFlag) {
//                            ok(file, INFO, date, kline0, weekline, nextN, "", context);
//                            return;
//                        } else {
//                            error(INFO, date, "guaili error");
//                            return;
//                        }
//                    }
//
////                    ok(INFO, date, kline0, null, nextN, "");
////                    Log.log("mainProcess(\""+file+"\", \""+date+"\", \"KN\", true, true);");
//                }
//
//                return;
//            }
//            boolean flag = false;
//            int line = 0;
//
//            if (kline0.getMax() > kline0.getMA30() && nextN.touch(nextN.getMA30(), 1.0f)) {
//                line = 30;
//                flag = true;
//            }
//            if (kline0.getMax() > kline0.getMA60() && nextN.touch(nextN.getMA60(), 1.0f)) {
//                line = 60;
//                flag = true;
//            }
//            if (kline0.getMax() > kline0.getMA120() && nextN.touch(nextN.getMA120(), 2.0f)) {
//                line = 120;
//                flag = true;
//            }
//            if (kline0.getMax() > kline0.getMA250() && kline0.getMax() > nextN.getMA30() && nextN.touch(kline0.getMA250(), 2.0f)) {
//                line = 250;
//                flag = true;
//            }
//            float vFrac = 2.1f;
//            if (useweek == 1) {
//                if (stockState.dayPos == 10) {
//                    vFrac = 2.1f;
//                } else {
//                    vFrac = 1.0f;
//                }
//            }
//
//            int len = Math.abs(DateUtil.getDayLen(DateUtil.stringToDate3(nextN.getDate()), DateUtil.stringToDate3(kline0.getDate())));
//            if (len < 5 && nextN.touch(nextN.getMA10(), vFrac)) {
//                line = 10;
//                if (useweek == 1) {
//                    weekFlag = isTouchWeek(file, date, nextN, nextN.weekline, useweek);
//                    if (weekFlag) {
//                        flag = true;
//                    } else {
//                        if (stockState.dayPos == 10) {
//                            flag = true;
//                        } else {
//                            flag = false;
//                        }
//                    }
//                } else {
//                    flag = false;
//                }
//            }
//
//            float nZF2 = kline0.getPrevZF(3);
//            if (nZF2 > 17) {
//                error(file, date, "+17% in 3 days");
//                return;
//            }
//
//
//            if (flag) {
//                List<String> list = null;
//                Map<String, Bankuai> infoMap = AllModel.infoMap;
//                String code = file.substring(3).replace(".txt", "");
//
//
//                if (!Stragety.isResult) {
//                    Result result = new Result(file, date);
//                    result.stragety = KN.class.getSimpleName();
//                    Stragety.addLine(result);
//                    return;
//                } else {
//                    if (nZF < -7) {
//                        float frac = KLineUtil.compareMaxSign(nextN.getMA30(), nextN.getMA60());
//                        if (frac <= 0.6) {
//                            flag = true;
//                        }
//                    }
//
//                    if (useweek == 1) {
//                        Weekline weekline = nextN.weekline;
//                        if (nextN.getDate().equalsIgnoreCase("2023/05/22")) {
//                            int a = 0;
//                        }
//                        boolean flag3 = testGuaili(INFO, date, kline0, weekline, nextN);
//                        if (nextN.getClose() < nextN.getMA10()) {
//                            float frac = KLineUtil.compareMaxSign(nextN.getMin(), nextN.getMA30());
//                            float frac1 = KLineUtil.compareMaxSign(nextN.getMin(), nextN.getMA60());
//                            float frac2 = KLineUtil.compareMaxSign(nextN.getMin(), nextN.getMA120());
//                            float frac3 = KLineUtil.compareMaxSign(nextN.getMin(), nextN.getMA250());
//                            float minFrac = KLineUtil.getMin(frac, frac1, frac2, frac3);
//                            if (minFrac > 0 && minFrac < 2) {
//                                if (flag3) {
//                                    ok(file, INFO, date, kline0, null, nextN, "", context);
//                                    return;
//                                } else {
////                            error(INFO, date, "testGuaili error");
////                            return;
//                                }
//                            }
//                        } else {
//                            if (flag3) {
//                                ok(file, INFO, date, kline0, null, nextN, "", context);
//                                return;
//                            } else {
////                            error(INFO, date, "testGuaili error");
////                            return;
//                            }
//                        }
//
//                    }
//                }
////                return;
//            }
//        }
//
//        error(file, date, "16days over!");
//        return;
//
//    }
//
//    public void prs(String file, List<Kline> days, String date, String
//            stragetyName, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, LineContext contxt) {
//        prsIsN(file, days, date, weeks, moths, usemonth, useweek, contxt);
//    }
//
//
//}
