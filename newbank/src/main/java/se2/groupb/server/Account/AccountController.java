package se2.groupb.server.Account;

import se2.groupb.server.Customer.CustomerDTO;

// Presentation layer: Takes user inputs and displays system response
public class AccountController {
    // fields
    private final AccountService accountService;

    // constructor
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // methods

    /**
     * creates a new account in the users sets of accounts
     * 
     * @param customerDTO
     * @param requestInputs
     * @param openingBalance
     * @return string message of success of failusre
     */
    public String createNewAccount(CustomerDTO customerDTO, String[] requestInputs, double openingBalance) {
        return accountService.createAccount(customerDTO, requestInputs, openingBalance);
    }

    /**
     * @param accountId
     */
    public void displayAccountDetails(AccountDTO accountDTO) {
        if (accountDTO != null) {
            // display account details to user
            // TODO: i think thihs might need to be returned and not printed?
            System.out.println("Account details:");
            System.out.println("ID: " + accountDTO.getId());
            System.out.println("Account number: " + accountDTO.getAccountNumber());
            System.out.println("Balance: " + accountDTO.getBalance());
            System.out.println("Account type: " + accountDTO.getAccountType());
        } else {
            // display error message to user
            System.out.println("Account not found");
        }
    }

    public void creditAmount(AccountDTO accountDTO, double amount) {

        Long accountID = accountDTO.getId();
        boolean success = accountService.deposit(accountID, amount);
        if (success) {
            // display success message to user
            System.out.println("Deposit successful");
        } else {
            // display error message to user
            System.out.println("Deposit failed");
        }
    }

    public void debitAmount(Long accountId, double amount) {
        boolean success = accountService.withdraw(accountId, amount);
        if (success) {
            // display success message to user
            System.out.println("Withdrawal successful");
        } else {
            // display error message to user
            System.out.println("Withdrawal failed");
        }
    }

    // other methods for displaying customer details, transaction history, etc.
}
