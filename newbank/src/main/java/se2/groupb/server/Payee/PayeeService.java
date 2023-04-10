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
    public Payee getPayeeByDTO(PayeeDTO payeeDTO);

    /**
     * @param payeeName
     * @return
     */
    public Payee getPayeeByName(String payeeName);

    /**
     * @param payeeDTO
     * @return
     */
    public boolean addNewPayee(PayeeDTO payeeDTO);
}
