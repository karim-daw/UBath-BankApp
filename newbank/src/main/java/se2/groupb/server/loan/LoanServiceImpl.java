package se2.groupb.server.loan;

import se2.groupb.server.customer.*;
import se2.groupb.server.loanOffer.LoanOffer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import se2.groupb.server.account.*;
import se2.groupb.server.repository.*;

public class LoanServiceImpl implements LoanService {
    
	private final CustomerRepositoryImpl customerRepository;
	private final LoanRepositoryImpl loanRepository;
	public static final String[] loansColumns = {"NO","LENDER","BORROWER","PRINCIPAL","INTEREST","DURATION","START","END",
			"INSTALL NO","INTALL AMOUNT","STATUS"};
	
	// Constructor
	public LoanServiceImpl(CustomerRepositoryImpl customerRepository,LoanRepositoryImpl loanRepository) {
		this.customerRepository = customerRepository;
		this.loanRepository = loanRepository;
	}
	
	/**
	 * The Customer's Loans list
	 * @param customerID
	 * @return Map of Loans
	 */
	public Map<String,Loan> getLoans(UUID customerID) {
		return customerRepository.findLoansMap(customerID);
	}
	
	/**
	 * String display containing of the Customer's loans
	 * 
	 * @return display string
	 */
 	public String displayLoansByCustomer(UUID customerID) {
 		return displayLoansMap(getLoans(customerID));
	}
 	
 	/**
     * Helper method: String display of list
     * 
     * @param list
     * @return String display table of Loans list
     */
    public String displayLoansMap(Map<String,Loan> map) {
		String str="";
        if (map.isEmpty()) {
        	str = "There are no loans.";
        } else {
        	int noOfChars = 120;
        	str += displayChars('-',noOfChars) + "\n"; 	
        	String strFormat = "|%1$-5s|%2$-20s|%3$-20s|%4$-10s|%5$-10s|%6$-20s|%7$-10s|%8$-10s|%9$-10s|%10$-10s|%11$-20s|\n";
        	str += String.format(strFormat, (Object[])loansColumns);
        	str += displayChars('-',noOfChars) + "\n";
        	//iterates over the list
        	for(Map.Entry<String, Loan> mapKVpair : map.entrySet()){
        		ArrayList<String> loanList = mapKVpair.getValue().getLoanRowList();
        		loanList.add(0, String.valueOf(mapKVpair.getKey()));
        		String[] loanRowData = loanList.toArray(new String[loanList.size()]);
        		str += String.format(strFormat, (Object[]) loanRowData);  
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
     * Add new Loan to the Database
     * @param customerID
     * @param newLoan
     * @return
     */
	public boolean createLoan(UUID customerID, Loan newLoan) {
		return loanRepository.save(newLoan);
	}

}	

