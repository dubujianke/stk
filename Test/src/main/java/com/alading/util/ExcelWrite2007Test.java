package com.alading.util;


import com.alading.model.Col;
import com.alading.model.Table;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelWrite2007Test {
    public static String PATH = "D:\\stock\\Test\\res\\bottom\\";


    public static Table read(String path) {
        XSSFWorkbook xssfWorkbook = null;
        try {
            Table table = new Table();
            xssfWorkbook = new XSSFWorkbook(new FileInputStream(path));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            int maxRow = sheet.getLastRowNum();
            for (int row = 0; row <= maxRow; row++) {
                com.alading.model.Row row1 = new com.alading.model.Row();
                if (row == 90) {
                    int a = 0;
                }
                if (sheet.getRow(row) == null) {
                    table.add(row1);
                    continue;
                }
                int maxRol = sheet.getRow(row).getLastCellNum();
                for (int rol = 0; rol < maxRol; rol++) {
                    Cell cell = sheet.getRow(row).getCell(rol);
//                    Color color = cell.getCellStyle().getFillBackgroundColorColor();
//                    if(color!=null) {
//                        String str = color.toString();
//                    }
                    Col col = new Col("" + cell);
                    row1.add(col);
//                    System.out.print(sheet.getRow(row).getCell(rol) + "  ");
                }
                table.add(row1);
//                //System.out.println();
            }
            return table;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                xssfWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void mainNoSort(Table table, Table.Filter filter, String dstFile, boolean fileFlag) throws Exception {
        long begin = System.currentTimeMillis();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int realIdx = 0;

        CellStyle boldStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        boldStyle.setFont(font);

        for (int rowNumber = 0; rowNumber < table.getRowLen(); rowNumber++) {
            boolean ffilter = filter.filter(rowNumber, table.rows.get(rowNumber));
            if (!ffilter) {
                continue;
            }

            Row row = sheet.createRow(realIdx++);
            for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                String v = table.getCell(rowNumber, cellNumber);
                if (v.equalsIgnoreCase("Infinity")) {
                    v = "10000";
                }
                if (v.equalsIgnoreCase("")) {
                    v = "10000";
                }
                if (v.equalsIgnoreCase("true")) {
                    v = "1";
                }
                if (v.equalsIgnoreCase("false")) {
                    v = "0";
                }
                cell.setCellValue(v);
                if (table.isBold(rowNumber, cellNumber)) {
                    cell.setCellStyle(boldStyle);
                }
            }
        }
        for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
            sheet.autoSizeColumn(cellNumber);//对合并表头不生效
//            sheet.autoSizeColumn(cellNumber, true);//对合并表头生效
        }


        if (!fileFlag) {
            return;
        }
        File file = new File(PATH + dstFile + ".xlsx");
        if (file.exists()) {
            file.renameTo(new File(PATH + dstFile + "_" + System.currentTimeMillis() + ".xlsx"));
        }
        file = new File(PATH + dstFile + ".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        //System.out.println((double) (end - begin) / 1000);//5.003s
    }

    public static void mainNoSortABS(Table table, Table.Filter filter, String dstFile, boolean fileFlag) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int realIdx = 0;

        CellStyle boldStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        boldStyle.setFont(font);

        for (int rowNumber = 0; rowNumber < table.getRowLen(); rowNumber++) {
            boolean ffilter = filter.filter(rowNumber, table.rows.get(rowNumber));
            if (!ffilter) {
                continue;
            }

            Row row = sheet.createRow(realIdx++);
            for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                String v = table.getCell(rowNumber, cellNumber);
                if (v.equalsIgnoreCase("Infinity")) {
                    v = "10000";
                }
                if (v.equalsIgnoreCase("")) {
                    v = "10000";
                }
                if (v.equalsIgnoreCase("true")) {
                    v = "1";
                }
                if (v.equalsIgnoreCase("false")) {
                    v = "0";
                }
                cell.setCellValue(v);
                if (table.isBold(rowNumber, cellNumber)) {
                    cell.setCellStyle(boldStyle);
                }
            }
        }
        for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
            sheet.autoSizeColumn(cellNumber);//对合并表头不生效
//            sheet.autoSizeColumn(cellNumber, true);//对合并表头生效
        }


        if (!fileFlag) {
            return;
        }
        File file = new File(dstFile + ".xlsx");
        if (file.exists()) {
            file.renameTo(new File(dstFile + "_" + System.currentTimeMillis() + ".xlsx"));
        }
        file = new File(dstFile + ".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    public static void mainNoSortPATH(Table table, Table.Filter filter, String PATH, String dstFile, boolean fileFlag) throws Exception {
        long begin = System.currentTimeMillis();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int realIdx = 0;

        CellStyle boldStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        boldStyle.setFont(font);

        for (int rowNumber = 0; rowNumber < table.getRowLen(); rowNumber++) {
            boolean ffilter = filter.filter(rowNumber, table.rows.get(rowNumber));
            if (!ffilter) {
                continue;
            }

            Row row = sheet.createRow(realIdx++);
            for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                String v = table.getCell(rowNumber, cellNumber);
                if (v.equalsIgnoreCase("Infinity")) {
                    v = "10000";
                }
                if (v.equalsIgnoreCase("")) {
                    v = "10000";
                }
                if (v.equalsIgnoreCase("true")) {
                    v = "1";
                }
                if (v.equalsIgnoreCase("false")) {
                    v = "0";
                }
                cell.setCellValue(v);
                if (table.isBold(rowNumber, cellNumber)) {
                    cell.setCellStyle(boldStyle);
                }
            }
        }
        for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
            sheet.autoSizeColumn(cellNumber);//对合并表头不生效
