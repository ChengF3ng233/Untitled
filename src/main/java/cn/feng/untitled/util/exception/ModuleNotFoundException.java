package cn.feng.untitled.util.exception;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ModuleNotFoundException extends RuntimeException {
    public ModuleNotFoundException(String message) {
        super(message);
    }
}
