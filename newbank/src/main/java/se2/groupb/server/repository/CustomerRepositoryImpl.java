package se2.groupb.server.repository;

import java.util.HashMap;
import java.util.UUID;

import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerDTO;
import se2.groupb.server.security.Authentication;

public class CustomerRepositoryImpl implements EntityRepository<Customer, CustomerDTO> {

    // Temp HashMap Customer Repo
    private final HashMap<String, Customer> theCustomers;

    // private final HashMap<String, Account> theAccounts;
    // Temp constructor using HashMap as Customer Repo
    public CustomerRepositoryImpl(HashMap<String, Customer> customers) {
        this.theCustomers = customers;
    }

    // getter
    public HashMap<String, Customer> getTheCustomers() {
        return theCustomers;
    }

    /**
     * Searches the Customer Data Store by CustomerID
     * 
     * @param customerID
     * @return customer from database
     */
    @Override
    public Customer findByID(UUID customerID) {
        return theCustomers.get(customerID.toString());
    }

    /**
     * Searches the Customer Data Store by Username & Password
     * 
     * @param customerDto
     * @return customer from database
     */
    @Override
    public Customer findByDTO(CustomerDTO customerDto) {

        // unhash password to check
        Customer customer = null;
        String username = customerDto.getUsername();
        String plainTextPassword = customerDto.getPassword();

        // get user name and hash password and compare to password in database
        for (HashMap.Entry<String, Customer> cust : theCustomers.entrySet()) {
            String cust_username = cust.getValue().getUsername();
            String hashedPassword = cust.getValue().getPassword();

            // boolean passwordIsCorrect =
            // Authentication.authenticatePassword(plainTextPassword, hashedPassword);
            // boolean passwordIsCorrect = (hashedPassword.equals(plainTextPassword));

            // Authenticate
            boolean passwordIsCorrect = Authentication.authenticatePassword(plainTextPassword, hashedPassword);
            boolean usernameIsCorrect = (cust_username.equals(username));
            if (usernameIsCorrect && passwordIsCorrect) {
                cust.getValue().setloggedInStatus(true);
                customer = cust.getValue();
                break;
            }

        }
        return customer;
    }

    /**
     * Searches the Customer Data Store by Username & Password
     * 
     * @param customerDto
     * @return customer from database, if not found returns null
     */
    @Override
    public Customer findByName(String target_username) {
        Customer customer = null;

        for (HashMap.Entry<String, Customer> cust : theCustomers.entrySet()) {
            String cust_username = cust.getValue().getUsername();
            if ((cust_username.equals(target_username))) {
                customer = cust.getValue();
                break;
            }
        }
        return customer;
    }

    /**
     * saves new customer into database
     * 
     * @param customer
     * @return true if customer is added successfully, false if customer already
     *         exists or if there is an error
     */
    @Override
    public boolean save(Customer newCustomer) {
        try {
            if (findByID(newCustomer.getCustomerID()) == null) {
                theCustomers.put(newCustomer.getCustomerID().toString(), newCustomer);
                return true;
            } else {
                return false; // customer already exists
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // error occurred
        }
    }

    /**
     * updates the customer domain model
     * 
     * @param customer
     * @return true if customer is updated successfully, false if customer is not
     *         found or if there is an error
     */
    public boolean update(Customer newCustomer) {
        try {
            if (findByID(newCustomer.getCustomerID()) != null) {
                theCustomers.put(newCustomer.getCustomerID().toString(), newCustomer);
                return true; // customer updated successfully
            } else {
                return false; // customer not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // error occurred
        }
    }

    /**
     * Searches if the Username is already taken
     * 
     * @param username
     * @return
     */
    public boolean duplicateUsername(String username) {
        for (HashMap.Entry<String, Customer> cust : theCustomers.entrySet()) {
            String cust_username = cust.getValue().getUsername();
            if (cust_username.equals(username)) {
                return true;
            }
        }
        return false;
    }

}
