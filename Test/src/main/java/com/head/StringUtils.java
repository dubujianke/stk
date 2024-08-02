package com.head;

import java.io.*;

public class StringUtils {

    public static File getFrontFileName(File file) {
        for (File afile : file.listFiles()) {
            String name = afile.getName();
            if(name.contains("PM+00")) {
                return afile;
            }
        }
        return null;
    }
    public static File getRightFileName(File file) {
        for (File afile : file.listFiles()) {
            String name = afile.getName();
            if(name.contains("PM+45")) {
                return afile;
            }
        }
        return null;
    }
    public static File getLeftFileName(File file) {
        for (File afile : file.listFiles()) {
            String name = afile.getName();
            if(name.contains("PM-45")) {
                return afile;
            }
        }
        return null;
    }



    public static String getFileName(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf("."));
        return name;
    }

    public static String getFileExtName(File file) {
        String name = file.getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        return name;
    }

    public static String getParentFileExtName(File file) {
        String name = file.getParentFile().getName();
        return name;
    }

}
