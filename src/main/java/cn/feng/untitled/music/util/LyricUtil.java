package cn.feng.untitled.music.util;

import cn.feng.untitled.music.api.base.LyricChar;
import cn.feng.untitled.music.api.base.LyricLine;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ChengFeng
 * @since 2024/8/22
 **/
public class LyricUtil {
    public static List<LyricLine> parseLyrics(JsonObject response) {
        boolean isNewLyric = response.has("yrc");

        String allLyrics = response.get(isNewLyric ? "yrc" : "lrc")
                .getAsJsonObject()
                .get("lyric")
                .getAsString();

        List<String> lines = Arrays.stream(allLyrics.split("\n"))
                .filter(line -> !line.isEmpty() && !line.startsWith("{"))
                .toList();

        List<LyricLine> lyricLines = new ArrayList<>();
        for (String line : lines) {
            lyricLines.add(isNewLyric? parseNewLyricLine(line) : parseOldLyricLine(line));
        }

        return lyricLines;
    }

    private static LyricLine parseNewLyricLine(String line) {
        List<LyricChar> chars = new ArrayList<>();
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
            return new LyricLine(lineStartTime, lineDuration, chars, false);
        }

        return null;
    }

    /**
     * 老歌词没有单行时长，暂时设置为-1
     * @param line 歌词
     * @return 包装过的歌词
     */
    private static LyricLine parseOldLyricLine(String line) {
        Pattern pattern = Pattern.compile("\\[(\\d{2}):(\\d{2})\\.(\\d{2,3})]\\s*(.*)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            int minutes = Integer.parseInt(matcher.group(1));
            int seconds = Integer.parseInt(matcher.group(2));
            int fraction = Integer.parseInt(matcher.group(3));
            int startTime = (minutes * 60 * 1000) + (seconds * 1000) + (matcher.group(3).length() == 3 ? fraction : fraction * 10);

            String lyric = matcher.group(4);

            List<LyricChar> chars = lyric.isEmpty() ? List.of(new LyricChar(-1, -1, "[Music]"))
                    : Arrays.stream(lyric.split(""))
                    .map(str -> new LyricChar(-1, -1, str))
                    .toList();
            return new LyricLine(startTime, -1, chars, false);
        }

        return null;
    }

    public static List<LyricLine> parseTranslatedLyrics(JsonObject response) {
        boolean isNewTranslate = response.has("ytlrc");
        JsonElement je = response.get(isNewTranslate ? "ytlrc" : "tlyric");

        if (!(je instanceof JsonNull) && je != null) {
            String transCollection = je.getAsJsonObject().get("lyric").getAsString();
            return Arrays.stream(transCollection.split("\n"))
                    .filter(line -> !line.isEmpty())
                    .map(line -> parseTranslatedLyricLine(line, isNewTranslate))
                    .toList();
        }

        return new ArrayList<>();
    }

    private static LyricLine parseTranslatedLyricLine(String line, boolean newTranslate) {
        Pattern pattern = Pattern.compile(newTranslate ? "\\[(\\d{2}):(\\d{2})\\.(\\d{3})]\\s*(.*)"
                : "\\[(\\d{2}):(\\d{2})\\.(\\d{2})]\\s*(.*)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            int minutes = Integer.parseInt(matcher.group(1));
            int seconds = Integer.parseInt(matcher.group(2));
            int fraction = Integer.parseInt(matcher.group(3));
            int startTime = (minutes * 60 * 1000) + (seconds * 1000) + (newTranslate ? fraction : fraction * 10);

            String lyric = matcher.group(4);
            LyricChar lyricChar = new LyricChar(startTime, -1, lyric);
            return new LyricLine(startTime, -1, List.of(lyricChar), true);
        }

        return null;
    }
}
