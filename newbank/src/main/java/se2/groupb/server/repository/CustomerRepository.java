package se2.groupb.server.repository;

import java.util.UUID;

import se2.groupb.server.customer.Customer;

public interface CustomerRepository {

    /**
     * @param customerID
     * @return customer from database
     */
    Customer findByCustomerID(UUID customerID);

    /**
     * saves customer into database
     * 
     * @return
     */
    Boolean saveNewCustomer();

}
