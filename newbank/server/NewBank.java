package newbank.server;

import java.util.HashMap;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			String[] requestArray = request.split(" ");    
			//Split in the String request into an array of substrings
			
			switch(requestArray[0]) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			case "PAY" : return transferMoney(customer, requestArray[1]);
			default : return "FAIL";
			}
		}
			
		return "FAIL";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	private String transferMoney(CustomerID customer, String[] requestArray) {
        Customer c = customers.get(customer.getKey());
        String payeeName = requestArray[1];
      
        double transferAmount;
        try {
            transferAmount = Double.parseDouble(requestArray[2]);
        } catch (NumberFormatException e) {
            return "FAIL"; //return fail if input is not figures instead of an error
        }
        
        Account payee = c.getAccount(payeeName);
        if (payee == null || transferAmount < 0) {
            return "FAIL";
        } //Other than checking the existence of the account, it is to make sure the figure is not negative 
        
        payee.updateBalance(transferAmount);
        return "SUCCESS";
        
	}

}
