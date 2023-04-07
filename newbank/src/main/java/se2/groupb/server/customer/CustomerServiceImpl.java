package se2.groupb.server.customer;

import java.util.UUID;

import se2.groupb.server.repository.CustomerRepositoryImpl;
import se2.groupb.server.security.Authentication;

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

    @Override
    public Customer getCustomerbyName(String customerUsername) {
        return customerRepository.findByName(customerUsername);
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

        // get user name
        String username = customerDto.getUsername();

        // extract password and encrypt it
        String plainTextPassword = customerDto.getPassword();

        // set hashed password
        String hashedPassword = Authentication.hashPassword(plainTextPassword);

        Customer newCustomer = new Customer(username, hashedPassword);
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
    public boolean updatePassword(UUID customerID, String newPassword) {

        // TODO: need to do this with hashed password
        Customer customer = getCustomerByID(customerID);
        customer.setPassword(newPassword);
        boolean success = customerRepository.update(customer);// update customer model
        if (success) {
            return true;
        }
        return false;

    }

}
