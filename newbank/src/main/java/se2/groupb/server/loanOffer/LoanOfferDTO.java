package se2.groupb.server.loanOffer;

import java.math.BigDecimal;
import java.util.UUID;

public class LoanOfferDTO {
	
	// Fields: the information gained from the user
	
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final String lenderName;
	private final String lenderAccount; // account to debit loan principal from and credit loan repayment to
	private String offerName;
	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	private String minCreditScore;
	
	//Constructor
	public LoanOfferDTO(UUID lenderID, String lenderName,String offerName,String accountNumber, BigDecimal amount, BigDecimal interestRate, Integer duration,
			String durationType, Integer installments,String minCreditScore) {
		
		this.lenderID = lenderID;
		this.lenderName = lenderName;
		this.offerName = offerName;
		this.lenderAccount = accountNumber;
		this.principalAmount = amount;
		this.interestRate = interestRate;
		this.duration = duration;
		this.durationType = durationType;
		this.installments = installments;
		this.minCreditScore = minCreditScore;
	}
	
	//Getters:
	public UUID getLenderID() {
		return lenderID;
	}
	
	public String getLenderName() {
		return lenderName;
	}
	
	public String getOfferName() {
		return offerName;
	}
	
	public String getLenderAccount() {
		return lenderAccount;
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

	public String getMinCreditScore() {
		return minCreditScore;
	}
	
	@Override
	public String toString() {
		String str = "Name: " + offerName + "\n" +
				"Lender: " + lenderName + "\n" +
				"Account: " + lenderAccount + "\n" +
				"Principal Amount: " + principalAmount + "\n" + 
				"Interest Rate: " + interestRate + "\n" +
				"Duration: " + duration + " " + durationType + "\n" +
				"Number of Installments: " + installments + "\n" + 
				"Minimum Credit Score: " + minCreditScore + "\n";
		return str;
	}
}