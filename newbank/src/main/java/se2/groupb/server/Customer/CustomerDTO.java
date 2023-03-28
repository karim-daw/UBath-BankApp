package se2.groupb.server.customer;

import java.util.UUID;

public class CustomerDTO {

    private UUID customerID;
    private String username;

    public CustomerDTO(UUID customerID, String username) {
        this.customerID = customerID;
        this.username = username;

    }

    public UUID getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return username;
    }

}
