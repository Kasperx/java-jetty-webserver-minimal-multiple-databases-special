package main.java.com.mywebsite.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import main.java.com.mywebsite.Data.Person;
import main.java.com.mywebsite.common.logger.Logger;
import main.java.com.mywebsite.common.logger.LoggerConfig;

public class DatabaseSQLite extends Database
{  
    String path = System.getProperty("user.dir")+"/test.db";
    Connection connection = null;
    static Logger logger;
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
     * connect to database
     */
    public void connect()
    {
    	connect(false);
    }
    /**
     * connect to database
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
     * get data without sensible information
     */
    public ArrayList<Person> getData()
    {
        String sql = ""
        		+ "SELECT"
        		+ " position,"
        		+ " name,"
        		+ " action,"
        		+ " action_name"
        		+ " FROM"
        		+ " person;";
//        ArrayList<Person> data = getDataFromDBWithHeader(sql, false);
        ArrayList<Person> data = getDataFromDBWithoutHeader(sql, false);
        return data;
    }
//    /**
//     * 
//     */
//    public ArrayList<Person> getDataJSON()
//    {
//        String sql = ""
//                + "SELECT "
////        		+ "id, "
//                + "name, "
//                + "lastname "
//                + "FROM "
//                + "person "
//                + "where name != 'admin'";
//        ArrayList<Person> data = getDataFromDBWithHeader(sql);
//        return data;
//    }
    /**
     * get id for name
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
     * get data with all information
     */
    public ArrayList<Person> getAllData()
    {
        String sql = "SELECT"
                + " p.id,"
                + " p.name,"
                + " p.lastname,"
                + " login.p_password as password,"
                + " login.p_admin as admin"
//                + " FROM person p, login l"
                + " FROM person p"
                + " inner join login on p.id = login.p_id;";
        // "SELECT p.id, p.name, login.p_password, login.p_admin FROM person p inner join login on p.id = login.p_id where login.p_admin = 1"
//        ArrayList<Person> data = getDataFromDBWithHeader(sql, true);
        ArrayList<Person> data = getDataFromDBWithoutHeader(sql, true);
        return data;
    }
    /**
     * if permitted: create Database If Not Exists
     */
    public boolean createDatabaseIfNotExists()
    {
        if(permitCreateDB) {
            executeSet("drop table if exists person");
//            executeSet("drop table if exists login");
//            executeSet("delete from person");
//            executeSet("delete from login");
            //////////////////////////////
            executeSet("create table if not exists person ("
//                    + "id integer primary key autoincrement,"
                    + "position integer primary key autoincrement,"
                    + "name text,"
                    + "action text,"
                    + "action_name text"
                    + ")");
            /*
             *  table login with boolean.. but seems problem with jdbc -> sqlite
             *  sometimes boolean = int, sometimes not :(
             *  so boolean = int and works. remember for other dbs
             */
//            executeSet("create table if not exists login ("
//                    + "id integer primary key autoincrement,"
//                    + "p_id integer,"
//                    + "p_name text,"
//                    + "p_lastname text,"
//                    + "p_password text,"
////                    + "p_admin boolean default 'false',"
//                    + "p_admin int default 0,"
//                    + "foreign key (p_id) references person(id)"
//                    + ")");
            return true;
        } else {
            return false;
        }
    }
    /**
     * insert automatic data
     */
    public void insertData()
    {
        HashMap <String[], Integer> result = getNewData();
        ///////////////////////////////////////////////////////////
//        executeSet("insert into person (name, lastname) values ('admin', 'admin')");
//        executeSet("admin", "admin");
//        generateActualSql("insert into person (name, lastname) values (?,?);", "admin", "admin");
//        executeSet("insert into login (p_id, p_password, p_admin) values (1, 'secret', 'true')");
//        executeSet("secret", true);
//        insertAdminData(getId("admin"), "secret", true);
//        insertAdminData(getId("admin"), "secret");
//        generateActualSql("insert into login (p_id, p_password, p_admin) values (?,?,?);", 1, "secret", "true");
        
//        for(Entry <String[], Integer> entry: result.entrySet())
//        {
//        	String name = entry.getKey()[0];
//        	String lastname = entry.getKey()[1];
//        	int pw = entry.getValue();
////            executeSet(""
////            		+ "insert into person ("
////            		+ "name, "
////            		+ "lastname"
////            		+ ") "
////            		+ "values "
////            		+ "("
////            		+ "'"+name+"', "
////    				+ "'"+lastname+"'"
////					+ ")"
////					+ "");
////            String sql = ""
////            		+ "insert into person ("
////            		+ "name, "
////            		+ "lastname"
////            		+ ") "
////            		+ "values "
////            		+ "("
////            		+ "?, "
////    				+ "?"
////					+ ");";
//            executeSet(name, lastname);
//            executeSet(getId(name), String.valueOf(pw));
////        	generateActualSql(sql, name, lastname);
////            executeSet("insert into "
////            		+ "login ("
////            		+ "p_id, "
////            		+ "p_password"
////            		+ ") values ("
////            		+ ""+getId(name)+", "
////    				+ "'"+pw+"'"
////					+ ")");
////            sql = "insert into "
////            		+ "login ("
////            		+ "p_id, "
////            		+ "p_password"
////            		+ ") values ("
////            		+ "?, "
////    				+ "?"
////					+ ");";
////            executeSet(getId(name), pw);
////            generateActualSql(sql, getId(name), pw);
//        }
        insert(1, "Anna", "is-eating", "a cookie");
        insert(2, "Henry", "does", "a handstand");
        insert(3, "Sarah", "", "a pen");
        insert(4, "John", "tells", "");
        insert(5, "", "", "a drama");
    }
    void insert(int position, String text, String action, String action_name)
    {
        try {
            String sql = ""
                    +" insert into person ("
                    + "position,"
                    + "name,"
                    + "action,"
                    + "action_name"
                    + ") values ("
                    + "";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?,?,?"+");");
            stmt.setInt(1, position);
            stmt.setString(2, text);
            stmt.setString(3, action);
            stmt.setString(4, action_name);
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        } catch (Exception e) {
            logger.error("insert into person", e);
        }
    }
    /**
     * is permitted == admin ?
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
//            ResultSet resultSet = executeGet("SELECT id, name, password, admin FROM person where admin = 1");
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
     * execute sql cmd
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
     * get meta data
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
     * execute sql cms
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
     * insert data for admin
     * @param element1
     * @param element2
     */
