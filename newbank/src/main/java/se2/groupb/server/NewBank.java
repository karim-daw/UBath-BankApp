package se2.groupb.server;

import java.math.BigDecimal;
import java.util.HashMap;

import se2.groupb.server.account.Account;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.transaction.Transaction;

public class NewBank {

	public static final String BIC = "NEWBGB21";
	private static final NewBank bank = new NewBank(); // every instance of NewBank has the same bank info

	private HashMap<String, Customer> customers; // temp customer data store
	private HashMap<String, Account> accounts; // temp account data store
	private HashMap<String, Transaction> transactions;

	// Constructor
	private NewBank() {
		// create temp data store
		customers = new HashMap<>();
		accounts = new HashMap<>();
		transactions = new HashMap<>();
		// adding data for debugging
		addTestData();
		// displayCustomers();
	}

	/**
	 * debugging helper function that adds dummy data to a hashmap
	 */
	private void addTestData() {

		// password: "password"
		Customer bhagy = new Customer("Bhagy", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");

		getCustomers().put(bhagy.getCustomerID().toString(), bhagy);
		Account bhagy_acct1 = new Account(bhagy.getCustomerID(), "Current", "Main", BigDecimal.valueOf(20000));
		Account bhagy_acct2 = new Account(bhagy.getCustomerID(), "Savings", "Car", BigDecimal.valueOf(1000));
		getAccounts().put(bhagy_acct1.getAccountID().toString(), bhagy_acct1);
		getAccounts().put(bhagy_acct2.getAccountID().toString(), bhagy_acct2);
		bhagy.addAccount(bhagy_acct1);
		bhagy.addAccount(bhagy_acct2);

		// password: "1234"
		Customer christina = new Customer("Christina",
				"03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4");

		getCustomers().put(christina.getCustomerID().toString(), christina);
		Account christina_acct1 = new Account(christina.getCustomerID(), "Savings", "House", BigDecimal.valueOf(1500));
		getAccounts().put(christina_acct1.getAccountID().toString(), christina_acct1);
		christina.addAccount(christina_acct1);

		// password: "1111"
		Customer john = new Customer("John", "0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
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

	public HashMap<String, Transaction> getTransactions() {
		return transactions;
	}

	public static NewBank getBank() {
		return bank;
	}

}
