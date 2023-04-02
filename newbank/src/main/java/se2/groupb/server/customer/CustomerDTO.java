package se2.groupb.server.customer;

//import java.util.*;
//import se2.groupb.server.Account.Account;

//Subset of Domain for data transfer between layers
public final class CustomerDTO {

	private String username;
	private String password;


    // Constructor
    public CustomerDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    


    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

}
