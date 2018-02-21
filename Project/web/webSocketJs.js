/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var wsUri = "ws://10.0.0.13:8080/Project/endpoint";
var webSocket = new WebSocket(wsUri);
alert("On js file");
webSocket.onerror = function(event){ onError(event); };

function onError(event){
    writeToScreen('<span style="color : red;"> Error: </span>'+event.data);
}
var output = document.getElementById("output");
webSocket.onopen = function (evt) {
    
    onOpen(evt);
};
webSocket.onmessage = function (evt) {
    onMessage(evt);
};
function writeToScreen(message) {
    output.innerHTML += message;
}

function onOpen() {
    alert("Websocket onOpen");
    writeToScreen("Connected to " + wsUri + "</br>");
}
function onMessage(evt) {
    var message = evt.data;
    writeToScreen(message);
}



