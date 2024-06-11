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
import java.util.List;

import com.github.javafaker.Faker;
import main.java.com.mywebsite.Data.Person;
import org.apache.logging.log4j.LogManager;

public class DatabaseSQLite extends Database
{  
    String path = System.getProperty("user.dir")+"/test.db";
    Connection connection = null;
    public DatabaseSQLite()
    {
        logger = LogManager.getLogger(this.getClass().getName());
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
                + " surname,"
        		+ " action,"
        		+ " action_name"
        		+ " FROM"
        		+ " person;";
//        ArrayList<Person> data = getDataFromDBWithHeader(sql, false);
        ArrayList<Person> data = getDataFromDBWithoutHeader(sql, false);
        return data;
    }
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
            //////////////////////////////
            executeSet("create table if not exists person ("
                    + "id integer primary key autoincrement,"
                    + "position integer,"
                    + "name text,"
                    + "surname text,"
                    + "action text,"
                    + "action_name text"
                    + ");");
            /*
             *  table login with boolean.. but seems problem with jdbc -> sqlite
             *  sometimes boolean = int, sometimes not :(
             *  so boolean = int and works. remember for other dbs
             */
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
        insert(1, "Anna", "", "is-eating", "a cookie");
        insert(2, "Henry", "", "does", "a handstand");
        insert(3, "Sarah", "", "", "a pen");
        insert(4, "John", "", "tells", "");
        insert(5, "", "", "", "a drama");
        Faker faker;
        for(int i=6; i<16; i++){
            faker = new Faker();
            insert(i, faker.name().firstName(), faker.name().lastName(), faker.address().country(), faker.address().cityName());
        }
    }
    void insert(int position, String name, String surname, String action, String action_name)
    {
        try {
            String sql = ""
                    +" insert into person ("
                    + "position,"
                    + "name,"
                    + "surname,"
                    + "action,"
                    + "action_name"
                    + ") values ("
                    + "";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql+"?,?,?,?,?"+");");
            stmt.setInt(1, position);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, action);
            stmt.setString(5, action_name);
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
    /**
     * insert data to db
     * @param name
     * @param action
     * @param action_name
     * @return
     */
    boolean insertData(String position, String name, String action, String action_name)
    {
    	try
    	{
//            executeSet("insert into person (name, lastname) values ('admin', 'admin')");
    		String sql = "insert into person ("
    		        + "position,"
    				+ "name,"
    				+ "action,"
    				+ "action_name"
    				+ ") values (";
    		connect();
    		PreparedStatement stmt = connection.prepareStatement(sql+"?,?,?,?"+")");
    		stmt.setInt(1, toInt(position));
    		stmt.setString(2, name);
    		stmt.setString(3, action);
    		stmt.setString(4, action_name);
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
    /**
     * remove data from db
     * @param name
     * @param action
     * @param action_name
     * @return
     */
    boolean removeData(String name, String action, String action_name)
    {
        try
        {
            String sql = "delete from person where "
                    + "name = ?,"
                    + "action = ?,"
                    + "action_name = ?;";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
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
     * 
     * @param name
     * @param action
     * @param action_name
     * @return
     */
    boolean updateData(String name, String action, String action_name)
    {
        try
        {
            String sql = "update person (name, action, action_name) "
                    + "values ("
                    + "name = ?,"
                    + "action = ?,"
                    + "action_name = ?"
                    + ");";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, action);
            stmt.setString(3, action_name);
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
    /**
     * insert data for admin
     * @param element1
     * @param element2
     */
    void insertAdminData(int element1, String element2)
    {
        try
        {
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
    ArrayList<Person> getDataFromDBWithoutHeader(String sql, boolean admin)
    {
    	ArrayList<Person> data = new ArrayList<Person>();
    	try
    	{
        	data = getDataFromDB(sql, admin);
    	}
    	catch(Exception e)
    	{
    		logger.error(e);
    	}
    	return data;
    }
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
                	person = new Person();
                	person.setPosition(
                	        Database.toInt(resultSet.getString("position"))
                	        );
                	person.setVorname(
                	        resultSet.getString("name")
                	        );
                    person.setNachname(
                            resultSet.getString("surname")
                    );
                	person.setActivity(
                	        resultSet.getString("action")
                	        );
                	person.setActivity_name(
                	        resultSet.getString("action_name")
                	        );
                	data.add(person);
                }
            } else {
                while(resultSet.next())
                {
                    person = new Person();
                    person.setPosition(
                            Database.toInt(resultSet.getString("position"))
                            );
                    person.setVorname(
                            resultSet.getString("name")
                            );
                    person.setNachname(
                            resultSet.getString("surname")
                    );
                    person.setActivity(
                            resultSet.getString("action")
                            );
                    person.setActivity_name(
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
	@Override
	public boolean insertData(String[] data)
	{
	    // get next position number
	    if(data != null && data.length > 0 && toInt(data[0]) == 0) {
	        data[0] = String.valueOf(getNextPosition());
	    }
		if(insertData(data[0], data[1], data[2], data[3])) {
			return true;
		} else {
			return false;
		}
	}
    @Override
    public boolean removeData(String[] data)
    {
        if(removeData(data[0], data[1], data[2])) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean updateData(String[] data)
    {
        if(updateData(data[0], data[1], data[2])) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * get next int for position (no sql key)
     * @return
     */
    public int getNextPosition()
    {
        connect();
        String sql = ""
                + "SELECT"
                + " position"
                + " FROM"
                + " person"
                + ";";
        int findNextPos = 1;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            List<Integer> positions = new ArrayList<Integer>();
            while(resultSet.next())
            {
                int position = resultSet.getInt("position");
                if(positions.contains(position)) {
                    continue;
                } else {
                    positions.add(position);
                }
            }
            findNextPos = positions.size()+1;
            close(resultSet);
        }
        catch(SQLException e)
        {
            logger.error(e);
        }
        return findNextPos;
    }
}  
