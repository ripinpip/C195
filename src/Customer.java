public class Customer {
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;
    private String country;
    private String formattedAddress;

    /**
     * constructor for customer class
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionId, String country, String formattedAddress) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        this.country = country;
        this.formattedAddress = formattedAddress;
    }

    /**
     * @param formattedAddress
     * sets the formattedAddress
     */
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    /**
     * @param country
     * sets the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @param customerId
     * sets the customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @param customerName
     * sets the customerName
     */
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    /**
     * @param address
     * sets the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @param postalCode
     * sets the postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @param phone
     * sets the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * @param divisionId
     * sets the divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return formattedAddress
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
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
     * @return divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

}
