package net.minecraft.command;

import lombok.Getter;

@Getter
public class CommandException extends Exception {
    private final Object[] errorObjects;

    public CommandException(String message, Object... objects) {
        super(message);
        this.errorObjects = objects;
    }

}
