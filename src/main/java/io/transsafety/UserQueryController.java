/**
 * Implemented by @sixplanet312 (Justin Brown) & @aaron-alaman
 *
 * Controller class that manages the User Query interface shown after login.
 * Communicates with the Database class to retrieve business information, 
 * search results, and associated reviews. It dynamically builds UI components 
 * for displaying business entries and review data, and provides users with 
 * tools to search, filter, and submit reviews.
 *
 * ## Features ##
 * - Loads all business data on initialization
 * - Allows searching by name, city, and business type (case-insensitive)
 * - Displays business details including contact info, inclusivity status, 
 *   and website
 * - Dynamically loads and displays reviews for the selected business
 * - Supports submitting new reviews (console-only : rating + text)
 * - Highlights the currently selected business
 * - Provides a clear button to reset filters
 * - Allows returning to the Login page (logout placeholder)
 *
 * ## Database Interaction ##
 * - Fetches business documents from the "businesses" collection
 * - Fetches and inserts reviews in the "reviews" collection
 * - Uses regex filters for flexible name/city matching
 *
 * ## Methods ##
 * - initialize()             : Called on load; populates UI with initial data
 * - onSearchClicked()        : Performs a filtered business search
 * - onClearClicked()         : Resets search fields and reloads data
 * - containsIgnoreCase()     : Builds a case-insensitive regex filter
 * - performSearch()          : Executes MongoDB queries based on filters
 * - updateBusinessWindow()   : Refreshes the business display area
 * - loadBusinessData()       : Loads all businesses from the DB
 * - loadBusinessTypeOptions(): Loads unique business types into ComboBox
 * - createBusinessEntry()    : Builds a visual VBox entry for a business
 * - highlightBusiness()      : Visually highlights the selected business
 * - showReviews()            : Displays reviews for a selected business
 * - getReviewsForBusiness()  : Fetches review documents from DB
 * - addReview()              : Inserts a review into the database
 * - onSubmitReview()         : Handles review form submission
 * - onBackToLogin()          : Returns user to Login scene
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

    /**
     * Triggered when the Search button is clicked.
     * Collects input values, performs the business search,
     * and updates the UI with the results.
     */
    @FXML
    private void onSearchClicked() 
    {
        String name = searchNameField.getText().trim();
        String city = searchCityField.getText().trim();
        String type = businessTypeBox.getValue();

        List<Document> results = performSearch(name, city, type);
        updateBusinessWindow(results);
    }

    /**
     * Resets all search fields to default values and reloads the full
     * business list. Acts as a quick "reset search" function.
     */
    @FXML
    private void onClearClicked() 
    {
        searchNameField.clear();
        searchCityField.clear();
        businessTypeBox.getSelectionModel().clearSelection();
        businessTypeBox.setValue("Any");  

        loadBusinessData(); 
    }

    /**
     * Automatically executed when the FXML UI loads.
     * Populates the business list and loads available business types.
     */
    @FXML
    public void initialize() 
    {
        loadBusinessData();
        loadBusinessTypeOptions();
        // loadReviewsData();   
    }

    /**
     * Navigates the user back to the Login Page.
     * Placeholder for future logout/session clearing.
     */
    @FXML
    private void onBackToLogin() 
    {
        Stage stage = (Stage) businessWindow.getScene().getWindow();
        SceneSwitcher.switchToLoginScene(stage);
    }

    /**
     * Validates review input fields and prepares the review for submission.
     * Clears form input afterward. (DB save integration is handled separately.)
     */
    @FXML
    private void onSubmitReview() 
    {

        // checks if business is selected
        if (selectedBusinessBox == null) 
        {
            System.out.println("No business selected.");
            return;
        }

        // get business ID from selected business box
        String text = reviewInput.getText().trim();
        if (text.isEmpty())
        {
            System.out.println("Review cannot be empty.");
            return;
        }

        // will not submit if no rating is selected
        Integer rating = reviewRatingBox.getValue();
        if (rating == null) 
        {
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

    /**
     * Builds a case-insensitive regex filter for MongoDB.
     *
     * @param field the document field to filter on
     * @param value the text to search for
     * @return a Bson regex filter, or null if value is blank
     */
    private Bson containsIgnoreCase(String field, String value) 
    {
        if (value == null || value.isBlank()) return null;

        return Filters.regex(field, ".*" + Pattern.quote(value.trim()) + ".*", "i");
    }

    /**
     * Performs a database search for businesses matching the provided filters.
     *
     * @param name business name (partial allowed)
     * @param city business city/location (partial allowed)
     * @param type business type or category
     * @return a list of matching business documents
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

    /**
     * Clears and repopulates the business display window with the given documents.
     * Displays "No results found" when the list is empty.
     *
     * @param businesses the list of businesses to display
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

    /**
     * Loads all unique business types from the database and populates
     * the Business Type ComboBox. Includes "Any" as a default option.
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
     * Retrieves all businesses from the database and populates the main
     * business display window. Used during initialization and search resets.
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
     * Builds a styled VBox containing business information such as name,
     * type, contact details, inclusivity status, and a website link.
     *
     * @param doc the business MongoDB document
     * @return a fully formatted VBox entry
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

    /**
     * Applies a highlight border to the selected business entry, and clears
     * any previous highlight.
     *
     * @param box the VBox representing the selected business
     */
    private void highlightBusiness(VBox box) 
    {

        // clears previously selected business highlight
        if (selectedBusinessBox != null) 
        {
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

    /**
     * Loads and displays all reviews belonging to the selected business.
     *
     * @param businessDoc the MongoDB document of the selected business
     */
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

    /**
     * Retrieves all review documents linked to a specific business.
     *
     * @param businessId the unique ID of the business
     * @return list of review documents
     */
    private List<Document> getReviewsForBusiness(Object businessId) 
    {
        MongoCollection<Document> coll = db.getCollection("reviews");

        return coll.find(Filters.eq("business_id", businessId))
                .into(new ArrayList<>());
    }

    /**
     * Inserts a new review document into the database containing the business ID,
     * rating, comment, and current date.
     *
     * @param businessId ID of the business
     * @param rating     review rating (1–5)
     * @param comment    written review text
     */
    public void addReview(Object businessId, int rating, String comment) 
    {
        MongoCollection<Document> coll = db.getCollection("reviews");

        Document review = new Document("business_id", businessId)
                .append("rating", rating)
                .append("comment", comment)
                .append("date", java.time.LocalDate.now().toString());

        coll.insertOne(review);
    }
  
}
