package main.java.com.mywebsite.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.javafaker.Faker;

import main.java.com.mywebsite.Data.Person;
import main.java.com.mywebsite.common.logger.Logger;
import main.java.com.mywebsite.common.logger.LoggerConfig;
import main.java.com.mywebsite.database.DAO.Dao_DBConnect;
import main.java.com.mywebsite.database.Interfaces.DatabaseInterface;

public abstract class Database extends Dao_DBConnect implements DatabaseInterface
{
    static Logger logger = LoggerConfig.getLogger(Database.class.getName());
    protected boolean permitCreateDB = true;
    /**
     * enum for database use
     * @author cgl
     *
     */
    public static enum DatabaseType
    {
        sqlite("sqlite"),
        file("file"),
        mariadb("mariadb"),
        postgres("postgres");
        String value = null;
        DatabaseType(String value)
        {
        	this.value = value;
        }
        public static DatabaseType getValue()
        {
        	return sqlite;
        }
    };
    protected Connection connection;
    protected String serverIp;
    protected String path;
    boolean headerInUppercaseCharacter = true;
    HashMap<String, String> mapFromFile;
    /**
     * get instance
     * @return
     */
    public static Database getInstance()
    {
        return getInstance(DatabaseType.getValue());
    }
    /**
     * get instance
     * @param source
     * @return
     */
    public static Database getInstance(DatabaseType source)
    {
        Database data = null;
        switch(source)
        {
            case file:
                data = new DatabaseFile();
                break;
            case sqlite:
                data = new DatabaseSQLite();
                break;
//            case mariadb:
//                data = new DatabaseFile();
//                break;
//            case postgres:
//                data = new DatabasePostgres();
//                break;
            default:
            	logger.info("Not supported yet: source '"+source.value+"'. Using '"+DatabaseType.sqlite+"'.");
                data = new DatabaseSQLite();
                break;
        }
        return data;
    }
    /**
     * get new random data
     * @return
     */
    protected HashMap <String[], Integer> getNewData()
    {
        //////////////////////////////////////////
        HashMap<String[], Integer> result = new HashMap<String[], Integer>();
        Faker faker;
        int temp = 0;
        while(temp < 10)
        {
            faker = new Faker();
//            String name = faker.name().fullName();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
//            String streetAddress = faker.address().streetAddress();
//            result.put(firstName, new Random().nextInt(10000000) + 1000000);
//            result.put(new String[]{firstName, lastName}, new Random().nextInt(10000000) + 1000000);
            result.put(new String[]{firstName, lastName}, getRandom());
            temp++;
        }
        return result;
    }
    public abstract void connect();
    public abstract boolean createDatabaseIfNotExists();
    public abstract ArrayList<Person> getData();
    public abstract ArrayList<Person> getAllData();
    public abstract boolean isPermitted(String name, String password);
    public abstract int getId(String name);
    public abstract void insertData();
    public abstract boolean insertData(String [] data);
    public abstract boolean removeData(String [] data);
    public abstract boolean updateData(String [] data);
    public int getRandom()
    {
    	return new Random().nextInt(10000000) + 1000000;
    }
    /**
     * 
     * @return
     */
	public boolean isHeaderInUppercaseCharacter()
	{
		return headerInUppercaseCharacter;
	}
	/**
	 * 
	 * @param headerInUppercaseCharacter
	 */
	public void setHeaderInUppercaseCharacter(boolean headerInUppercaseCharacter)
	{
		this.headerInUppercaseCharacter = headerInUppercaseCharacter;
	}
	/**
	 * get properties
	 * @param filename
	 * @return
	 */
    public Map <String, String> getProperties(String filename)
    {
        mapFromFile = new HashMap<String, String>();
        if(!new File(filename).exists())
        {
            logger.info("File '"+filename+"' does not exist");
            return mapFromFile;
        }
        try (BufferedReader br = new BufferedReader( new FileReader(filename));)
        {
                        String line = "";
                mapFromFile = new HashMap<String, String>();
                        while ((line = br.readLine()) != null)
                        {
                String[] parts = line.split("=");
                String name = parts[0].trim();
                String value = parts[1].trim();
                if(!name.equals("") && !value.equals(""))
                {
                    mapFromFile.put(name, value);
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e);
            return mapFromFile;
        }
        return mapFromFile;
    }
    /**
     * load property
     * @param keyname
     * @return
     */
    public String getProperty (String keyname)
    {
        return mapFromFile.get(keyname);
    }
    /**
     * convert string to int value
     * @param text
     * @return
     */
    public static int toInt(String text)
    {
        try{
            if(text == null || text.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(text);
        } catch (Exception e) {
            logger.error(e);
            return 0;
        }
    }
    /**
     * string to boolean
     * @return
     */
    public static boolean toBoolean(String text)
    {
       if(text.equals("true")) {
           return true;
       } else {
           return false;
       }
    }
}
