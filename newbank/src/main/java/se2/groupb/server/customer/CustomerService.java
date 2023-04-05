package se2.groupb.server.customer;

import java.util.*;

public interface CustomerService {

    /**
     * @param customerID
     * @return
     */
    Customer getCustomerByID(UUID customerID);

    /**
     * @param customerDTO
     * @return
     */
    Customer getCustomerbyDTO(CustomerDTO customerDTO);

    /**
     * @param customerDTO
     * @return
     */
    Customer getCustomerbyName(String customerName);

    /**
     * 
     * @param customerDTO
     * @return
     */
    boolean addNewCustomer(CustomerDTO customerDTO);

    /**
     * @param customerID
     */
    public void userLogout(UUID customerID);

    /**
     * returns a string representation in the form of a list of accounts and prints
     * them
     * 
     * @param customerID
     * @return
     */
    public String displayAccounts(UUID customerID);

    /**
     * Returns true if duplicate username found in Customer Data Store
     * 
     * @param username
     * @return boolean
     */
    public boolean duplicateUsername(String username);

    /**
     * method that changes the password
     * old password need to be enter
     * then a new password, twice
     * 
     * @param customerID
     * @param requestInputs
     * @return true or false if successful
     */

    public boolean updatePassword(UUID customerID, String newPassword);

}
