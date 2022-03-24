
package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class DatabaseConnect
{  
    String path = System.getProperty("user.dir")+"/test.db";
    Connection connection = null;
    
    public DatabaseConnect()
    {
        File dbFile = new File(path);
        try
        {
            if(!dbFile.exists())
            {
                dbFile.createNewFile();
            }
            connect();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void connect()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+path);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public ArrayList <String> getData()
    {
        ArrayList <String> data = new ArrayList<String>();
        try
        {
            ResultSet resultSet = null;
            Statement statement = null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name FROM person");
            while(resultSet.next())
            {
                data.add(resultSet.getString("name"));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return data;
    }
    public boolean createDatabaseIfNotExists()
    {
        try
        {
            Statement statement = null;
            statement = connection.createStatement();
//            statement.executeUpdate("create database test if not exists");
            statement.executeUpdate("create table if not exists person (id serial, name text)");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void insertData()
    {
        try
        {
            Statement statement = null;
            statement = connection.createStatement();
            int random = new Random().nextInt();
            statement.executeUpdate("insert into person (name) values ('hallo"+random+"')");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}  

