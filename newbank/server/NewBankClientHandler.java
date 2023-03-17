package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private static BufferedReader in;
	private static PrintWriter out;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	public void run() {
		// keep getting requests from the client and processing them
		// The User is not logged into the system yet so CustomerID is null
		CustomerID customer = null;
		
		try {
			while (true) {
				/*
				 * Initial Welcome Screen:
				 * Existing users must login before accessing system
				 * New Users must register a unique username and password before allowed to
				 * login
				 */
				// Processes the user's response: LOGIN or REGISTER
				if (customer == null){
					String request = userWelcome();
					if (request.equals("LOGIN")) {
						customer = userLogIn();
					} else {
						customer = userRegistration();
					} 
				}
				else {
					// Asking for a request and process the request
					// TODO: #10 add a display class that takes car of all the string work
					out.println("\n");
					out.println("Select Option...");
					out.println("SHOWMYACCOUNTS");
					out.println("NEWACCOUNT");
					out.println("MOVE");
					out.println("PAY");
					out.println("CHANGEMYPASSWORD");
					out.println("LOGOUT");
					out.println("\n");
					String request = in.readLine();
					System.out.println("Request from " + customer.getKey());
					String responce = bank.processRequest(customer, request);
					if (bank.getCustomers().get(customer.getKey()).getloggedInStatus()==false) {
						customer = null;
					}
					out.println(responce);
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

	public static String userWelcome() throws IOException {
		out.println("##############################\n");
		out.println("**** Welcome to New Bank ****\n");
		out.println("##############################\n\n");
		out.println("If you're an existing customer type: LOGIN\n");
		out.println("If you're a new customer type: REGISTER\n");
		String request = in.readLine();
		boolean requestValid = false;
		do {
			if (request.equals("LOGIN") || request.equals("REGISTER")) {
				requestValid = true;
			} else {
				out.println("Invalid request: Please enter LOGIN or REGISTER");
				request = in.readLine();
			}
		} while (!requestValid);
		return request;
	}

	// Login for existing customers
	public CustomerID userLogIn() throws IOException {
		// Not a customer yet

		CustomerID customer = null;
		// Ask for existing username
		out.println("Enter Username");
		String userName = in.readLine();
		// ask for existing password
		out.println("Enter Password");
		String password = in.readLine();
		out.println("Please wait while we check your details");
		customer = bank.checkLogInDetails(userName, password);
		// Validate login details
		if (customer == null) {
			out.println("Log In Failed. Invalid Credentials, please try again.");
		} else {
			out.println("Log In Successful. What do you want to do?");
		}
		return customer;
	}

	// TO DO
	// Registration for new customers
	public CustomerID userRegistration() throws IOException {

		// flag for registrationed success
		CustomerID customerID = null;

		// Ask for existing username
		out.println("Enter Username");
		String userName = in.readLine();

		// ask password
		out.println("Choose Password");
		String passwordAttempt1 = in.readLine();
		out.println("Re-Enter Password");
		String passwordAttempt2 = in.readLine();

		if (!passwordAttempt2.equals(passwordAttempt1)) {
			out.println("Passwords do not match");
			return null;
		}
		// check if userName already exists, if yesm is registers gets changed to true
		customerID = bank.registerCustomer(userName, passwordAttempt2);
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
}
