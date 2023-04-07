package se2.groupb.server.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import se2.groupb.server.account.Account;
import se2.groupb.server.account.Payee;

//Customer Domain
public class Customer {

	// members
	private final UUID customerID;
	private String username;
	private String password;
	private ArrayList<Account> accounts;
	private ArrayList<Payee> payees;
	private boolean loggedInStatus;

	// constructor 1
	public Customer(String username, String password) {
		this.customerID = UUID.randomUUID();
		this.username = username;
		this.password = password;
		this.loggedInStatus = false;
		accounts = new ArrayList<>();
		payees = new ArrayList<>();
	}

	// constructor 2
	public Customer(CustomerDTO customerDto) {
		this.customerID = UUID.randomUUID();
		this.username = customerDto.getUsername();
		this.password = customerDto.getPassword();
		this.loggedInStatus = false;
		accounts = new ArrayList<>();
		payees = new ArrayList<>();
	}

	// methods

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

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

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
	 * @return a string of an account
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

	public HashMap<String, String> sourceAcctsMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		int i = 0;
		for (Account a : accounts) {
			if (!(a.isOverDrawn())) {
				i++;
				String key = Integer.toString(i);
				map.put(key, a.toString());
			}
		}
		return map;
	}

	/**
	 * map of options for new account names
	 * 
	 * @return a map containing numbered Account Names
	 */
	public HashMap<String, String> destinationAcctsMap(String sourceAcct) {
		HashMap<String, String> map = new HashMap<String, String>();
		int i = 0;
		for (Account a : accounts) {
			if (!(a.getAccountName().equals(sourceAcct))) {
				i++;
				String key = Integer.toString(i);
				map.put(key, a.toString());
			}
		}
		return map;
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

	/*
	 * @return payees
	 * TODO Set the list in alphabetical order
	*/

	public ArrayList<Payee> getPayees() {
		return payees;
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
	 * @return a string of an account
	 */

	/**
	 * checks if the desired account name already exists in the customers list of
	 * accounts
	 * 
	 * @param payeeName
	 * @return true false if account name exists in customers accounts
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
