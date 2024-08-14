package cn.feng.untitled.util.data;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class ImageUtil {
    public static BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
        // 创建一个新的缓冲图像
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        // 获取 Graphics2D 对象
        Graphics2D g2d = resizedImage.createGraphics();

        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 绘制缩小后的图像
        g2d.drawImage(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }
}
