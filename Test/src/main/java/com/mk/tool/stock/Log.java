package com.mk.tool.stock;

public class Log {
	public static void logString(StringBuffer sbf, String str) {
		sbf.append(str+"\r\n");
		System.out.println(str);
	}

	public static void log(String str) {
		System.out.println(str);
	}
	public static void d(String str) {
		System.out.println(str);
	}


}
