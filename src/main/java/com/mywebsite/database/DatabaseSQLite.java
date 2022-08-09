package main.java.com.mywebsite.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONObject;

import main.java.com.mywebsite.common.logger.Logger;
import main.java.com.mywebsite.common.logger.LoggerConfig;
import tech.tablesaw.api.Table;

public class DatabaseSQLite extends Database
{  
    String path = System.getProperty("user.dir")+"/test.db";
    Connection connection = null;
    static Logger logger;
//    boolean permitCreateDB = true;
    public DatabaseSQLite()
    {
        logger = LoggerConfig.getLogger(DatabaseSQLite.class.getName());
        File dbFile = new File(path);
        try
        {
            if(!dbFile.exists())
            {
                dbFile.createNewFile();
            }
            connect(true);
        }
        catch(IOException e)
        {
            logger.error(e);
        }
    }
    /**
     * 
     */
    public void connect()
    {
    	connect(false);
    }
    /**
     * 
     * @param showInfo
     */
    public void connect(boolean showInfo)
    {
        try
        {
        	if(connection == null || connection.isClosed())
        	{
        		Class.forName("org.sqlite.JDBC");
        		connection = DriverManager.getConnection("jdbc:sqlite:"+path);
        		if(showInfo)
        		{
        			logger.info("Connected to database '"+path+"'.");
        		}
        	}
        }
        catch(Exception e)
        {
            logger.error(e);
        }
    }
    /**
     * 
     */
    public ArrayList<ArrayList<String>> getData()
    {
        String sql = ""
        		+ "SELECT "
//        		+ "id, "
        		+ "name, "
        		+ "lastname "
        		+ "FROM "
        		+ "person "
        		+ "where name != 'admin'";
        ArrayList<ArrayList<String>> data = getDataFromDBWithHeader(sql);
        return data;
    }
    /**
     * 
     */
    public JSONObject getDataJson()
    {
        String sql = ""
                + "SELECT "
                //        		+ "id, "
                + "name, "
                + "lastname "
                + "FROM "
                + "person "
                + "where name != 'admin'";
        JSONObject json = getDataFromDBWithHeaderJSON(sql);
        return json;
    }
    /**
     * 
     */
    public ArrayList<ArrayList<String>> getDataJSON()
    {
        String sql = ""
                + "SELECT "
//        		+ "id, "
                + "name, "
                + "lastname "
                + "FROM "
                + "person "
                + "where name != 'admin'";
        ArrayList<ArrayList<String>> data = getDataFromDBWithHeader(sql);
        return data;
    }
    /**
     * 
     */
    public int getId(String name)
    {
        ResultSet resultSet = executeGet("SELECT id FROM person where name = '"+name+"'");
        try
        {
            if(resultSet.next())
            {
            	int id = resultSet.getInt("id");
            	close(resultSet);
                return id;
            }
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
        return -1;
    }
    /**
     * 
     */
    public ArrayList <ArrayList<String>> getAllData()
    {
        String sql = "SELECT "
                + " person.id,"
                + " person.name,"
                + " person.lastname,"
                + " login.p_password as password,"
                + " login.p_admin as admin_permission"
                + " FROM person"
                + " join login on person.id = login.p_id";
        ArrayList <ArrayList<String>> data = getDataFromDBWithHeader(sql);
        return data;
    }
    /**
     * 
     */
    public boolean createDatabaseIfNotExists()
    {
        if(permitCreateDB) {
//            executeSet("drop table if exists person");
//            executeSet("drop table if exists login");
            executeSet("delete from person");
            executeSet("delete from login");
            //////////////////////////////
            executeSet("create table if not exists person ("
                    + "id integer primary key autoincrement,"
                    + "name text unique,"
                    + "lastname text"
                    + ")");
            /*
             *  table login with boolean.. but seems problem with jdbc -> sqlite
             *  sometimes boolean = int, sometimes not :(
             *  so boolean = int and works. remember for other dbs
             */
            executeSet("create table if not exists login ("
                    + "id integer primary key autoincrement,"
                    + "p_id integer,"
                    + "p_name text,"
                    + "p_lastname text,"
                    + "p_password text unique,"
//                    + "p_admin boolean default 'false',"
                    + "p_admin int default 0,"
                    + "foreign key (p_id) references person(id)"
                    + ")");
            return true;
        } else {
            return false;
        }
    }
    /**
     * 
     */
    public void insertData()
    {
        HashMap <String[], Integer> result = getNewData();
        ///////////////////////////////////////////////////////////
//        executeSet("insert into person (name, lastname) values ('admin', 'admin')");
        executeSet("admin", "admin");
//        generateActualSql("insert into person (name, lastname) values (?,?);", "admin", "admin");
//        executeSet("insert into login (p_id, p_password, p_admin) values (1, 'secret', 'true')");
//        executeSet("secret", true);
        insertAdminData(getId("admin"), "secret", true);
//        generateActualSql("insert into login (p_id, p_password, p_admin) values (?,?,?);", 1, "secret", "true");
        for(Entry <String[], Integer> entry: result.entrySet())
        {
        	String name = entry.getKey()[0];
        	String lastname = entry.getKey()[1];
        	int pw = entry.getValue();
//            executeSet(""
//            		+ "insert into person ("
//            		+ "name, "
//            		+ "lastname"
//            		+ ") "
//            		+ "values "
//            		+ "("
//            		+ "'"+name+"', "
//    				+ "'"+lastname+"'"
//					+ ")"
//					+ "");
//            String sql = ""
//            		+ "insert into person ("
//            		+ "name, "
//            		+ "lastname"
//            		+ ") "
//            		+ "values "
//            		+ "("
//            		+ "?, "
//    				+ "?"
//					+ ");";
            executeSet(name, lastname);
//        	generateActualSql(sql, name, lastname);
//            executeSet("insert into "
//            		+ "login ("
//            		+ "p_id, "
//            		+ "p_password"
//            		+ ") values ("
//            		+ ""+getId(name)+", "
//    				+ "'"+pw+"'"
//					+ ")");
//            sql = "insert into "
//            		+ "login ("
//            		+ "p_id, "
//            		+ "p_password"
//            		+ ") values ("
//            		+ "?, "
//    				+ "?"
//					+ ");";
            executeSet(getId(name), pw);
//            generateActualSql(sql, getId(name), pw);
        }
    }
    /**
     * 
     */
    public boolean isPermitted(String name, String password)
    {
        try
        {
//            ResultSet resultSet = executeGet("SELECT p.name, login.p_password, login.p_admin FROM person p inner join login on p.id = login.p_id where login.p_admin = 'true'");
//            ResultSet resultSet = executeGet("SELECT p.id, p.name, login.p_password, login.p_admin FROM person p inner join login on p.id = login.p_id where login.p_admin = 'true'");
            /*
             *  table login with boolean.. but seems problem with jdbc -> sqlite
             *  sometimes boolean = int, sometimes not :(
             *  so boolean = int and works. remember for other dbs
             */
            ResultSet resultSet = executeGet("SELECT p.id, p.name, login.p_password, login.p_admin FROM person p inner join login on p.id = login.p_id where login.p_admin = 1");
            if(resultSet.next())
            {
                String tempname = resultSet.getString("name");
                String temppw = resultSet.getString("p_password");
                if(name != null && password != null & name.equals(tempname) && password.equals(temppw))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            close(resultSet);
            return false;
        }
        catch(SQLException e)
        {
            logger.error(e);
            return false;
        }
    }
    /**
     * 
     * @param sql
     * @return
     */
    ResultSet executeGet(String sql)
    {
        try
        {
            logger.info(sql);
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            return stmt.executeQuery();
        }
        catch(SQLException e)
        {
            logger.error(e);
            return null;
        }
    }
    /**
     * 
     * @param sql
     * @return
     */
    ResultSetMetaData getMetaData(String sql)
    {
    	try
    	{
    		logger.info(sql);
    		PreparedStatement stmt = connection.prepareStatement(sql);
    		return stmt.getMetaData();
    	}
    	catch(SQLException e)
    	{
    		logger.error(e);
    		return null;
    	}
    }
    /**
     * 
     * @param sql
     */
    void executeSet(String sql)
    {
        try
        {
            logger.info(sql);
            connect();
            connection.prepareStatement(sql).executeUpdate();
            close(null);
        }
        catch(SQLException e)
        {
        	logger.error(e);
        }
    }
    /**
     * 
     * @param element1
     * @param element2
     */
    void executeSet(String element1, String element2)
    {
        try
        {
//            executeSet("insert into person (name, lastname) values ('admin', 'admin')");
            String sql = "insert into person ("
                    + "name,"
                    + "lastname"
                    + ") values (";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?"+")");
            stmt.setString(1, element1);
            stmt.setString(2, element2);
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
    }
    /**
     * 
     * @param element1
     * @param element2
     */
    void executeSet(String element1, boolean element2)
    {
        try
        {
//            executeSet("insert into person (name, lastname) values ('admin', 'admin')");
            String sql = "insert into login ("
                    + "p_password,"
                    + "p_admin"
                    + ") values (";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?"+")");
            stmt.setString(1, element1);
            stmt.setBoolean(2, element2);
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
    }
    /**
     * 
     * @param element1
     * @param element2
     * @param element3
     */
    void executeSet(int element1, String element2, String element3)
    {
        try
        {
//            executeSet("insert into login (p_id, p_password, p_admin) values (1, 'secret', 'true')");
            String sql = "insert into login ("
                    + "p_id,"
                    + "p_password,"
                    + "p_admin"
                    + ") values (";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?,?"+")");
            stmt.setInt(1, element1);
            stmt.setString(2, element2);
            stmt.setString(3, element3);
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
    }
    /**
     * 
     * @param element1
     * @param element2
     * @param element3
     */
    void insertAdminData(int element1, String element2, boolean admin)
    {
        try
        {
//            executeSet("insert into login (p_id, p_password, p_admin) values (1, 'secret', 'true')");
            String sql = "insert into login ("
                    + "p_id,"
                    + "p_password,"
                    + "p_admin"
                    + ") values (";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?,?"+")");
            stmt.setInt(1, element1);
            stmt.setString(2, element2);
            // test
//            stmt.setBoolean(3, admin);
            stmt.setInt(
                    3,
                    admin?
                        1
                        :
                        0
                );
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
    }
    /**
     * 
     * @param element1
     * @param element2
     */
    void executeSet(int element1, int element2)
    {
        try
        {
//            executeSet("insert into login (p_id, p_password) values (1, 'secret')");
            String sql = "insert into login ("
                    + "p_id,"
                    + "p_password"
                    + ") values (";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?"+")");
            stmt.setInt(1, element1);
            stmt.setInt(2, element2);
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
    }
    /**
     * 
     * @param connection
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    public static PreparedStatement prepareStatement(Connection connection, String sql, Object... values)
            throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
        logger.debug(sql + " " + Arrays.asList(values));
        return preparedStatement;
    }
    /**
     * 
     * @param sql
     * @return
     */
    ArrayList <ArrayList<String>> getDataFromDBWithoutHeader(String sql)
    {
    	ArrayList <ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    	try
    	{
        	ResultSet resultSet = executeGet(sql);
        	ResultSetMetaData rsmd = resultSet.getMetaData();
        	data = getDataFromDB(sql, resultSet, rsmd);
    	}
    	catch(SQLException e)
    	{
    		logger.error(e);
    	}
    	return data;
    }
    /**
     * 
     * @param sql
     * @param resultSet
     * @param rsmd
     * @return
     */
    ArrayList <ArrayList<String>> getDataFromDB(String sql, ResultSet resultSet, ResultSetMetaData rsmd)
    {
    	ArrayList <ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        try
        {
        	ArrayList <String> temp = new ArrayList<String>();
            while(resultSet.next())
            {
            	temp = new ArrayList<String>();
            	for(int column=1; column <= rsmd.getColumnCount(); column++)
            	{
        			temp.add(resultSet.getString(column));
            	}
            	data.add(temp);
            }
            close(resultSet);
        }
        catch(SQLException e)
        {
        	logger.error(e);
        }
        return data;
    }
    /**
     * 
     * @param sql
     * @return
     */
    ArrayList<ArrayList<String>> getDataFromDBWithHeader(String sql)
    {
    	ArrayList <ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    	ArrayList <ArrayList<String>> header = new ArrayList<ArrayList<String>>();
    	ArrayList <ArrayList<String>> content = new ArrayList<ArrayList<String>>();
    	try
    	{
//    		connect();
    		ResultSet resultSet = executeGet(sql);
//    		createDatabaseIfNotExists();
    		// get header
    		ResultSetMetaData rsmd = getMetaData(sql);
    		ArrayList <String> temp = new ArrayList<String>();
    		for(int column=1; column <= rsmd.getColumnCount(); column++)
    		{
    			if(headerInUppercaseCharacter)
    			{
    				temp.add(rsmd.getColumnName(column).toUpperCase());
    			}
    			else
    			{
    				temp.add(rsmd.getColumnName(column).toLowerCase());
    			}
    		}
    		header.add(temp);
    		// get content
    		content = getDataFromDB(sql, resultSet, rsmd);
    		close(resultSet);
    		// migrate
    		for(ArrayList<String> migrate: header)
    		{
    			data.add(migrate);
    		}
    		for(ArrayList<String> migrate: content)
    		{
    			data.add(migrate);
    		}
    	}
    	catch(SQLException e)
    	{
    		logger.error(e);
    	}
    	return data;
    }
    /**
     * 
     * @param sql
     * @return
     */
    JSONObject getDataFromDBWithHeaderJSON(String sql)
    {
        try
        {
            ResultSet resultSet = executeGet(sql);
            ResultSetMetaData rsmd = getMetaData(sql);
            Table data = Table.read().db(resultSet);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
        return new JSONObject();
    }
    /**
     * 
     * @param resultSet
     */
    private void close(ResultSet resultSet)
    {
    	try
    	{
    		if(resultSet != null && !resultSet.isClosed())
    		{
    			resultSet.close();
    		}
    		if(connection != null)
    		{
    			connection.close();
    		}
		}
    	catch (SQLException e)
    	{
    		logger.error(e);
		}
	}
//    /**
//     * 
//     */
//    public String generateActualSql(String sqlQuery, Object... parameters)
//    {
//        String[] parts = sqlQuery.split("\\?");
//        StringBuilder sb = new StringBuilder();
//        // This might be wrong if some '?' are used as litteral '?'. Careful!
//        for (int i = 0; i < parts.length; i++) {
//            String part = parts[i];
//            sb.append(part);
//            if (i < parameters.length) {
//                String temp = formatParameter(parameters[i]);
////                sb.append(formatParameter(parameters[i]));
//                sb.append(temp);
//            }
//        }
//        logger.info(sb.toString());
//        return sb.toString();
//    }
//    /**
//     * 
//     * @param parameter
//     * @return
//     */
//    String formatParameter(Object parameter) {
//        if (parameter == null) {
//            return "NULL";
//        } else {
//            if(parameter instanceof String) {
//                return "'"
//                        + ((String) parameter)
//                        .replace("'", "''")
//                        + "'";
//            } else if(parameter instanceof Timestamp) {
//                return "to_timestamp('" 
//                        + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
//                        .format(parameter)
//                        + "', 'mm/dd/yyyy hh24:mi:ss.ff3')";
//            } else if(parameter instanceof Date) {
//                return "to_date('"
//                        + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
//                        .format(parameter) + "', 'mm/dd/yyyy hh24:mi:ss')";
//            } else if(parameter instanceof Boolean) {
//                return ((Boolean) parameter).booleanValue() ? "1" : "0";
//            } else {
//                return parameter.toString();
//            }
//        }
//    }
    @Override
    /**
     * 
     */
    public JSONObject getAllDataJson()
    {
        return null;
    }
}  
