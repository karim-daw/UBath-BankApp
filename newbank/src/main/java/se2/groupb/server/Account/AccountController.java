package se2.groupb.server.account;

import se2.groupb.server.customer.CustomerDTO;

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
            System.out.println("ID: " + accountDTO.getAccountId());
            System.out.println("Account number: " + accountDTO.getAccountNumber());
            System.out.println("Balance: " + accountDTO.getBalance());
            System.out.println("Account type: " + accountDTO.getAccountType());
        } else {
            // display error message to user
            System.out.println("Account not found");
        }
    }

    public void depositAmount(AccountDTO accountDTO, double amount) {

        Long accountID = accountDTO.getAccountId();
        boolean success = accountService.deposit(accountID, amount);
        if (success) {
            // display success message to user
            System.out.println("Deposit successful");
        } else {
            // display error message to user
            System.out.println("Deposit failed");
        }
    }

    public void withdrawAmount(Long accountId, double amount) {
        boolean success = accountService.withdraw(accountId, amount);
        if (success) {
            // display success message to user
            System.out.println("Withdrawal successful");
        } else {
            // display error message to user
            System.out.println("Withdrawal failed");
        }
    }

    // add create account

    // other methods for displaying customer details, transaction history, etc.
}
