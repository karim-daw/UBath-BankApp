package se2.groupb.server.repository;

import java.util.UUID;

import se2.groupb.server.account.Account;

// TODO: see if we can add the dummy data for the accounts here

public interface AccountRepository {
    Account findByAccountID(UUID accountID);
}
