package com.mk.tool.stock;

import java.util.HashMap;
import java.util.Map;

public class GlobalContext {
    public static Map<String, SingleContext> map = new HashMap<>();
    private static boolean isFinish;
    public static boolean contain(String v) {
        return get(v)!=null;
    }

    public static SingleContext get(String v) {
        v =   AbsStragety.getCode(v);
        return map.get(v);
    }

    public static void put(String v, SingleContext singleContext) {
        v =   AbsStragety.getCode(v);
        if(!AbsStragety.isMonitor) {
//            Log.log("clear:"+v);
            clear();
        }
        map.put(v, singleContext);
    }

    public static boolean isFinish() {
        return isFinish;
    }

    public static void setFinish(boolean finish) {
        isFinish = finish;
    }

    public static void clear() {
        for(SingleContext singleContext:map.values()) {
            if(singleContext!=null) {
                singleContext.clear();
            }
        }
        map.clear();
    }

}
