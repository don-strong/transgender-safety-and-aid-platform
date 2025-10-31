package io.transsafety;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application 
{
    
    public void start(Stage stage) 
    {
        Label label = new Label("Coming soon!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Transgender Safety and Aid");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) 
    {
        Application.launch(args);
    }
}