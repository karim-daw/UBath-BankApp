package se2.groupb.server.loan;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.time.LocalDate;

public class Loan {

    // Domain model for loan
	private final UUID loanID;
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final String lenderName;
	private final UUID borrowerID; //the borrower
	private final String borrowerName;
	private final UUID lenderAccountID; // account to debit loan principal from and credit loan repayment to
	private final UUID borrowerAccountID; // account to credit principal with and debit loan repayment from
	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	private final LocalDate startDate;
	private final LocalDate endDate;
	private BigDecimal installmentPayment;
	private boolean isActive; //started and not ended
	
	public static final String[] loanColumns = {"LENDER","BORROWER","PRINCIPAL","INTEREST","DURATION","START","END",
	"INSTALL NO","INTALL AMOUNT","STATUS"};
	
	public static Map<String, String> loanDurations;
	static{
		loanDurations = new TreeMap<String,String>();
		loanDurations.put("1", "Days");
		loanDurations.put("2", "Weeks");
		loanDurations.put("3","Months");
		loanDurations.put("4","Years");
	}
		
	//Constructor 1
	public Loan(UUID loanOfferID, UUID lenderID, String lenderName, UUID borrowerID, String borrowerName, UUID lenderAccountID,
			UUID borrowerAccountID, BigDecimal principalAmount,BigDecimal interestRate, Integer duration, String durationType, 
			Integer installments, String minCreditScore) {
		
        this.loanID = UUID.randomUUID();
        this.lenderID = lenderID;
        this.lenderName = lenderName;
        this.borrowerID = borrowerID;
        this.borrowerName = borrowerName;
        this.lenderAccountID = lenderAccountID;
        this.borrowerAccountID = borrowerAccountID;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
		this.duration = duration;
		this.durationType = durationType;
		this.installments = installments;
		this.startDate = LocalDate.now();
		this.endDate = calculateEndDate();
		this.isActive = isActive();
        this.installmentPayment = calculateRepaymentAmount();

    }
	
	//// constructor 2
	public Loan(LoanDTO loanDTO) {
		this.loanID = UUID.randomUUID();
		this.lenderID = loanDTO.getLenderID();
		this.lenderName = loanDTO.getLenderName();
		this.borrowerID = loanDTO.getBorrowerID();
		this.borrowerName = loanDTO.getBorrowerName();
		this.lenderAccountID = loanDTO.getLenderAccountID();
		this.borrowerAccountID = loanDTO.getBorrowerAccountID();
		this.principalAmount = loanDTO.getPrincipalAmount();
		this.interestRate = loanDTO.getInterestRate();
		this.duration = loanDTO.getDuration();
		this.durationType = loanDTO.getDurationType();
		this.installments = loanDTO.getInstallments();
		this.startDate = LocalDate.now();
		this.endDate = calculateEndDate();
		this.isActive = isActive();
		this.installmentPayment = calculateRepaymentAmount();
	}
		
		
	//Getters
	public UUID getLoanID() {
		return loanID;
	}

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

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	public boolean isActive() {
		LocalDate today = LocalDate.now();
		if ((today.isAfter(startDate)) && (today.isBefore(endDate))) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public BigDecimal getInstallmentPayment() {
		return installmentPayment;
	}
	
	//Calculator Methods:
	
	/**
	 * the loan's end date
	 * @return
	 */
	public LocalDate calculateEndDate() {
		switch (durationType) {
			case "Days":
				return startDate.plusDays(duration);
			case "Weeks":
				return startDate.plusDays(duration*7);
			case "Months":
				return startDate.plusMonths(duration);
			case "Years":
				return startDate.plusYears(duration);
			default:
				return null;
		}
	}
	
	
	/**
	 * Calculate the repayment amount based on the principal and the interest rate
	 * @return
	 */
	public BigDecimal calculateRepaymentAmount() {
		BigDecimal percentInterest = interestRate.divide(new BigDecimal("100"));
		BigDecimal factor = percentInterest.add(BigDecimal.ONE);
		return principalAmount.multiply(factor);
	}

	
    /**
     * Transfers principal from the lender's account to the borrower's account.
     *
     * @return true if successful, otherwise false
     */
	/*
    public boolean transferPrincipal() {
        return accountService.transfer(lenderAccountID, borrowerAccountID , principalAmount);
    }
    */

    /**
     * Transfers the repayment amount from the borrower's account to the lender's account.
     *
     * @return true if successful, otherwise false
     */
	/*
    public boolean transferRepayment(){
    	return accountService.transfer(borrowerAccountID, lenderAccountID , repaymentAmount);
    }
    */
	
	//Helper method for toString()
		public ArrayList<String> getLoanRowList() {
			ArrayList<String> l = new ArrayList<>();
			l.add(lenderName);
			l.add(borrowerName);
			l.add(principalAmount.toString());
			l.add(interestRate.toString()+"%");
			l.add(Integer.toString(duration)+ " " + durationType);
			l.add(startDate.toString());
			l.add(endDate.toString());
			l.add(Integer.toString(installments));
			l.add(installmentPayment.toString());
			if (isActive) {
				l.add("Active");
			}
			else {
				l.add("Non-active");
			}
			return l;
		}
		
		@Override
		public String toString() {
			String[] loanRowData = getLoanRowList().toArray(new String[getLoanRowList().size()]);			
			String strFormat = "|%1$-20s|%2$-20s|%3$-10s|%4$-10s|%5$-20s|%6$-10s|%7$-10s|%8$-10s|%9$-10s|%10$-20s|\n";
	    	String str = String.format(strFormat, (Object[])loanColumns);
			str += String.format(strFormat, (Object[]) loanRowData);
			return str;
		}

}
