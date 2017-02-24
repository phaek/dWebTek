public class Customer {

    private String customerID;
    private String customerName;
    private Customer customer;

    Customer(String customerID, String customerName) {
        this.customerID = customerID;
        this.customerName = customerName;
    }


    public void setCustomerID(String id) {this.customerID = id; }
    public void setCustomerName(String name) {this.customerName = name; }
    public String getCustomerID() {return customerID; }
    public String getCustomerName() {return customerName; }
}