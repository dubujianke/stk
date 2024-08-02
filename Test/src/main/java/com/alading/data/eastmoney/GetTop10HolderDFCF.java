package com.alading.data.eastmoney;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.FileManager;
import com.alading.model.ConceptDFCF;
import com.alading.model.ScoreConcept;
import com.alading.tool.stock.AbsStragety;
import com.alading.tool.stock.Kline;
import com.alading.util.FileUtil;
import com.alading.util.HttpsUtils;
import com.alading.util.Log;
import com.alading.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GetTop10HolderDFCF {
    static String mode = "11";
    static String subName = "top10holder";
    static String getFile(String code) {
        return "res/data/" + code + "_" + mode + ".txt";
    }

    static String getCode(String code) {
        code = "bk_" + code;
        return code;
    }

    public static double retriveOrGetTop10Ratio(String code) {
        String v = retriveOrGet(code);
        JSONArray ret = JSONObject.parseArray(v);
        int len = ret.size();
        double total = 0;
        for(int i=0; i<len; i++) {
            JSONObject obj = ret.getJSONObject(i);
//            System.out.println(obj.getFloat("FREE_HOLDNUM_RATIO"));
            double ratio = obj.getFloat("FREE_HOLDNUM_RATIO");
            total+=ratio;
        }
        return StringUtil.toFix(total/100);
    }

    public static double getDecendRatio(String code) {
        double ratio = retriveOrGetTop10Ratio(code);
        if(ratio<0.01) {
            return 1;
        }
        if(ratio>1) {
            return 1;
        }
        ratio = 1/(1-ratio);
        return ratio;
    }

    public static String retriveOrGetShizhi(String code, double v) {
        return retriveOrGet(code);
    }

    public static String retriveOrGet(String code) {
        try {
            try {
                String v = FileManager.read(AbsStragety.resDir + "/res/"+subName+"/" + code + ".txt");
                if (!StrUtil.isEmpty(v)) {
                    return v;
                }
            } catch (Exception e) {
                return "[]";
            }

            String codeType = "";
            if (code.startsWith("6")) {
                codeType = "SH";
            } else {
                codeType = "SZ";
            }

            String date = GetTop10HolderDateDFCF.retriveOrGet(code);
            JSONObject jsonObject = new JSONObject();
            String url = "https://datacenter.eastmoney.com/securities/api/data/v1/get?reportName=RPT_F10_EH_FREEHOLDERS&columns=SECUCODE%2CSECURITY_CODE%2CEND_DATE%2CHOLDER_RANK%2CHOLDER_NEW%2CHOLDER_NAME%2CHOLDER_TYPE%2CSHARES_TYPE%2CHOLD_NUM%2CFREE_HOLDNUM_RATIO%2CHOLD_NUM_CHANGE%2CCHANGE_RATIO&quoteColumns=&filter=(SECUCODE%3D%22" + code + "." + codeType + "%22)(END_DATE%3D%27"+date+"%27)&pageNumber=1&pageSize=&sortTypes=1&sortColumns=HOLDER_RANK&source=HSF10&client=PC&v=047999239711719377";//
            String str = HttpsUtils.sendByHttpDFCF2(url);
            JSONObject jsonObject2 = JSONObject.parseObject(str);
            JSONArray ret = jsonObject2.getJSONObject("result").getJSONArray("data");
            FileManager.write(AbsStragety.resDir + "/res/"+subName+"/" + code + ".txt", "" + ret.toString());
            return ret.toJSONString();
        } catch (Exception e) {
        }
        return "";
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
            if(len==0) {
                continue;
            }
            for (int i = 0; i < len; i++) {
                JSONObject obj = jsonObject2.getJSONObject(i);
                String concept_name = obj.getString("BOARD_NAME");
                String concept_score = obj.getString("BOARD_YIELD");
                ConceptDFCF.add(name, concept_name, concept_score);
            }
        }

        List<ScoreConcept> ret = ConceptDFCF.getList("002049");
//        Map<String, List<ScoreConcept>> map = ConceptDFCF.codeMap;
//        Map<String, Concept> map2 = ConceptTHS.map;
//        for(String concept:map.keySet()) {
//            Concept v = ConceptTHS.contain(concept);
//            if(v != null) {
//                System.out.println(concept);
//                v.concept_score = map.get(concept).concept_score;
//            }
//        }
        int a = 0;

    }

    public static void initAll() {
        GetConceptTHS.initConcept();
        initConcept();
//        List<ScoreConcept> ret = ConceptDFCF.getList("002049");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File(AbsStragety.FILE);
        File fs[] = file.listFiles();
        for (File item : fs) {
            Thread.currentThread().sleep(10);
            String name = item.getName().replace(".txt", "");
//            if (item.toString().contains("002269")) {
//                int a = 0;
//                com.mk.tool.stock.Log.log(item.toString());
//            }
            String v = retriveOrGet(name);
        }

    }
}
