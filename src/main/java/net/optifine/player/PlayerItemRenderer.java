package net.optifine.player;

import lombok.Getter;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class PlayerItemRenderer {
    private int attachTo = 0;
    @Getter
    private ModelRenderer modelRenderer = null;

    public PlayerItemRenderer(int attachTo, ModelRenderer modelRenderer) {
        this.attachTo = attachTo;
        this.modelRenderer = modelRenderer;
    }

    public void render(ModelBiped modelBiped, float scale) {
        ModelRenderer modelrenderer = PlayerItemModel.getAttachModel(modelBiped, this.attachTo);

        if (modelrenderer != null) {
            modelrenderer.postRender(scale);
        }

        this.modelRenderer.render(scale);
    }
}
