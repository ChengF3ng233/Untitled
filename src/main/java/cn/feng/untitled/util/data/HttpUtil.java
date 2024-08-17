package cn.feng.untitled.util.data;

import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.util.misc.Logger;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class HttpUtil {
    public static BufferedImage downloadImage(String imageUrl) {
        return downloadImage(imageUrl, 0);
    }

    public static void downloadImage(String imageURL, File file, boolean rewrite) throws IOException {
        if (file.exists() && !rewrite) return;

        URL url = new URL(imageURL);
        File tempFile = File.createTempFile("tempImage", ".tmp");

        try (InputStream inputStream = url.openStream()) {
            // 将图像下载到临时文件
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (isSupportedImageFormat(tempFile)) {
                // 如果是 JPG 文件，将临时文件重命名为目标文件
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Logger.info("Downloaded image: " + url + " to " + file.getName());
            } else {
                // 使用 FFmpeg 转换图像
                if (file.exists()) file.delete();
                FFmpeg.atPath(new File("../ffmpeg/bin").toPath())
                        .addInput(UrlInput.fromPath(tempFile.toPath()))
                        .addOutput(UrlOutput.toPath(file.toPath()))
                        .addArguments("-f", "image2")
                        .execute();

                Logger.info("Converted image: " + url + " to " + file.getName());
            }
        } catch (IOException e) {
            Logger.error("IO Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 清理临时文件
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public static boolean isSupportedImageFormat(File file) throws IOException {
        // 尝试读取图像
        BufferedImage image = ImageIO.read(file);
        if (image != null) {
            return true; // 如果能够读取图像，说明是支持的格式
        }

        // 如果无法读取，尝试检查支持的格式
        try (ImageInputStream iis = ImageIO.createImageInputStream(file)) {
            if (iis == null) {
                return false; // 如果无法创建 ImageInputStream，文件可能不是图像格式
            }

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            return readers.hasNext(); // 如果有 ImageReader，说明文件是支持的格式
        }
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
