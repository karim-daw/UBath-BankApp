package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionService {

    /**
     * method takes care of the MOVE protocol
     * 
     * MOVE <Amount> <From> <To>
     * e.g. MOVE 100 Main Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerID
     * @param requestArray
     * @return boolean
     */
    public boolean executeMove(UUID fromAccountID, UUID toAccount, BigDecimal amount);

    /**
     * 
     * method takes care of the PAY feature indicated below, gven and
     * customer
     * (payer) and a requested payee, this will transaction money from these
     * accounts
     * and update balances accordingly
     * 
     * PAY <Person/Company> <Ammount>
     * e.g. PAY John 100
     * Returns SUCCESS or FAIL
     * 
     * @param sourceAccount
     * @param targetAccount
     * @param amount
     * @return boolean
     */
    public boolean executePay(UUID fromAccountID, UUID toAccountID, BigDecimal amount,String reference);

}
