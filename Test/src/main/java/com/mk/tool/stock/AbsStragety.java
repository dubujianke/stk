package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;
import com.mk.data.eastmoney.GetGuben;
import com.mk.trace.TimeTJ;
import com.mk.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.mk.tool.stock.StragetyZTBottom.MONITOR_DATE;

public class AbsStragety {
    public static boolean isMonitor = false;
    static Map<String, Stragety> MAP = new HashMap<>();
    public static boolean CURDAY_OPEN;

    public static boolean printMinute;
    public static boolean isNetProxy;
    public static boolean isNetLocalProxy;
    public static String resDir = "D:/stock/Test/";
    public static int minuteDayLen = -1;
    public static int minuteSingleDate = -1;
    public static String FILE = "";
    public static String FILE_MINUTE = "";
    public static String FILE_MINUTE_DATE = "";
    public static String INFO = "";
    public static boolean isTest = true;
    public static int MONITOR_LEN = 100;
    public static int IDX_PROXY = 0;
    public static String BOTTOM_PATH = "D:/stock/Test/res/bottom/";
    public static String REPORT_PATH = "D:/stock/Test/res/report/";


    static {
        Properties properties = new Properties();
        InputStream in = AbsStragety.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(in);
            String linePath = properties.getProperty("stk.linePath");
            String minuteBefore = properties.getProperty("stk.minuteBefore");
            String minuteDate = properties.getProperty("stk.minuteDate");
            FILE = linePath;
            FILE_MINUTE = minuteBefore;
            FILE_MINUTE_DATE = minuteDate;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static {
        MAP.put("IsBottom", new IsBottom());
        MAP.put("IsBottomDeep", new IsBottomDeep());
        MAP.put("IsBottomHor", new IsBottomHor());
        MAP.put("IsBottomGetNextZT", new IsBottomGetNextZT());
        MAP.put("IsZhangting", new IsZhangting());
    }

    public static void setWeek(Kline kline) {
        kline.weekline.setCurDayIdx(kline.getIdx());
    }

    public static int getIdx(List<Kline> days, String date) {
        int i = 0;
        for (Kline line : days) {
            if (line.getDate().equals(date)) {
                return i;
            }
            if (line.getDate().equals(date.replaceAll("-", "/"))) {
                return i;
            }
            i++;
        }
        return -1;
    }


    public static double  getMax(List<Kline> days, int offset, int dayNum) {
        double max = 0;
        for (int i = offset; i >= offset - dayNum; i--) {
            Kline kline = days.get(i);
            if (kline.getMax() > max) {
                max = kline.getMax();
            }
        }
        return max;
    }

    public static double compareFraction(double src, double dst) {
        double v = 0;
        v = 100 * (src - dst) / dst;
        return v;
    }


    public static boolean isIn(double src, double v1, double v2) {
        return src > v1 && src < v2;
    }


    public static int getWeekIdx(List<Weekline> days, String date) {
        int i = 0;
        for (Weekline line : days) {
            if (line.getDate().equals(date)) {
                return i;
            }
            i++;
        }
        List<Kline> allLineList = days.get(0).mdays.get(0).allLineList;
        int idx = getIdx(allLineList, date);
        Kline kline0 = allLineList.get(idx);
        return kline0.weekline.getIdx();
    }


    public static int getMonthIdx(List<MonthKline> days, String date) {
        int i = 0;
        String monStr = DateUtil.dateToStringYM(DateUtil.strToDate4(date));
        for (MonthKline line : days) {
            if (line.getDateYM().equals(monStr)) {
                return i;
            }
            i++;
        }
        return 0;
    }


    public static String getCode(String file) {
        return file.replace("SZ#", "").replace("SH#", "").replace(".txt", "").trim();
    }

//    public static List<String> readMinuteList(String date, String file) {
//        return FileManager.readListGB(FILE_MINUTE + "\\" + date + "\\" + file);
//    }

    public static List<String> readList(String file) {
        return FileManager.readListGB(FILE + file);
    }

    public static SingleContext getContext(String file, String singleDate) {
        SingleContext singleContext = GlobalContext.get(file);
        if (singleContext != null) {
            return singleContext;
        }
        List<Kline> days = new ArrayList();
        List<String> list = FileManager.readListGB(FILE + file);
        if (list.size() == 0) {
            String code = getCode(file);
            if (code.startsWith("0")) {
                code = "SZ#" + code + ".txt";
            } else if (code.startsWith("6")) {
                code = "SH#" + code + ".txt";
            }
            list = FileManager.readListGB(FILE + code);
        }
        int i = 0;
        int offset = 0;
        if (AbsStragety.isMonitor) {
            if (AbsStragety.MONITOR_LEN > 0) {
                offset = list.size() - AbsStragety.MONITOR_LEN;
                if (offset < 0) {
                    offset = 0;
                }
            }
        }
        for (String line : list) {
            i++;
            if (i == 1) {
                INFO = line.replace(StringUtil.STR1, "");
            }
            if (i < offset) {
                continue;
            }
            if (i < 3) {
                continue;
            }
            if (line.startsWith(StringUtil.STR2)) {
                break;
            }
            int idx = i - 3;
            Kline dayLine = new Kline(line.trim());
            dayLine.allLineList = days;
            dayLine.setIdx(days.size());
            days.add(dayLine);
        }
        singleContext = new SingleContext();
        singleContext.setDate(singleDate);
        singleContext.setFile(file);
        singleContext.setInfo(INFO);
        if (AbsStragety.isMonitor && StragetyZTBottom.step != 0) {
            Kline dayLine = new Kline();
            dayLine.setOpen(days.get(days.size() - 1).getClose());
            dayLine.setClose(days.get(days.size() - 1).getClose());
            dayLine.allLineList = days;
            dayLine.setIdx(days.size());
            dayLine.setDate(DateUtil.dateToString3(new Date()));
            dayLine.setDate(MONITOR_DATE);

            days.add(dayLine);
        }
        singleContext.setDays(days);

        GlobalContext.put(getCode(file), singleContext);
        return singleContext;
    }

//    public static boolean use000 = true;
//    public static boolean use600 = false;

    public static boolean use000 = false;
    public static boolean use600 = true;

    public static void prsItem(String file, String singleDate, String stragetyName, int usemonth, int useweek, LineContext context) {
        try {
            double totalV = GetGuben.retriveOrGet(getCode(file));
            context.setTotalV(totalV);
            Log.log(file+":"+singleDate);

            if (file.startsWith("8")) {
                return;
            }
            if (file.startsWith("688")) {
                return;
            }
            if (file.startsWith("300")) {
                return;
            }
            if (file.startsWith("4")) {
                return;
            }
            if (file.startsWith("30")) {
                return;
            }

            String code = getCode(file);
            if (code.startsWith("6")) {
                if (!use600) {
                    return;
                }
            }
            if (code.startsWith("0")) {
                if (!use000) {
                    return;
                }
            }
//            Log.log(code);

            SingleContext singleContext = getContext(file, singleDate);
            List<Kline> days = singleContext.getDays();
            int idx = getIdx(days, singleDate);
            if (idx == -1) {
                return;
            }
            context.setInfo(singleContext.getInfo());
            if (isTest) {
                if (CTROL_LEN > 0) {
                    LEN = CTROL_LEN;
                } else {
                    if (days.size() < LEN) {
                        LEN = days.size() - 50;
                    }
                }

                for (int j = 0; j < LEN; j++) {
                    Date adate = DateUtil.stringToDate3(singleDate);
                    adate = DateUtil.getBeforeDate(adate, j);
                    String dateStr = DateUtil.dateToString3(adate);
                    if (dateStr.equalsIgnoreCase("2023/06/01")) {
                        int a = 0;
                        a++;
                    }
                    prsIsN(file, days, dateStr, stragetyName, usemonth, useweek, context);
                }
            } else {
                if (isSingle || isSingleDate) {
                    prsIsN(file, days, singleDate, stragetyName, usemonth, useweek, context);
                } else {
                    if (days.size() < LEN) {
                        LEN = days.size() - 50;
                    }
                    if (singleDate.contains("~")) {
                        String[] dates = singleDate.split("~");
                        String date1 = dates[0].trim();
                        String date2 = dates[1].trim();
                        LEN = 1 + DateUtil.getDayLen(DateUtil.strToDate4(date1), DateUtil.strToDate4(date2));
                        for (int j = 0; j < LEN; j++) {
                            Date adate = DateUtil.stringToDate3(date2);
                            adate = DateUtil.getBeforeDate(adate, j);
                            String dateStr = DateUtil.dateToString3(adate);
                            prsIsN(file, days, dateStr, stragetyName, usemonth, useweek, context);
                        }
                    } else {
                        for (int j = 0; j < LEN; j++) {
                            Date adate = DateUtil.stringToDate3(DATE);
                            adate = DateUtil.getBeforeDate(adate, j);
                            String dateStr = DateUtil.dateToString3(adate);
                            prsIsN(file, days, dateStr, stragetyName, usemonth, useweek, context);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void prsIsN1(String file, List<Kline> days, String date) {
//        Log.log("" + file+ " "+date);
        int idx = getIdx(days, date);
        if (idx < 200) {
            return;
        }
        Kline kline0 = days.get(idx);
        double ma250 = kline0.getMA250();
        Kline maxLine = kline0.getMaxBefore2(30);

        List<Weekline> weeks = DateUtil.initANdGetAllWeekKLines(days, date, days.size() - 1);
        int weekIdx = getWeekIdx(weeks, date);
        if (weekIdx < 200) {
            return;
        }
        Weekline last = weeks.get(weekIdx);
        Weekline next = last.next();
        Weekline prev = last.prev();
        Weekline prev2 = prev.prev();

        double weekMa120 = last.getMA120();
        boolean flag = false;
//        if (last.getOpen() < weekMa120 && last.getClose() > weekMa120) {
//            flag = true;
//        }
//        if (!flag) {
//            return;
//        }
        if (next == null) {
            int a = 0;
            a++;
            return;
        }
        boolean nextIsDownLine = next.isMiddleeShadownDown();
        boolean prevIsUp = prev.isShadownUp(50) || prev2.isShadownUp(50);
        boolean nextIsZhangfu = next.getZhangfu() > 2;
        boolean nextFlag = nextIsDownLine && nextIsZhangfu;
        boolean prevFlag = prevIsUp;
        if (!prevFlag) {
            return;
        }
        if (prev.isShadownUp(50)) {
            prev = prev;
        } else {
            prev = prev2;
        }
        if ((prev.getMax() < next.getMin())) {
            return;
        }
        double tmp1 = KLineUtil.compareMax(last.getClose(), weekMa120);
        if (nextFlag && prevFlag) {
            if (last.getZhangfu() > 2 && last.getZhenfu() > 8) {
                Log.log("prs==========>Y " + file + "	" + date + "  " + KLineUtil.compareMax(kline0.getOpen(), maxLine.getMax()));
            }
        }
    }

    public static void prsIsN(String file, List<Kline> days, String date, String stragetyName, int usemonth, int useweek, LineContext context) {
        long t1 = System.currentTimeMillis();

        List<Weekline> weeks = null;
        List<MonthKline> moths = null;
        if (!AbsStragety.isMonitor) {
            if (days.size() < 100) {
                return;
            }
        }
        SingleContext singleContext = getContext(getCode(file), date);
        singleContext.setsIdx(context.getsIdx());
        int aIdx = days.size() - 1;
        if (aIdx == -1) {
            int a = 0;
        }
        Kline last = days.get(aIdx);
        if (usemonth == 1) {
            moths = singleContext.getMoths();
        }
        if (useweek == 1) {
            weeks = singleContext.getWeeks();
            for (Weekline weekline : last.weekline.allLineList) {
                weekline.setCurDayIdx(-1);
            }
            int idx = getIdx(days, date);
            if (idx == -1) {
                return;
            }
            Kline kline0 = days.get(idx);
            setWeek(kline0);
        }

        TimeTJ.begin();
        if (context.getUseMinute() > 0) {
            context.setStockAllMinuteLine(singleContext.getStockAllMinuteLine(date, context.isUseAll(), context.getUseMinuteLen()));
        }
        TimeTJ.next();
        if (StringUtil.isNull(stragetyName)) {
            for (Stragety stragety : MAP.values()) {
                stragety.prs(file, days, date, stragetyName, weeks, moths, usemonth, useweek, context);
            }
        } else {
            Stragety stragety = MAP.get(stragetyName);
            if (moths == null) {
                int a = 0;
            }
            stragety.prs(file, days, date, stragetyName, weeks, moths, usemonth, useweek, context);

        }

        long t2 = System.currentTimeMillis();
        long dlt = t2 - t1;
        //Log.log("DLT:"+file+"   "+days.size()+" "+dlt);
    }


    static String DATE = "2023/06/09";
    static boolean isSingle = false;
    static boolean isSingleDate = false;
    static boolean isBottomTest = false;
    static int LEN = 500;
    static int CTROL_LEN = 0;
    static int before_num = 0;
    static int after_num = 0;

    public static void mainProcess(String code, String date, String stragetyName, int usemonth, int useweek, LineContext contxt) {
        try {
            if (date == null) {
                date = DateUtil.dateToString3(new Date());
            }
            date = DateUtil.getTransDate(date);
            if (code.equalsIgnoreCase("")) {
            } else {
                if (!code.endsWith("txt")) {
                    code = code + ".txt";
                }
                if (code.startsWith("SZ") || code.startsWith("SH")) {

                } else {
                    if (code.startsWith("0")) {
                        code = "" + code;
                    }
                }
            }

            if (!StringUtil.isNull(date)) {
                if (code.equalsIgnoreCase("")) {
                    if (date.contains("~")) {
                        isTest = true;
                        isSingle = false;
                    } else if (!date.equalsIgnoreCase("-")) {
                        isTest = false;
                        isSingleDate = true;
                    }
                } else {
                    if (date.equalsIgnoreCase("-")) {
                        isTest = false;
                        isSingle = false;
                    } else if (date.contains("~")) {
                        isTest = false;
                        isSingle = false;
                    } else {
                        isTest = false;
                        isSingle = true;
                    }
                }
            } else {
                isTest = true;
                isSingle = false;
            }

            if (isTest) {
                File file = new File(FILE);
                File fs[] = file.listFiles();
                int fIdx = 0;
                PrsLog.total = fs.length;
                for (File item : fs) {
                    fIdx++;
                    if (date.contains("~")) {
                        String itemDate = date;
                        String[] dates = itemDate.split("~");
                        String date1 = dates[0].trim();
                        String date2 = dates[1].trim();
                        int len = 1 + DateUtil.getDayLen(DateUtil.strToDate4(date1), DateUtil.strToDate4(date2));
                        for (int j = 0; j < len; j++) {
                            Date adate = DateUtil.stringToDate3(date2);
                            adate = DateUtil.getBeforeDate(adate, j);
                            String dateStr = DateUtil.dateToString3(adate);
//                            if (item.getName().contains("603999")) {
//                                Log.log("----------------------------> " + item.getName() + " " + dateStr);
//                            }
                            prsItem(item.getName(), dateStr, stragetyName, usemonth, useweek, contxt);
                        }
                    } else {
                        prsItem(item.getName(), DATE, stragetyName, usemonth, useweek, contxt);
//                        prsItem("603178.txt", DATE, stragetyName, usemonth, useweek, contxt);
                    }
                }
            } else if (isSingleDate) {
                File file = new File(FILE);
                File fs[] = file.listFiles();
                for (File item : fs) {
                    if (item.toString().contains("000756")) {
                        int a = 0;
                        Log.log(item.toString());
                    }
                    prsItem(item.getName(), DATE, stragetyName, usemonth, useweek, contxt);
                }
            } else {
                if (code.toString().contains("603999")) {
                    int a = 0;
                }
                prsItem(code, date, stragetyName, usemonth, useweek, contxt);//2023/01/30
            }
        } catch (Exception e) {
        }
    }

}
