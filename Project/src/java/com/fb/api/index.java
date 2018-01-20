/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.GraphResponse;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.webhook.Change;
import com.restfb.types.webhook.ChangeValue;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.jboss.weld.logging.BeanLogger.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
@WebServlet(name = "index", urlPatterns = {"/index"})
public class index extends HttpServlet {

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
            throws ServletException, IOException, JSONException {
        System.out.println("In processRequest");
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

        DefaultJsonMapper mapper = new DefaultJsonMapper();
        WebhookObject webhookObject = mapper.toJavaObject(sb.toString(), WebhookObject.class);
        System.out.println("webhookObject : " + webhookObject.toString());
        for (WebhookEntry entry : webhookObject.getEntryList()) {
            //System.out.println("check1");
            for (Change change : entry.getChanges()) {
                if (change.getField().equals("feed")) {
                    JSONObject webhookResponse = new JSONObject(sb.toString());
                    JSONArray entryJson = webhookResponse.getJSONArray("entry");
                    JSONArray changes = entryJson.getJSONObject(0).getJSONArray("changes");
                    JSONObject value = changes.getJSONObject(0).getJSONObject("value");
                    System.out.println("Values : " + value);
                    JSONObject from = value.getJSONObject("from");
                    String postContent = value.getString("message");
                    String sender = from.getString("name");
                    System.out.println(sender + " : " + postContent);
                } else {
                    if (!entry.getMessaging().isEmpty()) {
                        for (MessagingItem item : entry.getMessaging()) {
                            String senderId = item.getSender().getId();
                            String message = item.getMessage().getText().toString();
                            System.out.println(senderId + " : " + message);
                        }

                    }
                }
            }

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
        //processRequest(request, response);
        System.out.println("In Get");
        PrintWriter out = response.getWriter();
        if ((request.getParameter("hub.verify_token")) != null) {
            if ((request.getParameter("hub.verify_token").equals("Shantha"))
                    && (request.getParameter("hub.mode").equals("subscribe"))) {
                out.write(request.getParameter("hub.challenge"));
            } else {
                out.println("WRONG TOKEN!");
            }
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
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("In doPost");

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
