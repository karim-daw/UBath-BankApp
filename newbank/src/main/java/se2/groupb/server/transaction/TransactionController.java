package se2.groupb.server.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.Payee.Payee;
import se2.groupb.server.Payee.PayeeController;
import se2.groupb.server.account.Account;
import se2.groupb.server.account.AccountService;
import se2.groupb.server.account.AccountController;
import se2.groupb.server.customer.Customer;
import se2.groupb.server.customer.CustomerController;
import se2.groupb.server.customer.CustomerService;

public class TransactionController {

    private final CustomerService customerService;
    //private final CustomerController customerController;
    private final PayeeController payeeController;
    //private final AccountService accountService;
    //private final AccountController accountController;
    private final TransactionService transactionService;
    private UserInput comms;
    
    private static final String payeesMenu = "\n" +
    		"====================================================\n" +
            "||         *** TRANSFER MONEY MENU ***            ||\n" +
            "|| Please select one of the following options:    ||\n" +
            "||      1. PAY AN EXISTING PAYEE                  ||\n" +
            "||      2. PAY A NEW PAYEE                        ||\n" +
            "||      3. CANCEL PAY & RETURN TO MAIN MENU       ||\n" +
            "|| Enter the number corresponding to your choice  ||\n" +
            "|| and press enter                                ||\n" +
            "====================================================\n" +
            "\nEnter Selection:";
    
    private static final int payeesMenuChoices = 3;
    
    private static final String noPayeesMenu = "\n" +
    		"====================================================\n" +
            "||         *** NO EXISTING PAYEES ***             ||\n" +
            "|| Please select one of the following options:    ||\n" +
            "||      1. PAY A NEW PAYEE                        ||\n" +
            "||      2. CANCEL PAY & RETURN TO MAIN MENU       ||\n" +
            "|| Enter the number corresponding to your choice  ||\n" +
            "|| and press enter                                ||\n" +
            "====================================================\n" +
            "\nEnter Selection:";
    
    private static final int noPayeesMenuChoices = 2;
    
    /* Previous Constructor
    public TransactionController(CustomerService customerService, CustomerController customerController, 
    		AccountController accountController, AccountService accountService,
            TransactionService transactionService, PayeeController payeeController, Payee payees, UserInput comms)
	*/
    
    public TransactionController(CustomerService customerService, TransactionService transactionService, 
    		PayeeController payeeController,UserInput comms) {
    	
        this.customerService = customerService;
        this.transactionService = transactionService;
        this.payeeController = payeeController;
        this.comms = comms;
		//this.customerController = customerController;
		//this.accountController = accountController;
		//this.accountService = accountService;
    }

    /**
     * The method takes care of the MOVE protocol.
     * MOVE <Amount> <From> <To>
     * e.g. MOVE 100 Main Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerID
     * @param requestArray
     * @return SUCCESS string or FAIL string
     */
    /**
     * The method takes care of the MOVE protocol.
     * MOVE <Amount> <From> <To>
     * e.g. MOVE 100 Main Savings
     * Returns SUCCESS or FAIL
     * 
     * @param customerID
     * @param requestArray
     * @return SUCCESS string or FAIL string
     */
    
