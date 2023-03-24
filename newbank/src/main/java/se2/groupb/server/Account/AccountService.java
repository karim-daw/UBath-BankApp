package se2.groupb.server.account;

import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerID;

public class AccountService {

    /**
     * Creates a new account for a given customer
     * 
     * NEWACCOUNT <Name>
     * e.g. NEWACCOUNT Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customer
     * @param requestInputs
     * @param openingBalance
     * @return string regarding success or failure of createtAccount request
     */
    private String createAccount(CustomerID customer, String[] requestInputs, double openingBalance) {

        int inputLength = requestInputs.length;
        if (inputLength < 2) {
            return "FAIL: Account type not specified";
        }

        String accountType = requestInputs[1];
        if (!accountType.equals(main) && !accountType.equals(checking) && !accountType.equals(savings)) {
            return "FAIL: Account type not recognised";
        } else {
            Customer c = customers.get(customer.getKey());

            // check if accounts exists if not, create a new account
            if (c.checkAccount(accountType) == false) {

                // create new account with open balance and add it
                Account newAccount = new Account(accountType, openingBalance);
                c.addAccount(newAccount);

                // print success message
                return "SUCCESS: Your " + accountType + " account has been created.";
            } else {
                return "FAIL: You already have a " + accountType + " account.";
            }
        }
    }
}
