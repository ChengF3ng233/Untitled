package cn.feng.untitled.music.api;

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
        Logger.info("Generating QR code...");
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
        Logger.info("Checking scan result...");
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
        Logger.info("Updating user info...");
        JsonObject responseData = fetchObject("/login/status?timestamp=" + System.currentTimeMillis()).get("data").getAsJsonObject();
        JsonObject profile = responseData.get("profile").getAsJsonObject();
        user.setUid(profile.get("userId").getAsString());
        user.setNickname(profile.get("nickname").getAsString());
        String avatarUrl = profile.get("avatarUrl").getAsString();
        user.setAvatarUrl(avatarUrl);
        user.setAvatarTexture(new DynamicTexture(HttpUtil.downloadImage(avatarUrl)));
    }

    public static List<PlayList> getUserPlayLists() {
        Logger.info("Getting user playlists...");
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
        Logger.info("Getting daily songs..");
        JsonArray songs = fetchObject("/recommend/songs").get("data").getAsJsonObject().get("dailySongs").getAsJsonArray();
        PlayList playList = new PlayList("每日推荐", "根据你的喜好，每日生成的推荐歌曲");
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            StringBuilder artistStr = new StringBuilder();
            for (JsonElement ar : obj.get("ar").getAsJsonArray()) {
                JsonObject artist = ar.getAsJsonObject();
                artistStr.append((!artistStr.isEmpty()) ? ", " + artist.get("name").getAsString() : artist.get("name").getAsString());
            }
            File file = new File(ConfigManager.coverDir, "music_" + obj.get("id").getAsLong() + ".jpg");
            downloadImage(obj.get("al").getAsJsonObject().get("picUrl").getAsString(), file, false);
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
        playList.setId(-1);
        return playList;
    }

    public static List<Music> fetchMusicList(PlayList playList) {
        Logger.info("Fetching music list for id [" + playList.getId() + "], offset: " + playList.getMusicList().size());
        JsonArray songs = fetchObject("/playlist/track/all?id=" + playList.getId() + "&limit=10&offset=" + playList.getMusicList().size()).get("songs").getAsJsonArray();

        if (songs.isEmpty()) {
            playList.setCompletelyDownloaded(true);
            Logger.info("Playlist [" + playList.getId() + "] is completely downloaded");
            return Collections.emptyList();
        }

        List<Music> musics = new ArrayList<>();
        for (JsonElement song : songs) {
            JsonObject obj = song.getAsJsonObject();
            StringBuilder artistStr = new StringBuilder();
            for (JsonElement ar : obj.get("ar").getAsJsonArray()) {
                JsonObject artist = ar.getAsJsonObject();
                artistStr.append((!artistStr.isEmpty()) ? ", " + artist.get("name").getAsString() : artist.get("name").getAsString());
            }

            File file = new File(ConfigManager.coverDir, "music_" + obj.get("id").getAsLong() + ".jpg");

            downloadImage(obj.get("al").getAsJsonObject().get("picUrl").getAsString(), file, false);

            Music music = new Music(
                    obj.get("name").getAsString(),
                    artistStr.toString(),
                    obj.get("al").getAsJsonObject().get("name").getAsString(),
                    obj.get("id").getAsLong(),
                    obj.get("dt").getAsInt(),
                    file,
                    obj.get("fee").getAsInt() == 0);

            playList.getMusicList().add(music);
            musics.add(music);

            if (playList.getCoverImage() == null) {
                playList.setCoverImage(file);
            }
        }
        return musics;
    }

    public static List<PlayList> getRecommendedPlayLists() {
        Logger.info("Getting recommended playlists...");
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
        Logger.info("Getting lyrics for id [" + id + "]");
        JsonObject response = fetchObject("/lyric/new?id=" + id);
        boolean isNew = response.has("yrc");
        String lyricCollection = response.get(isNew ? "yrc" : "lrc").getAsJsonObject().get("lyric").getAsString();
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
                } else {
                    pattern = Pattern.compile("\\[(\\d{2}):(\\d{2})\\.(\\d{3})]\\s*(.*)");
                    matcher = pattern.matcher(line);

                    if (matcher.find()) {
                        // 提取时间戳部分
                        int minutes = Integer.parseInt(matcher.group(1)); // 分钟
                        int seconds = Integer.parseInt(matcher.group(2)); // 秒
                        int ms = Integer.parseInt(matcher.group(3)); // 豪秒

                        // 将时间戳转换为毫秒
                        int timestampInMilliseconds = (minutes * 60 * 1000) + (seconds * 1000) + (ms);

                        // 提取歌词内容
                        String lyric = matcher.group(4);

                        LyricChar lyricChar = new LyricChar(timestampInMilliseconds, -1, lyric);
                        lyrics.add(new LyricLine(timestampInMilliseconds, -1, Arrays.stream(new LyricChar[]{lyricChar}).toList(), false));
                    }
                }
            }
        }

        boolean newTranslate = response.has("ytlrc");
        JsonElement je = response.getAsJsonObject().get(newTranslate ? "ytlrc" : "tlyric");
        List<LyricLine> translatedLyrics = new ArrayList<>();
        if (!(je instanceof JsonNull) && je != null)  {
            String transCollection = je.getAsJsonObject().get("lyric").getAsString();
            List<String> translates = new ArrayList<>(Arrays.stream(transCollection.split("\n")).toList());
            translates.removeIf(String::isEmpty);

            for (String line : translates) {
                Pattern pattern = Pattern.compile(newTranslate? "\\[(\\d{2}):(\\d{2})\\.(\\d{3})]\\s*(.*)" : "\\[(\\d{2}):(\\d{2})\\.(\\d{2})]\\s*(.*)");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    // 提取时间戳部分
                    int minutes = Integer.parseInt(matcher.group(1)); // 分钟
                    int seconds = Integer.parseInt(matcher.group(2)); // 秒
                    int centiseconds = Integer.parseInt(matcher.group(3)); // 厘秒

                    // 将时间戳转换为毫秒
                    int startTime = (minutes * 60 * 1000) + (seconds * 1000) + (centiseconds * (newTranslate? 1 : 10));

                    // 提取歌词内容
                    String lyric = matcher.group(4);

                    LyricChar lyricChar = new LyricChar(startTime, -1, lyric);
                    translatedLyrics.add(new LyricLine(startTime, -1, Arrays.stream(new LyricChar[]{lyricChar}).toList(), true));
                }
            }
        }
        return new LyricPair(lyrics, translatedLyrics);
    }

    public static String getMusicURL(long id, boolean retry) {
        try {
            for (JsonElement data : fetchObject("/song/url/v1?id=" + id + "&level=" + (retry? "standard" : "exhigh")).get("data").getAsJsonArray()) {
                // 获取第一个，因为就一个
                return data.getAsJsonObject().get("url").getAsString();
            }
        } catch (JsonSyntaxException e) {
            if (retry) throw new NullPointerException("No music source [" + id + "].");
            Logger.error("Failed to get exhigh music [" + id + "], retry...");
            return getMusicURL(id, true);
        }
        return null;
    }

    public static PlayList search(String keywords) {
        Logger.info("Searching keywords [" + keywords + "]");
        PlayList playList = new PlayList("搜索结果：" + keywords, "你刚搜的歌");
        JsonArray songs = fetchObject("/search?keywords=" + keywords + "&limit=10").get("result").getAsJsonObject().get("songs").getAsJsonArray();
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
                music.setCoverFile(map.get(music.getId()));
                if (playList.getCoverImage() == null) {
                    playList.setCoverImage(map.get(music.getId()));
                }
            } else {
                music.setCoverFile(MusicAPI.user.getAvatarFile());
            }
        }
        playList.setId(-1);
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
