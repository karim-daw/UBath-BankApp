package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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
		bhagy.setPassword("password");
		getCustomers().put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		christina.setPassword("1234");
		getCustomers().put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		getCustomers().put("John", john);
		christina.setPassword("4321");
	}

	public static NewBank getBank() {
		return bank;
	}

	// private double inputAmount() {
	// Scanner scanner = new Scanner(System.in);
	// System.out.println("Please input the amount.");
	// try {
	// double amount = scanner.nextDouble();
	// return amount;
	// } catch (InputMismatchException e) {
	// System.out.println("Please re-enter with numbers only.");
	// return inputAmount(); // call the method recursively to get a valid input
	// }
	// }

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
			case "PAY":
				return transferMoney(customer, requestInputs);

			default:
				return "FAIL";

		}
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
	 * NEWACCOUNT <Name>
	 * e.g. NEWACCOUNT Savings
	 * Returns SUCCESS or FAIL
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
	 * 
	 * this method takes care of the PAY feature indicated below, gven and customer
	 * (payer) and a requested payee, this will transfer money from these accounts
	 * and update balances accordingly
	 * PAY <Person/Company> <Ammount>
	 * e.g. PAY John 100
	 * Returns SUCCESS or FAIL
	 * 
	 * @param customer
	 * @param requestArray
	 * @return string that is SUCCESS or FAIL if transfer succeeded
	 */
	private String transferMoney(CustomerID customerID, String[] requestArray) {

		// Check if the customer exists in the hashmap.
		String customerName = customerID.getKey();
		String payeeName = requestArray[1];

		Customer customer = customers.get(customerName);
		System.out.println(payeeName);

		if (payeeName.equals(customerName)) {
			return "FAIL, you are trying to pay yourself";
		}

		if (!customers.containsKey(payeeName)) {
			return "FAIL, payee not a member of NewBank";
		}

		double transferAmount;
		try {
			transferAmount = Double.parseDouble(requestArray[2]);
		} catch (NumberFormatException e) {
			return "FAIL"; // return fail if input is not figures instead of an error
		}

		if (transferAmount < 0) {
			return "FAIL";
		}

		ArrayList<Account> payerAccounts = customer.getAccounts(); // payers accounts

		// first account in accounts list will be default for now for payer
		Account payerFirstAccount = payerAccounts.get(0);

		if (isOverDraft(payerFirstAccount, transferAmount)) {
			return "FAIL, insufficient funds for PAY amount";
		}
		payerFirstAccount.updateBalance(-transferAmount);

		// handle update on payee account
		CustomerID payeeCustomerID = new CustomerID(payeeName);
		Customer payeeCustomer = customers.get(payeeCustomerID.getKey());

		// get payee account as customer
		ArrayList<Account> payeeAccounts = payeeCustomer.getAccounts();
		Account payeeFirstAccount = payeeAccounts.get(0); // first account

		// update balance
		payeeFirstAccount.updateBalance(transferAmount);

		return "SUCCESS";

	}

	private boolean isOverDraft(Account account, double deduction) {

		double balance = account.getBalance();
		if (deduction > balance) {
			return true;
		}
		return false;
	}

	/**
	 * method takes care of the MOVE protocol
	 * 
	 * MOVE <Amount> <From> <To>
	 * e.g. MOVE 100 Main Savings
	 * Returns SUCCESS or FAIL
	 * 
	 * @param customerID
	 * @param requestArray
	 * @return SUCCESS string or FAIL string
	 */
	private String moveMoney(CustomerID customerID, String[] requestInputs) {

		// Check if request is incomplete
		int inputLength = requestInputs.length;
		if (inputLength < 4) {
			return "FAIL: Invalid MOVE request";
		}

		// Check if the customer exists in the hashmap.
		String customerName = customerID.getKey();
		Customer customer = customers.get(customerName);

		// Check if transfer amount is a number
		double transferAmount;
		try {
			transferAmount = Double.parseDouble(requestInputs[1]);
		} catch (NumberFormatException e) {
			return "FAIL"; // return fail if input is not figures instead of an error
		}

		// Check if transfer amount is negative
		if (transferAmount < 0) {
			return "FAIL";
		}

		// Get the accounts from the customer
		System.out.println("Select source account.");
		Account sourceAccount = customer.getAccountByName(requestInputs[2]);
		if (sourceAccount.equals(null)) {
			return "FAIL, source account does not exist";
		}

		// check if account has overdraft
		if (isOverDraft(sourceAccount, transferAmount)) {
			return "FAIL, insufficient funds in the source account.";
		}

		// update balance of source account
		sourceAccount.updateBalance(-transferAmount);

		// get destination account
		System.out.println("Select destination account.");
		Account destinationAccount = customer.getAccountByName(requestInputs[3]);
		if (destinationAccount == null) {
			return "FAIL, destination account does not exist";
		}

		// update balance of destination account
		destinationAccount.updateBalance(transferAmount);
		return "SUCCESS";

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
