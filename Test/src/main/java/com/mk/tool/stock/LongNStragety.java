package com.mk.tool.stock;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;

public class LongNStragety {

    public static int getIdx(List<Kline> days, String date) {
        int i = 0;
        for (Kline line : days) {
            if (line.getDate().equals(date)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public static int searchZhangting(List<Kline> days, int offset) {
        for (int i = 0; i < 30; i++) {
            Kline kline = days.get(offset - i);
            if (kline.isZhanging()) {
                return offset - i;
            }
        }
        return -1;
    }


    public static boolean isN(List<Kline> days, int idx, int toOffset) {
        if (idx == -1) {
            return false;
        }
        Kline kline = days.get(idx);
        if (kline.getZhangfu() < 7) {
            return false;
        }
        boolean flag = true;
        if (!kline.isWrapAfter(toOffset)) {
            flag = false;
        }
        if (!kline.isVolumStepDownAfter(toOffset)) {
            flag = false;
        }

        if (flag) {
            double  max = kline.getMaxBefore(20);
            if (max > kline.getOpen()) {
                if (Math.abs(max - kline.getOpen()) / kline.getOpen() * 100 > 3) {
                    flag = false;
                }
            }
        }
        return flag;
    }


    public static double  getMax(List<Kline> days, int offset, int dayNum) {
        double  max = 0;
        for (int i = offset; i >= offset - dayNum; i--) {
            Kline kline = days.get(i);
            if (kline.getMax() > max) {
                max = kline.getMax();
            }
        }
        return max;
    }

    public static double  compareFraction(double  src, double  dst) {
        double  v = 0;
        v = 100 * (src - dst) / dst;
        return v;
    }


    public static boolean isIn(double  src, double  v1, double  v2) {
        return src > v1 && src < v2;
    }

    public static void prsKN0(String file, List<Kline> days, String date) {
        int idx = getIdx(days, date);
        if (idx < 200) {
            return;
        }
        Kline day0 = days.get(idx);
        Kline day1 = days.get(idx - 1);
        Kline day2 = days.get(idx - 2);
        double  zf0 = day0.getZhangfu();
        double  zf1 = day1.getZhangfu();
        double  zf2 = day2.getZhangfu();

        double  recentMax = getMax(days, day2.getIdx() - 1, 30);
        double  tmp = compareFraction(day0.getOpen(), recentMax);
        boolean tmpFlag = isIn(recentMax, day0.getMin(), day0.getOpen());
        boolean volFlag = false;
        if (day0.getVolume() < day1.getVolume() && day1.getVolFraction() > 3 && tmpFlag) {
            volFlag = true;
        }
        boolean isDown = day1.getZhangfu() < -1;
        if (zf2 > 7 && isDown && day0.isShadownDown() && volFlag) {
            Log.log("==========>" + file + "	" + date + "  " + days.get(idx + 1).getZhangfu());
        }
    }

    public static void prsIsN(String file, List<Kline> days, String date) {
        int idx = getIdx(days, date);
        if (idx < 200) {
            return;
        }
        List<Kline> list = KLineUtil.searchDazhang30Kline(days, idx);
        if(list.size()<=10) {
        	return;
		}
        NStragetiy nStragetiy = new NStragetiy();
        nStragetiy.setDays(days);
        nStragetiy.setAllList(list);
        boolean flag = nStragetiy.process();
        if (flag) {
            Log.log("prsIsN==========>" + file + "	" + date + "  " + days.get(idx).getZhangfu());
        }
    }



    static String FILE = "D:\\new_tdx\\T0002\\export\\";

    public static void prs(String file) {
        if (file.startsWith("30")) {
            return;
        }
        List<Kline> days = new ArrayList();
        List<String> list = FileManager.readListGB(FILE + file);
        int i = 0;
        for (String line : list) {
            i++;
            if (i < 3) {
                continue;
            }
            if (line.startsWith("数据来源:通达信")) {
                break;
            }
            int idx = i - 3;
            Kline dayLine = new Kline(line.trim());
            dayLine.allLineList = days;
            dayLine.setIdx(idx);
            days.add(dayLine);
        }
        LEN = 100;
        if (isTest) {
            if (days.size() < LEN) {
                LEN = days.size() - 50;
            }
            for (int j = 0; j < LEN; j++) {
                Date adate = DateUtil.stringToDate3(DATE);
                adate = DateUtil.getBeforeDate(adate, j);
                String dateStr = DateUtil.dateToString3(adate);
                if(dateStr.equalsIgnoreCase("2023/06/01")) {
                	int a = 0;
                	a++;
				}
                prsIsN(file, days, dateStr);
            }
        } else {
			if(isSingle) {
				prsIsN(file, days, "2023/03/09");
//		prsIsN(file, days, "2022/12/05");

			}else {
				if (days.size() < LEN) {
					LEN = days.size() - 50;
				}
				for (int j = 0; j < LEN; j++) {
					Date adate = DateUtil.stringToDate3(DATE);
					adate = DateUtil.getBeforeDate(adate, j);
					String dateStr = DateUtil.dateToString3(adate);

					prsIsN(file, days, dateStr);
				}
			}


        }

    }


	static int LEN = 300;
    static final String DATE = "2023/06/01";
	static boolean isSingle = false;
	static boolean isTest = true;
    public static void main(String[] args) {
		if(isTest) {
			File file = new File(FILE);
			File fs[] = file.listFiles();
			for (File item :fs) {
			    if(item.toString().contains("000756")) {
			        int a = 0;
                    Log.log(item.toString());
                }
				prs(item.getName());
			}
		}else {
//		prs("000756.txt");//2022/12/05
//			prs("002229.txt");//2023/01/30
			prs("000756.txt");//2023/01/30



		}

    }

}
