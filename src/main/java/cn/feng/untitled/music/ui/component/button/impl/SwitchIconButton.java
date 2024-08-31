package cn.feng.untitled.music.ui.component.button.impl;

import cn.feng.untitled.util.data.resource.ResourceType;
import cn.feng.untitled.util.data.resource.ResourceUtil;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/19
 **/
public class SwitchIconButton extends IconButton {
    @Getter
    private String fileName;
    private final List<String> fileNames;
    private final List<ResourceLocation> icons = new ArrayList<>();

    public SwitchIconButton(String defaultIcon, List<String> icons, int size) {
        super(defaultIcon, size);
        fileName = defaultIcon;
        fileNames = icons;
        for (String icon : icons) {
            this.icons.add(ResourceUtil.getResource(icon, ResourceType.ICON));
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovering && button == 0) {
            int i = icons.indexOf(icon) + 1;
            if (i == icons.size()) i = 0;
            icon = icons.get(i);
            fileName = fileNames.get(i);
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
}
