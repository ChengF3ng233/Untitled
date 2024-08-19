package cn.feng.untitled.music.api.user;

import com.google.gson.JsonObject;
import lombok.Getter;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
public record ScanResult(QRCodeState state, JsonObject response) {
}