    // MOVE Function: Tatiana's version:
    public String moveMoney(UUID customerID) {
        Customer customer = customerService.getCustomerByID(customerID);
        List<Account> customerAccounts = customer.getAccounts();
        int noOfAccts = customerAccounts.size();
        Map<String, Account> sourceAccts = customer.sourceAcctsMap(); // The Customer's Source Accounts as an ordered numbered Map:
        int noOfSourceAccts = sourceAccts.size();
        if (noOfSourceAccts < 1 || noOfAccts < 2) {
            return "You need two or more accounts.\nRequest denied.\nReturning to Main Menu.";
        }
        
        //Display all potential Source Accounts to the customer:
        String prompt = "Select a Source Account from: \n" + customer.accountMapToString(sourceAccts) +
        		"Enter your choice: \n";
        //Get user's choice of Source Account:
 		String userInput = comms.getUserMenuChoice(prompt, noOfSourceAccts); //gets user's choice
 		Account sourceAccount = sourceAccts.get(userInput);
        String sourceAcctName = sourceAccount.getAccountName();
        
 		//Destination Accounts Map excludes the Source Account
 		Map<String, Account> destAccts = customer.destinationAcctsMap(sourceAccount.getAccountID());
 		int noOfDestAccts = destAccts.size();
        if (noOfDestAccts < 1) {
            return "No destination account available.\nReturning to Main Menu.";
        }
        
        //Display all potential Source Accounts to the customer:
 		prompt = "Select a Destination Account from: \n" + customer.accountMapToString(destAccts) + 
 				"Enter your choice: \n";
 		
 		//Get user's choice of Destination Account:
 		userInput = comms.getUserMenuChoice(prompt, noOfDestAccts); //gets user's choice
 		Account destinationAccount = destAccts.get(userInput);
 		String destAcctName = destinationAccount.getAccountName();
 				
 		//Get the user's input Amount:
        BigDecimal transferAmount = getTransferAmount(sourceAccount.getBalance());

        if (!confirmTransaction(transferAmount, sourceAcctName, destAcctName)) {
            return "Move transaction was cancelled.\nReturning to the Main Menu.";
        }

        boolean isSuccessfullyMoved = transactionService.executeMove(sourceAccount.getAccountID(),
                destinationAccount.getAccountID(), transferAmount);

        if (isSuccessfullyMoved) {
            return "Move Transaction Success.";
        } else {
            return "Move Transaction Failure.";
        }
    }
    
    
    private BigDecimal getTransferAmount(BigDecimal limit) {
        String prompt = "Transfer amount must be positive and not exceed the Source Account's balance.\nEnter an amount: ";
        return comms.getAmount(prompt, limit);
    }
    
    private String getTransferReference() {
        String prompt = "Enter a Reference for this Transaction: \n";
        return comms.getUserString(prompt);
    }

    
    private boolean confirmTransaction(BigDecimal transferAmount, String sourceAcctName, String destAcctName) {
        String prompt = "Move " + transferAmount + " from " + sourceAcctName + " to " +
                destAcctName
                + "?\nEnter 'y' for Yes or 'n' for No: \n";
        return comms.confirm(prompt);
    }

    

    /**
     * 
     * PAY <Person/Company> <Ammount>
     * e.g. PAY John 100
     * Returns SUCCESS or FAIL
     * 
     * @param customerID
     * @return
     */
    public String transferMoney(UUID customerID) {
        Customer customer = customerService.getCustomerByID(customerID);
        
        // The Customer's Source Accounts as an ordered numbered Map:
        Map<String, Account> sourceAccts = customer.sourceAcctsMap();
        int noOfSourceAccts = sourceAccts.size();
        if (noOfSourceAccts == 0) {
            return "No valid Source Accounts found.\nRequest denied.\nReturning to Main Menu.";
        }
        
        //Display all potential Source Accounts to the customer:
        String prompt = "Select a Source Account from: \n" + customer.accountMapToString(sourceAccts) +
        		"Enter your choice: \n";
        //Get user's choice of Source Account:
 		String userInput = comms.getUserMenuChoice(prompt, noOfSourceAccts); //gets user's choice
 		Account sourceAccount = sourceAccts.get(userInput);
 		String sourceAcctName = sourceAccount.getAccountName();
        UUID sourceAcctID = sourceAccount.getAccountID();
        
        //Select a Payee
        
 		//Payees Map
 		Map<String, Payee> payees = customer.payeesMap();
 		Payee payee = selectPayee(customerID, payees);
 		if (payee==null) {
 			return "Returning to Main Menu.";
 		}
		UUID payeeID = payee.getPayeeID();
        String payeeName = payee.getPayeeName();
		
		//Get the user's input Amount:
		BigDecimal transferAmount = getTransferAmount(sourceAccount.getBalance());
        
        //Get the user's reference for the transaction:
        String transferReference = getTransferReference();
        
        
        //Seek confirmation from user before proceeding:
        if (!confirmTransaction(transferAmount, sourceAcctName, payeeName)) {
            return "Transfer was cancelled.\nReturning to the Main Menu.";
        }
        
        // Execute Transaction:
        boolean isTransferSuccess = transactionService.executePay(sourceAcctID, payeeID, transferAmount,transferReference);

        if (isTransferSuccess) {
            return "Transfer Success.";
        } else {
            return "Transfer Failure.";
        }
    }
 		
