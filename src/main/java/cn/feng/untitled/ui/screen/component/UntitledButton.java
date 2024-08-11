package cn.feng.untitled.ui.screen.component;

import cn.feng.untitled.Client;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.util.animation.advanced.Animation;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.util.render.blur.BlurUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/6
 **/
public class UntitledButton {
    public float posX, posY, width, height;
    private final String text;
    public Runnable action;

    private final Animation scaleAnim = new SmoothStepAnimation(100, 0.1f, Direction.BACKWARDS);

    public UntitledButton(String text, Runnable action) {
        this.text = text;
        this.action = action;
    }

    public void draw(float x, float y, int mouseX, int mouseY) {
        posX = x;
        posY = y;
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, x, y + height / 4f, width, height);

        if (hovering && scaleAnim.getDirection() == Direction.BACKWARDS) {
            scaleAnim.changeDirection();
        } else if (!hovering && scaleAnim.getDirection() == Direction.FORWARDS) {
            scaleAnim.changeDirection();
        }

        RenderUtil.scaleStart(x + width / 2f, y + height / 2f, 1 + scaleAnim.getOutput().floatValue() * 0.2f);
        BlurUtil.processStart();
        RoundedUtil.drawRound(x, y + height / 4f, width, height, 5f, true, new Color(200, 200, 200, 255));
        BlurUtil.bloomEnd(1, 2);
        RenderUtil.scaleEnd();

        NanoFontLoader.greyCliff.drawGlowString(text, x + width / 2f, y + height / 2f, 20, 2f, NanoVG.NVG_ALIGN_CENTER, Color.WHITE, Color.WHITE);
    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
        boolean hovering = RenderUtil.hovering(mouseX, mouseY, posX, posY + height / 4f, width, height);
        if (hovering && button == 0) action.run();
    }
}
