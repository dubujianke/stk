package com.alading.tool.stock;

import cn.hutool.core.util.StrUtil;
import com.alading.model.Row;
import com.alading.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * (dopen-dma30) > 1 &&
 * (dopen-dma30)>0  && (dopen-dma30)>0  & (dopen-dma30)>0
 */
public class Command {
    List<Command> commands = new ArrayList<>();
    boolean isOr;
    public String code;
    public boolean result;

    public static int getNum(String code) {
        int idx = code.indexOf("(");
        int idx2 = code.indexOf(")");
        String item = code.substring(idx + 1, idx2);
        int num = Integer.parseInt(item);
        return num;
    }

    public static String[] getParams(String code) {
        int idx = code.indexOf("(");
        int idx2 = code.indexOf(")");
        String item = code.substring(idx + 1, idx2);
        String items[] = item.split(",");
        return items;
    }

    public static double  getFloat(String code) {
        int idx = code.indexOf("(");
        int idx2 = code.indexOf(")");
        String item = code.substring(idx + 1, idx2);
        double num = Integer.parseInt(item);
        return num;
    }

    public static double getMin(double minfrac, double frac) {
        if(frac < minfrac) {
            minfrac = frac;
        }
        return minfrac;
    }

    public double getValue(String code, Kline kLine, Table table, int rowIdx) {
        code = code.trim();
        try {
            return Double.parseDouble(code.trim());
        }catch (Exception e) {
            table.initIndex();
            Row row = table.rows.get(rowIdx);
            double v5 = row.getFloat(table.getColumn(code));
            return v5;
        }
    }

    public String getStringValue(String code, Kline kLine, Table table, int rowIdx) {
        code = code.trim();
        try {
            if(code.equalsIgnoreCase("true")) {
                return "true";
            }
            if(code.equalsIgnoreCase("false")) {
                return "false";
            }
            return ""+Double.parseDouble(code.trim());
        }catch (Exception e) {
            table.initIndex();
            Row row = table.rows.get(rowIdx);
            String v5 = row.getStr(table.getColumn(code));
            try {
                return ""+Double.parseDouble(v5);
            }catch (Exception e2) {
            }
            return v5;
        }
    }

//    public double getCoumputeValue(String code, Kline kline, Table weekline) {
//        if (containsSign(code)) {
//            code = code.trim();
////            code = code.substring(1, code.length()-1);
//            String[] codes = code.split("&");
//            CalCommand command = new CalCommand();
//            command.code = code;
//            return command.calValue(kline, weekline);
//        }
//        return 0;
//    }

    public boolean process(Kline kLine, Table table, int rowIdx) {
        if (commands.size() > 0) {
            if (isOr) {
                for (Command command : commands) {
                    boolean flag = command.process(kLine, table, rowIdx);
                    if (flag) {
                        return true;
                    }
                }
                return false;
            } else {
                for (Command command : commands) {
                    boolean flag = command.process(kLine, table, rowIdx);
                    if (!flag) {
                        return false;
                    }
                }
                return true;
            }

        } else {
            code = code.trim();
            if (code.contains(">=")) {
                String[] codes = code.split(">=");
                double v0 = getValue(codes[0], kLine, table, rowIdx);
                double v1 = getValue(codes[1], kLine, table, rowIdx);
                if (v0 >= v1) {
                    return true;
                }
                return false;
            } else if (code.contains("<=")) {
                String[] codes = code.split("<=");
                double v0 = getValue(codes[0], kLine, table, rowIdx);
                double v1 = getValue(codes[1], kLine, table, rowIdx);
                if (v0 <= v1) {
                    return true;
                }
                return false;
            }else if (code.contains(">")) {
                String[] codes = code.split(">");
                double v0 = getValue(codes[0], kLine, table, rowIdx);
                double v1 = getValue(codes[1], kLine, table, rowIdx);
                if (v0 > v1) {
                    return true;
                }
                return false;
            } else if (code.contains("<")) {
                String[] codes = code.split("<");
                double v0 = getValue(codes[0], kLine, table, rowIdx);
                double v1 = getValue(codes[1], kLine, table, rowIdx);
                if (v0 < v1) {
                    return true;
                }
                return false;
            }
            else if (code.contains("!=")) {
                String[] codes = code.split("!=");
                double v0 = getValue(codes[0], kLine, table, rowIdx);
                double v1 = getValue(codes[1], kLine, table, rowIdx);
                if (v0 != v1) {
                    return true;
                }
                return false;
            }
            else if (code.contains("==")) {
                String[] codes = code.split("==");
                String v0 = getStringValue(codes[0], kLine, table, rowIdx).trim();
                String v1 = getStringValue(codes[1], kLine, table, rowIdx).trim();
                if (StrUtil.equalsIgnoreCase(v0, v1)) {
                    return true;
                }
                return false;
            }
            else if (code.contains("=")) {
                String[] codes = code.split("=");
                double v0 = getValue(codes[0], kLine, table, rowIdx);
                double v1 = getValue(codes[1], kLine, table, rowIdx);
                if (v0 == v1) {
                    return true;
                }
                return false;
            }

        }
        return false;
    }

    public boolean containsSign(String s) {
        int a1 = s.indexOf("+");
        int a2 = s.indexOf("-");
        int a3 = s.indexOf("*");
        int a4 = s.indexOf("/");
        int a5 = s.indexOf("(");
        if (a1 == -1 && a2 == -1 && a3 == -1 && a4 == -1) {
            return false;
        }
        return true;
    }

    public void parse() {
        if (code.contains("||")) {
            String[] codes = code.split("||");
            for (String item : codes) {
                Command command = new Command();
                command.code = item.trim();
                command.parse();
                commands.add(command);
            }
            isOr = true;
        } else if (code.contains("&&")) {
            String[] codes = code.split("&&");
            for (String item : codes) {
                Command command = new Command();
                command.code = item.trim();
                command.parse();
                commands.add(command);
            }
        } else if (code.contains("&")) {
            String[] codes = code.split("&");
            for (String item : codes) {
                Command command = new Command();
                command.code = item.trim();
                command.parse();
                commands.add(command);
            }
        }
//        else if (containsSign(code)) {
//            code = code.trim();
//            code = code.substring(1, code.length()-1);
//            String[] codes = code.split("&");
//            CalCommand command = new CalCommand();
//            command.code =code;
//            commands.add(command);
//        } else {
//
//        }
    }
}
