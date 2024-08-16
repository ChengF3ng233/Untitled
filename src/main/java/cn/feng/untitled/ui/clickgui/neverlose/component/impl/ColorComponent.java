package cn.feng.untitled.ui.clickgui.neverlose.component.impl;

import cn.feng.untitled.ui.clickgui.neverlose.ThemeColor;
import cn.feng.untitled.ui.clickgui.neverlose.component.Component;
import cn.feng.untitled.ui.font.awt.CenterType;
import cn.feng.untitled.ui.font.awt.FontLoader;
import cn.feng.untitled.ui.font.awt.FontRenderer;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.CustomAnimation;
import cn.feng.untitled.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.util.render.StencilUtil;
import cn.feng.untitled.value.impl.ColorValue;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/8/2
 **/
public class ColorComponent extends Component<Color> {
    public ColorComponent(ColorValue value) {
        super(value);

        colorWidth = 65f;
        colorHeight = 12f;

        float maxHeight = 13f * 8f + 5f * 7f;
        heightAnim = new CustomAnimation(DecelerateAnimation.class, 100, maxHeight, minHeight);

        buttons = new ButtonComponent[2];
        buttons[0] = new ButtonComponent(value.rainbow);
        buttons[1] = new ButtonComponent(value.fade);

        numbers = new NumberComponent[5];
        numbers[0] = new NumberComponent(value.hue);
        numbers[1] = new NumberComponent(value.saturation);
        numbers[2] = new NumberComponent(value.brightness);
        numbers[3] = new NumberComponent(value.opacity);
        numbers[4] = new NumberComponent(value.speed);
    }

    private boolean expanded;
    private float minX, minY;
    private final float minHeight = 13f;
    private final float colorWidth, colorHeight;
    private final CustomAnimation heightAnim;
    private final ButtonComponent[] buttons;
    private final NumberComponent[] numbers;

    @Override
    public void init() {
        for (ButtonComponent button : buttons) {
            button.init();
        }
        for (NumberComponent number : numbers) {
            number.init();
        }
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.minX = x;
        this.minY = y;
        this.posX = x + panelWidth - 2 * xGap - colorWidth;
        this.posY = y - 3f;
        this.height = heightAnim.getOutput().floatValue();
        ColorValue cv = (ColorValue) value;

        if (heightAnim.getAnimation().finished(Direction.FORWARDS))
            expanded = false;

        RoundedUtil.drawRoundOutline(posX, posY, colorWidth, colorHeight, 2f, 0.2f, cv.getValue(), ThemeColor.outlineColor);
        FontLoader.rubik(16).drawCenteredString("#" + cv.getHexCode(), posX + colorWidth / 2f, posY + colorHeight / 2f, ColorUtil.getOppositeColor(cv.getValue()).getRGB(), CenterType.Both, true);

        if (expanded) {
            RoundedUtil.drawRound(minX + 2f, minY + minHeight + 2f, 0.5f, heightAnim.getOutput().floatValue() - minHeight - 6f, 1f, ThemeColor.grayColor);

            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRoundOutline(minX, minY, panelWidth - 2 * xGap + 2f, heightAnim.getOutput().floatValue(), 2f, 0.2f, cv.getValue(), ThemeColor.outlineColor);
            StencilUtil.readStencilBuffer(1);

            FontRenderer font = FontLoader.greyCliff(16);
            float textX = minX + 5f;
            float valueY = minY + minHeight + 5f;

            for (ButtonComponent button : buttons) {
                font.drawString(button.value.getName(), textX, valueY, button.textColAnim.getOutput().getRGB());
                button.draw(minX, valueY, mouseX, mouseY);
                valueY += button.height + 5f;
            }

            for (NumberComponent number : numbers) {
                font.drawString(number.value.getName(), textX, valueY, Color.WHITE.getRGB());
                number.draw(minX, valueY, mouseX, mouseY);
                valueY += number.height + 5f;
            }

            StencilUtil.uninitStencilBuffer();
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (RenderUtil.hovering(mouseX, mouseY, posX, posY, colorWidth, colorHeight)) {
            expanded = !expanded;
            heightAnim.changeDirection();
        }

        if (expanded && RenderUtil.hovering(mouseX, mouseY, minX, minY, panelWidth - 2 * xGap, heightAnim.getOutput().floatValue())) {
            for (ButtonComponent buttonComponent : buttons) {
                buttonComponent.onMouseClick(mouseX, mouseY, button);
            }
            for (NumberComponent number : numbers) {
                number.onMouseClick(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onMouseRelease() {
        if (expanded) {
            for (NumberComponent number : numbers) {
                number.onMouseRelease();
            }
        }
    }

    @Override
    public void onKeyTyped(char c, int keyCode) {
        if (expanded) {
            for (NumberComponent number : numbers) {
                number.onKeyTyped(c, keyCode);
            }
        }
    }
}
