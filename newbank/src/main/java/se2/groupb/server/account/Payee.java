package se2.groupb.server.account;

import java.util.UUID;

public class Payee {

    private final UUID payeeID;
    private final UUID customerID; // id of the person paying
    private final String payeeName;
    private final String accountNumber;
    private final String accountBIC;

    public Payee(UUID payeeID, UUID customerID, String payeeName, String accountNumber, String accountBIC) {
        this.payeeID = payeeID;
        this.customerID = customerID;
        this.payeeName = payeeName;
        this.accountNumber = accountNumber;
        this.accountBIC = accountBIC;
    }

    public UUID getPayeeID() {
        return payeeID;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountBIC() {
        return accountBIC;
    }
}
