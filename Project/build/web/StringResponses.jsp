<%-- 
    Document   : StringResponses
    Created on : Dec 28, 2017, 11:13:32 PM
    Author     : HP
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <style>
        h2{
            color : white;
            font-family: Georgia;
            font-size: 30px;

        }
        .header {
            position: absolute;
            left: 0;
            right: 0;
            top: 0;
            background-color: #4775d1;
            height: 50px;
        }
        .message{
            top:30%;
        }
        body{
            background-image: url("images/fbloginbckgrnd.jpg");
        }
    </style>
    <body>
        <div>
<!--            <div class="header"> 
                <h3 align="right" style="padding-right: 15px">Logged in as</h3>  
            </div>-->
            <center>
                <div class="message">
                    <% Object result = session.getAttribute("result");%>
                    <h2>
                        <%= result%>
                    </h2>
                </div>

            </center>
        </div>                
    </body>
</html>
