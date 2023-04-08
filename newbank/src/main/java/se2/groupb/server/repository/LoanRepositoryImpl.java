package se2.groupb.server.repository;

import java.util.HashMap;
import java.util.UUID;

//import se2.groupb.server.account.Account;
//import se2.groupb.server.customer.Customer;
import se2.groupb.server.loan.Loan;
import se2.groupb.server.loan.LoanDTO;
//import se2.groupb.server.loanOffer.LoanOffer;
//import se2.groupb.server.loanOffer.LoanOfferDTO;

public class LoanRepositoryImpl implements EntityRepository<Loan, LoanDTO>{

	// Temp HashMap Loan Repo
    private final HashMap<String, Loan> theLoans;

    // private final HashMap<String, Account> theAccounts;
    // Temp constructor using HashMap as Customer Repo
    public LoanRepositoryImpl(HashMap<String, Loan> loans) {
        this.theLoans = loans;
    }
    
    @Override
    public Loan findByID(UUID loanID) {
        return theLoans.get(loanID.toString());
    }
    
    /**
     * 
     * @param customerDto
     * @return customer from database
     */
    @Override
    public Loan findByDTO(LoanDTO loanDTO) {
    	return null;
    }

    /**
     * @param customerDto
     * @return
     */
    @Override
    public Loan findByName(String loanName) {
    	return null;
    }
    
    @Override
    public boolean save(Loan newLoan) {
        // check if loan offer exists in data store
        if (findByID(newLoan.getLoanID()) == null) {
            // if it doesn't exist put it in store
        	theLoans.put(newLoan.getLoanID().toString(), newLoan);
            return true;
        }
        return false;
    }
 
}
