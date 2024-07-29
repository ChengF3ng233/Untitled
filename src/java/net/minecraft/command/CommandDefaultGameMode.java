package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandDefaultGameMode extends CommandGameMode {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "defaultgamemode";
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.defaultgamemode.usage";
    }

    /**
     * Callback when the command is invoked
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.defaultgamemode.usage");
        } else {
            WorldSettings.GameType worldsettings$gametype = this.getGameModeFromCommand(sender, args[0]);
            this.setGameType(worldsettings$gametype);
            notifyOperators(sender, this, "commands.defaultgamemode.success", new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName()));
        }
    }

    protected void setGameType(WorldSettings.GameType gameMode) {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        minecraftserver.setGameType(gameMode);

        if (minecraftserver.getForceGamemode()) {
            for (EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().getPlayerList()) {
                entityplayermp.setGameType(gameMode);
                entityplayermp.fallDistance = 0.0F;
            }
        }
    }
}
