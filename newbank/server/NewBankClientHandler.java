package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	private Display display;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		display = new Display(out);
	}

	public void run() {
		// keep getting requests from the client and processing them
		try {
			while (true) {

				/*
				 * Initial Welcome Screen:
				 * Existing users must login before accessing system
				 * New Users must register a unique username and password before allowed to
				 * login
				 */

				// Welcome
				String request = userWelcomeAndCheckin();

				// The User is not logged into the system yet so CustomerID is null
				CustomerID customer = null;

				// Process the user's response: LOGIN or REGISTER
				while (true) {
					// lOGIN
					if (request.equals("LOGIN")) {
						customer = userLogIn();
						// REGISTER
					} else if (request.equals("REGISTER")) {
						customer = userRegistration();
					} else {
					}

					// if the user has logged-in, get requests from the user and process them
					request = processCustomerRequest(request, customer);
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

	/**
	 * @param request
	 * @param customer
	 * @return
	 * @throws IOException
	 */
	private String processCustomerRequest(String request, CustomerID customer) throws IOException {
		if (customer != null) {
			while (true) {
				// Asking for a request and process the request
				display.displayMainMenu();

				// get request from user
				request = in.readLine();
				System.out.println("Request from " + customer.getKey());

				// get responce from bank and display responce
				String responce = bank.processRequest(customer, request);
				out.println(responce);
			}
		}
		return request;
	}

	public String userWelcomeAndCheckin() throws IOException {

		// display welcome screen
		display.displayWelcome();

		// read input from user
		String request = in.readLine();

		// check if user logged in or registereed
		request = checkLoginOrRegister(request);
		return request;
	}

	/**
	 * This method will take a request and check if it is LOGIN or REGISTER, if they
	 * are, thhe valid request will get returned. If not, it will continuely ask for
	 * LOGIN or REGISTER
	 * 
	 * @param request
	 * @return request string of the valid request i.e LOGIN or REGISTER
	 * @throws IOException
	 */
	private String checkLoginOrRegister(String request) throws IOException {
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

	/**
	 * Registration for new customers
	 * 
	 * @return
	 * @throws IOException
	 */
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
