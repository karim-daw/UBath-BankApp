package se2.groupb.server.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.account.*;
import se2.groupb.server.customer.*;

public class LoanController {
	
	//initialise the various services required
	private final CustomerServiceImpl customerService;
	private final AccountController accountController;
	private final AccountServiceImpl accountService;
	private final LoanServiceImpl loanService;
	private UserInput comms;

	// Constructor
	public LoanController(AccountController accountController, CustomerServiceImpl customerService, 
			AccountServiceImpl accountService, LoanServiceImpl loanService, UserInput comms) {
		
		this.customerService = customerService;
		this.accountController = accountController;
		this.accountService = accountService;
		this.loanService = loanService;
		this.comms = comms;
	}
	

    // create a new loan offer and add it to the market
	public String newLoanOffer(UUID customerID) {
		
		String prompt;
		String response;
		
		// 1. Customer must specify a source account by its account number:
		Account sourceAccount = accountController.getAccountInput(customerID, "Source");
		
		
		// 2. Customer must specify a principal amount which must not exceed the account's balance:
		BigDecimal accountBalance = sourceAccount.getBalance();
		prompt = "Principal amount must not exceed " + accountBalance.toString()+ " \nEnter an amount: \n";
		BigDecimal principalAmount = comms.getAmount(prompt,accountBalance);
		if (principalAmount.compareTo(new BigDecimal("-1"))==0){
			response = "Error in thread input. Returning to main menu.";
			return response;
		}
		
		// 3. Customer must specify an interest rate:
		prompt = "Interest rate must be a number between 0 and 100.\nEnter an interest rate: \n";
		BigDecimal interestRate = comms.getAmount(prompt,new BigDecimal("100"));
		if (interestRate.compareTo(new BigDecimal("-1"))==0){
			response = "Error in thread input. Returning to main menu.";
			return response;
		}
		
		// 4. Customer must specify a duration type as time units: days, weeks, months, years
		prompt = "Enter the loan's duration type. Choose from:\n" + comms.mapToString(Loan.loanDurations) +
				"Enter your choice: \n";
		String userInput = comms.getUserMenuChoice(prompt, Loan.loanDurations.size());
		String durationType = Loan.loanDurations.get(userInput);
		
		// 5. Customer must specify a duration number (e.g. 5)
		prompt = "Enter the loan's duration. Must be a positive integer.\n";
		Integer duration = comms.getUserIntegerInput(prompt);
		
		// 6. Customer must specify the number of installments
		prompt = "Enter the number of installments. Must be a positive integer.\n";
		Integer installmentsNumber = comms.getUserIntegerInput(prompt);
		
		// 7. Customer must specify the minimum credit score: 
		prompt = "Enter the minimum credit score. Choose from the following:\n" + comms.mapToString(Customer.creditScores)+
				"Enter your choice: \n";
		userInput = comms.getUserMenuChoice(prompt, Customer.creditScores.size());
		String creditScore = Customer.creditScores.get(userInput);
		
		// 8. Customer must confirm before proceeding:
		prompt = "Create a new loan offer:\n" +
				"Account: " + sourceAccount.getAccountNumber() + "\n" +
				"Principal Amount: " + principalAmount + "\n" + 
				"Interest Rate: " + interestRate + "\n" +
				"Duration: " + duration + " " + durationType + "\n" +
				"Number of Installments: " + installmentsNumber + "\n" + 
				"Minimum Credit Score: " + creditScore + "\n" +
				"Enter 'y' to confirm or 'n' to cancel: \n";
		boolean userConfirm = comms.confirm(prompt);
		if (userConfirm) {
			//create a LoanDTO object = loan offer object
			LoanDTO loanDto = new LoanDTO(customerID,sourceAccount.getAccountNumber(),principalAmount,
					interestRate,duration,durationType,installmentsNumber,creditScore);
			
			
			// add it to the customer's list of loan offers:
			Customer customer = customerService.getCustomerByID(customerID);
			customer.addLoanOffer(loanDto);
			
			//add loan offer to Market:
			
			
			
			
			response=  "Your loan offer has been added to the MarketPlace";
		}
		else {
			response = "Loan offer creation was cancelled.\nReturning to the Main Menu.";
		}
		
		
		return response;
	}
		
	
	
	public Loan addNewLoan(UUID customerID, LoanDTO loanOffer) {
		
		return null;
	}
	
	public String acceptLoanOffer(UUID customerID) {
		// Customer must:
		// 1) Meet the minimum credit score
		// 2) Specify a destination account: by account number
		
		// System must:
		// 1) remove the loan offer from market
		// 2) convert loan offer to loan object and add it to borrower's and lender's loan list
		// 3) create the credit and debit transactions for the principal transfer
		// 4) generate an automated payment schedule based on the start date, the duration, the number of installments
		//    and the interest rate
		
		String response ="The loan amount has been credited to your specified account.\n" +
				"The loan repayment schedule has been generated";
		return response;
	}
}
