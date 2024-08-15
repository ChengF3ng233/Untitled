package net.optifine.shaders;

import lombok.Getter;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;

@Getter
public class CustomTexture implements ICustomTexture {
    private int textureUnit = -1;
    private String path = null;
    private ITextureObject texture = null;

    public CustomTexture(int textureUnit, String path, ITextureObject texture) {
        this.textureUnit = textureUnit;
        this.path = path;
        this.texture = texture;
    }

    public int getTextureId() {
        return this.texture.getGlTextureId();
    }

    public void deleteTexture() {
        TextureUtil.deleteTexture(this.texture.getGlTextureId());
    }

    public int getTarget() {
        return 3553;
    }

    public String toString() {
        return "textureUnit: " + this.textureUnit + ", path: " + this.path + ", glTextureId: " + this.getTextureId();
    }
}
