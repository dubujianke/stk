package com.mk.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class ScoreConcept {
    public double score;
    public Concept concept;

    public String toString() {
        return concept.name+" "+score;
    }
}
