package main.java.com.mywebsite.database;

import java.util.ArrayList;

import org.json.JSONObject;

public interface DatabaseInterface
{
    public void connect();
    public boolean createDatabaseIfNotExists();
    public ArrayList <ArrayList<String>> getData();
    public ArrayList <ArrayList<String>> getAllData();
    public boolean isPermitted(String name, String password);
    public int getId(String name);
//    public String generateActualSql(String sqlQuery, Object... parameters);
//    private ResultSet executeGet(String sql);
//    private void executeSet(String sql);
    public JSONObject getDataJson();
    /**
     * @return json data from database
     */
    public JSONObject getAllDataJson();
}
