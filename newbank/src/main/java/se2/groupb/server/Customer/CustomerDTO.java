package se2.groupb.server.customer;

public class CustomerDTO {

    private Long customerID;

    public CustomerDTO(Long customerID) {
        this.customerID = customerID;

    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long id) {
        this.customerID = id;
    }

}
