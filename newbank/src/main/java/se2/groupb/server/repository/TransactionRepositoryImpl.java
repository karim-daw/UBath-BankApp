package se2.groupb.server.repository;

import java.util.HashMap;
import java.util.UUID;

import se2.groupb.server.transaction.Transaction;
import se2.groupb.server.transaction.TransactionDTO;

public class TransactionRepositoryImpl implements EntityRepository<Transaction, TransactionDTO> {

    private final HashMap<String, Transaction> theTransactions;

    public TransactionRepositoryImpl(HashMap<String, Transaction> transactions) {
        this.theTransactions = transactions;
    }

    public HashMap<String, Transaction> getTheTransactions() {
        return theTransactions;
    }

    /**
     * Searches the Trnasaction Data Store by TransactionID
     * 
     * @param transactionID
     * @return Transaction from database
     */
    @Override
    public Transaction findByID(UUID transactionID) {
        return theTransactions.get(transactionID.toString());
    }

    public UUID findSourceAccountByID(UUID sourceAccountID) {
        return theTransactions.get(sourceAccountID.toString()).getSourceAccountID();
    }

    public UUID findDestinationAccountByID(UUID targetAccountID) {
        return theTransactions.get(targetAccountID.toString()).getTargetAccountID();
    }

    @Override
    public Transaction findByDTO(TransactionDTO transactionDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Transaction newTransaction) {
        try {
            if (newTransaction == null || newTransaction.getTransactionID() == null) {
                System.out.println("Transaction and TransactionID cannot be null.");
                return false;
            }

            Transaction previousTransaction = theTransactions.put(newTransaction.getTransactionID().toString(),
                    newTransaction);

            return previousTransaction == null; // return true if put was successful

        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return false; // error occurred
        }
    }

    @Override
    public Transaction findByName(String transactionName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean update(Transaction entity) {
        // TODO Auto-generated method stub
        return false;
    }

}
