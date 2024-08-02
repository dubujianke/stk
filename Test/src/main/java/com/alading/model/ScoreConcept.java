package com.alading.model;

public class ScoreConcept {
    public double score;
    public Concept concept;

    public String toString() {
        return concept.name+" "+score;
    }
}
