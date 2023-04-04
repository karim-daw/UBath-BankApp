package se2.groupb.server.customer;

import java.util.List;
import java.util.UUID;

import se2.groupb.server.account.Account;
import se2.groupb.server.repository.CustomerRepositoryImpl;

public class CustomerServiceImpl implements CustomerService {

    // fields
    private final CustomerRepositoryImpl customerRepository;

    // Constructor
    public CustomerServiceImpl(CustomerRepositoryImpl customerRepository) {
        this.customerRepository = customerRepository;
    }

    // methods

    /**
     * Returns the Customer object corresponding to the CustomerID provided.
     * 
     * @param customerID The ID of the customer to retrieve.
     * @return The customer with the specified ID.
     * @throws IllegalArgumentException if customerID is null or not found in the
     *                                  repository.
     */
    @Override
    public Customer getCustomerByID(UUID customerID) {
        if (customerID == null) {
            throw new IllegalArgumentException("Customer ID cannot be null.");
        }
        Customer customer = customerRepository.findByID(customerID);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found for ID " + customerID.toString() + ".");
        }
        return customer;
    }

    /**
     * Returns the Customer object corresponding to the provided DTO (username &
     * password).
     * 
     * @param customerDto The DTO of the customer to retrieve.
     * @return The customer with the specified DTO.
     * @throws IllegalArgumentException if customerDto is null or not found in the
     *                                  repository.
     */
    @Override
    public Customer getCustomerbyDTO(CustomerDTO customerDto) {
        if (customerDto == null) {
            throw new IllegalArgumentException("Customer DTO cannot be null.");
        }
        Customer customer = customerRepository.findByDTO(customerDto);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found for DTO " + customerDto.toString() + ".");
        }
        return customer;
    }

    /**
     * Returns the Customer object corresponding to the provided username.
     * 
     * @param customerUsername The username of the customer to retrieve.
     * @return The customer with the specified username.
     * @throws IllegalArgumentException if customerUsername is null or not found in
     *                                  the repository.
     */
    @Override
    public Customer getCustomerbyName(String customerUsername) {
        if (customerUsername == null) {
            throw new IllegalArgumentException("Customer username cannot be null.");
        }
        Customer customer = customerRepository.findByName(customerUsername);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found for username " + customerUsername + ".");
        }
        return customer;
    }

    /**
     * Returns true if a customer with the provided username already exists in the
     * repository.
     * 
     * @param username The username to check for duplicates.
     * @return true if the username is a duplicate, false otherwise.
     * @throws IllegalArgumentException if username is null.
     */
    @Override
    public boolean duplicateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null.");
        }
        return customerRepository.duplicateUsername(username);
    }

    /**
     * Returns true if a new customer has been added to the Customer Data Store
     * 
     * @param customerDto
     * @return boolean
     */
    public boolean addNewCustomer(CustomerDTO customerDto) {
        // Check if the provided customer DTO is null
        if (customerDto == null) {
            throw new IllegalArgumentException("CustomerDTO cannot be null");
        }

        // Check if the provided username is null or empty
        String username = customerDto.getUsername();
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        // Check if the provided password is null or empty
        String password = customerDto.getPassword();
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Check if a customer with the same username already exists in the repository
        if (customerRepository.findByDTO(customerDto) != null) {
            throw new IllegalArgumentException("A customer with the same username already exists");
        }

        // Create a new customer object from the provided DTO
        Customer newCustomer = new Customer(customerDto);

        // Save the new customer in the repository and return true if successful
        if (customerRepository.save(newCustomer)) {
            return true;
        }

        // If saving the new customer was not successful, throw an exception with an
        // informative error message
        throw new RuntimeException("Failed to add a new customer to the repository");
    }

    /**
     * Displays accounts as a list.
     * 
     * @param customerID ID of the customer
     * @return A string representing the accounts
     */
    @Override
    public String displayAccounts(UUID customerID) {
        Customer customer = customerRepository.findByID(customerID);
        if (customer == null) {
            return "ERROR: Customer does not exist.";
        }
        List<Account> accounts = customer.getAccounts();
        if (accounts.isEmpty()) {
            return "You have no accounts to display.";
        } else {
            return customer.accountsToString();
        }
    }

    /**
     * Logs the customer out.
     * 
     * @param customerID ID of the customer
     */
    @Override
    public void userLogout(UUID customerID) {
        Customer customer = customerRepository.findByID(customerID);
        if (customer == null) {
            throw new IllegalArgumentException("ERROR: Customer does not exist during logout.");
        }
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
    public String changePassword(UUID customerID, String[] requestInputs) {
        // check if the command is correct
        // return infinite loop of null, why ?
        int inputLength = requestInputs.length;
        if (inputLength < 4) {
            throw new IllegalArgumentException(
                    "FAIL. Please enter your old password and twice your new password after the command.");
        }

        String oldPassword = requestInputs[1];
        String newPassword = requestInputs[2];
        String confirmNewPassword = requestInputs[3];

        // customerRepository.findByCustomerID(customerDTO.getCustomerID());
        Customer customer = getCustomerByID(customerID);

        if (!customer.getPassword().equalsIgnoreCase(oldPassword)) {
            throw new IllegalArgumentException("FAIL. The old password is incorrect.");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("FAIL. Password confirmation does not match.");
        }

        customer.setPassword(newPassword);
        return "SUCCESS new password is: " + customer.getPassword();
    }

}
