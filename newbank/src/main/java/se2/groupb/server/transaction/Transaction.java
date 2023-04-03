package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    private final UUID sourceAccountID;
    private final UUID targetAccountID; // payee id
    private String sourceName;
    private String targetName; // payee id
    private final UUID transactionID;
    private final BigDecimal ammount;
    private Date date;
    private String description;

    /**
     * this contructor would be for a transaction between two accounts of the same
     * customer here the customer likely does not need to add a description, nore do
     * they need to add a source name or target name
     * 
     * @param sourceAccountID
     * @param targetAccountID
     * @param ammount
     */
    public Transaction(UUID sourceAccountID, UUID targetAccountID, BigDecimal ammount) {
        this.sourceAccountID = sourceAccountID;
        this.targetAccountID = targetAccountID;
        this.ammount = ammount;
        this.transactionID = UUID.randomUUID(); // Generate a unique ID for the transaction.
        this.date = new Date(); // Set the date of the transaction to the current date and time.
    }

    /**
     * this constructor would be for a transaction between two accounts of two
     * different customers, this could apply to both customers inside of NewBank and
     * outside of Newbank. Here there are extra paramteres including the source and
     * target name as well as a description, say for "rent" or "water bill"
     * 
     * @param sourceAccountID
     * @param targetAccountID
     * @param sourceName
     * @param targetName
     * @param ammount
     * @param description
     */
    public Transaction(UUID sourceAccountID, UUID targetAccountID, String sourceName, String targetName,
            BigDecimal ammount, String description) {
        this.sourceAccountID = sourceAccountID;
        this.targetAccountID = targetAccountID;
        this.ammount = ammount;
        this.transactionID = UUID.randomUUID(); // Generate a unique ID for the transaction.
        this.date = new Date(); // Set the date of the transaction to the current date and time.
        this.description = description;
        this.sourceName = sourceName;
        this.targetName = targetName;

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

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    @Override
    public String toString() {
        return "Transaction [sourceAccountID=" + sourceAccountID + ", targetAccountID=" + targetAccountID
                + ", transactionID=" + transactionID + ", ammount=" + ammount + ", date=" + date + ", description="
                + description + "]";
    }

}
