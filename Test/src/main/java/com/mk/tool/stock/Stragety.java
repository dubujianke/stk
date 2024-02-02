package com.mk.tool.stock;

import com.mk.data.GetAllBankuaiCode;
import com.mk.report.LineReports;

import java.io.IOException;
import java.util.*;

/**
 * KN
 */
public abstract class Stragety extends AbsStragety {
    public abstract void prs(String file, List<Kline> days, String date, String stagetyName, List<Weekline> weeks, List<MonthKline> moths, int usem, int usew, LineContext context);

    public static List<Result> retLines = new ArrayList<>();
    public static boolean isResult = false;
    public static int kn = 1;
    public static int forceLog = 1;

    public static void addLine(Result line) {
        if (contain(line)) {
            return;
        }
        retLines.add(line);
        before_num++;
    }

    public static boolean contain(Result ret) {
        for (Result result : retLines) {
            if (result.getInfo().equals(ret.getInfo())) {
                return true;
            }
        }
        return false;
    }

    public static void mainResult() {
        isResult = true;
//        Log.log("================================================================================================================" + before_num);
        for (Result result : retLines) {
            LineContext context = new LineContext();
            context.setUseMinute(1);
            mainProcess(result.code, result.date, result.stragety, 1, 1, context);
        }
//        Log.log(">>" + after_num);
    }


