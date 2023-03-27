package se2.groupb.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import se2.groupb.server.account.Account;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerDTO;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	private static String main = "Main";
	private static String savings = "Savings";
	private static String checking = "Checking";
	Scanner scanner = new Scanner(System.in);

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
		bhagy.setPassword("password");
		getCustomers().put("Bhagy", bhagy); // TODO: #29 Hashmap key should be unique number

		// TODO: #30 Helper service that generates some standard IDnumber
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		christina.setPassword("1234");
		getCustomers().put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		getCustomers().put("John", john);
		christina.setPassword("4321");
	}

	/**
	 * 
	 * commands from the NewBank customer are processed in this method
	 * 
	 * @param customer
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public synchronized String processRequest(CustomerDTO customerDto, String request) {

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
					return moveMoney(customer, requestInputs);
				case "LOGOUT":
					// return to the main menu userwelcome
					return logOut(customer);
				case "PAY":
					return transferMoney(customer, requestInputs);

				case "CHANGEMYPASSWORD":
					return changePassword(customer, requestInputs);

				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}

	/**
	 * checks login details of user, if valid username and password is found, a
	 * customer id is generated and returned
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public synchronized CustomerID checkLogInDetails(String username, String password) {
		// Check if the username input by the user exists in the bank's system

		if (customers.containsKey(username)) {
			// If username exists then check their password
			Customer customer = customers.get(username);
			// If the password input equals the password on system then create new
			// CustomerID
			if (customer.getPassword().equals(password)) {
				customer.setloggedInStatus(true);
				return new CustomerID(username);
			} else {
				customer.setloggedInStatus(false);
				return null;
			}
		} else {
			return null;
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
			newCustomer = new Customer();
			newCustomer.addAccount(new Account("Main", 0.0));
			newCustomer.setPassword(password);
			getCustomers().put(username, newCustomer);
			CustomerID customerID = new CustomerID(username);
			return customerID;
		}
		return null;
	}

	/**
	 * Logs out the current customer
	 * 
	 * @param customer
	 */

	private String logOut(CustomerID customer) {
		customers.get(customer.getKey()).setloggedInStatus(false);
		return "LOG OUT SUCCESSFUL";

	}

	public static NewBank getBank() {
		return bank;
	}

	public HashMap<String, Customer> getCustomers() {
		return customers;
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
