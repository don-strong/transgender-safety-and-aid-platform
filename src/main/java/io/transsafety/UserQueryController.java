package io.transsafety;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/* implemented by @Justin Brown
*
* start of user interface after login. User will run queries from here using various input fields and buttons.
* Displays two windows, one for user queries and one for displaying user reviews and ratings. Also a back button to return to login screen, 
* later the return to login screen button will trigger a logout function to clear session data.
*/


public class UserQueryController {

    @FXML
    private VBox businessWindow;

    @FXML
    public VBox reviewsWindow;

    public void initialize() {
        // Initialization code here
    }

    // Back to Login Button
    @FXML
    private void onBackToLogin() {
        Stage stage = (Stage) businessWindow.getScene().getWindow();
        SceneSwitcher.switchToLoginScene(stage);
    }

    
    
}
