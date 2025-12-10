/**
 * TODO: Add proper documentation 
 * TODO: reorganize code 
 * 
 */

/* implemented by @Justin Brown
*
* Start of user interface after login. User will run queries from here using various input fields and buttons.
* Displays two windows, one for user queries and one for displaying user reviews and ratings.
*
* Also a back button to return to login screen. Later this will trigger a logout function
* to clear session data.
*/

package io.transsafety;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

public class UserQueryController 
{

    // Database connection used to fetch business and review information
    private final Database db = new Database();

    // UI containers injected from UserQuery.fxml
    @FXML private VBox businessWindow;
    @FXML public VBox reviewsWindow;
    @FXML private ComboBox<String> businessTypeBox;
    @FXML private TextField searchNameField;
    @FXML private TextField searchCityField;
    @FXML private VBox reviewSubmitWindow;
    @FXML private TextField reviewInput;
    @FXML private ComboBox<Integer> reviewRatingBox;


    private VBox selectedBusinessBox = null;

    
    // added button functionality
    @FXML
    private void onSearchClicked() 
    {
        String name = searchNameField.getText().trim();
        String city = searchCityField.getText().trim();
        String type = businessTypeBox.getValue();

        List<Document> results = performSearch(name, city, type);
        updateBusinessWindow(results);
    }

    @FXML
    private void onClearClicked() 
    {
        searchNameField.clear();
        searchCityField.clear();
        businessTypeBox.getSelectionModel().clearSelection();
        businessTypeBox.setValue("Any");  

        loadBusinessData(); 
    }

    // took inspiration from the code you provided and wrote accessible code
    // for when a user types in business name that results will show 
    // without it having to be too specific - aaron

    /**
     * 
     * @param field
     * @param value
     * @return
     */
    private Bson containsIgnoreCase(String field, String value) 
    {
        if (value == null || value.isBlank()) return null;

        return Filters.regex(field, ".*" + Pattern.quote(value.trim()) + ".*", "i");
    }

    // added helper method
    /**
     * 
     * @param name
     * @param city
     * @param type
     * @return
     */
    private List<Document> performSearch(String name, String city, String type) 
    {

        MongoCollection<Document> coll = db.getCollection("businesses");
        List<Bson> filters = new ArrayList<>();

        Bson nameFilter = containsIgnoreCase("name", name);
        if (nameFilter != null) 
        {
            filters.add(nameFilter);
        }

        Bson cityFilter = containsIgnoreCase("location", city);
        if (cityFilter != null) 
        {
            filters.add(cityFilter);
        }

        if (type != null && !"Any".equals(type)) 
        {
            filters.add(Filters.eq("business_type", type));
        }

        if (filters.isEmpty()) 
        {
            return coll.find().into(new ArrayList<>());
        }

        return coll.find(Filters.and(filters)).into(new ArrayList<>());
    }

    // added 
    /**
     * 
     * @param businesses
     */
    private void updateBusinessWindow(List<Document> businesses) 
    {
        businessWindow.getChildren().clear();

        if (businesses.isEmpty()) 
        {
            Label none = new Label("No results found.");
            none.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
            businessWindow.getChildren().add(none);
            return;
        }

        for (Document doc : businesses) 
        {
            businessWindow.getChildren().add(createBusinessEntry(doc));
        }
    }

