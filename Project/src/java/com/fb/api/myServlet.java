/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

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
    String cookieValue = new String();
    String selectedPage = new String();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException, UnknownHostException {

        // processRequest(request, response);
        Cookie[] cookies = request.getCookies();

        for (int i = 0; i < cookies.length; i++) {
           // System.out.println("Cookiessss ------ "+cookies[i].getName()+" ----- "+cookies[i].getValue());
            //System.out.println(cookies[i].getName() + " : " + cookies[i].getValue());
            if (cookies[i].getName().equals("userId")) {
                System.out.println("myServlet cookievalue : "+cookieValue);
                cookieValue = cookies[i].getValue();
            }
            if (cookies[i].getName().equals("page")) {
                selectedPage = cookies[i].getValue();
                System.out.println("Selected Page : "+cookies[i].getValue()); 
            }
        }

        Activities activitiesObj = null;
        try {
            activitiesObj = new Activities(cookieValue,selectedPage);
        } catch (JSONException ex) {
            Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        String stringToJsp = new String();
        System.out.println("Path : " + getServletContext().getResourceAsStream("/WEB-INF/config.properties"));

        Properties prop = new Properties();

        prop.load(getServletContext().getResourceAsStream("/WEB-INF/config.properties"));
        String token = prop.getProperty("access_token");

        //button click for posting message
        if (request.getParameter("btnPost") != null) {
            String message = request.getParameter("StrPost");
            //call function to post on faceboook
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
         Cookie[] cookies = request.getCookies();

        for (int i = 0; i < cookies.length; i++) {
           // System.out.println("Cookiessss ------ "+cookies[i].getName()+" ----- "+cookies[i].getValue());
            //System.out.println(cookies[i].getName() + " : " + cookies[i].getValue());
            if (cookies[i].getName().equals("userId")) {
                System.out.println("myServlet cookievalue : "+cookieValue);
                cookieValue = cookies[i].getValue();
            }
            if (cookies[i].getName().equals("page")) {
                selectedPage = cookies[i].getValue();
                System.out.println("Selected Page : "+cookies[i].getValue()); 
            }
        }

        Activities activitiesObj = null;
        try {
            activitiesObj = new Activities(cookieValue,selectedPage);
        } catch (JSONException ex) {
            Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if((request.getParameter("btnReply"))!= null){
             String id = request.getParameter("senderId");
        String msg = request.getParameter("strMsg");
            System.out.println("Message : "+msg);
            System.out.println("Id : "+id);
            activitiesObj.sendMessageByWebhook(id, msg);
        }

    }

    //-- used for making http call --//
    public HttpResponse sendPost(String url, String data) throws UnsupportedEncodingException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(data);
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse urlResponse = null;
        try {
            urlResponse = httpClient.execute(httpPost);
        } catch (IOException ex) {
            Logger.getLogger(myServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        return urlResponse;
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