//            sheet.autoSizeColumn(cellNumber, true);//对合并表头生效
        }


        if (!fileFlag) {
            return;
        }
        File file = new File(PATH + dstFile + ".xlsx");
        if (file.exists()) {
            file.renameTo(new File(PATH + dstFile + "_" + System.currentTimeMillis() + ".xlsx"));
        }
        file = new File(PATH + dstFile + ".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        //System.out.println((double) (end - begin) / 1000);//5.003s

    }

    public static void main(Table table, Table.Filter filter, String dstFile, boolean fileFlag) throws Exception {
        table.sort();
        long begin = System.currentTimeMillis();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int realIdx = 0;

        CellStyle boldStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        boldStyle.setFont(font);

        CellStyle iboldStyle = workbook.createCellStyle();
        Font font2 = workbook.createFont();
        font2.setBold(true);
        font2.setItalic(true);
        iboldStyle.setFont(font2);
        iboldStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        iboldStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle icolorGreenStyle = workbook.createCellStyle();
        icolorGreenStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

        CellStyle icolorBlueStyle = workbook.createCellStyle();
        icolorBlueStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());

        CellStyle icolorYellowStyle = workbook.createCellStyle();
        icolorYellowStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());

        CellStyle icolorOrangeStyle = workbook.createCellStyle();
        icolorOrangeStyle.setFillBackgroundColor(IndexedColors.ORANGE.getIndex());

        Map<String, CellStyle> styleMap = new HashMap<>();
        styleMap.put("" + IndexedColors.GREEN.getIndex(), icolorGreenStyle);
        styleMap.put("" + IndexedColors.BLUE.getIndex(), icolorBlueStyle);
        styleMap.put("" + IndexedColors.YELLOW.getIndex(), icolorYellowStyle);
        styleMap.put("" + IndexedColors.ORANGE.getIndex(), icolorOrangeStyle);



        for (int rowNumber = 0; rowNumber < table.getRowLen(); rowNumber++) {
            boolean ffilter = filter.filter(rowNumber, table.rows.get(rowNumber));
            if (!ffilter) {
                continue;
            }
            Row row = sheet.createRow(realIdx++);
            com.alading.model.Row rowR = table.rows.get(rowNumber);
            for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                String v = table.getCell(rowNumber, cellNumber);
                cell.setCellValue(v);
                if (table.isBold(rowNumber, cellNumber)) {
                    cell.setCellStyle(boldStyle);
                }
                if (table.isItalic(rowNumber, cellNumber)) {
                    cell.setCellStyle(iboldStyle);
                }
                boolean catchF = false;
//                if(rowNumber>0) {
//                    if (rowR.getFloat(table.getColumn("上上月z幅")) > 15) {
//                        row.getCell(table.getColumn("上上月z幅")).setCellStyle(iboldStyle);
//                        catchF = true;
//                    }
//                    if (rowR.getFloat(table.getColumn("加速天量")) > 0) {
//                        row.getCell(table.getColumn("加速天量")).setCellStyle(iboldStyle);
//                        catchF = true;
//                    }
//                    if (rowR.getFloat(table.getColumn("减速天量")) > 0) {
//                        row.getCell(table.getColumn("减速天量")).setCellStyle(iboldStyle);
//                        catchF = true;
//                    }
//                    if (rowR.getFloat(table.getColumn("fistMinute")) > 1.1 || rowR.getFloat(table.getColumn("fistMinute")) < -0.7) {
//                        row.getCell(table.getColumn("fistMinute")).setCellStyle(iboldStyle);
//                        catchF = true;
//                    }
//                }
                if (catchF) {
                    row.getCell(table.getColumn("code")).setCellStyle(iboldStyle);
                }
            }
        }
        for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
            sheet.autoSizeColumn(cellNumber);//对合并表头不生效
        }
        if (!fileFlag) {
            return;
        }
        File file = new File(PATH + dstFile + ".xlsx");
        if (file.exists()) {
            file.renameTo(new File(PATH + dstFile + "_" + System.currentTimeMillis() + ".xlsx"));
        }
        file = new File(PATH + dstFile + ".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    public static void main(Table table, String dstFile) throws Exception {
        long begin = System.currentTimeMillis();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (int rowNumber = 0; rowNumber < table.getRowLen(); rowNumber++) {
            Row row = sheet.createRow(rowNumber);
            for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                String v = table.getCell(rowNumber, cellNumber);
                cell.setCellValue(v);
            }
        }
        File file = new File(PATH + dstFile + ".xlsx");
        if (file.exists()) {
            file.renameTo(new File(PATH + dstFile + "_" + System.currentTimeMillis() + ".xlsx"));
        }
        file = new File(PATH + dstFile + ".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        //System.out.println((double) (end - begin) / 1000);//5.003s
    }

    public static String getV(String v) {
        if (v.equalsIgnoreCase("Infinity")) {
            v = "10000";
        }
        if (v.equalsIgnoreCase("")) {
            v = "10000";
        }
        if (v.equalsIgnoreCase("true")) {
            v = "1";
        }
        if (v.equalsIgnoreCase("false")) {
            v = "0";
        }
        return v;
    }

    public static void mainABS(Table table, String dstFile) throws Exception {
        long begin = System.currentTimeMillis();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        CellStyle icolorGreenStyle = workbook.createCellStyle();
        icolorGreenStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

        CellStyle icolorBlueStyle = workbook.createCellStyle();
        icolorBlueStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());

        CellStyle icolorYellowStyle = workbook.createCellStyle();
        icolorYellowStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());

        CellStyle icolorOrangeStyle = workbook.createCellStyle();
        icolorOrangeStyle.setFillBackgroundColor(IndexedColors.ORANGE.getIndex());

        Map<String, CellStyle> styleMap = new HashMap<>();
        styleMap.put("" + IndexedColors.GREEN.getIndex(), icolorGreenStyle);
        styleMap.put("" + IndexedColors.BLUE.getIndex(), icolorBlueStyle);
        styleMap.put("" + IndexedColors.YELLOW.getIndex(), icolorYellowStyle);
        styleMap.put("" + IndexedColors.ORANGE.getIndex(), icolorOrangeStyle);

        CellStyle iboldStyle = workbook.createCellStyle();
        Font font2 = workbook.createFont();
        font2.setBold(true);
        font2.setItalic(true);
        font2.setColor(IndexedColors.GREEN.getIndex());
        iboldStyle.setFont(font2);
        iboldStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        iboldStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        for (int rowNumber = 0; rowNumber < table.getRowLen(); rowNumber++) {
            com.alading.model.Row rowR = table.rows.get(rowNumber);
            Row row = sheet.createRow(rowNumber);
            for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                String v = table.getCell(rowNumber, cellNumber);
                v = getV(v);
                cell.setCellValue(v);
                cell.setCellStyle(icolorGreenStyle);
            }
            boolean catchF = false;
            if(rowNumber>0) {
                if (rowR.getFloat(table.getColumn("上上月z幅")) > 15) {
                    row.getCell(table.getColumn("上上月z幅")).setCellStyle(iboldStyle);
                    catchF = true;
                }
                if (rowR.getFloat(table.getColumn("加速天量")) > 0) {
                    row.getCell(table.getColumn("加速天量")).setCellStyle(iboldStyle);
                    catchF = true;
                }
                if (rowR.getFloat(table.getColumn("减速天量")) > 0) {
                    row.getCell(table.getColumn("减速天量")).setCellStyle(iboldStyle);
                    catchF = true;
                }

                if (rowR.getFloat(table.getColumn("fistMinute")) > 1 || rowR.getFloat(table.getColumn("fistMinute")) < -0.7) {
                    if(rowR.getFloat(table.getColumn("fistMinute")) < 100) {
                        row.getCell(table.getColumn("fistMinute")).setCellStyle(iboldStyle);
                        catchF = true;
                    }

                }
            }
            if (catchF) {
                row.getCell(table.getColumn("code")).setCellStyle(iboldStyle);
            }
        }
        File file = new File(dstFile);
        if (file.exists()) {
            file.delete();
        }
        file = new File(dstFile);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        long end = System.currentTimeMillis();
    }

    public static void mainABS(Table table, Table table2, String dstFile) throws Exception {
        long begin = System.currentTimeMillis();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (int rowNumber = 0; rowNumber < table.getRowLen(); rowNumber++) {
            Row row = sheet.createRow(rowNumber);
            for (int cellNumber = 0; cellNumber < table.getRow(0).getColLen(); cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                String v = table.getCell(rowNumber, cellNumber);
                v = getV(v);
                cell.setCellValue(v);
            }
        }

        Sheet sheet2 = workbook.createSheet();
        for (int rowNumber = 0; rowNumber < table2.getRowLen(); rowNumber++) {
            Row row = sheet2.createRow(rowNumber);
            for (int cellNumber = 0; cellNumber < table2.getRow(0).getColLen(); cellNumber++) {
                Cell cell = row.createCell(cellNumber);
                String v = table2.getCell(rowNumber, cellNumber);
                v = getV(v);
                cell.setCellValue(v);
            }
        }

        File file = new File(dstFile);
        if (file.exists()) {
            file.delete();
//            file.renameTo(new File(dstFile.replace(".xlsx", "") + "_" + System.currentTimeMillis() + "_.xlsx"));
        }
        file = new File(dstFile);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        //System.out.println((double) (end - begin) / 1000);//5.003s
    }

}
