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
public class Test {

    public static void main(String[] args) throws UnknownHostException, JSONException {
        System.out.println("Program running");
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("testDb");
        DBCollection collection = db.getCollection("testEnterprise");
//        String senderId = "webhook2";
//        String EnterpriseId = "test123";
//        BasicDBObject query = new BasicDBObject("enterpriseId", EnterpriseId);
//        DBCursor cursor = collection.find(query);
//        if (cursor.count() > 1) {
//            System.out.println("ERROR : More than 1 record");
//        } else {
//            if (cursor.hasNext()) {
//                BasicDBObject savedData = (BasicDBObject) cursor.next();
//                List<BasicDBObject> savedCustomers = (List<BasicDBObject>) savedData.get("customerDetails");
//                for (int i = 0; i < savedCustomers.size(); i++) {
//                    BasicDBObject savedCustomer = savedCustomers.get(i);
//                    String savedSenderId = savedCustomer.getString("webhookId");
//                    if (savedSenderId.equals(senderId)) {
//                        String savedMobileNum = savedCustomer.getString("mobileNumber");
//                        if (savedMobileNum == null || savedMobileNum.equals(" ")) {
//                            System.out.println("Send URL");
//                        }
//                    }
//                    else {
//                        System.out.println("Append");
//                        BasicDBObject addItem = new BasicDBObject("customerDetails", new BasicDBObject("webhookId", "test2").append("convoId", "test2").append("mobileNumber","test2"));
//                        BasicDBObject updateQuery = new BasicDBObject("$push", addItem);
//                        collection.update(query, updateQuery);
//                    }
//
//                }
//            } else {
//                BasicDBObject objectToSave = new BasicDBObject();
//                BasicDBObject customerDetails = new BasicDBObject();
//                List<BasicDBObject> cust = new ArrayList<>();
//                customerDetails.put("webhookId", "webhook123");
//                customerDetails.put("convId", "convo123");
//                customerDetails.put("mobileNumber", "");
//                cust.add(customerDetails);
//                objectToSave.put("enterpriseId", "test123");
//                objectToSave.put("customerDetails", cust);
//                
//                collection.save(objectToSave);
//                System.out.println(objectToSave);
//            }
//        }
        System.out.println("Db connected");
        //   BasicDBObject query = new BasicDBObject("pageId","200864727141137");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            BasicDBObject savedData = (BasicDBObject) cursor.next();
            BasicDBObject savedATs = (BasicDBObject) savedData.get("accessTokens");
            List<BasicDBObject> savedPageData = (List<BasicDBObject>) savedATs.get("PageAccessTokens");
            if (savedPageData != null) {
                for (int i = 0; i < savedPageData.size(); i++) {
                    BasicDBObject savedPage = savedPageData.get(i);
                    String savedPageId = savedPage.getString("pageId");
                    if (savedPageId.equals("200864727141137")) {
                        System.out.println(savedData.get("userId") + " : " + savedPage.getString("pageName"));
                    }
                }
            }

        }
    }

}
