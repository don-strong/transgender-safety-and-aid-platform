package io.transsafety;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameOrEmailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private void onLogin(ActionEvent event) {
        // if (passwordField.getText().isEmpty() || usernameOrEmailField.getText().isEmpty()) {
        //     statusLabel.setText("Please enter both username/email and password.");  
        //     return;
        // }
        statusLabel.setText("Login clicked! (backend not yet implemented)");
    }

    @FXML
    private void onGoToRegister(ActionEvent event) {
        Stage stage = (Stage) usernameOrEmailField.getScene().getWindow();
        SceneSwitcher.switchToRegisterScene(stage);
    }
}