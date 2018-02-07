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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public class Test {
    
    
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, JSONException{
        MongoClient mongo = new MongoClient("localhost",27017);
        DB db = mongo.getDB("testDb");
        DBCollection collection = db.getCollection("testEnterprise");
        
//        BasicDBObject document = new BasicDBObject();
//        document.put("username", "Shantha");
//        
//        BasicDBObject accessToken = new BasicDBObject();
//        accessToken.put("accessToken", "ATForTest");
//        accessToken.put("longLivedAT", "LongLivedATForTest");
//        
//        BasicDBObject pageDetails = new BasicDBObject();
//        pageDetails.put("pageId","12345");
//        pageDetails.put("pageName","shanthaPage");
//        pageDetails.put("accessToken","PageATForTest");
//        
//        accessToken.put("PageAccessTokens",pageDetails);
//        document.put("accessToken",accessToken);
//        
//        collection.save(document);
        System.out.println("----Inserted----");
        
        DBCursor cursor = collection.find();
        System.out.println("Retrieved "+cursor.count());
        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
            
        
    }
}
