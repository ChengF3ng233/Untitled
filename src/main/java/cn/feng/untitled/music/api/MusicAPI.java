package cn.feng.untitled.music.api;

import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.music.api.base.LyricChar;
import cn.feng.untitled.music.api.base.LyricLine;
import cn.feng.untitled.music.api.base.Music;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.api.user.QRCode;
import cn.feng.untitled.music.api.user.QRCodeState;
import cn.feng.untitled.music.api.user.ScanResult;
import cn.feng.untitled.music.api.user.User;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            BufferedImage coverData = HttpUtil.downloadImage(obj.get("coverImgUrl").getAsString(), 300, 300);
            try {
                assert coverData != null;
                file.createNewFile();
                ImageIO.write(coverData, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
                    obj.get("dt").getAsInt(),
                    file,
                    obj.get("fee").getAsInt() == 0
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
                    obj.get("dt").getAsInt(),
                    file,
                    obj.get("fee").getAsInt() == 0
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

            BufferedImage coverData = HttpUtil.downloadImage(obj.get("picUrl").getAsString(), 300, 300);
            try {
                assert coverData != null;
                file.createNewFile();
                ImageIO.write(coverData, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
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

            BufferedImage coverData = HttpUtil.downloadImage(obj.get("picUrl").getAsString(), 300, 300);
            try {
                assert coverData != null;
                file.createNewFile();
                ImageIO.write(coverData, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    public static List<LyricLine> getLyrics(long id) {
        String fetch = fetch("/lyric/new?id=" + id);
        JsonObject response = DataUtil.gson.fromJson(fetch, JsonObject.class);
        boolean isNew = response.has("yrc");
        String lyricCollection = response.getAsJsonObject().get(isNew ? "yrc" : "lrc").getAsJsonObject().get("lyric").getAsString();
        List<String> lines = new ArrayList<>(Arrays.stream(lyricCollection.split("\n")).toList());
        lines.removeIf(String::isEmpty);

        List<LyricLine> lyrics = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("{")) continue;
            if (isNew) {
                // 正则表达式匹配整行的时间戳和持续时间
                List<LyricChar> chars = new ArrayList<>();

                // 正则表达式匹配每个字符的时间戳、持续时间和字符内容
                Pattern charPattern = Pattern.compile("\\((\\d+),(\\d+),\\d+\\)([^()]+)");
                Matcher charMatcher = charPattern.matcher(line);

                while (charMatcher.find()) {
                    int charStartTime = Integer.parseInt(charMatcher.group(1));
                    int charDuration = Integer.parseInt(charMatcher.group(2));
                    String character = charMatcher.group(3);
                    chars.add(new LyricChar(charStartTime, charDuration, character));
                }

                Pattern linePattern = Pattern.compile("\\[(\\d+),(\\d+)]");
                Matcher lineMatcher = linePattern.matcher(line);

                if (lineMatcher.find()) {
                    int lineStartTime = Integer.parseInt(lineMatcher.group(1));
                    int lineDuration = Integer.parseInt(lineMatcher.group(2));
                    lyrics.add(new LyricLine(lineStartTime, lineDuration, chars, false));
                }
            } else {
                Pattern pattern = Pattern.compile("\\[(\\d{2}):(\\d{2})\\.(\\d{2})]\\s*(.*)");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    // 提取时间戳部分
                    int minutes = Integer.parseInt(matcher.group(1)); // 分钟
                    int seconds = Integer.parseInt(matcher.group(2)); // 秒
                    int centiseconds = Integer.parseInt(matcher.group(3)); // 厘秒

                    // 将时间戳转换为毫秒
                    int timestampInMilliseconds = (minutes * 60 * 1000) + (seconds * 1000) + (centiseconds * 10);

                    // 提取歌词内容
                    String lyric = matcher.group(4);

                    LyricChar lyricChar = new LyricChar(timestampInMilliseconds, -1, lyric);
                    lyrics.add(new LyricLine(timestampInMilliseconds, -1, Arrays.stream(new LyricChar[]{lyricChar}).toList(), false));
                }
            }
        }
        return lyrics;
    }

    public static List<LyricLine> getTranslatedLyrics(long id) {
        String fetch = fetch("/lyric/new?id=" + id);
        JsonObject response = DataUtil.gson.fromJson(fetch, JsonObject.class);
        boolean isNew = response.has("ytlrc");
        System.out.println(fetch);
        JsonElement je = response.getAsJsonObject().get(isNew ? "ytlrc" : "tlyric");
        if (je instanceof JsonNull || je == null) return Collections.emptyList();
        String lyricCollection = je.getAsJsonObject().get("lyric").getAsString();
        List<String> lines = new ArrayList<>(Arrays.stream(lyricCollection.split("\n")).toList());
        lines.removeIf(String::isEmpty);

        List<LyricLine> lyrics = new ArrayList<>();

        for (String line : lines) {
            Pattern pattern = Pattern.compile(isNew? "\\[(\\d{2}):(\\d{2})\\.(\\d{3})]\\s*(.*)" : "\\[(\\d{2}):(\\d{2})\\.(\\d{2})]\\s*(.*)");
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                // 提取时间戳部分
                int minutes = Integer.parseInt(matcher.group(1)); // 分钟
                int seconds = Integer.parseInt(matcher.group(2)); // 秒
                int centiseconds = Integer.parseInt(matcher.group(3)); // 厘秒

                // 将时间戳转换为毫秒
                int startTime = (minutes * 60 * 1000) + (seconds * 1000) + (centiseconds * (isNew? 1 : 10));

                // 提取歌词内容
                String lyric = matcher.group(4);

                LyricChar lyricChar = new LyricChar(startTime, -1, lyric);
                lyrics.add(new LyricLine(startTime, -1, Arrays.stream(new LyricChar[]{lyricChar}).toList(), true));
            }
        }

        return lyrics;
    }

    public static String getMusicURL(long id) {
        String fetch = fetch("/song/url?id=" + id);
        System.out.println(fetch);
        for (JsonElement data : DataUtil.gson.fromJson(fetch, JsonObject.class).get("data").getAsJsonArray()) {
            // 获取第一个，因为就一个
            return data.getAsJsonObject().get("url").getAsString();
        }
        return null;
    }

    public static PlayList search(String keywords) {
        String fetch = fetch("/search?keywords=" + keywords);
        PlayList playList = new PlayList("搜索结果：" + keywords, "你刚搜的歌");
        JsonArray songs = DataUtil.gson.fromJson(fetch, JsonObject.class).get("result").getAsJsonObject().get("songs").getAsJsonArray();
        StringBuilder ids = new StringBuilder();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            StringBuilder artistStr = new StringBuilder();
            for (JsonElement ar : obj.get("artists").getAsJsonArray()) {
                JsonObject artist = ar.getAsJsonObject();
                artistStr.append((!artistStr.isEmpty()) ? ", " + artist.get("name").getAsString() : artist.get("name").getAsString());
            }
            playList.getMusicList().add(new Music(
                    obj.get("name").getAsString(),
                    artistStr.toString(),
                    obj.get("album").getAsJsonObject().get("name").getAsString(),
                    obj.get("id").getAsLong(),
                    obj.get("duration").getAsInt(),
                    obj.get("fee").getAsInt() == 0
            ));
            ids.append(ids.isEmpty()? obj.get("id").getAsLong() : "," + obj.get("id").getAsLong());
        }
        Map<Long, File> map = getSongCovers(ids.toString());
        for (Music music : playList.getMusicList()) {
            if (map.containsKey(music.getId())) {
                music.setCoverImage(map.get(music.getId()));
                playList.setCoverImage(map.get(music.getId()));
            }
        }

        return playList;
    }

    public static Map<Long, File> getSongCovers(String ids) {
        String fetch = fetch("/song/detail?ids=" + ids);
        JsonArray songs = DataUtil.gson.fromJson(fetch, JsonObject.class).get("songs").getAsJsonArray();
        Map<Long, File> result = new HashMap<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
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
            result.put(obj.get("id").getAsLong(), file);
        }
        return result;
    }
}
