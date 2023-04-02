package se2.groupb.server.account;

import java.util.*;
//import se2.groupb.server.repository.AccountRepository;
//import se2.groupb.server.repository.CustomerRepository;

public class AccountServiceImpl implements AccountService {
	/*
	 * 
	 * private final AccountRepository accountRepository;
	 * private final CustomerRepository customerRepository;
	 * public AccountServiceImpl(AccountRepository accountRepository,
	 * CustomerRepository customerRepository) {
	 * this.accountRepository = accountRepository;
	 * this.customerRepository = customerRepository;
	 * }
	 */

	// attributes
	private HashMap<String, Account> theAccounts;
	// private HashMap<String, Customer> theCustomers;

	// Constructor
	public AccountServiceImpl(HashMap<String, Account> accounts) {
		// this.theCustomers = customers;
		this.theAccounts = accounts;
	}

	// methods

	// get a list of Account objects by Customer ID
	/**
	 * @param customerID
	 * @return
	 */
	public ArrayList<Account> getAccounts(UUID customerID) {
		ArrayList<Account> l = new ArrayList<>();
		for (HashMap.Entry<String, Account> record : theAccounts.entrySet()) {
			UUID recordCustomerID = record.getValue().getCustomerID();
			if (recordCustomerID.equals(customerID)) {
				l.add(record.getValue());
			}
		}
		return l;
	}

	// get a list of Account objects by Customer ID and Account Type
	/**
	 * @param customerID
	 * @param accountType
	 * @return
	 */
	public ArrayList<Account> getAccountsByType(UUID customerID, String accountType) {
		ArrayList<Account> l = new ArrayList<>();
		for (Account a : getAccounts(customerID)) {
			if (a.getAccountType().equals(accountType)) {
				l.add(a);
			}
		}
		return l;
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
		for (Account a : getAccountsByType(customerID, accountType)) {
			if (a.getAccountName().equals(accountName)) {
				return true;
			}
		}
		return false;
	}

	// get the number of accounts of a specific Account Type for the Customer ID
	/**
	 * @param customerID
	 * @param accountType
	 * @return
	 */
	public Integer noAccountsByType(UUID customerID, String accountType) {
		return getAccountsByType(customerID, accountType).size();
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

	/*
	 * public ArrayList<String> getAccountNamesByType(UUID customerID,String
	 * accountType) {
	 * ArrayList<String> accountNames = new ArrayList<>();
	 * 
	 * for (Account a : accounts) {
	 * if (a.getAccountType().equals(accountType)) {
	 * accountsByType.add(a);
	 * }
	 * }
	 * return accountNames;
	 * }
	 */

	/**
	 * 
	 * Creates a new account for a given customer
	 * NEWACCOUNT <Name>
	 * e.g. NEWACCOUNT Savings
	 * 
	 * @param customer
	 * @param requestInputs
	 * @param openingBalance
	 * @return Returns SUCCESS or FAIL
	 */

	public Account createAccount(UUID customerID, AccountDTO accountDto) {
		Account newAccount = new Account(customerID, accountDto.getAccountType(), accountDto.getAccountName(),
				accountDto.getOpeningBalance());
		theAccounts.put(newAccount.getAccountID().toString(), newAccount);

		return newAccount;
	}

	/*
	 * 
	 * @Override
	 * public boolean credit(UUID accountID, double amount) {
	 * 
	 * // get the Account from db using id
	 * // create new transaction with amount
	 * //
	 * return false;
	 * }
	 */

	/*
	 * @Override
	 * public boolean debit(UUID accountID, double amount) {
	 * return false;
	 * }
	 */

	/*
	 * public boolean exceedsOverdraft() {
	 * if ((getBalance().compareTo(BigDecimal.ZERO)<0) &&
	 * (getBalance().abs().compareTo(this.overdraftLimit)>0)){
	 * return true;
	 * }
	 * else {
	 * return false;
	 * }
	 * }
	 */

	/**
	 * checks if a deduction would result in the account exceeding pre-arranged
	 * overdraft
	 * 
	 * @param account
	 * @param deduction
	 * @return true or false if overdraft
	 */
	/*
	 * public boolean exceedsOverdraft(BigDecimal deduction) {
	 * BigDecimal availableBalance = this.openingBalance.add(this.overdraftLimit);
	 * 
	 * if (availableBalance.compareTo(deduction) < 0) {
	 * return true;
	 * }
	 * return false;
	 * }
	 */

}
