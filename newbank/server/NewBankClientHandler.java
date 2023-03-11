package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import newbank.server.Requests.Login;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;

	public NewBankClientHandler(Socket socket) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}

	public void run() {
		// keep getting requests from the client and processing them
		try {
			while (true)
				loginSequence();
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
	 * Initial Welcome Screen:
	 * Existing users must login before accessing system
	 * New Users must register a unique username and password before allowed to
	 * login
	 * 
	 * @throws IOException
	 */
	private void loginSequence() throws IOException {

		// Welcome
		Display.WelcomeMessage(out);

		// The User is not logged into the system yet so CustomerID is null
		CustomerID customer = null;

		// Handle input from user, either LOGIN or REGISTRATION
		Login login = new Login();
		String request = login.handleRequest();

		// Process the user's response: LOGIN or REGISTER
		while (true) {
			if (request.equals("LOGIN")) {
				customer = userLogIn();
			} else {
				// TODO: add new user registration method or code
				// userRegistration()
			}

			// if user succesfully logged-in, get requests from user and process them
			if (customer != null) {

				// Asking for a request and process the request
				while (true) {

					// show main menu to user
					Display.MainMenu(out);

					// get user input as request
					request = in.readLine();

					// handle response from bank given request, for now just print
					handleBankResponse(request, customer);
				}
			}
		}
	}

	/**
	 * @param request
	 * @param customerID
	 */
	private void handleBankResponse(String request, CustomerID customerID) {
		String responce = bank.processRequest(customerID, request);
		out.println(responce);
	}

	/**
	 * Login sequence for an existing customer, returns the customerID if login was
	 * succesfull
	 * 
	 * @return customerID if login was successful, else null
	 * @throws IOException
	 */
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
	public void userRegistration() throws IOException {

	}
}
