/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public class myServlet extends HttpServlet {

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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // processRequest(request, response);
        Activities activitiesObj = new Activities();

        String stringToJsp = new String();

        Properties prop = new Properties();
        prop.load(getServletContext().getResourceAsStream("/WEB-INF/config.properties"));
        String token = prop.getProperty("access_token");

        //button click for posting message
        if (request.getParameter("btnPost") != null) {
            String message = request.getParameter("StrPost");
            //call function to post on faceboook
            //  stringToJsp = Activities.makePost(message);
            stringToJsp = activitiesObj.postToPage(message);
            if (stringToJsp != null) {
                //passing values to jsp page
                request.getSession().setAttribute("result", stringToJsp);

                request.getRequestDispatcher("/StringResponses.jsp").forward(request, response);
            }
        }
        // button click action for get messages
        if (request.getParameter("btnGetMsg") != null) {
            //function call
            JSONArray messages = null;
            try {
                messages = activitiesObj.getConversations();
            } catch (JSONException ex) {
                Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            stringToJsp = messages.toString();

            if (stringToJsp != null) {
                // passing vlue to jsp
                request.setAttribute("result", stringToJsp);
                request.getRequestDispatcher("/RedirectJsp.jsp").forward(request, response);
            }
        }

        //
        if (request.getParameter("btnGetCmnt") != null) {

            JSONArray posts = new JSONArray();

            try {
                posts = activitiesObj.getAllPostComments();

            } catch (JSONException ex) {
                Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            stringToJsp = posts.toString();

            request.getSession().setAttribute("result", stringToJsp);

            request.getRequestDispatcher("/forCheck.jsp").forward(request, response);

        }
        if (request.getParameter("btnSendMsg") != null) {

            String message = request.getParameter("StrMessage");
            String recipient = request.getParameter("id");
            String res = new String();
            try {
                res = activitiesObj.sendMessage(recipient, message);
            } catch (JSONException ex) {
                Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (res != null) {
                stringToJsp = "Message sent";
            } else {
                stringToJsp = "Error";
            }
            request.getSession().setAttribute("result", stringToJsp);
            request.getRequestDispatcher("/StringResponses.jsp").forward(request, response);
        }
        if (request.getParameter("btnSndMsg") != null) {
            String message = request.getParameter("strMsg");
            String recipientId = request.getParameter("recipientId");
            try {
                stringToJsp = activitiesObj.sendMessage(recipientId, message);
            } catch (JSONException ex) {
                Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.getSession().setAttribute("result", stringToJsp);
            request.getRequestDispatcher("/StringResponses.jsp").forward(request, response);
        }
        if (request.getParameter("btReply") != null) {
            stringToJsp = activitiesObj.replyToComment("201841883710088_202497246977885");
            request.getSession().setAttribute("result", stringToJsp);
            request.getRequestDispatcher("/StringResponses.jsp").forward(request, response);
        }

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
        //processRequest(request, response);
        FileReader reader = new FileReader("C:/Users/HP/Desktop/Documents/NetBeansProjects/Project/web/WEB-INF/config.properties");
        Properties prop = new Properties();
        prop.load(reader);
        
        String pat = prop.getProperty("pageAccessToken");

        JSONObject data = new JSONObject();
        JSONArray message = new JSONArray();
        JSONObject dynamicMsg = new JSONObject();
        JSONObject temp = new JSONObject();
        try {
            temp.put("text", "Broadcast test");
            temp.put("fallback_text", "Hello friends");
            dynamicMsg.put("dynamic_text", temp);
        } catch (JSONException ex) {
            Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        message.put(dynamicMsg);
        try {
            data.put("messages", message);
        } catch (JSONException ex) {
            Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
      //  CloseableHttpClient httpClient = HttpClients.createDefault();
        //HttpClient httpClient = HttpClientBuilder.create().build();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://graph.facebook.com/v2.11/me/message_creatives?access_token=" + pat);
        String payload = data.toString();
        StringEntity stringEntity = new StringEntity(payload);
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse urlResponse = httpClient.execute(httpPost);
        System.out.println("Payload : "+payload);
        
        System.out.println("Message Creatives : " + urlResponse);
        System.out.println(urlResponse.getStatusLine().toString());
       
            JSONObject jsonObj = new JSONObject(urlResponse);
            System.out.println("Array : "+jsonObj);
            String id = new String();
            
        
        
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
