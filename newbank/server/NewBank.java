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
	private void moveMoney(CustomerID customer, String sourceAccount, String destinationAccount, double amount){
        Customer c = customers.get(customer.getKey());
		double movingAmount;
		//check if the amount transfer is less than zero 
        try {
            movingAmount = Double.parseDouble(amount);
            if (movingAmount < 0.0) {
                return "Re-enter moving amount again.";
            }
        } catch (NumberFormatException e) {
            return "Please re-enter with numbers only.";
        }
		//check if the account is exist 
        try {
            if (c.checkAccount(sourceAccount) == false || c.checkAccount(destinationAccount) == false) {
                return "Account doee not exist";
            }

        } catch (Exception e) {
            return "Account does not exist";
        }
		//check the account value 
        Double currentBalance = c.getAccountValue();
		//deduct amount from the moving account 
        if (currentBalance >= movingAmount) {
			c.changeBalance(-movingAmount)
            return "SUCCESS";
        } else {
            return "FAIL";
        }
    }

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {

			String[] requestInputs = request.split("\\s+");
			String command = requestInputs[0];

			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			case "MOVE": return moveMoney(customer, "main", "savings", 2000);
			default : return "FAIL";
			
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

}
