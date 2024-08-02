package com.alading.tool.stock;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;

import java.io.*;
import java.util.*;

import static com.alading.tool.stock.AbsStragety.FILE_MINUTE;
import static com.alading.tool.stock.AbsStragety.FILE_MINUTE_DATE;

public class StockAllMinuteLine {

    public List<StockDayMinuteLine> allLineList = new ArrayList();
    private Map<String, StockDayMinuteLine> map = new HashMap<>();
    public Map<String, StockDayMinuteLine> proxyMap = new HashMap<>();
    private List<MinuteLine> alllines = new ArrayList();
    public static boolean useSingle;

    public void add(StockDayMinuteLine minuteLine) {
        allLineList.add(minuteLine);
        map.put(minuteLine.getDate(), minuteLine);
    }

    public void addOrReplace(StockDayMinuteLine stockDayMinuteLine) {
        if (allLineList.size() == 0) {
            int a = 0;
            a++;
        }
        if (stockDayMinuteLine == null || allLineList == null) {
            int a = 0;
            a++;
        }
        if (allLineList.size() - 1 < 0) {

        }
        StockDayMinuteLine temp = null;
        if (allLineList.size() > 0) {
            temp = allLineList.get(allLineList.size() - 1);
        }
        if (temp != null && temp.getDate().equals(stockDayMinuteLine.getDate())) {
        } else {
            allLineList.add(stockDayMinuteLine);
        }
        map.put(stockDayMinuteLine.getDate(), stockDayMinuteLine);
    }

    public StockDayMinuteLine getStockDayMinuteLine(String date) {
        if (date.endsWith(".txt")) {
            date = date.replace(".txt", "");
        }
        return map.get(date);
    }

    public StockDayMinuteLine getStockDayMinuteLineProxy(String date) {
        StockDayMinuteLine stockDayMinuteLine = proxyMap.get(date);
        if (stockDayMinuteLine == null) {
            proxyMap.put(date, map.get(date));
        }
        stockDayMinuteLine = proxyMap.get(date);
        return stockDayMinuteLine;
    }

