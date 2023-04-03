package se2.groupb.server.repository;

import java.util.UUID;

import se2.groupb.server.customer.*;

// TODO: see if we can add the dummy data for the customer here

public interface CustomerRepository {

    /**
     * @param customerID
     * @return customer from database
     */
    public Customer findByCustomerID(UUID customerID);
    
    
    /**
     * 
     * @param customerDto
     * @return customer from database
     */
    public Customer findByCustomerDTO(CustomerDTO customerDto);
    
    
    /**
     * 
     * @param customerDto
     * @return
     */
    public boolean duplicateUsername(String username);
    
    
    /**
     * saves customer into database
     * 
     * @return
     */
    boolean saveNewCustomer(Customer newCustomer);

}
