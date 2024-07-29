package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

public class CommandMessageRaw extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "tellraw";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.tellraw.usage";
    }

    /**
     * Callback when the command is invoked
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage");
        } else {
            EntityPlayer entityplayer = getPlayer(sender, args[0]);
            String s = buildString(args, 1);

            try {
                IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                entityplayer.addChatMessage(ChatComponentProcessor.processComponent(sender, ichatcomponent, entityplayer));
            } catch (JsonParseException jsonparseexception) {
                Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
                throw new SyntaxErrorException("commands.tellraw.jsonException", throwable == null ? "" : throwable.getMessage());
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
