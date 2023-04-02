package se2.groupb.server.repository;

import java.util.UUID;

import se2.groupb.server.customer.Customer;

// TODO: see if we can add the dummy data for the customer here

public interface CustomerRepository {

    /**
     * @param customerID
     * @return customer from database
     */
    public Customer findByCustomerID(UUID customerID);

    /**
     * saves customer into database
     * 
     * @return
     */
    Boolean saveNewCustomer();

}
