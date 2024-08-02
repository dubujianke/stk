package com.alading.tool.stock;

import org.jsoup.internal.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LineType {
    private List<LineType> lineTypeList;
    private int idx;
    private Kline kline;

    public List<MinuteLine> allLineList = new ArrayList();
    private int priceLevel = 0;//-1  -2  -3 0  1  2 3

    public MinuteLine getLastMinute() {
        return allLineList.get(allLineList.size() - 1);
    }

    public MinuteLine getFirstMinuteLine() {
        return allLineList.get(0);
    }

    public LineType next(int i) {
        return lineTypeList.get(idx + i);
    }

    public LineType prev(int i) {
        return lineTypeList.get(idx - i);
    }

    public LineType next() {
        return lineTypeList.get(idx + 1);
    }

    public LineType prev() {
        if (idx == 0) {
            return null;
        }
        return lineTypeList.get(idx - 1);
    }

    public static int getNumer(double v) {
        return (int) (v);
    }

    public LineType getFirst() {
        return lineTypeList.get(0);
    }

    public boolean add(Kline kline, MinuteLine prevMinuteLine, MinuteLine minuteLine) {
        if (kline.getDate().equalsIgnoreCase("2023/08/03")) {
            int a = 0;
            a++;
        }
        double prev = kline.prev().getClose();

        double dltPrice = 0;
        int dltNumer = 0;
        if (prevMinuteLine != null) {
            dltPrice = minuteLine.price - minuteLine.prev().price;
            dltNumer = (int) KLineUtil.compareMaxSign5(dltPrice, prev);
        }
        if (allLineList.size() == 0) {
            allLineList.add(minuteLine);
            setPriceLevel(dltNumer);
            return true;
        } else {
            if (this.getPriceLevel() == dltNumer) {
                allLineList.add(minuteLine);
                return true;
            } else {
                return false;
            }
        }
    }

    public String getTime() {
        return allLineList.get(0).getTime();
    }


    public int getPriceLevel() {
        return priceLevel;
    }

    public static int getLevel(double price, double prev) {
        double dltPrice = price - prev;
        int dlt = (int) ((int) KLineUtil.compareMaxSign5(dltPrice, prev));
        return dlt;
    }

    public Pos getCurDir() {
        MinuteLine fist = getFirstMinuteLine();
        int x = fist.getIdx();
        int y = getPriceLevel();
        Pos pos = new Pos();
        pos.idx = x;
        pos.x = 1;
        pos.y = y;
        return pos;
    }

    public Pos getLastDir() {
        MinuteLine item = prev().getLastMinute();
        int x = item.getIdx();
        int y = prev().getPriceLevel();
        Pos pos = new Pos();
        pos.idx = x;
        pos.x = 1;
        pos.y = y;
        return pos;
    }


    public Pos getFistPos() {
        MinuteLine fist = getFirstMinuteLine();
        int x = fist.getIdx();
        int y = getPriceLevel();
        Pos pos = new Pos();
        pos.x = x;
        pos.y = y;
        return pos;
    }

    public Pos getLastPos() {
        MinuteLine item = getLastMinute();
        int x = item.getIdx();
        int y = getPriceLevel();
        Pos pos = new Pos();
        pos.x = x;
        pos.y = y;
        return pos;
    }


    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public static int getTimeInt(String v) {
        if (v.charAt(0) == '0') {
            v = v.substring(1, v.length());
        }
        int from = Integer.parseInt(v);
        return from;
    }

    public boolean isBetween(String fromStr, String toStr) {
        String curTimeStr = getTime();
        int from = getTimeInt(fromStr);
        int to = getTimeInt(toStr);
        int cur = getTimeInt(curTimeStr);
        if (cur >= from && cur <= to) {
            return true;
        }
        return false;
    }


    public boolean isEndUpAVG() {
        return allLineList.get(0).getPrice() > allLineList.get(0).getAvgPrice();
    }

    public List<LineType> getLineTypeList() {
        return lineTypeList;
    }

    public void setLineTypeList(List<LineType> lineTypeList) {
        this.lineTypeList = lineTypeList;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public MinuteLine get(int miunteLineIdx) {
        for (MinuteLine minuteLine : allLineList) {
            if (minuteLine.getIdx() == miunteLineIdx) {
                return minuteLine;
            }
        }
        return null;
    }

    public MinuteLine get(String miunteTime) {
        for (MinuteLine minuteLine : allLineList) {
            if (minuteLine.getTime().equalsIgnoreCase(miunteTime)) {
                return minuteLine;
            }
        }
        return null;
    }

    public double getSelfFraction() {
        MinuteLine prev = getFirstMinuteLine().prev();
        MinuteLine cur = getLastMinute();
        double dlt = cur.price - prev.price;
        double fraction = 100.0 * dlt / kline.prev().getClose();
        return fraction;
    }

    public boolean isALLVOL(int min) {
        for (MinuteLine minuteLine : allLineList) {
            if (min > minuteLine.getVol()) {
                return false;
            }
        }
        return true;
    }


    public double caculateSelfLength() {
        int num = allLineList.size();
        double xLen = num;
        Pos dir = getCurDir();
        if (dir.y == 0) {
            xLen = 1;
        }
        double yLen = dir.y * xLen;
        double xLen2 = Grid.getPixelX(xLen);
        double yLen2 = Grid.getPixelY(yLen);
        double len = Math.sqrt(xLen2 * xLen2 + yLen2 * yLen2);
        return len;
    }

    public int caculateSelfVol() {
        int vol = 0;
        for (MinuteLine minuteLine : allLineList) {
            vol += minuteLine.getVol();
        }
        return vol;
    }


    public static double caculate(Pos pos1, Pos pos0) {
        try {
            double x1 = pos1.x;
            double y1 = pos1.y;
            double x0 = pos0.x;
            double y0 = pos0.y;

            pos0.x = Grid.getPixelX(pos0.x);
            pos0.y = Grid.getPixelY(pos0.y);
            pos1.x = Grid.getPixelX(pos1.x);
            pos1.y = Grid.getPixelY(pos1.y);

            if (pos1 == null) {
                return 0;
            }
            double dot = pos0.x * pos1.x + pos0.y * pos1.y;
            double normal = Math.sqrt(pos0.x * pos0.x + pos0.y * pos0.y) * Math.sqrt(pos1.x * pos1.x + pos1.y * pos1.y);
            double ret = (double) (dot / normal);
            double thea = (double) Math.acos(ret);
            double theta = (double) (thea / Math.PI * 180);

            // normal * sin
            double axb = pos1.x * pos0.y - pos1.y * pos0.x;
            theta = 180 - theta;
            if (axb < 0) {
                theta = -1 * theta;
            }
//            Log.log("thea:"+theta);
            return (int) theta;
        } catch (Exception e) {

        }
        return 0;
    }


    public double caculate() {
        try {
            Pos pos1 = getLastDir();
            Pos pos0 = getCurDir();
            return caculate(pos1, pos0);
        } catch (Exception e) {

        }
        return 0;
    }

    public boolean isDownUp() {
        try {
            Pos pos1 = getLastDir();
            Pos pos0 = getCurDir();
            if (pos1.y >= 0) {
                return false;
            }
            if (pos0.y <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean isRightHor() {
        try {
            Pos pos0 = getCurDir();
            return pos0.y <= 0;
        } catch (Exception e) {

        }
        return true;
    }

    public Angle caculate2() {
        try {
            MinuteLine fist = getFirstMinuteLine();
            double price = fist.getPrice();
            double prevClose = kline.prev().getClose();

            Angle angle = new Angle();
            Pos pos1 = getLastDir();
            Pos pos0 = getCurDir();
            double theata = caculate(pos1, pos0);
            double length1 = caculateLLength();
            double length2 = caculateRLength();
            int lvol = caculateLVol();
            int rvol = caculateRVol();

            angle.setTheata(theata);
            angle.setLeftLen(length1);
            angle.setRightLen(length2);
            angle.setLvol(lvol);
            angle.setRvol(rvol);
            angle.setPrice(price);
            angle.setPrevClose(prevClose);
            angle.setAvgPrice(fist.getAvgPrice());
            return angle;
        } catch (Exception e) {

        }
        return null;
    }


    public Angle caculateExtend() {
        try {
            Angle ret = caculate2();
            Angle angle_1 = prev().caculate2();
            Angle angle_2 = prev(2).caculate2();
            double angle_1Theata = angle_1.getTheata();
            double angle_2Theata = angle_2.getTheata();
            if (Math.abs(angle_1Theata) < 160 && Math.abs(angle_2Theata) < 160) {
                if (angle_1Theata < 0 && angle_2Theata < 0) {
                    if (Math.abs(angle_1Theata) > 153) {
                        ret.mergeLeft(angle_1);
                    }
                } else if (angle_1Theata > 0 && angle_2Theata > 0) {
                    if (Math.abs(angle_1Theata) > 153) {
                        ret.mergeLeft(angle_1);
                    }
                } else {
                    if (Math.abs(angle_1Theata) > 153) {
                        ret.mergeLeft(angle_1);
                        if (Math.abs(angle_2.getTheata()) > 153) {
                            ret.mergeLeft(angle_2);
                        }
                    }
                }
            } else {
                if (Math.abs(angle_1Theata) > 160) {
                    ret.mergeLeft(angle_1);
                    if (Math.abs(angle_2.getTheata()) > 160) {
                        ret.mergeLeft(angle_2);
                    }
                }
            }

            Angle angle1 = next().caculate2();
            Angle angle2 = next(2).caculate2();
            double angle1Theata = angle1.getTheata();
            double angle2Theata = angle2.getTheata();
            if (Math.abs(angle1Theata) < 160 && Math.abs(angle2Theata) < 160) {
                if (angle1Theata < 0 && angle_1Theata < 0) {
                    if (Math.abs(angle1Theata) > 153) {
                        ret.mergeRight(angle1);
                    }
                } else if (angle1Theata > 0 && angle_2Theata > 0) {
                    if (Math.abs(angle1Theata) > 153) {
                        ret.mergeRight(angle1);
                    }
                } else {
                    if (Math.abs(angle1Theata) > 153) {
                        ret.mergeRight(angle1);
                        if (Math.abs(angle2.getTheata()) > 153) {
                            ret.mergeRight(angle2);
                        }
                    }
                }
            } else {
                if (Math.abs(angle1.getTheata()) > 160) {
                    ret.mergeRight(angle1);
                    if (Math.abs(angle2.getTheata()) > 160) {
                        ret.mergeRight(angle_2);
                    }
                }
            }


            return ret;
        } catch (Exception e) {

        }
        return null;
    }

    public double caculateLength() {
        try {
            double len = caculateSelfLength();
            double len2 = prev().caculateSelfLength();
            return len + len2;
        } catch (Exception e) {

        }
        return 0;
    }

    public double caculateRLength() {
        try {
            double len = caculateSelfLength();
            return len;
        } catch (Exception e) {

        }
        return 0;
    }

    public double caculateLLength() {
        try {
            double len2 = prev().caculateSelfLength();
            return len2;
        } catch (Exception e) {

        }
        return 0;
    }

    public int caculateLVol() {
        try {
            int len = prev().caculateSelfVol();
            return len;
        } catch (Exception e) {
        }
        return 0;
    }

    public int caculateRVol() {
        try {
            int len = caculateSelfVol();
            return len;
        } catch (Exception e) {

        }
        return 0;
    }

    public Kline getKline() {
        return kline;
    }

    public void setKline(Kline kline) {
        this.kline = kline;
    }

    boolean useZhijiao = false;

    public String toString() {
        if (useZhijiao) {
            double theata = caculate();
            double len = caculateLength();
            String lenStr = String.format("%.2f", len);
            String str = allLineList.get(0).getTime() + " " + allLineList.size() + " " + getPriceLevel() + "  theata:" + theata + "(" + lenStr + ")";
//            for(MinuteLine minuteLine:allLineList) {
//                str = str + "\r\n" + minuteLine.getTime()+"  "+minuteLine.toString();
//            }
            return str;
        }
        return allLineList.get(0).getTime() + " " + allLineList.size() + " " + getPriceLevel();
    }

    public Angle isRightAngle(double fromAngle, double toAngle, double minLen) {
        try {
            double preClose = kline.prev().getClose();
            double theata = caculate();
            double len = caculateLength();
            double llen = caculateLLength();
            double rlen = caculateRLength();
            double avgSlant = getFirstMinuteLine().getAvgSlant(kline);

            String lenStr = String.format("%.2f %.2f %.2f", len, llen, rlen);
            if (kline.getDate().equalsIgnoreCase("2023/06/29") && "0940".equalsIgnoreCase(getFirstMinuteLine().prev().getTime())) {
                int a = 0;
                a++;
            }
            boolean isHor = isDownUp();
            if (!isHor) {
                return null;
            }
            if (theata >= fromAngle && theata <= toAngle) {
                Angle angle = caculateExtend();
                int totalVol = angle.getToalVol();
                double totalLen = angle.getTotalLen();
                boolean isUnder = angle.isUnderClose();
                if (theata > 115) {
                    return null;
                }
                //vol filter
                if (totalVol > 10000) {
                    return null;
                }
                if (theata <= 99) {
                    if (totalLen < 40) {
                        return null;
                    }
                } else if (theata < 115) {
                    if (totalLen < minLen) {
                        return null;
                    }
                }
                if (avgSlant < 0) {
                    return null;
                }

                return angle;
            } else {
                return null;
            }
        } catch (Exception e) {
        }
        return null;
    }


    public String getInfo(double fromAngle, double toAngle) {
        try {
            if (kline.getDate().equalsIgnoreCase("2023/06/28") && "0949".equalsIgnoreCase(getFirstMinuteLine().prev().getTime())) {
                int a = 0;
                a++;
            }
            double preClose = kline.prev().getClose();
            //special up down up down
            double frac0 = getSelfFraction();
            if (frac0 > 2) {
                MinuteLine prev = getFirstMinuteLine().prev();
                MinuteLine cur = getLastMinute();
                if (cur.getVol() > 2000 && prev.getVol() > 1000) {
                    double frac_1 = prev().getSelfFraction();
                    double frac_2 = prev(2).getSelfFraction();
                    if (frac_1 < 0 && Math.abs(frac_1) < 0.1 && frac_2 > 1) {
                        String str = "udud: " + getFirstMinuteLine().prev().getTime();
                        return str;
                    }
                }
            }
            if (frac0 > 1.0) {
                LineType prev = prev();
                LineType prev2 = prev(2);
                double frac_1 = prev().getSelfFraction();
                double frac_2 = prev(2).getSelfFraction();

            }
            if (frac0 > 0.4) {
                boolean flag = isALLVOL(4000);
                if (flag) {
                    LineType prev = prev();
                    LineType prev2 = prev(2);
                    if (prev.getPriceLevel() == 0) {
                        if (prev.isALLVOL(1000)) {
                            if (prev2.getPriceLevel() > 50) {
                                MinuteLine joint = getFirstMinuteLine().prev();
                                if (!joint.isABSMax()) {
                                    return "";
                                }
                                String str = "uhuh: " + getFirstMinuteLine().prev().getTime();
                                return str;
                            }
                        }
                    }
                }
            }

            double theata = caculate();
            double len = caculateLength();
            double llen = caculateLLength();
            double rlen = caculateRLength();
            double avgSlant = getFirstMinuteLine().getAvgSlant(kline);
            double avgSlant2 = getFirstMinuteLine().prev().getAvgSlant(kline);
            String lenStr = String.format("%.2f %.2f %.2f", len, llen, rlen);

            LineType prevLieType = prev();
            Angle prevIsRightAngleflag = prevLieType.isRightAngle(fromAngle, toAngle, 25);
            if (prevIsRightAngleflag != null) {
                Angle angle = caculateExtend();
                int totalVol = angle.getToalVol();
                int rvol = angle.getRvol();
                int lvol = angle.getLvol();
                if (lvol < 1000) {
                    return "";
                }
                double totalLen = angle.getTotalLen();
                boolean isUnder = angle.isUnderClose();
                if (theata < 160) {
                    return "";
                }
                if (totalLen < 33) {
                    return "";
                }
                if (angle.getRvol() < angle.getLvol()) {
                    return "";
                }
                if (angle.getRvol() < 3000) {
                    return "";
                }

                String str = "right shape: " + getFirstMinuteLine().prev().getTime() + " " +
                        allLineList.size() + " " +
                        getPriceLevel() + "  theata:" +
                        theata + "(" + lenStr + ")"
                        + " totalVol:" + totalVol + " totalLen:" + totalLen;
                return str;
            }

            boolean isHor = isRightHor();
            if (isHor) {
                return "";
            }

            if (theata >= fromAngle && theata <= toAngle) {
                Angle angle = caculateExtend();
                int totalVol = angle.getToalVol();
                double totalLen = angle.getTotalLen();
                boolean isUnder = angle.isUnderClose();
                if (theata > 115) {
                    return "";
                }
                //vol filter
                if (totalVol > 28000 && frac0<1.3f) {
                    return "";
                }
                if (theata <= 99) {
                    if (totalLen >= 35 && totalLen < 40) {//40
                        MinuteLine joint = getFirstMinuteLine().prev();
                        if (!joint.isMax()) {
                            return "";
                        }
                    } else if (totalLen > 40) {

                    } else {
                        return "";
                    }
                } else if (theata < 115) {
                    if (totalLen < 70) {
                        return "";
                    }
                } else {

                }


                MinuteLine joint = getFirstMinuteLine().prev();
                if (joint.isMax()) {
                    if (avgSlant < 5) {
                        return "";
                    }
                } else {
                    if (avgSlant < 11 || totalVol < 950) {
                        return "";
                    }
                }
                if (getFirstMinuteLine().prev().getTime().equalsIgnoreCase("1124")) {
                    int a = 0;
                }
                //角点
                String str = getFirstMinuteLine().prev().getTime() + " " + allLineList.size() + " " + getPriceLevel() + "  theata:" + theata + "(" + lenStr + ")"
                        + " avgSlant:" + avgSlant + " totalVol:" + totalVol + " totalLen:" + totalLen + " isUnder:" + isUnder;
                MinuteLine prevMinuteLine = getFirstMinuteLine().prev();
//            for(MinuteLine minuteLine:allLineList) {
//                str = str + "\r\n" + minuteLine.getTime()+"  "+minuteLine.toString();
//            }

                if (prevMinuteLine.getPrice() < prevMinuteLine.getAvgPrice()) {
//                    return  "";
                }
                if (!StringUtil.isBlank(str)) {
                    int a = 0;
                }
                return str;
            } else {
                return "";
            }
        } catch (Exception e) {
        }
        return "";
    }


}
