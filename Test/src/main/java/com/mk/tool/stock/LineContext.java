package com.mk.tool.stock;

import com.mk.model.Table;
import com.mk.tool.stock.model.KModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineContext {
    public boolean isUseAll() {
        return useAll;
    }

    public void setUseAll(boolean useAll) {
        this.useAll = useAll;
    }

    private boolean useAll;

    public KModel getkModel() {
        return kModel;
    }

    public void setkModel(KModel kModel) {
        this.kModel = kModel;
    }

    KModel kModel;
    public Table table;

    private int sIdx;
    private boolean is3MonthSmal;
    public Kline needForce;
    private double totalV;
    private MinuteLine lastMinuteLine;
    private String info;


    private String msg;
    private boolean isDayStandMA250;
    private boolean isDayStandMA120;
    private boolean isWeekStandMA250;
    private java.util.List<Weekline> weeks;
    private List<MonthKline> moths;
    private StockAllMinuteLine stockAllMinuteLine;
    private int usem;
    private int usew;
    private int useMinute;

    public int getUseMinuteLen() {
        return useMinuteLen;
    }

    public void setUseMinuteLen(int useMinuteLen) {
        this.useMinuteLen = useMinuteLen;
    }

    private int useMinuteLen=-1;
    private String type;
    private int finishId;
    private boolean localHorFlag;
    private boolean localWeekHorFlag;
    private boolean localMonthHorFlag;
    private boolean localBottomFlag;
    private boolean localWBottomFlag;
    private Map<String, Object> atrtrMap = new HashMap<>();

    public void add(String key, Object obj) {
        atrtrMap.put(key, obj);
    }

    public int getInt(String key) {
        int specialHor = -1;
        if(getkModel()!=null && getkModel().getRow() != null) {
            specialHor = getkModel().getRow().getInt("specialHor");
            return specialHor;
        }
        return (int) atrtrMap.get(key);
    }

    public Object get(String key) {
        return atrtrMap.get(key);
    }

    private boolean firsetIsZT;
    private boolean realBottomFlag;

    public int getType05() {
        return type05;
    }

    public void setType05(int type05) {
        this.type05 = type05;
    }

    public boolean getLft() {
        return lft;
    }

    public void setLft(boolean lft) {
        this.lft = lft;
    }

    public boolean getRgt() {
        return rgt;
    }

    public void setRgt(boolean rgt) {
        this.rgt = rgt;
    }

    private int type05;
    private boolean lft;
    private boolean rgt;

    public List<Weekline> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Weekline> weeks) {
        this.weeks = weeks;
    }

    public List<MonthKline> getMoths() {
        return moths;
    }

    public void setMoths(List<MonthKline> moths) {
        this.moths = moths;
    }

    public StockAllMinuteLine getStockAllMinuteLine() {
        return stockAllMinuteLine;
    }

    public void setStockAllMinuteLine(StockAllMinuteLine stockAllMinuteLine) {
        this.stockAllMinuteLine = stockAllMinuteLine;
    }

    public int getUsem() {
        return usem;
    }

    public void setUsem(int usem) {
        this.usem = usem;
    }

    public int getUsew() {
        return usew;
    }

    public void setUsew(int usew) {
        this.usew = usew;
    }

    public int getUseMinute() {
        return useMinute;
    }

    public void setUseMinute(int useMinute) {
        this.useMinute = useMinute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFirsetIsZT() {
        return firsetIsZT;
    }

    public void setFirsetIsZT(boolean firsetIsZT) {
        this.firsetIsZT = firsetIsZT;
    }

    public int getFinishId() {
        return finishId;
    }

    public void setFinishId() {
        this.finishId++;
    }

    public boolean isRealBottomFlag() {
        return realBottomFlag;
    }

    public void setRealBottomFlag(boolean realBottomFlag) {
        this.realBottomFlag = realBottomFlag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isWeekStandMA250() {
        return isWeekStandMA250;
    }

    public void setWeekStandMA250(boolean weekStandMA250) {
        isWeekStandMA250 = weekStandMA250;
    }

    public MinuteLine getLastMinuteLine() {
        return lastMinuteLine;
    }

    public void setLastMinuteLine(MinuteLine lastMinuteLine) {
        this.lastMinuteLine = lastMinuteLine;
    }

    public boolean isDayStandMA250() {
        return isDayStandMA250;
    }

    public void setDayStandMA250(boolean dayStandMA250) {
        isDayStandMA250 = dayStandMA250;
    }

    public int getsIdx() {
        return sIdx;
    }

    public void setsIdx(int sIdx) {
        this.sIdx = sIdx;
    }

    public boolean isIs3MonthSmal() {
        return is3MonthSmal;
    }

    public void setIs3MonthSmal(boolean is3MonthSmal) {
        this.is3MonthSmal = is3MonthSmal;
    }

    public double getTotalV() {
        return totalV;
    }

    public void setTotalV(double totalV) {
        this.totalV = totalV;
    }

    public boolean isDayStandMA120() {
        return isDayStandMA120;
    }

    public void setDayStandMA120(boolean dayStandMA120) {
        isDayStandMA120 = dayStandMA120;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isLocalHorFlag() {
        return localHorFlag;
    }

    public void setLocalHorFlag(boolean localHorFlag) {
        this.localHorFlag = localHorFlag;
    }

    public boolean isLocalWeekHorFlag() {
        return localWeekHorFlag;
    }

    public void setLocalWeekHorFlag(boolean localWeekHorFlag) {
        this.localWeekHorFlag = localWeekHorFlag;
    }

    public boolean isLocalMonthHorFlag() {
        return localMonthHorFlag;
    }

    public void setLocalMonthHorFlag(boolean localMonthHorFlag) {
        this.localMonthHorFlag = localMonthHorFlag;
    }

    public boolean isLocalBottomFlag() {
        return localBottomFlag;
    }

    public void setLocalBottomFlag(boolean localBottomFlag) {
        this.localBottomFlag = localBottomFlag;
    }

    public boolean isLocalWBottomFlag() {
        return localWBottomFlag;
    }

    public void setLocalWBottomFlag(boolean localWBottomFlag) {
        this.localWBottomFlag = localWBottomFlag;
    }
}
