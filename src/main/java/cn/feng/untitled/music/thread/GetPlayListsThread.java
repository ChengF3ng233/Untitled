package cn.feng.untitled.music.thread;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.base.PlayList;
import cn.feng.untitled.music.ui.gui.impl.PlayListGUI;
import cn.feng.untitled.music.ui.gui.impl.PlayListListGUI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
public class GetPlayListsThread extends Thread {
    public GetPlayListsThread() {
        setName("Music-GetPlayLists");
    }

    @Override
    public void run() {
        PlayListListGUI gui = (PlayListListGUI) Client.instance.musicManager.screen.categoryButtons.get(0).getGui();

        List<PlayList> userPlayLists = MusicAPI.getUserPlayLists();
        List<PlayList> recommendedPlayLists = MusicAPI.getRecommendedPlayLists();
        PlayList dailySongs = MusicAPI.getDailySongs();

        PlayList likedList = null;
        for (PlayList playList : userPlayLists) {
            if (playList.getName().equalsIgnoreCase(MusicAPI.user.getNickname() + "喜欢的音乐")) {
                PlayListGUI likedGUI = (PlayListGUI) Client.instance.musicManager.screen.categoryButtons.get(1).getGui();
                new FetchMusicsThread(playList, likedGUI.buttons).start();
                likedGUI.setPlayList(playList);
                likedList = playList;
                break;
            }
        }
        if (likedList != null) userPlayLists.remove(likedList);

        // 除去id重复的歌单
        List<PlayList> allLists = new ArrayList<>(userPlayLists);
        allLists.add(dailySongs);
        allLists.addAll(recommendedPlayLists);

        HashSet<PlayList> set = new HashSet<>(allLists);
        new ArrayList<>(set).forEach(gui::addPlayList);
    }
}
