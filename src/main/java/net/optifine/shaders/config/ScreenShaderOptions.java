package net.optifine.shaders.config;

import lombok.Getter;

@Getter
public class ScreenShaderOptions {
    private final String name;
    private final ShaderOption[] shaderOptions;
    private final int columns;

    public ScreenShaderOptions(String name, ShaderOption[] shaderOptions, int columns) {
        this.name = name;
        this.shaderOptions = shaderOptions;
        this.columns = columns;
    }

}
