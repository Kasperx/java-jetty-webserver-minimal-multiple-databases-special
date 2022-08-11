package main.java.com.mywebsite.database;

import java.util.ArrayList;

import main.java.com.mywebsite.Data.Person;

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
    public ArrayList<Person> getData()
    {
    	ArrayList<Person> data = new ArrayList<Person>();
        return data;
    }
    public int getId(String name)
    {
        return -1;
    }
    public ArrayList<Person> getAllData()
    {
    	ArrayList<Person> data = new ArrayList<Person>();
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
	@Override
	public boolean insertData(String[] data) {
		return false;
	}
}  

