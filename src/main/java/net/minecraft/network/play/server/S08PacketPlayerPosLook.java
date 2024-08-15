package net.minecraft.network.play.server;

import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class S08PacketPlayerPosLook implements Packet<INetHandlerPlayClient> {
    @Getter
    private double x;
    @Getter
    private double y;
    @Getter
    private double z;
    @Getter
    private float yaw;
    @Getter
    private float pitch;
    private Set<S08PacketPlayerPosLook.EnumFlags> field_179835_f;

    public S08PacketPlayerPosLook() {
    }

    public S08PacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn, Set<S08PacketPlayerPosLook.EnumFlags> p_i45993_9_) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
        this.yaw = yawIn;
        this.pitch = pitchIn;
        this.field_179835_f = p_i45993_9_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.field_179835_f = S08PacketPlayerPosLook.EnumFlags.func_180053_a(buf.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeByte(S08PacketPlayerPosLook.EnumFlags.func_180056_a(this.field_179835_f));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handlePlayerPosLook(this);
    }

    public Set<S08PacketPlayerPosLook.EnumFlags> func_179834_f() {
        return this.field_179835_f;
    }

    public enum EnumFlags {
        X(0),
        Y(1),
        Z(2),
        Y_ROT(3),
        X_ROT(4);

        private final int field_180058_f;

        EnumFlags(int p_i45992_3_) {
            this.field_180058_f = p_i45992_3_;
        }

        public static Set<S08PacketPlayerPosLook.EnumFlags> func_180053_a(int p_180053_0_) {
            Set<S08PacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);

            for (S08PacketPlayerPosLook.EnumFlags s08packetplayerposlook$enumflags : values()) {
                if (s08packetplayerposlook$enumflags.func_180054_b(p_180053_0_)) {
                    set.add(s08packetplayerposlook$enumflags);
                }
            }

            return set;
        }

        public static int func_180056_a(Set<S08PacketPlayerPosLook.EnumFlags> p_180056_0_) {
            int i = 0;

            for (S08PacketPlayerPosLook.EnumFlags s08packetplayerposlook$enumflags : p_180056_0_) {
                i |= s08packetplayerposlook$enumflags.func_180055_a();
            }

            return i;
        }

        private int func_180055_a() {
            return 1 << this.field_180058_f;
        }

        private boolean func_180054_b(int p_180054_1_) {
            return (p_180054_1_ & this.func_180055_a()) == this.func_180055_a();
        }
    }
}
