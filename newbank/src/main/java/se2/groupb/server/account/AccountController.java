package se2.groupb.server.account;

import java.util.UUID;

import se2.groupb.server.NewBank;
import se2.groupb.server.UserInput;
import se2.groupb.server.customer.*;
import se2.groupb.server.repository.CustomerRepository;
import se2.groupb.server.repository.AccountRepository;


// Presentation layer: Takes user inputs and displays system response
public class AccountController {

    // fields
    private final AccountServiceImpl accountService;
    private final NewBank bank;
    public UserInput comms;

    // constructor
    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService; // business logic
        bank = NewBank.getBank();
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
    
    // at this point a customer already exists in the system so they'd have a UUID
    public String createNewAccount(Customer customer) {
        String response = ""; // the system response to account creation
        
        

        int noOfChoices = customer.newAcctTypes().size();
        UUID customerID = customerDto.getCustomerID();

        if (noOfChoices > 0) { // if there are available account types for creation
            String systemPrompt = "Create a new account.\nChoose from: \n"
                    + customer.mapToString(customer.newAcctTypes()) + "\nEnter your option number: \n";
            String userInput = comms.getUserMenuChoice(systemPrompt, noOfChoices);
            // out.println(userInput);
            String accountType = customer.newAcctTypes().get(userInput); // gets the new account type
            // out.println(accountType);

            systemPrompt = "Enter an opening balance (must be positive): \n";
            double openingBalance = comms.getOpeningBalance(systemPrompt);

            systemPrompt = "Open a new " + accountType + " account with an opening balance of " + openingBalance
                    + "?\nEnter 'y' for Yes or 'n' for No: \n";
            boolean userConfirm = comms.confirm(systemPrompt);

            if (userConfirm) {
                customer.addAccount(new Account(customerID, accountType, openingBalance)); // adds new account to
                                                                                           // customer
                // Call NewBank method to add new customer account to bank's data store
                response = "SUCCESS: Your " + accountType + " account has been created.\nReturning to Main Menu.";
            } else {
                response = "Account creation was cancelled.\nReturning to the Main Menu.";
            }
        } else {
            response = "All possible account types have been created.\nReturning to Main Menu.";
            // newBankClientHandler.startup();
        }
        return response;
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

    public void creditAmount(AccountDTO accountDTO, double amount) {

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
