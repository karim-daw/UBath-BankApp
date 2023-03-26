package se2.groupb.server.customer;

import java.util.ArrayList;
import java.util.List;

import se2.groupb.server.account.Account;

public class Customer {

	// members

	private int customerID;
	private String username;
	private String password;
	private ArrayList<Account> accounts;
	private boolean loggedInStatus;

	// constructur

	public Customer() {
		accounts = new ArrayList<>();
	}

	// methods

	// customer id
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	// accounts list
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	// username
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @set a string as password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return a string of a password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @set a boolean as logged-in-status
	 */
	public void setloggedInStatus(boolean status) {
		this.loggedInStatus = status;
	}

	/**
	 * @return a Boolean of a loggedInStatus
	 */
	public boolean getloggedInStatus() {
		return this.loggedInStatus;
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
	 * checks if the desired accoutn name already exists in the customers list of
	 * accounts
	 * 
	 * @param accountName
	 * @return true false if account name exists in customers accounts
	 */
	public boolean checkAccount(String accountName) {
		for (Account account : accounts) {
			if (account.getAccountName().equals(accountName)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * method that changes the password
	 * old password need to be enter
	 * then a new password, twice
	 * 
	 * @param customer
	 * @param requestInputs
	 * @return
	 */

	public String changePassword(CustomerID customer, String[] requestInputs) {
		// check if the command is correct
		// return infinite loop of null, why ?
		int inputLength = requestInputs.length;
		if (inputLength < 4) {
			return "FAIL. Please enter your old password and twice your new password after the command.";
		}

		String oldPassword = requestInputs[1];
		String newPassword = requestInputs[2];
		String confirmNewPassword = requestInputs[3];
		Customer c = customers.get(customer.getKey());

		// check if the old password is correct
		if (!c.getPassword().equals(oldPassword)) {
			return "FAIL. The old password is incorrect.";
		}

		// check if the two new password inputs match.
		if (!newPassword.equals(confirmNewPassword)) {
			return "FAIL. Password confirmation does not match.";
		}

		else {
			c.setPassword(newPassword);
			return "SUCCESS new password is: " + c.getPassword();
		}
	}

}
