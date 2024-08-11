package cn.feng.untitled.ui.screen.music.api;

import cn.feng.untitled.util.data.DataUtil;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class MusicAPI {
    public static final String host = "https://neteasecloudmusicapi.vercel.app";
    private static String cookie = "";

    private static String get(String url) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        // 创建 GET 请求，设置 Cookie 头
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", cookie) // 设置 Cookie 头
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new NullPointerException("什么玩意这是");
        }
    }

    public static QRCode genQRCode() throws IOException {
        String key = get(host + "/login/qr/key&timestamp=" + System.currentTimeMillis());
        JsonObject keyObj = DataUtil.gson.fromJson(key, JsonObject.class);
        String uniqueKey = keyObj.get("data").getAsJsonObject().get("unikey").getAsString();

        String code = get(host + "/login/qr/create?key=" + uniqueKey + "&qrimg=true&timestamp=" + System.currentTimeMillis());
        JsonObject codeObj = DataUtil.gson.fromJson(code, JsonObject.class);
        String base64String = codeObj.get("data").getAsJsonObject().get("qrimg").getAsString();

        String base64Image = base64String.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);
        bis.close();

        return new QRCode(image, uniqueKey);
    }

    public static QRCodeState getScanResult(String key) {
        String response = get(host + "/login/qr/check?key=" + key + "&timestamp=" + System.currentTimeMillis());
        JsonObject object = DataUtil.gson.fromJson(response, JsonObject.class);
        int code = object.get("code").getAsInt();
        if (code == 803) cookie = object.get("cookie").getAsString();

        return switch (code) {
            case 801 -> QRCodeState.WAITING_SCAN;
            case 802 -> QRCodeState.WAITING_CONFIRM;
            case 803 -> QRCodeState.SUCCEED;
            default -> QRCodeState.EXPIRED;
        };
    }

    public static void getUserInfo() {
        System.out.println(get(host + "/user/account"));
    }
}
