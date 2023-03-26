package se2.groupb.server.customer;

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

}