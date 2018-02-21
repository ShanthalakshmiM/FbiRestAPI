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

/**
 *
 * @author HP
 */
public class MongoDBConnection {
    DBCollection collection = null;
    public DBCollection connectToDB(String collectionName) throws UnknownHostException{
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB("testDb");
        DBCollection collection = db.getCollection(collectionName);
        this.collection = collection;
        return collection;
    }
    public DBCursor getRecords(BasicDBObject query){
        DBCursor cursor = collection.find(query);
        return cursor;
    }
    public DB getDB() throws UnknownHostException{
         MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB("testDb");
        return db;
    }
}
