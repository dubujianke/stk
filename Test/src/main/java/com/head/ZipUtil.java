package com.head;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    public static void prs(String src, String dstFile) {
        File zipFile = new File(dstFile);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            File file = new File(src);
            addFileToZip(file, "0.jpg", zos);
            zos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void prss(List<File> list, String dstFile) {
        File zipFile = new File(dstFile);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            addFileToZip(list.get(0), "0.jpg", zos);
            addFileToZip(list.get(1), "1.jpg", zos);
            addFileToZip(list.get(2), "2.jpg", zos);
            zos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFileToZip(File file, String fileName, ZipOutputStream zos) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
        }
    }
}