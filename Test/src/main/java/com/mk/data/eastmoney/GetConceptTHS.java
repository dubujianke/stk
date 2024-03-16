package com.mk.data.eastmoney;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.FileManager;
import com.mk.model.Concept;
import com.mk.model.ConceptTHS;
import com.mk.model.ConceptTHSLeader;
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
import java.util.Set;

public class GetConceptTHS {
    static String mode = "11";

    static String getFile(String code) {
        return "res/data/" + code + "_" + mode + ".txt";
    }

    static String getCode(String code) {
        code = "bk_" + code;
        return code;
    }

    public static String retriveOrGetShizhi(String code, double v)  {
        return retriveOrGet(code);
    }

    public static String retriveOrGet(String code) {
        try {
            try {
                String v = FileManager.read(AbsStragety.resDir+"/res/code_conceptths/" + code + ".txt");
                if (!StrUtil.isEmpty(v)) {
                    return v;
                }
            } catch (Exception e) {

            }


//            JSONObject jsonObject = new JSONObject();
//            String url = "https://basic.10jqka.com.cn/basicapi/concept/stock_concept_list/?code="+code+"&locale=zh_CN";
//            String str = HttpsUtils.sendByHttpTHS(url);
//            JSONObject jsonObject2 = JSONObject.parseObject(str);
//            JSONArray ret = jsonObject2.getJSONArray("data");
//            FileManager.write(AbsStragety.resDir+"/res/code_conceptths/" + code + ".txt", ""+ret.toString());
//            return  ret.toJSONString();
        } catch (Exception e) {
        }
        return "[]";
    }

    public static void appendToFile(String cdde, List<String> list) {
        try {
            String file = cdde + ".txt";
            FileManager.write(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String append(String url, String param, String v) {
        return url + "&" + param + "=" + URLEncoder.encode(v);
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

    private static void log(String msg, String... vals) {
        ////System.out.println(String.format(msg, vals));
    }

    public static void initConcept() {
        File file = new File(AbsStragety.FILE);
        File fs[] = file.listFiles();
        for (File item : fs) {
            String name = item.getName().replace(".txt", "");
            String v = retriveOrGet(name);
            JSONArray jsonObject2 = JSONArray.parseArray(v);
            int len = jsonObject2.size();
            for(int i=0; i<len; i++) {
                JSONObject obj = jsonObject2.getJSONObject(i);
                String concept_name = obj.getString("concept_name");
                ConceptTHS.add(name, concept_name);
            }
        }
        for (File item : fs) {
            String name = item.getName().replace(".txt", "");
            String v = retriveOrGet(name);
            JSONArray jsonObject2 = JSONArray.parseArray(v);
            int len = jsonObject2.size();
            for(int i=0; i<len; i++) {
                JSONObject obj = jsonObject2.getJSONObject(i);
                String concept_name = obj.getString("concept_name");
                JSONArray array = obj.getJSONArray("leader");
                for(int j=0; j<array.size(); j++) {
                    JSONObject aitem = array.getJSONObject(j);
                    String aCode = aitem.getString("code");
//                    String aname = aitem.getString("name");
                    ConceptTHSLeader.add(aCode, concept_name);
                }
            }
        }

        Set<Concept> codeConcept = ConceptTHSLeader.getList("600375");
//        Map<String, ConceptLeader> map1 = ConceptLeader.map;

        int a = 0;

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        initConcept();

//        File file = new File(AbsStragety.FILE);
//        File fs[] = file.listFiles();
//        for (File item : fs) {
//            Thread.currentThread().sleep(10);
//            String name = item.getName().replace(".txt", "");
//            String v = retriveOrGet(name);
//        }

    }
}
