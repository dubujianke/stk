package com.head;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtil {
    public final static void scaleCut(String srcImageFile, String result, int x, int y, int w, int h) {
        BufferedImage src = scale(srcImageFile, result);
        cut(src, result, 0, 0, 64, 64);
    }

    public final static BufferedImage scale(String srcImageFile, int dstWidth) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            if(src.getWidth()<dstWidth) {
                return src;
            }
            int dstHeight = (int) (1.0 * dstWidth / src.getWidth() * src.getHeight());
            Image scaleImage = src.getScaledInstance(dstWidth, dstHeight, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(dstWidth, dstHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(scaleImage, 0, 0, null);
            g.dispose();
            return tag;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static BufferedImage scale(String srcImageFile, String result) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            int dstWidth = 64;
            int dstHeight = 64;//(int) (1.0 * 64 / width * height);
            Image scaleImage = src.getScaledInstance(dstWidth, dstHeight, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(dstWidth, dstHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(scaleImage, 0, 0, null);
            g.dispose();
            return tag;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static void write(BufferedImage src, String result) {
        try {
            ImageIO.write(src, "JPG", new File(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static void cut(BufferedImage src, String result, int x, int y, int w, int h) {
        try {
            BufferedImage bufferedImage = src.getSubimage(x, y, w, h);
            File imgCutFile = new File(result);
            ImageIO.write(bufferedImage, "JPEG", imgCutFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
