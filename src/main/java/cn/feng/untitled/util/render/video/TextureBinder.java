package cn.feng.untitled.util.render.video;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * 异步纹理绑定
 *
 * @version 1.1.0
 *
 * @author LingYuWeiGuang
 * @author HyperTap
 * @author ChengFeng
 */
public class TextureBinder {
    private int width;
    private int height;
    private final int textureID;
    private ByteBuffer buffer;

    public TextureBinder() {
        // 使用唯一的TextureID
        textureID = glGenTextures();
    }

    /**
     * 设定纹理数据、宽度、高度
     *
     * @param buffer 纹理数据
     * @param width 纹理宽度
     * @param height 纹理高度
     */
    public void setTexture(ByteBuffer buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    /**
     * 绑定纹理
     */
    public void bindTexture() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.width, this.height, 0, GL_RGB, GL_UNSIGNED_BYTE, this.buffer);
    }
}
