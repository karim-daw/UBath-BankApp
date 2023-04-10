package se2.groupb.server.repository;

import java.util.UUID;

import se2.groupb.server.Payee.Payee;
import se2.groupb.server.Payee.PayeeDTO;

public class PayeeRepositoryImpl implements EntityRepository<Payee, PayeeDTO> {

    @Override
    public Payee findByDTO(PayeeDTO payeeDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Payee findByID(UUID payeeID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Payee findByName(String payeeName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Payee payee) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(Payee payee) {
        // TODO Auto-generated method stub
        return false;
    }

}
