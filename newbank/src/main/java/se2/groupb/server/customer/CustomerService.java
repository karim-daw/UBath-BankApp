package se2.groupb.server.customer;

import java.util.*;

public interface CustomerService {

    /**
     * @param customerDto
     * @return
     */
    public UUID findCustomer(CustomerDTO customerDto);

    /**
     * @param customerID
     * @return
     */
    public boolean loginCustomer(UUID customerID);

    // public ArrayList<Account> getAccounts(CustomerDTO customerDto);

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

    /**
     * returns a string representation in the form of a list of accounts and prints
     * them
     * 
     * @param customerDTO
     * @return
     */
    public String displayAccountsAsString(UUID customerID);

    /**
     * @return
     */
    public String toString();
}