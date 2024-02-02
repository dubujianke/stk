package com.mk.tool.stock;

import com.alibaba.fastjson.JSONArray;
import com.huaien.core.util.DateUtil;
import com.mk.data.GetMInuteLineData;
import com.mk.trace.TimeTJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleContext {
    private int sIdx;
    private String file;
    private String info;
    private List<Kline> days = new ArrayList();
    public StockAllMinuteLine stockAllMinuteLine;
    private StockDayMinuteLine stockDayMinuteLine;
    private List<Weekline> weeks = null;
    private List<MonthKline> moths = null;
    private String date;
    public JSONArray dataProxy;
    public int idxProxy;
    public static Map<String, Boolean> cacheMap = new HashMap<>();
    public static void add(String key) {
        cacheMap.put(key, true);
    }

    public static Boolean get(String key) {
        return cacheMap.get(key)==null?false:true;
    }

    public void clear() {
        for(Kline item:days) {
            item.clear();
        }
        if(weeks!=null) {
            for(Weekline item:weeks) {
                item.clear();
            }
            for(MonthKline item:moths) {
                item.clear();
            }
        }
        if(stockAllMinuteLine !=null) {
            stockAllMinuteLine.clear();
        }
        if(days !=null) {
            days.clear();
        }
        if(weeks !=null) {
            weeks.clear();
        }
        if(moths !=null) {
            moths.clear();
        }

        stockAllMinuteLine = null;
        weeks = null;
        moths = null;
        stockDayMinuteLine = null;
        idxProxy = 0;
    }

    public StockDayMinuteLine getMinutes() {
        return stockDayMinuteLine;
    }


    public List<Kline> getDays() {
        return days;
    }

    public void setDays(List<Kline> days) {
        this.days = days;
    }


    public StockAllMinuteLine getStockAllMinuteLine(String date, boolean useAll, int len) {
        stockAllMinuteLine = getStockAllMinuteLineInner(useAll, len);
        if (AbsStragety.isMonitor) {
            StockDayMinuteLine stockDayMinuteLineMonitor = null;
            if (AbsStragety.isNetProxy) {
                if (AbsStragety.isNetLocalProxy) {
                    stockDayMinuteLineMonitor = getStockDayMinuteLineMonitorProxyLocal(date);
                    stockAllMinuteLine.addOrReplace(stockDayMinuteLineMonitor);
                    stockAllMinuteLine.getStockDayMinuteLine(file);
                } else {
                    stockDayMinuteLineMonitor = getStockDayMinuteLineMonitorProxy();
                    stockAllMinuteLine.addOrReplace(stockDayMinuteLineMonitor);
                    stockAllMinuteLine.getStockDayMinuteLine(file);
                }
            } else {
                stockDayMinuteLineMonitor = getStockDayMinuteLineMonitor();
                stockAllMinuteLine.addOrReplace(stockDayMinuteLineMonitor);
                stockAllMinuteLine.getStockDayMinuteLine(file);
            }
        }

        if(stockDayMinuteLine!= null && stockDayMinuteLine.allLineList != null) {
            if(stockDayMinuteLine.allLineList.size() >=240) {
                GlobalContext.setFinish(true);
            }
        }
        return stockAllMinuteLine;
    }

    public StockDayMinuteLine getStockDayMinuteLineMonitorProxy() {
        String code = AbsStragety.getCode(file);
        code = code.replace("SH#", "");
        code = code.replace("SZ#", "");
        code = code.replace(".txt", "");
        stockDayMinuteLine = GetMInuteLineData.saveAndGetProxy(this, code);
        return stockDayMinuteLine;
    }

    public StockDayMinuteLine getStockDayMinuteLineMonitorProxyLocal(String date) {
        String code = AbsStragety.getCode(file);
        code = code.replace("SH#", "");
        code = code.replace("SZ#", "");
        code = code.replace(".txt", "");
        if(code.equalsIgnoreCase("002151") && date.equalsIgnoreCase("2023-09-08")) {
            int a = 0;
            a++;
        }
        stockDayMinuteLine = GetMInuteLineData.saveAndGetProxyLocal(this, code, date);
        return stockDayMinuteLine;
    }



    public StockDayMinuteLine getStockDayMinuteLineMonitor() {
        String code = AbsStragety.getCode(file);
        code = code.replace("SH#", "");
        code = code.replace("SZ#", "");
        code = code.replace(".txt", "");
        stockDayMinuteLine = GetMInuteLineData.saveAndGet(this, code);
        return stockDayMinuteLine;
    }

    public StockAllMinuteLine getStockAllMinuteLineInner(boolean useAll, int len) {
        if (stockAllMinuteLine != null) {
            return stockAllMinuteLine;
        }
        StockAllMinuteLine stockAllMinuteLine = StockAllMinuteLine.get(days, AbsStragety.getCode(getFile()), date, useAll, len);

        setStockAllMinuteLine(stockAllMinuteLine);
        stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(getFile());

        return stockAllMinuteLine;
    }

//    public StockAllMinuteLine reGetStockAllMinuteLineInner() {
////        if (stockAllMinuteLine != null) {
////            return stockAllMinuteLine;
////        }
//        StockAllMinuteLine stockAllMinuteLine = StockAllMinuteLine.get(days, AbsStragety.getCode(getFile()), date, true);
//        setStockAllMinuteLine(stockAllMinuteLine);
//        stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(getFile());
//
//        return stockAllMinuteLine;
//    }

    public void setStockAllMinuteLine(StockAllMinuteLine stockAllMinuteLine) {
        this.stockAllMinuteLine = stockAllMinuteLine;
    }


//    public StockDayMinuteLine getStockDayMinuteLine() {
//        if(stockDayMinuteLine != null) {
//            return stockDayMinuteLine;
//        }
//        StockAllMinuteLine stockAllMinuteLine = StockAllMinuteLine.get(days, AbsStragety.getCode(getFile()));
//        setStockAllMinuteLine(stockAllMinuteLine);
//        stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(getFile());
//        return stockDayMinuteLine;
//    }
//
//    public void setStockDayMinuteLine(StockDayMinuteLine stockDayMinuteLine) {
//        this.stockDayMinuteLine = stockDayMinuteLine;
//    }

    public List<Weekline> getWeeks() {
        if (weeks != null) {
            return weeks;
        }
        weeks = DateUtil.initANdGetAllWeekKLines(days, "", days.size() - 1);
        return weeks;
    }

    public void setWeeks(List<Weekline> weeks) {
        this.weeks = weeks;
    }

    public List<MonthKline> getMoths() {
        if (moths != null) {
            return moths;
        }
        moths = DateUtil.initANdGetAllMonthKLines(days, 0, days.size() - 1);
        return moths;
    }

    public void setMoths(List<MonthKline> moths) {
        this.moths = moths;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getsIdx() {
        return sIdx;
    }

    public void setsIdx(int sIdx) {
        this.sIdx = sIdx;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
