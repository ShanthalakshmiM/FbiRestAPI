/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fb.api;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public class Chats {

    public DBCollection getCollection() throws UnknownHostException {
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("testDb");
        DBCollection collection = db.getCollection("chat");
        return collection;
    }

    public String getConversations(JSONArray customers) throws JSONException, UnknownHostException {
        DBCollection collection = getCollection();
        JSONArray availableChats = new JSONArray();
        for (int i = 0; i < customers.length(); i++) {
            JSONObject customer = customers.getJSONObject(i);
            String ID = customer.getString("customerID");
            BasicDBObject query = new BasicDBObject("customerId", ID);
            DBCursor cursor = collection.find(query);
            JSONArray chatsArr = new JSONArray();
           
            JSONArray chats = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("customerID", ID);
            //chatsArr.put(obj);
            while (cursor.hasNext()) {
                BasicDBObject savedData = (BasicDBObject) cursor.next();
                List<BasicDBObject> savedChat = (List< BasicDBObject>) savedData.get("chat");
                for (int j = 0; j < savedChat.size(); j++) {
                    
                    JSONObject customerChat = new JSONObject();
                    BasicDBObject savedChatData = savedChat.get(j);
                    long ts = savedChatData.getLong("timestamp");
                    //long ts = Long.parseLong(strTs);
                    customerChat.put("sender", savedChatData.getString("sender"));
                    customerChat.put("msg", savedChatData.getString("message"));
                    customerChat.put("time", ts);
                    chats.put(customerChat);
                    obj.put("chat", chats);
                    
                }
               // chatsArr.put(chats);
            }
            availableChats.put(obj);
        }
        return availableChats.toString();
    }

    public void saveMessage(JSONObject details, boolean sentFromPage) throws UnknownHostException, JSONException {
        System.out.println("Save Message");
        String IdToSave = null;
        if(sentFromPage == true){
            IdToSave = details.getString("pageId");
        }
        else{
            IdToSave = details.getString("customerId");
        }
        DBCollection collection = getCollection();
        BasicDBObject query = new BasicDBObject("customerId", details.getString("customerId"));
        DBCursor cursor = collection.find(query);
        
        if (cursor.hasNext()) {
            System.out.println("CustomerID record found");
            BasicDBObject savedData = (BasicDBObject) cursor.next();
            List<BasicDBObject> savedChat = (List< BasicDBObject>) savedData.get("chat");
            if (savedChat == null) {
                BasicDBObject addItem = new BasicDBObject("chat", new BasicDBObject("sender", IdToSave)
                        .append("message", details.getString("message")).append("timestamp", details.get("timestamp")));
                BasicDBObject updateQuery = new BasicDBObject("$push", addItem);
                collection.update(query, updateQuery);
            }
            else{
                BasicDBObject addItem = new BasicDBObject("chat", new BasicDBObject("sender", IdToSave)
                        .append("message", details.getString("message")).append("timestamp", details.get("timestamp")));
                BasicDBObject updateQuery = new BasicDBObject("$push", addItem);
                collection.update(query, updateQuery);
            }
            
        } else {
            System.out.println("CustomerID record not found");
            BasicDBObject document = new BasicDBObject();
            document.put("customerId", details.getString("customerId"));
            List<BasicDBObject> chats = new ArrayList<BasicDBObject>();
            BasicDBObject obj = new BasicDBObject();
            obj.put("sender", IdToSave);
            obj.put("message", details.getString("message"));
            obj.put("timestamp",details.get("timestamp"));
            chats.add(obj);
            document.put("chat", chats);
            collection.save(document);
        }

    }

}
