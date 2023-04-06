package se2.groupb.server.repository;

import java.util.UUID;

import se2.groupb.server.transaction.Transaction;
import se2.groupb.server.transaction.TransactionDTO;

public class TransactionRepositoryImpl implements EntityRepository<Transaction, TransactionDTO> {

    @Override
    public Transaction findByDTO(TransactionDTO transactionDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Transaction findByID(UUID transactionID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Transaction transaction) {
        // TODO Auto-generated method stub
        return false;
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
