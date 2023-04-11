package se2.groupb.server.Payee;

import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerService;
import se2.groupb.server.repository.PayeeRepositoryImpl;

public class PayeeController {
    private final PayeeRepositoryImpl payeeRepository;
    private final PayeeService payeeService;
    private final CustomerService customerService;
    private UserInput comms;

    public PayeeController(PayeeRepositoryImpl payeeRepository,PayeeService payeeService, CustomerService customerService,
    		UserInput comms) {
    	this.payeeRepository = payeeRepository;
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
    public Payee createPayee(UUID customerID) {
		//Payee Constructor: Payee(UUID customerID, String payeeName, String payeeAccountNumber, String payeeBIC)
    	Customer customer = customerService.getCustomerByID(customerID);
		String prompt="";
		String payeeName = null;
		boolean duplicateName = false;
		do {
			prompt = "Add a new payee: \n";
			prompt += "Enter the payee name: \n";
			payeeName = comms.getUserString(prompt);
			duplicateName = customer.duplicatePayee(payeeName);
			if (duplicateName) {
				comms.printSystemMessage("You already have an Payee with that name.");
			}
		} while (duplicateName);
		
		prompt = "Enter payee's account number: \n";
		String payeeAccountNumber = comms.getUserString(prompt);

		prompt = "Enter payee's BIC: \n";
		String payeeBIC = comms.getUserString(prompt);

		prompt = "Add " + payeeName + " as a new payee?\nEnter 'y' for Yes or 'n' for No: \n";
		boolean userConfirm = comms.confirm(prompt);
		if (userConfirm) {
			Payee newPayee = new Payee(customer.getCustomerID(), payeeName, payeeAccountNumber,payeeBIC);
			//Add Payee to the database:
			boolean savedToDB = payeeRepository.save(newPayee);
			if (savedToDB) {
				customer.addPayee(newPayee); // adds new payee to the customer
				comms.printSystemMessage("SUCCESS: The payee " + payeeName + " has been added.\n"); 
				return newPayee;
			}
			else {
				comms.printSystemMessage("FAIL: The payee " + payeeName + " has not been added.\n");
				return null;
			}
		} else {
			comms.printSystemMessage("Payee addition was cancelled.\n");
			return null;
		}
	}

}
