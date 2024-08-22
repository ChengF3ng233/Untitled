package cn.feng.untitled.util.misc;

import org.apache.logging.log4j.LogManager;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class Logger {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger("Untitled");
    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void debug(String msg) {
        logger.debug(msg);
    }
}
