package se2.groupb.server.customer;

import java.util.UUID;

//import java.util.*;
//import se2.groupb.server.Account.Account;


//Subset of Domain for data transfer between layers
public final class CustomerDTO {
	private String username;
	private String password;
	private UUID customerID;

	//Constructor
    public CustomerDTO(String username, String password) {
    	this.username = username;
        this.password = password;
    }
    
    public CustomerDTO(UUID customerID) {
        this.customerID = customerID;
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
}
