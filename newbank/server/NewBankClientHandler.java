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
	private User user;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		user = new User();
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

				// The User is not logged into the system yet so CustomerID is null
				CustomerID customer = null;

				// Welcome
				user.userWelcome();

				// Checkin User
				String request = user.userCheckin();

				// Process the user's response: LOGIN or REGISTER
				while (true) {

					// lOGIN
					if (request.equals("LOGIN")) {
						customer = user.userLogIn(bank);

						// REGISTER
					} else if (request.equals("REGISTER")) {
						customer = user.userRegistration(bank);
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

}
