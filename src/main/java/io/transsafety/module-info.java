module io.transsafety 
{
    requires javafx.controls;
    requires javafx.fxml;

    opens io.transsafety to javafx.fxml;
    exports io.transsafety;
}