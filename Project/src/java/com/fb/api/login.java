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
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            throws ServletException, IOException, UnknownHostException {
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
                        URL lngLivedURL = new URL("https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id=" + appId + "&client_secret=" + appSecret + "&fb_exchange_token=" + accessToken);

                        urlResponse = getResponse(lngLivedURL);
                        longLiveAT = getAccessToken(urlResponse, "access_token");
                        System.out.println("Long lived URL response: " + longLiveAT);
                        String outputString = new String();
                        URL forID = new URL("https://graph.facebook.com/v2.2/me?access_token=" + longLiveAT);
                        urlResponse = getResponse(forID);
                        userId = getAccessToken(urlResponse, "id");
                        userName = getAccessToken(urlResponse, "name");
                        System.out.println("Account Id : " + userId);
                        Cookie ck = new Cookie("userId", userId);
                        ck.setPath("/");
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
            JSONArray customers = new JSONArray();
            try {
                customers = getCustomers(cookieValue);
            } catch (JSONException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
            Chats chat = new Chats();
            String convos = null;
            try {
                convos = chat.getConversations(customers);
                System.out.println("convos : " + convos);
            } catch (JSONException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
            saveToDb(document);
            request.setAttribute("customers", customers.toString());
            request.setAttribute("pageDetails", pageDetails.toString());
            request.setAttribute("conversations", convos);
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

    public JSONArray getCustomers(String enterpriseId) throws UnknownHostException, JSONException {
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("testDb");
        DBCollection collection = db.getCollection("testCustomer");
        BasicDBObject query = new BasicDBObject("enterpriseId", enterpriseId);
        DBCursor cursor = collection.find(query);
        JSONArray customers = new JSONArray();
        if (cursor.hasNext()) {
            BasicDBObject savedData = (BasicDBObject) cursor.next();
            List<BasicDBObject> savedCustomers = (List<BasicDBObject>) savedData.get("customerDetails");

            for (int i = 0; i < savedCustomers.size(); i++) {
                BasicDBObject savedCustomer = savedCustomers.get(i);
                JSONObject customer = new JSONObject();
                customer.put("customerID", savedCustomer.getString("webhookId"));
                customer.put("identity", savedCustomer.getString("mobileNumber"));
                customers.put(customer);
            }

        }
        return customers;
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
    public final static HashMap<String, webSocketEndpoint> sockets = new HashMap<>();

    static {
        peers = Collections.synchronizedSet(new HashSet<Session>());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException, FileNotFoundException, UnknownHostException {

        System.out.println(" Cookie Value : " + cookieValue);
        System.out.println("In login begining");

        System.out.println("In post");
        String pageId = null;
        Chats chat = new Chats();
        if ((request.getParameter("replyMsg")) != null) {
            JSONObject msgToSend = new JSONObject();
            String customerId = request.getParameter("customer");
            String replyMsg = request.getParameter("replyMsg");
            String strTs = request.getParameter("timestamp");
            try {
                msgToSend.put("message", replyMsg);
                msgToSend.put("customerId", customerId);
                msgToSend.put("pageId", request.getParameter("pageId"));
                System.out.println("Send message : "+request.getParameter("pageId"));
                msgToSend.put("timestamp", Long.parseLong(strTs));
            } catch (JSONException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }

            Activities obj = null;
            try {
                obj = new Activities(cookieValue, request.getParameter("pageId"));
            } catch (JSONException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
            obj.sendMessageByWebhook(customerId, replyMsg);
            try {
                chat.saveMessage(msgToSend, true);
            } catch (JSONException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            StringBuffer sb = new StringBuffer();
            String line = new String();

            try {
                BufferedReader br = request.getReader();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
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
            String welcomeMsg = "Hi. Kindly identify yourself by clicking on the below link";

            for (WebhookEntry entry : webhookObject.getEntryList()) {
                if (!entry.getMessaging().isEmpty()) {

                    for (MessagingItem item : entry.getMessaging()) {
                        senderId = item.getSender().getId();
                        pageId = item.getRecipient().getId();
                        JSONObject msgJson = new JSONObject();
                        try {
                            msgJson.put("customerId", senderId);
                            msgJson.put("message", item.getMessage().getText());
                            msgJson.put("pageId", pageId);
                            Date date = item.getTimestamp();
                            long ts = date.getTime();
                            msgJson.put("timestamp", ts);
                            System.out.println("Timestamp : " + String.valueOf(ts));
                        } catch (JSONException ex) {
                            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            chat.saveMessage(msgJson, false);
                        } catch (JSONException ex) {
                            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        webhookMsg = msgJson.toString();
                        cookieValue = setMessage(pageId);
                        Activities activities = null;
                        try {
                            activities = new Activities(cookieValue, pageId);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex) {
                            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        socketStreaming();
                        String strTrue = "true";
                        String strFalse = "false";
                        String url = "https://cd7fd438.ngrok.io/RedirectApplication/index.jsp?senderId=" + senderId + "&enterpriseId=" + cookieValue;
                        DBCollection collection = dbConnection.connectToDB("testCustomer");
                        BasicDBObject query = new BasicDBObject("enterpriseId", cookieValue);
                        DBCursor cursor = dbConnection.getRecords(query);
                        Boolean bool = false;
                        if (cursor.count() > 1) {
                            System.out.println("ERROR : More than 1 record");
                        } else {
                            JSONObject detailsToSave = new JSONObject();
                            Date date = new Date();
                            long ts = date.getTime();
                            try {
                                detailsToSave.put("customerId", senderId);
                                detailsToSave.put("pageId", pageId);
                                detailsToSave.put("timestamp", ts);
                            } catch (JSONException ex) {
                                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (cursor.hasNext()) {
                                System.out.println("One record found for enterprise ID");
                                BasicDBObject savedData = (BasicDBObject) cursor.next();
                                List<BasicDBObject> savedCustomers = (List<BasicDBObject>) savedData.get("customerDetails");
                                for (int i = 0; i < savedCustomers.size(); i++) {
                                    System.out.println("Inside for");
                                    BasicDBObject savedCustomer = savedCustomers.get(i);
                                    String savedSenderId = savedCustomer.getString("webhookId");
                                    if (!(savedSenderId.equals(senderId))) {
                                        bool = true;

                                    } else {
                                        bool = false;
                                        break;
                                    }

                                }

                                if (bool) {

                                    System.out.println("Send URL :  senderID null");
                                    url = url + "&update=" + strTrue + "&set=" + strFalse;
                                    activities.sendMessageByWebhook(senderId, welcomeMsg);

                                    try {
                                        detailsToSave.put("message", welcomeMsg);
                                        chat.saveMessage(detailsToSave, true);
                                        detailsToSave.put("message", url);
                                        activities.sendMessageByWebhook(senderId, url);
                                        chat.saveMessage(detailsToSave, true);
                                    } catch (JSONException ex) {
                                        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                } else {
                                    System.out.println("DoNothing");
                                }
                            } else {
                                System.out.println("Send URL : enterpriseId null");
                                url = url + "&update=" + strFalse + "&set=" + strFalse;
                                activities.sendMessageByWebhook(senderId, welcomeMsg);
                                try {
                                        detailsToSave.put("message", welcomeMsg);
                                        chat.saveMessage(detailsToSave, true);
                                        detailsToSave.put("message", url);
                                        activities.sendMessageByWebhook(senderId, url);
                                        chat.saveMessage(detailsToSave, true);
                                    } catch (JSONException ex) {
                                        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                            }
                        }

                    }

                } else {
                    Streaming streaming = new Streaming();
                    System.out.println("Setting values");
                    streaming.setValue(sb.toString(), cookieValue);
                    try {
                        webhookMsg = streaming.onStreaming(entry);
                        socketStreaming();
                    } catch (JSONException ex) {
                        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        }

    }

    public String setMessage(String pageId) throws UnknownHostException {
        String savedUserID = null;
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
                        savedUserID = savedData.getString("userId");
                        System.out.println("savedUserId : " + savedUserID);
                    }
                }
            }

        }
        return savedUserID;
    }

    public void socketStreaming() throws IOException {
        System.out.println("Socket Streaming");
        Session session;
        Set set = sockets.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            System.out.println("Comparison : " + mentry.getKey() + " --- " + cookieValue);
            if (mentry.getKey().equals(cookieValue)) {
                webSocketEndpoint obj = (webSocketEndpoint) mentry.getValue();
                session = obj.getSession();
                System.out.println("Session : " + session + " : " + session.getId());
                System.out.println("Session ID : " + obj.getSessionId());
                obj.onMessage(webhookMsg, session);
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
