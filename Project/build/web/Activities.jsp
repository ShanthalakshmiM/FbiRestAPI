<%-- 
    Document   : Activities
    Created on : Dec 22, 2017, 3:06:10 PM
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <title>Facebook console</title>
        <style>
            #textarea{
                border:none;
                width: 700px;
                height: 500px;
                display: block;
                margin-left: auto;
                margin-right: auto;
            }
            body,table,td,th {
                margin: 0 auto;
                max-width: 1100px;
                padding: 0 20px;
                width: 100%;
                border-collapse: collapse;
                font-family: serif;
                font-size: large;
                background-color: ghostwhite ;
                height: 100%;
                /*#b0bcd5*/
            }
            .container{
                width: 650px;
            }
            .containner {
                border: 2px solid #dedede;
                background-color:#ddd;
                border-radius: 5px;
                padding: 10px;
                margin: 10px 0;
                width: fit-content;
            }
            .containner::after {
                content: "  ";
                clear: both;
                display: table;
            }
            #contacts
            {
                width: 200px;
                position: fixed;
                padding: 10px;
                margin: 10px 0;
                padding-bottom: 100%;
                overflow-y: scroll;
                padding-left: 10px;
                padding-right: 10px;
                right: 0px;
                border: 1px solid rgba(29, 49, 91, .3);

            }

            .sidebar-name 
            {
                padding-left: 10px;
                padding-right: 10px;
                margin-bottom: 4px;
                font-size: 12px;
            }

            .sidebar-name span
            {
                padding-left: 5px;
            }

            #sidebar-name-a
            {
                display: block;
                height: 100%;
                text-decoration: none;
                color: inherit;
            }

            #sidebar-name:hover
            {
                background-color:#e1e2e5;
            }

            #contactPic
            {
                width: 32px;
                height: 32px;
                vertical-align:middle;
            }

            .popup-box
            {
                display: none;
                position: fixed;
                bottom: 0px;
                right: 220px;
                height: 285px;
                background-color: rgb(237, 239, 244);
                width: 300px;
                border: 1px solid rgba(29, 49, 91, .3);
                padding-bottom: 10px;
            }

            .popup-box .popup-head
            {
                background-color: steelblue;
                /*#6d84b4*/
                padding: 5px;
                color: white;
                font-weight: bold;
                font-size: 14px;
                clear: both;
            }

            .popup-box .popup-head .popup-head-left
            {
                float: left;
            }

            .popup-box .popup-head .popup-head-right
            {
                float: right;
                opacity: 0.5;
            }

            .popup-box .popup-head .popup-head-right a
            {
                text-decoration: none;
                color: inherit;

            }

            .popup-box .popup-messages
            {
                height: 83%;
                overflow-y: scroll;

            }

            /*            #bodyLeft{
                            float: left;
                            border: 2px solid #dedede;
                            width: 80%;
                            height: fit-content;
                            margin: 10px 5px;
                            padding: 5px;
                            background-color: whitesmoke;
                            border-radius: 10px;
                        }*/
            #timeLeft{
                float: right;
                color: #999;
                height: 2px;
                font-size: 11px;
                margin-bottom: 5px;
                padding-bottom: 5px;
            }
            .bubble {
                box-sizing: border-box;
                float: left;
                width: auto;
                max-width: 80%;
                position: relative;
                clear: both;
                color: black;
                filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr='#95c2fd', endColorstr='#bee2ff');

                border: solid 1px rgba(0,0,0,0.5);
                -webkit-border-radius: 20px;
                -moz-border-radius: 20px;
                border-radius: 20px;

                -webkit-box-shadow: inset 0 8px 5px rgba(255,255,255,0.65), 0 1px 2px rgba(0,0,0,0.2);
                -moz-box-shadow: inset 0 8px 5px rgba(255,255,255,0.65), 0 1px 2px rgba(0,0,0,0.2);
                box-shadow: inset 0 8px 5px rgba(255,255,255,0.65), 0 1px 2px rgba(0,0,0,0.2);
                margin-bottom: 20px;
                padding: 6px 20px;
                word-wrap: break-word;
            }
