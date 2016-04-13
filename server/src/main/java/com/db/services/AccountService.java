package com.db.services;

import com.db.Database;
import com.game.model.Account;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class AccountService extends Database {

    private static MongoCollection<Document> accounts = database.getCollection("accounts");

    public static void addAccount(Account account) {
        accounts.insertOne(
                new Document("email", account.getEmail())
                        .append("password", account.getPassword())
                        .append("displayName", account.getDisplayName())
        );
    }

    public static Document getAccount(String email, String password) {
        return accounts.find(new Document("email", email).append("password", password)).first();
    }
}
