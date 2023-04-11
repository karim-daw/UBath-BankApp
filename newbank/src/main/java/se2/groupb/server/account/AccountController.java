package se2.groupb.server.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

/*
import java.util.HashMap;

import se2.groupb.server.NewBank;
import se2.groupb.server.customer.*;
import se2.groupb.server.repository.CustomerRepository;
import se2.groupb.server.repository.AccountRepository;
*/
import java.util.UUID;
import se2.groupb.server.UserInput;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerService;

// Presentation layer: Takes user inputs and displays system response
public class AccountController {

    // fields
    private final AccountService accountService;
    private final CustomerService customerService;
    public UserInput comms;

    // constructor
    public AccountController(AccountService accountService, CustomerService customerService, UserInput comms) {
        this.accountService = accountService; // business logic
        this.customerService = customerService; // business logic
        this.comms = comms;
    }

    /**
     * NEWACCOUNT <Name>
     * e.g. NEWACCOUNT Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerID
     * @return
     */
    public String newAccount(UUID customerID) {
        String response = ""; // the system response to the user's request
        // UUID customerID = customer.getCustomerID();
        HashMap<String, String> newAcctOptions = accountService.newAccountAvailableTypes(customerID);

        int noOfChoices = newAcctOptions.size(); // 0,1, or 2
        if (noOfChoices > 0) {
            String prompt = "Create a new account: \n" + mapToString(newAcctOptions)
                    + "\nEnter the number of your choice: ";
            String userInput = comms.getUserMenuChoice(prompt, noOfChoices);
            String accountType = newAcctOptions.get(userInput); // the choice of account type entered by the user
            String str = "Create a new " + accountType + " account.\n";
            comms.printSystemMessage(str);

            // check if customer already has an account type with that name
            boolean duplicateName;
            String accountName;
            do {
                prompt = "Enter an account name: ";
                accountName = comms.getUserString(prompt);
                duplicateName = accountService.hasAccount(customerID, accountType, accountName);
                if (duplicateName) {
                    comms.printSystemMessage("Account name taken. Please try again.");
                }
            } while (duplicateName);

            prompt = "Enter a positive opening balance (default is zero): \n";
            BigDecimal openingBalance = comms.getOpeningBalance(prompt);

            prompt = "Open a new " + accountType + " account: " + accountName + " with an opening balance of "
                    + openingBalance
                    + "?\nEnter 'y' for Yes or 'n' for No: \n";
            boolean userConfirm = comms.confirm(prompt);

            if (userConfirm) {

                // adds new account to customer
                AccountDTO accountDto = new AccountDTO(accountType, accountName, openingBalance);
                Account newAccount = accountService.createAccount(customerID, accountDto);
                Customer customer = customerService.getCustomerByID(customerID);
                customer.addAccount(newAccount);

                // Call NewBank method to add new customer account to bank's data store
                response = "SUCCESS: Your " + accountType + " account has been created.\nReturning to Main Menu.";
            } else {
                response = "Account creation was cancelled.\nReturning to the Main Menu.";
            }
        } else {
            response = "You have reached the maximum number of accounts.\nReturning to Main Menu.";
        }
        return response;
    }

    /**
     *
     * Helper method for printing the contents of a HashMap<String,String>
     * 
     * @return a string of the contents
     */
    public String mapToString(HashMap<String, String> map) {
        String s = "";
        if (map.size() > 0) {
            for (HashMap.Entry<String, String> item : map.entrySet()) {
                s += item.getKey() + " = " + item.getValue() + "\n";
            }
        }
        return s;
    }

    // methods
    
    /**
	 * displays the customers accounts as a list
	 * 
	 * @param customerDTO
	 * @return
	 */
	public String displayAccounts(UUID customerID) {
		return accountService.displayAccounts(customerID);
	}

    
    /**
     * Gets Account number input from user and validates it
     * @param customerID
     * @param accountDescription : Source or Destination
     * @return Account
     */
    public Account getAccountInput(UUID customerID){
		String prompt;
		String accountNumber;
		boolean hasAccount;
		//this ensures account exists
		do {
			prompt = "Enter your Account number: \n";
			accountNumber = comms.getUserString(prompt);
			
			hasAccount = accountService.hasAccountNumber(customerID, accountNumber);
			if (!hasAccount) {
				comms.printSystemMessage("Account not found. Please try again.");
			}
		} while (!hasAccount);
		
		return accountService.getAccountByNumber(customerID, accountNumber);
	}
    
    
    /*
     * public void creditAmount(AccountDTO accountDTO, double amount) {
     * 
     * Long accountID = accountDTO.getAccountId();
     * boolean success = accountService.deposit(accountID, amount);
     * if (success) {
     * // display success message to user
     * System.out.println("Deposit successful");
     * } else {
     * // display error message to user
     * System.out.println("Deposit failed");
     * }
     * }
     */

    /*
     * public void debitAmount(Long accountId, double amount) {
     * boolean success = accountService.withdraw(accountId, amount);
     * if (success) {
     * // display success message to user
     * System.out.println("Withdrawal successful");
     * } else {
     * // display error message to user
     * System.out.println("Withdrawal failed");
     * }
     * }
     */

    // other methods for displaying customer details, transaction history, etc.

}
