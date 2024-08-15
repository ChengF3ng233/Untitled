package net.optifine.entity.model;

import lombok.Getter;
import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.anim.ModelUpdater;

@Getter
public class CustomModelRenderer {
    private final String modelPart;
    private final boolean attach;
    private final ModelRenderer modelRenderer;
    private final ModelUpdater modelUpdater;

    public CustomModelRenderer(String modelPart, boolean attach, ModelRenderer modelRenderer, ModelUpdater modelUpdater) {
        this.modelPart = modelPart;
        this.attach = attach;
        this.modelRenderer = modelRenderer;
        this.modelUpdater = modelUpdater;
    }

}
