package se2.groupb.server.customer;

import java.util.UUID;

import se2.groupb.server.NewBank;
import se2.groupb.server.UserInput;

public class CustomerController {

    // fields
    private final CustomerServiceImpl customerService;
    private final NewBank bank;
    public UserInput comms;

    // Constructor
    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
        bank = NewBank.getBank();
    }

    public UUID checkLogInDetails(CustomerDTO customerDto) {
        return customerService.findCustomer(customerDto);
    }

    public boolean isLoggedIn(UUID customerID) {
        return customerService.isLoggedIn(customerID);
    }

    /*
     * public ArrayList<Account> getAccounts(UUID customerID){
     * return customerService.getAccounts(customerID);
     * }
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
