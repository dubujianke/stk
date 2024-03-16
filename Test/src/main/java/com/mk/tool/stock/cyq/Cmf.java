package com.mk.tool.stock.cyq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mk.data.eastmoney.GetLInes;
import com.mk.data.eastmoney.GetTop10HolderDFCF;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Kline;
import com.mk.util.StringUtil;
import java.io.IOException;
import java.util.List;

public class Cmf {


    public static void get(String code, int offset) throws Exception {
        double decentRatio = GetTop10HolderDFCF.getDecendRatio(code);
        System.out.println(decentRatio);
        List<Kline> lines = GetLInes.getFromNet(code);
        CYQCalculator cyqCalculator = new CYQCalculator(lines, decentRatio, null, null);
        int idx = AbsStragety.getIdx(lines, lines.get(lines.size()-1 - offset).date);
        if(idx<0) {
            return;
        }
        CYQCalculator.CYQData cyqData = cyqCalculator.calc(idx);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String ret = gson.toJson(cyqData.percentChips);
        System.out.println(ret);
    }

    public static void main(String[] args) throws Exception {
        Cmf.get("002376", 1);
    }
}
