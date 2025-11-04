package io.transsafety;

public class DBTester 
{
    public static void main(String[] args) 
    {
        Database db = new Database();

        try 
        {
            System.out.println("Database name: " + db.getDatabase().getName());
            db.createCollection("testCollect");
            db.createCollection("testCollect"); // run again to check exists message
        } 
        finally 
        {
            db.close();
        }
    }
}
