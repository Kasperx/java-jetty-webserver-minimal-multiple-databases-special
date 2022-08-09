package main.java.com.mywebsite.database.DAO;

import java.sql.Connection;

import main.java.com.mywebsite.main.DAO.Dao_Main;

public class Dao_DBConnect extends Dao_Main
{
    protected Connection connection;
    protected String serverIp;
    protected String path;
}
