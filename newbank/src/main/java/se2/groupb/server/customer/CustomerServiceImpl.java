package se2.groupb.server.customer;

import java.util.*;
import se2.groupb.server.NewBank;
import se2.groupb.server.NewBankClientHandler;
import se2.groupb.server.account.Account;
import se2.groupb.server.account.AccountDTO;
import se2.groupb.server.repository.CustomerRepository;

public class CustomerServiceImpl implements CustomerService {

    /*
     * private final CustomerRepository customerRepository;
     * //Repository Constructor
     * public CustomerServiceImpl(CustomerRepository customerRepository) {
     * this.customerRepository = customerRepository;
     * }
     */

    // Temp HashMap Customer Repo
    private final HashMap<String, Customer> theCustomers;

    // Temp constructor using HashMap as Customer Repo
    public CustomerServiceImpl(HashMap<String, Customer> customers) {
        this.theCustomers = customers;
    }

    public UUID findCustomer(CustomerDTO customerDto) {
        // CustomerService checks HashMap if they have a customer with the entered
        // username & password
        // If they do then provide the UUID else return null
        String username = customerDto.getUsername();
        // System.out.println(username);
        String password = customerDto.getPassword();
        // System.out.println(password);

        UUID customerID = null;
        for (HashMap.Entry<String, Customer> item : theCustomers.entrySet()) {
            String item_username = item.getValue().getUsername();
            // System.out.println(item_username);
            String item_password = item.getValue().getPassword();
            // System.out.println(item_password);
            if ((item_username.equals(username)) && (item_password.equals(password))) {
                // System.out.println("The customer ID is: " +
                // item.getValue().getCustomerID().toString());
                item.getValue().setloggedInStatus(true);
                customerID = item.getValue().getCustomerID();
                break;
            }
        }
        return customerID;
    }

    /**
     * method that checks if customer is logged
     * 
     * @param customerID
     * @return boolean
     */
    public boolean isLoggedIn(UUID customerID) {
        Customer customer = theCustomers.get(customerID.toString());
        return customer.getloggedInStatus();
    }

    /**
     * method that changes the password
     * old password need to be enter
     * then a new password, twice
     * 
     * @param customer
     * @param requestInputs
     * @return
     */

    @Override
    public String changePassword(CustomerDTO customerDTO, String[] requestInputs) {
        // check if the command is correct
        // return infinite loop of null, why ?
        int inputLength = requestInputs.length;
        if (inputLength < 4) {
            return "FAIL. Please enter your old password and twice your new password after the command.";
        }

        String oldPassword = requestInputs[1];
        String newPassword = requestInputs[2];
        String confirmNewPassword = requestInputs[3];

        // Customer customer =
        // customerRepository.findByCustomerID(customerDTO.getCustomerID());

        UUID customerID = customerDTO.getCustomerID();
        Customer customer = theCustomers.get(customerID.toString());

        // check if the old password is correct
        if (!customer.getPassword().equals(oldPassword)) {
            return "FAIL. The old password is incorrect.";
        }

        // check if the two new password inputs match.
        if (!newPassword.equals(confirmNewPassword)) {
            return "FAIL. Password confirmation does not match.";
        }

        else {
            customer.setPassword(newPassword);
            return "SUCCESS new password is: " + customer.getPassword();
        }
    }

    /**
     * displays accounts as a list
     * 
     * @param customer
     * @return
     */

    @Override
    public String displayAccountsAsString(UUID customerID) {
        // Customer customer = this.customerRepository.findByCustomerID(customerID);
        Customer customer = theCustomers.get(customerID.toString()); // temp repo
        return customer.accountsToString();
    }

    // printing out the Customer HashMap for checking
    public String toString() {
        String s = "";
        for (HashMap.Entry<String, Customer> item : theCustomers.entrySet()) {
            s += item.getKey() + " = " + item.getValue().getUsername() + "\n";
        }
        return s;
    }
}
