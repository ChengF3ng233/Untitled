package net.optifine.gui;

import lombok.Getter;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;

@Getter
public class GuiOptionButtonOF extends GuiOptionButton implements IOptionControl {
    private GameSettings.Options option = null;

    public GuiOptionButtonOF(int id, int x, int y, GameSettings.Options option, String text) {
        super(id, x, y, option, text);
        this.option = option;
    }

}
