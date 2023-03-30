package se2.groupb.server.Account;

import se2.groupb.server.Customer.CustomerDTO;

import java.math.BigDecimal;
import java.util.*;

//Business Logic: makes changes to Domain, sends results to Controller
public interface AccountService {
	public static final List<String> validAccountTypes = 
			Collections.unmodifiableList(Arrays.asList("Current","Savings"));
	public static final List<BigDecimal> defaultOverdraftLimits = 
			Collections.unmodifiableList(Arrays.asList(BigDecimal.valueOf(200),BigDecimal.ZERO));
	
    /**
     * Creates new account for a given customer with a default account balance and overdraft limit of
     * 0.0
     * 
     * NEWACCOUNT <Name>
     * e.g. NEWACCOUNT Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerDTO
     * @param accountType
     * @param accountName
     * @return string regarding success or failure of createtAccount request
     */
    boolean createAccount(CustomerDTO customer, String accountType, String accountName);

    /**
     * Creates a new account for a given customer
     * 
     * NEWACCOUNT <Name>
     * e.g. NEWACCOUNT Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerDTO
     * @param accountType
     * @param accountName
     * @param openingBalance
     * @param overdraftLimit
     * @return string regarding success or failure of createtAccount request
     */
    boolean createAccount(CustomerDTO customer, String accountType, String accountName, BigDecimal openingBalance,
    		BigDecimal overdfraftLimit);

    /**
     * adds money to account
     * 
     * @param accountID
     * @param amount
     * @return
     */
    //boolean credit(UUID accountID, BigDecimal amount);

    /**
     * subtracts money from account
     * 
     * @param accountID
     * @param amount
     * @return
     */
    //boolean debit(UUID accountID, BigDecimal amount);
    
    //boolean exceedsOverdraft() ;
    

}