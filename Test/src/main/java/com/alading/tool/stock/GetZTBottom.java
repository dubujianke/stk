package com.alading.tool.stock;

import com.alading.data.GetAllBankuaiCode;
import com.alading.report.LineReports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GetZTBottom extends AbsStragety {

    public void prs(String file, List<Kline> days, String date, String stagetyName, List<Weekline> weeks, List<MonthKline> moths, boolean usem, boolean usew, LineContext context) {
    }


    public static List<Result> retLines = new ArrayList<>();

    public static void addLine(Result line) {
        retLines.add(line);
        before_num++;
    }

    public static void mainResult() {
        Stragety.isResult = true;
        Log.log("================================================================================================================" + before_num);
        for (Result result : retLines) {
            LineContext context = new LineContext();
            context.setUseMinute(1);
            mainProcess(result.code, result.date, result.stragety, 1, 1, context);
        }
//        Log.log(">>" + after_num);
    }

    public static int kn = 3;
    public static void main(String[] args) {
        try {
            GetAllBankuaiCode.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FILE = "D:\\new_tdx\\T0001\\export\\";
        DATE = "2023/08/02";
        CTROL_LEN = 1;
        LineContext context = new LineContext();
        if (kn == 1) {
            context.setUseMinute(1);
        } else if (kn == 2) {
            mainProcess("600613.txt", "2023/08/02", "IsBottom", 1, 1, context);
        } else if (kn == 3) {
            mainProcess("",  "2023/08/10~2023/08/20", "IsBottom", 0, 0, context);
        }else if (kn == 4) {
//            DATE = DateUtil.dateToString3(new Date());
//            context.setUseMinute(1);
            mainProcess("", "", "IsBottom", 0, 0, context);
        }
        mainResult();

        LineReports.report();
    }

}


