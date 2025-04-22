package com.siddhu.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MCPServerMongoServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MCPServerMongoServiceClient.class);
    private final MongoClient mongoClient;

    /**
     * Initializes the MongoDB client with the given URI.
     */
    public MCPServerMongoServiceClient(@Value("${mongodb.uri}") String mongoUri) {
        logger.info("Initializing MCPServerMongoServiceClient with URI: {}", mongoUri);
        this.mongoClient = MongoClients.create(mongoUri);
    }

    /**
     * Lists all databases in MongoDB.
     */
    @Tool(description = "Provide he list of all databases in MongoDB.")
    public List<String> listDatabases() {
        logger.info("Fetching list of databases.");
        List<String> databaseNames = new ArrayList<>();
        for (Document db : mongoClient.listDatabases()) {
            databaseNames.add(db.getString("name"));
        }
        logger.info("Databases found: {}", databaseNames);
        return databaseNames;
    }

    /**
     * Lists all collections in the specified database.
     */
    @Tool(description = "Provide the list of all collections in the specified database.")
    public List<String> listCollections(String dbName) {
        logger.info("Fetching collections for database: {}", dbName);
        List<String> collectionNames = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase(dbName);
        for (String name : database.listCollectionNames()) {
            collectionNames.add(name);
        }
        logger.info("Collections found in {}: {}", dbName, collectionNames);
        return collectionNames;
    }   

    /**
     * Lists all indexes for a specific collection.
     */
    @Tool(description = "Provie the list of all the indexes for a specific collection.")
    public List<Document> listIndexes(String dbName, String collectionName) {
        logger.info("Fetching indexes for {}.{}", dbName, collectionName);
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collectionName);
        List<Document> indexes = new ArrayList<>();
        collection.listIndexes().into(indexes);
        logger.info("Indexes found: {}", indexes);
        return indexes;
    }

    /**
     * Creates a new collection in the specified database.
     */
    @Tool(description = "Create a new collection in the specified database.")
    public String createCollection(String dbName, String collectionName) {
        logger.info("Creating collection '{}' in database '{}'", collectionName, dbName);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        database.createCollection(collectionName);
        logger.info("Collection '{}' created successfully.", collectionName);
        return "Collection '" + collectionName + "' created successfully in database '" + dbName + "'.";
    }

}
