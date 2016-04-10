package com.server.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.server.Config;

public class Database {

    protected static MongoDatabase database;
    private MongoClient mongoClient = new MongoClient(Config.HOST, Config.DATABASE_PROT);

    public Database() {
        database = mongoClient.getDatabase("aoe");
    }

}