    private Payee selectPayee(UUID customerID, Map<String, Payee> payees) {
    	String prompt="";
    	int noOfPayees = payees.size();
 		String mainRequest = comms.getUserMenuChoice(payeesMenu, payeesMenuChoices);
 		
 		//If the customer chose to pay an existing Payee but they have none:
		if ((mainRequest=="1") && (noOfPayees==0)){
			prompt = "Invalid request. You have no existing Payees.\n";
			prompt += "You must choose from the following options: \n";
			comms.printSystemMessage(prompt);
			String subRequest = comms.getUserMenuChoice(noPayeesMenu, noPayeesMenuChoices);
			Integer subRequestInt = Integer.parseInt(subRequest)+1;
			mainRequest = subRequestInt.toString();
		}
		
		switch (mainRequest) {
			case "1":{ // Pay an existing Payee
				prompt = "Select a Payee from: \n" + payeeMapToString(payees) +
				"Enter your choice: \n";
				String userInput = comms.getUserMenuChoice(prompt, noOfPayees); //gets user's choice
		 		Payee payee = payees.get(userInput);
		 		return payee;
			}
			case "2":{ //Pay a new Payee: create a new payee and then do transfer
				Payee payee = payeeController.createPayee(customerID);
				if (payee == null) {
					comms.printSystemMessage("Returning to Main Menu.\n");
				}
				return payee;
			}
			case "3":{ //Cancel PAY request and return to Main Menu
				comms.printSystemMessage("PAY request cancelled.\n");
				return null;
			}
		}
		return null;
    }
 	
    
    /**Helper function for turning a Map to a string
	 * @return a string of the Payee map
	 */
	public String payeeMapToString(Map<String, Payee> map) {
		String s = "";
		for (Map.Entry<String, Payee> item : map.entrySet()) {
			s += item.getKey() + " = " + item.getValue().toString() + "\n";
		}
		return s;
	}
	
 // MOVE Function: Previous version (Karim):
    /* 
    public String moveMoney(UUID customerID) {
        Customer customer = customerService.getCustomerByID(customerID);

        List<Account> customerAccounts = customer.getAccounts();
        int noOfAccts = customerAccounts.size();

        HashMap<String, String> sourceAccts = customer.sourceAcctsMap();
        int noOfSourceAccts = sourceAccts.size();
        if (noOfSourceAccts < 1 || noOfAccts < 2) {
            return "You need two or more accounts.\nRequest denied.\nReturning to Main Menu.";
        }

        // Display all possible source accounts:
        String sourceAccountList = customer.mapToString(sourceAccts);
        comms.printSystemMessage(sourceAccountList);

        // THere is a problem with the below code, valid name isn't being returned
        String sourceAcctName = selectAccount("Select source account:", sourceAccts);
        

        Account sourceAccount = customer.getAccountByName(sourceAcctName);

        HashMap<String, String> destAccts = customer.destinationAcctsMap(sourceAcctName);
        int noOfDestAccts = destAccts.size();

        if (noOfDestAccts < 1) {
            return "No destination account available.\nReturning to Main Menu.";
        }

        String destAcctName = selectAccount("Select destination account:", destAccts);
        Account destinationAccount = customer.getAccountByName(destAcctName);
        BigDecimal transferAmount = getTransferAmount(sourceAccount.getBalance());

        if (!confirmTransaction(transferAmount, sourceAcctName, destAcctName)) {
            return "Move transaction was cancelled.\nReturning to the Main Menu.";
        }

        boolean isSuccessfullyMoved = transactionService.executeMove(sourceAccount.getAccountID(),
                destinationAccount.getAccountID(), transferAmount);

        if (isSuccessfullyMoved) {
            return "Move Transaction Success.";
        } else {
            return "Move Transaction Failure.";
        }
    }
	*/
    
    
    /**
     * @param prompt
     * @param accounts
     * @return
     */
    /*
    private String selectAccount(String prompt, Map<String, String> accounts) {
    	//e.g. selectAccount("Select source account:", sourceAccts);
    	
        String selectionNumber = comms.getUserMenuChoice(prompt, accounts.size());
        String accountBalanceString = accounts.get(selectionNumber);
        System.out.println(accountBalanceString);
        System.out.println("Selecting account...");

        // Define the regular expression pattern to match the account name
        String accountName = extractWord(accountBalanceString);
        if (accountName != null) {
            System.out.println("Selected account: " + accountName);
            return accountName;
        } else {
            System.out.println("Unable to extract account name: ");
            return null;
        }
    }
    */
	
