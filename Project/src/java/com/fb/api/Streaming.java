/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.restfb.DefaultJsonMapper;
import com.restfb.types.webhook.Change;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public class Streaming {

    private String webhookResponse = null;
    private String cookieValue = null;
    public static Set<Session> peers = null;
    String Message = new String();
    public void setValue(String responseObject, String cookie) {
        System.out.println("Values set in Streaming.java\nCookie value : " + cookie);
        this.webhookResponse = responseObject;
        this.cookieValue = cookie;

    }

    public String onStreaming(WebhookEntry entry) throws JSONException, JSONException, JSONException, JSONException, IOException {
        String message = new String();
        System.out.println("On Streaming Cookie Value : " + cookieValue);
        Activities activities = new Activities(cookieValue,null);
        DefaultJsonMapper mapper = new DefaultJsonMapper();
        WebhookObject webhookObject = mapper.toJavaObject(webhookResponse, WebhookObject.class);
        for (Change change : entry.getChanges()) {
            if (change.getField().equals("feed")) {
                JSONObject webhookResponse = new JSONObject(this.webhookResponse);
                JSONArray entryJson = webhookResponse.getJSONArray("entry");
                JSONArray changes = entryJson.getJSONObject(0).getJSONArray("changes");
//                        System.out.println("Changes : "+changes);
                JSONObject value = changes.getJSONObject(0).getJSONObject("value");
                JSONObject from = value.getJSONObject("from");
                String sender = from.getString("name");
//                        System.out.println("Values : "+value);
                String item = value.getString("item");

                //    for (Session peer : peers) {
                if (item.equals("reaction")) {
                    String reac_type = value.getString("reaction_type");
                    if (reac_type.equals("like")) {
                        System.out.println(sender + " liked your post");
                        message = sender + " liked your post";
                        // peer.getBasicRemote().sendText(sender + " liked your post");
                    } else {
                        System.out.println(sender + " reacted " + reac_type + " to your post");
                        message = sender + " reacted " + reac_type + " to your post";
                        //  peer.getBasicRemote().sendText(sender + " reacted " + reac_type + " to your post");
                    }

                } else if (item.equals("comment")) {
                    String postContent = value.getString("message");
                    System.out.println(sender + " : " + postContent);
                    message = sender + " : " + postContent;
                    //  peer.getBasicRemote().sendText(sender + " : " + postContent);
                }

                //  }
            }

        }
        return message;
    }

    public void setMessage(String message) {
        System.out.println("Message set");
        Message = message;
    }

    public void onStatus() throws IOException {
        for (Session peer : peers) {
            System.out.println("In streaming : "+Message);
            peer.getBasicRemote().sendText(Message);
        }
    }

}
