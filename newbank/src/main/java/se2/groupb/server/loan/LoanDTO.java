package se2.groupb.server.loan;

import java.math.BigDecimal;
import java.util.UUID;
import se2.groupb.server.loanOffer.LoanOffer;

public class LoanDTO {
	// Domain model for Loan DTO
	
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final String lenderName;
	private final UUID borrowerID;
	private final String borrowerName;
	private final UUID lenderAccountID; // account to debit loan principal from and credit loan repayment to
	private final UUID borrowerAccountID;
	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	
	//Constructor1
	public LoanDTO(UUID lenderID, String lenderName, UUID borrowerID, String borrowerName, UUID lenderAccountID, UUID borrowerAccountID, BigDecimal amount, 
			BigDecimal interestRate, Integer duration,String durationType, Integer installments) {
		
		this.lenderID = lenderID;
		this.lenderName = lenderName;
		this.borrowerID = borrowerID;
		this.borrowerName = borrowerName;
		this.lenderAccountID = lenderAccountID;
		this.borrowerAccountID = borrowerAccountID;
		this.principalAmount = amount;
		this.interestRate = interestRate;
		this.duration = duration;
		this.durationType = durationType;
		this.installments = installments;
	}
	
	// Constructor 2:
		public LoanDTO(UUID borrowerID, String borrowerName, UUID borrowerAccountID, LoanOffer offer) {
			
			this.lenderID = offer.getLenderID();
			this.lenderName = offer.getLenderName();
			this.borrowerID = borrowerID;
			this.borrowerName = borrowerName;
			this.lenderAccountID = offer.getLenderAccountID();
			this.borrowerAccountID = borrowerAccountID;
			this.principalAmount = offer.getPrincipalAmount();
			this.interestRate = offer.getInterestRate();
			this.duration = offer.getDuration();
			this.durationType = offer.getDurationType();
			this.installments = offer.getInstallments();
		}
	
		
	//Getters
	
	public UUID getLenderID() {
		return lenderID;
	}
	
	public String getLenderName() {
		return lenderName;
	}
	
	public UUID getBorrowerID() {
		return borrowerID;
	}
	
	public String getBorrowerName() {
		return borrowerName;
	}
	
	public UUID getLenderAccountID() {
		return lenderAccountID;
	}

	public UUID getBorrowerAccountID() {
		return borrowerAccountID;
	}

	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public Integer getDuration() {
		return duration;
	}

	public String getDurationType() {
		return durationType;
	}

	public Integer getInstallments() {
		return installments;
	}
}