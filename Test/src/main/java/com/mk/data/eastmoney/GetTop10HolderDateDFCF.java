package com.mk.data.eastmoney;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.FileManager;
import com.mk.model.ConceptDFCF;
import com.mk.model.ScoreConcept;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.util.FileUtil;
import com.mk.util.HttpsUtils;
import com.mk.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GetTop10HolderDateDFCF {
    static String mode = "11";
    static String subName = "top10holder";
    static String getFile(String code) {
        return "res/data/" + code + "_" + mode + ".txt";
    }

    static String getCode(String code) {
        code = "bk_" + code;
        return code;
    }



    public static String retriveOrGet(String code) {
        try {

            String codeType = "";
            if (code.startsWith("6")) {
                codeType = "SH";
            } else {
                codeType = "SZ";
            }

            JSONObject jsonObject = new JSONObject();
            String url = "https://datacenter.eastmoney.com/securities/api/data/v1/get?reportName=RPT_F10_EH_FREEHOLDERSDATE&columns=SECUCODE%2CEND_DATE&quoteColumns=&filter=(SECUCODE%3D%22"+ code + "." + codeType + "%22)&pageNumber=1&pageSize=&sortTypes=-1&sortColumns=END_DATE&source=HSF10&client=PC&v=022443932874690375";
            String str = HttpsUtils.sendByHttpDFCF2(url);
            JSONObject jsonObject2 = JSONObject.parseObject(str);
            JSONArray ret = jsonObject2.getJSONObject("result").getJSONArray("data");
//            FileManager.write(AbsStragety.resDir + "/res/"+subName+"/" + code + ".txt", "" + ret.toString());
            JSONObject obj = ret.getJSONObject(0);
            String date = obj.getString("END_DATE").replaceAll(" 00:00:00", "");
            return date;
        } catch (Exception e) {
        }
        return "";
    }



    public static List<String> read(String code) throws IOException {
        code = getCode(code);
        List<Kline> days = new ArrayList();
        String str = FileUtil.read(getFile(code));
        JSONObject jsonObject = JSONObject.parseObject(str);
        String data = jsonObject.getString("data");
        String[] lines = data.split(";");
        int i = 0;
        for (String line : lines) {
            int idx = i;
            Kline dayLine = new Kline(line.trim(), 0);
            dayLine.allLineList = days;
            dayLine.setIdx(idx);
            days.add(dayLine);
//            Log.log(dayLine.date+" "+dayLine.open+" "+dayLine.max);

            i++;
        }
        Log.log("" + days.size());
        return null;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File(AbsStragety.FILE);
        File fs[] = file.listFiles();
        for (File item : fs) {
            Thread.currentThread().sleep(10);
            String name = item.getName().replace(".txt", "");
            String v = retriveOrGet(name);
        }

    }
}
