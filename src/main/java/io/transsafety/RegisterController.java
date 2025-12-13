/**
 * Implemented by @sixplanet312 (Justin Brown) & @aaron-alaman
 *
 * Controller class that handles Registration Page functionality.
 * Communicates with the Database class to validate and insert 
 * new user records.
 *
 * ## Features ##
 * - Validates all registration fields (username, passwords, email)
 * - Checks password length, format, and matching confirmation
 * - Validates email format and matching confirmation
 * - Inserts a new user document into the database upon successful validation
 * - Displays dynamic error or success messages
 * - Allows navigation back to the Login Page
 *
 * ## Database Interaction ##
 * - Uses Database.java to query and modify the "users" collection
 * - usernameExists() checks if a username is already stored in the DB
 * - emailExists() checks for existing entries in the "email" field
 * - createUser() inserts a new MongoDB Document with user credentials
 * - All queries are executed via db.findDocuments() and db.insertOneDocument()
 * 
 * ## Methods ##
 * - handleRegister()           : Main registration workflow
 * - validateRegisterFields()   : Validates all user inputs
 * - usernameExists()           : Checks database for existing usernames
 * - emailExists(String)        : Checks database for existing emails
 * - createUser()               : Inserts new user into MongoDB
 * - handleBackToLogin()        : Navigates back to the Login scene
 */

package io.transsafety;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;


public class RegisterController 
{
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField; 
    @FXML private TextField confirmEmailField; 
    @FXML private Label errorLabel;
    private final Database db = new Database();

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
        String email = emailField.getText().trim(); 
        String confirmEmail = confirmEmailField.getText().trim(); 

        String error = validateRegisterFields(username, password, confirmPassword, email, confirmEmail);
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
        if (emailExists(email)) 
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
    private String validateRegisterFields(String username, String password, String confirmPassword, String email, String confirmEmail)
    {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) 
        {
            return "All fields are required.";
        }

        if (!username.matches("^[a-zA-Z0-9_]{6,20}$")) 
        {
            return "Username must be 6-20 characters long and can only contain letters, numbers, and underscores.";
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
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    private boolean emailExists(String email) 
    {
        List<Document> results = db.findDocuments("users", "email", email);
        return !results.isEmpty();
    } 

    /**
     * Creates a new user.
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
     * Returns to the login screen 
     */
    @FXML
    private void handleBackToLogin() 
    {
    Stage stage = (Stage) usernameField.getScene().getWindow();
    SceneSwitcher.switchToLoginScene(stage);
}
    
}
