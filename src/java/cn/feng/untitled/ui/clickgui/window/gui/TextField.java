package cn.feng.untitled.ui.clickgui.window.gui;

import cn.feng.untitled.ui.clickgui.window.ThemeColor;
import cn.feng.untitled.ui.font.CenterType;
import cn.feng.untitled.ui.font.Font;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.misc.Logger;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/2
 **/
public class TextField {
    private float posX, posY;
    private final float width, height, textMaxWidth;
    private boolean focused;
    public String text;

    private final Font font;
    private final Color backgroundColor, outlineColor;
    private final ColorAnimation textColorAnim;
    private final ColorAnimation cursorColorAnim;

    public TextField(float width, float height, Font font, Color backgroundColor, Color outlineColor) {
        this.width = width;
        this.textMaxWidth = width - 2f;
        this.height = height;
        this.font = font;
        this.backgroundColor = backgroundColor;
        this.outlineColor = outlineColor;
        this.textColorAnim = new ColorAnimation(Color.WHITE, ThemeColor.grayColor, 100);
        this.cursorColorAnim = new ColorAnimation(Color.WHITE, ColorUtil.TRANSPARENT_COLOR, 500);
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

        RoundedUtil.drawRoundOutline(posX, posY, width, height, 2f, 0.2f, backgroundColor, outlineColor);

        String visibleText = font.getStringWidth(text) > textMaxWidth? font.trimStringToWidth(text, textMaxWidth, true) : text;
        float textX = posX + 2f;
        float textY = posY + height / 2f;
        font.drawCenteredString(visibleText, textX, textY, textColorAnim.getOutput().getRGB(), CenterType.Vertical, true);

        if (focused) {
            RoundedUtil.drawRound(textX + font.getStringWidth(visibleText) + 1.5f, posY + 2f, 0.5f, height - 4f, 1f, cursorColorAnim.getOutput());
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
                if (!ChatAllowedCharacters.isAllowedCharacter(c)) return;
                text += c;
            }
        }
    }
}
