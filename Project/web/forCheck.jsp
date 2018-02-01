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
            var body = document.getElementsByTagName('body')[0];
            var tbl = document.createElement('table');
            tbl.style.width = '100%';
            tbl.setAttribute('border', '1');

            var header = ['post','comments','sender'];

            var tbdy = document.createElement('tbody');

                var tr = document.createElement('tr');
                header.forEach(function(ele){

                        var td = document.createElement('th');
                        td.appendChild(document.createTextNode(ele));
                        tr.appendChild(td)

                });
                tbdy.appendChild(tr);
                
            var posts =  <%= session.getAttribute("result") %>;
            for(var i=0; i<posts.length; i++ ){
                
                  if(posts[i].comments){
                var tr = document.createElement('tr');
                
		var td = document.createElement('td');
		td.appendChild(document.createTextNode(posts[i].postMessage));
                if(posts[i].comments.length > 1){
                    td.setAttribute('rowSpan', posts[i].comments.length)
                }
                tr.appendChild(td)
                
                var td = document.createElement('td');
		td.appendChild(document.createTextNode(posts[i].comments[0].comment));
		tr.appendChild(td)
                
                var td = document.createElement('td');
		td.appendChild(document.createTextNode(posts[i].comments[0].sender));
		tr.appendChild(td)
                tbdy.appendChild(tr);
		
                for(var j = 1; j<posts[i].comments.length; j++){
                        
                var tr = document.createElement('tr');
                var td = document.createElement('td');
		td.appendChild(document.createTextNode(posts[i].comments[j].comment));
		tr.appendChild(td)
                
                var td = document.createElement('td');
		td.appendChild(document.createTextNode(posts[i].comments[j].sender));
		tr.appendChild(td)
                tbdy.appendChild(tr);
                    }
		
		
		
                    
                }else{
                    var tr = document.createElement('tr');
                
                    var td = document.createElement('td');
                    td.appendChild(document.createTextNode(posts[i].postMessage));

                    tr.appendChild(td)

                    var td = document.createElement('td');
                    td.appendChild(document.createTextNode(" "));
                    tr.appendChild(td)

                    var td = document.createElement('td');
                    td.appendChild(document.createTextNode(" "));
                    tr.appendChild(td)

                    tbdy.appendChild(tr);
                }
                
            }
            
                tbl.appendChild(tbdy);
            document.body.appendChild(tbl)
            /*for(var i=0; i<posts.length; i++ ){
                 document.write("<textarea>");
                 
                if(Array.isArray(posts[i])){
                    for(j=0;j<posts[i].length;j++)
                        document.writeln(posts[i][j]['sender']+" : "+posts[i][j]['comment']);
                }
                else{
                    document.write(posts[i]['postMessage']);
                }
               document.write("</textarea>");
            }*/
            
        </script>
    </body>
</html>
