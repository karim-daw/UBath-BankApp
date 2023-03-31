package se2.groupb.server.repository;

import se2.groupb.server.account.Account;

public interface AccountRepository {
    Account findByAccountID(Long accountID);
}
