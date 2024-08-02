package com.alading.model;

import java.util.*;

public class ConceptTHS {
    public static Map<String, Concept> map = new HashMap<>();
    public static Map<String, List<Concept>> codeMap = new HashMap<>();

    public static Concept contain(String name) {
        name = name.replace("概念", "");
        for(String concept:map.keySet()) {
            if(concept.contains(name)) {
                return map.get(concept);
            }
        }
        return null;
    }

    public static void add(String code, String conceptName) {
        Concept concept = map.get(conceptName);
        if(concept == null) {
            Concept concept1 = new Concept();
            concept1.name = conceptName;
            map.put(conceptName, concept1);
        }
        concept = map.get(conceptName);
        concept.add(code);

        List<Concept> codeConcept = codeMap.get(code);
        if(codeConcept == null) {
            codeMap.put(code, new ArrayList<>());
        }
        List<Concept> codeConcept_ = codeMap.get(code);
        codeConcept_.add(concept);
    }

    public static List<Concept> getList(String code) {
        List<Concept> codeConcept = codeMap.get(code);
        return codeConcept;
    }
}
