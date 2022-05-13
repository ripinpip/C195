import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;


import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.EventObject;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;

public class LogInPageController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML Text loginText;
    @FXML Label usernameLabel;
    @FXML Label passwordLabel;
    @FXML Label locationLabel;
    @FXML Label userLocationLabel;
    @FXML TextField usernameInput;
    @FXML TextField passwordInput;
    @FXML Button loginButton;

    /**
     * goes to the MainPage and checks username and password and writes to login.txt
     *
     */
    public void OnLoginButton(ActionEvent event) throws IOException {
        try {
            File myObj = new File("login.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

        } catch (IOException exception) {
            System.out.println(exception);
        }

        Locale myLocale = Locale.getDefault();
        Alert alert = new Alert(Alert.AlertType.NONE);

        //connect to database and check username and password
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT User_Name, Password FROM users WHERE User_Name = ?");
            statement.setString(1, usernameInput.getText());
            ResultSet resultSet = statement.executeQuery();

            String userNameLogin;
            String passwordLogin;
            resultSet.next();
            userNameLogin = resultSet.getString("User_Name");
            passwordLogin = resultSet.getString("Password");
            con.close();

            if (usernameInput.getText().equals(userNameLogin) && passwordInput.getText().equals(passwordLogin)){

                try {
                    FileWriter myWriter = new FileWriter("login.txt", true);
                    myWriter.write("\nUsername: " + usernameInput.getText() + " Password: " + passwordInput.getText() + " Timestamp: " +
                            DataSingleton.convertDateToUTCOneInput(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date.from(Instant.now()))) + " LOGIN SUCCESSFUL");
                    myWriter.close();
                } catch (IOException exception) {
                    System.out.println(exception);
                }

                DataSingleton.setCurrentUser(userNameLogin);
                DataSingleton.setCurrentUserId(userNameLogin);
                DataSingleton.refreshAppointmentData();
                DataSingleton.refreshCustomerData();

                Appointment upcomingAppointment = DataSingleton.getUpcomingAppointment();
                if (upcomingAppointment == null) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("No upcoming appointments.");
                    alert.show();
                }
                if (upcomingAppointment != null) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("Appointment within 15 minutes! Appointment ID: " + upcomingAppointment.getAppointmentId() + "\nDate: " + DataSingleton.convertUTCToLocal(upcomingAppointment.getStart()));
                    alert.show();
                }

                Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else {
                try {
                    FileWriter myWriter = new FileWriter("login.txt", true);
                    myWriter.write("\nUsername: " + usernameInput.getText() + " Password: " + passwordInput.getText() + " Timestamp: " +
                            DataSingleton.convertDateToUTCOneInput(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date.from(Instant.now()))) + " LOGIN UNSUCCESSFUL");
                    myWriter.close();
                } catch (IOException exception) {
                    System.out.println(exception);
                }

                if (myLocale.toString().equals("fr")) {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("L'identifiant ou le mot de passe est incorrect!");
                    alert.show();
                }
                else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Username or password are incorrect!");
                    alert.show();
                }
            }
        } catch (Exception exception) {
            try {
                FileWriter myWriter = new FileWriter("login.txt", true);
                myWriter.write("\nUsername: " + usernameInput.getText() + " Password: " + passwordInput.getText() + " Timestamp: " +
                        DataSingleton.convertDateToUTCOneInput(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date.from(Instant.now()))) + " LOGIN UNSUCCESSFUL");
                myWriter.close();
            } catch (IOException | ParseException exception2) {
                System.out.println(exception2);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Locale myLocale = Locale.getDefault();

        ZoneId zoneId = ZoneId.systemDefault();
        userLocationLabel.setText(String.valueOf(zoneId));

        //translate to french
        if (myLocale.toString().equals("fr")) {
            loginText.setText("connexion");
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("le mot de passe");
            locationLabel.setText("emplacement");
        }

    }

}
