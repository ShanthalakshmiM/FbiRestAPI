/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author HP
 */
@ServerEndpoint("/endpoint/{userId}")
public class webSocketEndpoint {

    private static login streaming = new login();
    private  String sessionId = null;
    private Session session;
    static {
        System.out.println("On webSocket Endpoint");
        try {
            streaming.socketStreaming();
        } catch (IOException ex) {
            Logger.getLogger(webSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnOpen
    public void onOpen(@PathParam("userId") String loggeduserId, Session session) throws IOException {
         System.out.println(session.getId() + " has opened a connection");
        try{
       
        this.sessionId = loggeduserId;
        this.session = session;
        streaming.sockets.put(sessionId, this);
        }catch(Exception e){
            System.out.println(e.toString());
        }
        //this.session.getBasicRemote().sendText("Connected to " + sessionId);
    }

    /**
     * When a user sends a message to the server, this method will intercept the
     * message and allow us to react to it. For now the message is read as a
     * String.
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        System.out.println("Message from : " + message);
        if (session == this.session) {
            this.session.getBasicRemote().sendText(message + "\n");
        } else {
            System.out.println("Message not sent");
        }
//        try {
//            session.getBasicRemote().sendText(message);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    /**
     * The user closes the connection.
     *
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
        streaming.peers.remove(session);
    }

    public Session getSession() {
        return this.session;
    }
    public String getSessionId(){
        return this.sessionId;
    }

}
