package com.alading.tool.stock;

import com.huaien.core.util.DateUtil;

import java.util.*;

public class MaxSection {

    List<MPoint> list = new ArrayList<>();
    public List<MaxPoint> points = new ArrayList<>();
    int period = 30;

    public MaxSection(int i) {
        period = i;
    }

    public double  getMAX(Kline kline) {
        if (period == 10) {
            return kline.getMA10();
        }
        if (period == 30) {
            return kline.getMA30();
        }
        if (period == 60) {
            return kline.getMA60();
        }
        if (period == 120) {
            return kline.getMA120();
        }
        return kline.getMA30();
    }

    public static MonthKline getMinLine(List<MonthKline> all, int fromIdx, int toIdx) {
        int prevIdx = 0;
        double  min = 99999;
        MonthKline minLine = null;
        for (int i = 0; i < all.size(); i++) {
            MonthKline kline = all.get(i);
            if (kline.key.equalsIgnoreCase("2021/01/29")) {
                int a = 0;
            }
            int idx = kline.getIdx();
            if (idx < fromIdx || idx > toIdx) {
                continue;
            }
            if (kline.getMin() < min) {
                min = kline.getMin();
                minLine = kline;
            }
        }
        return minLine;
    }

    public void initDay(List<Kline> all, int fromIdx, int toIdx) {
        int prevIdx = 0;
        for (int i = 0; i < all.size(); i++) {
//            if (i < 29) {
//                continue;
//            }
            Kline kline = all.get(i);
            if (kline.date.equalsIgnoreCase("2020/11/06")) {
                int a = 0;
                a++;
            }
            int idx = kline.getIdx();
            if (idx < fromIdx || idx > toIdx) {
                continue;
            }
            double  max = getMAX(kline);
            if (kline.touch(max, 1.5f)) {
                if (list.size() == 0) {
                    MPoint mPoint = new MPoint();
                    mPoint.period = period;
                    list.add(mPoint);
                    mPoint.add(kline);
                } else {
                    if (idx - prevIdx == 1) {
                        MPoint mPoint = list.get(list.size() - 1);
                        mPoint.add(kline);
                    } else {
                        MPoint mPoint = new MPoint();
                        mPoint.period = period;
                        list.add(mPoint);
                        mPoint.add(kline);
                    }
                }
                prevIdx = i;
            }
        }

        for (MPoint mPoint : list) {
            mPoint.period = period;
            try {
                mPoint.prsDays(all);
            }catch (Exception e) {

            }
        }
        ////////////////////////////////////////////
        for (int i = 0; i < list.size(); i++) {
            MPoint mPoint = list.get(i);
//            Log.log(mPoint.list.get(0).toString());
            if (i ==0) {
                Kline temp2 = (Kline) all.get(fromIdx);
                Kline temp = (Kline) mPoint.list.get(0);
                double  v1 = temp2.getClose();
                if(v1>temp.getMA120()) {
                    MaxPoint point2 = getMaxDay(all, temp2.getIdx(), mPoint.klineIdx);
                    point2.flag = 0;
                    point2.flag2 = MPoint.MAX;
                    add(point2);
                }else {
                    MaxPoint point2 = getMinDay(all, temp2.getIdx(), mPoint.klineIdx);
                    point2.flag = 0;
                    point2.flag2 = MPoint.MIN;
                    add(point2);
                }

                Kline tmp = (Kline) mPoint.list.get(mPoint.list.size()/2);
                MaxPoint mPoint_ = new MaxPoint();
                mPoint_.value = tmp.getClose();
                mPoint_.kline = tmp;
                mPoint_.flag2 = mPoint.type;
                add(mPoint_);
                continue;
            }
            if (i == list.size()-1) {

                Kline tmp = (Kline) mPoint.list.get(mPoint.list.size()/2);
                MaxPoint mPoint_ = new MaxPoint();
                mPoint_.value = tmp.getClose();
                mPoint_.kline = tmp;
                mPoint_.flag2 = mPoint.type;
                add(mPoint_);

                Kline lastLine = (Kline) all.get(toIdx);
                Kline currentLine = (Kline) mPoint.list.get(mPoint.list.size()/2);
                double  v1 = lastLine.getClose();
                if(v1>=currentLine.getMA60()) {
                    MaxPoint point2 = getMaxDay(all, mPoint.klineIdx, lastLine.getIdx());
                    point2.flag = 0;
                    point2.flag2 = MPoint.MAX;
                    add(point2);
                }else {
                    MaxPoint point2 = getMinDay(all, mPoint.klineIdx, lastLine.getIdx());
                    point2.flag = 0;
                    point2.flag2 = MPoint.MIN;
                    add(point2);
                }


                continue;
            }
            MPoint mPoint2 = list.get(i + 1);
            if (mPoint.type == MPoint.UP) {
                Kline tmp = mPoint.list.get(mPoint.list.size()/2);
                MaxPoint mPoint_ = new MaxPoint();
                mPoint_.value = tmp.getClose();
                mPoint_.kline = tmp;
                mPoint_.flag2 = MPoint.UP;
                add(mPoint_);

                MaxPoint point = getMaxDay(all, mPoint.klineIdx, mPoint2.klineIdx);
                point.flag = 1;
                point.flag2 = MPoint.MAX;
                add(point);
            } else if (mPoint.type == MPoint.DOWN) {
                Kline tmp = mPoint.list.get(mPoint.list.size()/2);
                MaxPoint mPoint_ = new MaxPoint();
                mPoint_.value = tmp.getClose();
                mPoint_.kline = tmp;
                mPoint_.flag2 = MPoint.DOWN;
                add(mPoint_);

                MaxPoint point = getMinDay(all, mPoint.klineIdx, mPoint2.klineIdx);
                point.flag = 0;
                point.flag2 =MPoint.MIN;
                add(point);
            } else if (mPoint.type == MPoint.UP_DOWN) {
                MaxPoint point = getMaxDay(all, mPoint.list.get(0).getIdx(), mPoint.list.get(mPoint.list.size() - 1).getIdx());
                point.flag = 1;
                point.flag2 = 0;
                point.flag2 = MPoint.UP_DOWN;
                add(point);
                int len = mPoint2.klineIdx - mPoint.klineIdx;
                if(len>10) {
                    Kline temp = all.get((mPoint2.klineIdx + mPoint.klineIdx)/2);
                    double  v1 = temp.getClose();
                    if(v1>getMAX(temp)) {
                        MaxPoint point2 = getMaxDay(all, mPoint.klineIdx, mPoint2.klineIdx);
                        point2.flag = 0;
                        point2.flag2 = MPoint.MAX;
                        add(point2);
                    }else {
                        MaxPoint point2 = getMinDay(all, mPoint.klineIdx, mPoint2.klineIdx);
                        point2.flag = 0;
                        point2.flag2 = MPoint.MIN;
                        add(point2);
                    }
                }else {
                    MaxPoint point2 = getMinDay(all, mPoint.klineIdx, mPoint2.klineIdx);
                    point2.flag = 0;
                    point2.flag2 = MPoint.MIN;
                    add(point2);
                }

            } else if (mPoint.type == MPoint.DOWN_UP) {
                MaxPoint point = getMinDay(all, mPoint.list.get(0).getIdx(), mPoint.list.get(mPoint.list.size() - 1).getIdx());
                point.flag = 0;
                point.flag2 = MPoint.DOWN_UP;
                add(point);

                int len = mPoint2.klineIdx - mPoint.klineIdx;
                if(len>10) {
                    Kline temp = all.get((mPoint2.klineIdx + mPoint.klineIdx)/2);
                    double  v1 = temp.getClose();
                    if(v1>temp.getMA120()) {
                        MaxPoint point2 = getMaxDay(all, mPoint.klineIdx, mPoint2.klineIdx);
                        point2.flag = 0;
                        point2.flag2 = 2;
                        point2.flag2 = MPoint.MAX;
                        add(point2);
                    }else {
                        MaxPoint point2 = getMinDay(all, mPoint.klineIdx, mPoint2.klineIdx);
                        point2.flag = 0;
                        point2.flag2 = 3;
                        point2.flag2 = MPoint.MIN;
                        add(point2);
                    }
                }else {
                    MaxPoint point2 = getMaxDay(all, mPoint.klineIdx, mPoint2.klineIdx);
                    point2.flag = 1;
                    point2.flag2 = 2;
                    point2.flag2 = MPoint.MAX;
                    add(point2);
                }

            }
        }
    }

