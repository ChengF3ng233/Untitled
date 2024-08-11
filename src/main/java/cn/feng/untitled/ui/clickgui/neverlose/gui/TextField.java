package cn.feng.untitled.ui.clickgui.neverlose.gui;

import cn.feng.untitled.ui.clickgui.neverlose.ThemeColor;
import cn.feng.untitled.ui.font.awt.CenterType;
import cn.feng.untitled.ui.font.awt.Font;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/2
 **/
public class TextField {
    private float posX, posY;
    public float width, height, textMaxWidth, offsetX;
    public boolean focused;
    public float radius;
    public String text;

    private final NanoFontRenderer font;
    public Color backgroundColor, outlineColor;
    private final ColorAnimation textColorAnim;
    private final ColorAnimation cursorColorAnim;

    public TextField(float width, float height, NanoFontRenderer font, Color backgroundColor, Color outlineColor) {
        this.text = "";
        this.width = width;
        this.textMaxWidth = width - 2f;
        this.height = height;
        this.font = font;
        this.backgroundColor = backgroundColor;
        this.outlineColor = outlineColor;
        this.textColorAnim = new ColorAnimation(Color.WHITE, ThemeColor.grayColor, 100);
        this.cursorColorAnim = new ColorAnimation(Color.WHITE, ColorUtil.TRANSPARENT_COLOR, 500);
        this.radius = 2f;
    }

    public void draw(float x, float y, int mouseX, int mouseY) {
        posX = x;
        posY = y;

        if (focused)
            Keyboard.enableRepeatEvents(true);

        if (focused && textColorAnim.getDirection() == Direction.FORWARDS) {
            textColorAnim.changeDirection();
        } else if (!focused && textColorAnim.getDirection() == Direction.BACKWARDS) {
            textColorAnim.changeDirection();
        }

        if (focused && cursorColorAnim.isFinished()) {
            cursorColorAnim.changeDirection();
        }

        font.setSize(16);

        RoundedUtil.drawRoundOutline(posX, posY, width, height, radius, 0.1f, backgroundColor, outlineColor);

        String visibleText = font.getStringWidth(text) > textMaxWidth - 3f - offsetX? font.trimStringToWidth(text, textMaxWidth - 3f - offsetX, true) : text;
        float textX = posX + 2f;
        float textY = posY + height / 2f;
        font.drawString(visibleText, textX + offsetX, textY - font.getSize() + 0.5f, NanoVG.NVG_ALIGN_MIDDLE, textColorAnim.getOutput(), true);

        if (focused) {
            float h = height * 0.6f;
            RoundedUtil.drawRound(textX + offsetX + font.getStringWidth(visibleText) + 1.5f, posY + height / 2 - h / 2, 0.5f, h, 1f, cursorColorAnim.getOutput());
        }
    }

    public void onMouseClick(int mouseX, int mouseY, int button) {
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, posX, posY, width, height);
        if (hovering && button == 0) focused = true;
        if (!hovering) focused = false;
    }

    public void onKeyTyped(char c, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) focused = false;

        if (!focused) return;

        switch (keyCode) {
            case (Keyboard.KEY_BACK) -> {
                int max = text.length() - 1;
                if (max <= 0) {
                    text = "";
                } else {
                    text = text.substring(0, max);
                }
            }

            default -> {
                if (c == '\u0000') return;
                text += c;
            }
        }
    }
}
