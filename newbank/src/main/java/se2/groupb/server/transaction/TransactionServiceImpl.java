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
        String payeeAccountNumber = payee.getPayeeAccountNumber();
        Account payeeAccount = accountRepository.findByAccountNumber(payeeAccountNumber);
        boolean isNewBankAccount = true;

        if (payeeAccount == null) {
            isNewBankAccount = false;
        }

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

        // If the Payee's Account is a New Bank Account then add the credit to their
        // account:
        if (isNewBankAccount) {
            payeeAccount.credit(amount);
            payeeAccount.addTransaction(moveTransaction);

        }

        // save transaction to transaction store
        boolean success = transactionRepository.save(moveTransaction);

        // if transaction was successfully save this should return a boolean
        if (success) {
            return true;
        } else {
            return false;
        }

    }

}