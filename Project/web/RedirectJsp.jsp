<%-- 
    Document   : RedirectJsp
    Created on : Dec 19, 2017, 10:43:10 AM
    Author     : HP
--%>

<%@page import="com.fb.api.Activities"%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <style>
        #textarea{
            border:none;
            width: 750px;
            height: 500px;
            display: block;
            margin-left: auto;
            margin-right: auto;
            margin-top: 5%;
            font-family: Georgia;
            font-size: 24px;
        }
        .header {
            position: absolute;
            left: 0;
            right: 0;
            top: 0;
            background-color: #4775d1;
            height: 50px;
        }
        h3{
            color: white;
        }
        p{
            color:black;
        }
    </style>
    <body>
<!--        <div class="header"> 
            <h3 align="right" style="padding-right: 15px">Logged in as </h3>  
        </div>-->
<!--        <div>
            <p>
                <%= request.getParameter("result") %>
            </p>
        </div>
       -->
    <center>
        
<!--     
        <div>
            <p>
               
                
            </p>
        </div>-->
        <script>
            var convos = ${result};
           // document.writeln(messages);
         
         // document.writeln(convos[0]);
            
            for (i in convos) {
                document.write("<form action = '<%=request.getContextPath()%>/myServlet' method = 'get'>");
                var recipient = convos[i][0]['convId'];
              //  document.writeln(recipient);
                document.writeln("<textarea id = 'textarea'>");
                
                for(j in convos[i]){
                if(convos[i].postContent != null){
                    document.writeln("Post Message : "+messages[i].postContent);
                }
                else{
                    document.writeln(convos[i][j]['sender'] + " : " + convos[i][j]['content']+"\n");
                }
            }
            document.write("</textarea>");
            document.write("<input type= 'hidden' value = '"+recipient+"' name = 'recipientId'");
            document.writeln("<br/> <br/><input type = 'text' name = 'strMsg' /> <br/> <br/><input type = 'submit' name='btnSndMsg' value = 'send'/></form>");
        }
//            document.writeln(messages[0].convId);
//            document.writeln(messages[1].convId);
            //document.writeln("Global variables :" + obj );
            
           //for(var j=0; j<convos.length;j++){
//              for(i in convos){
//                  for(j in convos[i]){
//                      document.writeln(convos[i][j]['content']);
//                  }
//              }
      // }
        </script>
    </center>
           
</body>
</html>
