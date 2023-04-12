package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import se2.groupb.server.UserInput;
import se2.groupb.server.Payee.Payee;
import se2.groupb.server.Payee.PayeeController;
//import se2.groupb.server.Payee.PayeeService;
import se2.groupb.server.account.Account;
//import se2.groupb.server.account.AccountService;
//import se2.groupb.server.account.AccountController;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerService;

public class TransactionController {

    private final CustomerService customerService;
    // private final CustomerController customerController;
    private final PayeeController payeeController;
    // private final AccountService accountService;
    // private final AccountController accountController;
    private final TransactionService transactionService;
    // private PayeeService payeeService;
    private UserInput comms;

    private static final String payeesMenu = "\n" +
            "====================================================\n" +
            "||         *** TRANSFER MONEY MENU ***            ||\n" +
            "|| Please select one of the following options:    ||\n" +
            "||      1. PAY AN EXISTING PAYEE                  ||\n" +
            "||      2. PAY A NEW PAYEE                        ||\n" +
            "||      3. CANCEL PAY & RETURN TO MAIN MENU       ||\n" +
            "|| Enter the number corresponding to your choice  ||\n" +
            "|| and press enter                                ||\n" +
            "====================================================\n" +
            "\nEnter Selection:";

    private static final int payeesMenuChoices = 3;

    private static final String noPayeesMenu = "\n" +
            "====================================================\n" +
            "||         *** NO EXISTING PAYEES ***             ||\n" +
            "|| Please select one of the following options:    ||\n" +
            "||      1. PAY A NEW PAYEE                        ||\n" +
            "||      2. CANCEL PAY & RETURN TO MAIN MENU       ||\n" +
            "|| Enter the number corresponding to your choice  ||\n" +
            "|| and press enter                                ||\n" +
            "====================================================\n" +
            "\nEnter Selection:";

    private static final int noPayeesMenuChoices = 2;

    public TransactionController(CustomerService customerService, TransactionService transactionService,
            PayeeController payeeController, UserInput comms) {

        this.customerService = customerService;
        this.transactionService = transactionService;
        this.payeeController = payeeController;
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
        Map<String, Account> sourceAccts = customer.sourceAcctsMap(); // The Customer's Source Accounts as an ordered
                                                                      // numbered Map:
        int noOfSourceAccts = sourceAccts.size();
        if (noOfSourceAccts < 1 || noOfAccts < 2) {
            return "You need two or more accounts.\nRequest denied.\nReturning to Main Menu.";
        }

        // Display all potential Source Accounts to the customer:
        String prompt = "Select a Source Account from: \n" + customer.accountMapToString(sourceAccts) +
                "Enter your choice: \n";
        // Get user's choice of Source Account:
        String userInput = comms.getUserMenuChoice(prompt, noOfSourceAccts); // gets user's choice
        Account sourceAccount = sourceAccts.get(userInput);
        String sourceAcctName = sourceAccount.getAccountName();

        // Destination Accounts Map excludes the Source Account
        Map<String, Account> destAccts = customer.destinationAcctsMap(sourceAccount.getAccountID());
        int noOfDestAccts = destAccts.size();
        if (noOfDestAccts < 1) {
            return "No destination account available.\nReturning to Main Menu.";
        }

        // Display all potential Source Accounts to the customer:
        prompt = "Select a Destination Account from: \n" + customer.accountMapToString(destAccts) +
                "Enter your choice: \n";

        // Get user's choice of Destination Account:
        userInput = comms.getUserMenuChoice(prompt, noOfDestAccts); // gets user's choice
        Account destinationAccount = destAccts.get(userInput);
        String destAcctName = destinationAccount.getAccountName();

        // Get the user's input Amount:
        BigDecimal transferAmount = getTransferAmount(sourceAccount.getBalance());

        if (!confirmTransaction(transferAmount, sourceAcctName, destAcctName)) {
            return "Move transaction was cancelled.\nReturning to the Main Menu.";
        }

        boolean isSuccessfullyMoved = transactionService.executeMove(sourceAccount.getAccountID(),
                destinationAccount.getAccountID(), transferAmount);

        if (isSuccessfullyMoved) {
            return "Move Transaction Success.";
        } else {
            return "Move Transaction Failure.";
        }
    }

