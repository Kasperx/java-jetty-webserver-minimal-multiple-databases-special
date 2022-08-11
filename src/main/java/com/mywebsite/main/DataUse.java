package main.java.com.mywebsite.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.google.gson.Gson;

import main.java.com.mywebsite.Data.Person;
import main.java.com.mywebsite.common.logger.Logger;
import main.java.com.mywebsite.common.logger.LoggerConfig;
import main.java.com.mywebsite.database.Database.DatabaseType;
import main.java.com.mywebsite.main.DAO.Dao_Main;
import main.java.com.mywebsite.database.DatabaseFile;
import main.java.com.mywebsite.database.Database;
import main.java.com.mywebsite.database.DatabaseSQLite;

public class DataUse extends Dao_Main
{
    static Database databasesource;
    String httpbase;
    static String htmlhead_halfSize;
    static String htmlhead_fullSize;
    static String htmlend;
    static Database.DatabaseType databaseType;
    static Logger logger = LoggerConfig.getLogger(DatabaseFile.class.getName());
    
    public DataUse()
    {
        ///////////////////////////////////////////
        // get libs from online
        htmlhead_halfSize = ""
                + "<!DOCTYPE html>"
                + "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
                + "<meta charset=\"utf-8\">"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
                + "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>"
                + "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>"
                + "</head>"
                + "<body class=\"container\" style=\"font-size:30px\";>"
//                + "<body>"
//                + "<div class=\"container\" style=\"font-size:30px\";>"
                ;
        htmlhead_fullSize = ""
        		+ "<!DOCTYPE html>"
        		+ "<head>"
        		+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
        		+ "<meta charset=\"utf-8\">"
        		+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
        		+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>"
        		+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>"
        		+ "</head>"
        		+ "<body class=\"container-fluid\" style=\"font-size:30px\";>"
//                + "<body>"
//                + "<div class=\"container\" style=\"font-size:30px\";>"
;
        // get libs from offline
//        htmlhead = ""
//                + "<!DOCTYPE html>\n"
//                + "<head>"
//                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
//                + "<meta charset=\"utf-8\">"
//                + "<script src=\"libraries/jquery-3.3.1.min.js\"></script>"
//                + "<script src=\"libraries/bootstrap-3.3.7-dist/js/bootstrap.js\"></script>"
//                + "<script src=\"libraries/bootstrap-3.3.7-dist/js/bootstrap.min.js\"></script>"
//                + "<link rel=\"stylesheet\" href=\"libraries/bootstrap-3.3.7-dist/css/bootstrap.min.css\">"
//                + "<script src=\"libraries/moment.js\"></script>\n"
//                + "<script src=\"libraries/moment-with-locales.js\"></script>"
//                + "</head>"
//                + "<body class=\"container\">"
//                ;
        htmlend = ""
                + "</body>"
                ;
        databaseType = Database.DatabaseType.getValue();
        databasesource = Database.getInstance(databaseType);
//        databasesource = Database.getInstance(Database.DatabaseType.file);
//        databasesource = Database.getInstance(Database.DatabaseType.postgres);
        databasesource.setHeaderInUppercaseCharacter(true);
        databasesource.getProperties(System.getProperty("user.dir")+File.separator+"login.txt");
    }
    /**
     * 
     * @param httpbase
     */
    public void sethttpbase(String httpbase)
    {
        this.httpbase = httpbase;
    }
    /**
     * 
     * @param request
     * @param response
     */
    public void clientRequest_Website (HttpServletRequest request, HttpServletResponse response)
    {
        try {
            // value of filenames by client come with a slash, but java doesn't find files with slash, so cut first char...
            String wantedFileFromClient = httpbase+File.separator+request.getServletPath().substring(1);
            String fileContent;
            if(new File(wantedFileFromClient+"index.html").exists()) {
                wantedFileFromClient += "index.html";
            }
            fileContent = readFile(wantedFileFromClient);
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            if(wantedFileFromClient.endsWith(".html")) {
            	response.setContentType("text/html");
            }
            else if(wantedFileFromClient.endsWith(".css")) {
                response.setContentType("text/css");
            }
            else if(wantedFileFromClient.endsWith(".js")){
                response.setContentType("application/json");
            }
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setStatus(HttpServletResponse.SC_OK);
            response.reset();
            out.print(fileContent);
            //out.close();
        }
        catch (Exception e) {
            logger.error(e);
        }
    }
    /**
     * 
     * @param request
     * @param response
     */
    public void clientRequest_WebsiteFromBackend(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
        	initOptions(request);
            logger.info("Found request: "+request.getParameter("get"));
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            String websitedata = htmlhead_halfSize;
            websitedata += "<table class=\"table\">";
//            websitedata += "<tr>"
//                    + "<th>name</th>"
//                    + "</tr>"
//                    + "</thead>";
            websitedata += ""
                    + "<ul>"
                    + "<div> some codes </div>"
//                    + "<li><a href=\"localtion/?get=example\" target=\"_blank\">read from db</a></li>"
//                    + "<li><a href=\"localtion/?get=insert\" target=\"_blank\">insert data</a></li>"
//                    + "<li><a href=\"localtion/?get=data\" target=\"_blank\">get data</a></li>"
                    + "<li><a href=\"?get=insert\">insert data</a></li>"
                    + "<li><a href=\"?get=data\">get data</a></li>"
                    + "<li><a href=\"?get=admin\">get all data</a></li>"
                    + "</ul>"
                    ;
            websitedata += "</thead>";
            websitedata += "</table>";
            websitedata += htmlend;
            response.getWriter().println(websitedata);
//            PrintWriter out = response.getWriter();
//            out.print(result);
//            out.close();
        }
        catch (Exception e) {
            logger.error(e);
        }
    }
    /**
     * 
     * @param request
     * @param response
     */
    public static void clientRequest_Weather(HttpServletRequest request, HttpServletResponse response)
    {
        try {
        	initOptions(request);
            logger.info("Found request: "+request.getParameter("get"));
            String url = "https://dwd.api.bund.dev/stationOverviewExtended";
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//            Credentials credentials = new UsernamePasswordCredentials("username", "password");
            Credentials credentials = new UsernamePasswordCredentials("", "");
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
            HttpClient client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
//            HttpPost requ = new HttpPost(url);
            //////////////////
            URIBuilder builder = new URIBuilder();
//            builder.setScheme("https").setHost("dwd.api.bund.dev").setPath("/stationOverviewExtended")
//                .setParameter("stationIds", "NW");
            // https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
            builder
            .setScheme("https")
            .setHost("api.openweathermap.org")
            .setPath("/data/2.5/weather")
//            .setParameter("zip", "E14")
            .setParameter("lon", "51.5135872")
            .setParameter("lat", "7.4652981")
            .setParameter("appid", databasesource.getProperty("key"));
            URI uri = builder.build();
            logger.info(uri.toString());
            HttpGet httpget = new HttpGet(uri);
            //////////////////
//            HttpResponse resp = client.execute(requ);
            HttpResponse resp = client.execute(httpget);
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            String line = "";
            String textView = "";
            while ((line = rd.readLine()) != null)
            {    
                textView += line;
                logger.info(textView);
            }
            rd.close();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().append(textView);
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }
//    /**
//     * @param request
//     * @param response
//     */
//    public static void clientRequest_GetData(HttpServletRequest request, HttpServletResponse response)
//    {
//        try
//        {
//            logger.info("Found request: "+request.getParameter("get"));
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("text/html");
//            response.setStatus(HttpServletResponse.SC_OK);
//            ArrayList <ArrayList<String>> data = databasesource.getData();
//            String websitedata = fillWebsiteWithData(data);
//            logger.info(websitedata);
//            response.getWriter().append(websitedata);
//        }
//        catch (Exception e)
//        {
//            logger.error(e);
//        }
//    }
    /**
     * @param request
     * @param response
     */
    public static void clientRequest_GetData(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
        	initOptions(request);
            logger.info("Found request: "+request.getParameter("get"));
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
//            JSONObject data = databasesource.getDataJson();
            ArrayList<Person> data = databasesource.getData();
            String websitedata = null;
            if(useJson) {
                response.setContentType("application/json");
                Gson gson = new Gson();
                websitedata = gson.toJson(data);
            } else {
                response.setContentType("text/html");
                websitedata = fillWebsiteWithData(data);
                logger.info(websitedata);
            }
            response.getWriter().append(websitedata);
            
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }
//    public static void clientRequest_CallDataFromDb(HttpServletRequest request, HttpServletResponse response)
//    {
//    	try
//    	{
//    		logger.info("Found request: "+request.getParameter("get"));
////            DatabaseSQLite database = new DatabaseSQLite();
//    		ArrayList <ArrayList<String>> data = databasesource.getData();
//    		databasesource.close();
////            response.getWriter().println("{ \"status\": \"ok\"}");
////            array = new JSONArray(data);
////            json_mapForJSON = new JSONObject();
////            json_mapForJSON.put("Tablenames", array);
////            String result = new GsonBuilder().create().toJson(json_mapForJSON);
//    		response.setCharacterEncoding("utf-8");
////            response.setContentType("application/json");
//    		response.setContentType("text/html");
//    		response.setStatus(HttpServletResponse.SC_OK);
//    		String websitedata = htmlhead;
//    		websitedata += "<table class=\"table\">";
//    		websitedata += "<tr>"
//    				+ "<th>name</th>"
//    				+ "</tr>"
//    				+ "</thead>";
////            for(String temp: data)
////            {
////                websitedata += "<tr><td>";
////                websitedata += temp;
////                websitedata += "</tr></td>";
////            }
//    		for(ArrayList <String> tempList: data)
//    		{
//    			websitedata += "<tr>";
//    			for(String temp: tempList)
//    			{
//    				websitedata += "<td>";
//    				websitedata += temp;
//    				websitedata += "</td>";
//    			}
//    			websitedata += "</tr>";
//    		}
//    		websitedata += "</thead>";
//    		websitedata += "</table>";
//    		websitedata += htmlend;
////            response.getWriter().println(websitedata);
//    		response.getWriter().append(websitedata);
////            PrintWriter out = response.getWriter();
////            out.print(result);
////            out.close();
//    	}
//    	catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    }
    /**
     * 
     * @param request
     * @param response
     */
    public static void clientRequest_askUserData(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            String websitedata = htmlhead_halfSize;
            websitedata += "<form>";
            websitedata += ""
                    + "<label for=\"name\">name:</label><br>"
                    + "<input type=\"text\" id=\"name\"><br>"
                    + "<label for=\"pwd\">password:</label><br>"
                    + "<input type=\"password\" id=\"password\""
//                    + "pattern=\"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\" title=\"<Must contain at least one number and one uppercase and lowercase letter and at least 8 or more characters\" required"
                    + "><br>"
                    + "<input type=\"submit\" value=\"Submit\">"
                    ;
            websitedata += "</form>";
            websitedata += "<script>";
            websitedata += ""
                    + "const form = document.getElementById('name');\n"
                    + "const log = document.getElementById('password');\n"
                    + "form.addEventListener('submit', logSubmit);";
            websitedata += "</script>";
            websitedata += htmlend;
            response.getWriter().println(websitedata);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
//    /**
//     * 
//     * @param request
//     * @param response
//     */
//    public static void clientRequest_GetAllData(HttpServletRequest request, HttpServletResponse response)
//    {
//        try
//        {
//            logger.info("Found request: "+request.getParameter("get"));
//            logger.info("Found request: "+request.getParameter("name"));
//            String name = request.getParameter("user");
//            String pw = request.getParameter("pw");
//            if(databasesource.isPermitted(name, pw))
//            {
//                response.setCharacterEncoding("utf-8");
////            response.setContentType("application/json");
//                response.setContentType("text/html");
//                response.setStatus(HttpServletResponse.SC_OK);
//                ArrayList <ArrayList<String>> data = databasesource.getAllData();
//                String websitedata = fillWebsiteWithData(data, true);
//                response.getWriter().println(websitedata);
//            }
//            else
//            {
//                response.setCharacterEncoding("utf-8");
//                response.setContentType("application/json");
//                response.setStatus(HttpServletResponse.SC_OK);
//                response.getWriter().println("{ \"status\": \"wrong data\"}");
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    /**
     * 
     * @param request
     * @param response
     */
    public static void clientRequest_GetAllData(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
        	initOptions(request);
            logger.info("Found request: "+request.getParameter("get"));
            logger.info("Found request: "+request.getParameter("name"));
        	String name = request.getParameter("user");
        	String pw = request.getParameter("pw");
            if(databasesource.isPermitted(name, pw))
            {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setCharacterEncoding("utf-8");
                ArrayList<Person> data = databasesource.getAllData();
                String websitedata = null;
                if(useJson) {
                    response.setContentType("application/json");
                    Gson gson = new Gson();
                    websitedata = gson.toJson(data);
                } else {
                    response.setContentType("text/html");
                    websitedata = fillWebsiteWithData(data, true);
                    logger.info(websitedata);
                }
                // without json, delete
//                response.setContentType("text/html");
//                response.setStatus(HttpServletResponse.SC_OK);
//                ArrayList <ArrayList<String>> data = databasesource.getAllData();
//                String websitedata = fillWebsiteWithData(data, true);
                response.getWriter().println(websitedata);
            }
            else
            {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("{ \"status\": \"wrong data\"}");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
//    /**
//     * 
//     * @param data
//     * @return
//     */
//    private static String fillWebsiteWithData(ArrayList<Person> data)
//    {
//    	return fillWebsiteWithData(data, false);
//    }
    /**
     * @param data
     * @param admin
     * @return
     */
	private static String fillWebsiteWithData(ArrayList<Person> data, boolean admin)
	{
		String websitedata;
		websitedata = htmlhead_fullSize;
		websitedata += "<h1 style='font-size:50px;'><marquee>Admin view</marquee></h1></p>";
		websitedata += "<table class=\"table table-striped\">";
		websitedata += "<thead>";
		for(int row=0; row<data.size(); row++)
		{
			// build header
			Person person = data.get(row);
			if(row==0)
			{
				websitedata += "<thead>";
//				for(Person temp: tempList)
//				{
//					websitedata += ""
//						+ "<th>"
//						+ temp
//						+ "</th>";	
//				}
				websitedata += ""
			        + "<tr>"
//			        + "<th>"+person.header_firstName+"</th>"
//			        + "<th>"+person.header_lastName+"</th>"
			        + "<th>"+person.getHeader_n()+"</th>"
			        + "<th>"+person.getHeader_o()+"</th>"
			        + "<th>"+person.getHeader_s()+"</th>"
			        + "<th>"+person.getHeader_v()+"</th>"
	                + "</tr>"
			        ;	
				websitedata += "</thead>";
			}
			// build data
			else
			{
				websitedata += ""
			        + "<tr>"
			        + "<th>"+person.getN()+"</th>"
			        + "<th>"+person.getO()+"</th>"
			        + "<th>"+person.getS()+"</th>"
			        + "<th>"+person.getV()+"</th>"
	                + "</tr>"
			        ;	
				websitedata += "</thead>";
				websitedata += "</tr>";
			}
		}
		// Add extra line for user
		if(!admin) {
    		websitedata += "<tr>";
    		websitedata += "<td>";
    		websitedata += "<input placeholder='insert name'>";
    		websitedata += "</td>";
    		websitedata += "<td>";
    		websitedata += "<input placeholder='insert surname'></input>";
    		websitedata += "</td>";
    		websitedata += "</tr>";
    		websitedata += "<tr>";
    		websitedata += "<td>";
    		websitedata += "<button>Send</button>";
    		websitedata += "</td>";
    		websitedata += "<td>";
    		websitedata += "<div></div>";
    		websitedata += "</td>";
    		websitedata += "</tr>";
		}
		// Add extra line for user
		websitedata += "</table>";
		websitedata += htmlend;
		return websitedata;
	}
	private static String fillWebsiteWithData(ArrayList<Person> data)
	{
	    String websitedata;
        websitedata = htmlhead_halfSize;
        websitedata += "<h1 style='font-size:50px;'><marquee>User view</marquee></h1></p>";
	    websitedata += "<table class=\"table table-striped\">";
	    websitedata += "<thead>";
	    for(int row=0; row<data.size(); row++)
	    {
	        // build header
	        Person person = data.get(row);
	        if(row==0)
	        {
	            websitedata += "<thead>";
//				for(Person temp: tempList)
//				{
//					websitedata += ""
//						+ "<th>"
//						+ temp
//						+ "</th>";	
//				}
	            websitedata += ""
                    + "<tr>"
                    + "<th>"+person.getHeader_n()+"</th>"
                    + "<th>"+person.getHeader_o()+"</th>"
                    + "<th>"+person.getHeader_s()+"</th>"
                    + "<th>"+person.getHeader_v()+"</th>"
                    + "</tr>"
                    ;	
	            websitedata += "</thead>";
	        }
	        // build data
	        else
	        {
	            websitedata += ""
                    + "<tr>"
                    + "<th>"+person.getN()+"</th>"
                    + "<th>"+person.getO()+"</th>"
                    + "<th>"+person.getS()+"</th>"
                    + "<th>"+person.getV()+"</th>"
                    + "</tr>"
                    ;	
	            websitedata += "</thead>";
//				for(String temp: tempList)
//				{
//		            websitedata += "<td>";
//		            if(admin) {
//		                if(temp.equals(String.valueOf(1))) {
//		                    websitedata += "yes";
//    		            } else if(temp.equals(String.valueOf(0))) {
//    		                websitedata += "no";
//    		            } else {
//    		                websitedata += temp;
//    		            }
//	                } else {
//	                    websitedata += temp;
//	                }
//		            websitedata += "</td>";
//				}
	            websitedata += "</tr>";
	        }
	    }
	    // Add extra line for user
        websitedata += "<tr>";
        websitedata += "<td>";
        websitedata += "<input placeholder='insert name'>";
        websitedata += "</td>";
        websitedata += "<td>";
        websitedata += "<input placeholder='insert surname'></input>";
        websitedata += "</td>";
        websitedata += "</tr>";
        websitedata += "<tr>";
        websitedata += "<td>";
        websitedata += "<button>Send</button>";
        websitedata += "</td>";
        websitedata += "<td>";
        websitedata += "<div></div>";
        websitedata += "</td>";
        websitedata += "</tr>";
	    // Add extra line for user
	    websitedata += "</table>";
	    websitedata += htmlend;
	    return websitedata;
	}
//    public static void clientRequest_GetAllData(HttpServletRequest request, HttpServletResponse response)
//    {
//        try
//        {
//            JSONObject json_mapForJSON = null;
//            logger.info("Found request: "+request.getParameter("get"));
//            logger.info("Found request: "+request.getParameter("name"));
//            ////////////////////
//            DatabaseConnect database = new DatabaseConnect();
//            if(database.isPermitted(request.getParameter("name"), request.getParameter("pw")))
//            {
//                logger.info();
//            }
//            ArrayList <String> data = database.getData();
//            JSONArray array = new JSONArray();
//            array = new JSONArray(data);
//            json_mapForJSON = new JSONObject();
//            json_mapForJSON.put("Tablenames", array);
//            String result = new GsonBuilder().create().toJson(json_mapForJSON);
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("application/json");
//            response.setStatus(HttpServletResponse.SC_OK);
//            PrintWriter out = response.getWriter();
//            out.print(result);
//            out.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
	/**
	 * 
	 * @param request
	 * @param response
	 */
    public static void clientRequest_InsertDataToDb(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
        	initOptions(request);
            logger.info("Found request: "+request.getParameter("get"));
            databasesource.createDatabaseIfNotExists();
            /*
             * Yes, works normally on its own, but for different database models program needs different object-casts
             * because method is not content of normal code within abstract environment.
             */
            switch (databaseType)
            {
			case sqlite:
				((DatabaseSQLite)databasesource.getInstance()).insertData();
				break;
			case file:
				((DatabaseFile)databasesource.getInstance()).insertData();
				break;
			default:
				break;
			}
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{ \"status\": \"ok\"}");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 
     * @param request
     * @param response
     */
    public static void clientRequest_UseJson(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            logger.info("Found request: "+request.getParameter("get"));
            /*
             * Yes, works normally on its own, but for different database models program needs different object-casts
             * because method is not content of normal code within abstract environment.
             */
            String websitedata = null;
//            ArrayList<String> json = new ArrayList<String>();
            Map<String, Boolean> json = new HashMap<String, Boolean>();
            if(useJson) {
//                websitedata = "json:'yes'";
//            	json.add("json:true");
            	json.put("json", true);
            } else {
//                websitedata = "json:'no'";
//            	json.add(false);
//            	json.add("json:false");
            	json.put("json", false);
            }
            Gson gson = new Gson();
            websitedata = gson.toJson(json);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 
     * @param request
     * @param response
     */
    public static void clientRequest_AddUser(HttpServletRequest request, HttpServletResponse response)
    {
    	try
    	{
    		logger.info("Found request: "+request.getParameter("get : "
    				+request.getParameter("name")
    				+request.getParameter("action")
    				+request.getParameter("action_name")
    				));
    		String name = writeFirstCharacterUpperCase(request.getParameter("name"));
    		String action = request.getParameter("action");
    		String action_name = request.getParameter("action_name");
    		/*
    		 * Yes, works normally on its own, but for different database models program needs different object-casts
    		 * because method is not content of normal code within abstract environment.
    		 */
//    		String websitedata = null;
//    		if(useJson) {
//    			websitedata = "json:yes";
//    		} else {
//    			websitedata = "json:no";
//    		}
//    		Gson gson = new Gson();
//    		websitedata = gson.toJson(websitedata);
//    		response.setCharacterEncoding("utf-8");
//    		response.setContentType("application/json");
    		if(name.isEmpty() && action.isEmpty() && action_name.isEmpty()) {
    		    ;
    		} else {
        		databasesource.insertData(
        		        new String[] {name, action, action_name}
        		        );
        		response.setStatus(HttpServletResponse.SC_OK);
    		}
//    		response.getWriter().println(websitedata);
    	} catch (Exception e) {
    		logger.error("request insert with data", e);
    	}
    }
    /**
     * 
     * @param dataRows
     * @return
     */
    private List <List<String>> vectorToArrayList2D (Vector dataRows)
    {
        List <List<String>> array = new ArrayList<List<String>>();
        for (int i=0; i<dataRows.size(); i++)
        {
             Vector data_cols = (Vector)dataRows.elementAt(i);
             ArrayList <String> temp = new ArrayList<String>();
             for(int col=0; col<data_cols.size(); col++) {
                 temp.add(data_cols.elementAt(col).toString());
             }
             array.add(temp);
        }
        return array;
    }
    /**
     * 
     * @param fileName
     * @return
     */
    private String readFile (String fileName)
    {        
        List<String> filecontent = loadFile(fileName);
        if(filecontent == null) {
            return null;
        }
        String ckasl= "";
        for (String temp: filecontent) {
            ckasl += temp + "\n";
        }
        filecontent.clear(); 
        logger.info("Reading file: "+fileName);
        return ckasl;
    }
    /**
     * 
     * @param fpath
     * @return
     */
    public List <String> loadFile(String fpath)
    {
        try
        {
        	if(new File(fpath).exists())
        	{
		        return new ArrayList<String>(
		                Files.readAllLines(
		                        Paths.get(fpath),
		                        StandardCharsets.UTF_8)
		                );
        	}
        	else
        	{
        		new File(fpath).createNewFile();
        		return new ArrayList<String>();
        	}
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 
     * @param array
     * @return
     */
    private ArrayList <String> makeListEntriesUnique (ArrayList <String> array)
    {
        return (ArrayList) array.stream().distinct().collect(Collectors.toList());
    }
    /**
     * 
     */
    static String writeFirstCharacterUpperCase(String text)
    {
        char [] newText = new char[text.length()];
        for(int i=0; i<text.length(); i++) {
            if(i == 0) {
                newText[i] = text.charAt(i);
                String temp = String.valueOf(newText).toUpperCase();
                newText[i] = temp.toCharArray()[0];
            } else {
                newText[i] = text.charAt(i);
            }
        }
        return String.valueOf(newText);
    }
    /**
     * init options for server
     * @param request
     */
    static void initOptions(HttpServletRequest request)
    {
    	String test = request.getParameter("format");
    	if((test = request.getParameter("format")) != null && test.equals("json")) {
    		useJson = true;
    	} else {
    		useJson = false;
    	}
    }
}