    public void add(MaxPoint point) {
        int len = points.size();
        point.pIdx = len;
        points.add(point);
        point.points = points;
    }

    public void init(List<MonthKline> all, int fromIdx, int toIdx) {
        int prevIdx = 0;
        for (int i = 0; i < all.size(); i++) {
//            if (i < 29) {
//                continue;
//            }
            MonthKline kline = all.get(i);
            if (kline.key.equalsIgnoreCase("2020/11/06")) {
                int a = 0;
                a++;
            }
            int idx = kline.getIdx();
            if (idx < fromIdx || idx > toIdx) {
                continue;
            }
            double  max = getMAX(kline);
            if (kline.touch(max, 1.5f)) {
                if (list.size() == 0) {
                    MPoint mPoint = new MPoint();
                    mPoint.period = period;
                    list.add(mPoint);
                    mPoint.add(kline);
                } else {
                    if (idx - prevIdx == 1) {
                        MPoint mPoint = list.get(list.size() - 1);
                        mPoint.add(kline);
                    } else {
                        MPoint mPoint = new MPoint();
                        mPoint.period = period;
                        list.add(mPoint);
                        mPoint.add(kline);
                    }
                }
                prevIdx = i;
            }
        }

        for (MPoint mPoint : list) {
            mPoint.period = period;
            try {
                mPoint.prs(all);
            }catch (Exception e) {

            }
        }
        ////////////////////////////////////////////
        for (int i = 0; i < list.size(); i++) {
            MPoint mPoint = list.get(i);
//            Log.log(mPoint.list.get(0).toString());
            if (i ==0) {
                MonthKline temp2 = (MonthKline) all.get(fromIdx);
                MonthKline temp = (MonthKline) mPoint.list.get(0);
                double v1 = temp2.getClose();
                if(v1>temp.getMA120()) {
                    MaxPoint point2 = getMax(all, temp2.getIdx(), mPoint.klineIdx);
                    point2.flag = 0;
                    point2.flag2 = MPoint.MAX;
                    points.add(point2);
                }else {
                    MaxPoint point2 = getMin(all, temp2.getIdx(), mPoint.klineIdx);
                    point2.flag = 0;
                    point2.flag2 = MPoint.MIN;
                    points.add(point2);
                }

                MonthKline tmp = (MonthKline) mPoint.list.get(mPoint.list.size()/2);
                MaxPoint mPoint_ = new MaxPoint();
                mPoint_.value = tmp.getClose();
                mPoint_.kline = tmp;
                mPoint_.flag2 = mPoint.type;
                points.add(mPoint_);
                continue;
            }
            if (i == list.size()-1) {

                MonthKline tmp = (MonthKline) mPoint.list.get(mPoint.list.size()/2);
                MaxPoint mPoint_ = new MaxPoint();
                mPoint_.value = tmp.getClose();
                mPoint_.kline = tmp;
                mPoint_.flag2 = mPoint.type;
                points.add(mPoint_);

                MonthKline lastLine = (MonthKline) all.get(toIdx);
                MonthKline currentLine = (MonthKline) mPoint.list.get(mPoint.list.size()/2);
                double v1 = lastLine.getClose();
                if(v1>=currentLine.getMA120()) {
                    MaxPoint point2 = getMax(all, mPoint.klineIdx, lastLine.getIdx());
                    point2.flag = 0;
                    point2.flag2 = MPoint.MAX;
                    points.add(point2);
                }else {
                    MaxPoint point2 = getMin(all, mPoint.klineIdx, lastLine.getIdx());
                    point2.flag = 0;
                    point2.flag2 = MPoint.MIN;
                    points.add(point2);
                }


                continue;
            }
            MPoint mPoint2 = list.get(i + 1);
            if (mPoint.type == MPoint.UP) {
                MonthKline tmp = (MonthKline) mPoint.list.get(mPoint.list.size()/2);
                MaxPoint mPoint_ = new MaxPoint();
                mPoint_.value = tmp.getClose();
                mPoint_.kline = tmp;
                mPoint_.flag2 = MPoint.UP;
                points.add(mPoint_);

                MaxPoint point = getMax(all, mPoint.klineIdx, mPoint2.klineIdx);
                point.flag = 1;
                point.flag2 = MPoint.MAX;
                points.add(point);
            } else if (mPoint.type == MPoint.DOWN) {
                MonthKline tmp = (MonthKline) mPoint.list.get(mPoint.list.size()/2);
                MaxPoint mPoint_ = new MaxPoint();
                mPoint_.value = tmp.getClose();
                mPoint_.kline = tmp;
                mPoint_.flag2 = MPoint.DOWN;
                points.add(mPoint_);

                MaxPoint point = getMin(all, mPoint.klineIdx, mPoint2.klineIdx);
                point.flag = 0;
                point.flag2 =MPoint.MIN;
                points.add(point);
            } else if (mPoint.type == MPoint.UP_DOWN) {
                MaxPoint point = getMax(all, mPoint.list.get(0).getIdx(), mPoint.list.get(mPoint.list.size() - 1).getIdx());
                point.flag = 1;
                point.flag2 = 0;
                point.flag2 = MPoint.UP_DOWN;
                points.add(point);
                int len = mPoint2.klineIdx - mPoint.klineIdx;
                if(len>10) {
                    MonthKline temp = all.get((mPoint2.klineIdx + mPoint.klineIdx)/2);
                    double v1 = temp.getClose();
                    if(v1>getMAX(temp)) {
                        MaxPoint point2 = getMax(all, mPoint.klineIdx, mPoint2.klineIdx);
                        point2.flag = 0;
                        point2.flag2 = MPoint.MAX;
                        points.add(point2);
                    }else {
                        MaxPoint point2 = getMin(all, mPoint.klineIdx, mPoint2.klineIdx);
                        point2.flag = 0;
                        point2.flag2 = MPoint.MIN;
                        points.add(point2);
                    }
                }else {
                    MaxPoint point2 = getMin(all, mPoint.klineIdx, mPoint2.klineIdx);
                    point2.flag = 0;
                    point2.flag2 = MPoint.MIN;
                    points.add(point2);
                }

            } else if (mPoint.type == MPoint.DOWN_UP) {
                MaxPoint point = getMin(all, mPoint.list.get(0).getIdx(), mPoint.list.get(mPoint.list.size() - 1).getIdx());
                point.flag = 0;
                point.flag2 = MPoint.DOWN_UP;
                points.add(point);

                int len = mPoint2.klineIdx - mPoint.klineIdx;
                if(len>10) {
                    MonthKline temp = all.get((mPoint2.klineIdx + mPoint.klineIdx)/2);
                    double v1 = temp.getClose();
                    if(v1>temp.getMA120()) {
                        MaxPoint point2 = getMax(all, mPoint.klineIdx, mPoint2.klineIdx);
                        point2.flag = 0;
                        point2.flag2 = 2;
                        point2.flag2 = MPoint.MAX;
                        points.add(point2);
                    }else {
                        MaxPoint point2 = getMin(all, mPoint.klineIdx, mPoint2.klineIdx);
                        point2.flag = 0;
                        point2.flag2 = 3;
                        point2.flag2 = MPoint.MIN;
                        points.add(point2);
                    }
                }else {
                    MaxPoint point2 = getMax(all, mPoint.klineIdx, mPoint2.klineIdx);
                    point2.flag = 1;
                    point2.flag2 = 2;
                    point2.flag2 = MPoint.MAX;
                    points.add(point2);
                }

            }
        }


    }

