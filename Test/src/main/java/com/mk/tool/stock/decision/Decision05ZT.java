package com.mk.tool.stock.decision;

import com.mk.model.Row;
import com.mk.model.Table;
import com.mk.tool.stock.Log;
import com.mk.tool.stock.tree.*;


/**
 * OK
 * 大涨幅度在0-5之间
 */
public class Decision05ZT {
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


