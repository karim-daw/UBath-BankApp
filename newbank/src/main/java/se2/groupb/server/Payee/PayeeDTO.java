package se2.groupb.server.Payee;

public class PayeeDTO {

    private final String payeeName;
    private final String payeeAccountNumber;
    private final String payeeBIC;

    public PayeeDTO(String payeeName, String payeeAccountNumber, String payeeBIC) {
        this.payeeName = payeeName;
        this.payeeAccountNumber = payeeAccountNumber;
        this.payeeBIC = payeeBIC;
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
}
