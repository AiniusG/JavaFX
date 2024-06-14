module ainius.game {
    requires javafx.controls;
    requires javafx.fxml;


    opens ainius.game to javafx.fxml;
    exports ainius.game;
}