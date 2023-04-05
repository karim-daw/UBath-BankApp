package se2.groupb.server.loan;

import java.util.ArrayList;
import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.account.AccountService;
import se2.groupb.server.customer.CustomerService;

public class LoanController {
	private final CustomerService customerService;
	private final AccountService accountService;
	private final LoanService loanService;
	private UserInput comms;

	// Constructor
	public LoanController(CustomerService customerService, AccountService accountService, LoanService loanService,
			UserInput comms) {
		
		this.customerService = customerService;
		this.accountService = accountService;
		this.loanService = loanService;
		this.comms = comms;
	}
	

    // add funcitonality that interacts with the user and prints stuff to them
	public String newLoanOffer(UUID customerID) {
			
		// Customer must specify the following:
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
		
		LoanOffer loanOffer = loanService.newLoanOffer();
		
		return "Your loan offer has been added to the MarketPlace";
	}
		
		
	public String viewLoanMarket(UUID customerID) {
		String s="";
		ArrayList<LoanOffer> loanOffers = loanService.getLoanOffers();
		
		for (LoanOffer l: loanOffers) {
			s+=l.toString();
		}
		return s;
		
	}
		
	public String acceptLoanOffer(UUID customerID) {
		// Customer must:
		// 1) Meet the minimum credit score
		// 2) Specify a destination account: by account number
		
		// System must:
		// 1) remove the loan offer from market
		// 2) convert loan offer to loan object and add it to borrower's and lender's loan list
		// 3) create the credit and debit transactions for the principal transfer
		// 4) generate an automated payment schedule based on the start date, the duration, the number of installments
		//    and the interest rate
		
		String response ="The loan amount has been credited to your specified account.\n" +
				"The loan repayment schedule has been generated";
		return response;
	}
}
