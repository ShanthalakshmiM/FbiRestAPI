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
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HP
 */
public class Logout extends HttpServlet {

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
            out.println("<title>Servlet Logout</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Logout at " + request.getContextPath() + "</h1>");
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
String test = "EAAR6dIGZAVUBAMp4CZAbEyNTJVu2ZA2oNM2CYkjUZCTyOuhe3pRTEPcfZAcUZCOOZCXaIgIq0SrZCGaG0XaZBq3uuLA88A1ZCTS0thIV1VTHyxg1FFKjLzILQApO907yZCknIFXvRIcjPZCxAKBbZBaTx7f4Am1BbfL8ZB4bbdQ9NlBAwZDZD";
             Properties prop = new Properties();
              FileReader reader = new FileReader("C:/Users/HP/Desktop/Documents/NetBeansProjects/Project/web/WEB-INF/config.properties");

            prop.load(reader);
            String redirectUri = prop.getProperty("logoutUri");
            
            URL url = new URL("https://www.facebook.com/logout.php?next="+redirectUri+"&access_token="+test);
            System.out.println("URL "+url);
            String outputString = new String();

        URLConnection fbConnection;

        //read the response
        try {
            fbConnection = url.openConnection();
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(
                    fbConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                outputString = outputString + inputLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to connect with Facebook "
                    + e);
        }
        System.out.println("logout : "+outputString);
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
