package cn.feng.untitled.ui.clickgui.component.impl;

import cn.feng.untitled.ui.clickgui.ThemeColor;
import cn.feng.untitled.ui.clickgui.component.Component;
import cn.feng.untitled.ui.clickgui.gui.Icon;
import cn.feng.untitled.ui.font.awt.AWTFontLoader;
import cn.feng.untitled.ui.font.awt.AWTFont;
import cn.feng.untitled.util.animation.advanced.Direction;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.data.compare.StringComparator;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.ModeValue;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class ModeComponent extends Component<String> {
    private final AWTFont AWTFont = AWTFontLoader.greyCliff(16);
    private final Icon left;
    private final Icon right;
    private final ColorAnimation colorAnim;
    private boolean selected;

    public ModeComponent(ModeValue value) {
        super(value);
        List<String> modes = new ArrayList<>(List.of(value.values));
        modes.sort(new StringComparator(AWTFont));
        width = AWTFont.getStringWidth(modes.get(modes.size() - 1));
        height = 13f;
        left = new Icon(new ResourceLocation("untitled/icon/arrow-left.png"), 12f);
        right = new Icon(new ResourceLocation("untitled/icon/arrow-right.png"), 12f);
        colorAnim = new ColorAnimation(Color.WHITE, ThemeColor.grayColor, 100);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.posX = x + panelWidth - 2 * xGap - width - 12f;
        this.posY = y - 3f;

        if (selected) Keyboard.enableRepeatEvents(false);

        if (selected && colorAnim.getDirection() == Direction.FORWARDS) {
            colorAnim.changeDirection();
        } else if (!selected && colorAnim.getDirection() == Direction.BACKWARDS) {
            colorAnim.changeDirection();
        }

        if (!Keyboard.isKeyDown(Keyboard.KEY_LEFT) && left.colorAnim.getDirection() == Direction.FORWARDS) {
            left.colorAnim.changeDirection();
            left.lock = false;
        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && right.colorAnim.getDirection() == Direction.FORWARDS) {
            right.colorAnim.changeDirection();
            right.lock = false;
        }

        left.draw(this.posX - 9f, this.posY, mouseX, mouseY);
        right.draw(this.posX + width + 3f, this.posY, mouseX, mouseY);
        AWTFont.drawString(value.getValue(), this.posX + 3f, this.posY + 3.5f, colorAnim.getOutput().getRGB());
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0) {
            if (RenderUtil.hovering(mouseX, mouseY, this.posX - 9f, this.posY, 12f, 12f)) {
                ((ModeValue) value).previous();
            } else if (RenderUtil.hovering(mouseX, mouseY, this.posX + width + 2f, this.posY, 12f, 12f)) {
                ((ModeValue) value).next();
            }

            selected = RenderUtil.hovering(mouseX, mouseY, this.posX - 9f, this.posY, width + 24f, height + 6f);
        }
    }

    @Override
    public void onKeyTyped(char c, int keyCode) {
        if (!selected) return;

        if (keyCode == Keyboard.KEY_LEFT) {
            ((ModeValue) value).previous();
            if (left.colorAnim.getDirection() == Direction.BACKWARDS) {
                left.colorAnim.changeDirection();
                left.lock = true;
            }
        } else if (keyCode == Keyboard.KEY_RIGHT) {
            ((ModeValue) value).next();
            if (right.colorAnim.getDirection() == Direction.BACKWARDS) {
                right.colorAnim.changeDirection();
                right.lock = true;
            }
        }
    }
}
