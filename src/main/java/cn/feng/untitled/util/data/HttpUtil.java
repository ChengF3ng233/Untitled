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

    /**
     * 下载网络文件到本地路径
     *
     * @param fileURL      要下载的文件的URL
     * @param saveFilePath 保存文件的本地路径
     * @throws IOException 如果发生I/O错误
     */
    public static void downloadFile(String fileURL, String saveFilePath) {
        // 创建URL对象
        URL url;
        try {
            url = new URL(fileURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // 打开输入流并将数据缓冲
        try (InputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(saveFilePath)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            // 读取和写入数据
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage downloadImage(String imageUrl, int width, int height) {
        return downloadImage(imageUrl, width, height, 0);
    }

    private static BufferedImage downloadImage(String imageUrl, int width, int height, int count) {
        System.out.println(imageUrl);
        try {
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                if (image == null) {
                    return ImageUtil.resizeImage(ImageIO.read(new URL(MusicAPI.user.getAvatarUrl()).openStream()), width, height);
                }
                return ImageUtil.resizeImage(image, width, height);
            }
        } catch (OutOfMemoryError ee) {
            Logger.error("内存爆炸了！重新下载");
            System.gc();
            return downloadImage(imageUrl, width, height, count + 1);
        } catch (IOException e) {
            if (count >= 4) {
                throw new NullPointerException("试了五遍都不行，哥们你网是不是断了");
            }
            Logger.error("下载失败，重试...");
            return downloadImage(imageUrl, width, height, count + 1);
        }
    }
}
