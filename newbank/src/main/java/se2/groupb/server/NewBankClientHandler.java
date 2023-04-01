package se2.groupb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import se2.groupb.server.customer.*;

public class NewBankClientHandler extends Thread {

	// statics

	private static final String welcomeMessage = "\n" +
			"====================================================\n" +
			"||           *** WELCOME TO NEWBANK ***           ||\n" +
			"====================================================\n" +
			"|| Please select one of the following options:    ||\n" +
			"||      1. LOGIN                                  ||\n" +
			"||      2. REGISTER                               ||\n" +
			"|| Enter the number corresponding to your choice  ||\n" +
			"|| and press enter                                ||\n" +
			"====================================================\n" +
			"\nEnter Selection:";
	private static final int welcomeChoices = 2;

	private static final String requestMenu = "\n" +
			"====================================================\n" +
			"||           *** NEWBANK MAIN MENU ***            ||\n" +
			"====================================================\n" +
			"|| Please select one of the following options:    ||\n" +
			"||      1. View Accounts                          ||\n" +
			"||      2. Select Account to View Transactions    ||\n" +
			"||      3. Create New Account                     ||\n" +
			"||      4. Move Money                             ||\n" +
			"||      5. Pay Person/Company                     ||\n" +
			"||      6. Change Password                        ||\n" +
			"||      7. Logout                                 ||\n" +
			"|| Enter the number corresponding to your choice  ||\n" +
			"|| and press enter                                ||\n" +
			"====================================================\n" +
			"\nEnter Selection:";

	private static final int mainMenuChoices = 7;

	// fields

	private NewBank bank;
	private final BufferedReader in;
	private final PrintWriter out;
	public final UserInput comms;
	private CustomerController customerController;
	private CustomerServiceImpl customerService;
	
	// constructor
	//each client has the same bank but different comms because of different sockets
	public NewBankClientHandler(Socket s) throws IOException {
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		comms = new UserInput(in,out);
		bank = NewBank.getBank(); //static instance of the bank
		// Initialise controllers
		customerService = new CustomerServiceImpl(bank.getCustomers());
		//System.out.println(customerService);
		customerController = new CustomerController(customerService,comms);
	}
	
