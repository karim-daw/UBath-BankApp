package se2.groupb.server.Account;

import se2.groupb.server.Customer.CustomerDTO;

//Business Logic: makes changes to Domain, sends results to Controller
public interface AccountService {

    /**
     * Creates new account for a given customer with a default account balance of
     * 0.0
     * 
     * NEWACCOUNT <Name>
     * e.g. NEWACCOUNT Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerDTO
     * @param requestInputs
     * @param openingBalance
     * @return string regarding success or failure of createtAccount request
     */
    String createAccount(CustomerDTO customerDTO, String[] requestInputs);

    /**
     * Creates a new account for a given customer
     * 
     * NEWACCOUNT <Name>
     * e.g. NEWACCOUNT Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerDTO
     * @param requestInputs
     * @param openingBalance
     * @return string regarding success or failure of createtAccount request
     */
    String createAccount(CustomerDTO customerDTO, String[] requestInputs, double openingBalance);

    /**
     * adds money to account
     * 
     * @param accountID
     * @param amount
     * @return
     */
    boolean deposit(Long accountID, double amount);

    /**
     * substracts money from account
     * 
     * @param accountID
     * @param amount
     * @return
     */
    boolean withdraw(Long accountID, double amount);

}