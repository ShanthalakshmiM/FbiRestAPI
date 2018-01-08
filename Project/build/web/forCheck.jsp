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
        <script>
            var posts = ${result};
            document.write("<textarea>");
            for(var i =0; i<posts.length;i++){
                document.write(posts[i]['postMessage']);
               
            }
        </script>
    </body>
</html>
