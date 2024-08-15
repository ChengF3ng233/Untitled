package net.optifine.shaders.config;

import lombok.Getter;

@Getter
public class RenderScale {
    private float scale = 1.0F;
    private float offsetX = 0.0F;
    private float offsetY = 0.0F;

    public RenderScale(float scale, float offsetX, float offsetY) {
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public String toString() {
        return this.scale + ", " + this.offsetX + ", " + this.offsetY;
    }
}
