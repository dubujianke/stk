package com.mk.trace;

import com.mk.tool.stock.Log;

public class TimeTJ {
    public static long t1;
    public static long t2;

    public static void  begin() {
        t1 = System.currentTimeMillis();
    }
    public static void  next() {
        t2 = System.currentTimeMillis();
        if(t2-t1>0) {
            Log.log("time:"+(t2-t1));
        }
        t1 = t2;
    }
}
