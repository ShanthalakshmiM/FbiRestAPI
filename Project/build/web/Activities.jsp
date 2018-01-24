<%-- 
    Document   : Activities
    Created on : Dec 22, 2017, 3:06:10 PM
    Author     : HP
--%>

<%@page import="com.fb.api.Activities"%>
<%@page import="com.fb.api.Constants"%>
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
    </style>
    <body> 
        
    <center>
        <div>
            <div class="header" >
                <h3 align="right" style="padding-right: 15px" class="text-white">Communicate with facebook</h3>      
            </div>
            <div class="body">
                <form action="<%=request.getContextPath()%>/myServlet" method="get">
                    <div class="btn-group-vertical">
<% Object cust = Activities.availCustomers; %>

                        <input type="submit" class="btn btn-default" name="btnGetMsg" value="Get messages"/> <br/>
                        <input type="submit" class="btn btn-default" name="btnGetCmnt" value="Get Comments"/><br/>
                        <input type="button" class="btn btn-default" onclick="getCustomerDetails();" value="Get customer details"/><br/>
                        <input type="button" class="btn btn-default" onclick="addForMessage();" value="Send Message"/><br/>
                        <input type="button" class="btn btn-default"  onclick="add();" value="Post to your page"/> <br/>
                        <input type="submit" class="btn btn-default" name="btReply" value="Post a reply"/>
                        <span id="myspan" style="width: 250px; margin-left: 20px; font-weight: bold; float: none;"></span><br/>

                         Enter the recipient id: <input type="text" name="recipientId"/><br/>
                    Enter the message: <input type="text" name="strMsg" /> <br/>
                    <input type="submit" name ="btnSndMsg" value="Send Message"/>

                    </div>
                </form>
<form action="<%=request.getContextPath()%>/myServlet" method="post">
    <input type="submit" class="btn-btn-default" name="broadcast" value="Broadcast message"/>
</form>
                <!-- display textbox to get post message -->
                <script type="text/javascript">
                    function add() {
                        var element = document.createElement("input");
                        element.setAttribute("type", "textarea");
                        element.setAttribute("name", "StrPost");
                        var spanvar = document.getElementById("myspan");
                        spanvar.appendChild(element);
                        var br = document.createElement('br');
                        var br2 = document.createElement('br');
                        spanvar.appendChild(br);
                        spanvar.appendChild(br2);
                        var element = document.createElement("input");
                        element.setAttribute("type", "submit");
                        element.setAttribute("name", "btnPost");
                        element.setAttribute("value", "Post");
                        element.setAttribute("class", "btn btn-default");
                        spanvar.appendChild(element);
                    }

                    function addForMessage() {
                        var spanvar = document.getElementById("myspan");
                      //var customers = new Activities.getCustomerDetails();
                       // document.writeln(customers);
                        
                        var element = document.createElement("input");
                        element.setAttribute("type", "textarea");
                        element.setAttribute("name", "StrMessage");

                        spanvar.appendChild(element);
                        var br = document.createElement('br');
                        var br2 = document.createElement('br');
                        spanvar.appendChild(br);
                        spanvar.appendChild(br2);
                        var element = document.createElement("input");
                        element.setAttribute("type", "submit");
                        element.setAttribute("name", "btnSendMsg");
                        element.setAttribute("value", "Send");
                        element.setAttribute("class", "btn btn-default");
                        spanvar.appendChild(element);
                    }
                    function getCustomerDetails(){
                        var spanvar = document.getElementById("myspan");
                        var customers = '<%= cust %>';
                        var custJson = JSON.parse(customers);
                      // document.write(customers[0]['convId']);
                        for (var i = 0; i < custJson.length; i++) {
                            var checkbox = document.createElement("input");
                            checkbox.type = "checkbox";
                            checkbox.name = "name"+i;
                         //  checkbox.value = customers[0][convId];
                           // checkbox.id = "id" + i;
                            spanvar.appendChild(checkbox);
                            var label = document.createElement("label");
                            label.appendChild(document.createTextNode(custJson[i]['senderName']));
                             spanvar.appendChild(label);
                            var br = document.createElement('br');
                            spanvar.appendChild(br);

                        }
                    }

                </script>
<!--                <div>
                    <p>
                        
                    </p>
                </div>-->

            </div>

                        <div>
                            <p>
                                <%= session.getAttribute("username") %>
                            </p>
                        </div>     
        </div>

    </center>

</body>
</html>
