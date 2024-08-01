package cn.feng.untitled.ui.clickgui.window.component.impl;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.ui.clickgui.window.ThemeColor;
import cn.feng.untitled.ui.clickgui.window.component.Component;
import cn.feng.untitled.util.animation.advanced.composed.ColorAnimation;
import cn.feng.untitled.util.animation.advanced.composed.CustomAnimation;
import cn.feng.untitled.util.animation.advanced.impl.SmoothStepAnimation;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.value.impl.BoolValue;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/31
 **/
public class ButtonComponent extends Component<Boolean> {
    private Module module;
    private final boolean moduleMode;
    public ColorAnimation textColAnim;
    public ColorAnimation bgColAnim;
    public ColorAnimation circColAnim;
    public CustomAnimation circXAnim;

    private final float bgWidth = 15f;
    private final float bgHeight = 7f;

    public ButtonComponent(BoolValue value) {
        super(value);
        moduleMode = false;
        this.width = bgWidth + 6f;
        this.height = bgHeight + 6f;
    }

    public ButtonComponent(Module module) {
        super(null);
        this.module = module;
        moduleMode = true;
        this.width = bgWidth + 6f;
        this.height = bgHeight + 6f;
    }

    @Override
    public void init() {
        textColAnim = new ColorAnimation(isEnabled() ? ThemeColor.grayColor : Color.WHITE, isEnabled() ? Color.WHITE : ThemeColor.grayColor, 200);
        bgColAnim = new ColorAnimation(isEnabled() ? Color.BLACK : (moduleMode ? ThemeColor.barBgColor : ThemeColor.grayColor), isEnabled() ? (moduleMode ? ThemeColor.barBgColor : ThemeColor.grayColor) : Color.BLACK, 200);
        circColAnim = new ColorAnimation(isEnabled() ? ThemeColor.grayColor : (moduleMode ? ThemeColor.focusedColor : Color.WHITE), isEnabled() ? (moduleMode ? ThemeColor.focusedColor : Color.WHITE) : ThemeColor.grayColor, 200);
        circXAnim = new CustomAnimation(SmoothStepAnimation.class, 200, isEnabled() ? 0f : 6f, isEnabled() ? 6f : 0f);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x + panelWidth - 2 * xGap - bgWidth;
        this.y = y;
        float bgY = this.y - 1f;

        RoundedUtil.drawRound(this.x, bgY, bgWidth, bgHeight, 3f, bgColAnim.getOutput());
        RoundedUtil.drawRound(this.x + circXAnim.getOutput().floatValue(), bgY - 1f, 9f, 9f, 4f, circColAnim.getOutput());
    }

    private void toggle() {
        if (moduleMode) {
            if (!module.fixed)
                module.toggle();
        } else value.value = !value.value;
        textColAnim.changeDirection();
        bgColAnim.changeDirection();
        circXAnim.changeDirection();
        circColAnim.changeDirection();
    }

    private boolean isEnabled() {
        return moduleMode ? module.enabled : value.value;
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (RenderUtil.hovering(mouseX, mouseY, x - 3f, y - 3f, width, height) && button == 0) {
            toggle();
        }
    }
}
