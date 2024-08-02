package com.alading.model;


import java.util.*;

public class GlobalData {
    public static Map<String, Stock> map = new HashMap<String, Stock>();
    public static Set<String> noOK = new HashSet<>();
    public static Set<String> alreadyOK = new HashSet<>();

    public synchronized static List<Stock> getRet() {
        List<Stock> list = new ArrayList<>();
        for(Stock stock : ret) {
            list.add(stock);
        }
        return list;
    }

    public static List<Stock> ret = new ArrayList<>();
    public static void add(Stock stock) {
        map.put(stock.getCode(), stock);
    }

    public static void clear() {
        map.clear();
    }

    public static void addNoOK(String code) {
        noOK.add(code);
    }

    public static void addNoOK(Stock stock) {
        noOK.add(stock.getCode());
    }

    public synchronized static void setRet(List<Stock> retList) {
        ret = retList;
    }

    public static void addYesOK(Stock stock) {
        alreadyOK.add(stock.getCode());
    }

    public static int getNoOKLen() {
        return noOK.size();
    }
    public static boolean isNoOK(Stock stock) {
        return noOK.contains(stock.getCode());
    }
    public static boolean isYOK(Stock stock) {
        return alreadyOK.contains(stock.getCode());
    }

    public static int lenY() {
        return alreadyOK.size();
    }

    public static Stock[] get() {
        return map.values().toArray(new Stock[]{});
    }
}
