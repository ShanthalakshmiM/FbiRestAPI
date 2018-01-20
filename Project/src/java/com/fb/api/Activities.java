/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.Comment;
import com.restfb.types.Conversation;
import com.restfb.types.FacebookType;
import com.restfb.types.GraphResponse;
import com.restfb.types.Message;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.SendResponse;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public class Activities {
    static JSONObject customerDetails = new JSONObject();
    static FacebookClient fbclient = new DefaultFacebookClient(Constants.MY_ACCESS_TOKEN);

    //client with page access token 
    static FacebookClient fbPageClient = new DefaultFacebookClient(Constants.PAGE_ACCESS_TOKEN);

    public static String makePost(String fbmessage) {
        String postStatus = new String();
        //publish post with page ID and get post ID from response
        FacebookType postResponse = fbPageClient.publish(Constants.PAGE_ID + "/feed", FacebookType.class, Parameter.with("message", fbmessage));

        if (postResponse.getId() != null || postResponse.getId().equals("")) {

            // Constants.post_id = postResponse.getId();
            postStatus = "Status posted successfully in your page";
        }
        return postStatus;
    }

    public static JSONArray getAllPostComments() throws JSONException {
        //to hold the comments of all posts

        JSONArray pagePosts = new JSONArray();
        String postId = new String();
        String postMsg = new String();
        //fetch all the posts
        Connection<Post> pageFeed = fbPageClient.fetchConnection(Constants.PAGE_ID + "/feed", Post.class);
        for (List<Post> feed : pageFeed) {

            for (int i = 0; i < feed.size(); i++) {
                //for ech post
                JSONObject posts = new JSONObject();
                Post post = feed.get(i);
                // posts += post.getMessage();
                posts.put("postMessage", post.getMessage());
                postMsg = post.getMessage();
                postId = post.getId();
                //pagePosts.put(posts);
                Connection<Comment> cmntDetails = fbPageClient.fetchConnection(post.getId() + "/comments", Comment.class, Parameter.with("fields", "message,from{id,name}"));
                //if(cmntDetails.getTotalCount()==0){
                if (cmntDetails != null) {

                    List<Comment> cmntList = cmntDetails.getData();

                    for (Comment comment : cmntList) {
                        JSONArray receivedCmnts = new JSONArray();
                        JSONObject cmnts = new JSONObject();
                        //fetch sender name and message
                        try {
                            // cmnts.put("postMessage", post.getMessage());
                            cmnts.put("sender", comment.getFrom().getName());
                            cmnts.put("comment", comment.getMessage());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        receivedCmnts.put(cmnts);
                        posts.put("comments",receivedCmnts);

                    }
                }
                pagePosts.put(posts);
            }
            
        }
//        System.out.println(postId + postMsg);
//        fbPageClient.publish(postId+"/comments", String.class, Parameter.with("message", "comment through api"));

        return pagePosts;
    }

    public static JSONArray getComments() {
        JSONArray fullCmnts = new JSONArray();
        Connection<Post> pageFeed = fbPageClient.fetchConnection(Constants.PAGE_ID + "/feed", Post.class);
        for (List<Post> feed : pageFeed) {
            for (Post post : feed) {
                Connection<Comment> cmntDetails = fbPageClient.fetchConnection(post.getId() + "/comments", Comment.class, Parameter.with("fields", "message,from{id,name}"));

                if (cmntDetails != null) {
                    List<Comment> cmntList = cmntDetails.getData();

                    for (Comment comment : cmntList) {
                        JSONObject cmnts = new JSONObject();
                        //fetch sender name and message
                        try {
                            cmnts.put("postMessage", post.getMessage());
                            cmnts.put("sender", comment.getFrom().getName());
                            cmnts.put("comment", comment.getMessage());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        fullCmnts.put(cmnts);

                    }
                }
            }
        }
        return fullCmnts;

    }
    public static JSONArray availCustomers = new JSONArray();

    public static JSONArray getConversations() throws JSONException {
        JSONArray customers = new JSONArray();
        String id = new String();
        
        //fetch users sent message to your page
        Message m = null;
        Connection<Conversation> connection = fbPageClient.fetchConnection("me/conversations", Conversation.class);
        // Connection<Conversation> connection = fbPageClient.fetchConnection("me/conversations", Conversation.class);
        List<Conversation> conversationPageTemp = connection.getData();

        for (int i = 0; i < conversationPageTemp.size(); i++) {
            Conversation temp = conversationPageTemp.get(i);

            JSONObject customerDetails = new JSONObject();
            customerDetails.put("convId", temp.getId());
           // customerDetails.put("senderName", temp.getParticipants().get(0).getName());
            customers.put(customerDetails);
        }
        availCustomers = customers;
        JSONArray receivedConvos = new JSONArray();
        for (List<Conversation> conversationPage : connection) {
            for (Conversation convo : conversationPage) {
                JSONArray receivedMsgs = new JSONArray();
                id = convo.getId();
                Connection<Message> messages = fbPageClient.fetchConnection(id + "/messages", Message.class, Parameter.with("fields", "message, created_time, from, id"));
                System.out.println("Activities.java : Messages : "+messages.toString());
                try {

                    List<Message> data = messages.getData();
                    for (int i = 0; i < data.size(); i++) {
                        m = data.get(i);
                        JSONObject msg = new JSONObject();
                        msg.put("convId", id);
                        msg.put("sender", m.getFrom().getName());
                        msg.put("senderId", m.getFrom().getId());
                        msg.put("content", m.getMessage());
                        receivedMsgs.put(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                receivedConvos.put(receivedMsgs);

            }

        }

        return receivedConvos;
    }

    public static String postToPage(String message) {
        String status = new String();

        FacebookType response = fbPageClient.publish(Constants.PAGE_ID + "/feed", FacebookType.class, Parameter.with("message", message));
        if (response.getId() != null) {
            status = "Successfully posted";
        }

        return status;
    }
//    public String getCustomerDetails() throws JSONException{
//         Connection<Conversation> connection = fbPageClient.fetchConnection("me/conversations", Conversation.class);
//        List<Conversation> conversationPage = connection.getData();
//        if(customers != null){
//        for (int i = 0; i < conversationPage.size(); i++) {
//            Conversation temp = conversationPage.get(i);
//
//            JSONObject customerDetails = new JSONObject();
//            customerDetails.put("convId", temp.getId());
//            customerDetails.put("senderName", temp.getParticipants().get(0).getName());
//            customers.put(customerDetails);
//        }
//        }
//        return customers.toString();
//    }

    public static String sendMessage(String id, String message) throws JSONException {

        IdMessageRecipient recipient = new IdMessageRecipient(id);

        SendResponse resp = fbPageClient.publish(id + "/messages", SendResponse.class, Parameter.with("recipient", recipient), Parameter.with("message", message));

        return "success";
    }
    public static String test(String recipientId, String message){
        IdMessageRecipient recipient = new IdMessageRecipient(recipientId);

        SendResponse resp = fbPageClient.publish(recipientId + "/messages", SendResponse.class, Parameter.with("recipient", recipient), Parameter.with("message", message));
        
        return "success";
    }

}
