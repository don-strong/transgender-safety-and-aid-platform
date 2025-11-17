/**
 * Implemented by @aaron-alaman
 * 
 * Database class for MongoDB operations.
 * Contains CRUD operations (Create (+ Insert), Read, Update, Delete) 
 * and proper close connection.
 *
 * ## Built-in MongoDB Java Driver methods used ##
 * - getName()             : Gets the name of the connected database.
 * - listCollectionNames() : Lists all collections in the database.
 * - createCollection()    : Creates a new collection in the database.
 * - into()                : Collects all documents from a query (FindIterable) 
 *                           into a Java list.
 * - eq()                  : Filters documents where a field equals a specific 
 *                           value (used in queries).
 * - set()                 : Updates a specific field in a document with a new
 *                           value (used in updates).
 *
 * ## Methods ##
 * - insertOneDocument()   : Inserts a single document into a collection.
 * - getAllDocuments()     : Retrieves all documents from a collection.
 * - findDocuments()       : Retrieves documents that match a specific 
 *                           field-value pair.
 * - updateOneDocument()   : Updates the first document that matches a filter.
 * - deleteOneDocument()   : Deletes the first document that matches a filter.
 * - close()               : Safely closes the connection whenever needed.
 *
 */

package io.transsafety;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Database 
{
    private final String connectionString;
    private final String databaseName;
    private MongoDatabase database;
    private MongoClient mongoClient;

    /**
     * - Constructs a new {@code MongoDB} Database object.
     * - Establishes a connection to the MongoDB server at 
     *   {@code localhost:27017}.
     * - Opens the database {@code transsafety}; if it does not 
     *   exist, it will be created.
     * - Ensures that the collection {@code users} exists.
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
            createNewCollection("users"); 
        }
        catch (Exception e)
        {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
    }

    /**
     * Returns the active MongoDB instance connected to the server.
     *
     * @return MongoDatabase the connected database instance
     */
    public MongoDatabase getDatabase() 
    {
        return database;
    }

    /**
     * Create - inserts a new row of information.
     * 
     * @param collectionName (for locating where to put the document)
     * @param document (the name of the document inserted)
     */

    public void insertOneDocument(String collectionName, Document document)
    {
        try 
        {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.insertOne(document);
            System.out.println("Document inserted successfully into '" + collectionName + "'");
        }
        catch (Exception e)
        {
            System.out.println("Failed to insert document: " + e.getMessage());
        }
    }

    /**
     * Create - creates a new collection. If collection exists, no new 
     * collections will be created.
     * 
     * @param collectionName (name of collection to create)
     */
    public void createNewCollection(String collectionName) 
    {
        try 
        {
            boolean exists = false;

            for (String name : database.listCollectionNames()) 
            {
                if (name.equals(collectionName)) 
                {
                    exists = true;
                    break;
                }
            }

            if (!exists) 
            {
                database.createCollection(collectionName);
                System.out.println("Collection '" + collectionName + "' created successfully!");
            } 
            else 
            {
                System.out.println("Collection '" + collectionName + "' already exists.");
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Failed to create collection: " + e.getMessage());
        }
    }

    /**
     * Read - iterates through all documents and returns a list 
     * of exisitng documents.
     * 
     * @param collectionName
     * @return
     */
    public List<Document> getAllDocuments(String collectionName)
    {
        List<Document> docs = new ArrayList<>();
        try 
        {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.find().into(docs); 
        }
        catch (Exception e) 
        {
            System.out.println("Failed to get documents: " + e.getMessage());
        }
        return docs;
    }

    public List<Document> findDocuments(String collectionName, String field, Object value)
    {
        List<Document> docs = new ArrayList<>();
        try 
        {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.find(Filters.eq(field, value)).into(docs);
        }
        catch (Exception e) 
        {
            System.out.println("Failed to find documents: " + e.getMessage());
        }
        return docs;
    }

    /**
     * Update - updates a specific document.
     * 
     * @param collectionName
     * @param field
     * @param value
     * @param updateField
     * @param newValue
     */
    public void updateOneDocument(String collectionName, String field, Object value, String updateField, Object newValue)
    {
        try 
        {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(Filters.eq(field, value), Updates.set(updateField, newValue));
            System.out.println("Document updated successfully in '" + collectionName + "'");
        }
        catch (Exception e) 
        {
            System.out.println("Failed to update document: " + e.getMessage());
        }
    }

    /**
     * Delete - deletes a specific document.
     * 
     * @param collectionName
     * @param field
     * @param value
     */
    public void deleteOneDocument(String collectionName, String field, Object value) 
    {
        try 
        {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.deleteOne(Filters.eq(field, value));
            System.out.println("Document deleted successfully from '" + collectionName + "'");
        } 
        catch (Exception e) 
        {
            System.out.println("Failed to delete document: " + e.getMessage());
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