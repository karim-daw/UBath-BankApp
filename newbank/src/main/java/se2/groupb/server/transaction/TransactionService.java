package se2.groupb.server.transaction;

import java.util.ArrayList;

import se2.groupb.server.account.Account;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerID;

public class TransactionService {

    /*
     * this method takes care of the PAY feature indicated below, gven and customer
     * (pay2er) and a requested payee, this will transaction money from these
     * accounts
     * and update balances accordingly
     * PAY <Person/Company> <Ammount>
     * e.g. PAY John 100
     * Returns SUCCESS or FAIL
     * 
     * @param customer
     * 
     * @param requestArray
     * 
     * @return string that is SUCCESS or FAIL if transaction succeeded
     */
    private String transferMoney(CustomerID customerID, String[] requestArray) {

        if (requestArray.length < 3) {
            return "FAIL, incomplete PAY Request";
        }

        // Check if the customer exists in the hashmap.
        String customerName = customerID.getKey();
        String payeeName = requestArray[1];
        Customer customer = customers.get(customerName);
        System.out.println(payeeName);

        if (payeeName.equals(customerName)) {
            return "FAIL, you are trying to pay yourself";
        }

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

    /**
     * method takes care of the MOVE protocol
     * 
     * MOVE <Amount> <From> <To>
     * e.g. MOVE 100 Main Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerID
     * @param requestArray
     * @return SUCCESS string or FAIL string
     */
    private String moveMoney(CustomerID customerID, String[] requestInputs) {

        // Check if request is incomplete
        int inputLength = requestInputs.length;
        if (inputLength < 4) {
            return "FAIL: Invalid MOVE request";
        }

        // Check if the customer exists in the hashmap.
        String customerName = customerID.getKey();
        Customer customer = customers.get(customerName);

        // Check if transaction amount is a number
        double transactionAmount;
        try {
            transactionAmount = Double.parseDouble(requestInputs[1]);
        } catch (NumberFormatException e) {
            return "FAIL"; // return fail if input is not figures instead of an error
        }

        // Check if transaction amount is negative
        if (transactionAmount < 0) {
            return "FAIL";
        }

        // Get the accounts from the customer
        System.out.println("Select source account.");
        Account sourceAccount = customer.getAccountByName(requestInputs[2]);
        if (sourceAccount.equals(null)) {
            return "FAIL, source account does not exist";
        }

        // check if account has overdraft
        if (isOverDraft(sourceAccount, transactionAmount)) {
            return "FAIL, insufficient funds in the source account.";
        }

        // update balance of source account
        sourceAccount.updateBalance(-transactionAmount);

        // get destination account
        System.out.println("Select destination account.");
        Account destinationAccount = customer.getAccountByName(requestInputs[3]);
        if (destinationAccount == null) {
            return "FAIL, destination account does not exist";
        }

        // update balance of destination account
        destinationAccount.updateBalance(transactionAmount);
        return "SUCCESS";

    }
}
