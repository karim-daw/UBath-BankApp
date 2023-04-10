package se2.groupb.server.Payee;

import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerService;

public class PayeeController {

    private final PayeeService payeeService;
    private final CustomerService customerService;
    private UserInput comms;

    public PayeeController(PayeeService payeeService, CustomerService customerService, UserInput comms) {
        this.payeeService = payeeService;
        this.customerService = customerService;
        this.comms = comms;
    }

    /**
     * Returns the Customer object from the Data Store
     * 
     * @param customerID
     * @return
     */
    public Payee getPayee(UUID payeeID) {
        return payeeService.getPayeeByID(payeeID);
    }

    /**
     * displays the customers payees as a list
     * 
     * @param customerDTO
     * @return
     */
    public String displayPayees(UUID customerID) {
        return customerService.displayPayees(customerID);
    }

    /**
     * @param customerID
     * @return
     */
    public String createPayee(UUID customerID) {
        String response = ""; // the system response to the user's request
        String prompt = "Add a new payee: \n";
        prompt = "Enter the payee name: \n";
        boolean duplicateName;
        String payeeName = comms.getUserString(prompt);
        // Check if the payee already exists duplicateName =
        // accountService.alreadyExists(payeeID, payeeName);
        // while (duplicateName);

        prompt = "Enter payee's account number: \n";
        String payeeAccountNumber = comms.getUserString(prompt);

        prompt = "Enter payee's BIC: \n";
        String payeeBIC = comms.getUserString(prompt);

        prompt = "Add " + payeeName + " as a new payee?\nEnter 'y' for Yes or 'n' for No: \n";
        boolean userConfirm = comms.confirm(prompt);

        if (userConfirm) {
            Customer customer = customerService.getCustomerByID(customerID);
            Payee newPayee = new Payee(customer.getCustomerID(), payeeName, payeeAccountNumber,
                    payeeBIC);

            customer.addPayee(newPayee); // adds new payee to the customer

            response = "SUCCESS: The payee " + payeeName + " has been added.\nReturning to Main Menu.";
        } else {
            response = "Payee addition was cancelled.\nReturning to the Main Menu.";
        }
        return response;
    }

}
