package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;

import database.DatabaseConnect;

public class Web
{
    private Server server;
    private static String httpbase = System.getProperty("user.dir");
    private static int httpport = 4000;
    public Web () {}
    private void initHttpService(String httpbase, int port)
    {
        try
        {
        	System.out.println("webfolder: "+Web.httpbase);
        	System.out.println("port = "+httpport);
        	if(httpport < 0) {
        		return;
        	}
        	if (server != null) {
        		server = null;
        	}
        	server = new Server(port);
            ///////////////////////////////////////////////
            // manually handle all requests...
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            ServletHolder sh1 = new ServletHolder(new Web_());
            sh1.setInitParameter("resourceBase", httpbase);
            sh1.setInitParameter("dirAllowed", "true");
            context.addServlet(sh1, httpbase);
            context.setResourceBase(httpbase);
            context.setAllowNullPathInfo(true);
            ServletHolder sh = new ServletHolder(new Web_());
            context.addServlet(sh, "/");
            server.setHandler(context);
            ///////////////////////////////////////////////
            // end: manually handle all requests...
            
//            // automatically handle all requests (not doget, no control)...
//            ResourceHandler resource_handler = new ResourceHandler();
//            resource_handler.setResourceBase(httpbase);
//            //resource_handler.setResourceBase(httpbase+File.separator+"index.html");
//            resource_handler.setDirectoriesListed(true);
//            resource_handler.setWelcomeFiles(new String[]{"index.html"});
//            Path userDir = Paths.get(httpbase);
//            PathResource pathResource = new PathResource(userDir);
//            resource_handler.setBaseResource(pathResource);
//            HandlerList handlers = new HandlerList();
//            handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
//            server.setHandler(handlers);
//            // end: automatically handle all requests (not doget, no control)...
            server.start();
            server.join();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        for(int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-h") || args[i].equals("-help") || args[i].equals("-?") || args[i].equals("?")) {
                showHelp();
            }
            if(args[i].toLowerCase().equals("--httpport"))
            {
                try {
					httpport = Integer.parseInt(args[i + 1]);
				} catch (NumberFormatException e) {}
            }
            if(args[i].toLowerCase().equals("--httpbase"))
            {
                if(new File(args[i + 1]).isDirectory()) {
                    httpbase = args[i + 1];
                }
                else if(new File(args[i + 1]).isFile()) {
                    httpbase = args[i + 1].substring(0, args[i + 1].lastIndexOf("/"));
                }
            }
        }
        if(new File(httpbase).isFile()) {
            httpbase = httpbase.substring(0, httpbase.lastIndexOf("/"));
        }
        new Web().initHttpService(httpbase, httpport);
    }
    private static void showHelp ()
    {
            System.out.println();
            System.out.println("### This program is a webserver with a custom backend that connects to the custom webfolder (by parameter) ###");
            System.out.println(" It will show you available database tables to select, shows nearly all content and can export a file (-> new iafisspy)");
            System.out.println("Syntax: [-help | -h | -? | ?] <--httpport{1025-65536}> <--httpbase{}>");
            System.out.println("\t Options");
            System.out.println("\t\t -h/-help/-?/?                  show this help and exit");
            System.out.println("\t\t --httpport                     the port on that the server opens a connection");
            System.out.println("\t\t --httpbase                     the folder where to find the website");
            System.out.println("\nBye");
            System.exit(0);
    }

