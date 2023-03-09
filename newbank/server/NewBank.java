package newbank.server;

import java.io.*;

import java.util.HashMap;

import static java.lang.System.out;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	// This method creates a new bank account for the customer
	// The data are stored in a nested hashmap.
	// The outer-hashmap has KEY (customer name) that points another KEY(account
	// number)
	// The inner-hasmap key then points to (value amount in the account)
	//
	// -> Main Account -> Value amount
	// customer name -> Savings Account -> Value amount
	// -> Checking Account -> Value amount
	//

	private void createNewAccount() {

		HashMap<String, HashMap<String, Double>> customerAccounts = new HashMap<>();

		// first check if the customer has their name registered in the hashmap
		// if customer name is not in the hashmap, registered the new customer into the
		// hashmao with put()
		if (!customerAccounts.containsKey("guy1")) {
			customerAccounts.put("guy1", new HashMap<>());
		}
		// if the customer is exist in the hashmap, use get method to select the key and
		// use put method to create new account
		customerAccounts.get("guy1").put("00000000M", 1000.0);

		// Add saving another account for Guy1
		customerAccounts.get("guy1").put("00000000S", 5000.0);

		// Add an checking account for Guy2
		if (!customerAccounts.containsKey("guy2")) {
			customerAccounts.put("guy2", new HashMap<>());
		}
		customerAccounts.get("guy2").put("00000000C", 2000.0);

		// Print the balances from all the accounts
		HashMap<String, Double> guy1Accounts = customerAccounts.get("guy1");
		for (String accountNumber : guy1Accounts.keySet()) {
			double balance = guy1Accounts.get(accountNumber);
			System.out.println("Account " + accountNumber + " balance: $" + balance);
		}

	}

	// This method will be used to return the String representation of the account
	// type that user will select
	// when transfering money or creating a new account
	private String selectAccountType() {
		out.println("Select the account type by number");
		out.println("1. Savings account");
		out.println("2. Checking account");
		out.println("3. Main account");
		out.println("4. Return");

		String typeOfAccount;
		try {
			typeOfAccount = in.readLine();
			switch (typeOfAccount) {
				case "1":
					return "Savings";
				case "2":
					return "Checking";
				case "3":
					return "Main";
				case "4":
					return "Return";
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return "";
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
		if (customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if (customers.containsKey(customer.getKey())) {
			switch (request) {
				case "SHOWMYACCOUNTS":
					return showMyAccounts(customer);
				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

}
