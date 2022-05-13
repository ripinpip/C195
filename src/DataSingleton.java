import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DataSingleton {

    static ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();
    static ObservableList<Customer> customerData = FXCollections.observableArrayList();
    static int appointmentIdCounter = 0;
    static int customerIdCounter = 0;
    static String currentUser;
    static int currentUserId;

    /**
     *
     * Increases the customerIdCounter when called
     */
    public static int autoGenCustomerId() {
        customerIdCounter++;
        return customerIdCounter;
    }

    /**
     *
     * Increases the appointmentIdCounter when called
     */
    public static int autoGenAppointmentId() {
        appointmentIdCounter++;
        return appointmentIdCounter;
    }

    /**
     * @param inputCurrentUser
     * Sets the Current User using the application
     */
    public static void setCurrentUser(String inputCurrentUser) {
        currentUser = inputCurrentUser;
    }

    /**
     * @return currentUser
     * Returns the Current User using the application
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * @param userName
     * Sets the current User's id
     */
    public static void setCurrentUserId(String userName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT User_ID FROM Users WHERE User_Name = ?");
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            currentUserId = resultSet.getInt("User_ID");
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    /**
     * @return currentUserId
     * Returns the current User's Id
     */
    public static int getCurrentUserId() {
        return currentUserId;
    }

    /**
     *
     * Gets any upcoming appointments within 15 minutes that matches the currents User's Id
     */
    public static Appointment getUpcomingAppointment () throws ParseException {

        for(Appointment appointment : appointmentData) {
            if(appointment.getUserId() == currentUserId) {
                Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(appointment.getStart());
                String currentDateString = convertDateToUTCOneInput(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date.from(Instant.now())));
                Date currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(currentDateString);
                String fiftyMinutesBeforeStartString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date.from(startDate.toInstant().minus(15, ChronoUnit.MINUTES)));
                Date fiftyMinutesBeforeStart = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fiftyMinutesBeforeStartString);
                if (currentDate.after(fiftyMinutesBeforeStart) && currentDate.before(startDate)) {
                    return appointment;
                }
            }
            else if (appointment == null) {
                continue;
            }
        }
        return null;
    }

    /**
     *
     * gets all appointment data from the database and stores it in appointmentdata
     */
    public static void refreshAppointmentData() {
        ObservableList<Appointment> refreshedAppointmentData = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, " +
                    "Last_Updated_By, Customer_ID, User_ID, Contact_ID FROM appointments");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                String start = resultSet.getString("Start");
                String end = resultSet.getString("End");
                String createDate = resultSet.getString("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                String lastUpdate = resultSet.getString("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int customerId = resultSet.getInt("Customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");
                String contact = "";

                try {
                    PreparedStatement statement2 = con.prepareStatement("SELECT Contact_Name FROM Contact WHERE Contact_ID = ?");
                    statement2.setInt(1, contactId);
                    ResultSet resultSet2 = statement2.executeQuery();
                    resultSet2.next();
                    contact = resultSet2.getString("Contact_Name");
                } catch (Exception exception) {
                    System.out.println(exception);
                }

                refreshedAppointmentData.add(new Appointment(appointmentId, title, description, location, contact, type, start, end, customerId, userId, createDate, createdBy, lastUpdate, lastUpdatedBy));
                appointmentIdCounter = appointmentId;
            }
            con.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        appointmentData = refreshedAppointmentData;

    }

    /**
     *
     * gets all appointment data from the database and stores it in customerdata
     */
    public static void refreshCustomerData() {
        ObservableList<Customer> refreshedCustomerData = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID FROM customers");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                String createDate = resultSet.getString("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                String lastUpdate = resultSet.getString("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int divisionId = resultSet.getInt("Division_ID");
                String country = "";

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con2 = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/mydb",
                            "root", "Danger11!!");

                    PreparedStatement statement2 = con2.prepareStatement("SELECT Country_ID FROM First_Level_Divisions WHERE Division_ID = ?");
                    statement2.setInt(1, divisionId);
                    ResultSet resultSet2 = statement2.executeQuery();
                    resultSet2.next();
                    int countryId = resultSet2.getInt("Country_ID");

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con3 = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/mydb",
                                "root", "Danger11!!");

                        PreparedStatement statement3 = con3.prepareStatement("SELECT Country FROM Countries WHERE Country_ID = ?");
                        statement3.setInt(1, countryId);
                        ResultSet resultSet3 = statement3.executeQuery();
                        resultSet3.next();
                        country = resultSet3.getString("Country");
                        con3.close();
                    } catch (Exception exception) {
                        System.out.println(exception + "3st");
                    }

                    con2.close();

                } catch (Exception exception) {
                    System.out.println(exception + "2st");
                }

                String formattedAddress = country + " address: " + address;
                refreshedCustomerData.add(new Customer(customerId, customerName, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId, country, formattedAddress));
                customerIdCounter = customerId;

            }
            con.close();

        } catch (Exception exception) {
            System.out.println(exception + "1st");
        }
        customerData = refreshedCustomerData;
    }

    /**
     * @param customerId
     * looks up customer by customer ID from customerData
     */
    public static Customer lookupCustomer(int customerId){
        for(Customer customer : customerData) {
            if(customer == null) {
                continue;
            }
            else if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    /**
     * @param appointmentId
     * looks up appointment by appointment ID from appointmentData
     */
    public static Appointment lookupAppointment(int appointmentId){
        for(Appointment appointment : appointmentData) {
            if(appointment == null) {
                continue;
            }
            else if (appointment.getCustomerId() == appointmentId) {
                return appointment;
            }
        }
        return null;
    }

    /**
     * @return typeOptions
     * gets all type options from current appointmentData
     */
    public static ObservableList<String> getAllTypeOptions() {
        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        for(Appointment appointment : appointmentData) {
            if(appointment == null) {
                continue;
            }
            else {
                typeOptions.add(appointment.getType());
            }
        }
        return typeOptions;
    }

    /**
     * @return nameOptions
     * gets all types from current appointmentData
     */
    public static ObservableList<String> getAllCustomerNameOptions() {
        ObservableList<String> nameOptions = FXCollections.observableArrayList();
        for(Customer customer : customerData) {
            if(customer == null) {
                continue;
            }
            else if (!(nameOptions.contains(customer.getCustomerName()))) {
                nameOptions.add(customer.getCustomerName());
            }
        }
        return nameOptions;
    }

    /**
     * @reutrn contactOptions
     * gets all contacts from current appointmentData
     */
    public static ObservableList<String> getAllContactOptions() {
        ObservableList<String> contactOptions = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("SELECT Contact_Name FROM Contact");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                contactOptions.add(resultSet.getString(1));
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }

        return contactOptions;
    }

    /**
     * @reutrn appointments
     * @param contact
     * gets all appointment with contact name as input
     */
    public static ObservableList<Appointment> getAppointmentByContact(String contact) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for(Appointment appointment : appointmentData) {
            if(appointment == null) {
                continue;
            }
            else if (appointment.getContact().equals(contact)){
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    /**
     * @reutrn contactOptions
     * @param customerId
     * returns all appointments matching customerId
     */
    public static ObservableList<Appointment> getCustomerAppointmentsById(int customerId) {
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        for(Appointment appointment : appointmentData) {
            if(appointment == null) {
                continue;
            }
            else if (appointment.getCustomerId() == customerId){
                customerAppointments.add(appointment);
            }
        }
        return customerAppointments;
    }

    /**
     * @param importedStartDate
     * @param importedEndDate
     * @param customerId
     * returns true if the imported dates overlap with any other appointments with the same customerId
     */
    public static boolean checkForOverlappingAppointments(String importedStartDate, String importedEndDate, String customerId) throws ParseException {
        SimpleDateFormat importFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        importFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate1 = importFormat.parse(importedStartDate);
        Date endDate1 = importFormat.parse(importedEndDate);

        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        customerAppointments = getCustomerAppointmentsById(Integer.parseInt(customerId));

        for(Appointment appointment : customerAppointments) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date startDate2 = format.parse(appointment.getStart());
            Date endDate2 = format.parse(appointment.getEnd());
            if((startDate1.getTime() < endDate2.getTime()) && (startDate2.getTime() < endDate1.getTime())) {
                return true;
            }
            else {
                continue;
            }
        }
        return false;
    }

    /**
     * @param importedStartDate
     * @param importedEndDate
     * @param customerId
     * @param appointmentId
     * for the update appointment page controller, checks for overlapping appointments excluding the appointment being updated
     */
    public static boolean updateCheckForOverlappingAppointments(String importedStartDate, String importedEndDate, String customerId, String appointmentId) throws ParseException {
        SimpleDateFormat importFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        importFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate1 = importFormat.parse(importedStartDate);
        Date endDate1 = importFormat.parse(importedEndDate);

        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        customerAppointments = getCustomerAppointmentsById(Integer.parseInt(customerId));

        for(Appointment appointment : customerAppointments) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date startDate2 = format.parse(appointment.getStart());
            Date endDate2 = format.parse(appointment.getEnd());
            if(appointment.getAppointmentId() == Integer.parseInt(appointmentId)) {
                continue;
            }
            else if((startDate1.getTime() < endDate2.getTime()) && (startDate2.getTime() < endDate1.getTime())) {
                return true;
            }
            else {
                continue;
            }
        }
        return false;
    }

    /**
     * @return customerIds
     * gets all customerIds from current customer Data
     */
    public static ObservableList<String> getAllCustomerIds() {
        ObservableList<String> customerIds = FXCollections.observableArrayList();
        for(Customer customer : customerData) {
            if(customer == null) {
                continue;
            }
            else {
                customerIds.add(String.valueOf(customer.getCustomerId()));
            }
        }
        return customerIds;
    }

    /**
     * @return currentMonthAppointments
     * returns all appointments that are within the current month
     */
    public static ObservableList<Appointment> getCurrentMonthAppointments() {
        ObservableList<Appointment> currentMonthAppointments = FXCollections.observableArrayList();
        String currentMonth = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString().substring(5,7);
        for(Appointment appointment : appointmentData) {
            if(appointment.getStart().substring(5,7).equals(currentMonth)) {
                currentMonthAppointments.add(appointment);
            }
            else if (appointment == null){
                continue;
            }
            else {
                continue;
            }
        }
        return currentMonthAppointments;
    }

    /**
     * @return currentWeekAppointments
     * returns all appointments that are within the current week
     */
    public static ObservableList<Appointment> getCurrentWeekAppointments() {
        ObservableList<Appointment> currentWeekAppointments = FXCollections.observableArrayList();
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        String currentWeek = String.valueOf(date.get(weekFields.weekOfWeekBasedYear()));
        for(Appointment appointment : appointmentData) {
            LocalDate appointmentDate = LocalDate.parse(appointment.getStart().substring(0,10));
            String appointmentWeek = String.valueOf(appointmentDate.get(weekFields.weekOfWeekBasedYear()));
            if(appointmentWeek.equals(currentWeek)) {
                currentWeekAppointments.add(appointment);
            }
            else if (appointment == null){
                continue;
            }
            else {
                continue;
            }
        }
        return currentWeekAppointments;
    }

    /**
     * @return index of selectedCustomer
     * @param selectedCustomer
     * returns the index of the selected Customer from Customer Data
     */
    public static int getCustomerIndex(Customer selectedCustomer){
        return customerData.indexOf(selectedCustomer);
    }

    /**
     * @return index of selectedAppointment
     * @param selectedAppointment
     * returns the index of the selected appointment from appointment Data
     */
    public static int getAppointmentIndex(Appointment selectedAppointment){
        return appointmentData.indexOf(selectedAppointment);
    }

    /**
     * @param index
     * @param updatedCustomer
     * updates Customer with updatedCustomer at the input index
     */
    public static void updateCustomer(int index, Customer updatedCustomer){
        customerData.set(index, updatedCustomer);
    }

    /**
     * @param index
     * @param updatedAppointment
     * updates Appointment with updatedAppointment at the input index
     */
    public static void updateAppointment(int index, Appointment updatedAppointment){
        appointmentData.set(index, updatedAppointment);
    }

    /**
     * @param selectedCustomer
     * deletes the selected customer from database and the appointments associated with that customer's id
     */
    public static void deleteCustomer(Customer selectedCustomer) {
        Alert alert = new Alert(Alert.AlertType.NONE);

        int selectedCustomerId = selectedCustomer.getCustomerId();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("DELETE FROM Appointments WHERE Customer_ID = ?");
            statement.setInt(1, selectedCustomerId);
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

            PreparedStatement statement = con.prepareStatement("DELETE FROM Customers WHERE Customer_ID = ?");
            statement.setInt(1, selectedCustomerId);
            statement.executeUpdate();
            con.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText("Customer deleted.");
        alert.show();
        refreshCustomerData();
    }

    /**
     * @param selectedAppointment
     * deletes the selected appointment
     */
    public static void deleteAppointment(Appointment selectedAppointment) {
        int selectedAppointmentId = selectedAppointment.getAppointmentId();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root", "Danger11!!");

            PreparedStatement statement = con.prepareStatement("DELETE FROM Appointments WHERE Appointment_ID = ?");
            statement.setInt(1, selectedAppointmentId);
            statement.executeUpdate();
            con.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        refreshAppointmentData();
    }

    /**
     * @param date
     * @param time
     * converts the date input + time input to UTC from the user's local time
     */
    public static String convertDateToUTC(String date, String time) throws ParseException {
        String dateTime = date + " " + time;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date formattedDate = formatter.parse(dateTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedDate);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date times = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(times);
    }

    /**
     * @param date
     * converts the date input to UTC from the user's local time
     */
    public static String convertDateToUTCOneInput(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date formattedDate = formatter.parse(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedDate);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date times = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(times);
    }

    /**
     * @param date
     * converts the local date to UTC and includes seconds
     */
    public static String convertDateToUTCTimeStamp(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date formattedDate = formatter.parse(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedDate);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date times = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(times);
    }

    /**
     * @param importedDate
     * converts the UTC date to local time
     */
    public static String convertUTCToLocal(String importedDate) throws ParseException {
        String outputDate = null;
        Date inputDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            inputDate = simpleDateFormat.parse(importedDate);
            outputDate = outputDateFormat.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    /**
     * @param importedDate
     * converts the UTC date to EST time
     */
    public static String convertUTCToEST(String importedDate) throws ParseException {
        SimpleDateFormat importFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        importFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date formattedDate = importFormat.parse(importedDate);

        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setTimeZone(tz);
        return format.format(formattedDate);
    }

}
