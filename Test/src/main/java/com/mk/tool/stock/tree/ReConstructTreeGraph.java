package com.mk.tool.stock.tree;

import com.huaien.core.util.FileManager;
import com.mk.model.Table;
import com.mk.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
