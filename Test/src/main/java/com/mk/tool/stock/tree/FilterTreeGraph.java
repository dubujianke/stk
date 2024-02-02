package com.mk.tool.stock.tree;

import com.mk.model.Table;

public class FilterTreeGraph extends TreeGraph{
    public static FilterTreeGraph instance(String path) {
        FilterTreeGraph instance = new FilterTreeGraph();
        instance.read(path);
        return instance;
    }

    public boolean subProcess(Table row, int rowIdx) {
        return root.process(row, rowIdx);
    }
}