	/*
    private static String extractWord(String input) {
        String[] lines = input.split("\n");
        int start = 0;
        if (lines.length > 0 && lines[0].isEmpty()) {
            start = 1;
        }
        if (lines.length > start + 1) {
            String firstLine = lines[start + 1];
            int idx = firstLine.indexOf("(");
            if (idx >= 0) {
                String extracted = firstLine.substring(0, idx).trim();
                if (!extracted.isEmpty()) {
                    return extracted;
                }
            }
        }
        return null;
    }
    */
	
    /*
    public String transferMoney(UUID customerID) {
    	
    // int noOfSourceAccts = customer.sourceAcctsMap().size();

    // if (noOfSourceAccts < 1) {
    // return "You need atleast one non-overdrawn account.\nRequest
    // denied.\nReturning to Main Menu.";
    // }
    // // Displayed the payee list. Each paye has a number. The user choose the
    // number
    // // of the payee.
    // // If the payee is not in the list, ask to create a new one.

    // // Get customer name
    // String customerName = customer.getUsername();

    // // Prompt user to for NewBank member name to PAY to
    // String prompt = "Enter NewBank member name you want to PAY money to: ";
    // String payeeName = comms.getUserString(prompt); // user input

    // // Check if the customer is trying to pay himself.
    // if (payeeName.equals(customerName)) {
    // return "FAIL, you are trying to pay yourself";
    // }

    // // Check to see if payee if a member of NewBank
    // Customer payee;
    // try {
    // payee = customerService.getCustomerbyName(payeeName);
    // } catch (IllegalArgumentException e) {
    // System.out.println(e.getMessage());
    // return "FAIL, payee not a member of NewBank";
    // }

    // // Prompt user for a transaction amount
    // prompt = "Enter an transfer amount: ";
    // String amountString = comms.getUserString(prompt); // user input

    // BigDecimal transactionAmount;
    // try {
    // transactionAmount = new BigDecimal(amountString);
    // } catch (NumberFormatException e) {
    // return "FAIL, input is not a number"; // return fail if input is not figures
    // instead of an error
    // }

    // if (transactionAmount.compareTo(BigDecimal.ZERO) < 0) {
    // return "FAIL, input cannot be negative";
    // }

    // // get payee account as customer
    // ArrayList<Account> payeeAccounts = payee.getAccounts();
    // Account payeeFirstAccount = payeeAccounts.get(0); // first account
    // UUID payeeAccountID = payeeFirstAccount.getAccountID();

    // // needs to get customers account id
    // ArrayList<Account> customerAccounts = customer.getAccounts();
    // Account customerFirstAccount = customerAccounts.get(0); // first account
    // UUID customerAccountID = customerFirstAccount.getAccountID();

    // boolean isSuccessfullyPay = transactionService.executePay(customerAccountID,
    // payeeAccountID, transactionAmount);
    // if (isSuccessfullyPay) {
    // return "PAY transaction was successful.";
    // } else {
    // return "Something went wrong with the move";
    // }
    String prompt = "====================================================\n" +
                        "||           **TRANSFER MONEY MENU**              ||\n" +
                        "|| Please select one of the following options:    ||\n" +
                        "||      1. TRANSFER MONEY                         ||\n" +
                        "||      2. SHOW MY PAYEES                         ||\n" +
                        "||      3. ADD A PAYEE                            ||\n" +
                        "|| Enter the number corresponding to your choice  ||\n" +
                        "|| and press enter                                ||\n" +
                        "====================================================\n" +
                        "\nEnter Selection:";
        
        
        
        
        String userInput = comms.getUserMenuChoice(prompt,3);
            if (userInput.equals("1")){           
                ArrayList<String> listOfPayees = new ArrayList<String>();
                List<String> payeesList = customer.payeesToList();
		            for (int i=0; i<payeesList.size();i++) {
			            listOfPayees.add("\n"+ (i+1)+". " +payeesList.get(i));
		            }
                    prompt="Choose your payee" + listOfPayees.toString() + "\n 0. Add a payee. \nEnter Selection.";
                    userInput= comms.getUserString(prompt);
                    int userInputInt = comms.convertStringToInt(userInput);
                    if (userInputInt == 0){
                        customerController.createPayee(customerID);
                        return "Payee added";
                    }
                    //comparing the input with the index of the table.
                    else {
                            //The user chose a payee
                            for (int l=0; l<listOfPayees.size(); l++){
                                if (userInputInt - 1 == l){
                                    //pick the selected payee
                                    ArrayList<Payee> customerPayees = customer.getPayees();
                                    UUID payeeID = customerPayees.get(userInputInt-1).getPayeeID();
                                    String payeeName = customerPayees.get(userInputInt-1).getPayeeName(); 
                                    //choose a source account
                            
                                    ArrayList<String> listOfAccounts = new ArrayList<String>();
                                    List<String> accountsList = customer.accountsToList();
                                    //display the account list
                                    for (int k=0; k<accountsList.size();k++) {
                                        listOfAccounts.add("\n"+ (k+1)+accountsList.get(k)); // TO DO: Display only the account name and the balance of the account
                                    }
                                    prompt = "\nChoose an account." + listOfAccounts.toString()+ "\nEnter Selection.";
                                    userInput= comms.getUserMenuChoice(prompt, listOfAccounts.size());
                                    userInputInt = comms.convertStringToInt(userInput);

                                    for (int m =0; m<listOfAccounts.size(); m++){
                                        if (userInputInt-1 == m){
                                        //extract the PayeeId that matches with the selected payee.
                                        ArrayList<Account> customerAccounts = customer.getAccounts();
                                        UUID sourceAccountID = customerAccounts.get(userInputInt-1).getAccountID();
                                        //Extract the source account in order to set the new balance after the transfer
                                        Account sourceAccount = customerAccounts.get(userInputInt-1);
                                        //extract the balance of the selected account
                                        BigDecimal sourceAccountBalance = customerAccounts.get(userInputInt-1).getBalance();     
                                    
                                        //enter the amount of the payment
                                            prompt = "Enter an amount.";
                                            BigDecimal transactionAmount = comms.getAmount(prompt,sourceAccountBalance);
                                            
                                            // confirmation of the transfert/
                                            
                                            prompt = transactionAmount + " will be transfered to " + payeeName + "\n Confirm ? Y/N ";
                                            boolean transferConfirmed = comms.confirm(prompt);
                                            if (!transferConfirmed){
                                                return "Move transaction was cancelled.\nReturning to the Main Menu.";
                                            }
                                            else 
                                            {
                                                Transaction transfertTransaction = new Transaction(sourceAccountID, payeeID, transactionAmount);
                                                // set a new balance for the account
                                                sourceAccount.withdraw(transactionAmount);
                                                
                                                return "Transfert done. Your balance account is now : " + sourceAccount.getBalance();//check  
                                            }
                                            
                                        }
                                    }
                                    
                                }
                            }

                        }

                        // enter the amount of the payment
                        prompt = "Enter an amount.";
                        userInput = comms.getUserString(prompt);
                        double transactionAmount = Double.parseDouble(userInput);

                        return "Check";// check

                    }

                }
            }
            // If payee doesn't exist, add a payee, call the createPayee
            // If the payee exists! proceed
            return "ok";
        }
        
        
        
        if (userInput.equals("2")) {
            return customerController.displayPayees(customerID);
        }
        if (userInput.equals("3")) {
            return customerController.createPayee(customerID);
        }
        return "FAIL";

    }
     * 
     * 
	*/
}
