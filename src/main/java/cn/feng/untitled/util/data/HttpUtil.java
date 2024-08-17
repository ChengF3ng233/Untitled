package cn.feng.untitled.util.data;

import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.util.misc.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
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

    public static void downloadFile(String fileURL, File file, boolean rewrite) throws IOException {
        if (file.exists() && !rewrite) return;
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // 检查响应码
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = httpConn.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else {
            System.out.println("没有文件可下载：" + fileURL);
        }

        httpConn.disconnect();
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
