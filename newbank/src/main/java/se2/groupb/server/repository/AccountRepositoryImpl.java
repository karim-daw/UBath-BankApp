package se2.groupb.server.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import se2.groupb.server.account.Account;
import se2.groupb.server.account.AccountDTO;

public class AccountRepositoryImpl implements EntityRepository<Account, AccountDTO> {

    private final HashMap<String, Account> theAccounts;

    // Temp constructor using HashMap as Customer Repo
    public AccountRepositoryImpl(HashMap<String, Account> accounts) {
        this.theAccounts = accounts;
    }

    @Override
    public Account findByDTO(AccountDTO accountDTO) {

        return null;
    }

    @Override
    public Account findByID(UUID accountID) {
        return theAccounts.get(accountID.toString());
    }
    
    
    public UUID findCustomerByID(UUID accountID) {
    	return theAccounts.get(accountID.toString()).getCustomerID();
    }
    
    @Override
    public boolean save(Account newAccount) {
        // check if account exists in repo
        if (findByID(newAccount.getAccountID()) == null) {
            // if doesnt exist put it in store
            theAccounts.put(newAccount.getAccountID().toString(), newAccount);
            return true;
        }
        return false;
    }

    @Override
    public Account findByName(String entityName) {
        // TODO Auto-generated method stub
        return null;
    }
}
