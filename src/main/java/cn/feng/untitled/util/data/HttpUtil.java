package cn.feng.untitled.util.data;

import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.util.misc.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class HttpUtil {
    public static BufferedImage downloadImage(String imageUrl) {
        return downloadImage(imageUrl, 0);
    }

    private static BufferedImage downloadImage(String imageUrl, int count) {
        try {
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                return image == null? ImageIO.read(new URL(MusicAPI.user.getAvatarUrl())) : image;
            }
        } catch (IOException e) {
            if (count >= 4) {
                throw new NullPointerException("试了五遍都不行，哥们你网是不是断了");
            }
            Logger.error("下载失败，重试...");
            return downloadImage(imageUrl, count + 1);
        }
    }
}
