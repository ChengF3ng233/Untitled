package cn.feng.untitled.util.render.particle;

import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.render.ColorUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.Random;

/**
 * @author ChengFeng
 * @since 2024/8/22
 **/
public class Particle extends MinecraftInstance {
    private final float radius;
    private float x;
    private float y;
    private final float speed;

    public Particle() {
        ScaledResolution sr = new ScaledResolution(mc);
        Random random = new Random();
        radius = random.nextFloat(2.5f, 3f);
        x = random.nextFloat(0, (float) sr.getScaledWidth_double());
        y = -radius;
        speed = random.nextFloat(0.1f, 0.5f);
    }

    public void render() {
        ScaledResolution sr = new ScaledResolution(mc);
        RoundedUtil.drawRound(x, y, radius, radius, radius / 3.5f, ColorUtil.applyOpacity(Color.WHITE, 0.8f));
        x += (float) (speed * RenderUtil.frameTime) * 0.7f;
        y += (float) (speed * RenderUtil.frameTime);

        if (y > sr.getScaledHeight()) {
            y = -radius;
        }

        if (x > sr.getScaledWidth()) {
            x = 0;
        }
    }
}
