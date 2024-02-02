package com.mk.model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllModel {
    public static Map<String, Bankuai> infoMap = new HashMap<>();
    public static Map<String, BankuaiModel> nameMap = new HashMap<>();
    public static Map<String, List<BankuaiModel>> map = new HashMap<>();
    public static BankuaiModel get(String name) {
        return nameMap.get(name);
    }

    public static void add(String code, String bkName) {
        BankuaiModel bankuaiModel = get(bkName);
        if(bankuaiModel == null) {
            bankuaiModel = new BankuaiModel();
            nameMap.put(bkName, bankuaiModel);
        }
        bankuaiModel.add(code);
    }

    public static void add(String code, Bankuai info) {
        infoMap.put(code, info);
    }

    public static void add(String code, List<String> bkNames) {
        for(String bkName: bkNames) {
            add(code, bkName);
        }
    }

}
