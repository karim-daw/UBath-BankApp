package se2.groupb.server.loanOffer;

import java.math.BigDecimal;
import java.util.UUID;


public class LoanOffer {
	// Domain model for loan offers
	
	private final UUID loanOfferID;
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final String lenderName;
	private String offerName;
	private final String lenderAccount; // account to debit loan principal from and credit loan repayment to
	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	private String minCreditScore;
	public boolean isActive; //a borrower has accepted it
	public boolean isTaken; //a borrower has accepted it
	
	public static final String loanOfferHeadings = "Offer" + "\t|" + "Lender" + "\t|" + "Amount" + "\t|" +
			"Interest" + "\t|" + "Duration" + "\t\t|" + "Installments" + "\t|" + "Min Credit Score" +"\n";
			
	//Constructor 1
	public LoanOffer(UUID lenderID, String lenderName, String offerName,String accountNumber, BigDecimal amount, 
			BigDecimal interestRate, Integer duration, String durationType, Integer installments,String minCreditScore) {
		this.loanOfferID = UUID.randomUUID();
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
		this.isActive = true;
		this.isTaken = false;
	}
	
	//Constructor 2
	public LoanOffer(LoanOfferDTO loanOfferDto) {
		this.loanOfferID = UUID.randomUUID();
		this.lenderID = loanOfferDto.getLenderID();
		this.lenderName = loanOfferDto.getLenderName();
		this.offerName = loanOfferDto.getOfferName();
		this.lenderAccount = loanOfferDto.getLenderAccount();
		this.principalAmount = loanOfferDto.getPrincipalAmount();
		this.interestRate = loanOfferDto.getInterestRate();
		this.duration = loanOfferDto.getDuration();
		this.durationType = loanOfferDto.getDurationType();
		this.installments = loanOfferDto.getInstallments();
		this.minCreditScore = loanOfferDto.getMinCreditScore();
		this.isActive = true;
		this.isTaken = false;
		
	}
	
	//Getters
	
	public UUID getLoanOfferID() {
		return this.loanOfferID;
	}

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

	public boolean isActive() {
		return isActive;
	}

	public boolean isTaken() {
		return isTaken;
	}
	
	public void setIsActive(boolean newStatus) {
		this.isActive = newStatus;
	}

	public void setIsTaken(boolean newStatus) {
		this.isTaken = newStatus;
	}
	
	@Override
	public String toString() {
		String str = offerName + "\t|" +
				lenderName + "\t|" +
				principalAmount.toString() + "\t|" +
				interestRate.toString() + "\t|" +
				Integer.toString(duration) + " " + durationType + "\t|" +
				Integer.toString(installments) + "\t|" +
				minCreditScore + "\n";
		return str;
	}
	
}