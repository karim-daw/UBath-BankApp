package se2.groupb.server.loan;

public class LoanServiceImpl implements LoanService {
    
	
	private final CustomerRepositoryImpl customerRepository;
	private final AccountRepositoryImpl accountRepository;
	private final LoanRepositoryImpl loanRepository;
	
	//Map<String, Integer> accountTypeLimits = Account.accountTypeLimits;

	// Constructor
	public LoanServiceImpl(CustomerRepositoryImpl customerRepository, AccountRepositoryImpl accountRepository,
			LoanRepositoryImpl loanRepository) {
		
		this.customerRepository = customerRepository;
		this.accountRepository = accountRepository;
		this.loanRepository = loanRepository;
	}
	
	
	
	
}
