package se2.groupb.server.Account;

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

	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public double getBalance() {
		return openingBalance;
	}

	public void updateBalance(double amount) {
		this.openingBalance += amount;
	}

	/**
	 * checks if a deduction would result in teh account going overdraft
	 * 
	 * @param account
	 * @param deduction
	 * @return true or false if overdraftt
	 */
	private boolean isOverDraft(double deduction) {

		double balance = this.getBalance();
		if (deduction > balance) {
			return true;
		}
		return false;
	}
}
