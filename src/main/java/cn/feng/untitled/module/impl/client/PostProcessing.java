package cn.feng.untitled.module.impl.client;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.impl.ShaderEvent;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.blur.KawaseBloom;
import cn.feng.untitled.util.render.blur.KawaseBlur;
import cn.feng.untitled.value.impl.BoolValue;
import cn.feng.untitled.value.impl.NumberValue;
import net.minecraft.client.shader.Framebuffer;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class PostProcessing extends Module {
    public PostProcessing() {
        super("PostProcessing", ModuleCategory.Client);
        locked = true;
    }

    private static Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);

    public static final BoolValue blur = new BoolValue("Blur", false);
    public static final NumberValue blurRadius = new NumberValue("BlurRadius", 3f,  5f, 1f, 1f);
    public static final NumberValue blurOffset = new NumberValue("BlurOffset", 3f,  5f, 1f, 1f);
    public static final BoolValue bloom = new BoolValue("Bloom", false);
    public static final NumberValue bloomRadius = new NumberValue("BloomRadius", 3f,  5f, 1f, 1f);
    public static final NumberValue bloomOffset = new NumberValue("BloomOffset", 3f,  5f, 1f, 1f);

    public static void blurScreen() {
        if (blur.getValue()) {
            stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
            stencilFramebuffer.framebufferClear();
            stencilFramebuffer.bindFramebuffer(false);
            Client.instance.eventBus.post(new ShaderEvent(false));
            stencilFramebuffer.unbindFramebuffer();
            KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, blurRadius.getValue().intValue(), blurOffset.getValue().intValue());
        }

        if (bloom.getValue()) {
            stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
            stencilFramebuffer.framebufferClear();
            stencilFramebuffer.bindFramebuffer(false);
            Client.instance.eventBus.post(new ShaderEvent(true));
            stencilFramebuffer.unbindFramebuffer();
            KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, bloomRadius.getValue().intValue(), bloomOffset.getValue().intValue());
        }
    }
}