    // added
    /**
     * 
     */
    private void loadBusinessTypeOptions() 
    {
        try 
        {
            MongoCollection<Document> coll = db.getCollection("businesses");
            List<String> types = coll.distinct("business_type", String.class)
                                    .into(new ArrayList<>());
            types.add(0, "Any");
            businessTypeBox.getItems().setAll(types);

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * initialize() is automatically called when the FXML is loaded.
     * It acts like a "constructor" for the scene.
     * We load business data here so the window populates immediately.
     */
    @FXML
    public void initialize() 
    {
        loadBusinessData();
        loadBusinessTypeOptions();
        // loadReviewsData();   
    }

    /**
     * Button handler to return to Login screen.
     * Later this will trigger logout.
     */
    @FXML
    private void onBackToLogin() 
    {
        Stage stage = (Stage) businessWindow.getScene().getWindow();
        SceneSwitcher.switchToLoginScene(stage);
    }

    /**
     * Loads all business-related documents from MongoDB and populates the top window.
     */
    private void loadBusinessData() 
    {

        businessWindow.getChildren().clear();

        // Fetch documents from "businesses" collection
        // (you may rename this to match your actual collection)
        List<Document> businesses = db.getAllDocuments("businesses");

        // If none found, show a simple message
        if (businesses.isEmpty()) 
        {
            Label none = new Label("No businesses found.");
            none.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
            businessWindow.getChildren().add(none);
            return;
        }

        // Create a display entry for each business
        for (Document doc : businesses) 
        {
            VBox entry = createBusinessEntry(doc);
            businessWindow.getChildren().add(entry);
        }
    }

    /**
     * Creates a formatted VBox entry for a single business.
     * Builds UI labels for each field, and adds a hyperlink for the business website.
     */
    private VBox createBusinessEntry(Document doc) 
    {

        VBox box = new VBox();
        box.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-spacing: 6;");

        box.setOnMouseClicked(e -> {
            highlightBusiness(box);
            showReviews(doc); // pass whole MongoDB document
        });

        // Extract fields from MongoDB document
        String name = doc.getString("name");
        String type = doc.getString("business_type");
        String phone = doc.getString("contact_by_phone");
        String email = doc.getString("contact_by_email");
        String website = doc.getString("link_to_website");
        String transInclusive = doc.getString("trans_inclusive");

        // Add fields to UI
        box.getChildren().add(new Label("Name: " + name));
        box.getChildren().add(new Label("Type: " + type));
        box.getChildren().add(new Label("Phone: " + phone));
        box.getChildren().add(new Label("Email: " + email));
        box.getChildren().add(new Label("Inclusive: " + transInclusive));

        // Handle website hyperlink
        if (website != null && !website.isBlank()) 
        {
            Hyperlink siteLink = new Hyperlink(website);
            siteLink.setOnAction(e -> {
                try 
                {
                    if (Desktop.isDesktopSupported()) 
                    {
                        Desktop.getDesktop().browse(new URI(website));
                    }
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                }
            });

            box.getChildren().add(new Label("Website:"));
            box.getChildren().add(siteLink);

        } 
        else 
        {
            box.getChildren().add(new Label("Website: (not provided)"));
        }

        return box;
    }

    private void highlightBusiness(VBox box) 
    {

        // clears previously selected business highlight
        if (selectedBusinessBox != null) {
            selectedBusinessBox.setStyle(
                "-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1;"
            );
        }

        // highlights currently selected business
        selectedBusinessBox = box;
        box.setStyle(
            "-fx-padding: 10; -fx-border-color: blue; -fx-border-width: 2;"
        );
    }

    private void showReviews(Document businessDoc) 
    {
        reviewsWindow.getChildren().clear();

        String businessName = businessDoc.getString("name");
        Object businessId = businessDoc.getObjectId("_id");

        Label title = new Label("Reviews for: " + businessName);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        reviewsWindow.getChildren().add(title);

        List<Document> reviews = getReviewsForBusiness(businessId);

        if (reviews.isEmpty()) 
        {
            reviewsWindow.getChildren().add(new Label("No reviews yet."));
            return;
        }

        for (Document rev : reviews) 
        {
            VBox box = new VBox();
            box.setStyle(
                "-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1; -fx-spacing: 4;"
            );

            Integer rating = rev.getInteger("rating");
            String comment = rev.getString("comment");
            String date = rev.getString("date");

            if (rating != null)
                box.getChildren().add(new Label("Rating: " + rating + " ★"));

            box.getChildren().add(new Label("Comment: " + comment));

            if (date != null)
                box.getChildren().add(new Label("Date: " + date));

            reviewsWindow.getChildren().add(box);
        }
    }


    private List<Document> getReviewsForBusiness(Object businessId) 
    {
        MongoCollection<Document> coll = db.getCollection("reviews");

        return coll.find(Filters.eq("business_id", businessId))
                .into(new ArrayList<>());
    }

    public void addReview(Object businessId, int rating, String comment) 
    {
        MongoCollection<Document> coll = db.getCollection("reviews");

        Document review = new Document("business_id", businessId)
                .append("rating", rating)
                .append("comment", comment)
                .append("date", java.time.LocalDate.now().toString());

        coll.insertOne(review);
    }


    // added review submission functionality for accompanied fxml  
    @FXML
    private void onSubmitReview() {

        // checks if business is selected
        if (selectedBusinessBox == null) {
            System.out.println("No business selected.");
            return;
        }

        // get business ID from selected business box
        String text = reviewInput.getText().trim();
        if (text.isEmpty()) {
            System.out.println("Review cannot be empty.");
            return;
        }

        // will not submit if no rating is selected
        Integer rating = reviewRatingBox.getValue();
        if (rating == null) {
            System.out.println("Please select a rating.");
            return;
        }

        // appends teh date at the time of review submission
        String date = java.time.LocalDate.now().toString();

        // logs submission to console for now (needs to be stored in DB)
        System.out.println("Review Submitted:");
        System.out.println("Text: " + text);
        System.out.println("Rating: " + rating);
        System.out.println("Date: " + date);

        // retrieve business document based on selected boxe
        reviewInput.clear();
        reviewRatingBox.getSelectionModel().clearSelection();
    }

      
}
