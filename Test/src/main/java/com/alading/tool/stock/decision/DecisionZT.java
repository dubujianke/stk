package com.alading.tool.stock.decision;

import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.tree.TreeGraphZT;



public class DecisionZT {
    public static Boolean judge(Table table, int rowNumber) {
        if (rowNumber == 0) {
            return  false;
        }
        Row row = table.rows.get(rowNumber);
        if (row.isNull()) {
            return false;
        }

        Boolean flag = TreeGraphZT.instance.process(table, rowNumber);
        return flag;
    }
    

}


