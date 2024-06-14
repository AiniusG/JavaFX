module ainius.loan {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens ainius.loan to javafx.fxml;
    exports ainius.loan;
}