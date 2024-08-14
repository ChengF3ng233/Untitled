package cn.feng.untitled.music.api;

import com.google.gson.JsonObject;
import lombok.Getter;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
public class ScanResult {
    private final QRCodeState state;
    private final JsonObject response;

    public ScanResult(QRCodeState state, JsonObject response) {
        this.state = state;
        this.response = response;
    }
}
