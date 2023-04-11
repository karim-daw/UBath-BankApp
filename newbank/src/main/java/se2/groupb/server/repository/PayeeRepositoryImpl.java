package se2.groupb.server.repository;

import java.util.HashMap;
import java.util.UUID;

import se2.groupb.server.Payee.Payee;
import se2.groupb.server.Payee.PayeeDTO;

public class PayeeRepositoryImpl implements EntityRepository<Payee, PayeeDTO> {

    // fields
    private final HashMap<String, Payee> thePayees;

    // constrtuctor
    public PayeeRepositoryImpl(HashMap<String, Payee> thePayees) {
        this.thePayees = thePayees;
    }

    // getter
    public HashMap<String, Payee> getThePayees() {
        return thePayees;
    }

    @Override
    public Payee findByDTO(PayeeDTO payeeDTO) {
        // return thePayees.get(payeeID.toString());
        return null;
    }

    @Override
    public Payee findByID(UUID payeeID) {
        return thePayees.get(payeeID.toString());
    }

    @Override
    public Payee findByName(String payeeName) {
        Payee payee = null;

        for (HashMap.Entry<String, Payee> p : thePayees.entrySet()) {
            String payeeUsername = p.getValue().getPayeeName();
            if ((payeeName.equals(payeeUsername))) {
                payee = p.getValue();
                break;
            }
        }
        return payee;
    }

    @Override
    public boolean save(Payee newPayee) {
        try {
            if (findByID(newPayee.getPayeeID()) == null) {
                thePayees.put(newPayee.getPayeeID().toString(), newPayee);
                return true;
            } else {
                return false; // payee already exists
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // error occurred
        }
    }

    @Override
    public boolean update(Payee newPayee) {
        try {
            if (findByID(newPayee.getPayeeID()) != null) {
                thePayees.put(newPayee.getPayeeID().toString(), newPayee);
                return true;
            } else {
                return false; // payee already exists
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // error occurred
        }
    }

}
