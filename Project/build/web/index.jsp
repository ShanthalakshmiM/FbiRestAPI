<%-- 
    Document   : index
    Created on : Dec 18, 2017, 10:41:58 AM
    Author     : HP
--%>

<%@page import="java.io.IOException"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.FileInputStream"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <style>
        body{
            background-image: url("images/fbloginbckgrnd.jpg");
        }
        #parent{
            display: table;
            width: 100%;
        }
        #form{
            padding: 0 5px 0 0;
            vertical-align: -50%;

        }
    </style>
    <%

        FileInputStream fis = new FileInputStream("C:/Users/HP/Desktop/Documents/NetBeansProjects/Project/web/WEB-INF/config.properties");
        Properties prop = new Properties();

        try {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String appId = prop.getProperty("appId");
        String uri = prop.getProperty("redirectUri");
    %>
    <body

        <div id="parent">
            <center>
               
                <form action="<%= request.getContextPath()%>/login" method="get" id="form">
                    <a href="https://www.facebook.com/dialog/oauth?client_id=<%= appId%>&redirect_uri=<%= uri%>&scope=manage_pages,read_page_mailboxes,pages_messaging,publish_actions,publish_pages,user_about_me,email,user_posts" > <img src="./images/facebookLoginButton.jpg"/> </a>

                </form>
            </center>   
        </div>

    </body>
</html>
