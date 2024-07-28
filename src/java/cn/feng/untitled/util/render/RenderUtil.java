package cn.feng.untitled.util.render;

import net.minecraft.client.renderer.GlStateManager;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class RenderUtil {
    /**
     * This will set the alpha limit to a specified value ranging from 0-1
     * @param limit minimal alpha value
     */
    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    /**
        Sometimes colors get messed up in for loops, so we use this method to reset it to allow new colors to be used
     */
    public static void resetColor() {
        GlStateManager.color(1, 1, 1, 1);
    }

    /**
     * Scales the data that you put in the runnable
     * @param x start x pos
     * @param y start y pos
     * @param scale scale
     */
    public static void scaleStart(float x, float y, float scale) {
        glPushMatrix();
        glTranslatef(x, y, 0);
        glScalef(scale, scale, 1);
        glTranslatef(-x, -y, 0);
    }

    /**
     * End scale
     */
    public static void scaleEnd() {
        glPopMatrix();
    }
}
