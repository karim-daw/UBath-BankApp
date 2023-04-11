package se2.groupb.server.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import se2.groupb.server.loanOffer.LoanOffer;
import se2.groupb.server.loanOffer.LoanOfferDTO;


public class LoanOfferRepositoryImpl implements EntityRepository<LoanOffer, LoanOfferDTO>{

	// Temp HashMap Loan Repo
    private final HashMap<String, LoanOffer> theLoanOffers;
  
    // private final HashMap<String, Account> theAccounts;
    // Temp constructor using HashMap as Customer Repo
    public LoanOfferRepositoryImpl(HashMap<String, LoanOffer> loanOffers) {
        this.theLoanOffers = loanOffers;
    }
    
   
    //Methods:
    /**
     * @param loanOfferID
     * @return Loan Offer from database
     */
    @Override
    public LoanOffer findByID(UUID loanOfferID) {
        return theLoanOffers.get(loanOfferID.toString());
    }
    
    /**
     * Returns all the Loan Offers in the MarketPlace as a List
     * 
     * @return ArrayList<LoanOffer>
     */
    /*
    public ArrayList<LoanOffer> getAllLoanOffers() {
    	ArrayList<LoanOffer> list = new ArrayList<>();
    	for (Map.Entry<String, LoanOffer> offer : theLoanOffers.entrySet()) {
    		list.add(offer.getValue());
    	}
    	return list;
    }
    */
    
	/**
     * Returns all the Loan Offers in the MarketPlace as a numbered Map
     * 
     * @return Map<String,LoanOffer>
     */
    public Map<String,LoanOffer> getAllLoanOffersMap() {
    	Map<String, LoanOffer> offersMap = new TreeMap<>();
    	int index =0;
    	for (Map.Entry<String, LoanOffer> offer : theLoanOffers.entrySet()) {
    		index++;
    		offersMap.put(String.valueOf(index), offer.getValue());
    		
    	}
    	return offersMap;
    }
        	
    /**
     * saves Loan Offer to Database
     * 
     * @return true if success or false if failure
     */
    @Override
    public boolean save(LoanOffer loanOffer) {
        // check if loan offer exists in data store
        if (findByID(loanOffer.getLoanOfferID()) == null) {
            // if it doesn't exist put it in store
        	theLoanOffers.put(loanOffer.getLoanOfferID().toString(), loanOffer);
            return true;
        }
        return false;
    }
    
    
    /**
     * removes a Loan Offer from the Database
     * 
     * @return true if success or false if failure
     */
    public boolean remove(LoanOffer loanOffer) {
        // check if loan offer exists in data store
        if (findByID(loanOffer.getLoanOfferID()) == null) {
            return false;
        }
        else {
        	theLoanOffers.remove(loanOffer.getLoanOfferID().toString(), loanOffer);
        	return true;
        }
    }
    
    /**
     * 
     * @param loanOfferDto
     * @return Loan Offer from database
     */
    @Override
    public LoanOffer findByDTO(LoanOfferDTO LoanOfferDto) {
    	return null;
    }

    /**
     * @param
     * @return
     */
    @Override
    public LoanOffer findByName(String entityName) {
    	return null;
    }

}