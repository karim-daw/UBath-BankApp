package se2.groupb.server;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import se2.groupb.server.Account.Account;
import se2.groupb.server.Account.AccountController;
import se2.groupb.server.Account.AccountService;
import se2.groupb.server.Customer.Customer;
import se2.groupb.server.Customer.CustomerDTO;
import se2.groupb.server.Customer.CustomerController;
import se2.groupb.server.Customer.CustomerService;

public class NewBank {
	
	public static final String BIC = "NEWBGB21";
	private static final NewBank bank = new NewBank(); //every instance of NewBank has the same bank info
	private HashMap<String,Customer> customers;
	public static final List<String> validAcctList = 
		    Collections.unmodifiableList(Arrays.asList("Main","Savings","Checking"));
	
	//Constructor
	private CustomerController customerController;
	private CustomerService customerService;
	private AccountController accountController;
	private AccountService accountService;


	private NewBank() {

		// create temp data store
		customers = new HashMap<>();

		// init constorllers
		customerController = new CustomerController(customerService);
		accountController = new AccountController(accountService);

		// adding data for debugging
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

	public static NewBank getBank() {
		return bank;
	}

	/*
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
	*/
	
	/**
	 * checks login details of user, if valid username and password is found, a
	 * customer id is generated and returned
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public synchronized CustomerDTO checkLogInDetails(String username, String password) {
		// Check if the username input by the user exists in the bank's system

		if (customers.containsKey(username)) {
			// If username exists then check their password
			Customer customer = customers.get(username);
			// If the password input equals the password on system then create new
			// CustomerID
			if (customer.getPassword().equals(password)) {
				customer.setloggedInStatus(true);
				Long customerID = customer.getCustomerID();
				return new CustomerDTO(customerID, username);
			} else {
				customer.setloggedInStatus(false);
				return null;
			}
		} else {
			return null;
		}
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
	
	/*
	private String createAccount(Customer customer, String accountType, double openingBalance) {
		//adds account to bank's data store
		return "SUCCESS"; //or FAIL
	}
	
	*/
	
	/**
	 * Logs out the current customer
	 * 
	 * @param customer
	 */

	public String logOut(CustomerDTO customerDTO) {
		customers.get(customerDTO.getCustomerName()).setloggedInStatus(false);
		return "LOG OUT SUCCESSFUL";

	}
	
	/**
	 * this method takes care of the PAY feature indicated below, gven and customer
	 * (pay2er) and a requested payee, this will transfer money from these accounts
	 * and update balances accordingly
	 * PAY <Person/Company> <Ammount>
	 * e.g. PAY John 100
	 * Returns SUCCESS or FAIL
	 * 
	 * @param customer
	 * 
	 * @param requestArray
	 * 
	 * @return string that is SUCCESS or FAIL if transfer succeeded
	 */
	/*
	public String transferMoney(CustomerID customerID, String[] requestArray) {

		if (requestArray.length < 3) {
			return "FAIL, incomplete PAY Request";
		}

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
	*/
	
	/*
	public boolean isOverDraft(Account account, double deduction) {

		double balance = account.getBalance();
		if (deduction > balance) {
			return true;
		}
		return false;
	}
	*/
	
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
	/*
	public String moveMoney(CustomerID customerID, String[] requestInputs) {

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
	*/

	/**
	 * method that changes the password
	 * old password need to be enter
	 * then a new password, twice
	 * 
	 * @param customer
	 * @param requestInputs
	 * @return
	 */

	 public String changePassword(CustomerDTO customerID, String[] requestInputs){
		//check if the command is correct
		//return infinite loop of null, why ? 
		int inputLength = requestInputs.length;
		if (inputLength < 4) {
			return "FAIL. Please enter your old password and twice your new password after the command.";
		}

		String oldPassword = requestInputs[1];
		String newPassword = requestInputs[2];
		String confirmNewPassword = requestInputs[3];
		Customer c = customers.get(customerID.getCustomerName());

		//check if the old password is correct
		if (!c.getPassword().equals(oldPassword)){
			return "FAIL. The old password is incorrect.";			
		}

		//check if the two new password inputs match. 
		if (!newPassword.equals(confirmNewPassword)){
			return "FAIL. Password confirmation does not match.";
		}

		else {
			c.setPassword(newPassword);
			return "SUCCESS new password is: " + c.getPassword();
		}	
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
	/*
	public synchronized String processRequest(CustomerDTO customerDto, String request) {

		if (customers.containsKey(customerDto.getCustomerName())) {
			String[] requestInputs = request.split("\\s+");
			String command = requestInputs[0];

			switch (command) {
				case "SHOWMYACCOUNTS":
					return customerController.displayAccounts(customerDto);
				case "NEWACCOUNT":
					return accountController.createNewAccount(customerDto, requestInputs, 0);
				case "MOVE":
					return transactionController(customerDto, requestInputs);
				case "LOGOUT":
					// return to the main menu userwelcome
					return logOut(customerDto);
				case "PAY":
					return transferMoney(customerDto, requestInputs);

				case "CHANGEMYPASSWORD":
					return changePassword(customerDto, requestInputs);

				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}
	*/
	 
	

	/**
	 * Registers a new customer to hashmap, performs validaiton to see if customer
	 * key is already in the hashmap
	 * 
	 * @param username
	 * @param password
	 * @return true is user successfull registered, false if username already exists
	 */
	public synchronized Customer registerCustomer(String username, String password) {

		Customer newCustomer = null;

		if (!customers.containsKey(username)) {
			newCustomer = new Customer();
			newCustomer.addAccount(new Account("Main", 0.0));
			newCustomer.setPassword(password);
			getCustomers().put(username, newCustomer);
			return newCustomer;
		}
		return null;
	}


}
