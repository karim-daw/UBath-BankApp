package se2.groupb.server.repository;

import java.util.UUID;

import se2.groupb.server.loan.Loan;
import se2.groupb.server.loan.LoanDTO;

public class LoanRepositoryImpl implements EntityRepository<Loan, LoanDTO> {

    @Override
    public Loan findByDTO(LoanDTO entityDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Loan findByID(UUID entityID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Loan findByName(String entityName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Loan entity) {
        // TODO Auto-generated method stub
        return false;
    }

}
