package com.head;

import com.huaien.core.util.FileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageDirs {

    public static void moveFile(Path source, Path target) throws IOException {
        Files.createDirectories(target.getParent());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        Files.delete(source);
    }
    public static void trans(File tmp, int idx) {
        if(StringUtils.getFileExtName(tmp).endsWith("txt")) {
            return;
        }
        for (File file : tmp.listFiles()) {
            if(StringUtils.getFileExtName(file).endsWith("txt") || StringUtils.getFileExtName(file).endsWith("db")) {
                continue;
            }
            System.out.println(file);
            String path = "F:\\heads\\POSE2\\"+StringUtils.getParentFileExtName(file)+"\\"+StringUtils.getFileName(file)+".jpg";
            String dir =   "F:\\heads\\POSE2\\"+StringUtils.getParentFileExtName(file);
            FileManager.mkdirs(dir);
            try {
                Image src = ImageIO.read(file);
                File imgCutFile = new File(path);
                int width = src.getWidth(null);
                int height = src.getHeight(null);
                BufferedImage thumb = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                thumb.getGraphics().drawImage(src, 0, 0, width, height, null);
                ImageIO.write(thumb, "jpg", imgCutFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        String img = "F:\\heads\\POSE";
        File tmp = new File(img);
        int idx = 0;
        for (File file : tmp.listFiles()) {
            trans(file, idx);
            idx++;
        }
    }
}
