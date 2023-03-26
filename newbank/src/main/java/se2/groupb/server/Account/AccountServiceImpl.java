package se2.groupb.server.account;

import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerDTO;
import se2.groupb.server.repository.AccountRepository;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private static final String main = "main";
    private static final String checking = "checking";
    private static final String savings = "savings";

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

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
    @Override
    public String createAccount(CustomerDTO customerDTO, String[] requestInputs, double openingBalance) {

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

    @Override
    public boolean deposit(Long accountID, double amount) {

        // get the Account from db using id
        // create new transaction with amount
        //
        return false;
    }

    @Override
    public boolean withdraw(Long accountID, double amount) {
        return false;
    }

}