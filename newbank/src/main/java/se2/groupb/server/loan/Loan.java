package se2.groupb.server.loan;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.Calendar;
import java.time.LocalDate;
import java.sql.Date;
import se2.groupb.server.account.AccountServiceImpl;
import se2.groupb.server.customer.CustomerDTO;

public class Loan {

    // Domain model for loan
	private final UUID loanID;
	private final UUID lenderID; //lender creates the loan: derived from lenderAccountID
	private final UUID borrowerID; //the borrower
	private final UUID lenderAccountID; // account to debit loan principal from and credit loan repayment to
	private final UUID borrowerAccountID; // account to credit principal with and debit loan repayment from
	private BigDecimal principalAmount; //the original amount
	private BigDecimal interestRate;
	private Integer duration;
	private String durationType; //days,weeks,months,years
	private Integer installments;
	private Integer minCreditScore;
	public boolean isTaken; //a borrower has accepted it
	
	private AccountServiceImpl accountService;
	
	//Constructor 1
	public Loan(UUID lenderID, UUID lenderAccountID,UUID borrowerAccountID, BigDecimal principalAmount,
			BigDecimal interestRate, Date startDate, Date endDate) {
		
        this.loanID = UUID.randomUUID();
        this.lenderID = lenderID;
        this.lenderAccountID = lenderAccountID;
        this.borrowerAccountID = borrowerAccountID;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        
        this.installmentPayment = repaymentPlan();
        this.totalInterest = totalInterestCalculation();
        
    }
	
	//// constructor 2
	public Loan(LoanDTO loanDto) {
		this.loanID = UUID.randomUUID();
		this.lenderID = loanDto.getLenderID();
		this.lenderAccountID = loanDto.getLenderAccountID();
		this.password = customerDto.getPassword();
		this.loggedInStatus = false;
		account
		
		
	//Getters
	
	/**
	 * The unique loan id
	 * @return
	 */
	public UUID getLoanID() {
		return loanID;
	}

	/**
	 * The customer id of the person who created the loan
	 * @return
	 */
	public UUID getCreatorID() {
		return creatorID;
	}

	/**
	 * The lender's account id to debit loan principal from and credit loan repayment amount to 
	 * @return
	 */
	public UUID getLenderAccountID() {
		return lenderAccountID;
	}

	
	/**
	 * The borrower's account id to credit loan principal to and debit loan repayment amount from 
	 * @return
	 */
	public UUID getBorrowerAccountID() {
		return borrowerAccountID;
	}

	/**
	 * The loan's principal amount (the amount credited to the borrower on the start date)
	 * @return
	 */
    public BigDecimal getPrincipal() {
		return principalAmount;
	}

    /**
	 * The loan's interest rate for the term
	 * @return
	 */
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	
	/**
	 * Calculate the repayment amount based on the principal and the interest rate
	 * @return
	 */
	public BigDecimal getRepaymentAmount() {
		BigDecimal percentInterest = interestRate.divide(new BigDecimal("100"));
		BigDecimal factor = percentInterest.add(BigDecimal.ONE);
		return principalAmount.multiply(factor);
	}

	/**
	 * the loan's start date
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * the loan's end date
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	public boolean isLoanStarted() {
		if ((today.after(startDate)) && (today.before(endDate))){
			return true;
		}
		return false;
	}


	public boolean isLoanFinished() {
		if (today.after(endDate)){
			return true;
		}
		return false;
	}

	/**
     * Retrieves the lender's customer UUID
     * @return UUID
     */
    public UUID getlenderID() {
        // Retrieve customerID via accountID.
        return accountService.getCustomer(lenderAccountID);
    }

    /**
     * Retrieves the borrower's customer UUID
     * @return UUID.
     */
    public UUID getborrowerID() {
        return accountService.getCustomer(borrowerAccountID);
    }

    /**
     * Transfers principal from the lender's account to the borrower's account.
     *
     * @return true if successful, otherwise false
     */
    public boolean transferPrincipal() {
        return accountService.transfer(lenderAccountID, borrowerAccountID , principalAmount);
    }

    /**
     * Transfers the repayment amount from the borrower's account to the lender's account.
     *
     * @return true if successful, otherwise false
     */
    public boolean transferRepayment(){
    	return accountService.transfer(borrowerAccountID, lenderAccountID , repaymentAmount);
    }

}
