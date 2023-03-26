package se2.groupb.server.account;

import java.math.BigDecimal;

public class AccountDTO {
    private Long id;
    private int accountNumber;
    private BigDecimal balance;
    private String accountType;

    public AccountDTO(Long id, int accountNumber, BigDecimal balance, String accountType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
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
