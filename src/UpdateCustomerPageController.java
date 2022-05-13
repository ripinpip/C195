import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UpdateCustomerPageController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML TextField customerNameTextField;
    @FXML TextField customerAddressTextField;
    @FXML TextField customerPostalCodeTextField;
    @FXML TextField customerPhoneTextField;
    @FXML ComboBox countryComboBox;
    @FXML ComboBox firstLevelDivisionComboBox;

    ObservableList<String> divisionUS = FXCollections.observableArrayList();
    ObservableList<String> divisionUK = FXCollections.observableArrayList();
    ObservableList<String> divisionCanada = FXCollections.observableArrayList();

    Customer importedCustomer;

    /**
     * goes to the CustomerPage
     */
    public void OnCancelClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * updates current customer with the values in the text fields.
     */
    public void OnSaveClicked(ActionEvent event) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        int divisionId = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = ?");
            statement.setString(1, firstLevelDivisionComboBox.getValue().toString());
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            divisionId = resultSet.getInt(1);
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }

        int customerId = importedCustomer.getCustomerId();
        String customerName = customerNameTextField.getText();
        String address = customerAddressTextField.getText();
        String postalCode = customerPostalCodeTextField.getText();
        String phone = customerPhoneTextField.getText();
        String createDate = importedCustomer.getCreateDate();
        String createdBy = importedCustomer.getCreatedBy();
        String lastUpdate = dtf.format(now);
        String lastUpdatedBy = DataSingleton.getCurrentUser();
        String country = countryComboBox.getValue().toString();

        String formattedAddress = country + " address: " + address;

        Customer customer = new Customer(customerId, customerName, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId, country, formattedAddress);
        DataSingleton.updateCustomer(DataSingleton.getCustomerIndex(MainPageController.clickedOnCustomer), customer);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("DELETE FROM Customers WHERE Customer_ID = ?");
            statement.setInt(1, customerId);
            statement.executeUpdate();
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("INSERT INTO Customers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, customerId);
            statement.setString(2, customerName);
            statement.setString(3, address);
            statement.setString(4, postalCode);
            statement.setString(5, phone);
            statement.setString(6, createDate);
            statement.setString(7, createdBy);
            statement.setString(8, lastUpdate);
            statement.setString(9, lastUpdatedBy);
            statement.setInt(10, divisionId);
            statement.executeUpdate();
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }

        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * changes the first level division options according to the value in the country Combo Box
     */
    public void ChangeComboBoxOptions() {
        if (countryComboBox.getValue().equals("U.S")) {
            firstLevelDivisionComboBox.setItems(divisionUS);
            firstLevelDivisionComboBox.getSelectionModel().select(0);
        }

        if (countryComboBox.getValue().equals("UK")) {
            firstLevelDivisionComboBox.setItems(divisionUK);
            firstLevelDivisionComboBox.getSelectionModel().select(0);
        }

        if (countryComboBox.getValue().equals("Canada")) {
            firstLevelDivisionComboBox.setItems(divisionCanada);
            firstLevelDivisionComboBox.getSelectionModel().select(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> countryOptions = FXCollections.observableArrayList();;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement countryStatement = con.prepareStatement("SELECT Country FROM countries");
            ResultSet countryResultSet = countryStatement.executeQuery();

            while(countryResultSet.next())
                countryOptions.add(countryResultSet.getString(1));

            PreparedStatement divisionUSStatement = con.prepareStatement("SELECT Division FROM first_level_divisions WHERE Country_ID = 1");
            ResultSet divisionUSResultSet = divisionUSStatement.executeQuery();

            while(divisionUSResultSet.next())
                divisionUS.add(divisionUSResultSet.getString(1));

            PreparedStatement divisionUKStatement = con.prepareStatement("SELECT Division FROM first_level_divisions WHERE Country_ID = 2");
            ResultSet divisionUKResultSet = divisionUKStatement.executeQuery();

            while(divisionUKResultSet.next())
                divisionUK.add(divisionUKResultSet.getString(1));

            PreparedStatement divisionCanadaStatement = con.prepareStatement("SELECT Division FROM first_level_divisions WHERE Country_ID = 3");
            ResultSet divisionCanadaResultSet = divisionCanadaStatement.executeQuery();

            while(divisionCanadaResultSet.next())
                divisionCanada.add(divisionCanadaResultSet.getString(1));
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }

        countryComboBox.setItems(countryOptions);

        importedCustomer = MainPageController.clickedOnCustomer;
        customerNameTextField.setText(importedCustomer.getCustomerName());
        customerAddressTextField.setText(importedCustomer.getAddress());
        customerPostalCodeTextField.setText(importedCustomer.getPostalCode());
        customerPhoneTextField.setText(importedCustomer.getPhone());
        countryComboBox.getSelectionModel().select(importedCustomer.getCountry());

        String firstLevelDivision = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT Division FROM first_level_divisions WHERE Division_ID = ?");
            statement.setString(1, String.valueOf(importedCustomer.getDivisionId()));
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            firstLevelDivision = resultSet.getString(1);
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }


        if (countryComboBox.getValue().equals("U.S")) {
            firstLevelDivisionComboBox.setItems(divisionUS);
            firstLevelDivisionComboBox.getSelectionModel().select(firstLevelDivision);
        }

        if (countryComboBox.getValue().equals("UK")) {
            firstLevelDivisionComboBox.setItems(divisionUK);
            firstLevelDivisionComboBox.getSelectionModel().select(firstLevelDivision);
        }

        if (countryComboBox.getValue().equals("Canada")) {
            firstLevelDivisionComboBox.setItems(divisionCanada);
            firstLevelDivisionComboBox.getSelectionModel().select(firstLevelDivision);
        }
    }
}
