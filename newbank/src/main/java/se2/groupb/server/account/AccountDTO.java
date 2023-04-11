package se2.groupb.server.account;

import java.math.BigDecimal;

//Subset of Domain for data transfer between layers
public class AccountDTO {

    private String accountType;
    private String accountName;
    private BigDecimal openingBalance;

    // Constructor
    public AccountDTO(String accountType, String accountName, BigDecimal openingBalance) {
        this.accountType = accountType;
        this.accountName = accountName;
        this.openingBalance = openingBalance;

    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }
}
