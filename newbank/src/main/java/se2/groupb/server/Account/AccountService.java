package se2.groupb.server.account;

import se2.groupb.server.customer.CustomerDTO;

public interface AccountService {

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