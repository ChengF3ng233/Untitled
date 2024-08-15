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

        PlayList toRemove = null;
        for (PlayList playList : userPlayLists) {
            if (playList.getName().equalsIgnoreCase(MusicAPI.user.getNickname() + "喜欢的音乐")) {
                PlayListGUI gui1 = (PlayListGUI) Client.instance.musicManager.screen.categoryButtons.get(1).getGui();
                gui1.setPlayList(playList);
                new FetchPlayListThread(playList, gui1.buttons).start();
                toRemove = playList;
            }
        }
        if (toRemove != null) userPlayLists.remove(toRemove);


        List<PlayList> playLists = new ArrayList<>(userPlayLists);
        playLists.add(dailySongs);
        playLists.addAll(recommendedPlayLists);

        HashSet<PlayList> playLists1 = new HashSet<>(playLists);
        new ArrayList<>(playLists1).forEach(gui::addPlayList);
    }
}
