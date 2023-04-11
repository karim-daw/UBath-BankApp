package se2.groupb.server.loanOffer;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.ArrayList;
import se2.groupb.server.customer.Customer;


public class LoanOffer {
	// Domain model for loan offers
	
	private final UUID loanOfferID;
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final String lenderName;
	private String offerName;
	private final UUID lenderAccountID; // account to debit loan principal from and credit loan repayment to
	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	private String minCreditScore;
	public boolean isActive; //a borrower has accepted it
	public boolean isTaken; //a borrower has accepted it
	
	public static final String[] loanOfferColumns = {"NAME","LENDER","AMOUNT","INTEREST","DURATION","INSTALLMENTS",
	"MIN CREDIT SCORE"};
	
	//Constructor 1
	public LoanOffer(UUID lenderID, String lenderName, String offerName,UUID lenderAccountID, BigDecimal amount, 
			BigDecimal interestRate, Integer duration, String durationType, Integer installments,String minCreditScore) {
		this.loanOfferID = UUID.randomUUID();
		this.lenderID = lenderID;
		this.lenderName = lenderName;
		this.offerName = offerName;
		this.lenderAccountID = lenderAccountID;
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
		this.lenderAccountID = loanOfferDto.getLenderAccountID();
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
	
	
	public UUID getLenderAccountID() {
		return lenderAccountID;
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
	
	//Helper method for toString()
	public ArrayList<String> getLoanOfferRowList() {
		ArrayList<String> l = new ArrayList<>();
		
		l.add(offerName);
		l.add(lenderName);
		l.add(principalAmount.toString());
		l.add(interestRate.toString()+"%");
		l.add(Integer.toString(duration)+ " " + durationType);
		l.add(Integer.toString(installments));
		l.add(Customer.creditScores.get(minCreditScore));
		
		return l;
	}
	
	@Override
	public String toString() {
		String[] offerRowData = getLoanOfferRowList().toArray(new String[getLoanOfferRowList().size()]);
		String strFormat = "|%1$-20s|%2$-15s|%3$-10s|%4$-10s|%5$-20s|%6$-15s|%7$-16s|\n";
    	String str = String.format(strFormat, (Object[])loanOfferColumns);
		str += String.format(strFormat, (Object[]) offerRowData);
		return str;
	}
	
}