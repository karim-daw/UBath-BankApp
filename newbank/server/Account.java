package server;

public class Account {

	private String accountName;
	private double openingBalance=0; //default

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public Double getAccountValue() {
		return this.openingBalance;
	}

	public void changeBalance(Double transaction) {
		this.openingBalance += transaction;
	}

	public String toString() {
		return (accountName + ": " + openingBalance);
	}

}
