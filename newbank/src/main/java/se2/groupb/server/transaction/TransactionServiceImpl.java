package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.UUID;

import se2.groupb.server.account.Account;
import se2.groupb.server.account.AccountService;
import se2.groupb.server.Payee.Payee;
import se2.groupb.server.repository.AccountRepositoryImpl;
import se2.groupb.server.repository.CustomerRepositoryImpl;
import se2.groupb.server.repository.TransactionRepositoryImpl;
import se2.groupb.server.repository.PayeeRepositoryImpl;

public class TransactionServiceImpl implements TransactionService {

    private final AccountRepositoryImpl accountRepository;
    private final TransactionRepositoryImpl transactionRepository;
    private final CustomerRepositoryImpl customerRepository;
    private final PayeeRepositoryImpl payeeRepository;

    public TransactionServiceImpl(
            AccountRepositoryImpl accountRepository, PayeeRepositoryImpl payeeRepository,
            TransactionRepositoryImpl transactionRepository,
            CustomerRepositoryImpl customerRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.payeeRepository = payeeRepository;
    }

    @Override
    public boolean executeMove(UUID fromAccountID, UUID toAccountID, BigDecimal amount) {

        // get source and target account
        Account sourceAccount = accountRepository.findByID(fromAccountID);
        Account targetAccount = accountRepository.findByID(toAccountID);
        Transaction moveTransaction = new Transaction(fromAccountID, toAccountID, amount);

        // subtract money from the sourceAccount by the amount
        // create transaction and add it to source account transactions list
        sourceAccount.debit(amount);
        sourceAccount.addTransaction(moveTransaction);

        // add money to the target account by the amount
        targetAccount.credit(amount);
        targetAccount.addTransaction(moveTransaction);

        // save transaction to transaction store
        boolean success = transactionRepository.save(moveTransaction);

        // TODO: update date base
        // success = true;

        // if transaction was successfully save this should return a boolean
        if (success) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean executePay(UUID fromAccountID, UUID toPayeeID, BigDecimal amount, String reference) {

        // get source and target account
        Account sourceAccount = accountRepository.findByID(fromAccountID);

        Payee payee = payeeRepository.findByID(toPayeeID);
        Transaction moveTransaction = new Transaction(fromAccountID, toPayeeID, amount, reference);

        // check if source account has enough funds
        boolean insufficient = sourceAccount.hasInsufficientFunds(amount);

        if (insufficient) {
            return false;
        }

        // subtract money from the sourceAccount by the amount
        // create transaction and add it to source account transactions list
        sourceAccount.debit(amount);
        sourceAccount.addTransaction(moveTransaction);

        // add money to the target account by the amount

        // get account number form payee
        String payeeAccountNumber = payee.getPayeeAccountNumber();

        Account payeeNewBankAccount = accountRepository.findByAccountNumber(payeeAccountNumber);
        if (payeeNewBankAccount != null) {
            payeeNewBankAccount.credit(amount);
            payeeNewBankAccount.addTransaction(moveTransaction);
        }
        // do nothing to the account holder, its outsIDE newbank

        // save transaction to transaction store
        boolean success = transactionRepository.save(moveTransaction);

        // if transaction was successfully save this should return a boolean
        if (success) {
            return true;
        } else {
            return false;
        }

    }

    // TODO: generate UUID for every type of transaction

    /*
     * this method takes care of the PAY feature indicated below, gven and
     * customer
     * (payer) and a requested payee, this will transaction money from these
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
    // private String transferMoney(CustomerDTO customerDTO, String[] requestArray)
    // {

    // if (requestArray.length < 3) {
    // return "FAIL, incomplete PAY Request";
    // }
    // }
}

// // Check if the customer exists in the hashmap.
// String customerName = customerDTO.getUsername();
// String payeeName = requestArray[1];

// // TODO: #34 add access to database here to gett customer data
// Customer customer = customers.get(customerName);
// System.out.println(payeeName);

// if (payeeName.equals(customerName)) {
// return "FAIL, you are trying to pay yourself";
// }

// if (!customers.containsKey(payeeName)) {
// return "FAIL, payee not a member of NewBank";
// }

// double transactionAmount;
// try {
// transactionAmount = Double.parseDouble(requestArray[2]);
// } catch (NumberFormatException e) {
// return "FAIL"; // return fail if input is not figures instead of an error
// }

// if (transactionAmount < 0) {
// return "FAIL";
// }

// // first account in accounts list will be default for now for payer
// ArrayList<Account> payerAccounts = customer.getAccounts(); // payers accounts
// Account payerFirstAccount = payerAccounts.get(0);

// if (isOverDraft(payerFirstAccount, transactionAmount)) {
// return "FAIL, insufficient funds for PAY amount";
// }
// payerFirstAccount.updateBalance(-transactionAmount);

// // handle update on payee account
// CustomerID payeeCustomerID = new CustomerID(payeeName);
// Customer payeeCustomer = customers.get(payeeCustomerID.getKey());

// // get payee account as customer
// ArrayList<Account> payeeAccounts = payeeCustomer.getAccounts();
// Account payeeFirstAccount = payeeAccounts.get(0); // first account

// // update balance
// payeeFirstAccount.updateBalance(transactionAmount);

// return "SUCCESS";
// }

// /**
// * method takes care of the MOVE protocol
// *
// * MOVE <Amount> <From> <To>
// * e.g. MOVE 100 Main Savings
// * Returns SUCCESS or FAIL
// *
// * @param customerID
// * @param requestArray
// * @return SUCCESS string or FAIL string
// */
// public String moveMoneyEnhancement(CustomerID customerID) {
// // MOVE <Amount> <From> <To>
// Customer customer = bank.getCustomers().get(customerID.getKey());
// String response = null;

// // Get the customer's existing accounts list
// int noOfSourceAccts = customer.sourceAcctsMap().size();
// int noOfAccts = customer.accountsToList().size();

// if ((noOfSourceAccts >= 1) && (noOfAccts >= 2)) {
// // Select a source account (excludes overdrawn accounts)
// String prompt = "Move Money.\nSelect source account: \n" +
// customer.mapToString(customer.sourceAcctsMap())
// + "Enter your option number: \n";
// String userInput = comms.getUserMenuChoice(prompt, noOfSourceAccts);
// String sourceAcctBalance = customer.sourceAcctsMap().get(userInput);
// String sourceAcct = sourceAcctBalance.split("\\:")[0];

// // Select a destination account (excludes source account)
// // out.println(customer.destinationAcctsMap(sourceAcct));
// prompt = "Select destination account: \n" +
// customer.mapToString(customer.destinationAcctsMap(sourceAcct))
// + "\nEnter your option number: \n";
// int noOfDestAccts = customer.destinationAcctsMap(sourceAcct).size();
// userInput = comms.getUserMenuChoice(prompt, noOfDestAccts);
// String destinationAcctBalance =
// customer.destinationAcctsMap(sourceAcct).get(userInput);
// String destinationAcct = destinationAcctBalance.split("\\:")[0];

// // Enter a positive amount
// prompt = "Transfer amount must be positive and not exceed the Source
// Account's balance.\nEnter an amount: ";

// double limit = customer.getAccountByName(sourceAcct).getBalance();
// double transferAmount = comms.getAmount(prompt, limit);
// prompt = "Move " + transferAmount + " from " + sourceAcct + " to " +
// destinationAcct
// + "?\nEnter 'y' for Yes or 'n' for No: \n";
// boolean userConfirm = comms.confirm(prompt);

// if (userConfirm) {
// // update balance of source account
// customer.getAccountByName(sourceAcct).updateBalance(-transferAmount);
// // update balance of destination account
// customer.getAccountByName(destinationAcct).updateBalance(transferAmount);
// response = "Move transaction was successful.";
// } else {
// response = "Move transaction was cancelled.\nReturning to the Main Menu.";
// }
// } else {
// response = "You need two or more accounts.\nRequest denied.\nReturning to
// Main Menu.";
// // newBankClientHandler.startup();
// }
// return response;
// }

// // private String moveMoney(CustomerID customerID, String[] requestInputs) {

// // // Check if request is incomplete
// // int inputLength = requestInputs.length;
// // if (inputLength < 4) {
// // return "FAIL: Invalid MOVE request";
// // }

// // // Check if the customer exists in the hashmap.
// // String customerName = customerID.getKey();
// // Customer customer = customers.get(customerName);

// // // Check if transaction amount is a number
// // double transactionAmount;
// // try {
// // transactionAmount = Double.parseDouble(requestInputs[1]);
// // } catch (NumberFormatException e) {
// // return "FAIL"; // return fail if input is not figures instead of an error
// // }

// // // Check if transaction amount is negative
// // if (transactionAmount < 0) {
// // return "FAIL";
// // }

// // // Get the accounts from the customer
// // System.out.println("Select source account.");
// // Account sourceAccount = customer.getAccountByName(requestInputs[2]);
// // if (sourceAccount.equals(null)) {
// // return "FAIL, source account does not exist";
// // }

// // // check if account has overdraft
// // if (isOverDraft(sourceAccount, transactionAmount)) {
// // return "FAIL, insufficient funds in the source account.";
// // }

// // // update balance of source account
// // sourceAccount.updateBalance(-transactionAmount);

// // // get destination account
// // System.out.println("Select destination account.");
// // Account destinationAccount = customer.getAccountByName(requestInputs[3]);
// // if (destinationAccount == null) {
// // return "FAIL, destination account does not exist";
// // }

// // // update balance of destination account
// // destinationAccount.updateBalance(transactionAmount);
// // return "SUCCESS";

// // }
// }
