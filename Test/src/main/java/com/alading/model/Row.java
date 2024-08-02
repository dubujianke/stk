package com.alading.model;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private int idx;
    public List<Col> cols = new ArrayList<>();
    public void add(Col col) {
        cols.add(col);
    }
    public int color = 0;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    Table table;

    public boolean isNull() {
        if(cols.size()==0) {
            return true;
        }
        return cols.get(0).data.trim().equalsIgnoreCase("") || cols.get(0).data.trim().equalsIgnoreCase("null");
    }
    public Col getCol(int i) {
        if(cols.size()==0) {
            return new Col();
        }
        return cols.get(i);
    }

    public void setCol(int i, String v) {
        if(cols.size()==0) {
            return;
        }
        Col col =  cols.get(i);
        col.data = v;
    }

    public String getStr(int i) {
        if(cols.size()==0) {
            return "";
        }
        return cols.get(i).data.trim();
    }

    public String getStr(String name) {
        if(cols.size()==0) {
            return "";
        }
        try {
            return cols.get(table.getColumn(name)).data;
        }catch (Exception e) {
            return "0";
        }

    }

    public int getInt(String name) {
        if(cols.size()==0) {
            return -999;
        }

        String data = cols.get(table.getColumn(name)).data.replace("+", "").trim();
        if(data.contains(".")) {
            return (int) Double.parseDouble(data);
        }
        return Integer.parseInt(data);
    }


    public double getFloat(String name) {
        if(cols.size()==0) {
            return -999;
        }
        try {
            return Double.parseDouble(cols.get(table.getColumn(name)).data.replace("+", ""));
        }catch (Exception e) {
            return 0;
        }
    }

    public boolean getBoolean(String name) {
        if(cols.size()==0) {
            return false;
        }
        return Boolean.parseBoolean(cols.get(table.getColumn(name)).data.replace("+", ""));
    }

    public double getFloat(int i) {
        if(cols.size()==0) {
            return -999;
        }
        String data = cols.get(i).data.replace("+", "");
        if(data.equalsIgnoreCase("true")) {
            return 1;
        }
        if(data.trim().equalsIgnoreCase("")) {
            return 0;
        }
        if(data.equalsIgnoreCase("false")) {
            return 0;
        }
        try {
            return Double.parseDouble(data);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getFloatSplit0(int i) {
        if(cols.size()==0) {
            return -999;
        }
        String ret = cols.get(i).data.replace("+", "");
        return Math.abs(Double.parseDouble(ret.trim().split("/")[0]));
    }

    public double getFloat0(int i) {
        if(cols.size()==0) {
            return -999;
        }
        String v = cols.get(i).data.split("/")[0].replace("+", "");
        return Double.parseDouble(v);
    }
    public double getFloat1(int i) {
        if(cols.size()==0) {
            return -999;
        }
        String v = cols.get(i).data.split("/")[1].replace("+", "");
        return Double.parseDouble(v);
    }

    public double getFloat2(int i) {
        if(cols.size()==0) {
            return -999;
        }
        String v = cols.get(i).data.split("\\(")[0].replace("+", "");
        return Double.parseDouble(v);
    }


    public int getInt(int i) {
        try {
            if(cols.size()==0) {
                return -999;
            }
            return Integer.parseInt(cols.get(i).data.trim());
        }catch (Exception e) {
            return 0;
        }
    }

    public boolean getBoolean(int i) {
        try {
            if(cols.size()==0) {
                return false;
            }
            return Boolean.parseBoolean(cols.get(i).data.trim());
        }catch (Exception e) {
            return false;
        }
    }

    public double getFloatAbs(int i) {
        if(cols.size()==0) {
            return -999;
        }
        return Math.abs(Double.parseDouble(cols.get(i).data));
    }

    public int getColLen() {
        return cols.size();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int rol = 0; rol < cols.size(); rol++) {
            stringBuffer.append(cols.get(rol).data+" ");
        }
        return stringBuffer.toString();
    }


    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
