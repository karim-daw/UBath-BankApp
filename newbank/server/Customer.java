package newbank.server;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	
	private ArrayList<Account> accounts;
	
	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}
	//display the accounts content into a list
	public List<String> accountsToList() {
		ArrayList<String> l = new ArrayList<String>() {
			
		};
		for(Account a : accounts) {
			l.add(a.toString());
		}
		return l;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}
}
