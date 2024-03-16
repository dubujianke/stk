package com.mk.tool.stock.tree;

import com.mk.model.Table;
import com.mk.tool.stock.Log;
import com.mk.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    static int mm = 0;
    public boolean filterFalse = true;
    public boolean process(Table row, int rowIdx) {
        if(filterFalse) {
            return process__(row, rowIdx);
        }
        return process_(row, rowIdx);
    }

    public boolean process_(Table row, int rowIdx) {
        if (StringUtil.isNull(columnName)) {
            boolean ret = tag.equalsIgnoreCase("True");
            return ret;
        }
        double gini = this.getGini();
        double v = row.getRow(rowIdx).getFloat(row.getColumn(columnName));
        if (v <= value) {
            return left.process(row, rowIdx);
        } else {
            return right.process(row, rowIdx);
        }
    }

    public boolean process__(Table row, int rowIdx) {
        double gini = this.getGini();
        if (StringUtil.isNull(columnName)) {
            boolean ret = tag.equalsIgnoreCase("True");
            if(Math.abs(gini)<0.01 && !ret) {
                return false;
            }
            return true;
        }

        double v = row.getRow(rowIdx).getFloat(row.getColumn(columnName));
        if (v <= value) {
            return left.process(row, rowIdx);
        } else {
            return right.process(row, rowIdx);
        }
    }


    public double getGini() {
        int idx = note.indexOf("gini");
        if(idx<0) {
            return 100;
        }
        String str = note.substring(idx+"gini".length());
        int idx2 = str.indexOf("\\n");
        str = str.substring(0, idx2).replace("=", "").trim();
        return Double.parseDouble(str);
    }

    public boolean subProcess(Table row, int rowIdx) {
        if (StringUtil.isNull(columnName)) {
            boolean ret = tag.equalsIgnoreCase("True");
            return ret;
        }

        double v = row.getRow(rowIdx).getFloat(row.getColumn(columnName));

        if (v <= value) {
            if (left == null) {
                return true;
            }
            return left.subProcess(row, rowIdx);
        } else {
            if (right == null) {
                return true;
            }
            return right.subProcess(row, rowIdx);
        }
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private String columnName;
    public String tag;
    public String gini = "";
    private String op;//>= <= > < ==
    private double value;

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    private TreeNode left;
    private TreeNode right;
    public String note;

    public void setNode(TreeNode treeNode) {
        if (left == null) {
            left = treeNode;
            return;
        }
        if (right == null) {
            right = treeNode;
            return;
        }
        int a = 0;
        a++;
    }

    boolean checkFlag = false;

    public void getLeaf(List<TreeNode> list) {
        if (left == null && right == null) {
            list.add(this);
            return;
        }
        left.getLeaf(list);
        right.getLeaf(list);
    }

    public boolean isGini0() {
        double vGini = Double.parseDouble(gini);
        if (vGini > 0) {
            return false;
        }
        return true;
    }

    public boolean isAllGiniN0() {
        List<TreeNode> list = new ArrayList<>();
        getLeaf(list);
        for (TreeNode node : list) {
            if (node.isGini0() && node.tag.equalsIgnoreCase("False")) {
                return false;
            }
        }
        return true;
    }

    public void restruct() {
        if (left != null) {
            if (left.isAllGiniN0()) {
                left = null;
            } else {
                left.restruct();
            }
        }

        if (right != null) {
            if (right.isAllGiniN0()) {
                right = null;
            } else {
                right.restruct();
            }
        }

    }

    public String toString() {
        return note + " " + gini;
    }
}
