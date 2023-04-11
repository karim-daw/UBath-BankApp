package se2.groupb.server.Payee;

import java.util.UUID;

public interface PayeeService {

    /**
     * @param payeeID
     * @return
     */
    public Payee getPayeeByID(UUID payeeID);

    /**
     * @param payeeDTO
     * @return
     */
    public boolean addNewPayee(UUID customerID, PayeeDTO payeeDTO);

    public String displayPayees(UUID customerID);

    public String createPayee(UUID customerID);
}
