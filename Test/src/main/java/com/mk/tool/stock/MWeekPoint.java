package com.mk.tool.stock;

import java.util.ArrayList;
import java.util.List;

public class MWeekPoint {
    public static final int DOWN = 0;
    public static final int UP = 1;
    public static final int HOR = 2;
    public static final int UP_DOWN = 3;
    public static final int DOWN_UP = 4;

    public static final int MAX = 5;
    public static final int MIN = 6;





    public int idx;
    public int klineIdx;
    public int type;//0 down 1 up 2 horizontal
    public Kline kline;
    public List<Kline> list = new ArrayList<>();
    int period = 30;

    public float getMAX(Kline kline) {
        if(period == 10) {
            return kline.getMA10();
        }
        if(period == 30) {
            return kline.getMA30();
        }
        if(period == 60) {
            return kline.getMA60();
        }
        if(period == 120) {
            return kline.getMA120();
        }
        return kline.getMA30();
    }


    public void add(Kline kline) {
        list.add(kline);
    }

    public void prsDays(List<Kline> all) {
        Kline prev = null;
        Kline next = null;
        if (list.size() == 1) {
            idx = 0;
            Kline kline = list.get(0);
            klineIdx = kline.getIdx();
            prev = kline.prev();
            if(kline.getIdx() >= all.size()-1) {
                next = null;
            }else {
                next = kline.next();
            }

            if(prev.getClose() > getMAX(kline)) {
                if(next==null) {
                    type = DOWN;
                }else {
                    if(next.getClose() > getMAX(kline)) {
                        type = DOWN_UP;
                    }else {
                        type = DOWN;
                    }
                }
            }else {
                if(next==null) {
                    type = UP;
                }else {
                    if(next.getClose() > getMAX(kline)) {
                        type = UP;
                    }else {
                        type = UP_DOWN;
                    }
                }
            }

        } else {
            prev = list.get(0);
            next = list.get(list.size() - 1);
            idx = (prev.getIdx() + next.getIdx())/2;
            klineIdx = all.get(idx).getIdx();
            if(prev == null) {
                int a =0;
                a++;
            }
            Kline prev_ = prev.prev();
            Kline next_;
            if(next.getIdx() == all.size()-1) {
                next_ = null;
            }else {
                next_ = next.next();
            }

            if(next_ != null) {
                if(prev_.getClose() > prev_.getMAI(period)) {
                    if(next_.getClose() < next_.getMAI(period)) {
                        type = DOWN;
                    }else {
                        type = DOWN_UP;
                    }
                }

                if(prev_.getClose() < prev_.getMAI(period)) {
                    if(next_.getClose() > next_.getMAI(period)) {
                        type = UP;
                    }else {
                        type = UP_DOWN;
                    }
                }
            }else {
                if(prev_.getClose() > getMAX(prev_)) {
                    if(next_==null) {
                        type = DOWN;
                    }
                }else {
                    if(next_==null) {
                        type = UP;
                    }
                }
            }

        }
    }

    public void prs(List<Weekline> all) {
        Kline prev = null;
        Kline next = null;
        if (list.size() == 1) {
            idx = 0;
            Kline kline = list.get(0);
            klineIdx = kline.getIdx();
            prev = kline.prev();
            if(kline.getIdx() >= all.size()-1) {
                next = null;
            }else {
                next = kline.next();
            }

            if(prev.getClose() > getMAX(kline)) {
                if(next==null) {
                    type = DOWN;
                }else {
                    if(next.getClose() > getMAX(kline)) {
                        type = DOWN_UP;
                    }else {
                        type = DOWN;
                    }
                }
            }else {
                if(next==null) {
                    type = UP;
                }else {
                    if(next.getClose() > getMAX(kline)) {
                        type = UP;
                    }else {
                        type = UP_DOWN;
                    }
                }
            }

        } else {
            prev = list.get(0);
            next = list.get(list.size() - 1);
            idx = (prev.getIdx() + next.getIdx())/2;
            klineIdx = all.get(idx).getIdx();
            if(prev == null) {
                int a =0;
                a++;
            }
            Kline prev_ = prev.prev();
            Kline next_;
            if(next.getIdx() == all.size()-1) {
                next_ = null;
            }else {
                next_ = next.next();
            }

            if(next_ != null) {
                if(prev_.getClose() > prev_.getMAI(period)) {
                    if(next_.getClose() < next_.getMAI(period)) {
                        type = DOWN;
                    }else {
                        type = DOWN_UP;
                    }
                }

                if(prev_.getClose() < prev_.getMAI(period)) {
                    if(next_.getClose() > next_.getMAI(period)) {
                        type = UP;
                    }else {
                        type = UP_DOWN;
                    }
                }
            }else {
                if(prev_.getClose() > getMAX(prev_)) {
                    if(next_==null) {
                        type = DOWN;
                    }
                }else {
                    if(next_==null) {
                        type = UP;
                    }
                }
            }

        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for(Kline kline: list) {
            if(kline instanceof MonthKline) {
                stringBuffer.append("T:"+type+" "+((MonthKline)kline).key);
            }else {
                stringBuffer.append(" "+kline.getDate());
            }

        }
        return stringBuffer.toString();
    }

}
