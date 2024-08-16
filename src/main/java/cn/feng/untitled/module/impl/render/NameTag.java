package cn.feng.untitled.module.impl.render;

import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.NameTagEvent;
import cn.feng.untitled.event.impl.NanoEvent;
import cn.feng.untitled.event.impl.Render3DEvent;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import cn.feng.untitled.module.impl.client.PostProcessing;
import cn.feng.untitled.module.impl.client.Target;
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;
import cn.feng.untitled.util.data.MathUtil;
import cn.feng.untitled.util.render.ESPUtil;
import cn.feng.untitled.util.render.RenderUtil;
import cn.feng.untitled.util.render.RoundedUtil;
import cn.feng.untitled.value.impl.ColorValue;
import cn.feng.untitled.value.impl.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

/**
 * @author ChengFeng
 * @since 2024/8/5
 **/
public class NameTag extends Module {
    private final Map<Entity, Vector4f> entityPosition = new HashMap<>();

    private final NumberValue normalDistance = new NumberValue("NormalDistance", 10f, 90f, 5f, 1f);
    private final NumberValue fontSize = new NumberValue("FontSize", 16f, 25f, 15f, 0.1f);
    private final ColorValue backgroundColor = new ColorValue("BackgroundColor", new Color(0, 0, 0, 100));

    public NameTag() {
        super("NameTag", ModuleCategory.Render);
    }

    @SubscribeEvent
    private void onNameTag(NameTagEvent e) {
        e.cancel();
    }

    @SubscribeEvent
    private void onNano(NanoEvent e) {
        RenderUtil.scaleStart(0, 0, 2);
        for (Entity entity : entityPosition.keySet()) {
            if (!(entity instanceof EntityLivingBase elb)) continue;

            Vector4f pos = entityPosition.get(entity);
            float x = pos.getX(),
                    y = pos.getY(),
                    right = pos.getZ();

            NanoFontRenderer font = NanoFontLoader.rubik;

            float middle = x + ((right - x) / 2);

            // Some constants
            float scale = Math.min(1f, normalDistance.getValue().floatValue() /  mc.thePlayer.getDistanceToEntity(elb));
            font.setSize(fontSize.getValue().floatValue() * scale);
            float fontHeight = font.getHeight(fontSize.getValue().floatValue());

            float healthValue = elb.getHealth();
            Color healthColor = healthValue > elb.getMaxHealth() * .75 ? new Color(66, 246, 123) : healthValue > elb.getMaxHealth() * .5 ? new Color(228, 255, 105) : healthValue >elb.getMaxHealth() *  .35 ? new Color(236, 100, 64) : new Color(255, 65, 68);
            String healthText = "[HP] " + healthValue;
            float healthWidth = font.getStringWidth(healthText);

            String nameText = elb.getName();
            float nameWidth = font.getStringWidth(nameText);

            String distanceText = "[Distance] " + MathUtil.round(mc.thePlayer.getDistanceToEntity(elb), 1);
            float distanceWidth = font.getStringWidth(distanceText);

            float xGap = 3f * scale;
            float yGap = scale;
            float width = healthWidth + nameWidth + distanceWidth + xGap * 6f;
            float renderX = middle - width / 2f;
            float textY = y - (fontHeight + 7f);

            glPushMatrix();

            RoundedUtil.drawRound(renderX - xGap * scale, textY - yGap * scale, healthWidth + 2 * xGap * scale,
                    fontHeight + 4f * scale, 4 * scale, backgroundColor.getValue());

            RenderUtil.resetColor();

            if (PostProcessing.bloom.getValue()) {
                font.drawGlowString(healthText, renderX, textY, healthColor);
            } else {
                font.drawString(healthText, renderX, textY, healthColor);
            }

            renderX += healthWidth + 3 * xGap;

            RoundedUtil.drawRound(renderX - xGap * scale, textY - yGap * scale, nameWidth + 2 * xGap * scale,
                    fontHeight + 4f * scale, 4 * scale, backgroundColor.getValue());

            RenderUtil.resetColor();

            if (PostProcessing.bloom.getValue()) {
                font.drawGlowString(nameText, renderX, textY, Color.WHITE);
            } else {
                font.drawString(nameText, renderX, textY, Color.WHITE);
            }

            renderX += nameWidth + 3 * xGap;

            RoundedUtil.drawRound(renderX - xGap * scale, textY - yGap * scale, distanceWidth + 2 * xGap * scale,
                    fontHeight + 4f * scale, 4 * scale, backgroundColor.getValue());

            RenderUtil.resetColor();

            if (PostProcessing.bloom.getValue()) {
                font.drawGlowString(distanceText, renderX, textY, Color.WHITE);
            } else {
                font.drawString(distanceText, renderX, textY, Color.WHITE);
            }

            glPopMatrix();
        }
        RenderUtil.scaleEnd();
    }

    @SubscribeEvent
    private void onRender3D(Render3DEvent e) {
        entityPosition.clear();
        for (final Entity entity : mc.theWorld.loadedEntityList) {
            if (shouldRender(entity) && ESPUtil.isInView(entity)) {
                entityPosition.put(entity, ESPUtil.getEntityPositionsOn2D(entity));
            }
        }
    }

    private boolean shouldRender(Entity entity) {
        if (entity.isDead || entity.isInvisible()) {
            return false;
        }
        if (Target.players.getValue() && entity instanceof EntityPlayer) {
            if (entity == mc.thePlayer) {
                return mc.gameSettings.thirdPersonView != 0;
            }
            return !entity.getDisplayName().getUnformattedText().contains("[NPC");
        }
        if (Target.animals.getValue() && entity instanceof EntityAnimal) {
            return true;
        }

        return Target.mobs.getValue() && entity instanceof EntityMob;
    }

}
