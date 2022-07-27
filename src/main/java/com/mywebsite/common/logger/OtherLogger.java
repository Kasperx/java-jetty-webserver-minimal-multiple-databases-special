package main.java.com.mywebsite.common.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 *
 * @author cgl
 */
public class OtherLogger implements Logger{

    org.apache.logging.log4j.Logger logger;
    public OtherLogger(String name) {
        System.getProperty("log.configuration", null);
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.INFO);
        logger = LogManager.getLogger(name);
    }
    
    @Override
    public void warn(String text) {}

    @Override
    public void info(String text) {}

    @Override
    public void debug(String text) {}

    @Override
    public void error(String text) {}

    @Override
    public void error(String text, Exception e) {}

    @Override
    public void error(Exception error) {}
    
}
