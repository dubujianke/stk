package com.alading.tool.stock;

public class PrsLog {
	static int total;

	static int idx;

	static long time;
	public static void log(String str) {
		idx++;
		long t1 = System.currentTimeMillis();
		if(t1-time>1000) {
			time = t1;
			System.out.println("=========================================>"+str+" "+idx);
			idx = 0;
		}
	}

}
