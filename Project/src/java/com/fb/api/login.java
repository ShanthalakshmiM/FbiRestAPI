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
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.Parameter;
import com.restfb.types.GraphResponse;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.send.SendResponse;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
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
    // String cookieValue = new String();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

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
    String cookieValue = new String();
    String webhookMsg = new String();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("In get");
        PrintWriter out = response.getWriter();
        if ((request.getParameter("hub.verify_token")) != null) {
            if ((request.getParameter("hub.verify_token").equals("Shantha"))
                    && (request.getParameter("hub.mode").equals("subscribe"))) {
                out.write(request.getParameter("hub.challenge"));
            } else {
                out.println("WRONG TOKEN!");
            }
        } else {

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
                        ck.setPath("/Project");
                        response.addCookie(ck);

                        cookieValue = userId;

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
            document.put("accessTokens", accessTokenToDb);

            saveToDb(document);

            request.getRequestDispatcher("Activities.jsp").forward(request, response);
        }
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
        } else {
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
    DefaultFacebookClient fbPageClient;
    MongoDBConnection dbConnection = new MongoDBConnection();
    public static Set<Session> peers = null;

    static {
        peers = Collections.synchronizedSet(new HashSet<Session>());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //processRequest(request, response);
//       String cookieValue = new String();
//        Cookie[] cookies = request.getCookies();
//        for(Cookie cookie : cookies){
//            if(cookie.getName().equals("userId"))
//                cookieValue = cookie.getValue();
//        }
        System.out.println(" Cookie Value : " + cookieValue);
        System.out.println("In login begining");
        FileReader reader = new FileReader("C:/Users/HP/Desktop/Documents/NetBeansProjects/Project/web/WEB-INF/config.properties");
        Properties prop = new Properties();

        prop.load(reader);
        String pat = prop.getProperty("pageAccessToken");
        fbPageClient = new DefaultFacebookClient(pat);
        System.out.println("In post");
        StringBuffer sb = new StringBuffer();
        String line = new String();

        try {
            BufferedReader br = request.getReader();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        if (sb != null) {
            response.setStatus(HttpServletResponse.SC_OK);

        } else {
            System.out.println("response json is null");
        }
        String senderId = null;
        DefaultJsonMapper mapper = new DefaultJsonMapper();
        WebhookObject webhookObject = mapper.toJavaObject(sb.toString(), WebhookObject.class);
        // System.out.println("webhookObject : " + webhookObject.toString());
        String welcomeMsg = "Hi. Kindly identify yourself by clicking on the below link";
        for (WebhookEntry entry : webhookObject.getEntryList()) {
            if (!entry.getMessaging().isEmpty()) {
                Activities activities = new Activities(cookieValue);

                //  System.out.println("*********************");
                for (MessagingItem item : entry.getMessaging()) {
                    senderId = item.getSender().getId();
                    String message = item.getMessage().getText();
                    System.out.println(senderId + " ---- " + message);
                    String msg = senderId + " ---- " + message;
                    setMessage(item.getRecipient().getId(), msg);
                    if (webhookMsg != null) {
                        socketStreaming();
                    }
//                    for (Session peer : peers) {
//                        System.out.println("In streaming : " + webhookMsg);
//                        peer.getBasicRemote().sendText(webhookMsg);
//                    }
//                    Streaming streaming = new Streaming();
//                    streaming.setMessage(webhookMsg);

                    String strTrue = "true";
                    String strFalse = "false";
                    String url = "http://localhost:8080/RedirectApplication/index.jsp?senderId=" + senderId + "&enterpriseId=" + cookieValue + "&update=" + strFalse + "&set=" + strFalse;
                    DBCollection collection = dbConnection.connectToDB("testCustomer");
                    BasicDBObject query = new BasicDBObject("enterpriseId", cookieValue);
                    DBCursor cursor = dbConnection.getRecords(query);
                    Boolean bool = false;
                    if (cursor.count() > 1) {
                        System.out.println("ERROR : More than 1 record");
                    } else {
                        if (cursor.hasNext()) {
                            System.out.println("One record found for enterprise ID");
                            BasicDBObject savedData = (BasicDBObject) cursor.next();
                            List<BasicDBObject> savedCustomers = (List<BasicDBObject>) savedData.get("customerDetails");
                            for (int i = 0; i < savedCustomers.size(); i++) {
                                BasicDBObject savedCustomer = savedCustomers.get(i);
                                String savedSenderId = savedCustomer.getString("webhookId");
                                if (savedSenderId.equals(senderId)) {
                                    bool = true;
                                    System.out.println("Webhook ID found");
                                    String savedMobileNum = savedCustomer.getString("mobileNumber");
                                    if (savedMobileNum == null || savedMobileNum.equals("")) {
                                        System.out.println("Send URL");
                                        url = url + "&update=" + strFalse + "&set=" + strTrue;
                                        activities.sendMessageByWebhook(senderId, url);
//                                        request.setAttribute("enterpriseId", cookieValue);
//                                        request.getRequestDispatcher("/ForCheck").forward(request, response);

                                    } else {
                                        System.out.println("Do nothing");
                                    }
                                    break;
                                }

                            }
//                            if (!(bool)) {
//                                System.out.println("Append");
//                                url = url + "&update=" + strTrue + "&set=" + strFalse;
//                                activities.sendMessageByWebhook(senderId, url);
//
//                            }
                        } else {

                            activities.sendMessageByWebhook(senderId, url);
//                            request.setAttribute("enterpriseId", cookieValue);
//                            request.getRequestDispatcher("/ForCheck").forward(request, response);
                        }
                    }

                }
//                System.out.println("Press y to send reply");
//                Scanner scan = new Scanner(System.in);
//                String resp = scan.next();
//                String respMsg = null;
//                if(resp.equalsIgnoreCase("y")){
//                    respMsg = scan.nextLine();
//                    activities.sendMessageByWebhook(senderId, respMsg);
//                }
// send reply

            } else {
                Streaming streaming = new Streaming();
                System.out.println("Setting values");
                streaming.setValue(sb.toString(), cookieValue);
                try {
                    streaming.onStreaming(entry);
                } catch (JSONException ex) {
                    Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    public void setMessage(String pageId, String message) throws UnknownHostException {
        System.out.println("In setMessage() : " + pageId);
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("testDb");
        DBCollection collection = db.getCollection("testEnterprise");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            BasicDBObject savedData = (BasicDBObject) cursor.next();
            BasicDBObject savedATs = (BasicDBObject) savedData.get("accessTokens");
            List<BasicDBObject> savedPageData = (List<BasicDBObject>) savedATs.get("PageAccessTokens");
            if (savedPageData != null) {
                for (int i = 0; i < savedPageData.size(); i++) {
                    BasicDBObject savedPage = savedPageData.get(i);
                    String savedPageId = savedPage.getString("pageId");
                    if (savedPageId.equals(pageId)) {
                        System.out.println("webhookMsg set");
                        webhookMsg = message;
                    }
                }
            }

        }

    }

    public void socketStreaming() throws IOException {

        for (Session peer : peers) {            
            if (peer.isOpen()) {            
                peer.getBasicRemote().sendText(webhookMsg + "\n");
            } else {
                System.out.println("Message is not sent");
            }
        }
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
