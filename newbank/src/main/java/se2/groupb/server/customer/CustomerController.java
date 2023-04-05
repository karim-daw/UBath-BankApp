package se2.groupb.server.customer;

import java.io.IOException;
import java.util.*;
import se2.groupb.server.UserInput;
import se2.groupb.server.account.*;
import se2.groupb.server.loan.*;
import java.math.BigDecimal;

public class CustomerController {
	// fields

	private final CustomerService customerService;
	private final AccountService accountService;
	private final LoanService loanService;
	private UserInput comms;

	// Constructor
	public CustomerController(CustomerService customerService, AccountService accountService, LoanService loanService,
			UserInput comms) {
		
		this.customerService = customerService;
		this.accountService = accountService;
		this.loanService = loanService;
		this.comms = comms;
	}

	// methods

	/**
	 * Returns the Customer object from the Data Store
	 * 
	 * @param customerID
	 * @return
	 */
	public Customer getCustomer(UUID customerID) {
		return customerService.getCustomerByID(customerID);
	}

	/**
	 * Login for existing customers
	 * 
	 * @return Customer object
	 */
	public UUID userLogin() {
		String systemResponse = "";
		String username = comms.getUserString("Enter Username");
		String password = comms.getUserString("Enter Password");
		CustomerDTO customerDto = new CustomerDTO(username, password);
		comms.printSystemMessage("Please wait while we check your details");
		Customer customer = customerService.getCustomerbyDTO(customerDto);

		// Validate login details
		if (customer == null) {
			systemResponse = "LOGIN FAIL. Invalid Credentials, please try again.";
			comms.printSystemMessage(systemResponse);
			return null;
		} else {
			systemResponse = "LOGIN SUCCESS. What do you want to do?";
			comms.printSystemMessage(systemResponse);
			return customer.getCustomerID();
		}
	}

	/**
	 * Create a new Customer
	 * 
	 * @return CustomerDTO
	 */
	public UUID userRegistration() {
		String username;
		boolean duplicateUsername;
		do {
			username = comms.getUserString("Enter a username");
			duplicateUsername = customerService.duplicateUsername(username);
			if (duplicateUsername) {
				comms.printSystemMessage("Username taken. Please try again. ");
			}
		} while (duplicateUsername);

		// ask for a password
		String passwordAttempt1;
		String passwordAttempt2;
		boolean matchedPasswords;
		do {
			passwordAttempt1 = comms.getUserString("Choose Password");
			passwordAttempt2 = comms.getUserString("Re-Enter Password");
			matchedPasswords = passwordAttempt2.equals(passwordAttempt1);
			if (!matchedPasswords) {
				comms.printSystemMessage("Passwords do not match. Please try again.");
			}
		} while (!matchedPasswords);

		String prompt = "Create new user with username " + username + " and password " + passwordAttempt2 + "?";
		boolean userConfirm = comms.confirm(prompt);
		if (userConfirm) {
			CustomerDTO customerDto = new CustomerDTO(username, passwordAttempt2);
			if (customerService.addNewCustomer(customerDto)) {
				String str = String.format("Registration succesfull. Please login to proceed.");
				comms.printSystemMessage(str);
				/*
				Customer customer = customerService.getCustomerbyDTO(customerDto);
				UUID customerID = customer.getCustomerID();
				return customerID;
				*/
			} else {
				String str = String.format("Database update failed. User not registered.");
				comms.printSystemMessage(str);
			}
		} else {
			String str = "Registration cancelled by user.";
			comms.printSystemMessage(str);
		}
		return null;
	}

	/**
	 * @param customerID
	 * @return
	 */
	public String userLogout(UUID customerID) {
		String systemResponse = "";
		String prompt = "Are you sure you want to log out?";
		boolean userConfirm = comms.confirm(prompt);
		if (userConfirm) {
			customerService.userLogout(customerID);
			systemResponse = "LOGOUT SUCCESS";
		} else {
			systemResponse = "LOGOUT CANCELLED. RETURNING YOU TO THE MAIN MENU.";
		}
		return systemResponse;
	}

	/**
	 * displays the customers accounts as a list
	 * 
	 * @param customerDTO
	 * @return
	 */
	public String displayAccounts(UUID customerID) {
		return customerService.displayAccounts(customerID);
	}

