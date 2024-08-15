package net.optifine.entity.model;

import lombok.Getter;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ModelAdapter {
    private final Class entityClass;
    private final String name;
    private final float shadowSize;
    private String[] aliases;

    public ModelAdapter(Class entityClass, String name, float shadowSize) {
        this.entityClass = entityClass;
        this.name = name;
        this.shadowSize = shadowSize;
    }

    public ModelAdapter(Class entityClass, String name, float shadowSize, String[] aliases) {
        this.entityClass = entityClass;
        this.name = name;
        this.shadowSize = shadowSize;
        this.aliases = aliases;
    }

    public abstract ModelBase makeModel();

    public abstract ModelRenderer getModelRenderer(ModelBase var1, String var2);

    public abstract String[] getModelRendererNames();

    public abstract IEntityRenderer makeEntityRender(ModelBase var1, float var2);

    public ModelRenderer[] getModelRenderers(ModelBase model) {
        String[] astring = this.getModelRendererNames();
        List<ModelRenderer> list = new ArrayList();

        for (String s : astring) {
            ModelRenderer modelrenderer = this.getModelRenderer(model, s);

            if (modelrenderer != null) {
                list.add(modelrenderer);
            }
        }

        ModelRenderer[] amodelrenderer = list.toArray(new ModelRenderer[list.size()]);
        return amodelrenderer;
    }
}
