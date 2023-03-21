package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
		"||      5. Logout                                 ||\n" +
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
		// check if userName already exists, if yesm is registers gets changed to true
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
					return bank.showMyAccounts(customerID);
				case "2":
					// inputBalance
					//return createAccount(customer, requestInputs, 0);
					return createAccountEnhancement(customerID);
				/*
				case "MOVE":
					return moveMoney(customerID, requestInputs);
				case "PAY":
					return transferMoney(customerID, requestInputs);
				case "LOGOUT":
					// return to the main menu userwelcome
					return logOut(customerID);
				case "CHANGEMYPASSWORD":
					return changePassword(customerID,requestInputs);
				*/
				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}
	
	public String createAccountEnhancement(CustomerID customerID) {
		
		String response=""; //the system response to the user's request
		Customer customer = bank.getCustomers().get(customerID.getKey()); //the current customer
		int noOfChoices =customer.newAcctTypes().size();
		if (noOfChoices>0) { //if there are available account types for creation
			String systemPrompt = "Choose from: \n" + customer.newAcctTypesToString() +"\nEnter your option number: \n";
			String userInput = comms.getUserMenuChoice(systemPrompt,noOfChoices);
			//out.println(userInput);
			String accountType = customer.newAcctTypes().get(userInput); //gets the new account type
			//out.println(accountType);
			
			systemPrompt = "Enter an opening balance: \n";
			double openingBalance = comms.getAmount(systemPrompt);
			
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
	
	
	
}