//    void executeSet(String name, String lastname, String password)
    void insertAdminData(String name, String lastname, String password)
    {
        try
        {
//            executeSet("insert into person (name, lastname) values ('admin', 'admin')");
            String sql = "insert into person ("
                    + "name,"
                    + "lastname"
                    + "password"
                    + ") values (";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?,?"+")");
            stmt.setString(1, name);
            stmt.setString(2, lastname);
            stmt.setString(3, password);
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
    }
//    /**
//     * 
//     * @param element1
//     * @param element2
//     */
//    void executeSet(String element1, boolean element2)
//    {
//        try
//        {
////            executeSet("insert into person (name, lastname) values ('admin', 'admin')");
//            String sql = "insert into login ("
//                    + "p_password,"
//                    + "p_admin"
//                    + ") values (";
//            connect();
//            PreparedStatement stmt = connection.prepareStatement(sql+"?,?"+")");
//            stmt.setString(1, element1);
//            stmt.setBoolean(2, element2);
//            logger.info(stmt.toString());
//            stmt.execute();
//            close(null);
//        }
//        catch(SQLException e)
//        {
//            logger.error(e);
//        }
//    }
    /**
     * execute sql for person = name, surname
     * @param firstName
     * @param lastName
     */
    boolean executeSet(String firstName, String lastName)
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
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
            return true;
        }
        catch(SQLException e)
        {
            logger.error(e);
            return false;
        }
    }
    boolean insertData(String name, String action, String action_name)
    {
    	try
    	{
//            executeSet("insert into person (name, lastname) values ('admin', 'admin')");
    		String sql = "insert into person ("
    				+ "name,"
    				+ "action,"
    				+ "action_name"
    				+ ") values (";
    		connect();
    		PreparedStatement stmt = connection.prepareStatement(sql+"?,?,?"+")");
    		stmt.setString(1, name);
    		stmt.setString(2, action);
    		stmt.setString(3, action_name);
    		logger.info(stmt.toString());
    		stmt.execute();
    		close(null);
//    		executeSet(getId(firstName), "");
    		return true;
    	}
    	catch(SQLException e)
    	{
    		logger.error(e);
    		return false;
    	}
    }
    /**
     * execute sql for login = id, password
     * @param id
     * @param password
     */
    void executeSet(int id, String password)
    {
        try
        {
        	if(password.isEmpty()) {
        		password = String.valueOf(getRandom());
        	}
//            executeSet("insert into person (name, lastname) values ('admin', 'admin')");
            String sql = "insert into login ("
                    + "p_id,"
                    + "p_password"
                    + ") values (";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?"+")");
            stmt.setInt(1, id);
            stmt.setString(2, password);
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
    }
