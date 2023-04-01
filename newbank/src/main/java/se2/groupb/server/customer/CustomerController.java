package se2.groupb.server.customer;

import java.io.IOException;
import java.util.*;
import se2.groupb.server.UserInput;
//import se2.groupb.server.account.Account;

public class CustomerController {
    // fields
    private final CustomerServiceImpl customerService;
    private UserInput comms;

    // Constructor
    public CustomerController(CustomerServiceImpl customerService, UserInput comms) {
        this.customerService = customerService;
        this.comms = comms;
    }
    
 // Login for existing customers
 	public UUID userLogin() throws IOException {
 		String systemResponse = "";
 		String username = comms.getUserString("Enter Username");
 		String password = comms.getUserString("Enter Password");
 		CustomerDTO customerDto = new CustomerDTO(username, password);
 		comms.printSystemMessage("Please wait while we check your details");
 		UUID customerID = customerService.userLogin(customerDto);

 		// Validate login details
 		if (customerID == null) {
 			systemResponse = "Log In Failed. Invalid Credentials, please try again.";
 			comms.printSystemMessage(systemResponse);
 		} else {
 			systemResponse = "Log In Successful. What do you want to do?";
 			comms.printSystemMessage(systemResponse);
 		}
 		return customerID;
 	}
 	
    
    public String userLogout(UUID customerID) {
    	String systemResponse = "";
    	String prompt = "Are you sure you want to log out?";
    	boolean userConfirm = comms.confirm(prompt);
    	if (userConfirm) {
    		customerService.userLogout(customerID);
    		systemResponse = "LOGOUT SUCCESSFUL";
    	}
    	else {
    		systemResponse = "LOGOUT CANCELLED. RETURNING YOU TO THE MAIN MENU.";
    	}
    	return systemResponse;
    }
    
    /*
    public ArrayList<Account> getAccounts(UUID customerID){
    	return customerService.getAccounts(customerID);
    }
    */
    
    /**
     * displays the customers accounts as a list
     * 
     * @param customerDTO
     * @return
     */
    public String displayAccounts(UUID customerID) {
        return customerService.displayAccounts(customerID);
    }

}
