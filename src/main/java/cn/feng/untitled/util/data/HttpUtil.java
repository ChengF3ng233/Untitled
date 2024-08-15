package cn.feng.untitled.util.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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
