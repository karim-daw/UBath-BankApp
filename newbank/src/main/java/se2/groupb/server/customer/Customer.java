package se2.groupb.server.customer;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import se2.groupb.server.account.Account;
import se2.groupb.server.Payee.Payee;
import se2.groupb.server.loanOffer.LoanOffer;
import se2.groupb.server.loan.Loan;

//Customer Domain
public class Customer {

	// members
	private final UUID customerID;
	private String username;
	private String password;
	private ArrayList<Account> accounts;
	private ArrayList<Payee> payees;
	private boolean loggedInStatus;
	private Integer creditScore;
	private ArrayList<LoanOffer> loanOffers;
	private ArrayList<Loan> loans;

	//public static final Map<String, String> creditScoresOld = Map.of("1", "Poor", "2", "Fair", "3", "Good", "4", "Very Good", "5", "Excellent");
	
	public static Map<String, String> creditScores;
	static{
		creditScores = new TreeMap<>();
		creditScores.put("1","Poor");
		creditScores.put("2","Fair");
		creditScores.put("3","Good");
		creditScores.put("4","Very Good");
		creditScores.put("5","Excellent");
	}
	    
	// constructor 1
	public Customer(String username, String password) {
		this.customerID = UUID.randomUUID();
		this.username = username;
		this.password = password;
		this.loggedInStatus = false;
		this.creditScore = 3; //default credit score
		accounts = new ArrayList<>();
		payees = new ArrayList<>();
		loanOffers = new ArrayList<>();
		loans = new ArrayList<>();
	}

	// constructor 2
	public Customer(CustomerDTO customerDto) {
		this.customerID = UUID.randomUUID();
		this.username = customerDto.getUsername();
		this.password = customerDto.getPassword();
		this.loggedInStatus = false;
		this.creditScore = 3; //default credit score
		accounts = new ArrayList<>();
		payees = new ArrayList<>();
		loanOffers = new ArrayList<>();
		loans = new ArrayList<>();
	}

	// Customer methods

	// customer id
	public UUID getCustomerID() {
		return customerID;
	}

	// username
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return a string of a password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @set a string as password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return a Boolean of a loggedInStatus
	 */
	public boolean getloggedInStatus() {
		return this.loggedInStatus;
	}

	/**
	 * @set a boolean as logged-in-status
	 */
	public void setloggedInStatus(boolean status) {
		this.loggedInStatus = status;
	}
	
	/**
	 * Gets the customer's credit score
	 * @return Integer
	 */
	public Integer getCreditScore() {
		return this.creditScore;
	}

	/**
	 * Changes the Customer's credit score
	 * @param score
	 */
	public void setCreditScore(Integer score) {
		this.creditScore = score;
	}
	
	
	//Account Methods:
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	/**
	 * Returns the Customer's accounts filtered by Account Type: Current, Savings
	 * @param accountType
	 * @return list of Account
	 */
	public ArrayList<Account> getAccountsByType(String accountType) {
		ArrayList<Account> accountsByType = new ArrayList<>();
		for (Account a : accounts) {
			if (a.getAccountType().equals(accountType)) {
				accountsByType.add(a);
			}
		}
		return accountsByType;
	}

	/**
	 * Returns a string of customer's accounts list for display
	 * @return a String
	 */
	public String accountsToString() {
		String s = "";
		for (Account a : accounts) {
			s += a.toString();
		}
		return s;
	}
	
	/**
	 * display the accounts content into a list
	 * 
	 * @return a list containing Accounts
	 */
	public List<String> accountsToList() {
		ArrayList<String> l = new ArrayList<String>();
		for (Account a : accounts) {
			l.add(a.toString());
		}
		return l;
	}

	/**
	 * display the account names only into a list
	 * 
	 * @return a list containing Account Names
	 */
	public List<String> acctTypesToList() {
		ArrayList<String> l = new ArrayList<String>();
		for (Account a : accounts) {
			l.add(a.getAccountName());
		}
		return l;
	}

	/**
	 * adds account to list of accounts
	 * 
	 * @param account
	 */
	public void addAccount(Account account) {
		accounts.add(account);
	}
	
	/**
	 * @param accountName
	 * @return
	 */
	public Account getAccountByName(String accountName) {
		for (Account account : accounts) {
			if (account.getAccountName().equals(accountName)) {
				return account;
			}
		}
		return null;
	}

	/**
	 * @return a string of an account
	 */

