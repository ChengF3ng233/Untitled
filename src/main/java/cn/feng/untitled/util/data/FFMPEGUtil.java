package cn.feng.untitled.util.data;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/8/22
 **/
public class FFMPEGUtil {
    public static void convertMusic(String songURL, File outputFile) {
        try (
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(songURL);
                FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 2)
        ) {
            // 配置输入
            grabber.setAudioChannels(2);
            grabber.setSampleRate(44100);
            grabber.start();

            // 配置输出
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_PCM_S16LE);
            recorder.setAudioChannels(2);
            recorder.setSampleRate(44100);
            recorder.start();

            // 读取和写入音频帧
            Frame frame;
            while ((frame = grabber.grab()) != null) {
                recorder.record(frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convertImage(File inputFile, File outputFile) {
        try {
            // 使用 OpenCV 的 imread 方法直接读取图像
            Mat mat = opencv_imgcodecs.imread(inputFile.getAbsolutePath());

            if (mat.empty()) {
                throw new IOException("Failed to load image: " + inputFile.getAbsolutePath());
            }

            // 将 Mat 转换为 BufferedImage
            BufferedImage bufferedImage = matToBufferedImage(mat);

            // 将 BufferedImage 写出为 JPEG 文件
            ImageIO.write(bufferedImage, "jpg", outputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.data().get(buffer);

        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);

        return image;
    }
}
