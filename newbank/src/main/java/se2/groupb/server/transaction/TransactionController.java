package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.account.Account;
import se2.groupb.server.account.AccountService;
import se2.groupb.server.account.Payee;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerController;
import se2.groupb.server.customer.CustomerService;

public class TransactionController {

    private final CustomerService customerService;
    private final CustomerController customerController;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Payee payees;
    private UserInput comms;

    public TransactionController(CustomerService customerService, CustomerController customerController, AccountService accountService,
            TransactionService transactionService, Payee payees, UserInput comms) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.customerController = customerController;
        this.payees = payees;
        this.comms = comms;
    }

    /**
     * The method takes care of the MOVE protocol.
     * MOVE <Amount> <From> <To>
     * e.g. MOVE 100 Main Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerID
     * @param requestArray
     * @return SUCCESS string or FAIL string
     */
    public String moveMoney(UUID customerID) {
        Customer customer = customerService.getCustomerByID(customerID);

        // Get the customer's existing accounts list
        List<Account> customerAccounts = customer.getAccounts();
        int noOfAccts = customerAccounts.size();
        int noOfSourceAccts = customer.sourceAcctsMap().size();

        if (noOfSourceAccts < 1 || noOfAccts < 2) {
            return "You need two or more accounts.\nRequest denied.\nReturning to Main Menu.";
        }

        // Select a source account (excludes overdrawn accounts)
        String prompt = "Move Money.\nSelect source account: \n" +
                customer.mapToString(customer.sourceAcctsMap())
                + "Enter your option number: \n";
        String userInput = comms.getUserMenuChoice(prompt, noOfSourceAccts);
        String sourceAcctBalance = customer.sourceAcctsMap().get(userInput);
        String sourceAcctName = sourceAcctBalance.split("\\:")[0];
        Account sourceAccount = customer.getAccountByName(sourceAcctName);

        // Select a destination account (excludes source account)
        prompt = "Select destination account: \n" +
                customer.mapToString(customer.destinationAcctsMap(sourceAcctName))
                + "\nEnter your option number: \n";
        int noOfDestAccts = customer.destinationAcctsMap(sourceAcctName).size();
        userInput = comms.getUserMenuChoice(prompt, noOfDestAccts);
        String destinationAcctBalance = customer.destinationAcctsMap(sourceAcctName).get(userInput);
        String destinationAcctName = destinationAcctBalance.split("\\:")[0];
        Account destinationAccount = customer.getAccountByName(destinationAcctName);

        // Enter a positive amount
        prompt = "Transfer amount must be positive and not exceed the Source Account's balance.\nEnter an amount: ";
        BigDecimal limit = sourceAccount.getBalance();
        BigDecimal transferAmount = comms.getAmount(prompt, limit);

        // Confirm transaction
        prompt = "Move " + transferAmount + " from " + sourceAcctName + " to " +
                destinationAcctName
                + "?\nEnter 'y' for Yes or 'n' for No: \n";
        boolean userConfirm = comms.confirm(prompt);

        if (!userConfirm) {
            return "Move transaction was cancelled.\nReturning to the Main Menu.";
        }

        // Execute the transaction
        UUID sourceAccountID = sourceAccount.getAccountID();
        UUID destinationAccountID = destinationAccount.getAccountID();
        boolean isSuccessfullyMoved = transactionService.executeMove(sourceAccountID,
                destinationAccountID, transferAmount);

        if (isSuccessfullyMoved) {
            return "Move transaction was successful.";
        } else {
            return "Something went wrong with the move";
        }
    }

    /**
     * 
     * PAY <Person/Company> <Ammount>
     * e.g. PAY John 100
     * Returns SUCCESS or FAIL
     * 
     * @param customerID
     * @return
     */
    public String transferMoney(UUID customerID) {

        Customer customer = customerService.getCustomerByID(customerID);

        int noOfSourceAccts = customer.sourceAcctsMap().size();

        if (noOfSourceAccts < 1) {
            return "You need atleast one non-overdrawn account.\nRequest denied.\nReturning to Main Menu.";
        }
        // Displayed the payee list. Each paye has a number. The user choose the number of the payee. 
        //If the payee is not in the list, ask to create a new one.

        String prompt = "====================================================\n" +
                        "||           **TRANSFER MONEY MENU**              ||\n" +
                        "|| Please select one of the following options:    ||\n" +
                        "||      1. TRANSFER MONEY                         ||\n" +
                        "||      2. SHOW MY PAYEES                         ||\n" +
                        "||      3. ADD A PAYEE                            ||\n" +
                        "|| Enter the number corresponding to your choice  ||\n" +
                        "|| and press enter                                ||\n" +
                        "====================================================\n" +
                        "\nEnter Selection:";

        String userInput = comms.getUserMenuChoice(prompt,3);
            if (userInput.equals("1")){           
                ArrayList<String> listOfPayees = new ArrayList<String>();
                List<String> payeesList = customer.payeesToList();
		            for (int i=0; i<payeesList.size();i++) {
			            listOfPayees.add("\n"+ (i+1)+". " +payeesList.get(i));
		            }
                    prompt="Choose your payee" + listOfPayees.toString() + "\n 0. Add a payee. \nEnter Selection.";
                    userInput= comms.getUserString(prompt);
                    int userInputInt = comms.convertStringToInt(userInput);
                    if (userInputInt == 0){
                        customerController.createPayee(customerID);
                        return "Payee added";
                    }
                    //comparing the input with the index of the table.
                    else {
                            //The user chose a payee
                            for (int l=0; l<listOfPayees.size(); l++){
                                if (userInputInt - 1 == l){
                                    //pick the selected payee
                                    ArrayList<Payee> customerPayees = customer.getPayees();
                                    UUID payeeID = customerPayees.get(userInputInt-1).getPayeeID();
                                    String payeeName = customerPayees.get(userInputInt-1).getPayeeName(); 
                                    //choose a source account
                            
                                    ArrayList<String> listOfAccounts = new ArrayList<String>();
                                    List<String> accountsList = customer.accountsToList();
                                    //display the account list
                                    for (int k=0; k<accountsList.size();k++) {
                                        listOfAccounts.add("\n"+ (k+1)+accountsList.get(k)); // TO DO: Display only the account name and the balance of the account
                                    }
                                    prompt = "\nChoose an account." + listOfAccounts.toString()+ "\nEnter Selection.";
                                    userInput= comms.getUserMenuChoice(prompt, listOfAccounts.size());
                                    userInputInt = comms.convertStringToInt(userInput);

                                    for (int m =0; m<listOfAccounts.size(); m++){
                                        if (userInputInt-1 == m){
                                        //extract the PayeeId that matches with the selected payee.
                                        ArrayList<Account> customerAccounts = customer.getAccounts();
                                        UUID sourceAccountID = customerAccounts.get(userInputInt-1).getAccountID();
                                        //Extract the source account in order to set the new balance after the transfer
                                        Account sourceAccount = customerAccounts.get(userInputInt-1);
                                        //extract the balance of the selected account
                                        BigDecimal sourceAccountBalance = customerAccounts.get(userInputInt-1).getBalance();     
                                    
                                        //enter the amount of the payment
                                            prompt = "Enter an amount.";
                                            BigDecimal transactionAmount = comms.getAmount(prompt,sourceAccountBalance);
                                            
                                            // confirmation of the transfert/
                                            
                                            prompt = transactionAmount + " will be transfered to " + payeeName + "\n Confirm ? Y/N ";
                                            boolean transferConfirmed = comms.confirm(prompt);
                                            if (!transferConfirmed){
                                                return "Move transaction was cancelled.\nReturning to the Main Menu.";
                                            }
                                            else 
                                            {
                                                Transaction transfertTransaction = new Transaction(sourceAccountID, payeeID, transactionAmount);
                                                // set a new balance for the account
                                                sourceAccount.withdraw(transactionAmount);
                                                
                                                return "Transfert done. Your balance account is now : " + sourceAccount.getBalance();//check  
                                            }
                                            
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
            else if (userInput.equals("2")){
                return customerController.displayPayees(customerID);
            }
            else if (userInput.equals("3")){
                return customerController.createPayee(customerID);
            }
            return "FAIL";
        
    }

}
