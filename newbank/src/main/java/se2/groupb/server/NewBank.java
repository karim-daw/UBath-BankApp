package se2.groupb.server;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

import se2.groupb.server.Payee.Payee;
import se2.groupb.server.account.Account;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.loan.Loan;
import se2.groupb.server.loanOffer.LoanOffer;
import se2.groupb.server.transaction.Transaction;


public class NewBank {

	public static final String BIC = "NEWBGB21";
	private static final NewBank bank = new NewBank(); // every instance of NewBank has the same bank info

	
	private HashMap<String, Customer> customers; //temp customer data store
	private HashMap<String, Account> accounts; //temp account data store
	private HashMap<String, Transaction> transactions;
	private HashMap<String, Payee> payees;
	private HashMap<String, LoanOffer> loanMarket; //temp loan offer data store
	private HashMap<String, Loan> loans; //temp loan offer data store
	
	// Constructor
	private NewBank() {
		// create temp data store
		customers = new HashMap<>();
		accounts = new HashMap<>();
		transactions = new HashMap<>();
		payees = new HashMap<>();
		loans = new HashMap<>();
		loanMarket = new HashMap<>();
		
		// adding data for debugging
		addTestData();
	}

	/**
	 * debugging helper function that adds dummy data to a hashmap
	 */
	private void addTestData() {
		// password: "password" = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"
		Customer bhagy = new Customer("Bhagy", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
		
		Account bhagy_acct1 = new Account(bhagy.getCustomerID(), "Current", "Main", BigDecimal.valueOf(20000));
		Account bhagy_acct2 = new Account(bhagy.getCustomerID(), "Savings", "Car", BigDecimal.valueOf(30000));
		bhagy.addAccount(bhagy_acct1);
		bhagy.addAccount(bhagy_acct2);
		
		Payee bhagy_payee1 = new Payee(bhagy.getCustomerID(), "Jean Doe", "012345", "OTHBAN");
		Payee bhagy_payee2 = new Payee(bhagy.getCustomerID(), "Robert Ham", "678910", "OTHBAN");
		
		bhagy.addPayee(bhagy_payee1);
		bhagy.addPayee(bhagy_payee2);
		
		LoanOffer bhagy_offer1 = new LoanOffer(bhagy.getCustomerID(), bhagy.getUsername(), "Offer1",bhagy_acct1.getAccountID(), 
				BigDecimal.valueOf(10000), BigDecimal.valueOf(15), 1, "Years", 12, "Good");
		LoanOffer bhagy_offer2 = new LoanOffer(bhagy.getCustomerID(), bhagy.getUsername(), "Offer2",bhagy_acct2.getAccountID(), 
				BigDecimal.valueOf(20000), BigDecimal.valueOf(10), 2, "Years", 24, "Good");
		bhagy.addLoanOffer(bhagy_offer1);
		bhagy.addLoanOffer(bhagy_offer2);
		
		//Add Bhagy's Customer, Accounts and Payees to Database:
		getCustomers().put(bhagy.getCustomerID().toString(), bhagy);
		getAccounts().put(bhagy_acct1.getAccountID().toString(), bhagy_acct1);
		getAccounts().put(bhagy_acct2.getAccountID().toString(), bhagy_acct2);
		getPayees().put(bhagy_payee1.getPayeeID().toString(), bhagy_payee1);
		getPayees().put(bhagy_payee2.getPayeeID().toString(), bhagy_payee2);
		getLoanMarket().put(bhagy_offer1.getLoanOfferID().toString(), bhagy_offer1);
		getLoanMarket().put(bhagy_offer2.getLoanOfferID().toString(), bhagy_offer2);
		
		// password: "1234" = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4"
		Customer christina = new Customer("Christina",
				"03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4");
		Account christina_acct1 = new Account(christina.getCustomerID(), "Savings", "House", BigDecimal.valueOf(50000));
		christina.addAccount(christina_acct1);
		LoanOffer christina_offer1 = new LoanOffer(christina.getCustomerID(), christina.getUsername(), "Offer1",
				christina_acct1.getAccountID(), BigDecimal.valueOf(10000), BigDecimal.valueOf(10), 1, "Years", 12, "Good");
		christina.addLoanOffer(christina_offer1);
		
		Payee bhagy_payee3 = new Payee(bhagy.getCustomerID(), "Christina", christina_acct1.getAccountNumber(), "NEWBGB21");
		bhagy.addPayee(bhagy_payee3);
		getPayees().put(bhagy_payee3.getPayeeID().toString(), bhagy_payee3);
		
		//Add Christina's Customer, Accounts and Payees to Database:
		getCustomers().put(christina.getCustomerID().toString(), christina);
		getAccounts().put(christina_acct1.getAccountID().toString(), christina_acct1);
		getLoanMarket().put(christina_offer1.getLoanOfferID().toString(), christina_offer1);
		
		// password: "1111" = "0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c"
		Customer john = new Customer("John", "0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c");
		Account john_acct1 = new Account(john.getCustomerID(), "Current", "Main", BigDecimal.valueOf(250));
		john.addAccount(john_acct1);
		getCustomers().put(john.getCustomerID().toString(), john);
		getAccounts().put(john_acct1.getAccountID().toString(), john_acct1);
		
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

	public HashMap<String, Payee> getPayees() {
		return payees;
	}
	public HashMap<String, LoanOffer> getLoanMarket() {
		return loanMarket;
	}
	
	public HashMap<String, Loan> getLoans() {
		return loans;
	}

	public static NewBank getBank() {
		return bank;
	}
	
	/*
	public void displayCustomers() {
		System.out.println(getCustomers().keySet());
	}
	*/
}