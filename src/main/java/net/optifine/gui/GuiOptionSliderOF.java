package net.optifine.gui;

import lombok.Getter;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.settings.GameSettings;

@Getter
public class GuiOptionSliderOF extends GuiOptionSlider implements IOptionControl {
    private GameSettings.Options option = null;

    public GuiOptionSliderOF(int id, int x, int y, GameSettings.Options option) {
        super(id, x, y, option);
        this.option = option;
    }

}
