package com.mk.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankuaiModel {
    public List<String> list = new ArrayList<>();
    public Map<String, String> map = new HashMap<>();
    public String name;
    public void add(String code) {
        list.add(code);
        map.put(code, "");
    }

}
