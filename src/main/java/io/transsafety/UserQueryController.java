package io.transsafety;

import com.mongodb.client.MongoCollection;
import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;

/* implemented by @Justin Brown
*
* Start of user interface after login. User will run queries from here using various input fields and buttons.
* Displays two windows, one for user queries and one for displaying user reviews and ratings.
*
* Also a back button to return to login screen. Later this will trigger a logout function
* to clear session data.
*/



/**
 * Suggested some logic for the name search to make searching easier for users.
 * Currently not implemented in the UI, but could be added later.
 * 
 * 
String nameInput = searchNameField.getText().trim();

List<Bson> filters = new ArrayList<>();

// NAME SEARCH (substring, case-insensitive)
if (!nameInput.isEmpty()) {
    filters.add(Filters.regex("businessName", ".*" + Pattern.quote(nameInput) + ".*", "i"));
}

 */

public class UserQueryController {

    // Database connection used to fetch business and review information
    private final Database db = new Database();

    // UI containers injected from UserQuery.fxml
    @FXML
    private VBox businessWindow;

    @FXML
    public VBox reviewsWindow;

    @FXML
    private ComboBox<String> businessTypeBox;


private void loadBusinessTypeOptions() {
    try {
        MongoCollection<Document> coll = db.getCollection("businesses");

        List<String> types = coll.distinct("business_type", String.class)
                                 .into(new ArrayList<>());

        // Add default "Any"
        types.add(0, "Any");

        businessTypeBox.getItems().setAll(types);

    } catch (Exception e) {
        e.printStackTrace();
    }
}


/**
 * initialize() is automatically called when the FXML is loaded.
 * It acts like a "constructor" for the scene.
 * We load business data here so the window populates immediately.
 */
@FXML
public void initialize() {
        loadBusinessData();
        loadBusinessTypeOptions();
        // loadReviewsData();   
    }

    /**
     * Button handler to return to Login screen.
     * Later this will trigger logout.
     */
    @FXML
    private void onBackToLogin() {
        Stage stage = (Stage) businessWindow.getScene().getWindow();
        SceneSwitcher.switchToLoginScene(stage);
    }

    /**
     * Loads all business-related documents from MongoDB and populates the top window.
     */
    private void loadBusinessData() {

        businessWindow.getChildren().clear();

        // Fetch documents from "businesses" collection
        // (you may rename this to match your actual collection)
        List<Document> businesses = db.getAllDocuments("businesses");

        // If none found, show a simple message
        if (businesses.isEmpty()) {
            Label none = new Label("No businesses found.");
            none.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
            businessWindow.getChildren().add(none);
            return;
        }

        // Create a display entry for each business
        for (Document doc : businesses) {
            VBox entry = createBusinessEntry(doc);
            businessWindow.getChildren().add(entry);
        }
    }

    /**
     * Creates a formatted VBox entry for a single business.
     * Builds UI labels for each field, and adds a hyperlink for the business website.
     */
    private VBox createBusinessEntry(Document doc) {

        VBox box = new VBox();
        box.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-spacing: 6;");

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
        if (website != null && !website.isBlank()) {

            Hyperlink siteLink = new Hyperlink(website);

            siteLink.setOnAction(e -> {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI(website));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            box.getChildren().add(new Label("Website:"));
            box.getChildren().add(siteLink);

        } else {
            box.getChildren().add(new Label("Website: (not provided)"));
        }

        return box;
    }

      
}
