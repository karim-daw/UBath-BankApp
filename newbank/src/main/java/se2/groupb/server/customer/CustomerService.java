package se2.groupb.server.customer;

import java.util.*;

public interface CustomerService {

    /**
     * @param customerID
     * @return
     */
	Customer getCustomerByID(UUID customerID);

    /**
     * @param customerDto
     * @return
     */
	Customer getCustomerbyDTO(CustomerDTO customerDto);
	
	/**
	 * Returns true if duplicate username found in Customer Data Store
	 * @param username
	 * @return boolean
	 */
	boolean duplicateUsername(String username);
	
	/**
	 * 
	 * @param customer
	 * @return
	 */
	boolean addNewCustomer(CustomerDTO customerDto);
	
    /**
     * @param customer
     */
    public void userLogout(Customer customer);

    /**
     * returns a string representation in the form of a list of accounts and prints
     * them
     * 
     * @param customerDTO
     * @return
     */
    public String displayAccounts(Customer customer);

    /**
     * method that changes the password
     * old password need to be enter
     * then a new password, twice
     * 
     * @param customer
     * @param requestInputs
     * @return
     */

    public String changePassword(UUID customerID, String[] requestInputs);


}
