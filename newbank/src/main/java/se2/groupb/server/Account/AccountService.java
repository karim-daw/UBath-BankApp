package se2.groupb.server.Account;

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
            // check if the customer already have the account
            // account does not exist, continue to create a new account
            if (c.checkAccount(accountType) == false) {
                c.addAccount(new Account(accountType, openingBalance));
                return "SUCCESS: Your " + accountType + " account has been created.";
            } else {
                return "FAIL: You already have a " + accountType + " account.";
            }
        }
    }
}
