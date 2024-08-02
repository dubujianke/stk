package com.alading.tool.stock.decision;

import com.alading.model.Row;
import com.alading.model.Table;
import com.alading.tool.stock.Log;
import com.alading.tool.stock.tree.*;


/**
 * OK
 * 大涨幅度在0-5之间
 */
public class Decision05 {
    public static StringBuffer resultBuffer = new StringBuffer();
    static ReConstructTreeGraph aFilterTreeGraph = ReConstructTreeGraph.instance("D:\\py\\pythonProject\\tree7.dot");

    static ReConstructTreeGraph aFilterTreeGraphZT = ReConstructTreeGraph.instance("D:\\py\\pythonProject\\zt_bottom.dot");

    static FilterTreeGraph aFilterTreeGraph2 = FilterTreeGraph.instance("D:\\py\\pythonProject\\tree_00.dot");
    public static Boolean[] judge(Table table, int rowNumber) {
        if (rowNumber == 0) {
            return new Boolean[]{false, false};
        }
        Row row = table.rows.get(rowNumber);
        if (row.isNull()) {
            return new Boolean[]{false, false};
        }

        String acode = row.getCol(0).data;
        acode = acode.substring(0, acode.indexOf(" "));
        String adate = row.getCol(3).data;
//        Boolean aFlag = aFilterTreeGraph.subProcess(table, rowNumber);
//        if(!aFlag) {
//            return new Boolean[]{false, false};
//        }

//        Boolean flag = TreeGraph2.instance.process(table, rowNumber);
//        if(flag) {
            if(acode.contains("000897")) {
                int a = 0;
                a++;
            }
            Boolean[] flags = TreeGraph.instance.process(table, rowNumber);
            Log.log(acode + " " + adate + " " + flags[0] + " " + flags[1]);
            return flags;
//        }
//        return new Boolean[]{false, false};
    }


}


