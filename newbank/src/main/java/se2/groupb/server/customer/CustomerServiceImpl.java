package se2.groupb.server.customer;

import java.util.*;
import se2.groupb.server.repository.CustomerRepositoryImpl;

public class CustomerServiceImpl implements CustomerService {

    // fields
    private final CustomerRepositoryImpl customerRepository;

    // Constructor
    public CustomerServiceImpl(CustomerRepositoryImpl customerRepository) {
        this.customerRepository = customerRepository;
    }

    // methods

    // returns the Customer object corresponding to the CustomerID provided
    /**
     * returns Customer object from DataStore with the required ID
     * 
     * @param customerID
     * @return Customer
     */
    @Override
    public Customer getCustomerByID(UUID customerID) {
        return customerRepository.findByID(customerID);
    }

    /**
     * returns Customer object from DataStore with the required DTO (username &
     * password)
     * 
     * @param customerDto
     * @return
     */
    @Override
    public Customer getCustomerbyDTO(CustomerDTO customerDto) {
        return customerRepository.findByDTO(customerDto);
    }

    /**
     * Returns true if duplicate username found in Customer Data Store
     * 
     * @param username
     * @return boolean
     */
    @Override
    public boolean duplicateUsername(String username) {
        return customerRepository.duplicateUsername(username);
    }

    /**
     * Returns true if a new customer has been added to the Customer Data Store
     * 
     * @param customer
     * @return boolean
     */
    public boolean addNewCustomer(CustomerDTO customerDto) {
        Customer newCustomer = new Customer(customerDto);
        if (customerRepository.save(newCustomer)) {
            return true;
        }
        return false;
    }

    /**
     * displays accounts as a list
     * 
     * @param customer
     * @return
     */
    @Override
    public String displayAccounts(UUID customerID) {
        Customer customer = customerRepository.findByID(customerID);
        if (customer.accountsToList().isEmpty()) {
            return "You have no accounts to display.";
        } else {
            return customer.accountsToString();
        }
    }

    @Override
    public void userLogout(UUID customerID) {
        Customer customer = customerRepository.findByID(customerID);
        customer.setloggedInStatus(false);
        customer = null;
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
    public String changePassword(UUID customerID, String[] requestInputs) {
        // check if the command is correct
        // return infinite loop of null, why ?
        int inputLength = requestInputs.length;
        if (inputLength < 4) {
            return "FAIL. Please enter your old password and twice your new password after the command.";
        }

        String oldPassword = requestInputs[1];
        String newPassword = requestInputs[2];
        String confirmNewPassword = requestInputs[3];

        // customerRepository.findByCustomerID(customerDTO.getCustomerID());
        Customer customer = getCustomerByID(customerID);

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

}
