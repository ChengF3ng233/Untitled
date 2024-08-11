package cn.feng.untitled.music.api;

import cn.feng.untitled.util.data.DataUtil;
import com.google.gson.JsonObject;
import okhttp3.*;

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
    public static String cookie = "";

    private static String fetch(String url) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = new FormBody.Builder()
                .add("cookie", cookie)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            return string;
        } catch (IOException e) {
            throw new NullPointerException("什么玩意这是");
        }
    }

    public static QRCode genQRCode() throws IOException {
        String key = fetch(host + "/login/qr/key");
        JsonObject keyObj = DataUtil.gson.fromJson(key, JsonObject.class);
        String uniqueKey = keyObj.get("data").getAsJsonObject().get("unikey").getAsString();

        String code = fetch(host + "/login/qr/create?key=" + uniqueKey + "&qrimg=true&timestamp=" + System.currentTimeMillis());
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
        String response = fetch(host + "/login/qr/check?key=" + key + "&timestamp=" + System.currentTimeMillis());
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
        System.out.println(fetch(host + "/user/account"));
    }
}
