package se2.groupb.server.loan;

import java.math.BigDecimal;
import java.util.UUID;

public class LoanOffer {
	// Domain model for loan
	private final UUID loanOfferID;
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final String lenderAccount; // account to debit loan principal from and credit loan repayment to

	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	private Integer minCreditScore;
	public boolean isTaken; //a borrower has accepted it
	
	//Constructor
	public LoanOffer(UUID lenderID, String accountNumber, BigDecimal amount, BigDecimal interestRate) {
		this.loanOfferID = UUID.randomUUID();
		this.lenderID = lenderID;
		this.lenderAccount = accountNumber;
		this.principalAmount = amount;
		this.interestRate = interestRate;
		
		
		
	}
	
	
}
