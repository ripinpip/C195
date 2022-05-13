import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddAppointmentPageController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML TextField titleTextField;
    @FXML TextField descriptionTextField;
    @FXML TextField locationTextField;
    @FXML TextField typeTextField;
    @FXML ComboBox contactComboBox;
    @FXML DatePicker startDatePicker;
    @FXML DatePicker endDatePicker;
    @FXML ComboBox startTimeComboBox;
    @FXML ComboBox endTimeComboBox;
    @FXML ComboBox customerIdComboBox;
    @FXML ComboBox userIdComboBox;

    /**
     * adds the appointment to the database and to appointment data
     */
    public void OnAddClicked(ActionEvent event) throws IOException, ParseException {
        Alert alert = new Alert(Alert.AlertType.NONE);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        String start = DataSingleton.convertDateToUTC(startDatePicker.getValue().toString(), startTimeComboBox.getValue().toString());
        String end = DataSingleton.convertDateToUTC(endDatePicker.getValue().toString(), endTimeComboBox.getValue().toString());
        String customerId = customerIdComboBox.getValue().toString();
        String userId = userIdComboBox.getValue().toString();
        String contact = contactComboBox.getValue().toString();
        String createDate = dtf.format(now);
        String createdBy = DataSingleton.getCurrentUser();
        String lastUpdate = dtf.format(now);
        String lastUpdatedBy = DataSingleton.getCurrentUser();

        //check if appointment is scheduled in the same day
        if (!(startDatePicker.getValue().toString().equals(endDatePicker.getValue().toString()))) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Appointment must be scheduled in the same day!");
            alert.show();
            return;
        }
        //check if start is within business hours
        if (Integer.parseInt(DataSingleton.convertUTCToEST(start).substring(11,13)) < 8 || Integer.parseInt(DataSingleton.convertUTCToEST(start).substring(11,13)) > 22) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Appointment is scheduled outside of business hours!");
            alert.show();
            return;
        }
        //check if end is within business hours
        if (Integer.parseInt(DataSingleton.convertUTCToEST(end).substring(11,13)) < 8 || Integer.parseInt(DataSingleton.convertUTCToEST(end).substring(11,13)) > 22) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Appointment is scheduled outside of business hours!");
            alert.show();
            return;
        }
        //check if time overlaps with other customer appointments
        if (DataSingleton.checkForOverlappingAppointments(start, end, customerId) == true) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Appointment overlaps with customer's previous appointment!");
            alert.show();
            return;
        }

        //get contactId
        int contactId = 1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT Contact_ID From Contact WHERE Contact_Name = ?");
            statement.setString(1, contact);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            contactId = resultSet.getInt(1);
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }

        int appointmentId = DataSingleton.autoGenAppointmentId();

        Appointment appointment = new Appointment(appointmentId, title, description, location, contact, type, start, end, Integer.parseInt(customerId),
                Integer.parseInt(userId), createDate, createdBy, lastUpdate, lastUpdatedBy);
        DataSingleton.appointmentData.add(appointment);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("INSERT INTO Appointments VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, appointmentId);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, location);
            statement.setString(5, type);
            statement.setString(6, start);
            statement.setString(7, end);
            statement.setString(8, createDate);
            statement.setString(9, createdBy);
            statement.setString(10, lastUpdate);
            statement.setString(11, lastUpdatedBy);
            statement.setInt(12, Integer.parseInt(customerId));
            statement.setInt(13, Integer.parseInt(userId));
            statement.setInt(14, contactId);
            statement.executeUpdate();

            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }

        Parent root = FXMLLoader.load(getClass().getResource("AppointmentsPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * goes to the Appointment Page
     */
    public void OnCancelClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AppointmentsPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> contactOptions = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT Contact_Name FROM contact");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next())
                contactOptions.add(resultSet.getString(1));
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }

        ObservableList<String> userOptions = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT User_ID FROM Users");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next())
                userOptions.add(resultSet.getString(1));
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }

        ObservableList<String> customerOptions = DataSingleton.getAllCustomerIds();

        ObservableList<String> timeOptions = FXCollections.observableArrayList();
        timeOptions.addAll("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");

        contactComboBox.setItems(contactOptions);
        contactComboBox.getSelectionModel().select(0);

        startTimeComboBox.setItems(timeOptions);
        startTimeComboBox.getSelectionModel().select(0);

        endTimeComboBox.setItems(timeOptions);
        endTimeComboBox.getSelectionModel().select(0);

        customerIdComboBox.setItems(customerOptions);
        if (!(customerOptions==null)) {
            customerIdComboBox.getSelectionModel().select(0);
        }

        userIdComboBox.setItems(userOptions);
        userIdComboBox.getSelectionModel().select(0);
    }
}