    private BigDecimal getTransferAmount(BigDecimal limit) {
        String prompt = "Transfer amount must be positive and not exceed the Source Account's balance.\nEnter an amount: ";
        return comms.getAmount(prompt, limit);
    }

    private String getTransferReference() {
        String prompt = "Enter a Reference for this Transaction: \n";
        return comms.getUserString(prompt);
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

        // The Customer's Source Accounts as an ordered numbered Map:
        Map<String, Account> sourceAccts = customer.sourceAcctsMap();
        int noOfSourceAccts = sourceAccts.size();
        if (noOfSourceAccts == 0) {
            return "No valid Source Accounts found.\nRequest denied.\nReturning to Main Menu.";
        }

        // Display all potential Source Accounts to the customer:
        String prompt = "Select a Source Account from: \n" + customer.accountMapToString(sourceAccts) +
                "Enter your choice: \n";
        // Get user's choice of Source Account:
        String userInput = comms.getUserMenuChoice(prompt, noOfSourceAccts); // gets user's choice
        Account sourceAccount = sourceAccts.get(userInput);
        String sourceAcctName = sourceAccount.getAccountName();
        UUID sourceAcctID = sourceAccount.getAccountID();

        // Select a Payee

        // Payees Map
        Map<String, Payee> payees = customer.payeesMap();
        Payee payee = selectPayee(customerID, payees);
        if (payee == null) {
            return "Returning to Main Menu.";
        }
        UUID payeeID = payee.getPayeeID();
        String payeeName = payee.getPayeeName();

        // Get the user's input Amount:
        BigDecimal transferAmount = getTransferAmount(sourceAccount.getBalance());

        // Get the user's reference for the transaction:
        String transferReference = getTransferReference();

        // Seek confirmation from user before proceeding:
        if (!confirmTransaction(transferAmount, sourceAcctName, payeeName)) {
            return "Transfer was cancelled.\nReturning to the Main Menu.";
        }

        // Execute Transaction:
        boolean isTransferSuccess = transactionService.executePay(sourceAcctID, payeeID, transferAmount,
                transferReference);

        if (isTransferSuccess) {
            return "Transfer Success.";
        } else {
            return "Transfer Failure.";
        }
    }

    /**
     * @param customerID
     * @param payees
     * @return
     */
    private Payee selectPayee(UUID customerID, Map<String, Payee> payees) {
        String prompt = "";
        int noOfPayees = payees.size();
        String mainRequest = comms.getUserMenuChoice(payeesMenu, payeesMenuChoices);

        // If the customer chose to pay an existing Payee but they have none:
        if ((mainRequest == "1") && (noOfPayees == 0)) {
            prompt = "Invalid request. You have no existing Payees.\n";
            prompt += "You must choose from the following options: \n";
            comms.printSystemMessage(prompt);
            String subRequest = comms.getUserMenuChoice(noPayeesMenu, noPayeesMenuChoices);
            Integer subRequestInt = Integer.parseInt(subRequest) + 1;
            mainRequest = subRequestInt.toString();
        }

        switch (mainRequest) {
            case "1": { // Pay an existing Payee
                prompt = "Select a Payee from: \n" + payeeMapToString(payees) +
                        "Enter your choice: \n";
                String userInput = comms.getUserMenuChoice(prompt, noOfPayees); // gets user's choice
                Payee payee = payees.get(userInput);
                return payee;
            }
            case "2": { // Pay a new Payee: create a new payee and then do transfer
                Payee payee = payeeController.createPayee(customerID);
                if (payee == null) {
                    comms.printSystemMessage("Returning to Main Menu.\n");
                }
                return payee;
            }
            case "3": { // Cancel PAY request and return to Main Menu
                comms.printSystemMessage("PAY request cancelled.\n");
                return null;
            }
        }
        return null;
    }

    /**
     * Helper function for turning a Map to a string
     * 
     * @return a string of the Payee map
     */
    public String payeeMapToString(Map<String, Payee> map) {
        String s = "";
        for (Map.Entry<String, Payee> item : map.entrySet()) {
            s += item.getKey() + " = " + item.getValue().toString() + "\n";
        }
        return s;
    }

}
