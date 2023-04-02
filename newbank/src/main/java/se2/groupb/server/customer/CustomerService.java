package se2.groupb.server.customer;

import java.util.*;

public interface CustomerService {

    /**
     * @param customerID
     * @return
     */
    public Customer getCustomer(UUID customerID);

    /**
     * @param customerDto
     * @return
     */
    public UUID userLogin(CustomerDTO customerDto);

    /**
     * @param customerID
     */
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

    public String changePassword(UUID customerID, String[] requestInputs);

    /**
     * @return
     */
    public String toString();

}
