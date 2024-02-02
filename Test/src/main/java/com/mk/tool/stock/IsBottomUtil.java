package com.mk.tool.stock;

import java.util.List;

/**
 * 大底
 */
public class IsBottomUtil extends Stragety {

    static int NUM = 60;

    public static boolean prsIsN2(String file, Kline kline0, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek) {
        try {
            int idx = getIdx(days, date);
            if (idx < 200) {
                return false;
            }
            float minValjue = 0;
            float minIdx = 0;

            boolean flag = false;
            int offsetLen = 0;
            if (moths != null) {
                int monIdx = getMonthIdx(moths, date);
                if (monIdx < 20) {
                    return false;
                }
                MonthKline current = moths.get(monIdx);

                int fromIdx = 0;//monIdx-17;
                MaxSection maxSection60 = new MaxSection(30);
                maxSection60.init(moths, fromIdx, monIdx);
                List<MaxPoint> points = maxSection60.getRange(monIdx - NUM, monIdx);
                KLineUtil.sortAescMonthlinePoint(points);
                if (points.size() == 0) {
                    int a = 0;
                    a++;
                    return false;
                }
                MonthKline minMonth = (MonthKline) points.get(0).kline;
                if (monIdx == minMonth.getIdx()) {
                    flag = true;
                }
                if (points.size() > 1) {
                    MonthKline minMonth2 = (MonthKline) points.get(1).kline;
                    offsetLen = Math.abs(minMonth2.getIdx() - minMonth.getIdx() + 1);
                    if (monIdx == minMonth2.getIdx()) {
                        float frac = KLineUtil.compareMax(minMonth.getMin(), minMonth2.getMin());
                        if (frac < 10) {
                            flag = true;
                        }
                        if (minMonth2.touch(minMonth2.getMA60())) {
                            flag = true;
                        }
                        float frac2 = KLineUtil.compareMax(minMonth2.getMin(), minMonth2.getMA60());
                        if (minMonth2.getMin() < minMonth2.getMA60() && frac2 < 18) {
                            flag = true;
                        }

                        if (minMonth2.getIdx() > minMonth.getIdx()) {
                            float tfrac = KLineUtil.compareMax(minMonth.getMin(), minMonth2.getMin());
                            if (frac < 20) {
                                flag = true;
                            }
                        }

                    } else {
                        float frac = KLineUtil.compareMax(minMonth.getMin(), minMonth2.getMin());
                        float frac2 = KLineUtil.compareMax(minMonth.getEntityMin(), minMonth2.getMin());
                        if (frac < 20) {
                            flag = true;
                        }
                        if (frac2 < 20) {
                            flag = true;
                        }
                        if (minMonth.getEntityMin() < 1 && minMonth2.getMin() < 1) {
                            flag = true;
                        }
                        MonthKline maxLine = (MonthKline) current.getPrevMAXKline(NUM);
                        MonthKline minLine = (MonthKline) maxLine.getPrevMINKline(6);
                        float afrac = KLineUtil.compareMax(maxLine.getMax(), minLine.getMin());
                        if (afrac < 100) {
                            return false;
                        }
                        int dltLen = maxLine.getIdx() - minLine.getIdx() + 1;
                        if (dltLen <= 3) {

                        }
                        int durationLen = monIdx - maxLine.getIdx() - 1;
                        MonthKline minLine2 = (MonthKline) current.getPrevMINKline(durationLen);

                        float jf = KLineUtil.compareMax(minLine2.getMin(), maxLine.getMax());
                        int curDlt = current.getIdx() - minLine2.getIdx();

                        if (jf > 50 && curDlt <= 3) {
                            return true;
                        }
                        int a = 0;
                    }
                }
            } else {
                flag = true;
            }

            if (!flag) {
                return false;
            }
            return true;
        } catch (Exception e) {

        }
        return false;
    }


    public static boolean prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek) {
        try {


            int idx = getIdx(days, date);
            if (idx < 200) {
                return false;
            }
            Kline kline0 = days.get(idx);
            boolean flag = false;
            int offsetLen = 0;
            if (moths != null) {
                int monIdx = getMonthIdx(moths, date);
                if (monIdx < 20) {
                    return false;
                }
                MonthKline current = moths.get(monIdx);

                int fromIdx = 0;//monIdx-17;
                MaxSection maxSection60 = new MaxSection(30);
                maxSection60.init(moths, fromIdx, monIdx);
                List<MaxPoint> points = maxSection60.getRange(monIdx - NUM, monIdx);
                KLineUtil.sortAescMonthlinePoint(points);
                if (points.size() == 0) {
                    int a = 0;
                    a++;
                    return false;
                }
                MonthKline minMonth = (MonthKline) points.get(0).kline;
                if (monIdx == minMonth.getIdx()) {
                    flag = true;
                }
                if (points.size() > 1) {
                    MonthKline minMonth2 = (MonthKline) points.get(1).kline;
                    offsetLen = Math.abs(minMonth2.getIdx() - minMonth.getIdx() + 1);
                    if (monIdx == minMonth2.getIdx()) {
                        float frac = KLineUtil.compareMax(minMonth.getMin(), minMonth2.getMin());
                        if (frac < 10) {
                            flag = true;
                        }
                        if (minMonth2.touch(minMonth2.getMA60())) {
                            flag = true;
                        }
                        float frac2 = KLineUtil.compareMax(minMonth2.getMin(), minMonth2.getMA60());
                        if (minMonth2.getMin() < minMonth2.getMA60() && frac2 < 18) {
                            flag = true;
                        }

                        if (minMonth2.getIdx() > minMonth.getIdx()) {
                            float tfrac = KLineUtil.compareMax(minMonth.getMin(), minMonth2.getMin());
                            if (frac < 20) {
                                flag = true;
                            }
                        }

                    } else {
                        float frac = KLineUtil.compareMax(minMonth.getMin(), minMonth2.getMin());
                        float frac2 = KLineUtil.compareMax(minMonth.getEntityMin(), minMonth2.getMin());
                        if (frac < 20) {
                            flag = true;
                        }
                        if (frac2 < 20) {
                            flag = true;
                        }
                        if (minMonth.getEntityMin() < 1 && minMonth2.getMin() < 1) {
                            flag = true;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            }
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean prsIsN3(String file, Kline kline0, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek) {
        int idx = getIdx(days, date);
        if (idx < 200) {
            return false;
        }
        boolean flag = false;

        Weekline weekline = kline0.weekline;
        if (!kline0.hasPrevZT(45)) {
            return false;
        }

        boolean fflag = weekline.allIsLessAbsThan(5, 1, 3.0f);

        if (!fflag) {
            return false;
        }
        return true;
    }

    public static boolean prsIsN4(String file, Kline kline0, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek) {
        int idx = getIdx(days, date);
        if (idx < 200) {
            return false;
        }
        boolean flag = false;

        Weekline weekline = kline0.weekline;
        if (!kline0.hasPrevZT(45)) {
            return false;
        }

        boolean fflag = weekline.allIsLessAbsThan(19, 3, 3.0f);
        if (!fflag) {
            return false;
        }
        return true;
    }

    public static boolean prsIsNDF(String file, Kline kline0, List<Kline> days, String date, int num, float frac) {
        int idx = getIdx(days, date);
        if (idx < 200) {
            return false;
        }

        Weekline weekline = kline0.weekline;
        float v = weekline.getPrevDF(num);
        if (v < frac) {
            return false;
        }
        return true;
    }

    @Override
    public void prs(String file, List<Kline> days, String date, String stagetyName, List<Weekline> weeks, List<MonthKline> moths, int usem, int usew, LineContext context) {

    }
}
