package cn.feng.untitled.music.api;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.music.api.base.*;
import cn.feng.untitled.music.api.user.QRCode;
import cn.feng.untitled.music.api.user.QRCodeState;
import cn.feng.untitled.music.api.user.ScanResult;
import cn.feng.untitled.music.api.user.User;
import cn.feng.untitled.util.data.DataUtil;
import cn.feng.untitled.util.data.HttpUtil;
import cn.feng.untitled.util.misc.Logger;
import com.google.gson.*;
import net.minecraft.client.renderer.texture.DynamicTexture;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.feng.untitled.music.util.LyricUtil.parseLyrics;
import static cn.feng.untitled.music.util.LyricUtil.parseTranslatedLyrics;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class MusicAPI {
    public static final String host = "http://localhost:3000";
    public static User user = new User();

    public static String fetch(String api, String cookie) {
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

    private static JsonObject fetchObject(String api) {
        String fetch = fetch(api);
        return DataUtil.gson.fromJson(fetch, JsonObject.class);
    }

    private static void downloadImage(String url, File file, boolean rewrite) {
        try {
            HttpUtil.downloadImage(url + "?param=300y300", file, rewrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static QRCode genQRCode() throws IOException {
        Logger.debug("Generating QR code...");
        JsonObject keyObj = fetchObject("/login/qr/key?timestamp=" + System.currentTimeMillis());
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
        Logger.debug("Checking scan result...");
        JsonObject object = fetchObject("/login/qr/check?key=" + key + "&timestamp=" + System.currentTimeMillis());
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
        Logger.debug("Updating user info...");
        JsonObject responseData = fetchObject("/login/status?timestamp=" + System.currentTimeMillis()).get("data").getAsJsonObject();
        JsonObject profile = responseData.get("profile").getAsJsonObject();
        user.setUid(profile.get("userId").getAsString());
        user.setNickname(profile.get("nickname").getAsString());
        String avatarUrl = profile.get("avatarUrl").getAsString();
        user.setAvatarUrl(avatarUrl);
        user.setAvatarTexture(new DynamicTexture(HttpUtil.downloadImage(avatarUrl)));
    }

    public static List<PlayList> getUserPlayLists() {
        Logger.debug("Getting user playlists...");
        JsonArray playlistArray = fetchObject("/user/playlist?uid=" + user.getUid()).get("playlist").getAsJsonArray();
        List<PlayList> result = new ArrayList<>();
        for (JsonElement element : playlistArray) {
            JsonObject obj = element.getAsJsonObject();
            String description = obj.get("description") instanceof JsonNull ? "没有描述，你自己进去看看" : obj.get("description").getAsString();
            File file = new File(ConfigManager.coverDir, "playlist_" + obj.get("id").getAsLong() + ".jpg");

            downloadImage(obj.get("coverImgUrl").getAsString(), file, true);

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
        Logger.debug("Getting daily songs..");
        JsonArray songs = fetchObject("/recommend/songs").get("data").getAsJsonObject().get("dailySongs").getAsJsonArray();
        PlayList playList = new PlayList("每日推荐", "根据你的喜好，每日生成的推荐歌曲");
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            JsonObject privilege = obj.get("privilege").getAsJsonObject();

            Music music;
            if (Client.instance.musicManager.musicMap.containsKey(obj.get("id").getAsLong())) {
                music = Client.instance.musicManager.musicMap.get(obj.get("id").getAsLong());
            } else {
                StringBuilder artistStr = new StringBuilder();
                for (JsonElement ar : obj.get("ar").getAsJsonArray()) {
                    JsonObject artist = ar.getAsJsonObject();
                    artistStr.append((!artistStr.isEmpty()) ? ", " + artist.get("name").getAsString() : artist.get("name").getAsString());
                }
                File file = new File(ConfigManager.coverDir, "music_" + obj.get("id").getAsLong() + ".jpg");
                downloadImage(obj.get("al").getAsJsonObject().get("picUrl").getAsString(), file, false);
                music = new Music(
                        obj.get("name").getAsString(),
                        artistStr.toString(),
                        obj.get("al").getAsJsonObject().get("name").getAsString(),
                        obj.get("id").getAsLong(),
                        obj.get("dt").getAsInt(),
                        file
                );
                music.setFree(!("none").equals(privilege.get("flLevel").getAsString()));
                music.setQuality(MusicQuality.getQuality(privilege.get("dlLevel").getAsString()));

                Client.instance.musicManager.musicMap.put(music.getId(), music);
            }

            playList.getMusicList().add(music);
            if (playList.getCoverImage() == null) {
                playList.setCoverImage(music.getCoverFile());
            }
        }
        playList.setId(-1);
        playList.setCompletelyDownloaded(true);
        return playList;
    }

    public static List<Music> fetchMusicList(PlayList playList) {
        Logger.debug("Fetching music list for id [" + playList.getId() + "], offset: " + playList.getMusicList().size());
        JsonObject object = fetchObject("/playlist/track/all?id=" + playList.getId() + "&limit=10&offset=" + playList.getMusicList().size());
        JsonArray songs = object.get("songs").getAsJsonArray();
        JsonArray privileges = object.get("privileges").getAsJsonArray();

        if (songs.isEmpty()) {
            playList.setCompletelyDownloaded(true);
            Logger.debug("Playlist [" + playList.getId() + "] is completely downloaded");
            return Collections.emptyList();
        }

        List<Music> musics = new ArrayList<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();

            Music music;
            if (Client.instance.musicManager.musicMap.containsKey(obj.get("id").getAsLong())) {
                music = Client.instance.musicManager.musicMap.get(obj.get("id").getAsLong());
            } else {
                StringBuilder artistStr = new StringBuilder();
                for (JsonElement ar : obj.get("ar").getAsJsonArray()) {
                    JsonObject artist = ar.getAsJsonObject();
                    artistStr.append((!artistStr.isEmpty()) ? ", " + artist.get("name").getAsString() : artist.get("name").getAsString());
                }

                File file = new File(ConfigManager.coverDir, "music_" + obj.get("id").getAsLong() + ".jpg");

                downloadImage(obj.get("al").getAsJsonObject().get("picUrl").getAsString(), file, false);

                music = new Music(
                        obj.get("name").getAsString(),
                        artistStr.toString(),
                        obj.get("al").getAsJsonObject().get("name").getAsString(),
                        obj.get("id").getAsLong(),
                        obj.get("dt").getAsInt(),
                        file
                );

                Client.instance.musicManager.musicMap.put(obj.get("id").getAsLong(), music);
            }

            musics.add(music);

            if (playList.getCoverImage() == null) {
                playList.setCoverImage(music.getCoverFile());
            }
        }

        for (JsonElement e : privileges) {
            JsonObject privilege = e.getAsJsonObject();
            Music music = getMusicById(privilege.get("id").getAsLong(), musics);
            if (music == null) continue;
            music.setFree(!("none").equals(privilege.get("flLevel").getAsString()));
            music.setQuality(MusicQuality.getQuality(privilege.get("dlLevel").getAsString()));
        }

        playList.getMusicList().addAll(musics);

        return musics;
    }

    private static Music getMusicById(long id, List<Music> list) {
        for (Music music : list) {
            if (music.getId() == id) return music;
        }
        return null;
    }

    public static List<PlayList> getRecommendedPlayLists() {
        Logger.debug("Getting recommended playlists...");
        JsonArray playlistArray = fetchObject("/recommend/resource").get("recommend").getAsJsonArray();

        List<PlayList> result = new ArrayList<>();

        for (JsonElement element : playlistArray) {
            JsonObject obj = element.getAsJsonObject();
            String description = (obj.get("description") instanceof JsonNull || obj.get("description") == null) ? "没有描述，你自己进去看看" : obj.get("description").getAsString();
            File file = new File(ConfigManager.coverDir, "playlist_" + obj.get("id").getAsLong() + ".jpg");

            downloadImage(obj.get("picUrl").getAsString(), file, true);

            result.add(new PlayList(
                    obj.get("name").getAsString(),
                    description,
                    obj.get("id").getAsLong(),
                    file
            ));
        }


        return result;
    }

    public static LyricPair getLyrics(long id) {
        Logger.debug("Getting lyrics for id [" + id + "]");
        JsonObject response = fetchObject("/lyric/new?id=" + id);

        List<LyricLine> lyrics = parseLyrics(response);
        List<LyricLine> translatedLyrics = parseTranslatedLyrics(response);

        return new LyricPair(lyrics, translatedLyrics);
    }

    public static String getMusicURL(long id, boolean retry, MusicQuality quality) {
        try {
            for (JsonElement data : fetchObject("/song/url/v1?id=" + id + "&level=" + (retry? "standard" : quality.name())).get("data").getAsJsonArray()) {
                // 获取第一个，因为就一个
                return data.getAsJsonObject().get("url").getAsString();
            }
        } catch (JsonSyntaxException e) {
            if (retry) throw new NullPointerException("No music source [" + id + "].");
            Logger.error("Failed to get exhigh music [" + id + "], retry...");
            return getMusicURL(id, true, quality);
        }
        return null;
    }

    public static PlayList search(String keywords) {
        String sanitizedKeywords = keywords.replaceAll("[\\r\\n]", "");
        Logger.debug("Searching keywords [" + sanitizedKeywords + "]");
        PlayList playList = new PlayList("搜索结果：" + keywords, "你刚搜的歌");
        JsonArray songs = fetchObject("/search?keywords=" + keywords + "&limit=10").get("result").getAsJsonObject().get("songs").getAsJsonArray();
        StringBuilder ids = new StringBuilder();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();

            Music music;
            if (Client.instance.musicManager.musicMap.containsKey(obj.get("id").getAsLong())) {
                music = Client.instance.musicManager.musicMap.get(obj.get("id").getAsLong());
            } else {
                StringBuilder artistStr = new StringBuilder();
                for (JsonElement ar : obj.get("artists").getAsJsonArray()) {
                    JsonObject artist = ar.getAsJsonObject();
                    artistStr.append((!artistStr.isEmpty()) ? ", " + artist.get("name").getAsString() : artist.get("name").getAsString());
                }
                music = new Music(
                        obj.get("name").getAsString(),
                        artistStr.toString(),
                        obj.get("album").getAsJsonObject().get("name").getAsString(),
                        obj.get("id").getAsLong(),
                        obj.get("duration").getAsInt()
                );
                Client.instance.musicManager.musicMap.put(music.getId(), music);
            }

            playList.getMusicList().add(music);
            ids.append(ids.isEmpty()? obj.get("id").getAsLong() : "," + obj.get("id").getAsLong());
        }
        Map<Long, File> map = getSongCovers(ids.toString());
        for (Music music : playList.getMusicList()) {
            if (map.containsKey(music.getId())) {
                music.setCoverFile(map.get(music.getId()));
                if (playList.getCoverImage() == null) {
                    playList.setCoverImage(map.get(music.getId()));
                }
            }
        }
        playList.setId(-1);
        playList.setCompletelyDownloaded(true);
        return playList;
    }

    public static Map<Long, File> getSongCovers(String ids) {
        JsonArray songs = fetchObject("/song/detail?ids=" + ids).get("songs").getAsJsonArray();
        Map<Long, File> result = new HashMap<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            File file = new File(ConfigManager.coverDir, "music_" + obj.get("id").getAsLong() + ".jpg");
            downloadImage(obj.get("al").getAsJsonObject().get("picUrl").getAsString(), file, false);
            result.put(obj.get("id").getAsLong(), file);
        }
        return result;
    }
}
