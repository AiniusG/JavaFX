module ainius.stud_reg {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;


    opens ainius.stud_reg to javafx.fxml;
    exports ainius.stud_reg;
}