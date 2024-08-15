package cn.feng.untitled.command;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public abstract class Command {
    public String name;
    public String[] patterns;

    public Command(String name, String[] patterns) {
        this.name = name;
        this.patterns = patterns;
    }

    public abstract void execute(String[] args);
}
