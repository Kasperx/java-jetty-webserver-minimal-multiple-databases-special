package main.java.com.mywebsite.database;

import java.util.ArrayList;

import main.java.com.mywebsite.Data.Person;
import org.apache.logging.log4j.LogManager;

public class DatabasePostgres extends Database
{
    public DatabasePostgres()
    {
        logger = LogManager.getLogger(this.getClass().getName());
    }
    public void connect()
    {
    }
    public void close()
    {
    }
    public ArrayList<Person> getData()
    {
    	return new ArrayList<Person>();
    }
    public int getId(String name)
    {
        return -1;
    }
    public ArrayList<Person> getAllData()
    {
    	return new ArrayList<Person>();
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
    @Override
    public boolean removeData(String[] data)
    {
        return false;
    }
    @Override
    public boolean updateData(String[] data)
    {
        return false;
    }
}  

