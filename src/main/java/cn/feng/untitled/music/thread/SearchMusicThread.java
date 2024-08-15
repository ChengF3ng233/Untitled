package cn.feng.untitled.music.thread;

import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.ui.MusicPlayerScreen;
import cn.feng.untitled.music.ui.gui.MusicPlayerGUI;
import cn.feng.untitled.music.ui.gui.impl.PlayListGUI;
import lombok.Getter;

/**
 * @author ChengFeng
 * @since 2024/8/16
 **/
public class SearchMusicThread extends Thread {
    private final MusicPlayerScreen parent;
    @Getter
    private MusicPlayerGUI gui;

    public SearchMusicThread(MusicPlayerScreen parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        gui = new PlayListGUI(MusicAPI.search(parent.getSearchField().text), parent.getCurrentGUI());
    }
}
