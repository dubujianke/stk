package com.mk.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConceptDFCF {
    public static Map<String, Concept> map = new HashMap<>();
    public static Map<String, List<ScoreConcept>> codeMap = new HashMap<>();

    public static void add(String code, String conceptName, String concept_score) {
        Concept concept = map.get(conceptName);
        if(concept == null) {
            Concept concept1 = new Concept();
            concept1.name = conceptName;
            map.put(conceptName, concept1);
        }
        concept = map.get(conceptName);
        concept.add(code);

        List<ScoreConcept> codeConcept = codeMap.get(code);
        if(codeConcept == null) {
            codeMap.put(code, new ArrayList<>());
        }
        List<ScoreConcept> codeConcept_ = codeMap.get(code);
        ScoreConcept scoreConcept = new ScoreConcept();
        scoreConcept.score= Double.parseDouble(concept_score.trim());
        scoreConcept.concept = concept;
        codeConcept_.add(scoreConcept);
    }

    public static List<ScoreConcept> getList(String code) {
        List<ScoreConcept> ret = new ArrayList<>();
        List<ScoreConcept> codeConcept = codeMap.get(code);
        if(codeConcept!=null) {
            for(ScoreConcept scoreConcept:codeConcept) {
                double score = scoreConcept.score;
                if(score>1.5) {
                    ret.add(scoreConcept);
                }
            }
        }
        return ret;
    }

    public static String getListStr(String code) {
        StringBuffer stringBuffer = new StringBuffer();
        List<ScoreConcept> ret = new ArrayList<>();
        List<ScoreConcept> codeConcept = codeMap.get(code);
        if(codeConcept!=null) {
            for(ScoreConcept scoreConcept:codeConcept) {
                double score = scoreConcept.score;
                if(score>1.6) {
                    ret.add(scoreConcept);
                    stringBuffer.append(scoreConcept.concept.name+"("+scoreConcept.score+") ");
                }
            }
        }
        return stringBuffer.toString();
    }
}