//    /**
//     * 
//     * @param element1
//     * @param element2
//     * @param element3
//     */
//    void executeSet(int id, String element2, String element3)
//    {
//        try
//        {
////            executeSet("insert into login (p_id, p_password, p_admin) values (1, 'secret', 'true')");
//            String sql = "insert into login ("
//                    + "p_id,"
//                    + "p_password,"
//                    + "p_admin"
//                    + ") values (";
//            connect();
//            PreparedStatement stmt = connection.prepareStatement(sql+"?,?,?"+")");
//            stmt.setInt(1, id);
//            stmt.setString(2, element2);
//            stmt.setString(3, element3);
//            logger.info(stmt.toString());
//            stmt.execute();
//            close(null);
//        }
//        catch(SQLException e)
//        {
//            logger.error(e);
//        }
//    }
    /**
     * insert data for admin
     * @param element1
     * @param element2
     * @param element3
     */
    void insertAdminData(int element1, String element2)
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
            stmt.setBoolean(3, true);
            // test
//            stmt.setBoolean(3, admin);
//            stmt.setInt(
//                    3,
//                    admin?
//                        1
//                        :
//                        0
//                );
            logger.info(stmt.toString());
            stmt.execute();
            close(null);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
    }
//    /**
//     * 
//     * @param element1
//     * @param element2
//     */
//    void executeSet(int element1, int element2)
//    {
//        try
//        {
////            executeSet("insert into login (p_id, p_password) values (1, 'secret')");
//            String sql = "insert into login ("
//                    + "p_id,"
//                    + "p_password"
//                    + ") values (";
//            connect();
//            PreparedStatement stmt = connection.prepareStatement(sql+"?,?"+")");
//            stmt.setInt(1, element1);
//            stmt.setInt(2, element2);
//            logger.info(stmt.toString());
//            stmt.execute();
//            close(null);
//        }
//        catch(SQLException e)
//        {
//            logger.error(e);
//        }
//    }
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
    ArrayList<Person> getDataFromDBWithoutHeader(String sql, boolean admin)
    {
    	ArrayList<Person> data = new ArrayList<Person>();
    	try
    	{
//        	ResultSet resultSet = executeGet(sql);
//        	ResultSetMetaData rsmd = resultSet.getMetaData();
        	data = getDataFromDB(sql, admin);
    	}
    	catch(Exception e)
    	{
    		logger.error(e);
    	}
    	return data;
    }
    /**
     * get data from db, choose admin or not
     * @param sql
     * @param resultSet
     * @param rsmd
     * @return
     */
