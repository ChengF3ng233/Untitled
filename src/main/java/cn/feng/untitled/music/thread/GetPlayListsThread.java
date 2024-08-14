package cn.feng.untitled.music.thread;

import cn.feng.untitled.Client;
import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.PlayList;
import cn.feng.untitled.music.ui.gui.impl.PlayListGUI;
import cn.feng.untitled.music.ui.gui.impl.PlayListListGUI;

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
        List<PlayList> playLists = MusicAPI.getUserPlayLists();
        List<PlayList> recommendedPlayLists = MusicAPI.getRecommendedPlayLists();
        List<PlayList> personalizedPlayLists = MusicAPI.getPersonalizedPlayLists();

        PlayList toRemove = null;
        for (PlayList playList : playLists) {
            if (playList.getName().equalsIgnoreCase(MusicAPI.user.getNickname() + "喜欢的音乐")) {
                PlayListGUI gui = (PlayListGUI) Client.instance.musicManager.screen.categoryButtons.get(1).getGui();
                gui.setPlayList(playList);
                new FetchPlayListThread(playList, gui.buttons).start();
                toRemove = playList;
            }
        }
        if (toRemove != null) playLists.remove(toRemove);

        PlayListListGUI gui = (PlayListListGUI) Client.instance.musicManager.screen.categoryButtons.get(0).getGui();

        playLists.forEach(gui::addPlayList);
        gui.addPlayList(MusicAPI.getDailySongs());
        recommendedPlayLists.forEach(gui::addPlayList);
        personalizedPlayLists.forEach(gui::addPlayList);
    }
}
