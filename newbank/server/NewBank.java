package newbank.server;

import java.io.*;

import java.util.HashMap;
import java.util.Scanner;

import static java.lang.System.out;

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

	private String createAccount(CustomerID customer, String accountType, double openingBalance) {

		if (!accountType.equals(main) && !accountType.equals(checking) && !accountType.equals(savings)) {
			return "Please select account type again.";
		}
		try {
			Customer c = customers.get(customer.getKey());
			// check if the customer already have the account
			// account does not exist, continue to create a new account
			if (c.checkAccount(accountType) == false) {
				c.addAccount(new Account(accountType, openingBalance));
				return "Your " + accountType + "account has been successfully created.";
			} else {
				return "You already have " + accountType + ".";
			}
		} catch (Exception e) {

		}
		return "Fail to create a new account.";
	}

	

		// type that user will select
	// when transfering money or 
	
	private String selectAccountType() {
		
		out.println("Select the account type by number");
		out.println("1. Main account");
		out.println("2. Savings account");
		out.println("3. Checkings account");
		out.println("4. Return");
		
		String request = scanner.nextLine();
		String[] typeOfAccount = request.split("\\s+");
		switch (typeOfAccount[0]){
			case "1" :
				return "Main";
			case "2":
				return "Savings";
			case "3":
				return "Checkings";
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
			String[] requestInputs = request.split("\\s+");
			String command = requestInputs[0];

			switch (command) {
				case "SHOWMYACCOUNTS":
					return showMyAccounts(customer);
				case "NEWACCOUNT":
					selectAccountType = selectAccountType();
					// inputBalance
					return createAccount(customer, selectAccountType, 0);
				case "MOVE":
					// return moveMoney(customer);
				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	
