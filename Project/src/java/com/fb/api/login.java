/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Properties prop = new Properties();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FileReader reader = new FileReader("C:/Users/HP/Desktop/Documents/NetBeansProjects/Project/web/WEB-INF/config.properties");

        prop.load(reader);

        String appId = prop.getProperty("appId");
        String appSecret = prop.getProperty("appSecret");
        String redirectUri = prop.getProperty("redirectUri");

        String accessToken = new String();
        String longLiveAT = new String();
        String userId = new String();
        String userName = new String();
        JSONArray pageDetails = new JSONArray();
      

        try {
            String rid = request.getParameter("request_ids");
            if (rid != null) {
                response.sendRedirect("https://www.facebook.com/dialog/oauth?client_id=" + appId + "&redirect_uri=" + redirectUri + "&scope=manage_pages,read_page_mailboxes,publish_actions,pages_messaging,publish_pages,user_about_me,email,user_posts");
            } else {
                String code = request.getParameter("code");

                //get Response 
                if (code == null || code.equals("")) {
                    throw new RuntimeException("Error: Code is null");
                } else {
                    URL url;

                    //hit url to get access token
                    try {

                        url = new URL("https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUri + "&client_secret=" + appSecret + "&code=" + code);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new RuntimeException("Invlaid code");
                    }
                    String urlResponse = getResponse(url);
                    accessToken = getAccessToken(urlResponse, "access_token");

                    //save accress token for further use
                    System.out.println("Access Token :" + accessToken);
                    prop.setProperty("accessToken", accessToken);

                    URL lngLivedURL = new URL("https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id=" + appId + "&client_secret=" + appSecret + "&fb_exchange_token=" + accessToken);

                    urlResponse = getResponse(lngLivedURL);
                    longLiveAT = getAccessToken(urlResponse, "access_token");
                    System.out.println("Long lived URL response: " + longLiveAT);
                    prop.setProperty("longLivedAccessToken", longLiveAT);

                    String outputString = new String();
                    URL forID = new URL("https://graph.facebook.com/v2.2/me?access_token=" + longLiveAT);
                    urlResponse = getResponse(forID);
                    userId = getAccessToken(urlResponse, "id");
                    userName = getAccessToken(urlResponse, "name");
                    System.out.println("Account Id : " + userId);
                    prop.setProperty("username", userName);
                     prop.setProperty("userId", userId);
                    Cookie ck = new Cookie("userId", userId);
                    response.addCookie(ck);
                    

                    URL forPageToken = new URL("https://graph.facebook.com/v2.2/" + userId + "/accounts?access_token=" + longLiveAT);
                    outputString = getResponse(forPageToken);
                    System.out.println("OutputString : " + outputString);
                    pageDetails = getPageATs(outputString);
                    System.out.println("Final response : " + getPageATs(outputString).toString());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        prop.store(new FileWriter("C:/Users/HP/Desktop/Documents/NetBeansProjects/Project/web/WEB-INF/config.properties"), "The end");
        //  writer.close();
        BasicDBObject document = new BasicDBObject();
        document.put("userId", userId);
        document.put("username", userName);

        BasicDBObject accessTokenToDb = new BasicDBObject();
        accessTokenToDb.put("accessToken", accessToken);
        accessTokenToDb.put("longLivedAT", longLiveAT);
        List<BasicDBObject> dbPageDetailsList = new ArrayList<BasicDBObject>();
        for (int j = 0; j < pageDetails.length(); j++) {

            try {
                BasicDBObject dbPageDetails = new BasicDBObject();
                dbPageDetails.put("pageId", pageDetails.getJSONObject(j).getString("ID"));
                dbPageDetails.put("pageName", pageDetails.getJSONObject(j).getString("Name"));
                dbPageDetails.put("accessToken", pageDetails.getJSONObject(j).getString("Token"));
                dbPageDetailsList.add(dbPageDetails);

            } catch (JSONException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        accessTokenToDb.put("PageAccessTokens", dbPageDetailsList);
        document.put("accessToken", accessTokenToDb);

        saveToDb(document);

        request.getRequestDispatcher("Activities.jsp").forward(request, response);
    }

    public void saveToDb(BasicDBObject dbObject) throws UnknownHostException {

        MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("testDb");
        DBCollection collection = db.getCollection("testEnterprise");
        BasicDBObject query = new BasicDBObject("userId", dbObject.get("userId").toString());
        DBCursor cursor = collection.find(query);
        System.out.println("Retrieved " + cursor.count());
        if (cursor.hasNext()) {
                    System.out.println("Record already exists");
        } 
        else {
            collection.save(dbObject);
            System.out.println("Inserted");
        }

    }

    public String getResponse(URL url) {
        String response = new String();
        
        
        URLConnection fbConnection;

        //read the response
        try {
            fbConnection = url.openConnection();
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(
                    fbConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response = response + inputLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to connect with Facebook "
                    + e);
        }
        //extract access token alone from response

        return response;
    }

    public String getAccessToken(String response, String key) throws JSONException {
        JSONObject responseJson = new JSONObject(response);
        String accessToken = responseJson.getString(key);
        return accessToken;
    }

    public JSONArray getPageATs(String response) throws JSONException {
        JSONObject responseJson = new JSONObject(response);
        JSONArray pages = new JSONArray();
        JSONArray data = responseJson.getJSONArray("data");
        if (data.length() == 0) {
            prop.setProperty("pageAccessToken", null);
        } else {
            prop.setProperty("pageAccessToken", data.getJSONObject(0).get("access_token").toString());
            prop.setProperty("pageId", data.getJSONObject(0).get("id").toString());

        }
        System.out.println("In login - Pat : " + prop.getProperty("pageAccessToken"));
        for (int i = 0; i < data.length(); i++) {
            JSONObject pageDetails = new JSONObject();
            pageDetails.put("Token", data.getJSONObject(i).get("access_token"));
            pageDetails.put("Name", data.getJSONObject(i).get("name"));
            pageDetails.put("ID", data.getJSONObject(i).get("id"));
            pages.put(pageDetails);
        }
        return pages;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
