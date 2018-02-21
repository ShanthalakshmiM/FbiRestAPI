<%-- 
    Document   : Activities
    Created on : Dec 22, 2017, 3:06:10 PM
    Author     : HP
--%>

<%@page import="com.fb.api.Activities"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*" %>






<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <title>JSP Page</title>
    </head>
    <style>
        body{
            background-image: url("images/fbloginbckgrnd.jpg");
        }
        .header {
            position: absolute;
            left: 0;
            right: 0;
            top: 0;

            height: 50px;
        }
        .body{
            top:180px;
            position: absolute;
            vertical-align: middle;
            left: 40%;
        }
        h3{
            color: white;
        }
        output{
            color : white;
        }
    </style>


    <body> 
    
    <center>
        <div>
            <div class="header" >
                <h3 align="right" style="padding-right: 15px" class="text-white">Communicate with facebook</h3>      
            </div>
            <div class="body">


                <div class="btn-group-vertical">
                    <button type="button"  data-toggle="collapse" data-target="#forPost">Post to your page</button> <br/><br/>
                    <button type="button"  data-toggle="collapse" data-target="#forMessage">Send Message</button> <br/><br/>
                    <form action="<%=request.getContextPath()%>/myServlet" method="get">
                        <div class = "collapse" id = "forPost">
                            <input type="text" name="StrPost"/><br/><br/>
                            <input type="submit" value="Post" name="btnPost"/> <br/><br/>
                        </div>
                        <div class = "collapse" id = "forMessage">
                            Enter the recipient id: <input type="text" name="recipientId"/><br/><br/>
                            Enter the message: <input type="text" name="strMsg" /> <br/><br/>
                            <input type="submit" name ="btnSndMsg" value="Send"/><br/><br/>
                        </div>

                        <input type="submit" class="btn btn-default" name="btnGetMsg" value="Get messages"/> <br/><br/>
                        <input type="submit" class="btn btn-default" name="btnGetCmnt" value="Get Comments"/><br/><br/>
                        <!--                        <input type="button" class="btn btn-default" onclick="getCustomerDetails();" value="Get customer details"/><br/>-->
                        <!--                        <input type="submit" class="btn btn-default" name="btReply" value="Post a reply"/>-->
                    </form> 
                </div>

                <form action="<%=request.getContextPath()%>/myServlet" method="post">
                    <input type="submit" class="btn-btn-default" name="broadcast" value="Broadcast message"/>
                </form> <br/><br/>

                <div id="output"> </div>
            </div>
        </div>
    </center>
    
    <!--                                     <script type="text/javascript" src="webSocketJs.js"></script>-->
    <!-- display textbox to get post message -->
    <script type="text/javascript">
        var wsUri = "ws://10.0.0.13:8080/Project/endpoint";
        var webSocket = new WebSocket(wsUri);
        alert("On js file");
        webSocket.onerror = function (event) {
            onError(event);
        };

        function onError(event) {
            writeToScreen('<span style="color : red;"> Error: </span>' + event.data);
        }
        var output = document.getElementById("output");
        webSocket.onopen = function (evt) {

            onOpen(evt);
        };
        webSocket.onmessage = function (evt) {
            onMessage(evt);
        };
        function writeToScreen(message) {
            output.innerHTML += message +' <br/>';
        }

        function onOpen() {
            alert("Websocket onOpen");
            writeToScreen("Connected to " + wsUri + "</br>");
        }
        function onMessage(evt) {
            var message = evt.data;
            writeToScreen(message);
        }

    </script>
    <script type="text/javascript">
        var acc = document.getElementsByClassName("accordion");
        var i;

        for (i = 0; i < acc.length; i++) {
            acc[i].addEventListener("click", function () {
                this.classList.toggle("active");
                var panel = this.nextElementSibling;
                if (panel.style.display === "block") {
                    panel.style.display = "none";
                } else {
                    panel.style.display = "block";
                }
            });
        }


    </script>



</body>
</html>
