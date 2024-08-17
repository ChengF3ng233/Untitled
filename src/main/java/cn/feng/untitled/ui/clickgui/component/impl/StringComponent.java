package cn.feng.untitled.ui.clickgui.component.impl;

import cn.feng.untitled.ui.clickgui.ThemeColor;
import cn.feng.untitled.ui.clickgui.component.Component;
import cn.feng.untitled.ui.clickgui.gui.TextField;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.value.impl.StringValue;

/**
 * @author ChengFeng
 * @since 2024/8/1
 **/
public class StringComponent extends Component<String> {
    private final TextField textField;

    public StringComponent(StringValue value) {
        super(value);

        width = 65f;
        height = 13f;

        textField = new TextField(width, height, NanoFontLoader.misans, ThemeColor.barColor, ThemeColor.outlineColor);
        textField.text = value.getValue();
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY) {
        this.posX = x + panelWidth - width - xGap * 2;
        this.posY = y - 4.0F;

        textField.draw(this.posX, this.posY, mouseX, mouseY);
    }
    
    @Override
    public void onKeyTyped(char c, int keyCode) {
        textField.keyTyped(c, keyCode);
        value.setValue(textField.text);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        textField.mouseClicked(mouseX, mouseY, button);
    }
}
