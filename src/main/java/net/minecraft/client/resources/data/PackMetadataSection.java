package net.minecraft.client.resources.data;

import lombok.Getter;
import net.minecraft.util.IChatComponent;

@Getter
public class PackMetadataSection implements IMetadataSection {
    private final IChatComponent packDescription;
    private final int packFormat;

    public PackMetadataSection(IChatComponent p_i1034_1_, int p_i1034_2_) {
        this.packDescription = p_i1034_1_;
        this.packFormat = p_i1034_2_;
    }

}
