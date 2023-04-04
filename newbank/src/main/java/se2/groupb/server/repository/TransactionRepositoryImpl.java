package se2.groupb.server.repository;

import java.util.HashMap;
import java.util.UUID;

import se2.groupb.server.account.Account;
import se2.groupb.server.transaction.Transaction;

public class TransactionRepositoryImpl implements EntityRepository {
	
	private final HashMap<String, Transaction> theTransactions;

    // Temp constructor using HashMap as Customer Repo
    public TransactionRepositoryImpl(HashMap<String, Transaction> transactions) {
        this.theTransactions = transactions;
    }
	
	public UUID findSourceAccountByID(UUID sourceAccountID) {
    	return theTransactions.get(sourceAccountID.toString()).getSourceAccountID();
    }
	
	public UUID findDestinationAccountByID(UUID targetAccountID) {
    	return theTransactions.get(targetAccountID.toString()).getTargetAccountID();
    }
	
    @Override
    public Object findByDTO(Object entityDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object findByID(UUID entityID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Object entity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object findByName(String entityName) {
        // TODO Auto-generated method stub
        return null;
    }

}
