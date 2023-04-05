package se2.groupb.server.repository;

import java.util.HashMap;
import java.util.UUID;

import se2.groupb.server.customer.Customer;
import se2.groupb.server.loan.Loan;
import se2.groupb.server.loan.LoanDTO;

public class LoanRepositoryImpl implements EntityRepository<Loan, LoanDTO> {

	// Temp HashMap Loan Repo
    private final HashMap<String, LoanDTO> theLoanOffers;
    private final HashMap<String, Loan> theLoans;

    // private final HashMap<String, Account> theAccounts;
    // Temp constructor using HashMap as Customer Repo
    public LoanRepositoryImpl(HashMap<String, LoanDTO> loanDTOs, HashMap<String, Loan> loans) {
        this.theLoanOffers = loanDTOs;
        this.theLoans = loans;
    }

	
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
