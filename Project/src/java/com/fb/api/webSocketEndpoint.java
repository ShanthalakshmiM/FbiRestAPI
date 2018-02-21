/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author HP
 */
@ServerEndpoint("/endpoint")
public class webSocketEndpoint {
    
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private static login streaming = new login();
   static{
      
       peers = Collections.synchronizedSet(new HashSet());
        try {
            streaming.socketStreaming();
//        try {
//           // System.out.println("onStatus called");
//           // streaming.onStatus();
//        } catch (IOException ex) {
//            Logger.getLogger(webSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
//        }
        } catch (IOException ex) {
            Logger.getLogger(webSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    @OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection"); 
       streaming.peers.add(session);
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
        
        System.out.println("Message from " + session.getId() + ": " + message);
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
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
        streaming.peers.remove(session);
    }

    
    
    
}
