package cn.feng.untitled.music.api;

import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.util.data.DataUtil;
import cn.feng.untitled.util.data.HttpUtil;
import cn.feng.untitled.util.misc.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.texture.DynamicTexture;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class MusicAPI {
    public static final String host = "https://neteasecloudmusicapi.vercel.app";
    public static User user = new User();

    public static String fetch(String api, String cookie) {
        Logger.info("Fetching " + api + " using cookie: " + cookie);
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = new FormBody.Builder()
                .add("cookie", cookie)
                .build();

        Request request = new Request.Builder()
                .url(host + api)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException("什么玩意这是");
        }
    }

    private static String fetch(String api) {
        String result = null;
        do {
            try {
                result = fetch(api, user.getCookie());
            } catch (Exception e) {
                Logger.error("Failed to fetch " + api + ". Retry...");
            }
        } while (result == null);
        return result;
    }

    public static QRCode genQRCode() throws IOException {
        String key = fetch("/login/qr/key?timestamp=" + System.currentTimeMillis());
        JsonObject keyObj = DataUtil.gson.fromJson(key, JsonObject.class);
        String uniqueKey = keyObj.get("data").getAsJsonObject().get("unikey").getAsString();

        String code = fetch("/login/qr/create?key=" + uniqueKey + "&qrimg=true&timestamp=" + System.currentTimeMillis());
        JsonObject codeObj = DataUtil.gson.fromJson(code, JsonObject.class);
        String base64String = codeObj.get("data").getAsJsonObject().get("qrimg").getAsString();

        String base64Image = base64String.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);
        bis.close();

        return new QRCode(image, uniqueKey);
    }

    public static ScanResult getScanResult(String key) {
        String response = fetch("/login/qr/check?key=" + key + "&timestamp=" + System.currentTimeMillis());
        JsonObject object = DataUtil.gson.fromJson(response, JsonObject.class);
        int code = object.get("code").getAsInt();

        QRCodeState state = switch (code) {
            case 801 -> QRCodeState.WAITING_SCAN;
            case 802 -> QRCodeState.WAITING_CONFIRM;
            case 803 -> QRCodeState.SUCCEED;
            default -> QRCodeState.EXPIRED;
        };

        return new ScanResult(state, object);
    }

    public static void updateUserInfo() {
        String fetch = fetch("/login/status?timestamp=" + System.currentTimeMillis());
        JsonObject responseData = DataUtil.gson.fromJson(fetch, JsonObject.class).get("data").getAsJsonObject();
        JsonObject profile = responseData.get("profile").getAsJsonObject();
        user.setUid(profile.get("userId").getAsString());
        user.setNickname(profile.get("nickname").getAsString());
        String avatarUrl = profile.get("avatarUrl").getAsString();
        user.setAvatarUrl(avatarUrl);
        user.setAvatarTexture(new DynamicTexture(HttpUtil.downloadImage(avatarUrl, 200, 200)));
    }

    public static List<PlayList> getUserPlayLists() {
        String fetch = fetch("/user/playlist?uid=" + user.getUid());
        JsonArray playlistArray = DataUtil.gson.fromJson(fetch, JsonObject.class).get("playlist").getAsJsonArray();
        List<PlayList> result = new ArrayList<>();
        for (JsonElement element : playlistArray) {
            JsonObject obj = element.getAsJsonObject();
            String description = obj.get("description") instanceof JsonNull ? "没有描述，你自己进去看看" : obj.get("description").getAsString();
            File file = new File(ConfigManager.cacheDir, "playlist_" + obj.get("id").getAsLong() + ".jpg");
            if (!file.exists()) {
                BufferedImage coverData = HttpUtil.downloadImage(obj.get("coverImgUrl").getAsString(), 300, 300);
                try {
                    assert coverData != null;
                    file.createNewFile();
                    ImageIO.write(coverData, "jpg", file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            result.add(new PlayList(
                    obj.get("name").getAsString(),
                    description,
                    obj.get("id").getAsLong(),
                    file
            ));
        }
        return result;
    }

    public static PlayList getDailySongs() {
        String fetch = MusicAPI.fetch("/recommend/songs");
        JsonArray songs = DataUtil.gson.fromJson(fetch, JsonObject.class).get("data").getAsJsonObject().get("dailySongs").getAsJsonArray();
        PlayList playList = new PlayList("每日推荐", "根据你的喜好，每日生成的推荐歌曲");
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            StringBuilder artistStr = new StringBuilder();
            for (JsonElement ar : obj.get("ar").getAsJsonArray()) {
                JsonObject artist = ar.getAsJsonObject();
                artistStr.append((!artistStr.isEmpty()) ? ", " + artist.get("name").getAsString() : artist.get("name").getAsString());
            }
            File file = new File(ConfigManager.cacheDir, "music_" + obj.get("id").getAsLong() + ".jpg");
            if (!file.exists()) {
                BufferedImage coverData = HttpUtil.downloadImage(obj.get("al").getAsJsonObject().get("picUrl").getAsString(), 300, 300);
                try {
                    assert coverData != null;
                    file.createNewFile();
                    ImageIO.write(coverData, "jpg", file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            playList.getMusicList().add(new Music(
                    obj.get("name").getAsString(),
                    artistStr.toString(),
                    obj.get("al").getAsJsonObject().get("name").getAsString(),
                    obj.get("id").getAsLong(),
                    obj.get("dt").getAsLong(),
                    file
            ));
            if (playList.getCoverImage() == null) {
                playList.setCoverImage(file);
            }
        }
        return playList;
    }

    public static void fetchMusicList(PlayList playList, int offset) {
        String fetch = fetch("/playlist/track/all?id=" + playList.getId() + "&limit=10&offset=" + offset);
        JsonArray songs = DataUtil.gson.fromJson(fetch, JsonObject.class).get("songs").getAsJsonArray();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            StringBuilder artistStr = new StringBuilder();
            for (JsonElement ar : obj.get("ar").getAsJsonArray()) {
                JsonObject artist = ar.getAsJsonObject();
                artistStr.append((!artistStr.isEmpty()) ? ", " + artist.get("name").getAsString() : artist.get("name").getAsString());
            }
            File file = new File(ConfigManager.cacheDir, "music_" + obj.get("id").getAsLong() + ".jpg");
            if (!file.exists()) {
                BufferedImage coverData = HttpUtil.downloadImage(obj.get("al").getAsJsonObject().get("picUrl").getAsString(), 300, 300);
                try {
                    assert coverData != null;
                    file.createNewFile();
                    ImageIO.write(coverData, "jpg", file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            playList.getMusicList().add(new Music(
                    obj.get("name").getAsString(),
                    artistStr.toString(),
                    obj.get("al").getAsJsonObject().get("name").getAsString(),
                    obj.get("id").getAsLong(),
                    obj.get("dt").getAsLong(),
                    file
            ));
            if (playList.getCoverImage() == null) {
                playList.setCoverImage(file);
            }
        }
    }

    public static List<PlayList> getPersonalizedPlayLists() {
        String fetch = fetch("/personalized");
        JsonArray playlistArray = DataUtil.gson.fromJson(fetch, JsonObject.class).get("result").getAsJsonArray();

        List<PlayList> result = new ArrayList<>();
        for (JsonElement element : playlistArray) {
            JsonObject obj = element.getAsJsonObject();
            String description = (obj.get("description") instanceof JsonNull || obj.get("description") == null) ? "没有描述，你自己进去看看" : obj.get("description").getAsString();
            File file = new File(ConfigManager.cacheDir, "playlist_" + obj.get("id").getAsLong() + ".jpg");
            if (!file.exists()) {
                BufferedImage coverData = HttpUtil.downloadImage(obj.get("picUrl").getAsString(), 300, 300);
                try {
                    assert coverData != null;
                    file.createNewFile();
                    ImageIO.write(coverData, "jpg", file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            result.add(new PlayList(
                    obj.get("name").getAsString(),
                    description,
                    obj.get("id").getAsLong(),
                    file
            ));
        }

        return result;
    }

    public static List<PlayList> getRecommendedPlayLists() {
        String fetch = fetch("/recommend/resource");
        JsonArray playlistArray = DataUtil.gson.fromJson(fetch, JsonObject.class).get("recommend").getAsJsonArray();

        List<PlayList> result = new ArrayList<>();
        for (JsonElement element : playlistArray) {
            JsonObject obj = element.getAsJsonObject();
            String description = (obj.get("description") instanceof JsonNull || obj.get("description") == null) ? "没有描述，你自己进去看看" : obj.get("description").getAsString();
            File file = new File(ConfigManager.cacheDir, "playlist_" + obj.get("id").getAsLong() + ".jpg");
            if (!file.exists()) {
                BufferedImage coverData = HttpUtil.downloadImage(obj.get("picUrl").getAsString(), 300, 300);
                try {
                    assert coverData != null;
                    file.createNewFile();
                    ImageIO.write(coverData, "jpg", file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            result.add(new PlayList(
                    obj.get("name").getAsString(),
                    description,
                    obj.get("id").getAsLong(),
                    file
            ));
        }


        return result;
    }

    public static String getMusicURL(long id) {
        String fetch = fetch("/song/url/v1?id=" + id + "&level=exhigh");
        for (JsonElement data : DataUtil.gson.fromJson(fetch, JsonObject.class).get("data").getAsJsonArray()) {
            // 获取第一个，因为就一个
            return data.getAsJsonObject().get("url").getAsString();
        }
        return null;
    }
}
