package com.mk.tool.stock.cyq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mk.data.eastmoney.GetLInes;
import com.mk.tool.stock.Kline;
import com.mk.util.StringUtil;

import java.io.IOException;
import java.util.List;

public class Cmf {
    CYQCalculator cyqCalculator;

    public void getCmf() throws Exception {
        String code = "603999";
        List<Kline> lines = GetLInes.getFromNet(code);
        cyqCalculator = new CYQCalculator(lines, 9, null, null);
        CYQCalculator.CYQData cyqData = cyqCalculator.calc(209);
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String ret = gson.toJson(cyqData.percentChips);
        System.out.println(ret);
    }

    public static void main(String[] args) throws Exception {
        new Cmf().getCmf();
    }
}
