
package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class DatabaseFile extends Database implements Serializable
{  
    int id;
    String name;
    String pw;
    boolean admin;
    
    public DatabaseFile()
    {
        path = System.getProperty("user.dir")+"/test";
//        path = System.getProperty("user.dir")+"/test";
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
        path = System.getProperty("user.dir")+"/test";
        File dbFile = new File(path);
        try
        {
            if(!dbFile.exists())
            {
                dbFile.createNewFile();
            }
        }
        catch(Exception e)
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
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path)))
        {
            DatabaseFile db;
            Object obj;
//            db = new DatabaseFile();
//            db = inFile.readObject();
            while((obj = inFile.readObject()) != null)
            {
//                obj = inFile.readObject();
                db = new DatabaseFile();
                db = (DatabaseFile)obj;
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(String.valueOf(db.id));
                temp.add(db.name);
//                temp.add(db.pw);
//                temp.add(String.valueOf(db.admin));
                data.add(temp);
            }
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
        HashMap <String, Integer> result = getNewData();
        ///////////////////////////////////////////////////////////
        ArrayList <DatabaseFile> data = new ArrayList<DatabaseFile>();
        data.add(new DatabaseFile(
                id++,
                "admin",
                "secret",
                true
                ));
        for(Entry <String, Integer> entry: result.entrySet())
        {
            data.add(new DatabaseFile(
                  id++,
                  entry.getKey(),
                  String.valueOf(entry.getValue()),
                  false
                  ));
        }
        
//        ArrayList <DatabaseFile> data = new ArrayList<DatabaseFile>();
//        for(int i=0; i<10; i++)
//        {
//            data.add(new DatabaseFile(
//                    id++,
//                    "hallo"+String.valueOf(new Random().nextInt(10) + (10)),
//                    String.valueOf(new Random().nextInt(1000000) + (100000)),
//                    false
//                    ));
//        }
        try(ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream(path)))
        {
            for(DatabaseFile temp: data)
            {
                write.writeObject((Object)temp);
            }
        }
        catch(NotSerializableException nse)
        {
            nse.printStackTrace();
        }
        catch(IOException eio)
        {
            eio.printStackTrace();
        }
    }
    public boolean isPermitted(String name, String password)
    {
        return false;
    }
}  

