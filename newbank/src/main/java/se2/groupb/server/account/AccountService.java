package se2.groupb.server.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

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
        public Account createAccount(UUID customerID, AccountDTO accountDto);

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
         * adds money to account via the account number. This method will only return
         * true if the accountNumber of the payee is a member of NewBank, if not itll be
         * false
         * 
         * @param accountNumber
         * @param amount
         * @return
         */
        public boolean credit(String accountNumber, BigDecimal amount);

        /**
         * subtracts money from account
         * 
         * @param accountID
         * @param amount
         * @return
         */
        public boolean debit(UUID accountID, BigDecimal amount);

}