    public static List<MonthKline> getMin(List<MonthKline> weeklines, List<MaxPoint> all, int from, int to) {
        List<MonthKline> lines = new ArrayList<>();
        for (int i = 0; i < all.size() - 1; i++) {
            MaxPoint m1 = all.get(i);
            if(m1.flag2 == 3) {
                lines.add((MonthKline) m1.kline);
            }
        }
        return lines;
    }

    public List<MaxPoint> getRange(int from, int to) {
        if(from<0) {
            from = 0;
        }
        List<MaxPoint> lines = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            MaxPoint m1 = points.get(i);
            if(m1.flag2 != MPoint.MIN) {
               continue;
            }
            if(m1.idx<from) {
                continue;
            }
            if(m1.idx>to) {
                continue;
            }
            lines.add(m1);
        }
        return lines;
    }

    public List<MaxPoint> getRangeAllMAX(int from, int to) {
        if(from<0) {
            from = 0;
        }
        List<MaxPoint> lines = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            MaxPoint m1 = points.get(i);
            if(m1.flag2 != MPoint.MAX) {
                continue;
            }
            if(m1.idx<from) {
                continue;
            }
            if(m1.idx>to) {
                continue;
            }
            lines.add(m1);
        }
        return lines;
    }

    public static MaxPoint getMax(List<MaxPoint> all) {
        double max = 0;
        int idx = -1;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).flag == 0) {
                continue;
            }
            if (all.get(i).kline.getMax() > max) {
                max = all.get(i).kline.getMax();
                idx = i;
            }
        }
        if (idx >= 0) {
            return all.get(idx);
        }
        return null;
    }

    public static MaxPoint getMinPoint(List<MaxPoint> all) {
        double min = 9999;
        int idx = -1;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).flag == 1) {
                continue;
            }
            if (all.get(i).kline.getMin() < min) {
                min = all.get(i).kline.getMin();
                idx = i;
            }
        }
        if (idx >= 0) {
            return all.get(idx);
        }
        return null;
    }

    public static List<MaxPoint> getBetween(List<MaxPoint> points, Date v1, Date v2) {
        List<MaxPoint> ret = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            MaxPoint maxPoint = points.get(i);
            if (DateUtil.isBetweenTime(maxPoint.kline.getDateObj(), v1, v2)) {
                ret.add(maxPoint);
            }
        }
        return ret;
    }

    public boolean isBottomRecent(List<MonthKline> all) {
        MaxPoint point = points.get(points.size() - 1);
        MonthKline monthKline = all.get(all.size() - 1);
        int len = monthKline.getIdx() - point.kline.getIdx() + 1;
        if (len > 8) {
            return true;
        }
        return false;
    }

    public static MaxPoint getMaxDay(List<Kline> all, int from, int to) {
        double max = 0;
        int idx = 0;
        for (int i = from; i <= to; i++) {
            if (all.get(i).getMax() > max) {
                max = all.get(i).getMax();
                idx = i;
            }
        }
        MaxPoint maxPoint = new MaxPoint();
        maxPoint.idx = idx;
        maxPoint.value = max;
        maxPoint.kline = all.get(idx);
        return maxPoint;
    }

    public static MaxPoint getMinDay(List<Kline> all, int from, int to) {
        double min = 99999;
        int idx = 0;
        for (int i = from; i <= to; i++) {
            if (all.get(i).getMin() < min) {
                min = all.get(i).getMin();
                idx = i;
            }
        }
        MaxPoint maxPoint = new MaxPoint();
        maxPoint.idx = idx;
        maxPoint.value = min;
        maxPoint.kline = all.get(idx);
        return maxPoint;
    }

    public static MaxPoint getMax(List<MonthKline> all, int from, int to) {
        double max = 0;
        int idx = 0;
        for (int i = from; i <= to; i++) {
            if (all.get(i).getMax() > max) {
                max = all.get(i).getMax();
                idx = i;
            }
        }
        MaxPoint maxPoint = new MaxPoint();
        maxPoint.idx = idx;
        maxPoint.value = max;
        maxPoint.kline = all.get(idx);
        return maxPoint;
    }

    public static MaxPoint getMin(List<MonthKline> all, int from, int to) {
        double min = 99999;
        int idx = 0;
        for (int i = from; i <= to; i++) {
            if (all.get(i).getMin() < min) {
                min = all.get(i).getMin();
                idx = i;
            }
        }
        MaxPoint maxPoint = new MaxPoint();
        maxPoint.idx = idx;
        maxPoint.value = min;
        maxPoint.kline = all.get(idx);
        return maxPoint;
    }


    public List<MaxPoint> getMins() {
        List<MaxPoint> ret = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            MaxPoint maxPoint = points.get(i);
            if (maxPoint.flag == 0) {
                ret.add(maxPoint);
            }
        }
        return ret;
    }

    public List<MaxPoint> geFirst2Mins() {
        List<MaxPoint> mins = getMins();
        mins.sort(new Comparator<MaxPoint>() {
            @Override
            public int compare(MaxPoint o1, MaxPoint o2) {
                return (int) (o1.value - o2.value);
            }
        });
        List<MaxPoint> ret = new ArrayList<>();
        ret.add(mins.get(0));
        ret.add(mins.get(1));
        ret.sort(new Comparator<MaxPoint>() {
            @Override
            public int compare(MaxPoint o1, MaxPoint o2) {
                return (int) (o2.kline.getDateObj().getTime() - o1.kline.getDateObj().getTime());
            }
        });

        return ret;
    }


    public boolean isBigBottom() {
        List<MaxPoint> mins = geFirst2Mins();
        if (mins.size() < 2) {
            return false;
        }
        int len = DateUtil.getDurationMonth(mins.get(1).kline.getDateObj(), mins.get(0).kline.getDateObj());
        int fraction = (int) mins.get(1).kline.compare(mins.get(0).kline);
        if (len > 12 && fraction > -20 && fraction < 5) {
            return true;
        }
        return false;
    }

}
