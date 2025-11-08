package io.transsafety;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application 
{
    
@Override
public void start(Stage stage) {
    try {

        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/io/transsafety/Main.fxml")
            );

        // calls the FXML and sets the scene
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Still need relevant title");
        stage.setScene(scene);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) 
    {
        launch(args);
    }
}