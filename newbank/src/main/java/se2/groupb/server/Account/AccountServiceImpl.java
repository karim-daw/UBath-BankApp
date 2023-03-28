package se2.groupb.server.Account;

import se2.groupb.server.Customer.Customer;
import se2.groupb.server.Customer.CustomerDTO;
import se2.groupb.server.repository.AccountRepository;
import se2.groupb.server.repository.CustomerRepository;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private static final String main = "main";
    private static final String checking = "checking";
    private static final String savings = "savings";

    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
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
    public String createAccount(CustomerDTO customerDTO, String[] requestInputs, double openingBalance) {

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

}
