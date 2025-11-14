package io.transsafety;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("TransSafety");
        SceneSwitcher.switchToLoginScene(stage);  // Load Login.fxml
    }

    public static void main(String[] args) {
        launch(args);
    }
}