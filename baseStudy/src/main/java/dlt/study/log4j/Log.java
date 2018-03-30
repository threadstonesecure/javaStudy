package dlt.study.log4j;

import org.apache.commons.logging.LogFactory;

/**
 * Created by denglt on 16/9/29.
 */
public class Log {
    private static org.apache.commons.logging.Log log = LogFactory.getLog(Log.class);


    public static boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public static boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    public static boolean isFatalEnabled() {
        return log.isFatalEnabled();
    }

    public static boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    public static boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    public static boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    public static void trace(Object message) {
        log.trace(message);
    }

    public static void trace(Object message, Throwable t) {
        log.trace(message, t);
    }

    public static void debug(Object message) {
        log.debug(message);
    }

    public static void debug(Object message, Throwable t) {
        log.debug(message, t);
    }

    public static void info(Object message) {
        log.info(message);
    }

    public static void info(Object message, Throwable t) {
        log.info(message, t);
    }

    public static void warn(Object message) {
        log.info(message);
    }

    public static void warn(Object message, Throwable t) {
        log.warn(message, t);
    }

    public static void error(Object message) {
        log.error(message);
    }

    public static void error(Object message, Throwable t) {
        log.error(message, t);
    }

    public static void fatal(Object message) {
        log.fatal(message);
    }

    public static void fatal(Object message, Throwable t) {
        log.fatal(message, t);
    }
}
