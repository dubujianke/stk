package com.alading.model;

import java.util.*;

public class Table {
    public boolean sort = true;
    public Map<String, Integer> map = new HashMap<>();
    public List<Row> rows = new ArrayList<>();
    private Filter filter;

    public Map<String, Boolean> cacheMap = new HashMap<>();
    public Map<String, Row> rowMap = new HashMap<>();

    public void add(String key) {
        cacheMap.put(key, true);
    }

    public Row getRow(String code) {
        for (Row row : rows) {
            String acode = row.cols.get(0).data;
            if(acode.trim().equalsIgnoreCase("code")) {
                continue;
            }
            acode = acode.substring(0, acode.indexOf(" "));
            if (code.equalsIgnoreCase(acode)) {
                return row;
            }
        }
        return null;
    }

    public void initColName() {
        Row header = rows.get(0);
        int colSize = header.cols.size();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            if(row.cols.size()==330) {
                list.add(i);
            }
        }
        list.forEach(v-> {
            rows.remove(v);
        });
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            for (int j = 0; j < colSize; j++) {
                String colName = header.getStr(j);
                if(row.cols.size()==330) {
                    int a = 0;
                }
                row.getCol(j).colName = colName;
            }
        }
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public interface Filter {
        boolean filter(int rowNumber, Row row);

    }

    public void addFirst(Row col) {
        col.setTable(this);
        rows.add(0, col);
    }

    public void add(Row col) {
        if (cacheMap.get(col.getStr(0)) != null) {
            return;
        }
        add(col.getStr(0));
        col.setTable(this);
        rows.add(col);
    }


    public void add(Table table) {
        for (int i = 1; i < table.rows.size(); i++) {
            //TODO
            table.rows.get(i).setTable(this);
            rows.add(table.rows.get(i));
        }
    }

    public void initIndex() {
        Row row = rows.get(0);
        for (int i = 0; i < row.cols.size(); i++) {
            map.put(row.cols.get(i).data, i);
        }

        for(int i=0; i<rows.size(); i++) {
            rows.get(i).setTable(this);
        }
        initColName();
    }


    public Row getRow(int i) {
        return rows.get(i);
    }

    public int getRowLen() {
        return rows.size();
    }

    public String getCell(int r, int c) {
        return rows.get(r).getCol(c).data;
    }

    public boolean isBold(int r, int c) {
        return rows.get(r).getCol(c).isBold;
    }

    public boolean isItalic(int r, int c) {
        return rows.get(r).getCol(c).isItalic;
    }

    public int getColumn(String name) {
        if (map == null) {
            int a = 0;
        }
        Object obj =  map.get(name);
        if(obj==null) {
            int a = 0;
        }
        return map.get(name);
    }


    public double[] getColumn(String name, int from, int to) {
        initIndex();
        int len = to - from + 1;
        double[] vs = new double[len];
        int col = getColumn(name);
        for (int i = 0; i < len; i++) {
            vs[i] = Double.parseDouble(getCell(from + i - 1, col));
        }
        return vs;
    }


    public void filter(String name, String names[], int from, int to, double min) {
        initIndex();
        int len = to - from + 1;
        int col = getColumn(name);
        for (int i = 0; i < len; i++) {
            double v = Double.parseDouble(getCell(from + i - 1, col));
            String code = getCell(from + i - 1, 0);
            String date = getCell(from + i - 1, 2);
            if (Math.abs(v) < min) {
                String str = code + " " + date;
                for (String aname : names) {
                    int acol = getColumn(aname);
                    str += "\t" + aname + ":" + getCell(from + i - 1, acol);
                }
                ////System.out.println(str + " " + v);
            }
        }
    }


    public void sort() {
        this.initIndex();
        if (!sort) {
            return;
        }
        rows.sort(new Comparator<Row>() {
            @Override
            public int compare(Row o1, Row o2) {
                try {
                    String v = o1.cols.get(0).data;
                    String v2 = o2.cols.get(0).data;
                    if (v.equalsIgnoreCase("code")) {
                        return 1;
                    }
                    if (v2.equalsIgnoreCase("code")) {
                        return 1;
                    }
                    double f1 = o1.getFloat(getColumn("涨幅"));
                    double f2 = o2.getFloat(getColumn("涨幅"));
                    return (int) (100 * (f2 - f1));
                } catch (Exception e) {
                    return 1;
                }

            }
        });
    }

}
