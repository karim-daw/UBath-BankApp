package se2.groupb.server;

import java.math.BigDecimal;
import java.util.HashMap;
import se2.groupb.server.account.Account;
import se2.groupb.server.customer.Customer;


public class NewBank {

	public static final String BIC = "NEWBGB21";
	private static final NewBank bank = new NewBank(); // every instance of NewBank has the same bank info
	
	private HashMap<String, Customer> customers; //temp customer data store
	private HashMap<String, Account> accounts; //temp account data store

	// Constructor
	private NewBank() {
		// create temp data store
		customers = new HashMap<>();
		accounts = new HashMap<>();
		// adding data for debugging
		addTestData();
		//displayCustomers();
	}

	/**
	 * debugging helper function that adds dummy data to a hashmap
	 */
	private void addTestData() {
		Customer bhagy = new Customer("Bhagy", "password");
		getCustomers().put(bhagy.getCustomerID().toString(), bhagy);
		
		Account bhagy_acct1 = new Account(bhagy.getCustomerID(), "Current", "Main", BigDecimal.valueOf(20000));
		Account bhagy_acct2 =new Account(bhagy.getCustomerID(), "Savings", "Car", BigDecimal.valueOf(1000));
		getAccounts().put(bhagy_acct1.getAccountID().toString(), bhagy_acct1);
		getAccounts().put(bhagy_acct2.getAccountID().toString(), bhagy_acct2);
		bhagy.addAccount(bhagy_acct1);
		bhagy.addAccount(bhagy_acct2);
		
		Customer christina = new Customer("Christina", "1234");
		getCustomers().put(christina.getCustomerID().toString(), christina);
		Account christina_acct1 = new Account(christina.getCustomerID(), "Savings", "House", BigDecimal.valueOf(1500));
		getAccounts().put(christina_acct1.getAccountID().toString(), christina_acct1);
		christina.addAccount(christina_acct1);
		
		Customer john = new Customer("John", "1111");
		getCustomers().put(john.getCustomerID().toString(), john);
		Account john_acct1 = new Account(john.getCustomerID(), "Current", "Main", BigDecimal.valueOf(250));
		getAccounts().put(john_acct1.getAccountID().toString(), john_acct1);
		john.addAccount(john_acct1);
	}

	public HashMap<String, Customer> getCustomers() {
		return customers;
	}
	
	public HashMap<String, Account> getAccounts() {
		return accounts;
	}
	
	/*
	public void displayCustomers() {
		System.out.println(getCustomers().keySet());
	}
	*/
	
	public static NewBank getBank() {
		return bank;
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
	 * public String transferMoney(CustomerID customerID, String[] requestArray) {
	 * 
	 * if (requestArray.length < 3) {
	 * return "FAIL, incomplete PAY Request";
	 * }
	 * 
	 * // Check if the customer exists in the hashmap.
	 * String customerName = customerID.getKey();
	 * String payeeName = requestArray[1];
	 * 
	 * Customer customer = customers.get(customerName);
	 * System.out.println(payeeName);
	 * 
	 * if (payeeName.equals(customerName)) {
	 * return "FAIL, you are trying to pay yourself";
	 * }
	 * 
	 * if (!customers.containsKey(payeeName)) {
	 * return "FAIL, payee not a member of NewBank";
	 * }
	 * 
	 * double transferAmount;
	 * try {
	 * transferAmount = Double.parseDouble(requestArray[2]);
	 * } catch (NumberFormatException e) {
	 * return "FAIL"; // return fail if input is not figures instead of an error
	 * }
	 * 
	 * if (transferAmount < 0) {
	 * return "FAIL";
	 * }
	 * 
	 * ArrayList<Account> payerAccounts = customer.getAccounts(); // payers accounts
	 * 
	 * // first account in accounts list will be default for now for payer
	 * Account payerFirstAccount = payerAccounts.get(0);
	 * 
	 * if (isOverDraft(payerFirstAccount, transferAmount)) {
	 * return "FAIL, insufficient funds for PAY amount";
	 * }
	 * payerFirstAccount.updateBalance(-transferAmount);
	 * 
	 * // handle update on payee account
	 * CustomerID payeeCustomerID = new CustomerID(payeeName);
	 * Customer payeeCustomer = customers.get(payeeCustomerID.getKey());
	 * 
	 * // get payee account as customer
	 * ArrayList<Account> payeeAccounts = payeeCustomer.getAccounts();
	 * Account payeeFirstAccount = payeeAccounts.get(0); // first account
	 * 
	 * // update balance
	 * payeeFirstAccount.updateBalance(transferAmount);
	 * 
	 * return "SUCCESS";
	 * 
	 * }
	 */

	/*
	 * public boolean isOverDraft(Account account, double deduction) {
	 * 
	 * double balance = account.getBalance();
	 * if (deduction > balance) {
	 * return true;
	 * }
	 * return false;
	 * }
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
	 * public String moveMoney(CustomerID customerID, String[] requestInputs) {
	 * 
	 * // Check if request is incomplete
	 * int inputLength = requestInputs.length;
	 * if (inputLength < 4) {
	 * return "FAIL: Invalid MOVE request";
	 * }
	 * 
	 * // Check if the customer exists in the hashmap.
	 * String customerName = customerID.getKey();
	 * Customer customer = customers.get(customerName);
	 * 
	 * // Check if transfer amount is a number
	 * double transferAmount;
	 * try {
	 * transferAmount = Double.parseDouble(requestInputs[1]);
	 * } catch (NumberFormatException e) {
	 * return "FAIL"; // return fail if input is not figures instead of an error
	 * }
	 * 
	 * // Check if transfer amount is negative
	 * if (transferAmount < 0) {
	 * return "FAIL";
	 * }
	 * 
	 * // Get the accounts from the customer
	 * System.out.println("Select source account.");
	 * Account sourceAccount = customer.getAccountByName(requestInputs[2]);
	 * if (sourceAccount.equals(null)) {
	 * return "FAIL, source account does not exist";
	 * }
	 * 
	 * // check if account has overdraft
	 * if (isOverDraft(sourceAccount, transferAmount)) {
	 * return "FAIL, insufficient funds in the source account.";
	 * }
	 * 
	 * // update balance of source account
	 * sourceAccount.updateBalance(-transferAmount);
	 * 
	 * // get destination account
	 * System.out.println("Select destination account.");
	 * Account destinationAccount = customer.getAccountByName(requestInputs[3]);
	 * if (destinationAccount == null) {
	 * return "FAIL, destination account does not exist";
	 * }
	 * 
	 * // update balance of destination account
	 * destinationAccount.updateBalance(transferAmount);
	 * return "SUCCESS";
	 * 
	 * }
	 */
}
