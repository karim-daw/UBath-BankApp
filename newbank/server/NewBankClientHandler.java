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

	private void transferMoney(String sourceAccount, String destinationAccount, double amount){

		//Check if there is sufficient amount to transfer, otherwise ask to input new amount again
		if (customer.get(sourceAccount).get(balance) < amount) {
			out.println("Not enough amount in your account. Would you like to choose different amount? y/n")
			String choice = in.readLine();
			if choice.equalsIgnoreCase("y") {
				out.println("Enter a new amount: ");
				amount = in.readDouble();
				out.println("Enter new source account: ")
				sourceAccount = in.readString();
				transferMoney(sourceAccount,destinationAccount,amount);
			}
			//if amount is sufficient, make changes to source account and destination account
			customer.put(sourceAccount, customer.get(destinationAccount).get(balance) - amount);
			customer.put(targetAccount, customer.get(destinationAccount).get(balance)  + amount);
			out.println("The transaction has been completed.")
		}
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
					out.println("Log In Successful. What do you want to do?");
					while (true) {
						String request = in.readLine();
						System.out.println("Request from " + customer.getKey());
						String responce = bank.processRequest(customer, request);
						out.println(responce);
					}
				} else {
					out.println("Log In Failed");
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
