
package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class DatabaseFile extends Database
{  
    int id;
    String name;
    String pw;
    boolean admin;
    String path = System.getProperty("user.dir")+"/test";
    
    public DatabaseFile()
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
    
    public DatabaseFile(int id, String name, String pw, boolean admin)
    {
        File dbFile = new File(path);
        try
        {
            if(!dbFile.exists())
            {
                dbFile.createNewFile();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.admin = admin;
    }

    public void connect()
    {
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
        DatabaseFile db;
        ArrayList<ArrayList<DatabaseFile>> data = new ArrayList<DatabaseFile>();
        try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path)))
        {
            db = new DatabaseFile();
            db.id = inFile.readLine();
            db.name = inFile.readLine();
            db.pw = inFile.readLine();
            db.admin = inFile.readLine();
            db = inFile.readObject();
            return data;
        }
        catch(ClassNotFoundException cnfe)
        {
            //do something
        }
        catch(FileNotFoundException fnfe)
        {
            //do something
        }
        catch(IOException e)
        {
            //do something
        }
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
        int id=0;
        DatabaseFile file2 = new DatabaseFile(id++, "hallo", String.valueOf(new Random().nextInt(1000000) + (100000)), false);
        DatabaseFile file3 = new DatabaseFile(id++, "hallo", String.valueOf(new Random().nextInt(1000000) + (100000)), false);
        DatabaseFile file4 = new DatabaseFile(id++, "hallo", String.valueOf(new Random().nextInt(1000000) + (100000)), false);
        
        ArrayList <DatabaseFile> data = new ArrayList<DatabaseFile>();
        for(int i=0; i<10; i++)
        {
            data.add(new DatabaseFile(id++, "hallo", String.valueOf(new Random().nextInt(1000000) + (100000)), false));
        }
        try(ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream(path)))
        {
            for(DatabaseFile temp: data)
            {
                write.writeObject(temp);
            }
        }
        catch(NotSerializableException nse)
        {
            //do something
        }
        catch(IOException eio)
        {
            //do something
        }
    }
    public boolean isPermitted(String name, String password)
    {
        return false;
    }
}  

