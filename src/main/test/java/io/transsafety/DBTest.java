/**
 * Implemented by @aaron-alaman
 * 
 * Test class for the {@code Database} class and verifies MongoDB operations and connection functionality.
 * Contains test cases for all CRUD operations (Create, Read, Update, Delete).
 *
 *  ## Database class methods tested ##
 * - insertOneDocument() 
 * - getAllDocuments()
 * - findDocuments()
 * - updateOneDocument()
 * - deleteOneDocument()
 * - close()
 * 
 * ## Built-in MongoDB Java Driver methods used ##
 * - getName()              : Gets the name of the connected database.
 *
 * ## Methods ##
 * - testConnection()       : Tests the connection to the database.
 * - testCreateCollection() : Tests creating a new collection.
 * - testInsertDocument()   : Tests inserting a document into a collection.
 * - testListDocuments()    : Tests retrieving all documents from a collection.
 * - testFindDocuments()    : Tests finding specific documents based on field-value filters.
 * - testUpdateDocument()   : Tests updating a specific field in a document.
 * - testDeleteDocument()   : Tests deleting a document from a collection.
 * - testCloseConnection()  : Safely closes the MongoDB connection.
 */

package io.transsafety;

import org.bson.Document;
import java.util.List;

public class DBTest 
{
    private Database db;
    private final String testCollectionName = "test collection";

    // this main can go to a separate class for cleaner readability
    public static void main(String[] args)
    {
        DBTest tester = new DBTest();
        tester.runTests();
    }

    /**
     * Initializes the Database object and runs selected tests.
     */
    public void runTests()
    {
        db = new Database();

        try 
        {
            testConnection();
            testCreateCollection();
            testInsertDocument();
            testListDocuments();
            testFindDocuments();
            testUpdateDocument();
            testListDocuments(); // verify update
            testDeleteDocument();
            testListDocuments(); // verify deletion
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        finally 
        {
            testCloseConnection();
        }
    }


    public void testConnection()
    {
        System.out.println("Test: Connection");
        System.out.println("Connected to database: " + db.getDatabase().getName());
    }

    public void testCreateCollection()
    {
        System.out.println("\nTest: Create Collection");
        db.createNewCollection(testCollectionName);
    }

    public void testInsertDocument()
    {
        System.out.println("\nTest: Insert Document");
        Document doc = new Document("name", "Aaron")
                            .append("username", "aaronalam")
                            .append("email", "aaron@example.edu");
        db.insertOneDocument(testCollectionName, doc);
    }

    public void testListDocuments()
    {
        System.out.println("\nTest: List ALL Documents");
        List<Document> docs = db.getAllDocuments(testCollectionName);
        if (docs.isEmpty())
        {
            System.out.println("No documents found.");
        }
        else 
        {
            for (Document d: docs)
            {
                System.out.println(d.toJson());
            }
        }
    }

    public void testFindDocuments()
    {
        System.out.println("\nTest: Find Documents (Aaron)");
        List<Document> foundDocs = db.findDocuments(testCollectionName, "name", "Aaron");
        for (Document d : foundDocs) {
            System.out.println(d.toJson());
        }
    }

    public void testUpdateDocument()
    {
        System.out.println("\nTest: Update Document (change email)");
        db.updateOneDocument(testCollectionName, "name", "Aaron", "email", "aaronalam.example.edu");
    }

    public void testDeleteDocument()
    {
        System.out.println("\nTest: Delete Document (Aaron)");
        db.deleteOneDocument(testCollectionName, "name", "Aaron");
    }

    public void testCloseConnection() 
    {
        System.out.println("\nTest: Close Connection");
        db.close();
    }
}
