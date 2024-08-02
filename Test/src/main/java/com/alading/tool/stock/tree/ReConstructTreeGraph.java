package com.alading.tool.stock.tree;

import com.alading.model.Table;

public class ReConstructTreeGraph extends TreeGraph{


    public static ReConstructTreeGraph instance(String path) {
        ReConstructTreeGraph instance = new ReConstructTreeGraph();
        instance.read(path);
        instance.restruct();
        return instance;
    }

    public Boolean subProcess(Table row, int rowIdx) {
        if(root==null) {
            return true;
        }
        return root.subProcess(row, rowIdx);
    }


    public void restruct() {
        if(root.isAllGiniN0()) {
//            root = null;
        }
        root.restruct();
    }



    public static void main(String[] args) {
        ReConstructTreeGraph aTreeGraph = instance("D:\\py\\pythonProject\\tree7.dot");
        aTreeGraph.restruct();
        int a = 0;
        a++;
    }
}