	public CustomerController getCustomerController() {
		return customerController;
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		// The User is not logged into the system yet so CustomerID is null
		// CustomerID customerID = null;
		String request = "";
		String response = "";
		UUID customerID = null;
		
		try {
			while (true) {
				if (customerID == null) {
					request = comms.getUserMenuChoice(welcomeMessage, welcomeChoices);

					// Processes the user's response: 1=LOGIN or 2=REGISTER
					if (request.equals("1")) {
						customerID = getCustomerController().userLogin();
					} else {
						// customerID = userRegistration();
					}
				} else {
					request = comms.getUserMenuChoice(requestMenu, mainMenuChoices);
					String systemMessage = "Request from: " + customerID.toString();
					comms.printSystemMessage(systemMessage);
					response = processRequest(customerID, request);
					comms.printSystemMessage(response);
					String strCustomerID =customerID.toString();
					if (bank.getCustomers().get(strCustomerID).getloggedInStatus()==false) {
						customerID = null;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}


	/*
	 * // Registration for new customers
	 * public CustomerDTO userRegistration() throws IOException {
	 * 
	 * // Ask for existing username
	 * String userName = comms.getUserString("Choose Username");
	 * // ask password
	 * String passwordAttempt1 = comms.getUserString("Choose Password");
	 * String passwordAttempt2 = comms.getUserString("Re-Enter Password");
	 * 
	 * if (!passwordAttempt2.equals(passwordAttempt1)) {
	 * out.println("Passwords do not match");
	 * return null;
	 * }
	 * // check if userName already exists, if yes is registers gets changed to true
	 * CustomerDTO customerDto = bank.registerCustomer(userName, passwordAttempt2);
	 * if (customerDto != null) {
	 * String str = String.format("Registration succesfull. New Customer %s",
	 * userName);
	 * out.println(str);
	 * } else {
	 * String str = String.
	 * format("Customer name %s already exists, try registerings with a different name"
	 * ,
	 * userName);
	 * out.println(str);
	 * }
	 * return customerDto;
	 * }
	 */

	public synchronized String processRequest(UUID customerID, String request) throws IOException {
		
		//I don't think we need this check, only a customer that has logged in and has a valid customer ID 
		//can action a request
		if (customerID==null) return "FAIL";
		/*
		HashMap<String, Customer> customers = bank.getCustomers();
		boolean isCustomer = false;
		
		
		for (Customer customer : customers.values()) {
			if (customer.getCustomerID().equals(customerDto.getCustomerID())) {
				isCustomer = true;
				break; // this should not be needed but who knows??
			}
		}
		*/
		
		switch (request) {
			case "1":
			case "SHOWMYACCOUNTS":
				return getCustomerController().displayAccounts(customerID);
			/*
			case "2":
			case "NEWACCOUNT":
				// return createAccountEnhancement(customerID);
			case "3":
			case "MOVE":
				// return moveMoneyEnhancement(customerID);
				/*
				 * case "4":
				 * case "PAY":
				 * return transferMoney(customerID, requestInputs);
				 * case "5":
				 * case "CHANGEMYPASSWORD":
				 * return changePassword(customerID,requestInputs);
			*/
			case "7":
			case "LOGOUT":
				return getCustomerController().userLogout(customerID);
			default:
				return "FAIL";
		}
	}


	/*
	 * 
	 * public String createAccountEnhancement(CustomerID customerID) {
	 * String response = ""; // the system response to the user's request
	 * Customer customer = bank.getCustomers().get(customerID.getKey()); // the
	 * current customer
	 * int noOfChoices = customer.newAcctTypes().size();
	 * if (noOfChoices > 0) { // if there are available account types for creation
	 * String systemPrompt = "Create a new account.\nChoose from: \n"
	 * + customer.mapToString(customer.newAcctTypes()) +
	 * "\nEnter your option number: \n";
	 * String userInput = comms.getUserMenuChoice(systemPrompt, noOfChoices);
	 * // out.println(userInput);
	 * String accountType = customer.newAcctTypes().get(userInput); // gets the new
	 * account type
	 * // out.println(accountType);
	 * 
	 * systemPrompt = "Enter an opening balance (must be positive): \n";
	 * double openingBalance = comms.getOpeningBalance(systemPrompt);
	 * 
	 * systemPrompt = "Open a new " + accountType +
	 * " account with an opening balance of " + openingBalance
	 * + "?\nEnter 'y' for Yes or 'n' for No: \n";
	 * boolean userConfirm = comms.confirm(systemPrompt);
	 * 
	 * if (userConfirm) {
	 * customer.addAccount(new Account(accountType, openingBalance)); // adds new
	 * account to customer
	 * // Call NewBank method to add new customer account to bank's data store
	 * response = "SUCCESS: Your " + accountType +
	 * " account has been created.\nReturning to Main Menu.";
	 * } else {
	 * response = "Account creation was cancelled.\nReturning to the Main Menu.";
	 * }
	 * } else {
	 * response =
	 * "All possible account types have been created.\nReturning to Main Menu.";
	 * // newBankClientHandler.startup();
	 * }
	 * return response;
	 * }
	 */

	/*
	 * 
	 * public String moveMoneyEnhancement(CustomerID customerID) {
	 * // MOVE <Amount> <From> <To>
	 * Customer customer = bank.getCustomers().get(customerID.getKey());
	 * String response = null;
	 * 
	 * // Get the customer's existing accounts list
	 * int noOfSourceAccts = customer.sourceAcctsMap().size();
	 * int noOfAccts = customer.accountsToList().size();
	 * 
	 * if ((noOfSourceAccts >= 1) && (noOfAccts >= 2)) {
	 * // Select a source account (excludes overdrawn accounts)
	 * String prompt = "Move Money.\nSelect source account: \n" +
	 * customer.mapToString(customer.sourceAcctsMap())
	 * + "Enter your option number: \n";
	 * String userInput = comms.getUserMenuChoice(prompt, noOfSourceAccts);
	 * String sourceAcctBalance = customer.sourceAcctsMap().get(userInput);
	 * String sourceAcct = sourceAcctBalance.split("\\:")[0];
	 * 
	 * // Select a destination account (excludes source account)
	 * // out.println(customer.destinationAcctsMap(sourceAcct));
	 * prompt = "Select destination account: \n" +
	 * customer.mapToString(customer.destinationAcctsMap(sourceAcct))
	 * + "\nEnter your option number: \n";
	 * int noOfDestAccts = customer.destinationAcctsMap(sourceAcct).size();
	 * userInput = comms.getUserMenuChoice(prompt, noOfDestAccts);
	 * String destinationAcctBalance =
	 * customer.destinationAcctsMap(sourceAcct).get(userInput);
	 * String destinationAcct = destinationAcctBalance.split("\\:")[0];
	 * 
	 * // Enter a positive amount
	 * prompt =
	 * "Transfer amount must be positive and not exceed the Source Account's balance.\nEnter an amount: "
	 * ;
	 * 
	 * double limit = customer.getAccountByName(sourceAcct).getBalance();
	 * double transferAmount = comms.getAmount(prompt, limit);
	 * prompt = "Move " + transferAmount + " from " + sourceAcct + " to " +
	 * destinationAcct
	 * + "?\nEnter 'y' for Yes or 'n' for No: \n";
	 * boolean userConfirm = comms.confirm(prompt);
	 * 
	 * if (userConfirm) {
	 * // update balance of source account
	 * customer.getAccountByName(sourceAcct).updateBalance(-transferAmount);
	 * // update balance of destination account
	 * customer.getAccountByName(destinationAcct).updateBalance(transferAmount);
	 * response = "Move transaction was successful.";
	 * } else {
	 * response = "Move transaction was cancelled.\nReturning to the Main Menu.";
	 * }
	 * } else {
	 * response =
	 * "You need two or more accounts.\nRequest denied.\nReturning to Main Menu.";
	 * // newBankClientHandler.startup();
	 * }
	 * return response;
	 * }
	 */

	/*
	 * 
	 * public String logOut(CustomerID customerID) {
	 * bank.getCustomers().get(customerID.getKey()).setloggedInStatus(false);
	 * return "LOG OUT SUCCESSFUL";
	 * 
	 * }
	 */

}
