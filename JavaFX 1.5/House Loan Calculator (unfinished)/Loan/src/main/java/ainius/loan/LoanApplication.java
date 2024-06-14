package ainius.loan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoanApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoanApplication.class.getResource("loan-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Image icon = new Image("money.png");

        stage.getIcons().add(icon);
        stage.setTitle("House Loan Calculator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}