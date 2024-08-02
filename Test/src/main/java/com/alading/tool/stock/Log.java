package com.alading.tool.stock;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;

import java.io.IOException;
import java.util.Date;

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

	public static void logErr(String str) {
		System.out.println(str);
	}

	public static void logFile(String str) {
		try {
			str = str.substring(23);
			str = str.replace("\";", "");
			str = DateUtil.dateToTimeString(new Date())+" "+str;
			FileManager.append("d:/stock/log.txt", str+"\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
