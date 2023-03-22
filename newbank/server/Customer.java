package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Customer {
	
	
	private ArrayList<Account> accounts;
	private String password;
	private boolean loggedInStatus;

	public Customer() {
		accounts = new ArrayList<>();
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
	 * @return a string of all the customer's accounts and their balances
	 */

	public String accountsToString() {
		String s = "";
		for (Account a : accounts) {
			s += a.toString()+"\n";
		}
		return s;
	}

	/**
	 * display the account names & balances into a list
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
	 * map of source accounts: non-overdrawn accounts & their balances
	 * 
	 * @return a map containing numbered Accounts & Balances
	 */
	public HashMap<String,String> sourceAcctsMap(){
		HashMap<String,String> map = new HashMap<String,String>();
		int i=0;
		for (Account a : accounts) {
			if (!(a.isOverDrawn())) {
				i++;
				String key=Integer.toString(i);
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
	public HashMap<String,String> destinationAcctsMap(String sourceAcct){
		HashMap<String,String> map = new HashMap<String,String>();
		int i=0;
		for (Account a : accounts) {
			if (!(a.getAccountName().equals(sourceAcct))) {
				i++;
				String key=Integer.toString(i);
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
		for (HashMap.Entry<String,String> item: map.entrySet()){
	        s += item.getKey()+ " = "+ item.getValue()+"\n";
	    }
		return s;
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
	 * @return list of accounts
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
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

	public boolean checkAccount(String accountName) {
		for (Account account : accounts) {
			if (account.getAccountName().equals(accountName)) {
				return true;
			}
		}
		return false;

	}
	
	/**
	 * map of options for new account names
	 * 
	 * @return a map containing numbered Account Names 
	 */
	public HashMap<String,String> newAcctTypes(){
		HashMap<String,String> map = new HashMap<String,String>();
		int i=0;
		for (String a : NewBank.validAcctList) {
			if (!acctTypesToList().contains(a)) {
				i++;
				String key=Integer.toString(i);
				map.put(key, a); //HashMap showing the available account types for new account creation
			}
		}
		return map;
	}
	
	
}
