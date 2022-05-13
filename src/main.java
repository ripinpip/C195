import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.ParseException;
import java.util.Locale;
import java.sql.*;

public class main extends Application {
    @Override
    public void start(Stage stage) throws ParseException {
        //Locale.setDefault(new Locale("fr"));
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LogInPage.fxml"));
            Scene mainFormScene = new Scene(root);
            stage.setScene(mainFormScene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
