package se2.groupb.server.repository;

import java.util.HashMap;
import java.util.UUID;

import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerDTO;
//import se2.groupb.server.customer.CustomerService;

// TODO: see if we can add the dummy data for the customer here

public class CustomerRepositoryImpl implements EntityRepository<Customer, CustomerDTO> {

    // Temp HashMap Customer Repo
    private final HashMap<String, Customer> theCustomers;

    // private final HashMap<String, Account> theAccounts;
    // Temp constructor using HashMap as Customer Repo
    public CustomerRepositoryImpl(HashMap<String, Customer> customers) {
        this.theCustomers = customers;
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
        Customer customer = null;
        String target_username = customerDto.getUsername();
        String target_password = customerDto.getPassword();

        for (HashMap.Entry<String, Customer> cust : theCustomers.entrySet()) {
            String cust_username = cust.getValue().getUsername();
            String cust_password = cust.getValue().getPassword();
            if ((cust_username.equals(target_username)) && (cust_password.equals(target_password))) {
                cust.getValue().setloggedInStatus(true);
                customer = cust.getValue();
                break;
            }
        }
        return customer;
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

    /**
     * saves new customer into database
     * 
     * @param customer
     * @return
     */
    public boolean save(Customer newCustomer) {
        if (findByID(newCustomer.getCustomerID()) == null) {
            theCustomers.put(newCustomer.getCustomerID().toString(), newCustomer);
            return true;
        }
        return false;
    }

}
