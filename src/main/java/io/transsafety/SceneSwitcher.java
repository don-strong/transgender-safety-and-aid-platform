package io.transsafety;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {

    public static void switchTo(Stage stage, String fxmlPath, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void switchToLoginScene(Stage stage) {
        switchTo(stage, "/io/transsafety/Login.fxml", 600, 400);
    }

    public static void switchToRegisterScene(Stage stage) {
        switchTo(stage, "/io/transsafety/Register.fxml", 600, 400);
    }
}
