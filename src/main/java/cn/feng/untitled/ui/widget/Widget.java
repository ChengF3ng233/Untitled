package cn.feng.untitled.ui.widget;

import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.ui.font.awt.AWTFontLoader;
import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.misc.ChatUtil;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Widget extends MinecraftInstance {
    public String name;
    /**
     * x, y都使用相对位置 （百分比），防止因窗口缩放导致组件乱动
     */
    public float x, y;
    protected float renderX, renderY;
    public float width, height;
    public final boolean defaultOn;
    public boolean dragging;
    private int dragX, dragY;
    private int align;

    protected ScaledResolution sr;

    public Widget(String name, boolean defaultOn) {
        this.name = name;
        this.defaultOn = defaultOn;
        this.x = 0f;
        this.y = 0f;
        this.width = 100f;
        this.height = 100f;
        this.align = WidgetAlign.LEFT | WidgetAlign.TOP;
    }

    public Widget(String name, boolean defaultOn, int align) {
        this(name, defaultOn);
        this.align = align;
    }

    public void onShader(ShaderEvent event) {

    }

    public abstract void render();

    public void updatePos() {
        sr = new ScaledResolution(mc);

        renderX = x * sr.getScaledWidth();
        renderY = y * sr.getScaledHeight();

        if (renderX < 0f) x = 0f;
        if (renderX > sr.getScaledWidth() - width) x = (sr.getScaledWidth() - width) / sr.getScaledWidth();
        if (renderY < 0f) y = 0f;
        if (renderY > sr.getScaledHeight() - height) y = (sr.getScaledHeight() - height) / sr.getScaledHeight();

        if (align == (WidgetAlign.LEFT | WidgetAlign.TOP)) return;

        if ((align & WidgetAlign.RIGHT) != 0) {
            renderX -= width;
        } else if ((align & WidgetAlign.CENTER) != 0) {
            renderX -= width / 2f;
        }

        if ((align & WidgetAlign.BOTTOM) != 0) {
            renderY -= height;
        } else if ((align & WidgetAlign.MIDDLE) != 0) {
            renderY -= height / 2f;
        }
    }

    public final void onChatGUI(int mouseX, int mouseY, boolean drag) {
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, renderX, renderY, width, height);

        if (hovering || dragging) {
            AWTFontLoader.rubik(16).drawString(name, renderX, renderY - AWTFontLoader.rubik(16).getFontHeight() - 3f, Color.WHITE.getRGB(), true);
        }

        if (dragging) {
            RoundedUtil.drawRoundOutline(renderX, renderY, width, height, 2f, 0.05f, ColorUtil.TRANSPARENT_COLOR, Color.WHITE);
        }

        if (hovering && Mouse.isButtonDown(0) && !dragging && drag) {
            dragging = true;
            dragX = mouseX;
            dragY = mouseY;
        }

        if (!Mouse.isButtonDown(0)) dragging = false;

        if (dragging) {
            float deltaX = (float) (mouseX - dragX) / sr.getScaledWidth();
            float deltaY = (float) (mouseY - dragY) / sr.getScaledHeight();

            x += deltaX;
            y += deltaY;

            dragX = mouseX;
            dragY = mouseY;
        }
    }
}
