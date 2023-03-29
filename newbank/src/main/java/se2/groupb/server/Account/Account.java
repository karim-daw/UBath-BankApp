package se2.groupb.server.account;

public class Account {
	private String accountName;
	private double openingBalance = 0; // default

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}

	// TODO: #28 add customer id as attribute for account class
	public String getAccountName() {
		return this.accountName;
	}

	public double getBalance() {
		return openingBalance;
	}

	public boolean isOverDrawn() {

		if (getBalance() < 0) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	// public void updateBalance(BigDecimal amount) {
	// this.openingBalance += amount;
	// }

	public void deposit(double amount) {
		openingBalance += amount;
	}

	public void withdraw(double amount) {
		openingBalance -= amount;
	}

	/**
	 * checks if a deduction would result in the account going overdraft
	 * 
	 * @param account
	 * @param deduction
	 * @return true or false if overdraft
	 */
	public boolean isOverDraft(double deduction) {

		double balance = this.getBalance();
		if (deduction > balance) {
			return true;
		}
		return false;
	}
}
