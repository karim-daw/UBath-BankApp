package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class Transaction {

    private final UUID sourceAccountID;
    private final UUID targetAccountID; // payee id
    private final UUID transactionID;
    private final BigDecimal ammount;

    public Transaction(UUID sourceAccountID, UUID targetAccountID, BigDecimal ammount) {
        this.sourceAccountID = sourceAccountID;
        this.targetAccountID = targetAccountID;
        this.ammount = ammount;
        this.transactionID = UUID.randomUUID();

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
