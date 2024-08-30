package cn.feng.untitled.music.api.user;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.io.File;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/

@Setter
@Getter
public class User {
    private String uid;
    private String nickname;
    private transient String tempNickname;
    private String cookie = "";
    private String avatarUrl;
    private boolean loggedIn = false;
    private transient int avatarTexture = 0;
    private transient int tempAvatarTexture = 0;
}
