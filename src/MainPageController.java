import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    static int clickedOnCustomerId;
    static Customer clickedOnCustomer;

    private Stage stage;
    private Scene scene;

    @FXML TableView customerTable;
    @FXML TableColumn customerId;
    @FXML TableColumn customerName;
    @FXML TableColumn address;
    @FXML TableColumn postalCode;
    @FXML TableColumn phone;
    @FXML TableColumn createDate;
    @FXML TableColumn createdBy;
    @FXML TableColumn lastUpdate;
    @FXML TableColumn lastUpdatedBy;
    @FXML TableColumn divisionId;

    /**
     * goes to the AddCustomerPage
     */
    public void OnAddClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddCustomerPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * goes to the UpdateCustomerPage
     */
    public void OnUpdateClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UpdateCustomerPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * deletes customer from customer table and customer data
     */
    public void OnDeleteClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Customer ID: " + clickedOnCustomer.getCustomerId() + "\nCustomer deleted.");
        alert.show();

        DataSingleton.deleteCustomer(clickedOnCustomer);
        customerTable.getItems().remove(customerTable.getSelectionModel().getSelectedItem());
    }

    /**
     * goes to the ReportsPage
     */
    public void OnReportsClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ReportsPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * goes to the AddCustomerPage
     */
    public void GoToAppointments(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("AppointmentsPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customerTable.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
                clickedOnCustomerId = customer.getCustomerId();
                clickedOnCustomer = DataSingleton.lookupCustomer(clickedOnCustomerId);
            }
        });

        DataSingleton.refreshCustomerData();
        final ObservableList<Customer> customerTableView = DataSingleton.customerData;
        customerId.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<Customer,String>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<Customer,String>("formattedAddress"));
        postalCode.setCellValueFactory(new PropertyValueFactory<Customer,String>("postalCode"));
        phone.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
        createDate.setCellValueFactory(new PropertyValueFactory<Customer,String>("createDate"));
        createdBy.setCellValueFactory(new PropertyValueFactory<Customer,String>("createdBy"));
        lastUpdate.setCellValueFactory(new PropertyValueFactory<Customer,String>("lastUpdate"));
        lastUpdatedBy.setCellValueFactory(new PropertyValueFactory<Customer,String>("lastUpdatedBy"));
        divisionId.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("divisionId"));
        customerTable.setItems(customerTableView);

    }

}
