package com.alading.tool.stock.cyq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.alading.data.eastmoney.GetLInes;
import com.alading.data.eastmoney.GetTop10HolderDFCF;
import com.alading.tool.stock.AbsStragety;
import com.alading.tool.stock.Kline;
import java.util.List;

public class Cmf {


    public static CYQCalculator.CYQData get(String code, List<Kline> lines, String date) throws Exception {
        double decentRatio = GetTop10HolderDFCF.getDecendRatio(code);
        System.out.println(decentRatio);
        CYQCalculator cyqCalculator = new CYQCalculator(lines, decentRatio, null, 210);
        int idx = AbsStragety.getIdx(lines, date);
        if(idx<0) {
            return null;
        }
        CYQCalculator.CYQData cyqData = cyqCalculator.calc(idx);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        cyqData.x = null;
        cyqData.y = null;
        String ret = gson.toJson(cyqData);
        System.out.println(ret);
        return cyqData;
    }

    public static void main(String[] args) throws Exception {
//        List<Kline> lines = GetLInes.getFromNet("000518");
//        CYQCalculator.CYQData cyqData = Cmf.get("000518", lines, "2024-03-14");

//        List<Kline> lines = GetLInes.getFromNet("000063");
//        CYQCalculator.CYQData cyqData = Cmf.get("000063", lines, "2024-03-14");


        List<Kline> lines = GetLInes.getFromNet("603214");
        CYQCalculator.CYQData cyqData = Cmf.get("000063", lines, "2024-03-14");
    }
}
