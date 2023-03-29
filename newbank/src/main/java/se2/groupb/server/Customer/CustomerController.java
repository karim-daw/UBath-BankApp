package se2.groupb.server.Customer;

//Presentation layer: Takes user inputs and displays system response
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
