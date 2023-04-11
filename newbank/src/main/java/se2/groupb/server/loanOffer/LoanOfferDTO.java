package se2.groupb.server.loanOffer;

import java.math.BigDecimal;
import java.util.UUID;

public class LoanOfferDTO {
	
	// Fields: the information gained from the user
	
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final String lenderName;
	private String offerName;
	private final UUID lenderAccountID; // account to debit loan principal from and credit loan repayment to
	private final String lenderAccountNumber;
	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	private String minCreditScore;
	
	//Constructor
	public LoanOfferDTO(UUID lenderID, String lenderName,String offerName,UUID lenderAccountID, String lenderAccountNumber, 
			BigDecimal amount, BigDecimal interestRate, Integer duration, String durationType, Integer installments,
			String minCreditScore) {
		
		this.lenderID = lenderID;
		this.lenderName = lenderName;
		this.offerName = offerName;
		this.lenderAccountID = lenderAccountID;
		this.lenderAccountNumber = lenderAccountNumber;
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
	
	public UUID getLenderAccountID() {
		return lenderAccountID;
	}
	
	public String lenderAccountNumber() {
		return lenderAccountNumber;
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
	
	public String getLenderAccountName() {
		
		return "";
	}
	
	@Override
	public String toString() {
		String str = "Name: " + offerName + "\n" +
				"Lender: " + lenderName + "\n" +
				"Account: " + lenderAccountNumber + "\n" +
				"Principal Amount: " + principalAmount + "\n" + 
				"Interest Rate: " + interestRate + "\n" +
				"Duration: " + duration + " " + durationType + "\n" +
				"Number of Installments: " + installments + "\n" + 
				"Minimum Credit Score: " + minCreditScore + "\n";
		return str;
	}
}