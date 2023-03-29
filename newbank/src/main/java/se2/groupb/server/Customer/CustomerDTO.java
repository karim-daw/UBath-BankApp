package se2.groupb.server.Customer;

import java.util.ArrayList;
import java.util.UUID;

import se2.groupb.server.Account.Account;

//Subset of Domain for data transfer between layers
public final class CustomerDTO {
	
	public final UUID customerID;
	private String username;
	private String password;
	private ArrayList<Account> accounts;
	//private ArrayList<Payee> payees;
	private boolean loggedInStatus;

	//Constructor
    public CustomerDTO(Customer customer) {
        this.customerID = customer.getCustomerID();
        this.username = customer.getUsername();

    }

    public UUID getCustomerID() {
		return this.customerID;
	}
    
}
