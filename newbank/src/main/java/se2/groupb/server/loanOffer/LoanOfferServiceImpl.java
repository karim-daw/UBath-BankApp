package se2.groupb.server.loanOffer;

import java.util.ArrayList;
import java.util.UUID;

import se2.groupb.server.account.Account;
import se2.groupb.server.repository.*;

public class LoanOfferServiceImpl implements LoanOfferService {
    
	
	private final CustomerRepositoryImpl customerRepository;
	//private final AccountRepositoryImpl accountRepository;
	private final LoanOfferRepositoryImpl loanOfferRepository;
	

	// Constructor
	public LoanOfferServiceImpl(CustomerRepositoryImpl customerRepository, AccountRepositoryImpl accountRepository,
			LoanOfferRepositoryImpl loanOfferRepository) {
		
		this.customerRepository = customerRepository;
		//this.accountRepository = accountRepository;
		this.loanOfferRepository = loanOfferRepository;
	}
	
	
	//Methods:
	
	/**
	 * The Customer's Loan Offer list
	 * @param customerID
	 * @return Array List of Loan Offers
	 */
	public ArrayList<LoanOffer> getLoanOffers(UUID customerID) {
		ArrayList<LoanOffer> offers = customerRepository.findLoanOffers(customerID);
		return offers;

	}
	
	/**
     * String of the Customer's loan offers
     * 
     * @param customerID
     * @return String list of the Customer's LoanOffers
     */
    public String displayLoanOffersByCustomer(UUID customerID) {
		
        if (getLoanOffers(customerID).isEmpty()) {
            return "You have no loan offers to display.";
        } else {
        	String s = LoanOffer.loanOfferHeadings +"\n";
    		for (LoanOffer offer : getLoanOffers(customerID)) {
    			s += offer.toString();
    		}
    		return s;
        }
    }
	
	/**
	 * String containing all loan offers
	 * @return
	 */
	public String displayLoanOfferMarket() {
		return loanOfferRepository.displayAllLoanOffers();	
	}
	
    
    /**
     * Returns the Customer's Loan Offer that matches the desired Name
     * @param customerID
     * @param offerName
     * @return
     */
	public LoanOffer getLoanOfferByName(UUID customerID, String offerName) {
		return customerRepository.findLoanOfferByName(customerID,offerName);
	}
	
	/**
     * Return true if Customer has a Loan Offer with the specified Name
     * @param customerID
     * @param offerName
     * @return true if customer has loan offer with name, else false
     */
	public boolean hasLoanOfferName(UUID customerID, String offerName) {
		if (getLoanOfferByName(customerID,offerName)==null){
    		return false;
    	}
    	return true;
	}
	
    
	/**
     * Returns the new Loan Offer object which has been added to the Loan Offer Database
     * 
     * @param customerID
     * @param loanOfferDto
     * @return LoanOffer
     */
    public LoanOffer addNewLoanOffer(UUID customerID, LoanOfferDTO loanOfferDto) {
    	LoanOffer newLoanOffer = new LoanOffer(loanOfferDto);

		boolean success = loanOfferRepository.save(newLoanOffer);
		if (success) {
			return newLoanOffer;
		}
		return null;
	}
    
}	

