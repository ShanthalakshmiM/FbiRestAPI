/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HP
 */
public class ForCheck extends HttpServlet {

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
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet ForCheck</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ForCheck at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }

        if (request.getParameter("check") != null) {
            System.out.println("Passed value : " + request.getParameter("str"));
        } else {
            System.out.println("Nothing recieved");
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
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String mobileNumber = null;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("In post -- Success");
        if ((request.getParameter("customerIdentity")) != null) {
            System.out.println("Redirection success");
            mobileNumber = request.getParameter("strIdentity");
            String senderId = request.getParameter("senderId");
            String enterpriseId = request.getParameter("enterpriseId");
            if (request.getParameter("update").equals("true")) {
                update(enterpriseId, senderId);

            } else if (request.getParameter("set").equals("true")) {
                set(enterpriseId, senderId);

            } else {
                save(enterpriseId, senderId);
            }

        }

    }
    MongoDBConnection dbConnection = new MongoDBConnection();

    public void save(String id, String senderId) throws UnknownHostException {

        DBCollection collection = dbConnection.connectToDB("testCustomer");
        System.out.println("Passed value : " + id);
        BasicDBObject objectToSave = new BasicDBObject();
        BasicDBObject customerDetails = new BasicDBObject();
        List<BasicDBObject> cust = new ArrayList<>();
        System.out.println("In svae() : " + senderId);
        customerDetails.put("webhookId", senderId);
        customerDetails.put("convId", "");
        System.out.println("Else MN : " + mobileNumber);

        customerDetails.put("mobileNumber", mobileNumber);
        cust.add(customerDetails);
        objectToSave.put("enterpriseId", id);
        objectToSave.put("customerDetails", cust);

        collection.save(objectToSave);
        System.out.println(objectToSave);
    }

    public void update(String id, String webhookId) throws UnknownHostException {
        System.out.println("In update()");
        DBCollection collection = dbConnection.connectToDB("testCustomer");
        BasicDBObject query = new BasicDBObject("enterpriseId", id);
        BasicDBObject addItem = new BasicDBObject("customerDetails", new BasicDBObject("webhookId", webhookId)
                .append("convoId", "").append("mobileNumber", mobileNumber));
        BasicDBObject updateQuery = new BasicDBObject("$push", addItem);
        collection.update(query, updateQuery);
    }

    public void set(String id, String webhookId) throws UnknownHostException {

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