    public static boolean findZT = true;
    public static void main(String[] args) {
        try {
            GetAllBankuaiCode.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FILE = "D:\\new_tdx\\T0001\\export\\";
        DATE = "2023/08/04";
        CTROL_LEN = 20;
        LineContext context = new LineContext();
        kn = 1;
        if (kn == 1) {
            context.setUseMinute(1);
//            mainProcess("605289.txt", "2023/04/20", "KN", 0, 0, context);//Y special long N
//            mainProcess("600665.txt", "2023/06/27", "KN", 1, 1, context);//Y week120
//            mainProcess("601138.txt", "2023/03/03", "KN", 1, 1, context);//Y  week120

//            mainProcess("605365.txt", "2023/09/06", "KN", 1, 1, context);
//            mainProcess("600202.txt", "2023/09/04", "KN", 1, 1, context);
//            mainProcess("003010.txt", "2023/05/17", "KN", 1, 1, context);//Y  function2
//            mainProcess("600685.txt", "2023/05/08", "KN", 1, 1, context);//Y week30
//            mainProcess("600482.txt", "2023/05/08", "KN", 1, 1, context);//Y week10
//            mainProcess("600310.txt", "2023/05/17", "KN", 1, 1, context);//Y week60
//            mainProcess("600520.txt", "2023/06/26", "KN", 1, 1, context);//Y week10
//            mainProcess("605133.txt", "2023/05/26", "KN", 1, 1, context);//week30 +9
//            mainProcess("603097.txt", "2023/05/25", "KN", 1, 1, context);//Y week30
//            mainProcess("002211.txt", "2023/06/26", "KN", 1, 1, context);//Y week120  FORCE
//            mainProcess("600293.txt", "2023/06/13", "KN", 0, 0, context);
//            mainProcess("002719.txt", "2023/07/03", "KN", 1, 1, context);//Y week120
//            mainProcess("601086.txt", "2023/07/20", "KN", 1, 1, context);//Y week120
//            mainProcess("603038.txt", "2023/04/27", "KN", 1, 1, context);//N
//            mainProcess("600959.txt", "2023/04/24", "KN", 1, 1, context);//Y cross
//            mainProcess("603999.txt", "2023/04/24", "KN", 1, 1, context);

//            mainProcess("603326.txt", "2023/08/25", "KN", 1, 1, context);

//            mainProcess("000536.txt", "2023/08/31", "KN", 1, 1, context);

            mainProcess("600725.txt", "2023/09/04", "KN", 1, 1, context);
        } else if (kn == 3) {
//            KN.testCode = "600202";
//            KN.testDate = "2023/09/04";
            mainProcess("", "2023/08/24~2023/09/06", "KN", 0, 0, context);
        } else if (kn == 4) {
            CTROL_LEN = 1;
            mainProcess("", "2023/09/01~2023/09/07", "KN", 0, 0, context);
        } else if (kn == 2) {
            context.setUseMinute(1);
//            mainProcess("001222.txt", "2023/08/15", "KN", 1, 1, context);
//            mainProcess("000856.txt", "2023/04/24", "KN", 1, 1, context);//Y  function2
//            mainProcess("002689.txt", "2023/06/29", "KN", 1, 1, context);//E

//            mainProcess("603838.txt", "2023/04/07", "KN", 1, 1, context);//N
//            mainProcess("600777.txt", "2023/04/17", "KN", 1, 1, context);//N

//            mainProcess("603191.txt", "2023/04/26", "KN", 1, 1, context);//N
//            mainProcess("002625.txt", "2023/04/27", "KN", 1, 1, context);//N
//            mainProcess("600827.txt", "2023/05/26", "KN", 1, 1, context);//N

//            mainProcess("603519.txt", "2023/06/05", "KN", 1, 1, context);//N
//            mainProcess("002520.txt", "2023/06/16", "KN", 1, 1, context);//N
//            mainProcess("002331.txt", "2023/05/09", "KN", 1, 1, context);//N
//            mainProcess("000504.txt", "2023/05/26", "KN", 1, 1, context);//N
//            mainProcess("000068.txt", "2023/05/23", "KN", 1, 1, context);//N
//            mainProcess("603439.txt", "2023/05/19", "KN", 1, 1, context);//N  POS 10 guaili no change
//            mainProcess("603630.txt", "2023/06/14", "KN", 1, 1, context);//N
//            mainProcess("002641.txt", "2023/06/02", "KN", 1, 1, context);//N

//            mainProcess("603266.txt", "2023/07/04", "KN", 1, 1, context);//N  POS 10 guaili no change
//            mainProcess("002833.txt", "2023/06/16", "KN", 1, 1, context);//N 不是多头排列&乖离太大（MA120 10）
//            mainProcess("002254.txt", "2023/06/27", "KN", 1, 1, context);//N
//            mainProcess("002979.txt", "2023/06/29", "KN", 1, 1, context);//N
//            mainProcess("002975.txt", "2023/06/29", "KN", 1, 1, context);//Y week120
//            mainProcess("603871.txt", "2023/07/10", "KN", 1, 1, context);//N getEntityZhangfu<5
//            mainProcess("600121.txt", "2023/06/28", "KN", 1, 1, context);//N
//            mainProcess("603029.txt", "2023/07/07", "KN", 1, 1, context);//N  MA30~MA120 space is smaller
//            mainProcess("600293.txt", "2023/07/10", "KN", 0, 0, context);
//            mainProcess("000032.txt", "2023/07/04", "KN", 1, 1, context);//Y week120
//            mainProcess("000151.txt", "2023/06/29", "KN", 1, 1, context);//Y week120
//            mainProcess("002818.txt", "2023/07/06", "KN", 1, 1, context);//N  POS=10  不是三线顺上 所以要达到MA30
//            mainProcess("002623.txt", "2023/07/03", "KN", 1, 1, context);//N month open<MA60 30 120
//            mainProcess("601231.txt", "2023/07/03", "KN", 1, 1, context);//Y  week10=low
//            mainProcess("600714.txt", "2023/06/30", "KN", 1, 1, context);//Y  week10=low
//            mainProcess("600778.txt", "2023/07/11", "KN", 1, 1, context);//E
//            mainProcess("600551.txt", "2023/07/05", "KN", 1, 1, context);//Y Error
//            mainProcess("605108.txt", "2023/07/03", "KN", 1, 1, context);//E
//            mainProcess("600149.txt", "2023/06/27", "KN", 1, 1, context);//E
//            mainProcess("600593.txt", "2023/06/30", "KN", 1, 1, context);//E
//            mainProcess("002251.txt", "2023/07/10", "KN", 1, 1, context);//E <<MA250
//            mainProcess("002256.txt", "2023/07/03", "KN", 1, 1, context);//E
//            mainProcess("600237.txt", "2023/06/30", "KN", 1, 1, context);//E
//            mainProcess("002101.txt", "2023/07/03", "KN", 1, 1, context);//E
//            mainProcess("600714.txt", "2023/06/30", "KN", 1, 1, context);//E
//            mainProcess("000957.txt", "2023/07/03", "KN", 1, 1, context);//Y week60 day:0+
//            mainProcess("002487.txt", "2023/07/03", "KN", 1, 1, context);//E
//            mainProcess("600960.txt", "2023/07/04", "KN", 1, 1, context);//Y week10
//            mainProcess("003008.txt", "2023/06/29", "KN", 1, 1, context);
//            mainProcess("000561.txt", "2023/06/28", "KN", 1, 1, context);//没涨就大跌
//            mainProcess("000860.txt", "2023/06/27", "KN", 1, 1, context);//没涨就大跌
//            mainProcess("603355.txt", "2023/07/04", "KN", 1, 1, context);//没涨就大跌
//            mainProcess("600658.txt", "2023/07/05", "KN", 1, 1, context);//Y week10
//            mainProcess("002653.txt", "2023/07/04", "KN", 1, 1, context);//没涨就大跌
//            mainProcess("002792.txt", "2023/06/27", "KN", 1, 1, context);
//            mainProcess("002232.txt", "2023/07/04", "KN", 1, 1, context);//next weekline is zhang
//            mainProcess("600313.txt", "2023/07/07", "KN", 1, 1, context);
//            mainProcess("603860.txt", "2023/08/23", "KN", 1, 1, context);
//            mainProcess("002370.txt", "2023/08/15", "KN", 1, 1, context);
//            mainProcess("002394.txt", "2023/08/21", "KN", 1, 1, context);
//            mainProcess("603122.txt", "2023/08/24", "KN", 1, 1, context);
            mainProcess("600202.txt", "2023/09/04", "KN", 1, 1, context);
        }

        mainResult();
        LineReports.report();

    }

}