//    ArrayList<Person> getDataFromDB(String sql, ResultSet resultSet, boolean admin)
    ArrayList<Person> getDataFromDB(String sql, boolean admin)
    {
        ResultSet resultSet = executeGet(sql);
    	ArrayList<Person> data = new ArrayList<Person>();
        try
        {
            Person person;
            if(admin) {
                while(resultSet != null && resultSet.next())
                {
//                	person = new Person(
//                	        resultSet.getString("name"),
//                	        resultSet.getString("lastname"),
//                	        false
//            	        );
//                	person = new Person();
//                	person.id = DatabaseObject.toInt(resultSet.getString("id"));
//                	person.firstName = resultSet.getString("name");
//                	person.lastName = resultSet.getString("lastName");
//                	person.password = resultSet.getString("password");
//                	person.isAdmin = resultSet.getInt("admin");
//                	data.add(person);
                	person = new Person();
                	person.setN(
                	        Database.toInt(resultSet.getString("position"))
                	        );
                	person.setO(
                	        resultSet.getString("name")
                	        );
                	person.setS(
                	        resultSet.getString("action")
                	        );
                	person.setV(
                	        resultSet.getString("action_name")
                	        );
                	data.add(person);
                }
            } else {
                while(resultSet.next())
                {
//                    person = new Person(
//                            DatabaseObject.toInt(resultSet.getString("id")),
//                            resultSet.getString("name"),
//                            resultSet.getString("lastname"),
//                            resultSet.getString("password"),
//                            false
//                            );
//                    person = new Person();
//                    person.firstName = resultSet.getString("name");
//                    person.lastName = resultSet.getString("lastName");
//                    data.add(person);
                    person = new Person();
                    person.setN(
                            Database.toInt(resultSet.getString("position"))
                            );
                    person.setO(
                            resultSet.getString("name")
                            );
                    person.setS(
                            resultSet.getString("action")
                            );
                    person.setV(
                            resultSet.getString("action_name")
                            );
                    data.add(person);
                }
            }
            close(resultSet);
        }
        catch(SQLException e)
        {
        	logger.error(e);
        }
        return data;
    }
//    /**
//     * 
//     * @param sql
//     * @return
//     */
//    ArrayList<Person> getDataFromDBWithHeader(String sql, boolean admin)
//    {
//    	ArrayList<Person> data = new ArrayList<Person>();
//    	ArrayList<Person> header = new ArrayList<Person>();
//    	ArrayList<Person> content = new ArrayList<Person>();
//    	try
//    	{
////    		connect();
//    		ResultSet resultSet = executeGet(sql);
////    		createDatabaseIfNotExists();
//    		// get header
////    		ResultSetMetaData rsmd = getMetaData(sql);
////    		ArrayList<Person> temp = new ArrayList<Person>();
//    		Person person = new Person();
//    		ResultSetMetaData rsmd = getMetaData(sql);
////            ArrayList <String> temp = new ArrayList<String>();
//    		if(admin) {
//    		    if(headerInUppercaseCharacter) {
//    		        person.header_id = rsmd.getColumnName(1).toUpperCase();
//    		        person.header_firstName = rsmd.getColumnName(2).toUpperCase();
//    		        person.header_lastName = rsmd.getColumnName(3).toUpperCase();
//    		        person.header_password = rsmd.getColumnName(4).toUpperCase();
//    		    } else {
//    		        person.header_id = rsmd.getColumnName(1).toLowerCase();
//    		        person.header_firstName = rsmd.getColumnName(2).toLowerCase();
//    		        person.header_lastName = rsmd.getColumnName(3).toLowerCase();
//    		        person.header_password = rsmd.getColumnName(4).toLowerCase();
//    		    }
//    		} else {
//    		    if(headerInUppercaseCharacter) {
//    		        person.header_firstName = rsmd.getColumnName(1).toUpperCase();
//    		        person.header_lastName = rsmd.getColumnName(2).toUpperCase();
//    		    } else /* if(headerInUppercaseCharacter) */{
//    		        person.header_firstName = rsmd.getColumnName(1).toLowerCase();
//    		        person.header_lastName = rsmd.getColumnName(2).toLowerCase();
//    		    }
//    		}
//    		header.add(person);
//    		// get content
//    		content = getDataFromDB(sql, resultSet, admin);
//    		close(resultSet);
//    		// migrate
//    		for(Person migrate: header)
//    		{
//    			data.add(migrate);
//    		}
//    		for(Person migrate: content)
//    		{
//    			data.add(migrate);
//    		}
//    	}
//    	catch(Exception e)
//    	{
//    		logger.error(e);
//    	}
//    	return data;
//    }
    /**
     * close db connection
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
	public boolean insertData(String[] data)
	{
		if(insertData(data[0], data[1], data[2])) {
			return true;
		} else {
			return false;
		}
	}
}  
