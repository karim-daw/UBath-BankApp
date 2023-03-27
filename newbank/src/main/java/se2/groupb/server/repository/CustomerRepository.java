package se2.groupb.server.repository;

import se2.groupb.server.customer.Customer;

public interface CustomerRepository {

    Customer findByCustomerID(Long customerID);

    Boolean saveNewCustomer()

}
