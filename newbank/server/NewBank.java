package newbank.server;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	public NewBank() {
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
		getCustomers().put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		getCustomers().put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
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
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if (customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		// TODO: #8 we need to add functionality here to check for the correct password
		// TODO: #9 we need to add password to Account class
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
	public String showMyAccounts(CustomerID customerID) {
		// create a list that will be displayed
		List<String> accountList = new ArrayList<String>();
		accountList = customers.get(customerID.getKey()).accountsToList();
		String s = "";
		for (String a : accountList) {
			s += a.toString() + "\n";
		}
		return s;

	}

}
