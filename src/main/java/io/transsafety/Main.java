/**
 * Implemented by @sixplanet312 (Justin Brown)
 *
 * Entry point for the Trans Safety application. 
 * Initializes the JavaFX runtime and loads the initial Login scene.
 *
 * ## Responsibilities ##
 * - Sets the main application window title
 * - Initializes the first UI scene (Login Page)
 * - Launches the JavaFX application lifecycle
 *
 * ## Methods ##
 * - start(Stage)   : Configures the primary stage and loads Login.fxml
 * - main(String[]) : Launches the JavaFX application
 */

package io.transsafety;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application 
{

    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("Trans Safety");
        SceneSwitcher.switchToLoginScene(stage);  // Load Login.fxml
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}