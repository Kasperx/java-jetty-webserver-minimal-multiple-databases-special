package database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.github.javafaker.Faker;

public abstract class Database extends Dao_DBConnect implements DatabaseInterface
{
    protected enum Enum_Database{
        sqlite, file, mariadb, postgres
    };
    protected Connection connection;
    protected String serverIp;
    protected String path;
//    public Database()
//    {
////        int f = Enum_Database.file.ordinal();
////        if(Enum_Database.file)
////        {
////            
////        }
//        switch(Enum_Database)
//        {
//        }
//    }
    public static Database getInstance()
    {
        return getInstance(Enum_Database.file);
    }
    public static Database getInstance(Enum_Database source)
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
            case mariadb:
                data = new DatabaseFile();
                break;
            case postgres:
                data = new DatabasePostgres();
                break;
            default:
                data = new DatabaseSQLite();
                break;
        }
        return data;
    }
    
    protected HashMap <String, Integer> getNewData()
    {
        //////////////////////////////////////////
        // get names, but only once (-> set)
//        String[] names = { "detlef", "arnold", "ulrike", "emil", "lena", "laura", "achim", "mia", "anna", "jonas" };
//        HashMap<String, Integer> result = new HashMap<String, Integer>();
//        Set <String> unique = new HashSet<String>();
//        int temp = 0;
//        while(temp < 5)
//        {
//            String found = names[new Random().nextInt(10)];
//            if(unique.add(found))
//            {
//                result.put(found, new Random().nextInt(10000000) + 1000000);
//                temp++;
//            }
//        }
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        Faker faker;
        int temp = 0;
        while(temp < 10)
        {
            faker = new Faker();
//            String name = faker.name().fullName();
            String firstName = faker.name().firstName();
//            String lastName = faker.name().lastName();
//            String streetAddress = faker.address().streetAddress();
            result.put(firstName, new Random().nextInt(10000000) + 1000000);
            temp++;
        }
        return result;
    }
    
    @Override
    abstract public void connect();

    @Override
    abstract public boolean createDatabaseIfNotExists();

    @Override
    abstract public ArrayList<ArrayList<String>> getData();

    @Override
    abstract public ArrayList<ArrayList<String>> getAllData();

    @Override
    abstract public void close();

    @Override
    abstract public boolean isPermitted(String name, String password);

    @Override
    abstract public int getId(String name);

//    @Override
//    abstract ResultSet executeGet(String sql);

//    @Override
//    abstract void executeSet(String sql);
}
