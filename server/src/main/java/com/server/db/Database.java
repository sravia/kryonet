package com.server.db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.server.Config;

public class Database {

    private MongoClient mongoClient = new MongoClient(Config.HOST, Config.DATABASE_PROT);
    protected static MongoDatabase database;

    public Database(){
        database = mongoClient.getDatabase("aoe");
    }

}
