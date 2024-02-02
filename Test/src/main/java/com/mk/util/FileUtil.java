package com.mk.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void write(String path, final String serial) {
        try {
            FileUtils.write(new File(path), serial, "utf8", false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAppend(String path, final String serial) {
        try {
            FileUtils.write(new File(path), serial, "utf8", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String read(String path) {
        try {
            return FileUtils.readFileToString(new File(path), "utf8");
        } catch (IOException e) {
        }
        return "";
    }

    public static List<String> readLines(String path) {
        try {
            return FileUtils.readLines(new File(path), "utf8");
        } catch (IOException e) {
        }
        return new ArrayList<>();
    }
}
