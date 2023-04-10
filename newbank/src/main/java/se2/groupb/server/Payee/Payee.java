package se2.groupb.server.Payee;

import java.util.Arrays;
import java.util.UUID;

public class Payee {

    private final UUID payeeID;
    private final UUID customerID; // id of the person paying
    private final String payeeName;
    private final String payeeAccountNumber;
    private final String payeeBIC;

    public Payee(UUID customerID, String payeeName, String payeeAccountNumber, String payeeBIC) {
        this.payeeID = UUID.randomUUID();
        this.customerID = customerID;
        this.payeeName = payeeName;
        this.payeeAccountNumber = payeeAccountNumber;
        this.payeeBIC = payeeBIC;
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

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public String getPayeeBIC() {
        return payeeBIC;
    }

    // overrides default toString() method for Display objects
    @Override
    public String toString() {
        // rounding balance down to 2 decimals

        String displayPayee = /* "\n" + displayChars('=', 52) + "\n" + */payeeName + displayChars(' ', 3) + payeeBIC +
                displayChars(' ', 3) + payeeAccountNumber + "\n" /*
                                                                  * +
                                                                  * displayChars('=', 52)
                                                                  */;
        return displayPayee;
    }

    // Helper method for printing out the same characters multiple times
    public String displayChars(char myChar, int number) {
        char[] myChars = new char[number];
        Arrays.fill(myChars, myChar);
        return new String(myChars);
    }
}
