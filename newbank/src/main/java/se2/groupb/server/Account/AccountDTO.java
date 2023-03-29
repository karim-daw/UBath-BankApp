package se2.groupb.server.Account;

import java.math.BigDecimal;

//Subset of Domain for data transfer between layers
public class AccountDTO {
    private Long accountID;
    private int accountNumber;
    private BigDecimal balance;
    private String accountType;

    public AccountDTO(Long accountID, int accountNumber, BigDecimal balance, String accountType) {
        this.accountID = accountID;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    public Long getAccountId() {
        return accountID;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

}
