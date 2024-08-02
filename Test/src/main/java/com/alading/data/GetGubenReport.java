package com.alading.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.alading.data.eastmoney.GetGuben;
import com.alading.tool.stock.AbsStragety;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetGubenReport {

    static Map<String, Integer> map = new HashMap<>();

    public static void add(int v) {
        if (v < 100) {
            int v1 = map.get("0");
            v1++;
            map.put("0", v1);
        } else if (v >= 100 && v < 200) {
            int v1 = map.get("1");
            v1++;
            map.put("1", v1);
        } else if (v >= 200 && v < 300) {
            int v1 = map.get("2");
            v1++;
            map.put("2", v1);
        } else {
            int v1 = map.get("3");
            v1++;
            map.put("3", v1);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        map.put("0", 0);
        map.put("1", 0);
        map.put("2", 0);
        map.put("3", 0);
        File file = new File(AbsStragety.FILE);
        File fs[] = file.listFiles();
        for (File item : fs) {
            String name = item.getName().replace(".txt", "");
            int v = (int) GetGuben.retriveOrGetReal(name);
            add(v);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String ret = gson.toJson(map);
        System.out.println(ret);
    }
}
