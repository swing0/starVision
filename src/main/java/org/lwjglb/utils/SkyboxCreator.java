package org.lwjglb.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * 将从Stellarium生成的6张图片转为需要的天空盒图片格式
 * 参考 https://zhuanlan.zhihu.com/p/377263547
 * 如果是白天，开启太阳系的天体，再手动把太阳月亮p掉
 */
public class SkyboxCreator {

    private static final int CROPPED_SIZE = 1080;//2k屏幕1361；1080p屏幕1080

    //north==front; east==left; west==right; south==back
    public static void main(String[] args) {
        try {
            createSkybox(
                    "src/main/java/org/lwjglb/utils/images/beijingDay/Unity2-east.png",
                    "src/main/java/org/lwjglb/utils/images/beijingDay/Unity3-south.png",
                    "src/main/java/org/lwjglb/utils/images/beijingDay/Unity4-west.png",
                    "src/main/java/org/lwjglb/utils/images/beijingDay/Unity1-north.png",
                    "src/main/java/org/lwjglb/utils/images/beijingDay/Unity5-top.png",
                    "src/main/java/org/lwjglb/utils/images/beijingDay/Unity6-bottom.png",
                    "resources/models/skybox/skybox0.png"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createSkybox(String leftPath, String backPath, String rightPath, String frontPath, String topPath, String bottomPath, String outputPath) throws Exception {
        // 创建一个大的BufferedImage来存储天空盒的纹理
        BufferedImage skybox = new BufferedImage(CROPPED_SIZE * 3, CROPPED_SIZE * 2, BufferedImage.TYPE_INT_ARGB);

        // 读取并裁剪6张图像
        BufferedImage left = cropCenter(ImageIO.read(new File(leftPath)), CROPPED_SIZE, CROPPED_SIZE);
        BufferedImage back = cropCenter(ImageIO.read(new File(backPath)), CROPPED_SIZE, CROPPED_SIZE);
        BufferedImage right = cropCenter(ImageIO.read(new File(rightPath)), CROPPED_SIZE, CROPPED_SIZE);
        BufferedImage front = cropCenter(ImageIO.read(new File(frontPath)), CROPPED_SIZE, CROPPED_SIZE);
        BufferedImage top = cropCenter(ImageIO.read(new File(topPath)), CROPPED_SIZE, CROPPED_SIZE);
        BufferedImage bottom = rotateImage(cropCenter(ImageIO.read(new File(bottomPath)), CROPPED_SIZE, CROPPED_SIZE), 90);

        // 将6张图像放置到大图像的相应位置
        // Left
        skybox.getGraphics().drawImage(left, 0, 0, null);
        // Back
        skybox.getGraphics().drawImage(back, CROPPED_SIZE, 0, null);
        // Right
        skybox.getGraphics().drawImage(right, CROPPED_SIZE * 2, 0, null);
        // Front
        skybox.getGraphics().drawImage(front, 0, CROPPED_SIZE, null);
        // Top
        skybox.getGraphics().drawImage(top, CROPPED_SIZE, CROPPED_SIZE, null);
        // Bottom
        skybox.getGraphics().drawImage(bottom, CROPPED_SIZE * 2, CROPPED_SIZE, null);

        // 保存大图像到文件
        ImageIO.write(skybox, "png", new File(outputPath));
    }

    private static BufferedImage cropCenter(BufferedImage src, int width, int height) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int x = (srcWidth - width) / 2;
        int y = (srcHeight - height) / 2;

        return src.getSubimage(x, y, width, height);
    }

    private static BufferedImage rotateImage(BufferedImage img, int angle) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(h, w, img.getType());

        Graphics2D g = dimg.createGraphics();
        g.translate((h - w) / 2, (h - w) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.translate(-(h - w) / 2, -(h - w) / 2);
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return dimg;
    }
}
