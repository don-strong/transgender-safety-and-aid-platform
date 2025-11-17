/**
 * Added new methods : @aaron-alaman
 * - validateFields()
 * - usernameExists()
 * - emailExists() : NOT YET FUNCTIONAL UNTIL PROPERLY ADDED TO JAVAFX
 * - createUser()
 * - handleCancel()
 */

package io.transsafety;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// added import - @aaron-alaman
import org.bson.Document;


public class RegisterController 
{
    // fixed structure
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField; // for later use
    @FXML private TextField confirmEmailField; // for later use 
    @FXML private Label errorLabel;

    // added variable - @aaron-alaman
    private final Database db = new Database();

    // TODO: add email functionality, there was no code other than username

    /**
     * Handles the user registration process: validates input, checks if 
     * the username exists, creates a new user if all checks pass, and 
     * displays success or error messages.
     */
    @FXML
    private void handleRegister() 
    {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText().trim(); // for later use
        String confirmEmail = confirmEmailField.getText().trim(); // for later use 

        String error = validateFields(username, password, confirmPassword, email, confirmEmail);
        if (error != null) 
        {
            errorLabel.setText(error);
            return;
        }

        if (usernameExists(username))
        {
            errorLabel.setText("Username already exists.");
            return;
        }

        // checks for existing email 
        if (emailExists(email)) // for later use
        {
            errorLabel.setText("Email already exists.");
            return;
        }

        createUser(username, password, email);

        errorLabel.setText("Registration successful!");
    }
    
    /**
     * Primary method to handle all cases.
     * - Checks if all fields required are not empty
     * - Password check for specific range of 3-20 characters
     * - Checks if 'password' & 'confirm password' matches
     * - Checks if password length is at least 6+
     * 
     * Otherwise, returns null -- indicating there are no errors.
     * 
     * @param username
     * @param password
     * @param confirmPassword
     * @param email
     * @param confirmEmail
     */
    private String validateFields(String username, String password, String confirmPassword, String email, String confirmEmail)
    {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) 
        {
            return "All fields are required.";
        }

        if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) 
        {
            return "Username must be 3–20 characters long and can only contain letters, numbers, and underscores.";
        }

        if (!password.equals(confirmPassword)) 
        {
            return "Passwords do not match.";
        }

        if (password.length() < 6) 
        {
            return "Password must be at least 6 characters.";
        }

        if (email.isEmpty() || confirmEmail.isEmpty()) 
        {
            return "Email fields cannot be empty.";
        }  

        if (!email.equals(confirmEmail)) 
        {
            return "Emails do not match.";
        }

        // email format check, requires at least one "@" and "."
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) 
        {
            return "Invalid email format.";
        }   

        return null; 
    }

    /**
     * Checks if username exists.
     * Uses findDocuments() from Database.java
     * 
     * @param username
     * @return
     */
    private boolean usernameExists(String username) 
    {
        List<Document> results = db.findDocuments("users", "username", username);
        return !results.isEmpty();
    }


    /**
     * Checks if an email already exists in the database.
     * Uses findDocuments() from Database.java
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    private boolean emailExists(String email) 
    {
        List<Document> results = db.findDocuments("users", "email", email);
        return !results.isEmpty();
    } // code for later

    /**
     * Creates a new user.
     * Uses insertOneDocument() from Database.java
     * 
     * @param username
     * @param password
     */
    private void createUser(String username, String password, String email)
    {
        Document newUser = new Document("username", username)
        .append("password", password)
        .append("email", email);

        db.insertOneDocument("users", newUser);
    }

    /**
     *  returns to the login screen when "Cancel" is clicked.
     */
    @FXML
    private void handleBackToLogin() 
    {
    Stage stage = (Stage) usernameField.getScene().getWindow();
    SceneSwitcher.switchToLoginScene(stage);
}
    
}
