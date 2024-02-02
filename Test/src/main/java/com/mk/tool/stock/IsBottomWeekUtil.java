package com.mk.tool.stock;

import java.util.List;

/**
 * 大底
 */
public class IsBottomWeekUtil extends Stragety {

    static int NUM = 60*5;

    public static boolean prsIsN2(String file, Kline kline0, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek) {
        int idx = getIdx(days, date);
        if (idx < 200) {
            return false;
        }
        float minValjue = 0;
        float minIdx = 0;

        boolean flag = false;
        int offsetLen = 0;
        if (moths != null) {
            int monIdx = getWeekIdx(weeks, date);
            if (monIdx < 20) {
                return false;
            }
            Weekline current = weeks.get(monIdx);

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
            Weekline minMonth = (Weekline) points.get(0).kline;
            if (monIdx == minMonth.getIdx()) {
                flag = true;
            }
            if (points.size() > 1) {
                Weekline minMonth2 = (Weekline) points.get(1).kline;
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
                    Weekline maxLine = (Weekline) current.getPrevMAXKline(NUM);
                    Weekline minLine = (Weekline) maxLine.getPrevMINKline(6);
                    float afrac = KLineUtil.compareMax(maxLine.getMax(), minLine.getMin());
                    if (afrac < 100) {
                        return false;
                    }
                    int dltLen = maxLine.getIdx() - minLine.getIdx() + 1;
                    if (dltLen <= 3) {

                    }
                    int durationLen = monIdx - maxLine.getIdx() - 1;
                    Weekline minLine2 = (Weekline) current.getPrevMINKline(durationLen);

                    float jf = KLineUtil.compareMax(minLine2.getMin(), maxLine.getMax());
                    int curDlt = current.getIdx() - minLine2.getIdx();

                    if (jf > 50 && curDlt <= 3) {
                        return true;
                    }
                    int a = 0;
                }
            }
        }

        if (!flag) {
            return false;
        }
        return true;
    }

    public static boolean prsIsN(String file, List<Kline> days, String date, List<Weekline> weeks, List<MonthKline> moths, int usemonth, int useweek, int lineNumber) {
        int idx = getIdx(days, date);
        if (idx < 200) {
            return false;
        }
        Kline kline0 = days.get(idx);
        boolean flag = false;
        int offsetLen = 0;
        if (weeks != null) {
            int weekIdx = getWeekIdx(weeks, date);
            if (weekIdx < 20) {
                return false;
            }

            int fromIdx = 0;//weekIdx-17;
            MaxWeekSection maxSection60 = new MaxWeekSection(30);
            maxSection60.init(weeks, fromIdx, weekIdx);
            List<MaxPoint> points = maxSection60.getRange(weekIdx - NUM, weekIdx);
            KLineUtil.sortAescMonthlinePoint(points);
            if (points.size() == 0) {
                int a = 0;
                a++;
                return false;
            }
            Weekline minMonth = (Weekline) points.get(0).kline;
            if (weekIdx == minMonth.getIdx()) {
                flag = true;
            }
            if (points.size() > 1) {
                Weekline minMonth2 = (Weekline) points.get(1).kline;
                offsetLen = Math.abs(minMonth2.getIdx() - minMonth.getIdx() + 1);
                if (weekIdx == minMonth2.getIdx()) {
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
                    offsetLen = Math.abs(minMonth2.getIdx() - minMonth.getIdx() + 1);
                    if(offsetLen<6) {
                        if (frac < 11) {
                            flag = true;
                        }
                        //long bottom line
                        if (frac2 < 5) {
                            flag = true;
                        }
                        if (minMonth.getEntityMin() < 1 && minMonth2.getMin() < 1) {
                            flag = true;
                        }
                    }else {
                        Kline maxLine = kline0.getPrevMAXKline(lineNumber);
//                        Kline maxLine = kline0.getPrevMAXKline(220*10);
                        int maxIdx = maxLine.getIdx();
                        Kline minLine = kline0.getPrevMINKline(40);
                        float frac1 = KLineUtil.compareMax(minLine.getMin(), maxLine.getMax());
                        if(frac1>=50) {
                            float frac3 = KLineUtil.compareMax(kline0.getClose(), minMonth.getMin());
                            if(frac3>=270) {
                                return false;
                            }
                            flag = true;
                        }
                    }

                }
            }
        }else {
            flag = true;
        }

        if (!flag) {
            return false;
        }


        return true;
    }


    @Override
    public void prs(String file, List<Kline> days, String date, String stagetyName, List<Weekline> weeks, List<MonthKline> moths, int usem, int usew, LineContext context) {

    }
}
