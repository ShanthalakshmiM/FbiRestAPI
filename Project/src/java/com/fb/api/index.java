/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.restfb.DefaultJsonMapper;
import com.restfb.Parameter;
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
        String check = "before try";
        try(PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
//            String temp = "xxx";
//            out.println(temp);
//            temp = request.getParameter("hub.token");
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet index</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet index at " + request.getContextPath() + "</h1>");
//            out.println(temp);
//            out.println("</body>");
//            out.println("</html>");
                check = "before webhook";
            if((request.getParameter("hub.verify_token").equals("Shantha")) && 
                        (request.getParameter("hub.mode").equals("subscribe"))){
                        out.write(request.getParameter("hub.challenge"));
                    } else
                out.println("WRONG TOKEN!");
             //   request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
             check = "after webhook";
             
             String result = new String();
        String message = request.getParameter("StrMsg");
        String body = request.getReader().lines().reduce("", (accumulator,actual) -> accumulator + actual);
        DefaultJsonMapper mapper = new DefaultJsonMapper();
        WebhookObject object = mapper.toJavaObject(body, WebhookObject.class);
        
        check = "before sending";
        for(WebhookEntry entry : object.getEntryList()){
            if(!entry.getMessaging().isEmpty()){
                for(MessagingItem item : entry.getMessaging()){
                    String senderId = item.getSender().getId();
                    check = "inside sending";
                    IdMessageRecipient recpient = new IdMessageRecipient((senderId));
                    
                    if(item.getMessage() != null){
                        Message msg = new Message("Hello");
                        
                       GraphResponse resp= Activities.fbPageClient.publish("me/messages", GraphResponse.class , Parameter.with("recipient", recpient), Parameter.with("message", msg));
                       
                       if(resp.isSuccess()){
                           out.println("Success "+resp.getId());
                       }
                       else
                           out.println("Failure");
                    }
                }
            }
        }
        check = "after sending";
        out.println(check);
        request.getRequestDispatcher("/RedirectJsp.jsp").forward(request, response);
                }
        catch(Exception e){
            out.println(e.toString());
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
        processRequest(request, response);
//        String VERIFY_TOKEN = "Shantha";
//        
//        if(request.getParameter("hub.token").equals(VERIFY_TOKEN) && 
//            request.getParameter("hub.mode").equals("subscribe")){
//            response.getWriter().write(request.getParameter("hub.challenge"));
//        }
//        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
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
