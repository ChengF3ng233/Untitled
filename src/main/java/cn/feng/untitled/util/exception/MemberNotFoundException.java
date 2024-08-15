package cn.feng.untitled.util.exception;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
