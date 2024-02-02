package com.mk.tool.stock.filter;

import com.huaien.core.util.FileManager;
import com.mk.tool.stock.AbsStragety;
import com.mk.tool.stock.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNextZT {

    public static int nextMaohao(String items[], int from) {
        int i = from + 1;
        if (i >= items.length) {
            return items.length;
        }
        for (int j = i; j < items.length; j++) {
            String item = items[j];
            if (item.contains(":")) {
                return j;
            }
        }
        return items.length;
    }


    public static String[] assemble(String line) {
        String items[] = line.split("\\s+");
        return assemble(items);
    }

    public static String[] assemble(String items[]) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            if (item.contains(":")) {
                int idx = nextMaohao(items, i);
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = i; j < idx; j++) {
                    stringBuffer.append(" " + items[j]);
                }
                list.add(stringBuffer.toString());
                i = idx;
                i--;
            } else {
                list.add(item);
            }
        }
        return list.toArray(new String[]{});
    }


    public static void main(String[] args) {

        File files = new File(AbsStragety.resDir + "res/bottom/");
        File[] fs = files.listFiles();
        StringBuffer stringBuffer = new StringBuffer();
        for (File file : fs) {
            String dateMonitor = file.getName();
            if (!dateMonitor.startsWith("2023")) {
                continue;
            }
            List<String> list = FileManager.readList(AbsStragety.resDir + "res/bottom/" + dateMonitor);
            for (int i = 0; i < list.size(); i++) {
                String line = list.get(i);
                String items[] = line.split("\\s+");
                items = assemble(items);
                String code = items[0].trim();
                String type = items[2].trim();
                String date = items[3].trim();
                if (line.contains("next: 9.")) {
                    Log.logString(stringBuffer, line);
                }
                if (line.contains("next:10.")) {
                    Log.logString(stringBuffer, line);
                }
            }
        }
        try {
            FileManager.write("d:/zt.txt", stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
