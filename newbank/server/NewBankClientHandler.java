package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	public void run() {
		// keep getting requests from the client and processing them
		try {
			while (true) {
				// ask for user name
				out.println("Enter Username");
				String userName = in.readLine();

				String test = String.format("name is %s", userName);
				System.out.println(test);

				// ask for password
				out.println("Enter Password");
				String password = in.readLine();

				String test2 = String.format("password is %s", password);
				System.out.println(test2);

				out.println("Checking Details...");

				// authenticate user and get customer ID token from bank for use in subsequent
				// requests
				CustomerID customer = bank.checkLogInDetails(userName, password);
				// if the user is authenticated then get requests from the user and process them
				if (customer != null) {
					out.println("Log In Successful.");
					while (true) {

						// Asking for a request and process the request
						// TODO: #10 add a display class that takes car of all the string work
						out.println("\n");
						out.println("Select Option...");
						out.println("SHOWMYACCOUNTS");
						out.println("NEWACCOUNT");
						out.println("MOVE");
						out.println("PAY");
						out.println("\n");

						String request = in.readLine();
						System.out.println("Request from " + customer.getKey());
						String responce = bank.processRequest(customer, request);
						out.println(responce);
					}
				} else {
					out.println("Log In Failed\n");
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

}
