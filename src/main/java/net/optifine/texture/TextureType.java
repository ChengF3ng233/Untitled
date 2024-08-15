package net.optifine.texture;

import lombok.Getter;

@Getter
public enum TextureType {
    TEXTURE_1D(3552),
    TEXTURE_2D(3553),
    TEXTURE_3D(32879),
    TEXTURE_RECTANGLE(34037);

    private final int id;

    TextureType(int id) {
        this.id = id;
    }

}
