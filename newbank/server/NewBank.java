package newbank.server;


import java.util.ArrayList;
import java.util.List;

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


	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if (customers.containsKey(userName)) {
			return new CustomerID(userName);

	/**
	 * @param userName
	 * @param password
	 * @return null
	 */
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		//Check if the username input by the user exists in the bank's system
		if(customers.containsKey(userName)) {
			//If username exists then check their password
			Customer customer = customers.get(userName);
			//If the password input equals the password on system then create new CustomerID
			if(customer.getPassword().equals(password)) {
				return new CustomerID(userName);
			}

		}
		return null;
	}

	private void moveMoney(CustomerID customer, String sourceAccount, String destinationAccount, double amount){
        Customer c = customers.get(customer.getKey());
		double movingAmount;
		//check if the amount transfer is less than zero 
        try {
            movingAmount = Double.parseDouble(amount);
            if (movingAmount < 0.0) {
                return "Re-enter moving amount again.";
            }
        } catch (NumberFormatException e) {
            return "Please re-enter with numbers only.";
        }
		//check if the account is exist 
        try {
            if (c.checkAccount(sourceAccount) == false || c.checkAccount(destinationAccount) == false) {
                return "Account doee not exist";
            }

        } catch (Exception e) {
            return "Account does not exist";
        }
		//check the account value 
        Double currentBalance = c.getAccountValue();
		//deduct amount from the moving account 
        if (currentBalance >= movingAmount) {
			c.changeBalance(-movingAmount)
            return "SUCCESS";
        } else {
            return "FAIL";
        }
    }

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {

			String[] requestInputs = request.split("\\s+");
			String command = requestInputs[0];

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
          return moveMoney(customer, "main", "savings", 2000);
				default:
					return "FAIL";

			}
		}
		return "FAIL\n";
	}

<<<<<<< HEAD
}
=======

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
	
	//TO DO:
	
	/**
	 * validates username entered during new user registration 
	 * 
	 * @param username
	 * @return
	 */
	public boolean isUserNameValid(String userName) {
		return false;
		
	}
	
	
	//TO DO
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

>>>>>>> 9e4850373fed5b9aa85beda7af794d191aa8de01
