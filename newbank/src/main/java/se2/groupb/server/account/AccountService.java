package se2.groupb.server.account;

import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerDTO;

import java.math.BigDecimal;
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