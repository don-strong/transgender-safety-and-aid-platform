package io.transsafety;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {

    @FXML private Label statusLabel;
    @FXML private TextField collectionInput;
    @FXML private Button connectButton;
    @FXML private Button createCollectionButton;

    private Database db;

    @FXML
    private void initialize() {
        statusLabel.setText("Ready to connect to MongoDB...");
        createCollectionButton.setDisable(true);
    }

    @FXML
    private void connectToDatabase() {
        try {
            db = new Database();
            statusLabel.setText("Connected to database: " + db.getDatabase().getName());
            createCollectionButton.setDisable(false);
        } catch (Exception e) {
            statusLabel.setText("Failed to connect: " + e.getMessage());
        }
    }

    @FXML
    private void createCollection() {
        if (db == null) {
            statusLabel.setText("Not connected to database!");
            return;
        }

        String name = collectionInput.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setText("Please enter a collection name.");
            return;
        }

        db.createCollection(name);
        statusLabel.setText("Checked/created collection: " + name);
    }
}