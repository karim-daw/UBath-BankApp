package se2.groupb.server.account;

import java.math.BigDecimal;
//import se2.groupb.server.repository.AccountRepository;
//import se2.groupb.server.repository.CustomerRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import se2.groupb.server.repository.AccountRepositoryImpl;

public class AccountServiceImpl implements AccountService {

	private final AccountRepositoryImpl accountRepository;

	Map<String, Integer> accountTypeLimits = Account.accountTypeLimits;

	// Constructor
	public AccountServiceImpl(AccountRepositoryImpl accountRepository) {
		this.accountRepository = accountRepository;
	}

	// methods

	/**
	 * get a list of Account objects by Customer ID
	 * 
	 * @param customerID
	 * @return
	 */
	public ArrayList<Account> getAccounts(UUID customerID) {
		ArrayList<Account> accountList = accountRepository.findAccounts(customerID);
		return accountList;

	}

	/**
	 * adds account to repo
	 * 
	 * @param accountID
	 * @return
	 */
	public UUID getCustomer(UUID accountID) {
		return accountRepository.findByID(accountID).getCustomerID();
	}

	/**
	 * get a list of Account objects by Customer ID and Account Type
	 * 
	 * @param customerID
	 * @param accountType
	 * @return
	 */
	public ArrayList<Account> getAccountsByType(UUID customerID, String accountType) {
		ArrayList<Account> accountList = accountRepository.findAccountsByType(customerID, accountType);
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
		ArrayList<Account> accountList = accountRepository.findAccountsByType(customerID, accountType);
		for (Account a : accountList) {
			if (a.getAccountName().equals(accountName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get the number of accounts of a specific Account Type for the Customer ID
	 * 
	 * @param customerID
	 * @param accountType
	 * @return
	 */
	public Integer noAccountsByType(UUID customerID, String accountType) {
		ArrayList<Account> accountList = accountRepository.findAccountsByType(customerID, accountType);
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

	/**
	 * adds money to account via the account number. This method will only return
	 * true if the accountNumber of the payee is a member of NewBank, if not itll be
	 * false
	 * 
	 * @param accountNumber
	 * @param amount
	 * @return
	 */
	@Override
	public boolean credit(String accountNumber, BigDecimal amount) {
		// Check if account number is in the system
		if (accountNumber == null || accountNumber.isEmpty()) {
			System.err.println("credit method received invalid account number");
			return false;
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.err.println("credit method received invalid amount");
			return false;
		}
		Account foundAccount = accountRepository.findByAccountNumber(accountNumber);
		if (foundAccount == null) {
			System.err.println("credit method could not find account");
			return false;
		}
		try {
			foundAccount.deposit(amount);
		} catch (Exception e) {
			System.err.println("deposit failed, returning false");
			return false;
		}
		boolean success = accountRepository.update(foundAccount);
		if (success) {
			return true;
		} else {
			System.err.println("account was found but updating the repo didnt work");
			return false;
		}
	}

	@Override
	public boolean debit(UUID accountID, BigDecimal amount) {
		if (accountID == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.err.println("debit method received invalid inputs");
			return false;
		}
		Account account = accountRepository.findByID(accountID);
		BigDecimal balance = account.getBalance();
		if (balance.compareTo(amount) < 0) {
			return false;
		}
		try {
			account.withdraw(amount);
		} catch (Exception e) {
			System.err.println("withdraw failed, returning false");
			return false;
		}
		return accountRepository.update(account);
	}

}
