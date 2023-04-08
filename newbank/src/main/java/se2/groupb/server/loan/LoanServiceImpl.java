package se2.groupb.server.loan;

import se2.groupb.server.customer.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import se2.groupb.server.account.*;
import se2.groupb.server.repository.*;

public class LoanServiceImpl implements LoanService {
    
	
	private final CustomerRepositoryImpl customerRepository;
	private final AccountRepositoryImpl accountRepository;
	private final LoanRepositoryImpl loanRepository;
	

	// Constructor
	public LoanServiceImpl(CustomerRepositoryImpl customerRepository, AccountRepositoryImpl accountRepository,
			LoanRepositoryImpl loanRepository) {
		
		this.customerRepository = customerRepository;
		this.accountRepository = accountRepository;
		this.loanRepository = loanRepository;
	}
	
	
	public LoanDTO newLoanOffer(UUID customerID, LoanDTO loanDto) {
		boolean success = loanRepository.saveLoanOffer(loanDto);
		if (success) {
			return loanDto;
		}
		return null;
	}

}	

