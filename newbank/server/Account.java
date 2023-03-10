package newbank.server;

// this is karim doing a git commit test

public class Account {

	private String accountName;
	private double openingBalance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}

	public String toString() {
		return (accountName + ": " + openingBalance);
	}
	
	public String getAccount() {
		return this.accountName;
	}

    public double getBalance() {
        return openingBalance;
    }
    
	public void updateBalance(double amount){
		this.openingBalance += amount;
	}
}