    public class Web_ extends HttpServlet
	{
		private static final long serialVersionUID = 1L;
        private String request_stringToGetWebsite = "/";
        private String request_api_weather = "weather";
        private String request_api_example = "example";
        private String request_api_call_data_from_db = "data";
        private String request_api_insert_data_to_db = "insert";
        private String requestByClient = "";
        public Web_() {}
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            String parameter = request.getParameter("get");
            requestByClient = request.getRequestURI().toLowerCase();
            if (requestByClient.contains(request_stringToGetWebsite)
                    && parameter == null
                    )
            {
                clientRequest_Website(request, response);
            }
            else if (request_api_weather.equals(parameter))
            {
                clientRequest_Weather(request, response);
            }
            else if (request_api_example.equals(parameter))
            {
                clientRequest_TableNames(request, response);
            }
            else if (request_api_call_data_from_db.equals(parameter))
            {
                clientRequest_CallDataFromDb(request, response);
            }
            else if (request_api_insert_data_to_db.equals(parameter))
            {
                clientRequest_InsertDataToDb(request, response);
            }
        }
        private void clientRequest_Website (HttpServletRequest request, HttpServletResponse response)
        {
            try {
                // value of filenames by client come with a slash, but java doesn't find files with slash, so cut first char...
                String wantedFileFromClient = httpbase+File.separator+request.getServletPath().substring(1);
                String fileContent;
                if (new File(wantedFileFromClient+"index.html").exists()) {
                	wantedFileFromClient += "index.html";
                }
                fileContent = readFile(wantedFileFromClient);
                PrintWriter out = response.getWriter();
                if (wantedFileFromClient.endsWith(".html")) {
                    response.setContentType("text/html;charset=UTF-8");
                }
                else if (wantedFileFromClient.endsWith(".css")) {
                    response.setContentType("text/css;charset=UTF-8");
                }
                else if (wantedFileFromClient.endsWith(".js")){
                    response.setContentType("application/json;charset=UTF-8");
                }
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.setStatus(HttpServletResponse.SC_OK);
                response.reset();
                out.print(fileContent);
                //out.close();
            }
            catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        private void clientRequest_Weather (HttpServletRequest request, HttpServletResponse response)
        {
            try {
                System.out.println("Found request: "+request.getParameter("get"));
                // do something
            }
            catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        private void clientRequest_CallDataFromDb (HttpServletRequest request, HttpServletResponse response)
        {
            JSONObject json_mapForJSON = null;
            try
            {
                System.out.println("Found request: "+request.getParameter("get"));
                DatabaseConnect database = new DatabaseConnect();
                database.createDatabaseIfNotExists();
//                database.insertData();
                JSONArray array = new JSONArray();
                ArrayList <String> data = database.getData();
                array = new JSONArray(data);
                json_mapForJSON = new JSONObject();
                json_mapForJSON.put("Tablenames", array);
                String result = new GsonBuilder().create().toJson(json_mapForJSON);
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(result);
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void clientRequest_InsertDataToDb (HttpServletRequest request, HttpServletResponse response)
        {
            try
            {
                System.out.println("Found request: "+request.getParameter("get"));
                DatabaseConnect database = new DatabaseConnect();
                database.createDatabaseIfNotExists();
                database.insertData();
                JSONArray array = new JSONArray();
                array.put("Done");
                JSONObject json_mapForJSON = null;
                json_mapForJSON = new JSONObject();
                json_mapForJSON.put("Tablenames", array);
                String result = new GsonBuilder().create().toJson(json_mapForJSON);
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(result);
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void clientRequest_TableNames (HttpServletRequest request, HttpServletResponse response)
        {
            JSONObject json_mapForJSON = null;
            try
            {
                System.out.println("Found request: "+request.getParameter("get"));
                JSONArray array = new JSONArray();
                array.put("hallo1");
                array.put("hallo2");
                array.put("hallo3");
                json_mapForJSON = new JSONObject();
                json_mapForJSON.put("Tablenames", array);
                String result = new GsonBuilder().create().toJson(json_mapForJSON);
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(result);
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            System.out.println("Reading file: "+fileName);
            return ckasl;
        }
        public List <String> loadFile(String fpath)
        {
            try
            {
                return new ArrayList(
                        Files.readAllLines(
                                Paths.get(fpath),
                                StandardCharsets.UTF_8)
                        );
                
            } catch (Exception e) {
            	e.printStackTrace();
                return null;
            }
        }
        private ArrayList <String> makeListEntriesUnique (ArrayList <String> array)
        {
            return (ArrayList) array.stream().distinct().collect(Collectors.toList());
        }
    }
}
