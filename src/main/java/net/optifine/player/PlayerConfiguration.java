package net.optifine.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.src.Config;

@Getter
public class PlayerConfiguration {
    private PlayerItemModel[] playerItemModels = new PlayerItemModel[0];
    @Setter
    private boolean initialized = false;

    public void renderPlayerItems(ModelBiped modelBiped, AbstractClientPlayer player, float scale, float partialTicks) {
        if (this.initialized) {
            for (PlayerItemModel playeritemmodel : this.playerItemModels) {
                playeritemmodel.render(modelBiped, player, scale, partialTicks);
            }
        }
    }

    public void addPlayerItemModel(PlayerItemModel playerItemModel) {
        this.playerItemModels = (PlayerItemModel[]) Config.addObjectToArray(this.playerItemModels, playerItemModel);
    }
}
