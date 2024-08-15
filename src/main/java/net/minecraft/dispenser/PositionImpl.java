package net.minecraft.dispenser;

import lombok.Getter;

@Getter
public class PositionImpl implements IPosition {
    protected final double x;
    protected final double y;
    protected final double z;

    public PositionImpl(double xCoord, double yCoord, double zCoord) {
        this.x = xCoord;
        this.y = yCoord;
        this.z = zCoord;
    }

}
