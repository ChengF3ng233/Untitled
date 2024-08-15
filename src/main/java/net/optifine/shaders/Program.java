package net.optifine.shaders;

import lombok.Getter;
import lombok.Setter;
import net.optifine.render.GlAlphaState;
import net.optifine.render.GlBlendState;
import net.optifine.shaders.config.RenderScale;

import java.nio.IntBuffer;
import java.util.Arrays;

@Getter
public class Program {
    private final int index;
    private final String name;
    private final ProgramStage programStage;
    private final Program programBackup;
    private final Boolean[] buffersFlip = new Boolean[8];
    private final boolean[] toggleColorTextures = new boolean[8];
    @Setter
    private GlAlphaState alphaState;
    @Setter
    private GlBlendState blendState;
    @Setter
    private RenderScale renderScale;
    @Setter
    private int id;
    @Setter
    private int ref;
    @Setter
    private String drawBufSettings;
    @Setter
    private IntBuffer drawBuffers;
    private IntBuffer drawBuffersBuffer;
    @Setter
    private int compositeMipmapSetting;
    @Setter
    private int countInstances;

    public Program(int index, String name, ProgramStage programStage, Program programBackup) {
        this.index = index;
        this.name = name;
        this.programStage = programStage;
        this.programBackup = programBackup;
    }

    public Program(int index, String name, ProgramStage programStage, boolean ownBackup) {
        this.index = index;
        this.name = name;
        this.programStage = programStage;
        this.programBackup = ownBackup ? this : null;
    }

    public void resetProperties() {
        this.alphaState = null;
        this.blendState = null;
        this.renderScale = null;
        Arrays.fill(this.buffersFlip, null);
    }

    public void resetId() {
        this.id = 0;
        this.ref = 0;
    }

    public void resetConfiguration() {
        this.drawBufSettings = null;
        this.compositeMipmapSetting = 0;
        this.countInstances = 0;

        if (this.drawBuffersBuffer == null) {
            this.drawBuffersBuffer = Shaders.nextIntBuffer(8);
        }
    }

    public void copyFrom(Program p) {
        this.id = p.getId();
        this.alphaState = p.getAlphaState();
        this.blendState = p.getBlendState();
        this.renderScale = p.getRenderScale();
        System.arraycopy(p.getBuffersFlip(), 0, this.buffersFlip, 0, this.buffersFlip.length);
        this.drawBufSettings = p.getDrawBufSettings();
        this.drawBuffers = p.getDrawBuffers();
        this.compositeMipmapSetting = p.getCompositeMipmapSetting();
        this.countInstances = p.getCountInstances();
        System.arraycopy(p.getToggleColorTextures(), 0, this.toggleColorTextures, 0, this.toggleColorTextures.length);
    }

    public String getRealProgramName() {
        if (this.id == 0) {
            return "none";
        } else {
            Program program;

            for (program = this; program.getRef() != this.id; program = program.getProgramBackup()) {
                if (program.getProgramBackup() == null || program.getProgramBackup() == program) {
                    return "unknown";
                }
            }

            return program.getName();
        }
    }

    public String toString() {
        return "name: " + this.name + ", id: " + this.id + ", ref: " + this.ref + ", real: " + this.getRealProgramName();
    }
}
