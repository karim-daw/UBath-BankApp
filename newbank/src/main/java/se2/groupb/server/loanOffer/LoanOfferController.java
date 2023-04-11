package se2.groupb.server.loanOffer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
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
	private final LoanController loanController;
	private final LoanOfferServiceImpl loanOfferService;
	private UserInput comms;

	// Constructor
	public LoanOfferController(CustomerController customerController, AccountController accountController, 
			LoanController loanController, LoanOfferServiceImpl loanOfferService, UserInput comms) {
		
		this.customerController = customerController;
		this.accountController = accountController;
		this.loanController = loanController;
		this.loanOfferService = loanOfferService;
		this.comms = comms;
	}
	
	/**
	 * String containing all loan offers
	 * 
	 * @return display string
	 */
 	public String displayLoanOfferMarket() {
 		return loanOfferService.displayLoanOfferMarket();
	}
 	
 	
	/**
	 * displays only the Customer's Loan Offers
	 * 
	 * @param customerID
	 * @return
	 */
	public String displayLoanOffers(UUID customerID) {
		return loanOfferService.displayLoanOffersByCustomer(customerID);
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
		
		// 3. Customer must specify an account number:
		comms.printSystemMessage(accountController.displayAccounts(customerID)); //displays the customer's accounts
		Account lenderAccount = accountController.getAccountInput(customerID);
		String lenderAccountNumber = lenderAccount.getAccountName();
		UUID lenderAccountID = lenderAccount.getAccountID();
		
		
		// 4. Customer must specify a principal amount which must not exceed the account's balance:
		BigDecimal accountBalance = lenderAccount.getBalance();
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
		//String creditScore = Customer.creditScores.get(userInput);
		String creditScore = comms.getUserMenuChoice(prompt, Customer.creditScores.size());
		
		
		// 10. Customer must confirm before proceeding:
		LoanOfferDTO loanOfferDto = new LoanOfferDTO(customerID,lenderName,offerName,lenderAccountID, lenderAccountNumber,
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
		
	public LoanOffer selectLoanOffer(UUID customerID) {
		int noOfChoices = loanOfferService.getAcceptableOffers(customerID).size(); //Map<String,LoanOffer> getAllLoanOffers()
		if (noOfChoices>0) {
			comms.printSystemMessage("LOAN MARKET OFFERS:");
			comms.printSystemMessage(displayLoanOfferMarket());
			String prompt = "Enter the offer's number: ";
			String userInput = comms.getUserMenuChoice(prompt, noOfChoices);
			return loanOfferService.getAllLoanOffers().get(userInput);
		}
		else {
			return null;
		}
	}
	
	
	public String acceptLoanOffer(UUID customerID) {
		// 1. Customer selects a Loan Offer:
		String response = "";
		LoanOffer offer = selectLoanOffer(customerID);
		if (offer == null) {
			// 1. Check if the Loan Market has offers.
			return "There are no acceptable Loan Offers in the Market.\nReturning you to the Loan Menu.";
		}
		else {
			// 2. Check if the borrower meets the selected offer's minimum credit score:
			Customer borrower = customerController.getCustomer(customerID);
			String borrowerName = borrower.getUsername();
			Integer minCreditScore = Integer.valueOf(offer.getMinCreditScore());
			if (borrower.getCreditScore() < minCreditScore) {
				return "Your credit score is too low.\nReturning you to the Loan Menu.";
			}
			else {
				// 3. Borrower must specify destination account for the principal transfer by account number:
				comms.printSystemMessage(accountController.displayAccounts(customerID)); //displays the customer's accounts
				Account borrowerAccount = accountController.getAccountInput(customerID);
				UUID borrowerAccountID = borrowerAccount.getAccountID();
			
				// 2) System asks borrower to confirm they want to accept the offer:
				String prompt= "Accept the loan offer with:\n" + offer.toString() + 
						"\nEnter 'y' to confirm or 'n' to cancel: \n";
				boolean userConfirm = comms.confirm(prompt);
				
				if (userConfirm) {	
					// 3) Create new Loan object and add it to both Lender and Borrower:
					LoanDTO loanDto = new LoanDTO(customerID,borrowerName,borrowerAccountID,offer);
					Loan newLoan = new Loan(loanDto);
					Customer lender = customerController.getCustomer(offer.getLenderID());
					
					//Add new Loan to the Loan Database by calling loanService:
					boolean newLoanAdded = loanController.addNewLoan(customerID,newLoan);
					if (newLoanAdded) {
						borrower.addLoan(newLoan); // add new loan to borrower
						lender.addLoan(newLoan); // add new loan to lender
						response =  "New loan has been added to your loans list.\n";
						response +=  "New loan has been added to the Loans database.\n";
						
						//Now that new load has been successfully added, remove the loan offer from database
						boolean removedOffer = loanOfferService.removeOfferFromMarket(offer);
						if (removedOffer) {
							response += "The offer has been succesfully removed from the Market.";
						}
						else {
							response += "Offer not found.";
						}
					}
					else {
						response +=  "Could not add loan to the Database.\n";
					}
					
					
				}
				else {
					response = "Loan offer creation was cancelled.\nReturning to the Main Menu.";
				}
				
				// 3) create the credit and debit transactions for the principal transfer
				response += "The loan principal has been credited to your specified account.\n";
				
				// 4) generate an automated repayment schedule:
				response += "\"The loan repayment schedule has been generated.\n";
				
				return response; 
						
			}
		}
	}
	
	
}
