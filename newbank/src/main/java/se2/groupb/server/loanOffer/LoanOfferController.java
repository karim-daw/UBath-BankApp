package se2.groupb.server.loanOffer;

import java.math.BigDecimal;
//import java.util.ArrayList;
import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.account.*;
import se2.groupb.server.customer.*;
import se2.groupb.server.loan.*;

public class LoanOfferController {
	
	//initialise the various services required
	private final CustomerController customerController;
	private final AccountController accountController;
	private final LoanOfferServiceImpl loanOfferService;
	private UserInput comms;

	// Constructor
	public LoanOfferController(CustomerController customerController, AccountController accountController, 
			LoanOfferServiceImpl loanOfferService, UserInput comms) {
		
		this.customerController = customerController;
		this.accountController = accountController;
		this.loanOfferService = loanOfferService;
		this.comms = comms;
	}
	
	/**
	 * displays the Customer's Loan Offers as a list
	 * 
	 * @param customerID
	 * @return
	 */
	public String displayLoanOffers(UUID customerID) {
		return loanOfferService.displayLoanOffersByCustomer(customerID);
	}
	
	public String displayLoanOfferMarket() {
		return loanOfferService.displayLoanOfferMarket();
	}
	
    // create a new loan offer and add it to the market
	public String newLoanOffer(UUID customerID) {
		
		String prompt;
		String response;
		
		// 1. Get the lender's username from the Customer Service
		String lenderName = customerController.getCustomer(customerID).getUsername();
		
		// 2. Get Customer to name the offer:
		boolean duplicateName;
		String offerName;
		do {
			prompt = "Enter an offer name: ";
			offerName = comms.getUserString(prompt);
			duplicateName = loanOfferService.hasLoanOfferName(customerID, offerName);
			if (duplicateName) {
				comms.printSystemMessage("You already have an offer with that name.");
			}
		} while (duplicateName);
		
		// 3. Customer must specify a source account by its account number:
		comms.printSystemMessage(accountController.displayAccounts(customerID)); //displays the customer's accounts
		Account sourceAccount = accountController.getAccountInput(customerID, "Source");
		
		
		// 4. Customer must specify a principal amount which must not exceed the account's balance:
		BigDecimal accountBalance = sourceAccount.getBalance();
		prompt = "Principal amount must not exceed " + accountBalance.toString()+ " \nEnter an amount: \n";
		BigDecimal principalAmount = comms.getAmount(prompt,accountBalance);
		if (principalAmount.compareTo(new BigDecimal("-1"))==0){
			response = "Error in thread input. Returning to main menu.";
			return response;
		}
		
		// 5. Customer must specify an interest rate:
		prompt = "Interest rate must be a number between 0 and 100.\nEnter an interest rate: \n";
		BigDecimal interestRate = comms.getAmount(prompt,new BigDecimal("100"));
		if (interestRate.compareTo(new BigDecimal("-1"))==0){
			response = "Error in thread input. Returning to main menu.";
			return response;
		}
		
		// 6. Customer must specify a duration type as time units: days, weeks, months, years
		prompt = "Enter the loan's duration type. Choose from:\n" + comms.mapToString(Loan.loanDurations) +
				"Enter your choice: \n";
		String userInput = comms.getUserMenuChoice(prompt, Loan.loanDurations.size());
		String durationType = Loan.loanDurations.get(userInput);
		
		// 7. Customer must specify a duration number (e.g. 5)
		prompt = "Enter the loan's duration. Must be a positive integer.\n";
		Integer duration = comms.getUserIntegerInput(prompt);
		
		// 8. Customer must specify the number of installments
		prompt = "Enter the number of installments. Must be a positive integer.\n";
		Integer installmentsNumber = comms.getUserIntegerInput(prompt);
		
		// 9. Customer must specify the minimum credit score: 
		prompt = "Enter the minimum credit score. Choose from the following:\n" + comms.mapToString(Customer.creditScores)+
				"Enter your choice: \n";
		userInput = comms.getUserMenuChoice(prompt, Customer.creditScores.size());
		String creditScore = Customer.creditScores.get(userInput);
		
		// 10. Customer must confirm before proceeding:
		LoanOfferDTO loanOfferDto = new LoanOfferDTO(customerID,lenderName,offerName,sourceAccount.getAccountNumber(),
				principalAmount,interestRate,duration,durationType,installmentsNumber,creditScore);
		prompt= "Create a new loan offer with:\n" +
				loanOfferDto.toString() + 
				"\nEnter 'y' to confirm or 'n' to cancel: \n";
		
		boolean userConfirm = comms.confirm(prompt);
		if (userConfirm) {
			//Add new Loan Offer to the Loan Offer Repo (the Market):
			LoanOffer newLoanOffer = loanOfferService.addNewLoanOffer(customerID, loanOfferDto);
			Customer customer = customerController.getCustomer(customerID);
			customer.addLoanOffer(newLoanOffer);
			
			//Send system response to Client Handler
			response=  "Your loan offer has been added to the MarketPlace";
		}
		else {
			response = "Loan offer creation was cancelled.\nReturning to the Main Menu.";
		}
		return response;
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
