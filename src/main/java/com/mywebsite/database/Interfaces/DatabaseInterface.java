package main.java.com.mywebsite.database.Interfaces;

import java.util.ArrayList;

import org.json.JSONObject;

import main.java.com.mywebsite.Data.Person;

public interface DatabaseInterface
{
    /**
     * connect to database
     */
    public void connect();
    /**
     * create new database if not exists
     * @return true if success, false otherwise
     */
    public boolean createDatabaseIfNotExists();
    /**
     * get data, but only user basic data
     * @return list with all data, without sensible information like passwords
     */
    public ArrayList<Person> getData();
    /**
     * get all data including passwords
     * @return list with all data
     */
    public ArrayList<Person> getAllData();
    /**
     * login to database, get permission for admin or not
     * @param name
     * @param password
     * @return boolean. true if admin login are correct, false otherwise
     */
    public boolean isPermitted(String name, String password);
    /**
     * get id of user by name
     * @param name username
     * @return id of wanted user
     */
    public int getId(String name);
//    public String generateActualSql(String sqlQuery, Object... parameters);
//    private ResultSet executeGet(String sql);
//    private void executeSet(String sql);
    /**
     * add entry to db
     * @param data
     * @return
     */
    public boolean insertData(String [] data);
    /**
     * delete entry in db
     * @param data
     * @return
     */
    public boolean removeData(String [] data);
    /**
     * update entry in db
     * @param data
     * @return
     */
    public boolean updateData(String [] data);
}
