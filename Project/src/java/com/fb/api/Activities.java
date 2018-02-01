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
import com.restfb.types.send.ButtonTemplatePayload;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.PostbackButton;
import com.restfb.types.send.SendResponse;
import com.restfb.types.send.TemplateAttachment;
import com.restfb.types.send.WebButton;
//import com.restfb.types.send.Message;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public class Activities {

    
   

    //client with page access token 
     FacebookClient fbPageClient;
     String pageId;
      public Activities() throws FileNotFoundException, IOException {
        System.out.println("Constructor called");
        FileReader reader = new FileReader("C:/Users/HP/Desktop/Documents/NetBeansProjects/Project/web/WEB-INF/config.properties");
        Properties prop = new Properties();
        
        prop.load(reader);
        String pat = prop.getProperty("pageAccessToken");
         this.fbPageClient = new DefaultFacebookClient(pat);
         System.out.println("In activities - Pat : "+prop.getProperty("pageAccessToken"));
         this.pageId = prop.getProperty("pageId");
    }
   
    public  String makePost(String fbmessage) {
        String postStatus = new String();
        //publish post with page ID and get post ID from response
        FacebookType postResponse = fbPageClient.publish(pageId+ "/feed", FacebookType.class, Parameter.with("message", fbmessage));

        if (postResponse.getId() != null || !(postResponse.getId().equals(""))) {

          
            postStatus = "Status posted successfully in your page";
        }
        return postStatus;
    }

    public  JSONArray getAllPostComments() throws JSONException {
        //to hold the comments of all posts

        JSONArray pagePosts = new JSONArray();
//        String postId = new String();
//        String postMsg = new String();
        //fetch all the posts
        Connection<Post> pageFeed = fbPageClient.fetchConnection(pageId+ "/feed", Post.class);
        System.out.println("---"+pageFeed);
        for (List<Post> feed : pageFeed) {

            for (Post post : feed) {
                //for ech post
                JSONObject posts = new JSONObject();
                
           
                Connection<Comment> cmntDetails = fbPageClient.fetchConnection(post.getId() + "/comments", Comment.class, Parameter.with("fields", "message,from{id,name}"));
                
                if (cmntDetails != null ) {

                    List<Comment> cmntList = cmntDetails.getData();
                    if(cmntList.size() > 0){
                       System.out.println("Comment Details : " + post.getMessage()+"---"+cmntList);
                       JSONObject newCmnts = new JSONObject();
                       JSONArray receivedCmnts = new JSONArray();
                       for (Comment comment : cmntList) {
                            
                            JSONObject cmnts = new JSONObject();
                            //fetch sender name and message
                            try {
                                
                                cmnts.put("sender", comment.getFrom().getName());
                                cmnts.put("comment", comment.getMessage());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            receivedCmnts.put(cmnts);
                            

                        }
                       newCmnts.put("postMessage", post.getMessage());
                       newCmnts.put("comments", receivedCmnts);
                       // System.out.println("==="+newCmnts);
                       pagePosts.put(newCmnts);
                       
                       
                    }else{
                        posts.put("postMessage", post.getMessage());
                        pagePosts.put(posts);
                    }
                    
                }else{
                    posts.put("postMessage", post.getMessage());
                    pagePosts.put(posts);
                }
                
            }

        }
//        System.out.println(postId + postMsg);
//        fbPageClient.publish(postId+"/comments", String.class, Parameter.with("message", "comment through api"));

        return pagePosts;
    }

    public String replyToComment(String commentId) {
        fbPageClient.publish(commentId + "/comments", String.class, Parameter.with("message", "reply through api"));
        return "success";
    }
   

    public JSONArray getConversations() throws JSONException, FileNotFoundException, IOException {
        
        String id = new String();

        //fetch users sent message to your page
        Message m = null;
        Connection<Conversation> connection = fbPageClient.fetchConnection("me/conversations", Conversation.class);
        // Connection<Conversation> connection = fbPageClient.fetchConnection("me/conversations", Conversation.class);

        JSONArray receivedConvos = new JSONArray();
        for (List<Conversation> conversationPage : connection) {
            for (Conversation convo : conversationPage) {

                JSONArray receivedMsgs = new JSONArray();
                id = convo.getId();
                Connection<Message> messages = fbPageClient.fetchConnection(id + "/messages", Message.class, Parameter.with("fields", "message, created_time, from, id"));
                //System.out.println("Activities.java : Messages : " + messages.toString());
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

    public String postToPage(String message) {
        String status = new String();
        
        FacebookType response = fbPageClient.publish(pageId + "/feed", FacebookType.class, Parameter.with("message", message));
        if (response.getId() != null) {
            status = "Successfully posted";
        }

        return status;
    }

    public String sendMessage(String id, String message) throws JSONException {
        // com.restfb.types.send.Message msg = new com.restfb.types.send.Message(message);
//        MediaAttachment image = new MediaAttachment(MediaAttachment.Type.IMAGE, "http://restfb.com/documentation/");
//        com.restfb.types.send.Message msg = new com.restfb.types.send.Message(image);
        IdMessageRecipient recipient = new IdMessageRecipient(id);

        SendResponse resp = fbPageClient.publish(id + "/messages", SendResponse.class, Parameter.with("recipient", recipient), Parameter.with("message", message));

        return "success";
    }

}
