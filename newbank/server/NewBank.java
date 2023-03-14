package server;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Scanner;

//import static java.lang.System.out;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	private static String main = "Main";
	private static String savings = "Savings";
	private static String checking = "Checking";
	Scanner scanner = new Scanner(System.in);

	public HashMap<String, Customer> getCustomers() {
		return customers;
	}

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	/**
	 * debugging helper function that adds dummy data to a hashmap
	 */
	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		bhagy.addAccount(new Account("Savings", 200.0));
		bhagy.setPassword("password");
		getCustomers().put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		christina.setPassword("1234");
		getCustomers().put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		john.setPassword("1111");
		getCustomers().put("John", john);
	}

	public static NewBank getBank() {
		return bank;
	}

	/**
	 * @param userName
	 * @param password
	 * @return null
	 */
	public synchronized CustomerID checkLogInDetails(String username, String password) {
		// Check if the username input by the user exists in the bank's system
		if (customers.containsKey(username)) {
			// If username exists then check their password
			Customer customer = customers.get(username);
			// If the password input equals the password on system then create new
			// CustomerID
			if (customer.getPassword().equals(password)) {
				return new CustomerID(username);
			}

		}
		return null;
	}

	/**
	 * 
	 * commands from the NewBank customer are processed in this method
	 * 
	 * @param customer
	 * @param request
	 * @return
	 */
	public synchronized String processRequest(CustomerID customer, String request) {

		if (customers.containsKey(customer.getKey())) {
			String[] requestInputs = request.split("\\s+");
			String command = requestInputs[0];

			switch (command) {
				case "SHOWMYACCOUNTS":
					return showMyAccounts(customer);
				case "NEWACCOUNT":
					// String selectAccountType = selectAccountType();
					// inputBalance
					return createAccount(customer, requestInputs, 0);
				case "MOVE":
					// return moveMoney(customer);
				default:
					return "FAIL";

			}
		}
		return "FAIL\n";
	}

	/**
	 * displays accounts as a list
	 * 
	 * @param customer
	 * @return
	 */

	private String showMyAccounts(CustomerID customer) {
		// create a list that will be displayed
		List<String> accountList = new ArrayList<String>();
		accountList = customers.get(customer.getKey()).accountsToList();
		String s = "";
		for (String a : accountList) {
			s += a.toString() + "\n";
		}
		return s;
	}

	/**
	 * Creates a new account for a given customer
	 * 
	 * @param customer
	 * @param requestInputs
	 * @param openingBalance
	 * @return string regarding success or failure of createtAccount request
	 */
	private String createAccount(CustomerID customer, String[] requestInputs, double openingBalance) {

		int inputLength = requestInputs.length;
		if (inputLength < 2) {
			return "FAIL: Account type not specified";
		}

		String accountType = requestInputs[1];
		if (!accountType.equals(main) && !accountType.equals(checking) && !accountType.equals(savings)) {
			return "FAIL: Account type not recognised";
		} else {
			Customer c = customers.get(customer.getKey());
			// check if the customer already have the account
			// account does not exist, continue to create a new account
			if (c.checkAccount(accountType) == false) {
				c.addAccount(new Account(accountType, openingBalance));
				return "SUCCESS: Your " + accountType + " account has been created.";
			} else {
				return "FAIL: You already have a " + accountType + " account.";
			}
		}
	}

	/**
	 * Registers a new customer to hashmap, performs validaiton to see if customer
	 * key is already in the hashmap
	 * 
	 * @param username
	 * @param password
	 * @return true is user successfull registered, false if username already exists
	 */
	public synchronized CustomerID registerCustomer(String username, String password) {

		Customer newCustomer = null;

		if (!customers.containsKey(username)) {

			// create a new sucomter with given username and password
			newCustomer = new Customer();
			newCustomer.addAccount(new Account("Main", 0.0));
			newCustomer.setPassword(password);

			// put customer into hash map
			getCustomers().put(username, newCustomer);
			CustomerID customerID = new CustomerID(username);

			// return added new customer id
			return customerID;
		}

		// returns null if user exists in hashmap already
		return null;

	}

	// TO DO
	/**
	 * validates password entered during new user registration
	 * 
	 * @param password
	 * @return
	 */
	public boolean isPasswordValid(String password) {
		return false;
	}

}
