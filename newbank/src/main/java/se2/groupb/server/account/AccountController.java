package se2.groupb.server.account;

/*
import java.util.HashMap;
import java.util.UUID;
import se2.groupb.server.NewBank;
import se2.groupb.server.customer.*;
import se2.groupb.server.repository.CustomerRepository;
import se2.groupb.server.repository.AccountRepository;
*/
import se2.groupb.server.account.*;
import se2.groupb.server.UserInput;

// Presentation layer: Takes user inputs and displays system response
public class AccountController {

    // fields
    private final AccountServiceImpl accountService;
    public UserInput comms;

    // constructor
    public AccountController(AccountServiceImpl accountService, UserInput comms) {
        this.accountService = accountService; // business logic
        this.comms = comms;
    }
    
    // methods
    
   
    /*
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
	*/
    
    /*
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
	*/
	
    // other methods for displaying customer details, transaction history, etc.
    
    
    
}
