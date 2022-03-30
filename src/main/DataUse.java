package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;

import database.Database;
import database.DatabaseFile;
import database.DatabaseSQLite;

public class DataUse
{
    static Database databasesource;
    String httpbase;
    static String htmlhead; 
    static String htmlend; 
    
    public DataUse()
    {
        ///////////////////////////////////////////
        // get libs from online
        htmlhead = ""
                + "<!DOCTYPE html>\n"
                + "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
                + "<meta charset=\"utf-8\">"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
                + "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>"
                + "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>-"
                + "</head>"
                + "<body class=\"container\" style=\"font-size:30px\";>"
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
        databasesource = Database.getInstance();
    }
            
    public void sethttpbase(String httpbase)
    {
        this.httpbase = httpbase;
    }
    public void clientRequest_Website (HttpServletRequest request, HttpServletResponse response)
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
    public void clientRequest_WebsiteFromBackend(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            System.out.println("Found request: "+request.getParameter("get"));
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            String websitedata = htmlhead;
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
            e.printStackTrace();
        }
    }
    public static void clientRequest_Weather(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            System.out.println("Found request: "+request.getParameter("get"));
            // do something
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    public static void clientRequest_CallDataFromDb(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            System.out.println("Found request: "+request.getParameter("get"));
//            DatabaseSQLite database = new DatabaseSQLite();
            ArrayList <ArrayList<String>> data = databasesource.getData();
            databasesource.close();
//            response.getWriter().println("{ \"status\": \"ok\"}");
//            array = new JSONArray(data);
//            json_mapForJSON = new JSONObject();
//            json_mapForJSON.put("Tablenames", array);
//            String result = new GsonBuilder().create().toJson(json_mapForJSON);
            response.setCharacterEncoding("utf-8");
//            response.setContentType("application/json");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            String websitedata = htmlhead;
            websitedata += "<table class=\"table\">";
            websitedata += "<tr>"
                    + "<th>name</th>"
                    + "</tr>"
                    + "</thead>";
//            for(String temp: data)
//            {
//                websitedata += "<tr><td>";
//                websitedata += temp;
//                websitedata += "</tr></td>";
//            }
            for(ArrayList <String> tempList: data)
            {
                websitedata += "<tr>";
                for(String temp: tempList)
                {
                    websitedata += "<td>";
                    websitedata += temp;
                    websitedata += "</td>";
                }
                websitedata += "</tr>";
            }
            websitedata += "</thead>";
            websitedata += "</table>";
            websitedata += htmlend;
//            response.getWriter().println(websitedata);
            response.getWriter().append(websitedata);
//            PrintWriter out = response.getWriter();
//            out.print(result);
//            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void clientRequest_askUserData(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            String websitedata = htmlhead;
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
    public static void clientRequest_GetAllData(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            System.out.println("Found request: "+request.getParameter("get"));
            System.out.println("Found request: "+request.getParameter("name"));
            String name = request.getParameter("user");
            String pw = request.getParameter("pw");
//            databasesource = new DatabaseSQLite();
            if(databasesource.isPermitted(name, pw))
            {
                ArrayList <ArrayList<String>> data = databasesource.getAllData();
                databasesource.close();
                response.setCharacterEncoding("utf-8");
//            response.setContentType("application/json");
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                String websitedata = htmlhead;
                websitedata += "<table class=\"table\">";
                websitedata += "<thead>";
                websitedata += "<tr>"
                        + "<th>id</th>"
                        + "<th>name</th>"
                        + "<th>password</th>"
                        + "<th>admin-permit</th>"
                        + "</tr>"
                        + "</thead>";
                for(ArrayList <String> tempList: data)
                {
                    websitedata += "<tr>";
                    for(String temp: tempList)
                    {
                        websitedata += "<td>";
                        websitedata += temp;
                        websitedata += "</td>";
                    }
                    websitedata += "</tr>";
                }
                websitedata += "</table>";
                websitedata += htmlend;
                response.getWriter().println(websitedata);
            }
            else
            {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("{ \"status\": \"wrong data\"}");
            }
//            PrintWriter out = response.getWriter();
//            out.print(result);
//            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void clientRequest_GetAllData(HttpServletRequest request, HttpServletResponse response)
//    {
//        try
//        {
//            JSONObject json_mapForJSON = null;
//            System.out.println("Found request: "+request.getParameter("get"));
//            System.out.println("Found request: "+request.getParameter("name"));
//            ////////////////////
//            DatabaseConnect database = new DatabaseConnect();
//            if(database.isPermitted(request.getParameter("name"), request.getParameter("pw")))
//            {
//                System.out.println();
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
    public static void clientRequest_InsertDataToDb(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            System.out.println("Found request: "+request.getParameter("get"));
//            DatabaseSQLite database = new DatabaseSQLite();
            databasesource.createDatabaseIfNotExists();
            ((DatabaseFile)databasesource.getInstance()).insertData();
            databasesource.close();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{ \"status\": \"ok\"}");
//            out.print(result);
//            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void clientRequest_TableNames(HttpServletRequest request, HttpServletResponse response)
//    {
//        JSONObject json_mapForJSON = null;
//        try
//        {
//            System.out.println("Found request: "+request.getParameter("get"));
//            JSONArray array = new JSONArray();
//            array.put("hallo1");
//            array.put("hallo2");
//            array.put("hallo3");
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
