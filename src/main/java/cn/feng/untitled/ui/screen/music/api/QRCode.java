package cn.feng.untitled.ui.screen.music.api;

import java.awt.image.BufferedImage;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class QRCode {
    private final BufferedImage image;
    private final String key;

    public QRCode(BufferedImage image, String key) {
        this.image = image;
        this.key = key;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }
}
