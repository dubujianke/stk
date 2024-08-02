package com.alading.model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class EastMoneyConceptLeader {
    public static Map<String, EastMoneyConceptLeader> map = new HashMap<>();
    public static Map<String, Set<EastMoneyConceptLeader>> codeMap = new HashMap<>();
    public String name;
    public Set<String> leaderList = new LinkedHashSet<>();
    public void add(String code) {
        leaderList.add(code);
    }

    public static  void add(String code, String conceptName) {
        EastMoneyConceptLeader concept = map.get(conceptName);
        if(concept == null) {
            EastMoneyConceptLeader concept1 = new EastMoneyConceptLeader();
            concept1.name = conceptName;
            map.put(conceptName, concept1);
        }
        concept = map.get(conceptName);
        concept.add(code);
        if(code.equalsIgnoreCase("000063")) {
            int a = 0;
        }
        Set<EastMoneyConceptLeader> codeConcept = codeMap.get(code);
        if(codeConcept == null) {
            codeMap.put(code, new LinkedHashSet<>());
        }
        Set<EastMoneyConceptLeader> codeConcept_ = codeMap.get(code);
        codeConcept_.add(concept);
    }

    public static Set<EastMoneyConceptLeader> getList(String code) {
        Set<EastMoneyConceptLeader> codeConcept = codeMap.get(code);
        return codeConcept;
    }
}
