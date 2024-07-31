package cn.feng.untitled.ui.clickgui.window;

import cn.feng.untitled.module.ModuleCategory;

public enum CategoryType {
    Attack,
    Visual,
    Misc,
    Setting;

    public static CategoryType getType(ModuleCategory category) {
        switch (category) {
            case Client -> {
                return Setting;
            }

            case Render, Widget -> {
                return Visual;
            }

            case Rage, Legit -> {
                return Attack;
            }

            default -> {
                return Misc;
            }
         }
    }
}
