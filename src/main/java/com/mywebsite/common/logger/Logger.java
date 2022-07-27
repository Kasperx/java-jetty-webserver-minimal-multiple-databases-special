package main.java.com.mywebsite.common.logger;

/**
 *
 * @author cgl
 */
public interface Logger {
    void warn(String text);
    void info(String text);
    void debug(String text);
    void error(String text);
    void error(Exception error);
    void error(String text, Exception error);
}
