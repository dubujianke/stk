package com.mk.tool.stock;

import java.util.ArrayList;
import java.util.List;

public class NStragetiy {
    private List<Kline> days = new ArrayList<>();
    private List<Kline> allList = new ArrayList<>();
    private List<Kline> startList = new ArrayList<>();
    private List<Kline> centerList = new ArrayList<>();
    private List<Kline> endList = new ArrayList<>();


    public List<Kline> getStartList() {
        return startList;
    }

    public void setStartList(List<Kline> startList) {
        this.startList = startList;
    }

    public List<Kline> getCenterList() {
        return centerList;
    }

    public void setCenterList(List<Kline> centerList) {
        this.centerList = centerList;
    }

    public List<Kline> getEndList() {
        return endList;
    }

    public void setEndList(List<Kline> endList) {
        this.endList = endList;
    }

    public List<Kline> getAllList() {
        return allList;
    }

    public void setAllList(List<Kline> allList) {
        this.allList = allList;
    }


    public boolean process() {
        List<Kline> list = KLineUtil.reverse(allList);
        int len = list.size();
        Kline kline0 = list.get(0);
        Kline kline1 = list.get(1);
        Kline kline2 = list.get(2);
        if(kline0.getIdx() +1 == kline1.getIdx() && kline1.getIdx() +1 == kline2.getIdx()) {
            startList.add(kline0);
            startList.add(kline1);
            startList.add(kline2);
            list.remove(kline0);
            list.remove(kline1);
            list.remove(kline2);
        }else if(kline0.getIdx() +1 == kline1.getIdx()) {
            startList.add(kline0);
            startList.add(kline1);
            list.remove(kline0);
            list.remove(kline1);
        }else {
            startList.add(kline0);
            list.remove(kline0);
        }

        len = list.size();
        Kline klineEnd = list.get(len-1);
        endList.add(klineEnd);



        list.remove(klineEnd);

        List<Kline> removeList = KLineUtil.removeInterscction(list, endList);
        if(removeList == null) {
            int a = 0;
            return false;
        }
        for(int i=0; i<removeList.size(); i++) {
            list.remove(removeList.get(i));
        }
        List<Kline> removeList2 = new ArrayList<>();
        for(int i=0; i<list.size(); i++) {
            Kline kline = list.get(i);
            if(kline.getZhenfu()>3) {
                removeList2.add(kline);
                endList.add(kline);
            }
        }
        for(int i=0; i<removeList2.size(); i++) {
            Kline kline = removeList2.get(i);
            list.remove(kline);
        }

        KLineUtil.sortDesc(endList);
        if(endList.size()==1) {
            endList = endList.subList(0, 1);
            endList.add(0,  days.get(endList.get(0).getIdx() -1));
        }else {
            endList = endList.subList(0, 2);
        }

//        if(endList.size() > 2) {
//            return false;
//        }
        double max = 0;
        double min = 0;


        Kline line0 = endList.get(0);
        Kline line1 = days.get(line0.getIdx() -1);
        max = line0.getMax();
        min = line1.getMin();
        max = KLineUtil.getHigher(max, 10);
        min = min;
        int fromIdx = startList.get(startList.size() - 1).getIdx();
        int endIdx = line1.getIdx() -1;

        int count = 0;
        int total = endIdx-fromIdx-1;
        for(int i=fromIdx+1; i<endIdx; i++) {
            Kline kline = days.get(i);
            if(kline.getEntityMax()<=max && kline.getEntityMin()>= min) {
                count++;
            }else {
                double fraction = Kline.getIntersection(min, max, kline.getMin(), kline.getMax());
                if(fraction>95) {
                    count++;
                }else {
                    double min2 = kline.getMin();
                    double fraction2 = kline.getZhangfu();
                    if(fraction2<2) {
                        count++;
                    }else {
                        int a= 0;
                    }
                }
                int a = 0;
            }
        }
        double frac = 100*count/total;
        double FRAC = 95;
        if(total<=13) {
            FRAC = 90;
        }
        if(frac>FRAC) {
            return true;
        }


        return false;
    }

    public List<Kline> getDays() {
        return days;
    }

    public void setDays(List<Kline> days) {
        this.days = days;
    }
}
