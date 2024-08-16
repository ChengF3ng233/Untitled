package dev.tr7zw.entityculling;

import java.util.Arrays;
import java.util.HashSet;

import cn.feng.untitled.module.impl.client.EntityCullingMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;

public class EntityCulling {
    public static EntityCulling instance = new EntityCulling();
    public OcclusionCullingInstance culling;
    public static boolean enabled = true; // public static to make it faster for the jvm
    public CullTask cullTask;
    private Thread cullThread;
    protected KeyBinding keybind = new KeyBinding("key.entityculling.toggle", -1, "EntityCulling");
    protected boolean pressed = false;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	//stats
	public int renderedBlockEntities = 0;
	public int skippedBlockEntities = 0;
	public int renderedEntities = 0;
	public int skippedEntities = 0;

	public void onInitialize() {
		instance = this;
        culling = new OcclusionCullingInstance(EntityCullingMod.tracingDist.getValue().intValue(), new Provider());
        cullTask = new CullTask(culling, new HashSet<>(Arrays.asList("tile.beacon")));

		cullThread = new Thread(cullTask, "CullThread");
		cullThread.setUncaughtExceptionHandler((thread, ex) -> {
			System.out.println("The CullingThread has crashed! Please report the following stacktrace!");
			ex.printStackTrace();
		});
		cullThread.start();
	}
    
    public void worldTick() {
        cullTask.requestCull = true;
    }
    
    public void clientTick() {
        if (keybind.isKeyDown()) {
            if (pressed)
                return;
            pressed = true;
            enabled = !enabled;
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if(enabled) {
                if (player != null) {
                    player.addChatMessage(new ChatComponentText("Culling on"));
                }
            } else {
                if (player != null) {
                    player.addChatMessage(new ChatComponentText("Culling off"));
                }
            }
        } else {
            pressed = false;
        }
        cullTask.requestCull = true;
    }
}
