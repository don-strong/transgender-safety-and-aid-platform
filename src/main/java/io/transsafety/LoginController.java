/**
 * Implemented by @sixplanet312 (Justin Brown) & @aaron-alaman
 *
 * Controller class that handles Login Page functionality.
 * Communicates with the Database class to retrieve user 
 * records and verify login credentials.
 *
 * ## Features ##
 * - Validates username/email and password fields
 * - Searches for users by both username and email
 * - Verifies stored passwords against user input
 * - Displays status messages for error and success states
 * - Navigates to Register or User Query scenes based on user actions
 * 
 * ## Database Interaction ##
 * - Uses Database.java to query the "users" collection
 * - fetchUserName() searches for matching "username" field values
 * - fetchUserEmail() searches for matching "email" field values
 * - passwordMatches() validates the provided password against the stored record
 * - Operates using List<Document> returned from MongoDB queries
 *
 * ## Methods ##
 * - onGoToRegister()       : Switches to the account registration scene
 * - onGoToUserQuery()      : Switches to the user query scene after login
 * - onLogin()              : Handles login logic, validation, and DB 
 *                            authentication
 * - validateLoginInput()   : Ensures username/email and password fields 
 *                            are filled
 * - fetchUserName()        : Retrieves a user document by username
 * - fetchUserEmail()       : Retrieves a user document by email
 * - passwordMatches()      : Verifies the entered password with stored data
 */

package io.transsafety;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

public class LoginController 
{

    // FXML frontend variables
    @FXML private TextField usernameOrEmailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    // Database.java object 'db' 
    private final Database db = new Database();

        /**
     * Switches stage when users click on "Create Account"
     * 
     * @param event
     */
    @FXML
    private void onGoToRegister(ActionEvent event) 
    {
        Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
        SceneSwitcher.switchToRegisterScene(stage);
    }


    /**
     * Switches stage when users successfully logged in.
     * 
     * @param event
     */
    @FXML
    private void onGoToUserQuery(ActionEvent event) 
    {
        Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
        SceneSwitcher.switchToUserQueryScene(stage);
    }

    /**
     * Handles the login button action: validates inputs, checks user existence,
     * verifies the password, and displays appropriate status messages.
     * 
     * @param event
     */
    @FXML
    private void onLogin(ActionEvent event) 
    {
        String username = usernameOrEmailField.getText().trim();
        String password = passwordField.getText();
        

        String error = validateLoginInput(username, password);
        if (error != null) 
        {
            statusLabel.setText(error);
            return;
        }

        Document user = fetchUserName(username);

        if (user == null) 
        {
            // if user did not input username, find email
            user = fetchUserEmail(username);
        }

        if (user == null) 
        {
            statusLabel.setText("User not found.");
            return;
        }

        if (!passwordMatches(user, password)) 
        {
            statusLabel.setText("Incorrect password.");
            return;
        }

        statusLabel.setText("Login successful!");

        Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
        SceneSwitcher.switchToUserQueryScene(stage);

    }

    /**
     * Checks if username and password fields are filled in.
     * 
     * @param username
     * @param password
     * @return
     */
    private String validateLoginInput(String username, String password)
    {
        if (username.isEmpty() || password.isEmpty()) 
        {
            return "Please enter both username and password.";
        }
        return null;
    }

    /**
     * Fetches a user document from the database using the username.
     * Returns the user document if found, or null if not.
     * 
     * @param username
     * @return
     */
    private Document fetchUserName(String username)
    {
        List<Document> users = db.findDocuments("users", "username", username);
        return users.isEmpty() ? null : users.get(0);
    }

    private Document fetchUserEmail(String email)
    {
        List<Document> userEmail = db.findDocuments("users", "email", email);
        return userEmail.isEmpty() ? null : userEmail.get(0);
    }

    /**
     * Verifies whether the entered password matches the stored password.
     * 
     * @param user
     * @param enteredPassword
     * @return
     */
    private boolean passwordMatches(Document user, String enteredPassword)
    {
        String storedPassword = user.getString("password");
        return storedPassword.equals(enteredPassword);
    }

}