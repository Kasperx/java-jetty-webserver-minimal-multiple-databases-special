package main.java.com.mywebsite.database;

import java.util.ArrayList;

import org.json.JSONObject;

public class DatabasePostgres extends Database
{  
    public DatabasePostgres()
    {
        connect();
    }
    public void connect()
    {
        try
        {
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void close()
    {
        try
        {
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public ArrayList<ArrayList<String>> getData()
    {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        return data;
    }
    public int getId(String name)
    {
        return -1;
    }
    public ArrayList <ArrayList<String>> getAllData()
    {
        ArrayList <ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        return data;
    }
    public boolean createDatabaseIfNotExists()
    {
        return true;
    }
    public void insertData()
    {
    }
    public boolean isPermitted(String name, String password)
    {
        return false;
    }
}  

