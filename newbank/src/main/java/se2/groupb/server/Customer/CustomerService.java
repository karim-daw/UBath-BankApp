package se2.groupb.server.customer;

import java.util.List;

public interface CustomerService {

    /**
     * method that changes the password
     * old password need to be enter
     * then a new password, twice
     * 
     * @param customer
     * @param requestInputs
     * @return
     */

    String changePassword(CustomerDTO customerDTO, String[] requestInputs);

    /**
     * returns a string representation in the form of a list of accounts and prints
     * them
     * 
     * @param customerDTO
     * @return
     */
    String showMyAccounts(CustomerDTO customerDTO);

}