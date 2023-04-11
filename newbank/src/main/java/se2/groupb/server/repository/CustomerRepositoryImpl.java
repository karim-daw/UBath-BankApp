package se2.groupb.server.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import se2.groupb.server.account.Account;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerDTO;
import se2.groupb.server.loanOffer.LoanOffer;
import se2.groupb.server.loan.Loan;

public class CustomerRepositoryImpl implements EntityRepository<Customer, CustomerDTO> {

    // Temp HashMap Customer Repo
    private final HashMap<String, Customer> theCustomers;

    //Constructor
    public CustomerRepositoryImpl(HashMap<String, Customer> customers) {
        this.theCustomers = customers;
    }
    
    
    //Methods for Customer info:
    /**
     * Searches the Customer Data Store by CustomerID
     * 
     * @param customerID
     * @return customer from database
     */
    @Override
    public Customer findByID(UUID customerID) {
        return theCustomers.get(customerID.toString());
    }

    /**
     * Searches the Customer Data Store by Username & Password
     * 
     * @param customerDto
     * @return customer from database
     */
    @Override
    public Customer findByDTO(CustomerDTO customerDto) {
   
        String target_username = customerDto.getUsername();
        String target_password = customerDto.getPassword();

        for (HashMap.Entry<String, Customer> cust : theCustomers.entrySet()) {
            String cust_username = cust.getValue().getUsername();
            String cust_password = cust.getValue().getPassword();
            if ((cust_username.equals(target_username)) && (cust_password.equals(target_password))) {
                cust.getValue().setloggedInStatus(true);
                return cust.getValue();
            }
        }
        return null;
    }

    /**
     * Searches the Customer Data Store by Username & Password
     * 
     * @param customerDto
     * @return customer from database
     */
    @Override
    public Customer findByName(String target_username) {
        Customer customer = null;

        for (HashMap.Entry<String, Customer> cust : theCustomers.entrySet()) {
            String cust_username = cust.getValue().getUsername();
            if ((cust_username.equals(target_username))) {
                customer = cust.getValue();
                break;
            }
        }
        return customer;
    }

    /**
     * saves new customer into database
     * 
     * @param customer
     * @return
     */
    @Override
    public boolean save(Customer newCustomer) {
        if (findByID(newCustomer.getCustomerID()) == null) {
            theCustomers.put(newCustomer.getCustomerID().toString(), newCustomer);
            return true;
        }
        return false;
    }

    /**
     * Searches if the Username is already taken
     * 
     * @param username
     * @return
     */
    public boolean duplicateUsername(String username) {
        for (HashMap.Entry<String, Customer> cust : theCustomers.entrySet()) {
            String cust_username = cust.getValue().getUsername();
            if (cust_username.equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    
    //Methods that return lists of Accounts for the Customer filtered by various criteria:
    
    /**
     * Returns a list of the customer's accounts
     * @param customerID
     * @return
     */
    public ArrayList<Account> findAccounts(UUID customerID) {
    	Customer customer = findByID(customerID);
    	//System.out.println(customer.getAccounts().toString());
    	return customer.getAccounts();
    }
    
    /**
     * Returns a list of Account objects filtered by Customer ID and Account Type
     * @param customerID
     * @param accountType
     * @return
     */
    public ArrayList<Account> findAccountsByType(UUID customerID, String accountType) {
        ArrayList<Account> l = new ArrayList<>();
        for (Account a : findAccounts(customerID)) {
            if (a.getAccountType().equals(accountType)) {
                l.add(a);
            }
        }
        return l;
    }
    
    
    /**
     * Return Account corresponding to Customer and Account Number specified
     * @param accountNumber
     * @return Account
     */
    public Account findAccountByNumber(UUID customerID, String accountNumber) {
    	for (Account a : findAccounts(customerID)) {
            if (a.getAccountNumber().equals(accountNumber)) {
                return a;
            }
        }
    	return null;
    }
    
    
  //Methods that return lists of Loan Offers for the Customer filtered by various criteria:
    
    
    /**
     * Returns the Customer's Loan Offers as a numbered Map
     * 
     * @return Map<String,LoanOffer>
     */
    public Map<String,LoanOffer> findLoanOffersMap (UUID customerID) {
    	Customer customer = findByID(customerID);
    	Map<String, LoanOffer> offersMap = new TreeMap<>();
    	
    	int index =0;
    	for (LoanOffer offer : customer.getLoanOffers()) {
    		index++;
    		offersMap.put(String.valueOf(index), offer);
    	}
    	return offersMap;
    }
    
    /**
     * Returns a list of the customer's Loan Offers
     * @param customerID
     * @return Array List of Loan Offers
     */
    public ArrayList<LoanOffer> findLoanOffers(UUID customerID) {
    	Customer customer = findByID(customerID);
    	return customer.getLoanOffers();
    }
    
    /**
     * Finds the Loan Offer by Name in the Customer's list of offers
     * @param customerID
     * @param offerName
     * @return
     */
    public LoanOffer findLoanOfferByName(UUID customerID,String offerName) {
    	for (LoanOffer offer : findLoanOffers(customerID)) {
            if (offer.getOfferName().equals(offerName)) {
                return offer;
            }
        }
    	return null;
    }
    
    public Map<String,Loan> findLoansMap(UUID customerID){
    	Customer customer = findByID(customerID);
    	Map<String, Loan> loansMap = new TreeMap<>();
    	
    	int index =0;
    	for (Loan loan : customer.getLoans()) {
    		index++;
    		loansMap.put(String.valueOf(index), loan);
    	}
    	return loansMap;
    }
}
