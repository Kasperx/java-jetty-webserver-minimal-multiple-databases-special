package main.java.com.mywebsite.common.logger;

import java.io.File;
import java.util.HashMap;

/**
 * MyLogger
 * 
 * <p>Makes easy and hidden use of a logger for console info
 * <p>Supported: mylogger & org.apache.logging.log4j.Logger
 * <p>This program decides following parameter from possibly available config:
 * <p>debug lvl: true | false
 * <p>show class names: 0 | 1 | 2
 * <p>0: classname will not be shown
 * <p>1: classname with package name will be shown
 * <p>2: classname without package name will be shown
 * 
 * @author cgl
 *
 */
public abstract class LoggerConfig implements Logger{
    
    static main.java.com.mywebsite.common.logger.Logger logger;
    static HashMap<String,Logger> map;
    static boolean useLog4j = false;
    final static String dateFormat = "yyyy.MM.dd-HH:mm:ss.S";
    
    static Logger initLogger(String className)
    {
        if(map.size()>0 && map.containsKey(className))
        {
            logger = map.get(className);
        }
        else
        {
            if(strToBoolean(System.getProperty("log.log4j", "true"))) {
                logger = new OtherLogger(className);
            } else {
                logger = new MyOwnLogger(
                        className,
                        strToBoolean(System.getProperty("log.debug", "false")),
                        strToInt(System.getProperty("log.showClasslevel", "1")),
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
        return initLogger(name);
    }
    public static <t> Logger getLogger(Class <t> name)
    {
        init(name.getName());
        return initLogger(name.getName());
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
                System.out.println("Set config 'log.configuration' = "+System.getProperty("log.configuration", null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // log.debug
        String configName = "log.debug";
        if(System.getProperty(configName, null) == null)
        {
            System.setProperty(configName, "false");
            System.out.println("Set config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
        } else {
            System.out.println("Found config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
        }
        configName = "log.showClassLevel";
        // log.showClasslevel
        if(System.getProperty(configName, null) == null)
        {
            System.setProperty(configName, "1");
            System.out.println("Set config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
        } else {
            System.out.println("Found config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
        }
        // log.dateformat
        configName = "log.dateformat";
        if(System.getProperty(configName, null) == null)
        {
            System.setProperty(configName, dateFormat);
            System.out.println("Set config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
        } else {
            System.out.println("Found config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
        }
        // log.log4j
        configName = "log.log4j";
        if(System.getProperty(configName, null) == null)
        {
            if(useLog4j) {
                System.setProperty(configName, "true");
                System.out.println("Set config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
            } else {
                System.setProperty(configName, "false");
                System.out.println("Set config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
            }
        } else {
            System.out.println("Found config '"+configName+"' = '"+System.getProperty(configName, null)+"'");
        }
    }
    static int strToInt(String loglvl)
    {
        try{
            return Integer.parseInt(loglvl);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    static boolean strToBoolean(String str)
    {
        boolean lvl = false;
        try{
            switch(str)
            {
            case "true":
                lvl = true;
                break;
            default:
                lvl = false;
            };
        } catch (Exception e) {
            e.printStackTrace();
            lvl = false;
        }
        return lvl;
    }
    public abstract void warn(String text);
    public abstract void info(String text);
    public abstract void debug(String text);
    public abstract void error(String text);
    public abstract void error(Exception error);
    public abstract void error(String text, Exception error);
}