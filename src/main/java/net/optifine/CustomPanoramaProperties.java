package net.optifine;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.optifine.config.ConnectedParser;

import java.util.Properties;

public class CustomPanoramaProperties {
    private final String path;
    @Getter
    private final ResourceLocation[] panoramaLocations;
    @Getter
    private int weight = 1;
    @Getter
    private int blur1 = 64;
    @Getter
    private int blur2 = 3;
    @Getter
    private int blur3 = 3;
    @Getter
    private int overlay1Top = -2130706433;
    @Getter
    private int overlay1Bottom = 16777215;
    @Getter
    private int overlay2Top = 0;
    @Getter
    private int overlay2Bottom = Integer.MIN_VALUE;

    public CustomPanoramaProperties(String path, Properties props) {
        ConnectedParser connectedparser = new ConnectedParser("CustomPanorama");
        this.path = path;
        this.panoramaLocations = new ResourceLocation[6];

        for (int i = 0; i < this.panoramaLocations.length; ++i) {
            this.panoramaLocations[i] = new ResourceLocation(path + "/panorama_" + i + ".png");
        }

        this.weight = connectedparser.parseInt(props.getProperty("weight"), 1);
        this.blur1 = connectedparser.parseInt(props.getProperty("blur1"), 64);
        this.blur2 = connectedparser.parseInt(props.getProperty("blur2"), 3);
        this.blur3 = connectedparser.parseInt(props.getProperty("blur3"), 3);
        this.overlay1Top = ConnectedParser.parseColor4(props.getProperty("overlay1.top"), -2130706433);
        this.overlay1Bottom = ConnectedParser.parseColor4(props.getProperty("overlay1.bottom"), 16777215);
        this.overlay2Top = ConnectedParser.parseColor4(props.getProperty("overlay2.top"), 0);
        this.overlay2Bottom = ConnectedParser.parseColor4(props.getProperty("overlay2.bottom"), Integer.MIN_VALUE);
    }

    public String toString() {
        return this.path + ", weight: " + this.weight + ", blur: " + this.blur1 + " " + this.blur2 + " " + this.blur3 + ", overlay: " + this.overlay1Top + " " + this.overlay1Bottom + " " + this.overlay2Top + " " + this.overlay2Bottom;
    }
}
