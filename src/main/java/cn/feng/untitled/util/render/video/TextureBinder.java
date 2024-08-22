package cn.feng.untitled.util.render.video;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * Texture-binder in LWJGL.
 *
 * @version 1.0.0
 *
 * @author LingYuWeiGuang
 * @author HyperTap
 */
public class TextureBinder {
    private int imageWidth, imageHeight, internalformat, textureID;
    private ByteBuffer imageBuffer;

    /**
     * Set image.
     *
     * @param buffer image object
     * @param width image width
     * @param height image height
     */
    public void setBuffer(ByteBuffer buffer, int width, int height) {
        this.setBuffer(buffer, width, height, GL_RGB);
    }

    /**
     * Set image.
     *
     * @param buffer image object
     * @param width image width
     * @param height image height
     * @param internalformat image color format
     */
    public void setBuffer(ByteBuffer buffer, int width, int height, int internalformat) {
        this.internalformat = internalformat;
        this.imageWidth = width;
        this.imageHeight = height;
        this.imageBuffer = buffer;
    }

    /**
     * Binding texture form buffered images.
     */
    public void bindTexture() {
        glDeleteTextures(this.textureID);

        this.textureID = glGenTextures();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, this.internalformat, this.imageWidth, this.imageHeight, 0, this.internalformat, GL_UNSIGNED_BYTE, this.imageBuffer);
    }
}
