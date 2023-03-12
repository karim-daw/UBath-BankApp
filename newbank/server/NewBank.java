package newbank.server;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	public HashMap<String, Customer> getCustomers() {
		return customers;
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
			switch (request) {
				case "SHOWMYACCOUNTS":
					return showMyAccounts(customer);
				default:
					return "FAIL\n";
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

	// TO DO:

	/**
	 * Registers a new customer to hashmap, performs validaiton to see if customer
	 * key is already in the hashmap
	 * 
	 * @param customerName
	 * @param password
	 * @return true is user successfull registered, false if username already exists
	 */
	public synchronized CustomerID registerCustomer(String customerName, String password) {

		Customer newCustomer = null;

		if (customers.containsKey(customerName)) {
			newCustomer = new Customer();
			newCustomer.addAccount(new Account("Main", 0.0));
			newCustomer.addAccount(new Account("Savings", 0.0));
			newCustomer.setPassword(password);
			getCustomers().put(customerName, newCustomer);
			CustomerID customerID = new CustomerID(customerName);
			return customerID;
		}
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
