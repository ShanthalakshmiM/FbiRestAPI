/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Page;
import com.restfb.types.User;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet login at " + request.getContextPath() + "</h1>");
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

        String accessToken = new String();
        String username = "initialized";
        
        Properties prop = new Properties();
        OutputStream output = new FileOutputStream("config.properties");

        String outputString = new String();
        try {
            String rid = request.getParameter("request_ids");
            if (rid != null) {
                response.sendRedirect("https://www.facebook.com/dialog/oauth?client_id=" + Constants.APP_ID + "&redirect_uri=" + Constants.REDIRECT_URI + "");
            } else {
                String code = request.getParameter("code");

                //get Response 
                if (code == null || code.equals("")) {
                    throw new RuntimeException("Error: Code is null");
                } else {
                    URL url;
             
                    //hit url to get access token
                    try {
                     
                        url = new URL("https://graph.facebook.com/oauth/access_token?client_id=" + Constants.APP_ID + "&redirect_uri=" + Constants.REDIRECT_URI + "&client_secret=" + Constants.APP_SECRET + "&code=" + code);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new RuntimeException("Invlaid code");
                    }
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
                    //extract access token alone from response
                    if (outputString.indexOf("access_token") != -1) {
                        username = "getting access token";
                        accessToken = outputString.substring(outputString.indexOf(":") + 1, outputString.indexOf(","));
                    }

                    if (accessToken.startsWith("{")) {
                        username = "error";
                        throw new RuntimeException("ERROR: Access Token Invalid: "
                                + accessToken);
                    }
                   

                    //save access token for further use
                    prop.setProperty("access_token", accessToken);
                    prop.store(output, null);
                    
                    Constants.MY_ACCESS_TOKEN = accessToken;
                    
                    URL lngLivedURL = new URL("https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id="+Constants.APP_ID+"&client_secret="+Constants.APP_SECRET+"&fb_exchange_token="+accessToken+"");
                    
                    URLConnection ATConnection = lngLivedURL.openConnection();
                    
                    BufferedReader br = new BufferedReader(new InputStreamReader(ATConnection.getInputStream()));
                    
                    String input;
                    String finalval = new String();
                    while((input = br.readLine()) != null){
                        finalval += input;
                    }
                    br.close();
                    username = finalval;
                }
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("accessToken", accessToken);
        request.getSession().setAttribute("user", username);
        request.getRequestDispatcher("Activities.jsp").forward(request, response);
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
