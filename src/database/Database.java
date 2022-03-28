package database;

import java.sql.Connection;
import java.util.ArrayList;

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
