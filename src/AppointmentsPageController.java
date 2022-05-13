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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class AppointmentsPageController implements Initializable {

    static int clickedOnAppointmentId;
    static Appointment clickedOnAppointment;

    private Stage stage;
    private Scene scene;

    @FXML TableView appointmentTable;
    @FXML TableColumn appointmentId;
    @FXML TableColumn title;
    @FXML TableColumn description;
    @FXML TableColumn aptLocation;
    @FXML TableColumn contact;
    @FXML TableColumn type;
    @FXML TableColumn start;
    @FXML TableColumn end;
    @FXML TableColumn customerId;
    @FXML TableColumn userId;
    @FXML ComboBox filterComboBox;

    /**
     * goes to the AddAppointmentPage
     */
    public void OnAddClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddAppointmentPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * goes to the UpdateAppointmentPage
     */
    public void OnUpdateClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UpdateAppointmentPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * deletes appointment from data and shows message in UI
     */
    public void OnDeleteClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Appointment ID: " + clickedOnAppointment.getAppointmentId() + " Type: " + clickedOnAppointment.getType() + "\nAppointment deleted.");
        alert.show();

        DataSingleton.deleteAppointment(clickedOnAppointment);
        appointmentTable.getItems().remove(appointmentTable.getSelectionModel().getSelectedItem());
    }

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
     * filters the table view by week, month, and all
     */
    public void FilterTableView(){
        if (filterComboBox.getValue().equals("Current Month")) {
            ObservableList<Appointment> appointmentMonthTableView = DataSingleton.getCurrentMonthAppointments();
            appointmentId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("appointmentId"));
            title.setCellValueFactory(new PropertyValueFactory<Appointment,String>("title"));
            description.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
            aptLocation.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
            contact.setCellValueFactory(new PropertyValueFactory<Appointment,String>("contact"));
            type.setCellValueFactory(new PropertyValueFactory<Appointment,String>("type"));
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
            customerId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("customerId"));
            userId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("userId"));
            appointmentTable.setItems(appointmentMonthTableView);
        }

        if (filterComboBox.getValue().equals("Current Week")) {
            ObservableList<Appointment> appointmentWeekTableView = DataSingleton.getCurrentWeekAppointments();
            appointmentId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("appointmentId"));
            title.setCellValueFactory(new PropertyValueFactory<Appointment,String>("title"));
            description.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
            aptLocation.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
            contact.setCellValueFactory(new PropertyValueFactory<Appointment,String>("contact"));
            type.setCellValueFactory(new PropertyValueFactory<Appointment,String>("type"));
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
            customerId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("customerId"));
            userId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("userId"));
            appointmentTable.setItems(appointmentWeekTableView);

        }

        if (filterComboBox.getValue().equals("All")) {
            final ObservableList<Appointment> appointmentTableView = DataSingleton.appointmentData;
            appointmentId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("appointmentId"));
            title.setCellValueFactory(new PropertyValueFactory<Appointment,String>("title"));
            description.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
            aptLocation.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
            contact.setCellValueFactory(new PropertyValueFactory<Appointment,String>("contact"));
            type.setCellValueFactory(new PropertyValueFactory<Appointment,String>("type"));
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
            customerId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("customerId"));
            userId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("userId"));
            appointmentTable.setItems(appointmentTableView);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> filterOptions = FXCollections.observableArrayList();
        filterOptions.addAll("All", "Current Month", "Current Week");

        appointmentTable.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                Appointment appointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
                clickedOnAppointmentId = appointment.getCustomerId();
                clickedOnAppointment = DataSingleton.lookupAppointment(clickedOnAppointmentId);
            }
        });

        DataSingleton.refreshAppointmentData();
        final ObservableList<Appointment> appointmentTableView = DataSingleton.appointmentData;
        appointmentId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<Appointment,String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
        aptLocation.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<Appointment,String>("contact"));
        type.setCellValueFactory(new PropertyValueFactory<Appointment,String>("type"));
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
        customerId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("customerId"));
        userId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("userId"));
        appointmentTable.setItems(appointmentTableView);

        filterComboBox.setItems(filterOptions);
        filterComboBox.getSelectionModel().select(0);
    }
}
