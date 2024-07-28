package cn.feng.untitled.command.impl;

import cn.feng.untitled.command.Command;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class HelpCommand extends Command {
    public HelpCommand() {
        super("HelpCommand", new String[]{"h", "help"});
    }

    @Override
    public void execute(String[] args) {

    }
}
