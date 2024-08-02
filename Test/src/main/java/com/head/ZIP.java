package com.head;

import com.alading.util.FileUtil;
import com.huaien.core.util.FileManager;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZIP {
    public static void unzip(String zipFilePath, String destDir) throws IOException {
        FileInputStream fis = new FileInputStream(zipFilePath);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();

        byte[] buffer = new byte[1024];
        int length;

        while (ze != null) {
            String fileName = ze.getName();
            String name = ze.getName();
            if (name.contains("1_neutral/")) {
                if (name.endsWith("1_neutral/")) {
                    System.out.println(ze.getName());
                    String dir = "F:/heads/" + name;
                    FileManager.mkdirs(dir);
                }
                if (name.endsWith(".jpg")) {
                    File f = new File("F:/heads/" + name);
                    if(!f.exists()) {
                        FileOutputStream fos = new FileOutputStream("F:/heads/" + name);
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                        fos.close();
                    }
                }
            }
            zis.closeEntry();
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        fis.close();
    }

    public static void main(String[] args) {
        try {
            int idx = 300;
            while (idx <= 340) {
                int idx1 = idx + 1;
                int idx2 = idx1 + 20 - 1;
                String idx1Str = idx1 < 100 ? "0" + idx1 : "" + idx1;
                String idx2Str = idx2 < 100 ? "0" + idx2 : "" + idx2;
                unzip("G:\\fsmview_trainset_images_" + idx1Str + "-" + idx2Str + ".zip", "F:/heads");
                idx+=20;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}