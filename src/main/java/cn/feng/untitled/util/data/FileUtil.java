package cn.feng.untitled.util.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FileUtil {

    public static String readFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void writeFile(File file, String content) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static ByteBuffer toByteBuffer(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            // 获取图像的宽度和高度
            int width = image.getWidth();
            int height = image.getHeight();

            // 获取图像的像素数据
            DataBufferByte dataBufferByte = (DataBufferByte) image.getRaster().getDataBuffer();
            byte[] data = dataBufferByte.getData();

            // 创建一个 ByteBuffer 用于存储图像数据
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(width * height * 4);
            byteBuffer.order(ByteOrder.nativeOrder()); // 使用本地字节顺序
            byteBuffer.put(data);
            byteBuffer.flip();

            return byteBuffer;
        } catch (IOException e) {
            return null;
        }
    }
}
