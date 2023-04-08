package se2.groupb.server.account;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import se2.groupb.server.NewBank;
import se2.groupb.server.transaction.Transaction; //needed for displaying the transactions

//Account Domain
public class Account {

	public static final List<String> validAccountTypes = Collections
			.unmodifiableList(Arrays.asList("Current", "Savings"));

	public static final Map<String, Integer> accountTypeLimits = Map.of("Current", 3, "Savings", 2);

	public static final Map<String, BigDecimal> defaultOverdraftLimits = Map.of("Current", BigDecimal.valueOf(200),
			"Savings", BigDecimal.ZERO);

	private final UUID accountID;
	private final UUID customerID;
	private final String accountType;
	private String accountName; // editable by the customer
	private final String accountNumber;
	private static final String accountBIC = NewBank.BIC;
	private BigDecimal currentBalance;
	private BigDecimal overdraftLimit;

	private ArrayList<Transaction> transactions;
	private static MathContext mc = new MathContext(3);

	// Constructor method for new Account object with default opening balance and
	// overdraft limit
	public Account(UUID customerID, String accountType, String accountName) {
		this.accountID = UUID.randomUUID();
		this.customerID = customerID;
		this.accountType = accountType;
		this.accountName = accountName;
		this.accountNumber = accountNumberGenerator();
		this.currentBalance = BigDecimal.ZERO;
		this.overdraftLimit = getOverdraftLimit();
		transactions = new ArrayList<>();
	}

	// Constructor method for new Account object
	public Account(UUID customerID, String accountType, String accountName, BigDecimal currentBalance) {
		this.accountID = UUID.randomUUID();
		this.customerID = customerID;
		this.accountType = accountType;
		this.accountName = accountName;
		this.accountNumber = accountNumberGenerator();
		this.currentBalance = currentBalance;
		this.overdraftLimit = getDefaultOverdraftLimit();
		transactions = new ArrayList<>();
	}

	// generates unique 8-digit Account Number
	private String accountNumberGenerator() {
		while (true) {
			String n = UUID.randomUUID().toString();
			byte[] b = n.getBytes();
			Long l = ByteBuffer.wrap(b).asLongBuffer().get();
			if (l > 0) {
				String number = Long.toString(l).substring(0, 8);
				return number;
			}
		}
	}

	public BigDecimal getDefaultOverdraftLimit() {

		if (this.accountType == "Current") {
			return BigDecimal.valueOf(200);
		} else {
			return BigDecimal.ZERO;
		}
	}

	// Getters and Setters
	public UUID getAccountID() {
		return this.accountID;
	}

	public UUID getCustomerID() {
		return this.customerID;
	}

	public boolean isOverDrawn() {

		BigDecimal balance = getBalance();
		BigDecimal zero = BigDecimal.ZERO;

		if (balance.compareTo(zero) == -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * add amount to account
	 * 
	 * @param amount
	 */
	public void deposit(BigDecimal amount) {
		// BigDecimal amountAsBigDecimal = BigDecimal.valueOf(amount);
		BigDecimal newBalance = currentBalance.add(amount);
		currentBalance = newBalance;
	}

	/**
	 * withdraw amount from account
	 * 
	 * @param amount
	 */
	public void withdraw(BigDecimal amount) {
		// BigDecimal amountAsBigDecimal = BigDecimal.valueOf(amount);
		BigDecimal newBalance = currentBalance.subtract(amount);
		currentBalance = newBalance;
	}

	public String getAccountType() {
		return this.accountType;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String newAccountName) {
		this.accountName = newAccountName;
	}

	public String getAccountNumber() {
		return this.accountNumber;
	}

	public String getAccountBIC() {
		return Account.accountBIC;
	}

	public BigDecimal getBalance() {
		return this.currentBalance;
	}

	/**
	 * adds transaction to list of transactions
	 * 
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	/**
	 * returns the array list of transaction
	 */
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	// public void credit(BigDecimal amount) {
	// // TO DO: create a new transaction for the credit
	// transactions.add(new Transaction("credit", amount));
	// this.openingBalance.add(amount);
	// }

	// public void debit(BigDecimal amount) {
	// // TO DO: create a new transaction for the debit
	// transactions.add(new Transaction("debit", amount));
	// this.openingBalance.subtract(amount);
	// }

	public BigDecimal getOverdraftLimit() {
		return this.overdraftLimit;
	}

	public void setOverdraftLimit(BigDecimal newOverdraftLimit) {
		this.overdraftLimit = newOverdraftLimit;
	}

	// overrides default toString() method for Account objects
	@Override
	public String toString() {
		// rounding balance down to 2 decimals
		BigDecimal balance = currentBalance.setScale(2, RoundingMode.FLOOR);
		int number = balance.toString().length(); // the number of characters in the balance

		String displayAccount = displayChars('=', 52) + "\n" 
				+ accountName + "(" + accountType + ")\n"
				+ accountBIC + displayChars(' ', 3) + accountNumber + "\n" + displayChars(' ', 52 - number) + balance + "\n"
				+ displayChars('=', 52)+"\n";
		return displayAccount;
	}

	// Helper method for printing out the same characters multiple times
	public String displayChars(char myChar, int number) {
		char[] myChars = new char[number];
		Arrays.fill(myChars, myChar);
		return new String(myChars);
	}

}
