package se2.groupb.server;

public class Account {

	private String accountName;
	private double openingBalance = 0; // default

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
	
	public boolean isOverDrawn() {

		if (getAccountValue()<0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public double getBalance() {
		return openingBalance;
	}

	public void updateBalance(double amount) {
		this.openingBalance += amount;
	}
}
