package main.java.com.mywebsite.common;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
 
public class MyLogger {
    
    static Logger log;
    
    Logger getLogger()
    {
        return log;
    }
    public static Logger getLogger(String name)
    {
        return LogManager.getLogger(name);
    }
    public static <t> Logger getLogger(Class <t> name)
    {
        return LogManager.getLogger(name);
    }
}