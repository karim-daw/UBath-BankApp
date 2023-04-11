package se2.groupb.server.repository;

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

    /**
     * @param accountID
     * @return
     */
    @Override
    public Account findByID(UUID accountID) {
        return theAccounts.get(accountID.toString());
    }

    /**
     * @param accountID
     * @return
     */
    public UUID findCustomerByID(UUID accountID) {
        return theAccounts.get(accountID.toString()).getCustomerID();
    }

    /**
     * Finds an account by account number.
     *
     * @param accountNumber the account number to look for
     * @return the account with the given account number, or null if not found or
     *         accountNumber is null
     */
    public Account findByAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            System.err.println("Error: Account number cannot be null.");
            return null;
        }

        try {
            return theAccounts.values().stream()
                    .filter(account -> account.getAccountNumber().equals(accountNumber))
                    .findFirst()
                    .orElseThrow(
                            () -> new Exception("Error: Account with account number " + accountNumber + " not found."));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param newAccount
     * @return true if account is added successfully, false if account is not found,
     *         if there is an error or if all parameters are null
     */
    @Override
    public boolean save(Account newAccount) {

        // return false if input is null or all parameters are null
        if (newAccount == null || (newAccount.getCustomerID() == null && newAccount.getAccountType() == null
                && newAccount.getAccountName() == null)) {
            return false;
        }

        try {
            // check if account exists in repo
            if (findByID(newAccount.getAccountID()) == null) {
                // if doesn't exist put it in store
                theAccounts.put(newAccount.getAccountID().toString(), newAccount);
                return true; // account update is successful
            } else {
                return false; // account not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // error occurred
        }
    }

    /**
     * @param newAccount
     * @return true if account is updated succesfully, false if account is not found
     *         or if there is an error
     */
    @Override
    public boolean update(Account newAccount) {
        try {
            // check if account exists in repo
            if (findByID(newAccount.getAccountID()) != null) {
                // if doesnt exist put it in store
                theAccounts.put(newAccount.getAccountID().toString(), newAccount);
                return true; // account update is sucessful
            } else {
                return false; // account not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // error occureed
        }
    }

    @Override
    public Account findByName(String entityName) {
        // TODO Auto-generated method stub
        return null;
    }
}
