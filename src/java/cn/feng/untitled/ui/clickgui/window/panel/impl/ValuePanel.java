package cn.feng.untitled.ui.clickgui.window.panel.impl;

import cn.feng.untitled.ui.clickgui.window.component.Component;
import cn.feng.untitled.ui.clickgui.window.component.impl.ButtonComponent;
import cn.feng.untitled.ui.clickgui.window.panel.Panel;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.value.Value;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.ModeValue;
import cn.feng.untitled.value.impl.NumberValue;
import cn.feng.untitled.value.impl.StringValue;

import java.awt.*;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ValuePanel extends Panel {
    public Value<?> value;
    private Component<?> component;

    public ValuePanel(Value<?> value) {
        this.value = value;
        if (!(value instanceof BoolValue)) return;

        if (value instanceof BoolValue bv) {
            component = new ButtonComponent(bv);
        } else if (value instanceof NumberValue) {

        } else if (value instanceof ModeValue) {

        } else if (value instanceof StringValue) {

        } else {
            // ColorValue
        }

        component.width = this.width;
    }

    @Override
    public void init() {
        if (!(value instanceof BoolValue)) return;
        component.init();
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        if (!(value instanceof BoolValue)) return;
        int textColor = Color.WHITE.getRGB();
        if (component instanceof ButtonComponent bc) {
            textColor = bc.textColAnim.getOutput().getRGB();
        }
        FontLoader.greyCliff(16).drawString(value.name, x, y, textColor);
        component.draw(x, y, mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        component.onMouseClick(mouseX, mouseY, button);
    }
}
