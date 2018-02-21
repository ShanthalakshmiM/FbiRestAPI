/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;

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
        // System.out.println("In processRequest");

        FileReader reader = new FileReader("C:/Users/HP/Desktop/Documents/NetBeansProjects/Project/web/WEB-INF/config.properties");
        Properties prop = new Properties();

        prop.load(reader);

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
        
         
           String cookieValue = request.getAttribute("userId").toString();
            System.out.println("Cookie Value : "+cookieValue);
            Streaming streaming = new Streaming();
            System.out.println("Setting values");
            streaming.setValue(sb.toString(), cookieValue);

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
//          Cookie[] ck = request.getCookies();
//          for(Cookie cookie : ck){
//              if(cookie.getName().equals("userId"))
//                  System.out.println(cookie.getValue());
//          }
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
            //   System.out.println("In doPost");
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
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
