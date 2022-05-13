public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String start;
    private String end;
    private int customerId;
    private int userId;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;

    /**
     * Constructor for Appointment class
     */
    public Appointment(int appointmentId, String title, String description, String location, String contact, String type, String start, String end, int customerId, int userId, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy){
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @param createDate
     * sets the createDate
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @param createdBy
     * sets the createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @param lastUpdate
     * sets the lastUpdate
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @param lastUpdatedBy
     * sets the lastUpdatedBy
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @param appointmentId
     * sets the appointmentId
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @param title
     * sets the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param description
     * sets the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param location
     * sets the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @param contact
     * sets the contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @param type
     * sets the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param start
     * sets the start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * @param end
     * sets the end
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * @param customerId
     * sets the customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @param userId
     * sets the userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @return lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @return lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @return appointmentId
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @return start
     */
    public String getStart() {
        return start;
    }

    /**
     * @return end
     */
    public String getEnd() {
        return end;
    }

    /**
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return userId
     */
    public int getUserId() {
        return userId;
    }
}
