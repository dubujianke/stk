package com.mk.tool.stock;

import com.huaien.core.util.DateUtil;
import com.huaien.core.util.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KNStragetyWeek {

    public static int getIdx(List<Weekline> days, String date) {
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
            float max = kline.getMaxBefore(20);
            if (max > kline.getOpen()) {
                if (Math.abs(max - kline.getOpen()) / kline.getOpen() * 100 > 3) {
                    flag = false;
                }
            }
        }
        return flag;
    }


    public static float getMax(List<Kline> days, int offset, int dayNum) {
        float max = 0;
        for (int i = offset; i >= offset - dayNum; i--) {
            Kline kline = days.get(i);
            if (kline.getMax() > max) {
                max = kline.getMax();
            }
        }
        return max;
    }

    public static float compareFraction(float src, float dst) {
        float v = 0;
        v = 100 * (src - dst) / dst;
        return v;
    }


    public static boolean isIn(float src, float v1, float v2) {
        return src > v1 && src < v2;
    }

    public static void prsIsN(String file, List<Kline> days, String date) {
        List<Weekline> weeks = DateUtil.initANdGetAllWeekKLines(days, date, days.size()-1);
        prsIsN1(file, weeks, date);
    }

    public static void prsIsN1(String file, List<Weekline> days, String date) {
        int idx = getIdx(days, date);
        if (idx < 200) {
            return;
        }

        Kline kline0 = days.get(idx);
        Kline kline1 = kline0.next();
        if(kline1 == null) {
            return;
        }

        Kline kline2 = kline1.next();
        if(kline2 == null) {
            return;
        }
        Kline kline3 = kline2.next();
        if(kline3 == null) {
            return;
        }
        Kline kline4 = kline3.next();
        if(kline4 == null) {
            return;
        }

        float zhangfu = kline0.getZhangfu();
        float zhangfu2 = kline1.getZhangfu();
        float tf = zhangfu+zhangfu2;
        float min = kline4.getMin();
        if(kline0.getZhangfu()>10 && kline1.getZhangfu()>10 && tf>30) {
            if(kline2.getZhangfu()<-6 && kline3.getZhangfu()<-6 && kline4.getZhangfu()<-6) {
                if(KLineUtil.compareMax(min, kline1.getMax()) > 30) {
                    Log.log("prsIsN==========>" + file + "	" + date + "  " + days.get(idx).getZhangfu());
                }
            }

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
				prsIsN(file, days, "2023/04/28");
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
	static boolean isTest = false;
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
            prs("SH#603999.txt");//2023/04/28
		}

    }

}
