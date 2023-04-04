package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.account.Account;
import se2.groupb.server.account.AccountService;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerService;

public class TransactionController {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private UserInput comms;

    public TransactionController(CustomerService customerService, AccountService accountService,
            TransactionService transactionService, UserInput comms) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
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

        // Check if the customer exists in the hashmap.
        String customerName = customer.getUsername();

        String prompt = "Enter NewBank member name you want to PAY money to\nEnter an amount: ";
        String payeeName = comms.getUserString(prompt);

        // System.out.println(payeeName);

        // TODO: make sure to fix this
        // if (accountNumber.equals(customerName)) {
        // return "FAIL, you are trying to pay yourself";
        // }

        // TODO: THIS IS WHEERE I LEFT OFF....
        Customer payee = customerService.getCustomerbyName(payeeName);

        if (!customers.containsKey(payeeName)) {
            return "FAIL, payee not a member of NewBank";
        }

        double transactionAmount;
        try {
            transactionAmount = Double.parseDouble(requestArray[2]);
        } catch (NumberFormatException e) {
            return "FAIL"; // return fail if input is not figures instead of an error
        }

        if (transactionAmount < 0) {
            return "FAIL";
        }

        // first account in accounts list will be default for now for payer
        ArrayList<Account> payerAccounts = customer.getAccounts(); // payers accounts
        Account payerFirstAccount = payerAccounts.get(0);

        if (isOverDraft(payerFirstAccount, transactionAmount)) {
            return "FAIL, insufficient funds for PAY amount";
        }
        payerFirstAccount.updateBalance(-transactionAmount);

        // handle update on payee account
        CustomerID payeeCustomerID = new CustomerID(payeeName);
        Customer payeeCustomer = customers.get(payeeCustomerID.getKey());

        // get payee account as customer
        ArrayList<Account> payeeAccounts = payeeCustomer.getAccounts();
        Account payeeFirstAccount = payeeAccounts.get(0); // first account

        // update balance
        payeeFirstAccount.updateBalance(transactionAmount);

        return "SUCCESS";
    }

}
