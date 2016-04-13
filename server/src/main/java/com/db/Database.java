package com.db;

import com.Config;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Database {

    protected static MongoDatabase database;
    private MongoClient mongoClient = new MongoClient(Config.HOST, Config.DATABASE_PROT);

    public Database() {
        database = mongoClient.getDatabase("aoe");
    }

}
