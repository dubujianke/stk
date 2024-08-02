package com.head;

import com.huaien.core.util.FileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageDir2 {

    public static void moveFile(Path source, Path target) throws IOException {
        Files.createDirectories(target.getParent());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        Files.delete(source);
    }

    public static void main(String[] args) {
        String img = "F:\\heads\\Normal";
        File tmp = new File(img);
        int idx = 0;
        for (File file : tmp.listFiles()) {
            System.out.println(file);
            idx++;
            String dir = "F:\\heads\\imgs2" + "\\" + idx;
            FileManager.mkdirs(dir);
            try {
                Image src = javax.imageio.ImageIO.read(file);
                String result = dir + "\\" + idx + ".jpg";
                File imgCutFile = new File(result);
                int width = src.getWidth(null);
                int height = src.getHeight(null);
                BufferedImage thumb = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                thumb.getGraphics().drawImage(src, 0, 0, width, height, null);
                javax.imageio.ImageIO.write(thumb, "jpg", imgCutFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
