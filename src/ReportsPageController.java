import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class ReportsPageController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML ComboBox monthComboBox;
    @FXML ComboBox typeComboBox;
    @FXML Label numOfAppointmentsLabel;
    @FXML ComboBox contactComboBox;
    @FXML TableView contactScheduleTable;
    @FXML TableColumn appointmentId;
    @FXML TableColumn title;
    @FXML TableColumn type;
    @FXML TableColumn description;
    @FXML TableColumn start;
    @FXML TableColumn end;
    @FXML TableColumn customerId;
    @FXML ComboBox customerComboBox;
    @FXML Label numberOfCustomersLabel;

    /**
     * goes to the MainPage
     */
    public void BackToCustomers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * gets the total number of appointments by month and type
     */
    public void OnComboBoxChange() {
        int totalNumberOfAppointments = 0;
        if (monthComboBox.getValue() != null && typeComboBox.getValue() != null) {
            String monthSelected = monthComboBox.getValue().toString();
            String typeSelected = typeComboBox.getValue().toString();
            for(Appointment appointment : DataSingleton.appointmentData) {
                if(appointment == null) {
                    continue;
                }
                else if (appointment.getStart().substring(5,7).equals(monthSelected) && appointment.getType().equals(typeSelected)) {
                    totalNumberOfAppointments = totalNumberOfAppointments + 1;
                }
            }
            numOfAppointmentsLabel.setText(String.valueOf(totalNumberOfAppointments));
        }
        else {
            numOfAppointmentsLabel.setText(String.valueOf(totalNumberOfAppointments));
        }
    }

    /**
     * get the total number of customers with the same name
     */
    public void OnCustomerComboBoxChange() {
        int totalNumberOfCustomers = 0;
        if (customerComboBox.getValue() != null) {
            String customerSelected = customerComboBox.getValue().toString();
            for(Customer customer : DataSingleton.customerData) {
                if(customer == null) {
                    continue;
                }
                else if (customer.getCustomerName().equals(customerSelected)) {
                    totalNumberOfCustomers = totalNumberOfCustomers + 1;
                }
            }
            numberOfCustomersLabel.setText(String.valueOf(totalNumberOfCustomers));
        }
        else {
            numberOfCustomersLabel.setText(String.valueOf(totalNumberOfCustomers));
        }
    }

    /**
     * gets the schedule of the selected contact
     */
    public void OnContactComboBoxChange() {
        if (contactComboBox.getValue() != null) {
            ObservableList<Appointment> contactScheduleTableView = DataSingleton.getAppointmentByContact(contactComboBox.getValue().toString());
            appointmentId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("appointmentId"));
            title.setCellValueFactory(new PropertyValueFactory<Appointment,String>("title"));
            type.setCellValueFactory(new PropertyValueFactory<Appointment,String>("type"));
            description.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
            start.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                    if (p.getValue() != null){
                        try {
                            return new SimpleStringProperty(DataSingleton.convertUTCToLocal(p.getValue().getStart()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            });
            end.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> p) {
                    if (p.getValue() != null){
                        try {
                            return new SimpleStringProperty(DataSingleton.convertUTCToLocal(p.getValue().getEnd()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            });
            customerId.setCellValueFactory(new PropertyValueFactory<Appointment,String>("customerId"));
            contactScheduleTable.setItems(contactScheduleTableView);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> monthOptions = FXCollections.observableArrayList();
        monthOptions.addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        monthComboBox.setItems(monthOptions);
        monthComboBox.getSelectionModel().select(0);

        ObservableList<String> typeOptions;
        typeOptions = DataSingleton.getAllTypeOptions();
        typeComboBox.setItems(typeOptions);
        if (!(typeOptions==null)) {
            typeComboBox.getSelectionModel().select(0);
        }

        OnComboBoxChange();

        ObservableList<String> contactOptions;
        contactOptions = DataSingleton.getAllContactOptions();
        contactComboBox.setItems(contactOptions);
        if (contactOptions != null) {
            contactComboBox.getSelectionModel().select(0);
            OnContactComboBoxChange();
        }

        ObservableList<String> customerNameOptions;
        customerNameOptions = DataSingleton.getAllCustomerNameOptions();
        customerComboBox.setItems(customerNameOptions);
        if (contactOptions != null) {
            customerComboBox.getSelectionModel().select(0);
            OnCustomerComboBoxChange();
        }

    }
}