	/**
	 * checks if the desired account name already exists in the customers list of
	 * accounts
	 * 
	 * @param accountName
	 * @return true false if account name exists in customers accounts
	 */
	public boolean hasAccount(String accountName) {
		for (Account account : accounts) {
			if (account.getAccountName().equals(accountName)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * map of source accounts: non-overdrawn accounts & their balances
	 * 
	 * @return a map containing numbered Accounts & Balances
	 */

	public Map<String, Account> sourceAcctsMap() {
		Map<String, Account> map = new TreeMap<>();
		int i = 0;
		for (Account a : accounts) {
			if (!(a.isOverDrawn())) {
				i++;
				String key = Integer.toString(i);
				map.put(key, a);
			}
		}
		return map;
	}
	
	/**
	 * map of options for destination accounts
	 * 
	 * @return a map containing numbered Accounts
	 */
	public Map<String, Account> destinationAcctsMap(UUID accountID) {
		Map<String, Account> map = new TreeMap<>();
		int i = 0;
		for (Map.Entry<String, Account> entry : sourceAcctsMap().entrySet()) {
			if (!entry.getValue().getAccountID().equals(accountID)) {
				i++;
				String key = Integer.toString(i);
				map.put(key, entry.getValue());
			}
		}
		return map;
	}
	
	
	/**
	 * @return a string of the new account options map
	 */
	public String accountMapToString(Map<String, Account> map) {
		String s = "";
		for (Map.Entry<String, Account> item : map.entrySet()) {
			s += item.getKey() + " = " + item.getValue().toString() + "\n";
		}
		return s;
	}
	
	
	
	
	
	
	//Loan Offer Methods:
	
	/**
	 * Gets the Customer's loan offers
	 * @return
	 */
	public ArrayList<LoanOffer> getLoanOffers() {
		return loanOffers;
	}
	
	/**
	 * adds Loan Offer to the Customer's list of offers
	 * 
	 * @param account
	 */
	public void addLoanOffer(LoanOffer offer) {
		loanOffers.add(offer);
	}
	
	
	
	
	// Loan Methods:
	/**
	 * Gets the Customer's loans
	 * @return
	 */
	public ArrayList<Loan> getLoans() {
		return loans;
	}
	
	/**
	 * adds Loan to the Customer's list of loans
	 * 
	 * @param account
	 */
	public void addLoan(Loan loan) {
		loans.add(loan);
	}
	
	/**
	 * Returns a string of customer's loans list for display
	 * @return a string
	 */
	public String loansToString() {
		String s = "";
		for (Loan l : loans) {
			s += l.toString();
		}
		return s;
	}
	
	
	
	
	

	/**
	 * @return a string of the new account options map
	 */
	public String mapToString(HashMap<String, String> map) {
		String s = "";
		for (HashMap.Entry<String, String> item : map.entrySet()) {
			s += item.getKey() + " = " + item.getValue() + "\n";
		}
		return s;
	}
	
	@Override
	public String toString() {
		return customerID.toString() + " " +username;
	}
	
	
	// Payee Methods
	/*
	 * @return payees
	 * TODO Set the list in alphabetical order
	 */

	public ArrayList<Payee> getPayees() {
		return payees;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, Payee> payeesMap() {
		Map<String, Payee> map = new TreeMap<>();
		int i = 0;
		for (Payee payee : payees) {
			i++;
			String key = Integer.toString(i);
			map.put(key, payee);
		}
		return map;
	}
	
	/**
	 * @return a string of the Payee map
	 */
	public String payeeMapToString(Map<String, Payee> map) {
		String s = "";
		for (Map.Entry<String, Payee> item : map.entrySet()) {
			s += item.getKey() + " = " + item.getValue().toString() + "\n";
		}
		return s;
	}
	
	
	/**
	 * @return a string of a payee
	 */

	public String payeesToString() {
		String s = "";
		for (Payee p : payees) {
			s += p.toString();
		}
		return s;
	}

	/**
	 * display the payees content into a list
	 * 
	 * @return a list containing Payees
	 */
	public List<String> payeesToList() {
		ArrayList<String> l = new ArrayList<String>();
		for (Payee p : payees) {
			l.add(p.toString());
		}
		return l;
	}

	/**
	 * adds a payee to the list of payees
	 * 
	 * @param payee
	 */
	public void addPayee(Payee payee) {
		payees.add(payee);
	}

	/**
	 * @param payeeName
	 * @return
	 */
	public Payee getPayeesByName(String payeeName) {
		for (Payee payee : payees) {
			if (payee.getPayeeName().equals(payeeName)) {
				return payee;
			}
		}
		return null;
	}

	/**
	 * checks if the desired payee name already exists in the customers list of
	 * accounts
	 * 
	 * @param payeeName
	 * @return true false if payee name exists in customers accounts
	 */
	public boolean alreadyExists(String payeeName) {
		for (Payee payee : payees) {
			if (payee.getPayeeName().equals(payeeName)) {
				return true;
			}
		}
		return false;

	}

}
