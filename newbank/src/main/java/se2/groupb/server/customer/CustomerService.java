package se2.groupb.server.customer;

import java.util.*;

//import se2.groupb.server.account.Account;


    /**
     * @param customerID
     * @return
     */
    public boolean loginCustomer(UUID customerID);

    /**
     * @param customerID
     * @return
     */
    public boolean logoutCustomer(UUID customerID);

    // public ArrayList<Account> getAccounts(CustomerDTO customerDto);


public interface CustomerService {
	
	UUID userLogin(CustomerDTO customerDto);
	
	public void userLogout(UUID customerID);
    
    /**
     * returns a string representation in the form of a list of accounts and prints
     * them
     * 
     * @param customerDTO
     * @return
     */
    public String displayAccounts(UUID customerID);
    

    /**
     * method that changes the password
     * old password need to be enter
     * then a new password, twice
     * 
     * @param customer
     * @param requestInputs
     * @return
     */

    public String changePassword(CustomerDTO customerDTO, String[] requestInputs);


    public String toString();
}