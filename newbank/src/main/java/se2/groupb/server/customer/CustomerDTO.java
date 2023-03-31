package se2.groupb.server.customer;

import java.util.UUID;

//import java.util.*;
//import se2.groupb.server.Account.Account;


//Subset of Domain for data transfer between layers
public final class CustomerDTO {
	private String username;
	private String password;

	//Constructor
    public CustomerDTO(String username, String password) {
    private UUID customerID;
    private String username;

    public CustomerDTO(UUID customerID, String username) {
        this.customerID = customerID;
        this.username = username;
        this.password = password;

    }


    public String getUsername() {
		return this.username;
	}
    
    public String getPassword() {
		return this.password;
	}

    public UUID getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return username;
    }

}
