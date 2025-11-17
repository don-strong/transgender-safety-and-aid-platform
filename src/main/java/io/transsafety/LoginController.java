/**
 * Added new methods : @aaron-alaman
 * - validateInputs()
 * - fetchUser()
 * - passwordMatches()
 */

package io.transsafety;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// added import - @aaron-alaman
import org.bson.Document;

public class LoginController 
{

    // fixed structure
    // TODO: email option 
    @FXML private TextField usernameOrEmailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    // added variable - @aaron-alaman
    private final Database db = new Database();

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

        String error = validateInputs(username, password);
        if (error != null) 
        {
            statusLabel.setText(error);
            return;
        }

        Document user = fetchUser(username);
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

        // TODO: switch to dashboard scene
    }

    /**
     * Checks if username and password fields are filled in.
     * 
     * @param username
     * @param password
     * @return
     */
    private String validateInputs(String username, String password)
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
    private Document fetchUser(String username)
    {
        List<Document> users = db.findDocuments("users", "username", username);
        return users.isEmpty() ? null : users.get(0);

        /* // for later
        users = db.findDocuments("users", "email", input);
        return users.isEmpty() ? null : users.get(0); */
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

    /**
     * 
     * @param event
     */
    @FXML
    private void onGoToRegister(ActionEvent event) 
    {
        Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
        SceneSwitcher.switchToRegisterScene(stage);
    }
}