/*            .bubble:before, .bubble:after {
                border-radius: 20px / 5px;
                content: '';
                display: block;
                position: absolute;
            }
            .bubble:before {
                border: 10px solid transparent;
                border-bottom-color: rgba(0,0,0,0.5);
                bottom: 0px;
                left: -7px;
                z-index: -2;

            }
            .bubble:after {
                border: 8px solid transparent;
                border-bottom-color: lightgrey;  arrow color 
                bottom: 1px;
                left: -5px;
            }
            .bubble-alt {
                float: right;
            }
            .bubble-alt:before {
                left: auto;
                right: -7px;
            }*/ 
            /*            #bodyRight{
                            float:  right;
                            border: 2px solid #dedede;
                            width: 80%;
                            height: fit-content;
                            margin: 10px 5px;
                            padding: 5px;
                            border-radius: 10px;
                            background-color: lightgrey;
                        }*/
            .popup-box .popup-reply{
                vertical-align: bottom;
                margin-bottom : 50px;
                width: 100%;
                height: 50px;
            }
            #replyButton{
                height: 30px;
                margin-left: 5px;
            }
            #textarea{
                border:none;
                width: 750px;
                height: 500px;
                display: block;
                margin-left: auto;
                margin-right: auto;
            }
            .sidenav {
                height: 45%;
                width: 300px;
                position: fixed;
                z-index: 1;
                top: 60px;
                left: 0;
                background-color: ghostwhite;
                overflow-x: hidden;
                padding-top: 10px;
            }
            #logout{
                float: right;
            }

        </style>

    </head>

    <body>

        <div id="test">
            <div>
                <img id="icon" class="sidenav" src="./images/twixor.png"/>
            </div>
            <div class="container">
                <h2>Communicate with Facebook</h2>
                <div id="logout">
                    <form action="<%=request.getContextPath()%>/Logout" method="get">
                        <input type="submit" name="btnLogout" value="Logout"/>
                    </form>
                </div>
                <div id="pageDropDown"> 

                </div><br/>

                <ul class="nav nav-tabs">
                    <li class="active" ><a data-toggle="tab" href="#menu1">view conversations</a></li>
                    <li ><a data-toggle="tab" href="#menu2" name="sendDM">send direct message</a></li>
                    <li><a data-toggle="tab" href="#menu3">Post a Status</a></li>
                    <li><a data-toggle="tab" href="#menu4">view posts</a></li>
                </ul>
                <div id="contacts" >
                    <h3> Chat </h3>
                    <h5> Contacts </h5> <br/>
                </div>

                <div class="tab-content">
                    <div id="menu1" class="tab-pane fade in active ">
                        <h3>Get messages</h3>
                        <form action="<%=request.getContextPath()%>/myServlet" method="get">
                            <input type = "submit" class =" btn btn-default" value = "Get Conversation" name = "btnGetMsg" />
                        </form>

                    </div>
                    <div id="menu2" class="tab-pane fade ">
                        <h3>Send direct message</h3>
                        <form action="<%=request.getContextPath()%>/myServlet" method="get">
                            <label>Recipient ID</label>
                            <input type="text" name="recipientId"> <br>
                            <label>Message </label>&nbsp;&nbsp;&nbsp;
                            <input type="text" name="strMsg"> <br>
                            <input type = "submit" class =" btn btn-default" value = "Send Message" name = "btnSndMsg" />

                        </form>

                    </div>

                    <div id="menu3" class="tab-pane fade">
                        <h3>Post a Status</h3>
                        <form action="<%=request.getContextPath()%>/myServlet" method="get">
                            <label>Status to be posted</label>
                            <input type="text" name="StrPost"> <br>
                            <input type = "submit" class =" btn btn-default" value = "Post" name = "btnPost" />
                        </form>

                    </div>

                    <div id="menu4" class="tab-pane fade">
                        <h3>Post and comments</h3>
                        <form action="<%=request.getContextPath()%>/myServlet" method="get">
                            <input type = "submit" class =" btn btn-default" value = "Get comments" name = "btnGetCmnt" />
                        </form>
                    </div>
                </div>
            </div>

        </div>
        <script type="text/javascript">

            <%
                String cookieValue = null;
                Cookie[] cookies = request.getCookies();
                System.out.println("Activities.jsp : " + cookies.toString());
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("userId")) {
                        cookieValue = cookie.getValue();
                    }

                }

            %>
            var param = <%= cookieValue%>;
            var wsUri = "ws://192.168.1.222:8080/Project/endpoint/" + param;
            alert(param);
            var webSocket = new WebSocket(wsUri);
            alert("On js file");
            webSocket.onerror = function (event) {
                onError(event);
            };

            function onError(event) {
                alert("ERROR  " + event.data);
                //    writeToScreen('<span style="color : red;"> Error: </span>' + event.data);
            }

            webSocket.onopen = function (evt) {

                onOpen(evt);
            };
            webSocket.onmessage = function (evt) {
                onMessage(evt);
            };


            function onOpen() {
                alert("Websocket onOpen");
                //   writeToScreen("Connected to " + wsUri + "</br>");
            }
            function onMessage(evt) {
                var message = evt.data;
                console.log(message);
                var obj = JSON.parse(message);
                if (obj.customerId !== undefined) {
                    var senderId = obj.customerId;
                    var timestamp = obj.timestamp;
                    writeMessageLeft(senderId, obj.message, timestamp);
                }

            }
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

            var element = document.getElementById("pageDropDown");
            var pages = ${pageDetails};
            var label = document.createElement("label");
            label.textContent = "Select a page     ";
            element.appendChild(label);
            var dropdown = document.createElement("select");
            dropdown.setAttribute("id", "pageSelection");
            var optionTemp = document.createElement("option");
            optionTemp.value = "select";
            optionTemp.textContent = "Select";
            dropdown.appendChild(optionTemp);


            for (i in pages) {
                var pageId = pages[i]['ID'];
                console.log(pageId);
                var pageName = pages[i]["Name"];
                console.log(pageName);
                var option = document.createElement("option");
                option.value = pageId;
                option.textContent = pageName;
                dropdown.appendChild(option);
            }
            element.appendChild(dropdown);
            var button = document.createElement("input");
            button.type = "button";
            button.value = "Change";
            button.addEventListener("click", function () {
                alert("Changed");
                var ele = document.getElementById("pageSelection");
                var selectedPage = ele.options[ele.selectedIndex].value;
                document.cookie = "page=" + selectedPage + "; path=/";
                alert(selectedPage);
            });
            var pageId = pages[0]['ID'];
            document.cookie = "page=" + pageId + "; path=/";
            var br = document.createElement("break");
            element.appendChild(br);
            element.appendChild(button);
            element.appendChild(br);

            Array.remove = function (array, from, to) {
                var rest = array.slice((to || from) + 1 || array.length);
                array.length = from < 0 ? array.length + from : from;
                return array.push.apply(array, rest);
            };

            var months = ["Jan", "Feb", "Mar", "April", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"];
            var total_popups = 0;
            var popups = [];
            var displayedPopups = [];
            var right = 220;
            var count = 0;

            function close_popup(id)
            {
                for (var iii = 0; iii < popups.length; iii++)
                {
                    if (id === popups[iii])
                    {
                        document.getElementById(id).style.display = "none";
                        calculate_popups();
                    }
                }
                for (var jjj = 0; jjj < displayedPopups.length; jjj++) {
                    if (id === displayedPopups[jjj]) {
                        Array.remove(displayedPopups, jjj);
                        count--;
                    }
                }
                if (right > 220) {
                    right = right - 320;
                }

                calculate_popups();
                display_popups_test();
                return;
            }

            function display_popups(id)
            {
                var iii = 0;
                var bool = 1;
                var val;

                for (iii; iii < total_popups; iii++)
                {
                    if (popups[iii] === id)
                    {
                        val = iii;
                        for (var s = 0; s < displayedPopups.length; s++) {
                            if (displayedPopups[s] !== id) {
                                bool = 1;

                            } else {
                                bool = 0;
                                document.getElementById(displayedPopups[s]).style.display = "block";
                                break;
                            }
                        }
                    }
                }
                if (bool === 1) {
                    var element = document.getElementById(popups[val]);
                    element.style.right = right + "px";
                    right = right + 320;
                    element.style.display = "block";
                    displayedPopups[count++] = id;
                }
                if (right >= 960) {
                    right = 220;
                }

                for (var jjj = iii; jjj < popups.length; jjj++)
                {
                    var element = document.getElementById(popups[jjj]);
                    element.style.display = "none";
                }
                scrollToBottom(id);
            }

            function register_popup(id, name)
            {

                for (var iii = 0; iii < popups.length; iii++)
                {
                    //already registered. Bring it to front.
                    if (id === popups[iii])
                    {
                        calculate_popups();
                        display_popups(id);

                        return;
                    }

                }

            }

            function calculate_popups()
            {
                var width = window.innerWidth;
                if (width < 540)
                {
                    total_popups = 0;
                } else
                {
                    width = width - 200;
                    //320 is width of a single popup box
                    total_popups = parseInt(width / 320);
                }
            }

            window.addEventListener("resize", calculate_popups);
            window.addEventListener("load", calculate_popups);
            //   alert("Customers");
            var customer = ${customers};
            console.log(customer);
            for (i in customer) {
                var id = customer[i]['customerID'];
                console.log(id);
                var identity = customer[i]['identity'];
                console.log(identity);
                register_popup_test(id, identity);
                var ele = document.getElementById("contacts");

                var div = document.createElement("div");
                div.id = "sidebar-name";
                var element = document.createElement("a");
                element.href = "javascript:display_popups('" + id + "')";
                element.id = "sidebar-name-a";
                var img = document.createElement("img");
                img.id = "contactPic";
                img.src = "./images/in-person.png";
                element.appendChild(img);
                var span = document.createElement("span");
                span.textContent = identity;
                element.appendChild(span);
                div.appendChild(element);
                var br = document.createElement("br");
                ele.appendChild(div);
            }

            var convos = ${conversations};
            console.log(convos);
            for (i in convos) {
                var customerId = convos[i]['customerID'];
                var chats = convos[i]['chat'];
                for (j in chats) {
                    var message = chats[j]['msg'];
                    var timestamp = chats[j]['time'];
                    if (chats[j]['sender'] === pageId) {
                        appendMessageRight(customerId, message, timestamp);
                    } else {
                        appendMessageLeft(customerId, message, timestamp);
                    }
                }
            }
            function appendMessageLeft(senderId, message, timestamp) {
                //  alert("writing Message");
                var idToCheck = "body" + senderId;
                var element = document.getElementById(senderId);
                var ele = element.getElementsByTagName("div");
                for (var i = 0; i < ele.length; i++) {
                    if (ele[i].id === idToCheck) {
                        var left = document.createElement("div");
//                        left.id = "bodyLeft";
                        left.setAttribute("class", "bubble");
                        left.style.cssFloat = "left";
                        left.style.backgroundColor = "whitesmoke";
                        var para = document.createElement("div");
                        para.innerHTML = message;
                        left.appendChild(para);
                        var span = document.createElement("span");
                        span.id = "timeLeft";
                        span.textContent = getTimestamp(timestamp);
                        left.appendChild(span);
                        ele[i].appendChild(left);
                        var br = document.createElement("br");
                        ele[i].appendChild(br);
                    }
                }
            }
            function appendMessageRight(senderId, message) {
                //  alert("writing Message");
                var idToCheck = "body" + senderId;
                var element = document.getElementById(senderId);
                var ele = element.getElementsByTagName("div");
                for (var i = 0; i < ele.length; i++) {
                    if (ele[i].id === idToCheck) {
                        var right = document.createElement("div");
//                        right.id = "bodyRight";
                        right.setAttribute("class", "bubble");
                        right.style.cssFloat = "right";
                        right.style.backgroundColor = "lightgrey";
                        var para = document.createElement("div");
                        para.innerHTML = message;
                        right.appendChild(para);
                        var span = document.createElement("span");
                        span.id = "timeLeft";
                        span.textContent = getTimestamp(timestamp);
                        right.appendChild(span);
                        ele[i].appendChild(right);
                        var br = document.createElement("br");
                        ele[i].appendChild(br);
                    }
                }
            }
            function scrollToBottom(customerId) {
                var id = "body" + customerId;
                var div = document.getElementById(id);
                div.scrollTop = div.scrollHeight;
            }

            function writeMessageLeft(senderId, message, timestamp) {
                alert("writing Message");
                var idToCheck = "body" + senderId;
                var name;

                var elementCheck = document.getElementById(senderId);
                if (elementCheck === null) {
                    register_popup_test(senderId, "unknown");
                    var ele = document.getElementById("contacts");

                    var div = document.createElement("div");
                    div.id = "sidebar-name";
                    var element = document.createElement("a");
                    element.href = "javascript:display_popups('" + id + "')";
                    element.id = "sidebar-name-a";
                    var img = document.createElement("img");
                    img.id = "contactPic";
                    img.src = "./images/in-person.png";
                    element.appendChild(img);
                    var span = document.createElement("span");
                    span.textContent = "unknown";
                    element.appendChild(span);
                    div.appendChild(element);
                    var br = document.createElement("br");
                    ele.appendChild(div);
                    ele.appendChild(br);
                }
                var element = document.getElementById(senderId);
                var ele = element.getElementsByTagName("div");
                for (var i = 0; i < ele.length; i++) {
                    if (ele[i].id === "headLeft") {
                        name = ele[i].textContent;
                        console.log(name);
                        register_popup(senderId, name);
                    }
                    if (ele[i].id === idToCheck) {
                        var left = document.createElement("div");
//                        left.id = "bodyLeft";
                        left.setAttribute("class", "bubble");
                        left.style.cssFloat = "left";
                        left.style.backgroundColor = "whitesmoke";
                        //  left.innerHTML += message;
                        var para = document.createElement("div");
                        para.innerHTML = message;
                        left.appendChild(para);
                        var span = document.createElement("span");
                        span.id = "timeLeft";
                        span.textContent = getTimestamp(timestamp);
                        left.appendChild(span);
                        ele[i].appendChild(left);

                        var br = document.createElement("br");
                        ele[i].appendChild(br);
                    }
                    ele[i].scrollTop = ele[i].scrollHeight;
                }

            }
            function writeMessageRight(senderId) {
                var popup = document.getElementById(senderId);
                var ele = popup.getElementsByTagName("div");
                var string;
                for (var i = 0; i < ele.length; i++) {
                    if (ele[i].id === "reply") {
                        var bottomDiv = ele[i].getElementsByTagName("input");
                        for (var j = 0; j < bottomDiv.length; j++) {
                            if (bottomDiv[j].id === "strReply") {
                                console.log(bottomDiv[j].value);
                                string = bottomDiv[j].value;
                                bottomDiv[j].value = " ";
                            }
                        }

                    }
                }

                //  alert("writing Message");
                var date = new Date();
                var timestamp = date.getTime();
                var idToCheck = "body" + senderId;
                var ele = document.getElementById(senderId).getElementsByTagName("div");
                for (var i = 0; i < ele.length; i++) {

                    if (ele[i].id === idToCheck) {
                        var right = document.createElement("div");
//                        right.id = "bodyRight";
                        right.setAttribute("class", "bubble");
                        right.style.cssFloat = "right";
                        right.style.backgroundColor = "lightgrey";
                        var para = document.createElement("div");
                        para.innerHTML = string;
                        right.appendChild(para);
                        var span = document.createElement("span");
                        span.id = "timeLeft";
                        span.textContent = getTimestamp(timestamp);
                        right.appendChild(span);
                        ele[i].appendChild(right);
                        var br = document.createElement("br");
                        ele[i].appendChild(br);
                    }
                    ele[i].scrollTop = ele[i].scrollHeight;
                }

                console.log(string);
                sendMessage(string, senderId, timestamp);

            }
            function sendMessage(string, id, ts) {
                var pageIdSelected = null;
            <%
                String pageId = null;
                Cookie[] cks = request.getCookies();
                for (Cookie ck : cks) {
                    if (ck.getName().equals("page")) {
                        pageId = ck.getValue();
                    }
                }

            %>
                pageIdSelected = <%= pageId%>;
                console.log(pageIdSelected);
                //  alert("sending message");
                var url = "login";

                var params = "replyMsg=" + string + "&customer=" + id + "&pageId=" + pageIdSelected + "&timestamp=" + ts;
                var xmlhttp;
                if (window.XMLHttpRequest) {
                    xmlhttp = new XMLHttpRequest();
                } else if (window.ActiveXObject) {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.open("POST", url, true);
                xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlhttp.send(params);
            }
            function getTimestamp(timestamp) {
                var today = new Date(timestamp);
                var mnth = months[today.getMonth()];
                var date = today.getDate();
                var hrs = today.getHours();
                var min = today.getMinutes();
                var dateToAppend = mnth + " " + date + " " + hrs + " : " + min;
                return dateToAppend;
            }

            function register_popup_test(id, name)
            {

                for (var iii = 0; iii < popups.length; iii++)
                {
                    //already registered. Bring it to front.
                    if (id === popups[iii])
                    {
                        Array.remove(popups, iii);

                        popups.unshift(id);

                        calculate_popups();


                        return;
                    }
                }
                var msgBodyId = "body" + id;
                var element = '<div class="popup-box chat-popup" id="' + id + '">';
                element = element + '<div class="popup-head">';
                element = element + '<div class="popup-head-left" id="headLeft">' + name + '</div>';
                element = element + '<div class="popup-head-right" id="headRight"><a href="javascript:close_popup(\'' + id + '\');">&#10005;</a></div>';
                element = element + '<div style="clear: both"></div></div><div class="popup-messages" id="' + msgBodyId + '"></div>';
                element = element + '<div id="reply"><input type="text" style="width:80%; border-radius:8px" id="strReply"/><a href = "javascript:writeMessageRight(' + id + ');">';
                element = element + '<img id ="replyButton" src="./images/send-button-hover.png"/></a></div></div>';
                document.getElementsByTagName("body")[0].innerHTML = document.getElementsByTagName("body")[0].innerHTML + element;

                popups.unshift(id);

                calculate_popups();
                //  display_popups(id);

            }
            function display_popups_test()
            {
                var right = 220;

                var iii = 0;
                for (iii; iii < total_popups; iii++)
                {
                    if (displayedPopups[iii] !== undefined)
                    {
                        var element = document.getElementById(displayedPopups[iii]);
                        element.style.right = right + "px";
                        right = right + 320;
                        element.style.display = "block";
                    }
                }

                for (var jjj = iii; jjj < displayedPopups.length; jjj++)
                {
                    var element = document.getElementById(displayedPopups[jjj]);
                    element.style.display = "none";
                }
            }
        </script>

    </body>
</html>
