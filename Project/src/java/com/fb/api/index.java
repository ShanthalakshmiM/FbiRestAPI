/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.GraphResponse;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.jboss.weld.logging.BeanLogger.LOG;

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
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if ((request.getParameter("hub.verify_token")) != null) {
            if ((request.getParameter("hub.verify_token").equals("Shantha"))
                    && (request.getParameter("hub.mode").equals("subscribe"))) {
                out.write(request.getParameter("hub.challenge"));
            } else {
                out.println("WRONG TOKEN!");
            }
        } else {
            // throw new RuntimeException("Error");
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

        PrintWriter out = response.getWriter();
        if ((request.getParameter("hub.verify_token")) != null) {
            if ((request.getParameter("hub.verify_token").equals("Shantha"))
                    && (request.getParameter("hub.mode").equals("subscribe"))) {
                out.write(request.getParameter("hub.challenge"));
            } else {
                out.println("WRONG TOKEN!");
            }
        } else {
            throw new RuntimeException("Error");
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
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        DefaultJsonMapper mapper = new DefaultJsonMapper();
        WebhookObject webhookObject = mapper.toJavaObject(body, WebhookObject.class);

        for (WebhookEntry entry : webhookObject.getEntryList()) {
            if (!entry.getMessaging().isEmpty()) {
                for (MessagingItem item : entry.getMessaging()) {

                    String senderId = item.getSender().getId();

                    // create recipient
                    IdMessageRecipient recipient = new IdMessageRecipient(senderId);

                    // check message
                    if (item.getMessage() != null && item.getMessage().getText() != null) {
                        // create simple text message
                        Message simpleTextMessage = new Message("Echo: " + item.getMessage().getText());

                        // build send client and send message
                        FacebookClient sendClient = new DefaultFacebookClient(Constants.PAGE_ACCESS_TOKEN, Version.VERSION_2_6);
                        sendClient.publish("me/messages", GraphResponse.class, Parameter.with("recipient", recipient),
                                Parameter.with("message", simpleTextMessage));
                    }

                    if (item.getPostback() != null) {
                        LOG.debug("run postback");
                    }
                }

            }
        }
        request.getSession().setAttribute("result", body);
        request.getRequestDispatcher("/forCheck.jsp").forward(request, response);
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
