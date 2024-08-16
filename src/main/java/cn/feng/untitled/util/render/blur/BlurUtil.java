package cn.feng.untitled.util.render.blur;

import cn.feng.untitled.util.render.RenderUtil;
import net.minecraft.client.shader.Framebuffer;

import static cn.feng.untitled.module.impl.client.PostProcessing.*;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class BlurUtil {
    private static Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);
    public static Framebuffer framebuffer = new Framebuffer(1, 1, true);

    public static void blur(Runnable runnable) {
        runnable.run();
    }

    public static void processStart() {
        stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
        stencilFramebuffer.framebufferClear();
        stencilFramebuffer.bindFramebuffer(false);
    }

    public static void blurEnd() {
        stencilFramebuffer.unbindFramebuffer();
        KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, blurRadius.getValue().intValue(), blurOffset.getValue().intValue());
    }

    public static void blurEnd(int radius, int offset) {
        stencilFramebuffer.unbindFramebuffer();
        KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, radius, offset);
    }

    public static void bloomEnd() {
        stencilFramebuffer.unbindFramebuffer();
        KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, bloomRadius.getValue().intValue(), bloomOffset.getValue().intValue());
    }

    public static void bloomEnd(int radius, int offset) {
        stencilFramebuffer.unbindFramebuffer();
        KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, radius, offset);
    }
}
