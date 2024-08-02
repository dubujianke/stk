package com.alading.model;

import java.util.*;

public class Concept {
    public String name;
    public Set<String> leaderList = new LinkedHashSet<>();
    public void add(String code) {
        leaderList.add(code);
    }
}
