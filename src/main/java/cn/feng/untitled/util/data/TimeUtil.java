package cn.feng.untitled.util.data;

/**
 * @author ChengFeng
 * @since 2024/8/14
 **/
public class TimeUtil {
    public static String formatTime(double millis) {
        int totalSeconds = (int) (millis / 1000); // 将毫秒转换为总秒数
        int minutes = totalSeconds / 60;          // 计算分钟数
        int seconds = totalSeconds % 60;          // 计算剩余的秒数

        // 格式化为 "分钟:秒" 的形式，秒数如果小于10，前面补0
        return String.format("%02d:%02d", minutes, seconds);
    }
}