    public List<MinuteLine> hibernate(String file) {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream("D:\\new_tdx\\T0001\\export_before_hibernate\\a.dat"))) {
            List<MinuteLine> sr = (List<MinuteLine>) stream.readObject();
            sr.size();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<String> list = FileManager.readListGB(file);
            List<MinuteLine> alllines = new ArrayList();
            int i = 0;
            int idx = 0;
            for (String line : list) {
                i++;
                if (i < 3) {
                    continue;
                }
                if (line.startsWith("数据来源:通达信")) {
                    break;
                }

                MinuteLine minuteLine = null;
                minuteLine = new MinuteLine(line.trim());
                minuteLine.setGlobalIdx(idx);
                idx++;
                alllines.add(minuteLine);
            }

            try {
                ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("D:\\new_tdx\\T0001\\export_before_hibernate\\a.dat"));
                stream.writeObject(alllines);
                stream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return alllines;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    public static MinuteLine getMinute(String file, String date) {
        boolean singleFlag = useSingle;
        try {
            List<String> list = FileManager.readListGB(file);
            int i = 0;
            for (String line : list) {
                i++;
                if (line.startsWith("数据来源:通达信")) {
                    break;
                }
                if (i < 3) {
                    continue;
                }
                if (line.contains("/")) {
                    String adate = date.replaceAll("-", "/");
                    if (!line.startsWith(adate)) {
                        continue;
                    }
                } else {
                    String adate = date.replaceAll("/", "-");
                    if (!line.startsWith(adate)) {
                        continue;
                    }
                }

                MinuteLine minuteLine = null;
                minuteLine = new MinuteLine(line.trim());
                if (singleFlag && !date.equalsIgnoreCase(minuteLine.getDate())) {
                    continue;
                }
                minuteLine.setGlobalIdx(0);
                return minuteLine;
            }
        } catch (Exception e) {
        }
        return null;
    }


    public StockAllMinuteLine prs(List<Kline> days, String file, int aidx, int offset, int type, int len, String date, boolean aUseAll, int dayLen, int leftLen) {
//        date = "2023-03-04";
        boolean singleFlag = useSingle;
        if (aUseAll) {
            singleFlag = !aUseAll;
        }
        try {
            StockDayMinuteLine stockDayMinuteLine = null;
            List<String> list = FileManager.readListGB(file);

            int i = 0;
            int idx = aidx;
            int j = 0;
            int dateOffset = 0;
            int dateOffsetBegin = 0;
            int dateOffsetEnd = 0;
            for (String line : list) {
                j++;
                if (line.startsWith(date)) {
                    dateOffset = j;
                    break;
                }
            }

            if (dateOffset == 0 && leftLen > 0) {
                dateOffset = list.size();
                dateOffsetBegin = dateOffset - leftLen * 240;
                dateOffsetEnd = dateOffset - 1;
            } else {
                if (dayLen > 1) {
                    dateOffsetBegin = dateOffset - 1 - dayLen * 240;
                    dateOffsetEnd = dateOffset - 1 + 240 - 1;
                }
            }
            String lastLine = list.get(list.size() - 1);
            MinuteLine lastMinuteLine = null;
            if (type == 0) {
                lastMinuteLine = new MinuteLine(lastLine.trim());
            } else {
                lastMinuteLine = new MinuteLine(lastLine.trim(), 0, 0);
            }
            boolean aflag = DateUtil.isAfter(DateUtil.stringToDate(date), DateUtil.stringToDate(lastMinuteLine.getDate()));
            if (dateOffset == 0 && aflag && dayLen > 0) {
                return null;
            }
            for (String line : list) {
                i++;
                if (len > 0) {
                    if (i <= list.size() - len) {
                        continue;
                    }
                }
                if (i < offset) {
                    continue;
                }
                if (line.startsWith("数据来源:通达信")) {
                    break;
                }
                if (AbsStragety.minuteSingleDate > 0) {
                    if (!line.startsWith(date)) {
                        continue;
                    }
                } else {
                    if (dayLen > 1) {
                        if (dateOffsetBegin > 1) {
                            if ((i - 1) < dateOffsetBegin || (i - 1) > dateOffsetEnd) {
                                continue;
                            }
                        }
                    }
                }


                MinuteLine minuteLine = null;
                if (type == 0) {
                    minuteLine = new MinuteLine(line.trim());
                } else {
                    minuteLine = new MinuteLine(line.trim(), 0, 0);
                }

                if (singleFlag && !date.equalsIgnoreCase(minuteLine.getDate())) {
                    continue;
                }
                minuteLine.setGlobalIdx(idx);
                idx++;
                minuteLine.setAllLineList(alllines);
                alllines.add(minuteLine);
                int idxDate = AbsStragety.getIdx(days, minuteLine.getDate());
                if (idxDate == -1) {
                    int a = 0;
                    a++;
                }
                Kline kline = days.get(idxDate);
                if (stockDayMinuteLine == null) {
                    stockDayMinuteLine = new StockDayMinuteLine();
                    stockDayMinuteLine.setDate(minuteLine.getDate());
                    stockDayMinuteLine.setKline(kline);
                    add(stockDayMinuteLine);
                } else {
                    int flag = stockDayMinuteLine.isSameDate(minuteLine);
                    if (flag == 0) {
                        stockDayMinuteLine = new StockDayMinuteLine();
                        stockDayMinuteLine.setDate(minuteLine.getDate());
                        stockDayMinuteLine.setKline(kline);
                        add(stockDayMinuteLine);
                    }
                }
                stockDayMinuteLine.add(minuteLine);
            }
            return this;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }


    public static int getBetweenDayLen(File fsDir[], String date, int len) {
        int cnt = 0;
        for (int i = 0; i < fsDir.length; i++) {
            File file = fsDir[i];
            String date1 = file.getName().replace(".txt", "");
            Date datea = DateUtil.stringToDate4(date1);
            Date dateb = DateUtil.stringToDate(date);
            int temp = (int) (dateb.getTime() - datea.getTime());
            if (temp >= 0) {
                cnt++;
            }
        }
        return cnt;
    }

    public static int getBetweenDayLen(String date1, String date2) {
        Date datea = DateUtil.stringToDate(date1);
        Date dateb = DateUtil.stringToDate(date2);
        return DateUtil.getDayLen(dateb, datea);
    }

    public static String getFirstDate() {
        File file = new File(FILE_MINUTE_DATE);
        File fsDir[] = file.listFiles();
        if (fsDir.length > 1) {
            Arrays.sort(fsDir, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    String date1 = o1.getName().replace(".txt", "");
                    String date2 = o2.getName().replace(".txt", "");
                    Date datea = DateUtil.stringToDate4(date1);
                    Date dateb = DateUtil.stringToDate4(date2);
                    return (int) (datea.getTime() - dateb.getTime());
                }
            });
        }
        if (fsDir.length == 0) {
            return "";
        }
        File afile = fsDir[0];
        String date1 = afile.getName().replace(".txt", "");
        return date1;
    }

    public static StockAllMinuteLine get(List<Kline> days, String code, String date, boolean useAll, int len) {
        File file = new File(FILE_MINUTE_DATE);
        File fsDir[] = file.listFiles();
        if (fsDir.length > 1) {
            Arrays.sort(fsDir, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    String date1 = o1.getName().replace(".txt", "");
                    String date2 = o2.getName().replace(".txt", "");
                    Date datea = DateUtil.stringToDate4(date1);
                    Date dateb = DateUtil.stringToDate4(date2);
                    return (int) (datea.getTime() - dateb.getTime());
                }
            });
        }
        int dayLen = getBetweenDayLen(fsDir, date, len);
        int leftLen = len - dayLen;
        if (len == -1) {
            leftLen = -1;
        }
        StockAllMinuteLine stockAllMinuteLine = new StockAllMinuteLine();
        if (AbsStragety.minuteDayLen == -1) {
            stockAllMinuteLine.prs(days, FILE_MINUTE + code + ".txt", 0, 3, 0, AbsStragety.minuteDayLen, date, useAll, len, leftLen);
            String lastDate = "";
            if (stockAllMinuteLine.alllines.size() > 0) {
                lastDate = stockAllMinuteLine.alllines.get(stockAllMinuteLine.alllines.size() - 1).getDate();
            }
            for (File itemDir : fsDir) {
                String path = itemDir.getAbsolutePath() + "/" + code + ".txt";
                String date1 = itemDir.getName().replace(".txt", "");
                int dltLen = getBetweenDayLen(date1, date);
                int dltLenFromLast = 99;//
                if (!lastDate.equalsIgnoreCase("")) {
                    dltLenFromLast = getBetweenDayLen(date1, lastDate);
                }
                if (dltLenFromLast > 0 && dltLen <= 0) {
                    int globalIdx = stockAllMinuteLine.alllines.size();
                    stockAllMinuteLine.prs(days, path, globalIdx, 0, 1, AbsStragety.minuteDayLen, date, useAll, -1, -1);
                }
            }
        } else {
            if (fsDir.length > 0) {
                String path = fsDir[fsDir.length - 1].getAbsolutePath() + "/" + code + ".txt";
                int globalIdx = stockAllMinuteLine.alllines.size();
                stockAllMinuteLine.prs(days, path, globalIdx, 0, 1, AbsStragety.minuteDayLen, date, useAll, -1, -1);
            } else {
                stockAllMinuteLine.prs(days, FILE_MINUTE + code + ".txt", 0, 3, 0, AbsStragety.minuteDayLen, date, useAll, -1, -1);
            }
        }
        return stockAllMinuteLine;
    }

    public static ZTReason getFirstSpeedUpReason(Kline kline, StockAllMinuteLine stockAllMinuteLine, String code, String date, LineContext context) {
        StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(date);
        if (stockDayMinuteLine == null) {
            int a = 0;
            return null;
        }
        ZTReason ztReason = stockDayMinuteLine.getFirstSpeedUp(kline, context);
        return ztReason;
    }

    public static MinuteLine getFirstSpeedUp(Kline kline, StockAllMinuteLine stockAllMinuteLine, String code, String date, LineContext context) {
        if(code.contains("001299")) {
            int a = 0;
        }
        StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(date);
        if (stockDayMinuteLine == null) {
            int a = 0;
        }
        ZTReason ztReason = stockDayMinuteLine.getFirstSpeedUp(kline, context);
        if(ztReason==null) {
            return null;
        }
        MinuteLine minuteLine = ztReason.minuteLine;
        if(minuteLine.getDltIdxTHS()>1120 && minuteLine.getDltIdxTHS()<1130) {
            return null;
        }
        if(minuteLine.getDltIdxTHS()>=1300 && minuteLine.getDltIdxTHS()<1310) {
            return null;
        }
        return minuteLine;
    }

    public static MinuteLine realTimeFilterFirstJump(Kline kline, StockAllMinuteLine stockAllMinuteLine, String code, String date, LineContext context) {
        StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(date);
        if (stockDayMinuteLine == null) {
            int a = 0;
        }
        ZTReason ztReason = stockDayMinuteLine.realTimeFilterFirstJump(kline, context);
        if(ztReason==null) {
            return null;
        }
        MinuteLine minuteLine = ztReason.minuteLine;
        return minuteLine;
    }




    public static MinuteLine getFirstSpeedDownWithVOLInDays(Kline kline, StockAllMinuteLine stockAllMinuteLine, String code, String date, LineContext context) {
        try {
            StockDayMinuteLine stockDayMinuteLine = stockAllMinuteLine.getStockDayMinuteLine(date);
            if (stockDayMinuteLine == null) {
                int a = 0;
            }
            MinuteLine minuteLine = stockDayMinuteLine.getFirstSpeedDown(kline);
            return minuteLine;
        } catch (Exception e) {

        }
        return null;
    }

    public void clear() {
        proxyMap.clear();
        map.clear();
        allLineList.clear();
        alllines.clear();
    }



}

