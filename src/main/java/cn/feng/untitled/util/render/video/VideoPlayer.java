package cn.feng.untitled.util.render.video;

import cn.feng.untitled.util.MinecraftInstance;
import cn.feng.untitled.util.misc.Logger;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 *
 * @author LingYuWeiGuang
 * @author HyperTap
 * @author ChengFeng
 * @version 1.1.0
 */
public class VideoPlayer extends MinecraftInstance {
    private FFmpegFrameGrabber frameGrabber;
    private TextureBinder textureBinder;

    private int frameLength;
    @Getter
    private final AtomicBoolean paused = new AtomicBoolean(false);

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    /**
     * 读取一个视频文件
     *
     * @param resource 资源地址
     */
    public void init(ResourceLocation resource) throws FFmpegFrameGrabber.Exception {
        File videoTemp;

        // 创建缓存文件
        try {
            videoTemp = File.createTempFile("video_temp", ".mp4");
            InputStream inputStream = mc.getResourceManager().getResource(resource).getInputStream();

            // 覆写
            Files.copy(inputStream, videoTemp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            videoTemp.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        frameGrabber = FFmpegFrameGrabber.createDefault(videoTemp);
        frameGrabber.setPixelFormat(avutil.AV_PIX_FMT_RGB24);
        avutil.av_log_set_level(avutil.AV_LOG_QUIET); // Log level -> quiet

        textureBinder = new TextureBinder();

        frameGrabber.start();
        frameLength = frameGrabber.getLengthInFrames();

        double frameRate = frameGrabber.getFrameRate();

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = scheduler.scheduleAtFixedRate(this::grabNextFrame, 0, (long) (1000 / frameRate), TimeUnit.MILLISECONDS);
    }

    /**
     * 没完没了地抓取下一帧并设置纹理绑定器数据
     */
    private void grabNextFrame() {
        // 暂停了就不再抓了
        if (paused.get()) return;
        try {
            Frame frame = frameGrabber.grabImage();
            if (frame != null && frame.image != null) {
                textureBinder.setTexture((ByteBuffer) frame.image[0], frame.imageWidth, frame.imageHeight);
                if (frameGrabber.getFrameNumber() == frameLength - 1) {
                    frameGrabber.setFrameNumber(0);
                }
            }
        } catch (FFmpegFrameGrabber.Exception e) {
            Logger.error(e.getMessage());
        }
    }

    /**
     * 绑定纹理并渲染到指定矩形。
     *
     * @param left   左端
     * @param top    顶端
     * @param right  右端
     * @param bottom 底端
     */
    public void render(int left, int top, int right, int bottom) throws FrameGrabber.Exception {
        textureBinder.bindTexture();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        // 绘制矩形
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(left, bottom, 0);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(right, bottom, 0);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(right, top, 0);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(left, top, 0);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * 调用这个，你就别想再启动了！
     * @throws FFmpegFrameGrabber.Exception
     */
    public void stop() throws FFmpegFrameGrabber.Exception {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
        }

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }

        textureBinder = null;

        if (frameGrabber != null) {
            frameGrabber.stop();
            frameGrabber.release();
            frameGrabber = null;
        }
    }
}
