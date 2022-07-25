package main.java.com.mywebsite.common;
/*
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;
*/
import main.java.com.mywebsite.common.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.naming.InitialContext;

public class MyLogger{
    
    static main.java.com.mywebsite.common.Logger logger;
    static HashMap<String,Logger> map;
    boolean useLog4j = true;
    final static String dateFormat = "yyyy.MM.dd-HH:mm:ss";
    
    Logger initLogger(String className)
    {
        if(map.size()>0 && map.containsKey(className))
        {
            logger = map.get(className);
        }
        else
        {
            if(useLog4j) {
                logger = new OtherLogger(className);
            } else {
                logger = new MyNewLogger(
                        className,
                        getLogLvl(System.getProperty("log.loglevel", "1")),
                        System.getProperty("log.dateformat")
                );
            }
            map.put(className, logger);
        }
        return logger;
    }
    public static Logger getLogger(String name)
    {
        init(name);
        return getLogger(name);
    }
    public static <t> Logger getLogger(Class <t> name)
    {
        init(name.getName());
        return getLogger(name.getName());
    }
    /**
    * method to init log4j configurations
    */
    private static void init(String name)
    {
        if(map == null)
        {
            map = new HashMap<String,Logger>();
            //map.put(name, logger);
        }
        //////////////////////////////////////////////////////
        // set default config
        if(System.getProperty("log.configuration", null) == null)
        {
            try {
                System.setProperty(
                    "log.configuration",
                    new File(System.getProperty("user.dir")+"/src/main/java/com/mywebsite/props/log4j.properties")
                    .toURI()
                    .toURL()
                    .toString()
                    );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(System.getProperty("log.loglevel", null) == null)
        {
            System.setProperty("log.loglevel", "1");
        }
        if(System.getProperty("log.dateformat", null) == null)
        {
            System.setProperty("log.dateformat", dateFormat);
        }
    }
    int getLogLvl(String loglvl)
    {
        try{
            return Integer.parseInt(loglvl);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}