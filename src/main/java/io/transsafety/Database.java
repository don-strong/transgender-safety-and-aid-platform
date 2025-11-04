
/**
 * Implemented by: Aaron
 * 
 * built-in methods of MongoDB Java Driver 
 * - getName() : gets database name
 * - listCollectionNames()
 */


package io.transsafety;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Database 
{
    private final String connectionString;
    private final String databaseName;
    private MongoDatabase database;
    private MongoClient mongoClient;

    /**
     * Constructs a new {@code MongoDB} object.
     * Establishes a connection to the MongoDB server on {@code localhost:27017}.
     * If the database {@code transsafety} does not exist, it will be created;
     * otherwise, the existing database will be opened.
     */
    public Database() 
    {
        this.connectionString = "mongodb://localhost:27017";
        this.databaseName = "transsafety";

        try 
        {
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);
            System.out.println("Connected to database: " + database.getName());
        }
        catch (Exception e)
        {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
    }

    /**
     * Returns the connected MongoDB instance.
     * 
     * @return the connected MongoDB
     */
    public MongoDatabase getDatabase() 
    {
        return database;
    }

    /**
     * Creates a new collection in the connected DB if it does 
     * not already exist.
     * 
     * @param collectionName (name of collection to create)
     */
    public void createCollection(String collectionName) {
        try {
            boolean exists = false;

            for (String name : database.listCollectionNames()) {
                if (name.equals(collectionName)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                database.createCollection(collectionName);
                System.out.println("Collection '" + collectionName + "' created successfully!");
            } else {
                System.out.println("Collection '" + collectionName + "' already exists.");
            }
        } catch (Exception e) {
            System.out.println("Failed to create collection: " + e.getMessage());
        }
    }
    
    /**
     * Closes the MongoDB client connection.
     */
    public void close()
    {
        if (mongoClient != null) 
        {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}