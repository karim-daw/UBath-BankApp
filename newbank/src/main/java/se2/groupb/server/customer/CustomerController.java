package se2.groupb.server.customer;

import java.util.*;

public class CustomerController {

    // fields
    private final CustomerServiceImpl customerService;

    // Constructor
    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }
    
    public UUID checkLogInDetails(CustomerDTO customerDto) {
    	return customerService.findCustomer(customerDto);	
	}
    
    /**
     * displays the customers accounts as a list
     * 
     * @param customerDTO
     * @return
     */
    public String displayAccounts(CustomerDTO customerDTO) {
        return customerService.showMyAccounts(customerDTO);
    }

}
