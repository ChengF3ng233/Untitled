package cn.feng.untitled.util.render;

import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.data.MathUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author ChengFeng
 * @since 2024/8/5
 **/
public class ESPUtil extends MinecraftInstance {
    private static final Frustum frustum = new Frustum();
    private static final FloatBuffer windPos = BufferUtils.createFloatBuffer(4);
    private static final IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer floatBuffer1 = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer floatBuffer2 = GLAllocation.createDirectFloatBuffer(16);

    public static boolean isInView(Entity ent) {
        frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        return frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) || ent.ignoreFrustumCheck;
    }

    public static Vector3f projectOn2D(float x, float y, float z, int scaleFactor) {
        glGetFloatv(GL_MODELVIEW_MATRIX, floatBuffer1);
        glGetFloatv(GL_PROJECTION_MATRIX, floatBuffer2);
        glGetIntegerv(GL_VIEWPORT, intBuffer);
        if (GLU.gluProject(x, y, z, floatBuffer1, floatBuffer2, intBuffer, windPos)) {
            return new Vector3f(windPos.get(0) / scaleFactor, (mc.displayHeight - windPos.get(1)) / scaleFactor, windPos.get(2));
        }
        return null;
    }

    public static double[] getInterpolatedPos(Entity entity) {
        float ticks = mc.timer.renderPartialTicks;
        return new double[]{
                MathUtil.interpolate(entity.lastTickPosX, entity.posX, ticks) - mc.getRenderManager().viewerPosX,
                MathUtil.interpolate(entity.lastTickPosY, entity.posY, ticks) - mc.getRenderManager().viewerPosY,
                MathUtil.interpolate(entity.lastTickPosZ, entity.posZ, ticks) - mc.getRenderManager().viewerPosZ
        };
    }

    public static AxisAlignedBB getInterpolatedBoundingBox(Entity entity) {
        final double[] renderingEntityPos = getInterpolatedPos(entity);
        final double entityRenderWidth = entity.width / 1.5;
        return new AxisAlignedBB(renderingEntityPos[0] - entityRenderWidth,
                renderingEntityPos[1], renderingEntityPos[2] - entityRenderWidth, renderingEntityPos[0] + entityRenderWidth,
                renderingEntityPos[1] + entity.height + (entity.isSneaking() ? -0.3 : 0.18), renderingEntityPos[2] + entityRenderWidth).expand(0.15, 0.15, 0.15);
    }

    public static Vector4f getEntityPositionsOn2D(Entity entity) {
        final AxisAlignedBB bb = getInterpolatedBoundingBox(entity);

        float yOffset = 0;

        final List<Vector3f> vectors = Arrays.asList(
                new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.minZ),
                new Vector3f((float) bb.minX, (float) bb.maxY - yOffset, (float) bb.minZ),
                new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.minZ),
                new Vector3f((float) bb.maxX, (float) bb.maxY - yOffset, (float) bb.minZ),
                new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.maxZ),
                new Vector3f((float) bb.minX, (float) bb.maxY - yOffset, (float) bb.maxZ),
                new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.maxZ),
                new Vector3f((float) bb.maxX, (float) bb.maxY - yOffset, (float) bb.maxZ));

        Vector4f entityPos = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);
        ScaledResolution sr = new ScaledResolution(mc);
        for (Vector3f vector3f : vectors) {
            vector3f = projectOn2D(vector3f.x, vector3f.y, vector3f.z, sr.getScaleFactor());
            if (vector3f != null && vector3f.z >= 0.0 && vector3f.z < 1.0) {
                entityPos.x = Math.min(vector3f.x, entityPos.x);
                entityPos.y = Math.min(vector3f.y, entityPos.y);
                entityPos.z = Math.max(vector3f.x, entityPos.z);
                entityPos.w = Math.max(vector3f.y, entityPos.w);
            }
        }
        return entityPos;
    }
}
