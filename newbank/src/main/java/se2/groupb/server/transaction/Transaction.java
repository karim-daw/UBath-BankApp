package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    private final UUID sourceAccountID;
    private final UUID targetAccountID; // payee id
    private final UUID transactionID;
    private final BigDecimal ammount;
    private Date date;
    private final String description;

    public Transaction(UUID sourceAccountID, UUID targetAccountID, BigDecimal ammount, String description) {
        this.sourceAccountID = sourceAccountID;
        this.targetAccountID = targetAccountID;
        this.ammount = ammount;
        this.transactionID = UUID.randomUUID(); // Generate a unique ID for the transaction.
        this.date = new Date(); // Set the date of the transaction to the current date and time.
        this.description = description;

    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public UUID getTransactionID() {
        return transactionID;
    }

    public UUID getSourceAccountID() {
        return sourceAccountID;
    }

    public UUID getTargetAccountID() {
        return targetAccountID;
    }

}
