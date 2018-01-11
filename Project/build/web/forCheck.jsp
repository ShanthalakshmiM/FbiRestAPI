<%-- 
    Document   : forCheck
    Created on : Jan 8, 2018, 5:22:43 PM
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>
            <%= session.getAttribute("result") %>
        </p>
        <script>
            var posts = ${result};
           
            for(var i=0; i<posts.length; i++ ){
                 document.write("<textarea>");
                 
                if(Array.isArray(posts[i])){
                    for(j=0;j<posts[i].length;j++)
                        document.writeln(posts[i][j]['sender']+" : "+posts[i][j]['comment']);
                }
                else{
                    document.write(posts[i]['postMessage']);
                }
               document.write("</textarea>");
            }
            
        </script>
    </body>
</html>
