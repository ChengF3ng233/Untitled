package cn.feng.untitled.ui.clickgui.window.component.impl;

import cn.feng.untitled.ui.clickgui.window.ThemeColor;
import cn.feng.untitled.ui.clickgui.window.component.Component;
import cn.feng.untitled.ui.clickgui.window.gui.TextField;
import cn.feng.untitled.ui.font.Font;
import cn.feng.untitled.ui.font.FontLoader;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.value.impl.StringValue;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class StringComponent extends Component<String> {
    private boolean hovered;
    private final TextField strField;

    public StringComponent(StringValue value) {
        super(value);
        Font font = FontLoader.greyCliff(16);
        this.strField = new TextField(font);
        this.width = 65.0F;
        this.height = font.height() + 4.0F;
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x + panelWidth - width - xGap * 2;
        this.y = y - 4.0F;
        this.hovered = RenderUtil.hovering(mouseX, mouseY, this.x, this.y, width, height);
        this.strField.setXPosition(this.x);
        this.strField.setYPosition(this.y);
        this.strField.setWidth(this.width);
        this.strField.setHeight(this.height);
        this.strField.setRadius(1.0F);
        this.strField.setFill(ThemeColor.barColor);
        this.strField.setOutline(ThemeColor.outlineColor);
        this.strField.drawTextBox();
    }


    @Override
    public void onKeyTyped(char c, int keyCode) {
        this.strField.keyTyped(c, keyCode);
        this.value.value = this.strField.getText();
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        this.strField.mouseClicked(mouseX, mouseY, button);
        this.strField.setFocused(button == 0 && this.hovered);
    }
}