	/**
	 * NEWACCOUNT <Name>
	 * e.g. NEWACCOUNT Savings
	 * Returns SUCCESS or FAIL
	 * 
	 * @param customerID
	 * @return
	 */
	public String newAccount(UUID customerID) {
		String response = ""; // the system response to the user's request
		// UUID customerID = customer.getCustomerID();
		HashMap<String, String> newAcctOptions = accountService.newAccountAvailableTypes(customerID);

		int noOfChoices = newAcctOptions.size(); // 0,1, or 2
		if (noOfChoices > 0) {
			String prompt = "Create a new account: \n" + mapToString(newAcctOptions)
					+ "\nEnter the number of your choice: ";
			String userInput = comms.getUserMenuChoice(prompt, noOfChoices);
			String accountType = newAcctOptions.get(userInput); // the choice of account type entered by the user
			String str = "Create a new " + accountType +" account.\n";
			comms.printSystemMessage(str);

			// check if customer already has an account type with that name
			boolean duplicateName;
			String accountName;
			do {
				prompt = "Enter an account name: ";
				accountName = comms.getUserString(prompt);
				duplicateName = accountService.hasAccount(customerID, accountType, accountName);
				if (duplicateName) {
					comms.printSystemMessage("Account name taken. Please try again.");
				}
			} while (duplicateName);
			
			prompt = "Enter a positive opening balance (default is zero): \n";
			BigDecimal openingBalance = comms.getOpeningBalance(prompt);

			prompt = "Open a new " + accountType + " account: " + accountName + " with an opening balance of "
					+ openingBalance
					+ "?\nEnter 'y' for Yes or 'n' for No: \n";
			boolean userConfirm = comms.confirm(prompt);

			if (userConfirm) {

				// adds new account to customer
				AccountDTO accountDto = new AccountDTO(accountType, accountName, openingBalance);
				Account newAccount = accountService.createAccount(customerID, accountDto);
				Customer customer = customerService.getCustomerByID(customerID);
				customer.addAccount(newAccount);

				// Call NewBank method to add new customer account to bank's data store
				response = "SUCCESS: Your " + accountType + " account has been created.\nReturning to Main Menu.";
			} else {
				response = "Account creation was cancelled.\nReturning to the Main Menu.";
			}
		} else {
			response = "You have reached the maximum number of accounts.\nReturning to Main Menu.";
		}
		return response;
	}

	
	
	public String newLoanOffer(UUID customerID) {
		
		// Customer must specify the following:
		// 1) a source account: by account number
		// 2) a principal amount: must validate the balance is sufficient
		// 3) an interest rate
		// 4) a duration value: e.g. 5
		// 5) a duration type: e.g. years
		// 6) number of installments
		// 7) minimum credit score required to accept offer
		
		// To string method displays each offer as:
		// Lender name + Amount + Interest + Duration + Duration Type + Installments
		// Bhagy 5,000 5% 5 years 12 700
		
		return "Your loan offer has been added to the MarketPlace";
	}
	
	
	public String viewLoanMarket(UUID customerID) {
		String s="";
		ArrayList<LoanOffer> loanOffers = loanService.getLoanOffers();
		
		for (LoanOffer l: loanOffers) {
			s+=l.toString();
		}
		return s;
		
	}
	
	public String acceptLoanOffer(UUID customerID) {
		// Customer must specify the following:
		// 1) a source account: by account number
		// 2) a principal amount: must validate the balance is sufficient
		// 3) an interest rate
		// 4) a duration type: e.g. years
		// 5) a duration value: e.g. 5
		// 6) number of installments
		// 7) minimum credit score required to accept offer
		return "Your loan offer has been added to the MarketPlace";
	}
	
	/**
	 *
	 * Helper method for printing the contents of a HashMap<String,String>
	 * 
	 * @return a string of the contents
	 */
	public String mapToString(HashMap<String, String> map) {
		String s = "";
		if (map.size() > 0) {
			for (HashMap.Entry<String, String> item : map.entrySet()) {
				s += item.getKey() + " = " + item.getValue() + "\n";
			}
		}
		return s;
	}

}
