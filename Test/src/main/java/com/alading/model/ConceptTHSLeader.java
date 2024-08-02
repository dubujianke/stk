package com.alading.model;

import java.util.*;

public class ConceptTHSLeader {
    public static Map<String, Concept> map = new HashMap<>();
    public static Map<String, Set<Concept>> codeMap = new HashMap<>();

    public static void add(String code, String conceptName) {
        Concept concept = map.get(conceptName);
        if(concept == null) {
            Concept concept1 = new Concept();
            concept1.name = conceptName;
            map.put(conceptName, concept1);
        }
        concept = map.get(conceptName);
        concept.add(code);

        Set<Concept> codeConcept = codeMap.get(code);
        if(codeConcept == null) {
            codeMap.put(code, new LinkedHashSet<>());
        }
        Set<Concept> codeConcept_ = codeMap.get(code);
        codeConcept_.add(concept);
    }

    public static Set<Concept> getList(String code) {
        Set<Concept> codeConcept = codeMap.get(code);
        return codeConcept;
    }
}
