package newbank.server;
import java.io.*;

import java.util.HashMap;

import static java.lang.System.out;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
	NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	private void addAccountToCustomer(String customerId) throws IOException {
		// Check if customer exists
		if (customers.containsKey(customerId)) {
			// Get the customer object
			Customer customer = customers.get(customerId);
			// Ask for the account type that wish to open
			String accountType = selectAccountType();
			// Ask for initial input
			double balance = openingBalance();
			Account account = new Account(accountType, balance);

			// Add the account to the customer's account hashmap
			customer.addAccount(account);
		} else {
			out.println("Customer not found!");
		}
	}
	public String selectAccountType() {
		out.println("Select the account type by number");
		out.println("1. Savings account");
		out.println("2. Checking account");
		out.println("3. Main account");
		out.println("4. Return");

		String typeOfAccount;
		try {
			BufferedReader in = null;
			assert false;
			typeOfAccount = in.readLine();
			switch (typeOfAccount) {
				case "1": return "Savings";
				case "2": return "Checking";
				case "3": return "Main";
				case "4": return "Return";
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return "";
	}

	private double openingBalance() throws IOException {
		out.println("Input the opening balance that you wish to put");
		DataInput in = null;
		assert false;
		return in.readDouble();
	}

	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			default : return "FAIL";
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

}
