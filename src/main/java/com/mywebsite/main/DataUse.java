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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.mywebsite.database.DAO.Dao_DBConnect;
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

import com.google.gson.Gson;

import main.java.com.mywebsite.Data.Person;
import main.java.com.mywebsite.database.DatabaseFile;
import main.java.com.mywebsite.database.Database;
import main.java.com.mywebsite.database.DatabaseSQLite;
import org.apache.logging.log4j.LogManager;

public class DataUse extends Dao_DBConnect
{
    static Database databasesource;
    String httpbase;
    static String htmlhead_halfSize;
    static String htmlhead_fullSize;
    static String htmlend;
    static Database.DatabaseType databaseType;

    public DataUse()
    {
        logger = LogManager.getLogger(this.getClass().getName());
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
        databasesource.setHeaderInUppercaseCharacter(true);
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
            //////////////////
            URIBuilder builder = new URIBuilder();
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
				websitedata += "<thead><tr>"
			        + "<th>"+person.getHeader_position()+"</th>"
			        + "<th>"+person.getHeader_vorname()+"</th>"
                    + "<th>"+person.getHeader_nachname()+"</th>"
			        + "<th>"+person.getHeader_activity()+"</th>"
			        + "<th>"+person.getHeader_activity_name()+"</th>"
	                + "</tr></thead>";
			}
			// build data
			else
			{
				websitedata += "<tr>"
			        + "<th>"+person.getPosition()+"</th>"
                    + "<th>"+person.getVorname()+"</th>"
                    + "<th>"+person.getNachname()+"</th>"
                    + "<th>"+person.getActivity()+"</th>"
                    + "<th>"+person.getActivity_name()+"</th>"
	                + "</tr>";
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
	            websitedata += "<thead>"
                    + "<tr>"
                    + "<th>"+person.getHeader_position()+"</th>"
                    + "<th>"+person.getHeader_vorname()+"</th>"
                    + "<th>"+person.getHeader_nachname()+"</th>"
                    + "<th>"+person.getHeader_activity()+"</th>"
                    + "<th>"+person.getHeader_activity_name()+"</th>"
                    + "</tr></thead>";
	        }
	        // build data
	        else
	        {
	            websitedata += "<tr>"
                    + "<th>"+person.getPosition()+"</th>"
                    + "<th>"+person.getVorname()+"</th>"
                    + "<th>"+person.getNachname()+"</th>"
                    + "<th>"+person.getActivity()+"</th>"
                    + "<th>"+person.getActivity_name()+"</th>"
                    + "</tr>";
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
				((DatabaseSQLite)Database.getInstance()).insertData();
				break;
			case file:
				((DatabaseFile)Database.getInstance()).insertData();
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
            Map<String, Boolean> json = new HashMap<String, Boolean>();
            if(useJson) {
            	json.put("json", true);
            } else {
            	json.put("json", false);
            }
            Gson gson = new Gson();
            websitedata = gson.toJson(json);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(websitedata);
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
    		        +request.getParameter("position")
    				+request.getParameter("name")
    				+request.getParameter("action")
    				+request.getParameter("action_name")
    				));
    		String position = request.getParameter("position");
    		String name = writeFirstCharacterUpperCase(request.getParameter("name"));
    		String action = request.getParameter("action");
    		String action_name = request.getParameter("action_name");
    		if(position.isEmpty() && name.isEmpty() && action.isEmpty() && action_name.isEmpty()) {
    		    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    		} else {
        		databasesource.insertData(new String[] {
        		        position,
        		        name,
        		        action,
        		        action_name
        		        });
        		response.setStatus(HttpServletResponse.SC_OK);
    		}
    	} catch (Exception e) {
    		logger.error("request insert with data", e);
    	}
    }
    public static void clientRequest_updateUser(HttpServletRequest request, HttpServletResponse response)
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
            if(name.isEmpty() && action.isEmpty() && action_name.isEmpty()) {
                ;
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                databasesource.updateData(new String[] {
                        name,
                        action,
                        action_name
                });
                response.setStatus(HttpServletResponse.SC_OK);
            }
//    		response.getWriter().println(websitedata);
        } catch (Exception e) {
            logger.error("request insert with data", e);
        }
    }
    public static void clientRequest_removeUser(HttpServletRequest request, HttpServletResponse response)
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
            if(name.isEmpty() && action.isEmpty() && action_name.isEmpty()) {
                ;
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                databasesource.removeData(new String[] {
                        name,
                        action,
                        action_name
                });
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            logger.error("request insert with data", e);
        }
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
        String text= "";
        for(String temp: filecontent) {
            text += temp + "\n";
        }
        filecontent.clear(); 
        logger.info("Reading file: "+fileName);
        return text;
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
     */
    static String writeFirstCharacterUpperCase(String text)
    {
        if(text.isEmpty()) {
            return text;
        } else {
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
            text = String.valueOf(newText);
        }
        return text;
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
