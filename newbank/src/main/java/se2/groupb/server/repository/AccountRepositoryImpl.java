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

    // get a list of Account objects by Customer ID
    /**
     * @param customerID
     * @return
     */
    public ArrayList<Account> findAccounts(UUID customerID) {
        ArrayList<Account> l = new ArrayList<>();
        for (HashMap.Entry<String, Account> record : theAccounts.entrySet()) {
            UUID recordCustomerID = record.getValue().getCustomerID();
            if (recordCustomerID.equals(customerID)) {
                l.add(record.getValue());
            }
        }
        return l;
    }

    // get a list of Account objects by Customer ID and Account Type
    /**
     * @param customerID
     * @param accountType
     * @return
     */
    public ArrayList<Account> findAccountsByType(UUID customerID, String accountType) {
        ArrayList<Account> l = new ArrayList<>();
        for (Account a : findAccounts(customerID)) {
            if (a.getAccountType().equals(accountType)) {
                l.add(a);
            }
        }
        return l;
    }

    @Override
    public Account findByName(String entityName) {
        // TODO Auto-generated method stub
        return null;
    }
}
