package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NewBankClientHandler extends Thread {
	
	public static final String welcomeMessage =
		"\n" +
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
	public static final int welcomeChoices = 2;
	
	public static final String requestMenu = "\n" +
		"====================================================\n" +
		"||           *** NEWBANK MAIN MENU ***            ||\n" +
		"====================================================\n" +
		"|| Please select one of the following options:    ||\n" +
		"||      1. View Accounts                          ||\n" +
		"||      2. Create New Account                     ||\n" +
		"||      3. Move Money                             ||\n" +
		"||      4. Pay Person/Company                     ||\n" +
		"||      5. Change Password                        ||\n" +
		"||      6. Logout                                 ||\n" +
		"|| Enter the number corresponding to your choice  ||\n" +
		"|| and press enter                                ||\n" +
		"====================================================\n" +
		"\nEnter Selection:";
	public static final int mainMenuChoices = 5;
	private NewBank bank;
	public BufferedReader in;
	public PrintWriter out;
	//private Socket socket;
	public UserInterface comms;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		//socket=s;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		comms = new UserInterface(in,out);
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		// The User is not logged into the system yet so CustomerID is null
		CustomerID customerID = null;
		String request = "";
		String response = "";
		try {
			while (true) {
				if (customerID == null){
					request = comms.getUserMenuChoice(welcomeMessage,welcomeChoices);
					// Processes the user's response: 1=LOGIN or 2=REGISTER
					if (request.equals("1")) {
						customerID = userLogIn();
					} else {
						customerID = userRegistration();
					} 
				}
				else {
					request = comms.getUserMenuChoice(requestMenu,mainMenuChoices);
					out.println("Request from " + customerID.getKey());
					response = processRequest(customerID, request);
					if (bank.getCustomers().get(customerID.getKey()).getloggedInStatus()==false) {
						customerID = null;
					}
					out.println(response);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	
	// Login for existing customers
	public CustomerID userLogIn() throws IOException {
		String userName = comms.getUserString("Enter Username");
		String password = comms.getUserString("Enter Password");
		comms.printSystemMessage("Please wait while we check your details");
		
		CustomerID customerID = bank.checkLogInDetails(userName, password);
		// Validate login details
		if (customerID == null) {
			out.println("Log In Failed. Invalid Credentials, please try again.");
		} else {
			out.println("Log In Successful. What do you want to do?");
		}
		return customerID;
	}

	// Registration for new customers
	public CustomerID userRegistration() throws IOException {

		// Ask for existing username
		String userName = comms.getUserString("Choose Username");
		// ask password
		String passwordAttempt1 = comms.getUserString("Choose Password");
		String passwordAttempt2 = comms.getUserString("Re-Enter Password");

		if (!passwordAttempt2.equals(passwordAttempt1)) {
			out.println("Passwords do not match");
			return null;
		}
		// check if userName already exists, if yes is registers gets changed to true
		CustomerID customerID = bank.registerCustomer(userName, passwordAttempt2);
		if (customerID != null) {
			String str = String.format("Registration succesfull. New Customer %s", userName);
			out.println(str);
		} else {
			String str = String.format("Customer name %s already exists, try registerings with a different name",
					userName);
			out.println(str);
		}
		return customerID;
	}
	
	public synchronized String processRequest(CustomerID customerID, String request) throws IOException{

		if (bank.getCustomers().containsKey(customerID.getKey())) {
			//String[] requestInputs = request.split("\\s+");
			//String command = requestInputs[0];

			switch (request) {
				case "1":
				case "SHOWMYACCOUNTS":
					return showMyAccounts(customerID);
				case "2":
				case "NEWACCOUNT":
					// inputBalance
					//return createAccount(customer, requestInputs, 0);
					return createAccountEnhancement(customerID);
				
				case "3":
				case "MOVE":
					return moveMoney(customerID);
				/*
				case "4":
				case "PAY":
					return transferMoney(customerID, requestInputs);
				case "5":
				case "CHANGEMYPASSWORD":
					return changePassword(customerID,requestInputs);
				*/
				case "6":
				case "LOGOUT":
					return logOut(customerID);
				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}
	
	public String showMyAccounts(CustomerID customerID) {
		Customer customer = bank.getCustomers().get(customerID.getKey());
		return customer.accountsToString();
	}
	
	public String createAccountEnhancement(CustomerID customerID) {
		
		String response=""; //the system response to the user's request
		Customer customer = bank.getCustomers().get(customerID.getKey()); //the current customer
		int noOfChoices =customer.newAcctTypes().size();
		if (noOfChoices>0) { //if there are available account types for creation
			String systemPrompt = "Choose from: \n" + customer.mapToString(customer.newAcctTypes()) +"\nEnter your option number: \n";
			String userInput = comms.getUserMenuChoice(systemPrompt,noOfChoices);
			//out.println(userInput);
			String accountType = customer.newAcctTypes().get(userInput); //gets the new account type
			//out.println(accountType);
			
			systemPrompt = "Enter an opening balance (must be positive): \n";
			double openingBalance = comms.getOpeningBalance(systemPrompt);
			
			systemPrompt="Open a new " + accountType + " account with an opening balance of " + openingBalance+ "?\nEnter 'y' for Yes or 'n' for No: \n";
			boolean userConfirm = comms.confirm(systemPrompt);
			
			if (userConfirm) {
				customer.addAccount(new Account(accountType, openingBalance)); //adds new account to customer
				//Call NewBank method to add new customer account to bank's data store
				response = "SUCCESS: Your " + accountType + " account has been created.\nReturning to Main Menu.";
			}
			else {
				response = "Account creation was cancelled.\nReturning to the Main Menu.";
			}		
		}
		else {
			response = "All possible account types have been created.\nReturning to Main Menu.";
			//newBankClientHandler.startup();
		}
		return response;
	}
	
	public String moveMoney(CustomerID customerID) {
		//MOVE <Amount> <From> <To>
		Customer customer = bank.getCustomers().get(customerID.getKey());
		String response = null;
		
		// Get the customer's existing accounts list
		int noOfSourceAccts =customer.sourceAcctsMap().size();
		int noOfAccts = customer.accountsToList().size();
		
		if ((noOfSourceAccts>=1)&&(noOfAccts>=2)) {
			//Select a source account (excludes overdrawn accounts)
			String prompt = "Select source account: \n" + customer.mapToString(customer.sourceAcctsMap()) +"\nEnter your option number: \n";
			String userInput = comms.getUserMenuChoice(prompt,noOfSourceAccts);
			String sourceAcctBalance = customer.sourceAcctsMap().get(userInput);
			String sourceAcct = sourceAcctBalance.split("\\:")[0];
					
			//Select a destination account (excludes source account)
			//out.println(customer.destinationAcctsMap(sourceAcct));
			prompt = "Select destination account: \n" + customer.mapToString(customer.destinationAcctsMap(sourceAcctBalance))+"\nEnter your option number: \n";
			int noOfDestAccts = customer.destinationAcctsMap(sourceAcctBalance).size();
			userInput = comms.getUserMenuChoice(prompt,noOfDestAccts);
			String destinationAcctBalance = customer.destinationAcctsMap(sourceAcctBalance).get(userInput);
			
			//Enter a positive amount
			prompt = "Transfer amount must be positive and not exceed the Source Account's balance.\nEnter an amount: ";
			String destinationAcct = destinationAcctBalance.split("\\:")[0];
			double limit= customer.getAccountByName(destinationAcct).getBalance();
			double transferAmount = comms.getAmount(prompt,limit);
			
			prompt="Move " + transferAmount + " from " + sourceAcct + " to " + destinationAcct + "?\nEnter 'y' for Yes or 'n' for No: \n";
			boolean userConfirm = comms.confirm(prompt);
			
			if (userConfirm) {
				// update balance of source account
				customer.getAccountByName(sourceAcct).updateBalance(-transferAmount);
				// update balance of destination account
				customer.getAccountByName(destinationAcct).updateBalance(transferAmount);
				response = "Move transaction was successful.";
			}
			else {
				response = "Move transaction was cancelled.\nReturning to the Main Menu.";
			}		
		}
		else {
			response = "You need two or more accounts.\nRequest denied.\nReturning to Main Menu.";
			//newBankClientHandler.startup();
		}
		return response;
	}
	
	public String logOut(CustomerID customerID) {
		bank.getCustomers().get(customerID.getKey()).setloggedInStatus(false);
		return "LOG OUT SUCCESSFUL";

	}
	
}
