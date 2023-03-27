package se2.groupb.server.customer;

public class CustomerDTO {

    private Long customerID;
    private String username;

    public CustomerDTO(Long customerID, String username) {
        this.customerID = customerID;
        this.username = username;

    }

    public Long getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return username;
    }

}
