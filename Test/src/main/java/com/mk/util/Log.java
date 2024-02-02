package com.mk.util;

public class Log {
    public static boolean logEnd = false;

    public static void log(String line) {
        System.out.println(line);
    }

    public static void logEnd(String line) {
        if(logEnd) {
            return;
        }
//        //System.out.println("END");
        logEnd = true;
    }
}
