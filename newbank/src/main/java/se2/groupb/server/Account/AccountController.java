package se2.groupb.server.account;

public class AccountController {

    // fields
    private final AccountService accountService;

    // constructor
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // methods

    // In a traditional REST API, this could be considered as POST request where the
    // AccountController handles the function that creates a new Account

    // public addUser(){
    // accountService.createAccount()
    // }

    /**
     * @param accountId
     */
    public void displayAccountDetails(AccountDTO accountDTO) {
        if (accountDTO != null) {
            // display account details to user
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

    public void depositAmount(AccountDTO accountDTO, double amount) {

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

    // other methods for displaying customer details, transaction history, etc.
}
