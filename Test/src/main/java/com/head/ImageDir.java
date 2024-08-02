package com.head;

import com.huaien.core.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageDir {

    public static void moveFile(Path source, Path target) throws IOException {
        Files.createDirectories(target.getParent());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        Files.delete(source);
    }

    public static void main(String[] args) {
        String img = "F:\\heads\\imgs";
        File tmp = new File(img);
        int idx = 0;
        for(File file:tmp.listFiles()) {
            System.out.println(file);
            idx++;
            String dir = tmp.getAbsolutePath()+"\\"+idx;
            FileManager.mkdirs(dir);
            try {
                Path sourceFile = Paths.get(file.getPath());
                Path targetDir = Paths.get(dir);
                moveFile(sourceFile, targetDir.resolve(sourceFile.getFileName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
