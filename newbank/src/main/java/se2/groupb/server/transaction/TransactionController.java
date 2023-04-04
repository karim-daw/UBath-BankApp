package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        List<Account> customerAccounts = customer.getAccounts();
        int noOfAccts = customerAccounts.size();

        Map<String, String> sourceAccts = customer.sourceAcctsMap();
        int noOfSourceAccts = sourceAccts.size();

        if (noOfSourceAccts < 1 || noOfAccts < 2) {
            return "You need two or more accounts.\nRequest denied.\nReturning to Main Menu.";
        }

        String sourceAcctName = selectAccount("Select source account:", sourceAccts);
        Account sourceAccount = customer.getAccountByName(sourceAcctName);

        Map<String, String> destAccts = customer.destinationAcctsMap(sourceAcctName);
        int noOfDestAccts = destAccts.size();

        if (noOfDestAccts < 1) {
            return "No destination account available.\nReturning to Main Menu.";
        }

        String destAcctName = selectAccount("Select destination account:", destAccts);
        Account destinationAccount = customer.getAccountByName(destAcctName);

        BigDecimal transferAmount = getTransferAmount(sourceAccount.getBalance());

        if (!confirmTransaction(transferAmount, sourceAcctName, destAcctName)) {
            return "Move transaction was cancelled.\nReturning to the Main Menu.";
        }

        boolean isSuccessfullyMoved = transactionService.executeMove(sourceAccount.getAccountID(),
                destinationAccount.getAccountID(), transferAmount);

        if (isSuccessfullyMoved) {
            return "Move transaction was successful.";
        } else {
            return "Something went wrong with the move";
        }
    }

    private String selectAccount(String prompt, Map<String, String> accounts) {
        String userInput = comms.getUserMenuChoice(prompt, accounts.size());
        String accountBalance = accounts.get(userInput);
        return accountBalance.split(":")[0];
    }

    private BigDecimal getTransferAmount(BigDecimal limit) {
        String prompt = "Transfer amount must be positive and not exceed the Source Account's balance.\nEnter an amount: ";
        return comms.getAmount(prompt, limit);
    }

    private boolean confirmTransaction(BigDecimal transferAmount, String sourceAcctName, String destAcctName) {
        String prompt = "Move " + transferAmount + " from " + sourceAcctName + " to " +
                destAcctName
                + "?\nEnter 'y' for Yes or 'n' for No: \n";
        return comms.confirm(prompt);
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

        // Get customer name
        String customerName = customer.getUsername();

        // Prompt user to for NewBank member name to PAY to
        String prompt = "Enter NewBank member name you want to PAY money to\nEnter an amount: ";
        String payeeName = comms.getUserString(prompt); // user input

        // Check if the customer is trying to pay himself.
        if (payeeName.equals(customerName)) {
            return "FAIL, you are trying to pay yourself";
        }

        // Check to see if payee if a member of NewBank
        Customer payee;
        try {
            payee = customerService.getCustomerbyName(payeeName);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return "FAIL, payee not a member of NewBank";
        }

        // Prompt user for a transaction amount
        prompt = "Enter an transfer amount: ";
        String amountString = comms.getUserString(prompt); // user input

        BigDecimal transactionAmount;
        try {
            transactionAmount = new BigDecimal(amountString);
        } catch (NumberFormatException e) {
            return "FAIL, input is not a number"; // return fail if input is not figures instead of an error
        }

        if (transactionAmount.compareTo(BigDecimal.ZERO) < 0) {
            return "FAIL, input cannot be negative";
        }

        // get payee account as customer
        ArrayList<Account> payeeAccounts = payee.getAccounts();
        Account payeeFirstAccount = payeeAccounts.get(0); // first account
        UUID payeeID = payeeFirstAccount.getAccountID();

        boolean isSuccessfullyPay = transactionService.executePay(customerID, payeeID, transactionAmount);
        if (isSuccessfullyPay) {
            return "PAY transaction was successful.";
        } else {
            return "Something went wrong with the move";
        }

    }

}
