package se2.groupb.server.customer;

public class CustomerController {

    // fields

    private final CustomerService customerService;

    // Constructor

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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
