package se2.groupb.server.loan;

import java.math.BigDecimal;
import java.util.UUID;


//Customer must specify the following:
// 1) a source account: account number
// 2) a principal amount: validate sufficient balance?
// 3) an interest rate
// 4) a duration value: e.g. 5
// 5) a duration type: e.g. years
// 6) number of installments
// 7) minimum credit score required to accept offer

// To string method displays each offer as:
// Lender name + Amount + Interest + Duration + Duration Type + Installments
// Bhagy offers 5,000 at 5% over 5 years with 12 installments. Mimimum credit score: 700

public class LoanDTO {
	// Domain model for loan
	private final UUID loanOfferID;
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final String lenderAccount; // account to debit loan principal from and credit loan repayment to
	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	private String minCreditScore;
	public boolean isActive; //a borrower has accepted it
	public boolean isTaken; //a borrower has accepted it
	
	//Constructor
	public LoanDTO(UUID lenderID, String accountNumber, BigDecimal amount, BigDecimal interestRate, Integer duration,
			String durationType, Integer installments,String minCreditScore) {
		this.loanOfferID = UUID.randomUUID();
		this.lenderID = lenderID;
		this.lenderAccount = accountNumber;
		this.principalAmount = amount;
		this.interestRate = interestRate;
		this.duration = duration;
		this.durationType = durationType;
		this.installments = installments;
		this.minCreditScore = minCreditScore;
		this.isActive = true;
		this.isTaken = false;
	}
	
	//Getters
	
	public UUID getLoanOfferID() {
		return this.loanOfferID;
	}
	
	// To string method displays each offer as:
			// Lender name + Amount + Interest + Duration + Duration Type + Installments
			// Bhagy offers 5,000 at 5% over 5 years with 12 installments. Mimimum credit score: 700
}