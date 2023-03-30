package se2.groupb.server.Account;

import java.math.BigDecimal;
import java.util.*;

import se2.groupb.server.Customer.Customer;
import se2.groupb.server.Customer.CustomerDTO;
import se2.groupb.server.repository.AccountRepository;
import se2.groupb.server.repository.CustomerRepository;

public class AccountServiceImpl implements AccountService{
    /*
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }
    */
    
	private UUID CustomerID;
	private HashMap<AccountDTO, Account> theAccounts;
    public AccountServiceImpl(UUID CustomerID, HashMap<AccountDTO, Account> accounts) {
    	this.CustomerID = CustomerID;
        this.theAccounts = accounts;
        
    }
    
    @Override
    public boolean createAccount(CustomerDTO customer, String accountType, String accountName) {
        // Generate an account id.
        Account newAccount = new Account(customer.getCustomerID(), accountType, accountName);

        theAccounts.put(newAccount.getAccountID(), newAccount);

        return newAccount;
    }
    
    /**
     * Creates a new account for a given customer
     * NEWACCOUNT <Name>
     * e.g. NEWACCOUNT Savings
     * 
     * @param customer
     * @param requestInputs
     * @param openingBalance
     * @return Returns SUCCESS or FAIL
     */
    @Override
    public boolean createAccount(CustomerDTO customer, String accountType, String accountName, BigDecimal accountBalance,
    		BigDecimal overdraftLimit) {
        // Generate an account id.
        AccountDTO newAccount = new Account(aCustomerId, myAccountId, anAccountName, aBalance);

        theAccounts.put(accountID, newAccount);

        return newAccount;
    }

    /*
    @Override
    public boolean credit(UUID accountID, double amount) {

        // get the Account from db using id
        // create new transaction with amount
        //
        return false;
    }

    @Override
    public boolean debit(UUID accountID, double amount) {
        return false;
    }

    @Override
    public String createAccount(CustomerDTO customerDTO, String[] requestInputs) {

        double openingBalance = 0.0;
        // validate inputs
        int inputLength = requestInputs.length;
        if (inputLength < 2) {
            return "FAIL: Account type not specified";
        }

        String accountType = requestInputs[1];
        if (!accountType.equals(main) && !accountType.equals(checking) && !accountType.equals(savings)) {
            return "FAIL: Account type not recognised";
        } else {
            Customer customer = customerRepository.findByCustomerID(customerDTO.getCustomerID());

            // check if accounts exists if not, create a new account
            if (customer.hasAccount(accountType) == false) {
                Account newAccount = new Account(accountType, openingBalance);
                customer.addAccount(newAccount);

                // print success message
                return "SUCCESS: Your " + accountType + " account has been created.";
            } else {
                return "FAIL: You already have a " + accountType + " account.";
            }
        }
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
  	*/
    
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
}
