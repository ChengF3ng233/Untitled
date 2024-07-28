package cn.feng.untitled.util.render;

import cn.feng.untitled.util.MinecraftInstance;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class RenderUtil extends MinecraftInstance {
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

    /**
     * GL Scissor
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     */
    public static void scissorStart(double x, double y, double width, double height) {
        GL11.glPushMatrix();
        glEnable(GL_SCISSOR_TEST);
        ScaledResolution sr = new ScaledResolution(mc);
        final double scale = sr.getScaleFactor();
        double finalHeight = height * scale;
        double finalY = (sr.getScaledHeight() - y) * scale;
        double finalX = x * scale;
        double finalWidth = width * scale;
        glScissor((int) finalX, (int) (finalY - finalHeight), (int) finalWidth, (int) finalHeight);
    }

    /**
     * End GL Scissor
     */
    public static void scissorEnd() {
        glDisable(GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    /**
     * Judge if cursor is hovering specific area.
     * @param mouseX mX
     * @param mouseY mY
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     * @return boolean
     */
    public static boolean hovering(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    /**
     * Judge if mouse button is down while the cursor in specific area.
     * @param mouseX mX
     * @param mouseY mY
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     * @param button mouse button
     * @return boolean
     */
    public static boolean holding(float mouseX, float mouseY, float x, float y, float width, float height, int button) {
        return Mouse.isButtonDown(button) && hovering(mouseX, mouseY, x, y, width, height);
    }

    /**
     This method colors the next avalible texture with a specified alpha value ranging from 0-1
      */
    public static void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    /**
     * Colors the next texture without a specified alpha value
     * @param color color
     */
    public static void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255.0F);
    }

}
