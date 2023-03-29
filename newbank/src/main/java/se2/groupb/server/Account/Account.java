package se2.groupb.server.Account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.nio.ByteBuffer;
import se2.groupb.server.NewBank;
import se2.groupb.server.Customer.CustomerDTO; //needed for the CustomerID
import se2.groupb.server.transaction.Transaction; //needed for displaying the transactions

//Account Domain
public class Account {
	private final UUID accountID;
	private final UUID customerID;
	private final String accountType;
	private String accountName; //editable by the customer
	private final String accountNumber;
	private static final String accountBIC = NewBank.BIC;
	private BigDecimal openingBalance = BigDecimal.ZERO; // default
	private BigDecimal overdraftLimit=BigDecimal.ZERO;// default
	private ArrayList<Transaction> transactions;

	//Constructor method for new Account object
	public Account(CustomerDTO customerDTO, String accountType, String accountName, String accountNumber,
			BigDecimal openingBalance, BigDecimal overdraftLimit) {
		this.accountID = UUID.randomUUID();
		this.customerID = customerDTO.getCustomerID();
		this.accountType = accountType;
		this.accountName = accountName;
		this.accountNumber = accountNumberGenerator();
		this.openingBalance = openingBalance;
		this.overdraftLimit = overdraftLimit;
		transactions = new ArrayList<>();
	}
	
	//generates unique 8-digit Account Number
	public String accountNumberGenerator() {
		while (true) {
			String n = UUID.randomUUID().toString();
			byte[] b = n.getBytes();
			Long l= ByteBuffer.wrap(b).asLongBuffer().get();
			if (l>0) {
				String number = Long.toString(l).substring(0,8);
				return number;
			}
			
		}
	}
	
	//Getters and Setters
	public String getAccountID() {
		return this.accountID.toString();
	}
	
	public String getCustomerID() {
		return this.customerID.toString();
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
		return this.openingBalance;
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
	
	public void credit(BigDecimal amount) {
		//TO DO: create a new transaction for the credit
		transactions.add(new Transaction("credit",amount));
		this.openingBalance.add(amount);
	}

	public void debit(BigDecimal amount) {
		//TO DO: create a new transaction for the debit
		transactions.add(new Transaction("debit",amount));
		this.openingBalance.subtract(amount);
	}
	
	public BigDecimal getOverdraftLimit() {
		return this.overdraftLimit;
	}
	
	public void setOverdraftLimit(BigDecimal newOverdraftLimit) {
		this.overdraftLimit= newOverdraftLimit;
	}
	
	//x.compareTo(y): returns 0 if x and y are equal, 1 if x is greater than y and -1 if x is smaller than y
	public boolean exceedsOverdraft() {
		if ((getBalance().compareTo(BigDecimal.ZERO)<0) && (getBalance().abs().compareTo(this.overdraftLimit)>0)){
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * checks if a deduction would result in the account exceeding pre-arranged overdraft
	 * 
	 * @param account
	 * @param deduction
	 * @return true or false if overdraft
	 */
	/*
	public boolean exceedsOverdraft(BigDecimal deduction) {
		BigDecimal availableBalance = this.openingBalance.add(this.overdraftLimit);
		
		if (availableBalance.compareTo(deduction) < 0) {
			return true;
		}
		return false;
	}
	*/
	
	//overrides default toString() method for Account objects
	public String toString() {
		//rounding balance down to 2 decimals
		BigDecimal balance = openingBalance.setScale(2, RoundingMode.FLOOR);
		int number= balance.toString().length(); //the number of characters in the balance
		String displayAccount = "\n" + displayChars('=',60)+ "\n" + accountName + "\n" + accountBIC + 
				displayChars(' ',3) + accountNumber + "\n" + displayChars(' ', 60-number) + balance +"\n"+
				displayChars('=',60);
		return displayAccount;
	}
	
	//Helper method for printing out the same characters multiple times
	public String displayChars(char myChar , int number) {
		char[] myChars = new char[number];
		Arrays.fill(myChars, myChar);
		return new String(myChars);
	}
	
	

	
}
