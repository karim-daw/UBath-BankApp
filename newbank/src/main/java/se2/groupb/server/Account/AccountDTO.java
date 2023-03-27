package se2.groupb.server.account;

import java.math.BigDecimal;

public class AccountDTO {
    private Long accountID;
    private int accountNumber;
    private BigDecimal balance;
    private String accountType;

    public AccountDTO(Long accountID, int accountNumber, BigDecimal balance, String accountType) {
        this.accountID = accountID;
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
