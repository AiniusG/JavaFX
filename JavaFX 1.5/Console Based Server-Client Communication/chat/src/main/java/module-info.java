module ainius.chat {
    requires javafx.controls;
    requires javafx.fxml;


    opens ainius.chat to javafx.fxml;
    exports ainius.chat;
}