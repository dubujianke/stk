package com.mk.tool.stock.tree;

import com.huaien.core.util.FileManager;
import com.mk.model.Table;
import com.mk.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TreeGraphZT {
    public static boolean filterFalse = true;
    public static TreeGraphZT instance = new TreeGraphZT();
    static {
        instance.read("D:\\py\\pythonProject\\bottom02_5_1.dot");
    }
    Map<String, TreeNode> map = new HashMap<>();
    TreeNode root;
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public Boolean process(Table row, int rowIdx) {
        boolean flag = root.process(row, rowIdx);
        return flag;
    }

    public static String getTag( String[] lines) {
        for(String line:lines) {
            if(line.startsWith("class")) {
                String ret =  line.trim().split("=")[1].trim();
                ret = ret.substring(0, ret.indexOf("\","));
                return ret.split("\\s+")[0];
            }
        }
        return "";
    }

    public static String getGini( String[] lines) {
        for(String line:lines) {
            if(line.startsWith("gini")) {
                String ret =  line.trim().split("=")[1].trim();
                if(ret.indexOf("\",")>=0) {
                    ret = ret.substring(0, ret.indexOf("\","));
                    return ret.split("\\s+")[0];
                }
                return ret;
            }
        }
        return "";
    }

    public void read(String path) {
        List<String> list = null;
        list = FileManager.readList(path);
        for (String str : list) {
            int off = str.indexOf(" ");
            if (off == -1) {
                continue;
            }
            String first = str.substring(0, off).trim();
            String last = str.substring(off + 1).trim();
            if (isInteger(first)) {
                if (last.startsWith("[label")) {
                    last = last.replace("[label=\"", "").replace("\"] ;", "");
                    String[] lines = last.split("\\\\n");
                    String tag = getTag(lines);
                    String gini = getGini(lines);

                    String[] items = lines[0].split("<=");
                    String columnName = items[0].trim();

                    if(items.length<2) {
                        if(tag.equalsIgnoreCase("False")) {
                            int a = 0;
                        }
                        String value = "";
                        TreeNode treeNode = new TreeNode();
                        treeNode.filterFalse = filterFalse;
                        treeNode.setColumnName("");
                        treeNode.tag = tag;
                        treeNode.gini = gini;
                        treeNode.setOp("");
                        treeNode.note = str;
                        treeNode.setValue(0);
                        map.put(first, treeNode);
                    }else {
                        String op = "<=";
                        String value = items[1].trim();
                        TreeNode treeNode = new TreeNode();
                        treeNode.filterFalse = filterFalse;
                        treeNode.setColumnName(columnName);
                        treeNode.setOp(op);
                        treeNode.tag = tag;
                        treeNode.note = str;
                        treeNode.setValue(Double.parseDouble(value));
                        map.put(first, treeNode);
                        if(root == null) {
                            root = treeNode;
                        }
                    }

                }
                if (last.startsWith("->")) {
                    last = last.trim();
                    int off2 = last.indexOf(" [");
                    if (off2 > 0) {
                        last = last.substring(0, off2);
                    }
                    last = last.replace("->", "").replace(" ;", "").trim();
                    Log.log(first+" ----> "+last);
                    TreeNode treeNode = map.get(first);
                    TreeNode treeNode2 = map.get(last);
                    treeNode.setNode(treeNode2);
                }
            }
        }
        int a = 0;
        a++;
    }

    public static void main(String[] args) {
        TreeGraphZT aTreeGraph = new TreeGraphZT();
        aTreeGraph.read("D:\\py\\pythonProject\\zt_bottom.dot");
    }
}
