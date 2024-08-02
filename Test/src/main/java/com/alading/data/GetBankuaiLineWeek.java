package com.alading.data;

import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.FileManager;
import com.alading.tool.stock.Kline;
import com.alading.util.FileUtil;
import com.alading.util.HttpsUtils;
import com.alading.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//http://d.10jqka.com.cn/v4/line/bk_881170/11/last.js  week
//http://d.10jqka.com.cn/v4/line/bk_881170/21/last.js month
//http://q.10jqka.com.cn/thshy/
public class GetBankuaiLineWeek {

    static String mode = "11";
    static String getFile(String code) {
        return "res/data/" + code + "_"+mode+".txt";
    }

    static String getCode(String code) {
        code = "bk_"+code;
        return code;
    }
    public static void save(String code) throws IOException {
        code =  getCode(code);
        if (FileManager.exist(getFile(code))) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        String str = HttpsUtils.sendByHttp(jsonObject, "http://d.10jqka.com.cn/v4/line/" + code + "/"+mode+"/last.js");
        str = str.replace("quotebridge_v4_line_"+code+"_"+mode+"_last(", "");
        str = str.trim().substring(0, str.length() - 1);
        JSONObject jsonObject2 = JSONObject.parseObject(str);
        FileUtil.write(getFile(code), jsonObject2.toJSONString());
        //System.out.println(jsonObject2.getInteger("total"));
    }

    public static List<String> read(String code) throws IOException {
        code =  getCode(code);
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
        Log.log(""+days.size());
        return null;
    }


    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException {
        GetBankuaiLineWeek.save("881162");
        List<String> list = GetBankuaiLineWeek.read("881162");
//        AllModel.add("sh600547", list);
//        log(""+AllModel.nameMap);
    }
}
