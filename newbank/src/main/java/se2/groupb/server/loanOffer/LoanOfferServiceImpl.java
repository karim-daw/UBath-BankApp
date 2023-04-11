package se2.groupb.server.loanOffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

//import se2.groupb.server.account.Account;
import se2.groupb.server.repository.*;

public class LoanOfferServiceImpl implements LoanOfferService {
    
	
	private final CustomerRepositoryImpl customerRepository;
	//private final AccountRepositoryImpl accountRepository;
	private final LoanOfferRepositoryImpl loanOfferRepository;
	public static final String[] loanOffersColumns = {"NO","NAME","LENDER","AMOUNT","INTEREST","DURATION","INSTALLMENTS",
	"MIN CREDIT SCORE"};
	
	// Constructor
	public LoanOfferServiceImpl(CustomerRepositoryImpl customerRepository, AccountRepositoryImpl accountRepository,
			LoanOfferRepositoryImpl loanOfferRepository) {
		
		this.customerRepository = customerRepository;
		//this.accountRepository = accountRepository;
		this.loanOfferRepository = loanOfferRepository;
	}
	
	
	//Methods:

	/**
	 * All the Loan Offers in the Market
	 * @param
	 * @return Array List of Loan Offers
	 */
	/*
	public ArrayList<LoanOffer> getAllLoanOffers() {
		ArrayList<LoanOffer> offers = loanOfferRepository.getAllLoanOffers();
		return offers;
	}
	*/
	
	/**
	 * All the Loan Offers in the Market
	 * @param
	 * @return Array List of Loan Offers
	 */
	public Map<String,LoanOffer> getAllLoanOffers() {
		return loanOfferRepository.getAllLoanOffersMap();
	}
	
	
	/**
	 * String containing all loan offers
	 * @return String display of the Loan Offer Market
	 */
 	public String displayLoanOfferMarket() {
 		return displayLoanOffersMap(getAllLoanOffers());
	}
        
        
	/**
	 * The Customer's Loan Offer list
	 * @param customerID
	 * @return Array List of Loan Offers
	 */
	public Map<String,LoanOffer> getLoanOffers(UUID customerID) {
		return customerRepository.findLoanOffersMap(customerID);
	}
	
	/**
	 * Retuns a Map of the Loan Offers that were not created by the Customer
	 * @param customerID
	 * @return
	 */
	public Map<String,LoanOffer> getAcceptableOffers(UUID customerID){
		Map<String, LoanOffer> offersMap = new TreeMap<>();
    	int index =0;
    	
		for(Map.Entry<String, LoanOffer> offer : getAllLoanOffers().entrySet()){
			if (!offer.getValue().getLenderID().equals(customerID)) {
				index++;
				offersMap.put(String.valueOf(index), offer.getValue());
			}
		}
    	return offersMap;
	}
	
	
	/**
	 * String display containing of the Customer's loan offers
	 * 
	 * @return display string
	 */
 	public String displayLoanOffersByCustomer(UUID customerID) {
 		return displayLoanOffersMap(getLoanOffers(customerID));
	}
 	
 	/**
     * Helper method: String display of list
     * 
     * @param list
     * @return String display table of Loan Offer list
     */
    public String displayLoanOffersMap(Map<String,LoanOffer> map) {
		String str="";
        if (map.isEmpty()) {
        	str = "There are no loan offers.";
        } else {
        	int noOfChars = 120;
        	str += displayChars('-',noOfChars) + "\n"; 	
        	String strFormat = "|%1$-5s|%2$-20s|%3$-15s|%4$-10s|%5$-10s|%6$-20s|%7$-15s|%8$-16s|\n";
        	str += String.format(strFormat, (Object[])loanOffersColumns);
        	str += displayChars('-',noOfChars) + "\n";
        	//iterates over the list
        	for(Map.Entry<String, LoanOffer> mapKVpair : map.entrySet()){
        		ArrayList<String> offerList = mapKVpair.getValue().getLoanOfferRowList();
        		offerList.add(0, String.valueOf(mapKVpair.getKey()));
        		String[] offerRowData = offerList.toArray(new String[offerList.size()]);
        		str += String.format(strFormat, (Object[]) offerRowData);  
        	}
        	str += displayChars('-',noOfChars) + "\n";
        } 
        return str;
    }
    
	/**
     * Helper method: String display of list
     * 
     * @param list
     * @return String display table of Loan Offer list
     */
    public String displayLoanOffers(ArrayList<LoanOffer> list) {
		String str="";
        if (list.isEmpty()) {
        	str = "There are no loan offers.";
        } else {
        	int noOfChars = 120;
        	str += displayChars('-',noOfChars) + "\n"; 	
        	String strFormat = "|%1$-5s|%2$-20s|%3$-15s|%4$-10s|%5$-10s|%6$-20s|%7$-15s|%8$-16s|\n";
        	str += String.format(strFormat, (Object[])loanOffersColumns);
        	str += displayChars('-',noOfChars) + "\n";
        	//iterates over the list
        	int i =0;
        	for(LoanOffer offer: list){
        		i++;
        		ArrayList<String> offerList = offer.getLoanOfferRowList();
        		offerList.add(0, String.valueOf(i));
        		String[] offerRowData = offerList.toArray(new String[offerList.size()]);
        		str += String.format(strFormat, (Object[]) offerRowData);  
        	}
        	str += displayChars('-',noOfChars) + "\n";
        } 
        return str;
    }
	
    // Helper method for printing out the same characters multiple times
 	public String displayChars(char myChar, int number) {
 		char[] myChars = new char[number];
 		Arrays.fill(myChars, myChar);
 		return new String(myChars);
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
    
    /**
     * removes a Loan Offer from the Database
     * 
     * @return true if success or false if failure
     */
    public boolean removeOfferFromMarket(LoanOffer loanOffer) {
        return loanOfferRepository.remove(loanOffer);
    }
    
}	

