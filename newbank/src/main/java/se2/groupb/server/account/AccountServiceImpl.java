package se2.groupb.server.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import se2.groupb.server.repository.AccountRepositoryImpl;
import se2.groupb.server.repository.CustomerRepositoryImpl;

public class AccountServiceImpl implements AccountService {

	private final AccountRepositoryImpl accountRepository;
	private final CustomerRepositoryImpl customerRepository;
	
	Map<String, Integer> accountTypeLimits = Account.accountTypeLimits;

	// Constructor
	public AccountServiceImpl(AccountRepositoryImpl accountRepository,CustomerRepositoryImpl customerRepository) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;

	}

	// methods

	
	/**
	 * Returns Customer's UUID from their Account UUID
	 * @param accountID
	 * @return Customer UUID
	 */
	public UUID getCustomer(UUID accountID) {
		return accountRepository.findByID(accountID).getCustomerID();
	}
	
	/**
	 * The Customer's Accounts list
	 * Account list filtered
	 * @param customerID
	 * @return
	 */
	public ArrayList<Account> getAccounts(UUID customerID) {
		ArrayList<Account> accountList = customerRepository.findAccounts(customerID);
		return accountList;

	}
	
	public int noOfAccts(UUID customerID) {
		if (getAccounts(customerID)== null) {
			return 0;
		}
		return getAccounts(customerID).size(); 
	}
	
	/**
	 * The Customer's Accounts list filtered by Account Type
	 * @param customerID
	 * @param accountType
	 * @return
	 */
	public ArrayList<Account> getAccountsByType(UUID customerID, String accountType) {
		ArrayList<Account> accountList = customerRepository.findAccountsByType(customerID, accountType);
		return accountList;
	}
	
	
		
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
	public boolean hasAccount(UUID customerID, String accountType, String accountName) {
		ArrayList<Account> accountList = customerRepository.findAccountsByType(customerID, accountType);
		for (Account a : accountList) {
			if (a.getAccountName().equals(accountName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return the Account Balance if Customer has Account with the specified Account Number
	 * @param customerID
	 * @param accountNumber
	 * @return the Account Balance
	 */
	public Account getAccountByNumber(UUID customerID, String accountNumber) {
		return customerRepository.findAccountByNumber(customerID,accountNumber);
	}
	
	/**
     * Return true if Customer has Account with the specified Account Number
     * @param customerID
     * @param accountNumber
     * @return true if customer has account number, else false
     */
	public boolean hasAccountNumber(UUID customerID, String accountNumber) {
		if (getAccountByNumber(customerID,accountNumber)==null){
    		return false;
    	}
    	return true;
	}
	
	public boolean isOverdrawn(UUID customerID, String accountNumber) {
		if (hasAccountNumber(customerID,accountNumber)){
			return getAccountByNumber(customerID,accountNumber).isOverDrawn();
    	}
    	return true;
	}
	
	
	/**
	 * Return the Account Balance if Customer has Account with the specified Account Number
	 * @param customerID
	 * @param accountNumber
	 * @return the Account Balance
	 */
	public BigDecimal getAccountBalance(UUID customerID, String accountNumber) {
		if (hasAccountNumber(customerID,accountNumber)) {
			return getAccountByNumber(customerID,accountNumber).getBalance();
		}
		else {
			return null;
		}
	}
	
	
	/**
     * displays accounts as a list
     * 
     * @param customerID
     * @return String list of the Customer's Accounts
     */
    public String displayAccounts(UUID customerID) {
    
        if (getAccounts(customerID).isEmpty()) {
            return "You have no accounts to display.";
        } else {
        	String s = "";
    		for (Account a : getAccounts(customerID)) {
    			s += a.toString();
    		}
    		return s;
        }
    }
    
    
	// get the number of accounts of a specific Account Type for the Customer ID
	/**
	 * @param customerID
	 * @param accountType
	 * @return
	 */
	public Integer noAccountsByType(UUID customerID, String accountType) {
		ArrayList<Account> accountList = customerRepository.findAccountsByType(customerID, accountType);
		return accountList.size();
	}

	/**
	 * Returns a map of account types and whether the customer has reached the
	 * maximum
	 * number of accounts allowed for each type.
	 *
	 * @param customerId The unique identifier of the customer.
	 * @return A map of account types and whether the customer has reached the
	 *         maximum
	 *         number of accounts allowed for each type.
	 */
	public HashMap<String, Boolean> maxAccountsReached(UUID customerID) {

		// Create a new HashMap to hold the results.
		HashMap<String, Boolean> accountStatuses = new HashMap<>();

		// get availble accoun type limits

		// Loop through each account type and its associated limit.
		for (HashMap.Entry<String, Integer> accountTypeLimit : accountTypeLimits.entrySet()) {
			String accountType = accountTypeLimit.getKey(); // the account type
			Integer limit = accountTypeLimit.getValue(); // the max number of accounts allowed

			// Determine how many accounts the customer has for this account type.
			int numAccounts = noAccountsByType(customerID, accountType);

			// Check whether the customer has reached the maximum number of accounts for
			// this type.
			boolean isMaxReached = numAccounts == limit;

			// Add the result to the account statuses map.
			accountStatuses.put(accountType, isMaxReached);
		}
		return accountStatuses;
	}

	/**
	 * Returns a map of available account types for new account creation for the
	 * given customer.
	 *
	 * @param customerId The unique identifier of the customer.
	 * @return A map of available account types for new account creation for the
	 *         given customer.
	 */
	public HashMap<String, String> newAccountAvailableTypes(UUID customerID) {
		HashMap<String, String> map = new HashMap<>();
		int i = 0;

		for (HashMap.Entry<String, Boolean> acctType : maxAccountsReached(customerID).entrySet()) {
			String accountType = acctType.getKey();
			Boolean maxReached = acctType.getValue();
			if (!maxReached) {
				i++;
				String key = Integer.toString(i);
				map.put(key, accountType); // HashMap showing the available account types for new account creation
			}
		}
		return map;
	}

	/**
	 * 
	 * Creates a new account for a given customer
	 * NEWACCOUNT <Name>
	 * e.g. NEWACCOUNT Savings
	 * 
	 * @param customer
	 * @param requestInputs
	 * @param openingBalance
	 * @return Returns the created Account or null if somethign went wrong
	 */

	public Account createAccount(UUID customerID, AccountDTO accountDto) {
		Account newAccount = new Account(customerID, accountDto.getAccountType(), accountDto.getAccountName(),
				accountDto.getOpeningBalance());
		boolean success = accountRepository.save(newAccount);
		if (success) {
			return newAccount;
		}
		return null;
	}

}
