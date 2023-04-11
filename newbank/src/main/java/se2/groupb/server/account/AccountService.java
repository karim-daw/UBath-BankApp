package se2.groupb.server.account;

import java.util.*;

//Business Logic: makes changes to Domain, sends results to Controller
public interface AccountService {

        /**
         * Creates a new account for a given customer
         * 
         * NEWACCOUNT <Name>
         * e.g. NEWACCOUNT Savings
         * Returns SUCCESS or FAIL
         * 
         * @param UUID
         * @param accountDto
         * @return string regarding success or failure of createtAccount request
         */
        Account createAccount(UUID customerID, AccountDTO accountDto);

        /*
         * a method that is responsibal for transfering money between two accounts for
         * the same customer
         * 
         * 
         */

        /**
         * 
         * 
         * @param customerID
         * @return
         */
        public HashMap<String, String> newAccountAvailableTypes(UUID customerID);

        /**
         * checks if the desired account name already exists in the customers list of
         * accounts by type
         * 
         * @param customerID
         * @param accountType
         * @param accountName
         * @return true false if account name exists in customers accounts of the
         *         specified type
         */
        public boolean hasAccount(UUID customerID, String accountType, String accountName);
        
        
        /**
         * displays accounts as a list
         * 
         * @param customerID
         * @return String list of the Customer's Accounts
         */
        public String displayAccounts(UUID customerID);
        
        
        /**
    	 * Return the Account Balance if Customer has Account with the specified Account Number
    	 * @param customerID
    	 * @param accountNumber
    	 * @return the Account Balance
    	 */
    	public Account getAccountByNumber(UUID customerID, String accountNumber);
    	
    	
        /**
         * Return true if Customer has Account with the specified Account Number
         * @param customerID
         * @param accountNumber
         * @return true if customer has account number, else false
         */
    	public boolean hasAccountNumber(UUID customerID, String accountNumber);
    	
    	/**
    	 * 
    	 * @param customerID
    	 * @param accountNumber
    	 * @return
    	 */
    	public boolean isOverdrawn(UUID customerID, String accountNumber);
    	
    	public int noOfAccts(UUID customerID);
    	
        
        /**
         * adds money to account
         * 
         * @param accountID
         * @param amount
         * @return
         */
        // boolean credit(UUID accountID, BigDecimal amount);

        /**
         * subtracts money from account
         * 
         * @param accountID
         * @param amount
         * @return
         */
        // boolean debit(UUID accountID, BigDecimal amount);

        // boolean exceedsOverdraft() ;

}