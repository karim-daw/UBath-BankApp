package se2.groupb.server.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.account.*;
import se2.groupb.server.customer.*;

public class LoanController {
	
	//initialise the various services required
	private final CustomerService customerService;
	private final AccountController accountController;
	private final AccountService accountService;
	private final LoanServiceImpl loanService;
	private UserInput comms;

	// Constructor
	public LoanController(AccountController accountController, CustomerService customerService, 
			AccountService accountService, LoanServiceImpl loanService, UserInput comms) {
		
		this.customerService = customerService;
		this.accountController = accountController;
		this.accountService = accountService;
		this.loanService = loanService;
		this.comms = comms;
	}	
	
	public boolean addNewLoan(UUID customerID, Loan newloan) {
		return loanService.createLoan(customerID, newloan);
	}
	
	/**
	 * displays only the Customer's Loan Offers
	 * 
	 * @param customerID
	 * @return
	 */
	public String displayLoans(UUID customerID) {
		return loanService.displayLoansByCustomer(customerID);
	} 
}
