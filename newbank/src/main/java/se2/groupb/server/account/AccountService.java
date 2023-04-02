package se2.groupb.server.account;

import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerDTO;

import java.math.BigDecimal;
import java.util.*;

//Business Logic: makes changes to Domain, sends results to Controller
public interface AccountService {
	
	public static final List<String> validAccountTypes = 
			Collections.unmodifiableList(Arrays.asList("Current","Savings"));
	
	public static final Map<String, Integer> accountTypeLimits = Map.of("Current", 3, "Savings", 2);
	
	public static final Map<String, BigDecimal> defaultOverdraftLimits = Map.of("Current", BigDecimal.valueOf(200), 
			"Savings", BigDecimal.ZERO);
			
	
	
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
    //boolean createAccount(UUID customerID, String accountType, String accountName, BigDecimal openingBalance);
    
    
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