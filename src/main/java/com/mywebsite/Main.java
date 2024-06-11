package main.java.com.mywebsite;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.mywebsite.database.DAO.Dao_DBConnect;
import main.java.com.mywebsite.main.DataUse;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main extends Dao_DBConnect
{
    Server server;
    static String httpbase = System.getProperty("user.dir");
    static int httpport = 5000;

    public Main(String httpbase, int httpport) {
        logger = LogManager.getLogger(Main.class.getName());
        if(new File(httpbase).isDirectory()) {
            Main.httpbase = httpbase;
        }
        else if(new File(httpbase).isFile()) {
            Main.httpbase = httpbase.substring(0, httpbase.lastIndexOf("/"));
        }
        initHttpService(Main.httpbase, Main.httpport);
    }
    public Main() {
        initHttpService(httpbase, httpport);
    }
    private void initHttpService(String httpbase, int port)
    {
        try
        {
        	logger.info("webfolder: "+ Main.httpbase);
        	logger.info("port = "+httpport);
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
            
        } catch (Exception e) {
            logger.error(e);
        }
    }
    public static void main(String args[])
    {
        for(int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-h") || args[i].equals("-help") || args[i].equals("-?") || args[i].equals("?")) {
                showHelp();
            }
            if(args[i].equalsIgnoreCase("--httpport"))
            {
                try {
					httpport = Integer.parseInt(args[i + 1]);
				} catch (NumberFormatException e) {
				    logger.error(e);
				}
            }
        }
        if(new File(httpbase).isFile()) {
            httpbase = httpbase.substring(0, httpbase.lastIndexOf("/"));
        }
//        new Main().initHttpService(httpbase, httpport);
        new Main();
    }
    private static void showHelp ()
    {
            logger.info("");
            logger.info("### This program is a webserver with a custom backend that connects to the custom webfolder (by parameter) ###");
            logger.info(" It will show you available database tables to select & shows nearly all content");
            logger.info("Syntax: [-help | -h | -? | ?] <--httpport{1025-65536}> <--httpbase{}>");
            logger.info("\t Options");
            logger.info("\t\t -h/-help/-?/?                  show this help and exit");
            logger.info("\t\t --httpport                     the port on that the server opens a connection");
            logger.info("\t\t --httpbase                     the folder where to find the website");
            logger.info("\nBye");
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
        private String request_api_get_data_for_admin = "admin";
        private String request_api_use_json = "use_json";
        private String request_api_add_user = "add_user";
        private String request_api_remove_user = "remove_user";
        private String request_api_update_user = "update_user";
        private String requestByClient = "";
        public Web_() {}
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
        {
            DataUse website = new DataUse();
            website.sethttpbase(httpbase);
            String parameter = request.getParameter("get");
            if(parameter != null)
            {
            	logger.info("Found parameter: "+parameter);
            }
            requestByClient = request.getRequestURI().toLowerCase();
            if(requestByClient.contains(request_stringToGetWebsite)
                    && parameter == null
                    ) {
                website.clientRequest_Website(request, response);
            }
            else if(request_api_weather.equals(parameter)) {
                DataUse.clientRequest_Weather(request, response);
            }
            else if(request_api_call_data_from_db.equals(parameter)) {
                DataUse.clientRequest_GetData(request, response);
            }
            else if(request_api_insert_data_to_db.equals(parameter)) {
                DataUse.clientRequest_InsertDataToDb(request, response);
            }
            else if(request_api_get_data_for_admin.equals(parameter)) {
                DataUse.clientRequest_GetAllData(request, response);
            }
            else if(request_api_use_json.equals(parameter)) {
                DataUse.clientRequest_UseJson(request, response);
            }
            else if(request_api_add_user.equals(parameter)) {
            	DataUse.clientRequest_AddUser(request, response);
            }
            else if(request_api_remove_user.equals(parameter)) {
                DataUse.clientRequest_removeUser(request, response);
            }
            else if(request_api_update_user.equals(parameter)) {
                DataUse.clientRequest_updateUser(request, response);
            }
        }
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
        {
        	super.doPost(request, response);
            String parameter = request.getParameter("get");
            logger.info("Found parameter: "+parameter);
            if(parameter != null || request.getParameter("remember") != null)
            {
            	requestByClient = request.getRequestURI().toLowerCase();
            }
        }
//        /**
//         * use cookies
//         * @param request
//         */
//        void cookie(HttpServletRequest request)
//        {
//            Cookie cookie = new Cookie(request.getRemoteUser(), "");
//            CookieManager cm = new CookieManager();
//        }
    }
}
