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

    public Particle(float startX, float radius) {
        this.radius = radius;
        x = startX;
        y = -radius;
        Random random = new Random();
        speed = random.nextFloat(0.1f, 0.5f);
    }

    public void render() {
        ScaledResolution sr = new ScaledResolution(mc);
        RoundedUtil.drawRound(x, y, radius, radius, radius / 3.5f, ColorUtil.applyOpacity(Color.WHITE, 0.6f));
        x += (float) (speed * RenderUtil.frameTime) * 0.7f;
        y += (float) (speed * RenderUtil.frameTime);

        if (y > sr.getScaledHeight()) y = -radius;
        if (x > sr.getScaledWidth()) {
            x = 0;
        }
    }
}
