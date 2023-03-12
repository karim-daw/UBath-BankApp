package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import newbank.server.Requests.LoginService;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	private LoginService login;

	public NewBankClientHandler(Socket socket) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		login = new LoginService();
	}

	public void run() {
		// keep getting requests from the client and processing them
		try {
			while (true)
				NewBankService();
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
	private void NewBankService() throws IOException {

		// Welcome
		Display.WelcomeMessage(out);

		// The User is not logged into the system yet so CustomerID is null
		CustomerID customer = null;

		// Handle input from user, either LOGIN or REGISTRATION
		String request = login.handleLoginOrRegister();

		// Process the user's response: LOGIN or REGISTER
		while (true) {
			if (request.equals("LOGIN")) {
				customer = login.verifyCustomer();
				processCustomerRequest(customer, request);

			} else if (request.equals("REGISTER")) {
				// TODO: #19 add new user registration method or code
				// userRegistration()
			}

		}
	}

	/**
	 * Given a valid customerID, system Displays the main menu to the logged in
	 * customer and continuesly processes transactions with bank in while loop
	 * 
	 * and prompts user to
	 * 
	 * @param customerID
	 * @param request
	 * @throws IOException
	 */
	private void processCustomerRequest(CustomerID customerID, String request) throws IOException {
		// if user succesfully logged-in, get requests from user and process them
		if (customerID != null) {

			// Asking for a request and process the request
			while (true) {

				// show main menu to user and prompt for an input
				Display.MainMenu(out);

				// get user input as request
				request = in.readLine();

				// handle response from bank given request, for now just print
				handleBankResponse(request, customerID);
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

}
