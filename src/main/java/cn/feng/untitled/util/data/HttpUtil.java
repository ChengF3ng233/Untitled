package cn.feng.untitled.util.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class HttpUtil {
    public static BufferedImage downloadImage(String imageUrl, int width, int height) {
        try {
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                return ImageUtil.resizeImage(image, width, height);
            }
        } catch (OutOfMemoryError ee) {
            System.gc();
            return downloadImage(imageUrl, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
