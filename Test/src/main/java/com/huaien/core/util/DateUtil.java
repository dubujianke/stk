package com.huaien.core.util;

import com.mk.tool.stock.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    public static Date startTime = getStartTime();
    public static Date amEndTime = getAMEndTime();
    public static Date pmStartTime = getPMStartTime();
    public static Date amStartTime15 = getAMStartTime15();
    public static Date pmBufferTime = getPMBufferTime();

    public static String GANG = "yyyy-MM-dd";
    public static String SLANT = "yyyy/MM/dd";
    public static String GANGMAO = "yyyy-MM-dd HH:mm:ss";
    public static String SLANTMAO = "yyyy/MM/dd HH:mm:ss";

    public static String dateToString(Date time) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            String ctime = formatter.format(time);
            return ctime;
        } catch (Exception e) {
        }
        return "";
    }

    public static String dateToString3(Date time) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy/MM/dd");
            String ctime = formatter.format(time);
            return ctime;
        } catch (Exception e) {
        }
        return "";
    }

    public static String dateToString(Date time, String format) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat(format);
            String ctime = formatter.format(time);
            return ctime;
        } catch (Exception e) {
        }
        return "";
    }

    public static String dateToStringYM(Date time) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM");
            String ctime = formatter.format(time);
            return ctime;
        } catch (Exception e) {
        }
        return "";

    }

    public static String dateToString_(Date time) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy_M_d");
            String ctime = formatter.format(time);
            return ctime;
        } catch (Exception e) {
        }
        return "";

    }

    public static String dateToString2(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMdd");
        String ctime = formatter.format(time);

        return ctime;
    }

    public static String dateToDateAndHour(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMdd-HH");
        String ctime = formatter.format(time);

        return ctime;
    }

    public static String transDate(String date, String format1, String format2) {
        Date dateObj = DateUtil.stringToDate(date, format1);
        return DateUtil.dateToString(dateObj, format2);
    }

    public static String dateToDateAndHourMinute(Date time) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            String ctime = formatter.format(time);
            return ctime;
        } catch (Exception e) {
        }
        return "";
    }

    public static String dateTimeToTimeString(Date time) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("HHmm");
            String ctime = formatter.format(time);
            return ctime;
        } catch (Exception e) {
        }
        return "";
    }

    public static String trans(String time, String format1, String format2) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat(format1);
            Date ctime = null;
            try {
                ctime = formatter.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat formatter2;
            formatter2 = new SimpleDateFormat(format2);
            String ctime2 = formatter2.format(ctime);
            return ctime2;
        } catch (Exception e) {
        }
        return "";
    }


    public static String dateTimeToString(Date time) {
        try {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ctime = formatter.format(time);
            return ctime;
        } catch (Exception e) {
        }
        return "";
    }

    public static String dateTimeToString2(Date time) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String ctime = formatter.format(time);

        return ctime;
    }

    public static String dateToTimeString(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);

        return ctime;
    }

    public static Date toDate(String time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ctime;
    }

    public static Date strToDate(String time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMdd");
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ctime;
    }


    public static Date strToDate4(String time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ctime;
    }

    public static Date strToDate2(String time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return ctime;
    }

    public static Date strToDate3(String time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return ctime;
    }

    public static Date getStartTime() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "09:30:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static Date getAMEndTime() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "11:30:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static Date getAMEndTime2() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "11:00:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static Date getPMEndTime() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "15:00:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static Date getCollectTime() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "16:00:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static Date getPMStartTime() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "13:00:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static Date getAMStartTime15() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "09:45:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static Date getPMBufferTime() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "13:10:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static Date getEndTime() {
        Date ctime = null;
        String dateStr = dateToString(new Date());
        String timeStr = "16:00:00";
        ctime = toDate(dateStr + " " + timeStr);
        return ctime;
    }

    public static long before() {
        Date ctime = getStartTime();
        Date current = new Date();
        long dlt = ctime.getTime() - current.getTime();
        if (dlt < 0) {
            dlt = 0;
        }
        return dlt;
    }

    public static Date getAfterDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
        return calendar.getTime();
    }

    public static Date getBeforeDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
        return calendar.getTime();
    }

    public static Date getAfterMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minutes);
        return calendar.getTime();
    }

    public static Date getBeforeMinute(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - minutes);
        return calendar.getTime();
    }

    public static Date getAfterSecond(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + minutes);
        return calendar.getTime();
    }

    public static Date getBeforeSecond(Date date, int v) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - v);
        return calendar.getTime();
    }

    public static Date strToDateTime(String time, String format) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(format);
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ctime;
    }

    public static Date strToDateTime2(String time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy/MM/dd HHmmss");
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ctime;
    }

    public static Date strToTime3(String time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("HHmm");
        Date ctime = null;
        try {
            ctime = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ctime;
    }

    public static long sleepForAM(Date current) {
        Date ctime = getAMEndTime();
        Date pmStarttime = getPMStartTime();
        long dlt = 0;

        if (current.getTime() > ctime.getTime() && current.getTime() < pmStarttime.getTime()) {
            dlt = pmStarttime.getTime() - current.getTime();
        } else {
            dlt = 0;
        }

        return dlt;
    }

    public static java.sql.Date toSQL(Date utilDate) {
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        // java.sql.Time sTime=new java.sql.Time(utilDate.getTime());
        // java.sql.Timestamp stp=new java.sql.Timestamp(utilDate.getTime());
        ////System.out.println(sqlDate);
        return sqlDate;
    }

    public static int getDaysOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return lastDay;
    }

    public static Date getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return cal.getTime();
    }

    public static int getDaysOfMonth(String date) {
        Date date1 = stringToDate3(date);
        int year = getYear(date1);
        int month = getMonth(date1);
        int ret = getDaysOfMonth(year, month);
        return ret;
    }

    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = 1;
        cal.set(Calendar.DAY_OF_MONTH, lastDay);

        return cal.getTime();
    }

    public static String getCommentTime(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        Date now = new Date();
        try {
            Date date = df.parse(str);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if (day > 0) {
                sb.append(day + "天");
            } else if (hour > 0) {
                sb.append(hour + "小时");
            } else if (min > 0) {
                sb.append(min + "分");
            } else {
                sb.append(s + "秒 前");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static int dayForWeek(int y, int m, int d) {
        Calendar cal = new GregorianCalendar();
        cal.set(y, m - 1, d);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int dayForWeek(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    public static Date StringToDate(String paramString1, String paramString2) {
        SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat(paramString2);
        try {
            Date aDate = aSimpleDateFormat.parse(paramString1);
            return aDate;
        } catch (Exception e) {
        }
        return null;
    }

    public static Date StringToDate(String paramString) {
        return StringToDate(paramString, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date stringToDate3(String paramString) {
        return StringToDate(paramString, "yyyy/MM/dd");
    }

    public static Date stringToDate(String paramString) {
        if(paramString.contains("/")) {
            return StringToDate(paramString, "yyyy/MM/dd");
        }
        return StringToDate(paramString, "yyyy-MM-dd");
    }

    public static Date stringToDate4(String paramString) {
        return StringToDate(paramString, "yyyy-MM-dd");
    }

    public static Date stringToDate(String paramString, String format) {
        return StringToDate(paramString, format);
    }

    public static long BetweenMillis(Date paramDate1, Date paramDate2) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(paramDate1);
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(paramDate2);
        return localCalendar.getTimeInMillis() - aCalendar.getTimeInMillis();
    }

    public static long dlt(Timestamp paramDate1, Timestamp paramDate2) {
        if (paramDate1 == null || paramDate2 == null) {
            return 0;
        }
        long t1 = paramDate1.getTime();
        long t2 = paramDate2.getTime();
        return t2 - t1;
    }

    public static long dlt(Timestamp paramDate1, Date paramDate2) {
        if (paramDate1 == null || paramDate2 == null) {
            return 0;
        }
        long t1 = paramDate1.getTime();
        long t2 = paramDate2.getTime();
        return t2 - t1;
    }

    public static long dlt(Date paramDate1, Timestamp paramDate2) {
        if (paramDate1 == null || paramDate2 == null) {
            return 0;
        }
        long t1 = paramDate1.getTime();
        long t2 = paramDate2.getTime();
        return t2 - t1;
    }

    public static long dlt(Date paramDate1, Date paramDate2) {
        if (paramDate1 == null || paramDate2 == null) {
            return 0;
        }
        long t1 = paramDate1.getTime();
        long t2 = paramDate2.getTime();
        return t2 - t1;
    }

    public static boolean isBetweenTime(Date current, Timestamp paramDate1, Timestamp paramDate2) {
        if (paramDate1 == null || paramDate2 == null) {
            return false;
        }
        long t0 = current.getTime();
        long t1 = paramDate1.getTime();
        long t2 = paramDate2.getTime();
        if (t0 < t1 || t0 > t2) {
            return false;
        }
        return true;
    }

    public static boolean isBetweenTime(Date current, Date paramDate1, Date paramDate2) {
        if (paramDate1 == null || paramDate2 == null) {
            return false;
        }
        long t0 = current.getTime();
        long t1 = paramDate1.getTime();
        long t2 = paramDate2.getTime();
        if (t0 < t1 || t0 > t2) {
            return false;
        }
        return true;
    }

    public static boolean isAfter(Date current, Date next) {
        long t0 = current.getTime();
        long t1 = next.getTime();
        return t0 > t1;
    }

    public static boolean isBefore(Date current, Date next) {
        long t0 = current.getTime();
        long t1 = next.getTime();
        return t0 < t1;
    }


    public static long getMyTimestamp() {
        Date localDate = StringToDate("2010-01-01 00:00:00");
        return BetweenMillis(new Date(), localDate) / 1000L;
    }


    public static Date getDayBegin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int aDate = c.get(Calendar.DATE);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, aDate);

        return cal.getTime();
    }


    public static Timestamp getTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }


    public static long getDayInterval(long time1, long time2) {
        TimeZone tz = TimeZone.getDefault();
        long delta = tz.getRawOffset();
        long base = 24 * 3600 * 1000L;
        long day1 = (time1 + delta) / base + 1L;
        long day2 = (time2 + delta) / base + 1L;
//		//System.out.println(new Date(time1));
//		//System.out.println(new Date(time2));
//		//System.out.println(day1);
//		//System.out.println(day2);
        return (day1 - day2);
    }

    public static String getLeftTime(Date date, int duration) {
        StringBuffer sb = new StringBuffer();
        Date now = new Date();
        try {
            long l = now.getTime() - date.getTime();
            l = duration - l;
            if (l < 0) {
                return "";
            }
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sb.append(day + "天 " + hour + "小时 " + min + "分");
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static List<MonthKline> getAllMonth(Date from, Date to) {
        List<MonthKline> months = new ArrayList<>();

        int year = from.getYear();
        int month = from.getMonth();
        int toYear = to.getYear();
        int toMonth = to.getMonth();
        for (int i = year; i <= toYear; i++) {
            int mon1 = 1;
            int mon2 = 12;
            if (i == year) {
                mon1 = month;
            }
            if (i == toYear) {
                mon1 = toMonth;
            }
            for (int j = mon1; j <= mon2; j++) {
                String item = String.format("%d-%d", i, j);
                MonthKline monthKline = new MonthKline();
                monthKline.year = i;
                monthKline.month = j;
                months.add(monthKline);
            }
        }
        return months;
    }

    public static int getYear(Date date) {
        return date.getYear() + 1900;
    }

    public static int getMonth(Date date) {
        return date.getMonth() + 1;
    }

    public static int getDate(Date date) {
        return date.getDate();
    }

    public static String getTransDate(String date) {
        if (date.contains("-")) {
            Date dt = DateUtil.stringToDate4(date);
            date = DateUtil.dateToString3(dt);
            return date;
        }
        return date;
    }


    public static int getWeek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK);
        return week_index - 1;
    }

    public static int getWeek2(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK);
        return week_index;
    }

    public static List<MonthKline> initANdGetAllMonthKLines(List<Kline> days) {
        int fromDate = days.get(0).getIdx();
        int toDate = days.get(days.size() - 1).getIdx();
        return initANdGetAllMonthKLines(days, fromDate, toDate);
    }

    public static List<MonthKline> initANdGetAllMonthKLines(List<Kline> allDays, int fromIdx, int toIdx) {
        Date from = DateUtil.stringToDate3(allDays.get(fromIdx).getDate());
        Date to = DateUtil.stringToDate3(allDays.get(toIdx).getDate());
        List<MonthKline> months = new ArrayList<>();

        int year = getYear(from);
        int month = getMonth(from);

        int toYear = getYear(to);
        int toMonth = getMonth(to);
        Map<String, MonthKline> map = new HashMap<>();
        int idx = 0;
        for (int i = year; i <= toYear; i++) {
            int mon1 = 1;
            int mon2 = 12;
            if (i == year) {
                mon1 = month;
            }
            if (i == toYear) {
                mon2 = toMonth;
            }
            for (int j = mon1; j <= mon2; j++) {
                String item = String.format("%d-%02d", i, j);
                MonthKline monthKline = new MonthKline();
                monthKline.year = i;
                monthKline.month = j;
                monthKline.key = item;
                map.put(item, monthKline);
                months.add(monthKline);
//                monthKline.setIdx(idx);
                monthKline.allLineList = months;
                idx++;
//                Log.log("===================>"+item);
            }
        }

        for (int i = fromIdx; i <= toIdx; i++) {
            Kline line = allDays.get(i);
            Date aDate = DateUtil.stringToDate3(line.getDate());
            String key = String.format("%d-%02d", getYear(aDate), getMonth(aDate));
            MonthKline monthKline = map.get(key);
            if (monthKline == null) {
                int a = 0;
            }
            monthKline.add(line);
        }
        for (int i = 0; i < months.size(); i++) {
            MonthKline monthKline = months.get(i);
            monthKline.init();
        }
        List<MonthKline> removeLines = new ArrayList();
        for (int i = 0; i < months.size(); i++) {
            MonthKline monthKline = months.get(i);
            if (monthKline.mdays.size() == 0) {
                removeLines.add(monthKline);
            }
        }
        for (int i = 0; i < removeLines.size(); i++) {
            MonthKline monthKline = removeLines.get(i);
            months.remove(monthKline);
        }
        for (int i = 0; i < months.size(); i++) {
            MonthKline monthKline = months.get(i);
            monthKline.setIdx(i);
        }

        return months;
    }

    public static int getDayLen(Date date, Date now) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        return (int) day;
    }

    public static boolean isSameDate(String beginDateStr, String curDateStr) {
        Date beginDate = stringToDate3(beginDateStr);
        Date curDate = stringToDate3(curDateStr);
        Date tmp = null;
        int dayLen = getDayLen(beginDate, curDate);
        if (dayLen < 0) {
            tmp = beginDate;
            beginDate = curDate;
            curDate = tmp;
            dayLen = -1 * dayLen;
        }
        int weekIdx = getWeek(beginDate);
        if (weekIdx + dayLen <= 6) {
            return true;
        }
        return false;
    }


    public static boolean isSameWeek(String beginDate2, String curDate2) {
        Date beginDate = strToDate2(beginDate2);
        Date curDate = strToDate2(curDate2);
        int weekIdx = getWeek(beginDate);
        int dayLen = getDayLen(beginDate, curDate);
        if (dayLen < 0) {
            return false;
        }
        if (weekIdx + dayLen <= 6) {
            return true;
        }
        return false;
    }

    public static boolean isSameDate(Date beginDate, Date curDate) {
        int weekIdx = getWeek(beginDate);
        int dayLen = getDayLen(beginDate, curDate);
        if (dayLen < 0) {
            return false;
        }
        if (weekIdx + dayLen <= 6) {
            return true;
        }
        return false;
    }

    public static void setWeekIdx(List<Kline> allDays, int idx) {
        Kline kline = allDays.get(0);
        Date beginDate = kline.getDateObj();
        Kline kline2 = allDays.get(idx);
        Date curDate = kline2.getDateObj();
        int dayLen = getDayLen(beginDate, curDate);
    }

    public static List<Weekline> initANdGetAllWeekKLinesFrom(List<Kline> allDays, int from, int toIdx) {
        Kline prev = allDays.get(0);
        Date prevDate = prev.getDateObj();
        int idx = 0;

        List<Weekline> weeks = new ArrayList<>();
        Weekline weekline = null;
        weekline = new Weekline();
        weeks.add(weekline);
        weekline.key = "" + idx;
        weekline.setIdx(idx);
        weekline.allLineList = weeks;
        weekline.add(prev);
        for (int j = from; j <= toIdx; j++) {
            Kline kline = allDays.get(j);
            Date curDate = kline.getDateObj();
            if (isSameDate(prevDate, curDate)) {
            } else {
                idx++;
                weekline = new Weekline();
                weeks.add(weekline);
                weekline.key = "" + idx;
                weekline.setIdx(idx);
                weekline.allLineList = weeks;
            }
            weekline = weeks.get(weeks.size() - 1);
            weekline.add(kline);
            prevDate = curDate;
//                Log.log("===================>"+item);
        }

        for (int i = 0; i < weeks.size(); i++) {
            Weekline monthKline = weeks.get(i);
            monthKline.init();
        }
        return weeks;
    }

    public static List<Weekline> initANdGetAllWeekKLines(List<Kline> allDays, String date, int toIdx) {
        Kline prev = allDays.get(0);
        Date prevDate = prev.getDateObj();
        int idx = 0;

//        int didx = AbsStragety.getIdx(allDays, date);
//        Kline kline0 = allDays.get(idx);

        List<Weekline> weeks = new ArrayList<>();
        Weekline weekline = null;
        weekline = new Weekline();
        weeks.add(weekline);
        weekline.key = "" + idx;
        weekline.setIdx(idx);
        weekline.allLineList = weeks;
        weekline.add(prev);

        for (int j = 1; j <= toIdx; j++) {
            Kline kline = allDays.get(j);
            Date curDate = kline.getDateObj();
            if (isSameDate(prevDate, curDate)) {
            } else {
                idx++;
                weekline = new Weekline();
                weeks.add(weekline);
                weekline.key = "" + idx;
                weekline.setIdx(idx);
                weekline.allLineList = weeks;
            }
            weekline = weeks.get(weeks.size() - 1);
            weekline.add(kline);
            prevDate = curDate;
//                Log.log("===================>"+item);
        }

        for (int i = 0; i < weeks.size(); i++) {
            Weekline monthKline = weeks.get(i);
            monthKline.init();
        }
        return weeks;
    }


    public static void main(String args[]) {
        boolean flag = isSameDate("2023/05/27", "2023/05/26");
        Log.log("" + flag);
    }

    public static int getDurationMonth(Date now, Date v1) {
        try {
            long l = now.getTime() - v1.getTime();
            long day = (long) (l / (24 * 60 * 60 * 1000));
            return (int) day / 30;
        } catch (Exception e) {
        }
        return 0;
    }

    public static String getNextWorkDate(String date) {
        date = date.replaceAll("-", "/");
        date = DateUtil.dateToString(DateUtil.getAfterDate(DateUtil.stringToDate3(date), 1));
        int weekIdx = DateUtil.getWeek(DateUtil.stringToDate4(date));
        if (weekIdx == 6) {
            date = DateUtil.dateToString(DateUtil.getAfterDate(DateUtil.stringToDate4(date), 2));
        }
        return date;
    }

    public static String getPrevWorkDate(String date) {
        date = DateUtil.dateToString(DateUtil.getBeforeDate(DateUtil.stringToDate(date), 1));
        int weekIdx = DateUtil.getWeek2(DateUtil.stringToDate4(date));
        if (weekIdx == 1) {
            date = DateUtil.dateToString(DateUtil.getBeforeDate(DateUtil.stringToDate4(date), 2));
        }
        return date;
    }

}
