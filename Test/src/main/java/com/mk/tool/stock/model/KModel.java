package com.mk.tool.stock.model;

import com.mk.model.Row;

import java.util.ArrayList;
import java.util.List;

public class KModel {

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Row row;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStragety() {
        return stragety;
    }

    public void setStragety(String stragety) {
        this.stragety = stragety;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public float getGuben() {
        return guben;
    }

    public void setGuben(float guben) {
        this.guben = guben;
    }

    public float getSpace250() {
        return space250;
    }

    public void setSpace250(float space250) {
        this.space250 = space250;
    }

    public float getNext() {
        return next;
    }

    public void setNext(float next) {
        this.next = next;
    }

    public List<Float> getChgHands() {
        return chgHands;
    }

    public void setChgHands(List<Float> chgHands) {
        this.chgHands = chgHands;
    }

    public float getPrevmon1() {
        return prevmon1;
    }

    public void setPrevmon1(float prevmon1) {
        this.prevmon1 = prevmon1;
    }

    public float getPrevmon2() {
        return prevmon2;
    }

    public void setPrevmon2(float prevmon2) {
        this.prevmon2 = prevmon2;
    }

    public boolean isCross() {
        return isCross;
    }

    public void setCross(boolean cross) {
        isCross = cross;
    }

    public List<MA> getMas() {
        return mas;
    }

    public void setMas(List<MA> mas) {
        this.mas = mas;
    }

    private String code;
    private String name;
    private String stragety;
    private String date;
    private String nextDate;
    private float guben;
    private float space250;
    private float next;
    private List<Float> chgHands = new ArrayList<>();
    private float prevmon1;
    private float prevmon2;
    private boolean isCross;
    private List<MA> mas = new ArrayList<>();

}
