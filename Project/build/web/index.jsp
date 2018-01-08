<%-- 
    Document   : index
    Created on : Dec 18, 2017, 10:41:58 AM
    Author     : HP
--%>

<%@page import="com.fb.api.Constants"%>
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
    <body
        
        <div id="parent">
            <center>
        <form action="<%= request.getContextPath()%>/login" method="get" id="form">
            <a href="https://www.facebook.com/dialog/oauth?client_id=<%= Constants.APP_ID%>&redirect_uri=<%= Constants.REDIRECT_URI%>&scope=email,user_friends,manage_pages,read_page_mailboxes,publish_actions,user_about_me,email,user_posts" > <img src="./images/facebookLoginButton.jpg"/> </a>
            
        </form>
            </center>   
        </div>
        
    </body>
</html>
