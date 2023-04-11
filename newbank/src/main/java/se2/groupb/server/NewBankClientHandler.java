package se2.groupb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import se2.groupb.server.customer.*;
import se2.groupb.server.account.*;
import se2.groupb.server.Payee.Payee;
import se2.groupb.server.transaction.*;
import se2.groupb.server.loan.*;
import se2.groupb.server.loanOffer.*;
import se2.groupb.server.repository.*;




public class NewBankClientHandler extends Thread {

	// statics
	private static final String welcomeMenu = "\n" +
			"====================================================\n" +
			"||           *** WELCOME TO NEWBANK ***           ||\n" +
			"====================================================\n" +
			"|| Please select one of the following options:    ||\n" +
			"||      1. LOGIN                                  ||\n" +
			"||      2. REGISTER                               ||\n" +
			"|| Enter the number corresponding to your choice  ||\n" +
			"|| and press enter                                ||\n" +
			"====================================================\n" +
			"\nEnter Selection:";
	private static final int welcomeMenuChoices = 2;

	
	private static final String mainmenu = "\n" +
			"====================================================\n" +
			"||           *** NEWBANK MAIN MENU ***            ||\n" +
			"====================================================\n" +
			"|| Please select one of the following options:    ||\n" +
			"||      1. BANKING MENU                           ||\n" +
			"||      2. LOAN MARKET MENU                       ||\n" +
			"||      3. LOGOUT                                 ||\n" +
			"|| Enter the number corresponding to your choice  ||\n" +
			"|| and press enter                                ||\n" +
			"====================================================\n" +
			"\nEnter Selection:";
	private static final int mainMenuChoices = 3;

	
	private static final String bankMenu = "\n" +
			"====================================================\n" +
			"||      *** NEWBANK BANKING MENU ***              ||\n" +
			"====================================================\n" +
			"|| Please select one of the following options:    ||\n" +
			"||      1. View All Accounts                      ||\n" +
			"||      2. Select Account to View Transactions    ||\n" +
			"||      3. Create New Account                     ||\n" +
			"||      4. Move Money                             ||\n" +
			"||      5. Pay Person/Company                     ||\n" +
			"||      6. Change Password                        ||\n" +
			"||      7. Return                                 ||\n" +
			"|| Enter the number corresponding to your choice  ||\n" +
			"|| and press enter                                ||\n" +
			"====================================================\n" +
			"\nEnter Selection:";
	private static final int bankMenuChoices = 7;
	
	private static final String loanMarketMenu = "\n" +
			"====================================================\n" +
			"||      *** NEWBANK LOAN MARKET MENU ***          ||\n" +
			"====================================================\n" +
			"|| Please select one of the following options:    ||\n" +
			"||      1. View Your Loan Offers                  ||\n" +
			"||      2. View All Loan Offers                   ||\n" +
			"||      3. Create New Loan Offer                  ||\n" +
			"||      4. Accept Loan Offer                      ||\n" +
			"||      5. View Loans                             ||\n" +
			"||      6. Return                                 ||\n" +
			"====================================================\n" +
			"\nEnter Selection:";
	private static final int loanMarketMenuChoices = 6;
	

	// fields
	private NewBank bank;
	private final BufferedReader in;
	private final PrintWriter out;
	public final UserInput comms;
	private Payee payees;
	
	//Controllers:
	private CustomerController customerController;
	private AccountController accountController;
	private TransactionController transactionController;
	private LoanController loanController;
	private LoanOfferController loanOfferController;
	
	//Services:
	//private CustomerServiceImpl customerService;
	//private AccountServiceImpl accountService;
	private CustomerService customerService;
	private AccountService accountService;
	private TransactionService transactionService;
	private LoanServiceImpl loanService;
	private LoanOfferServiceImpl loanOfferService;
	
	//Repos:
	private CustomerRepositoryImpl customerRepository;
	private AccountRepositoryImpl accountRepository;
	private TransactionRepositoryImpl transactionRepository;
	private LoanRepositoryImpl loanRepository;
	private LoanOfferRepositoryImpl loanOfferRepository;
	
	
	// constructor
	public NewBankClientHandler(Socket s) throws IOException {
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		// each client has the same bank but different comms because of different sockets
		comms = new UserInput(in, out);
		bank = NewBank.getBank(); // static instance of the bank
		// Initialise repos
		customerRepository = new CustomerRepositoryImpl(bank.getCustomers());
		accountRepository = new AccountRepositoryImpl(bank.getAccounts());
		transactionRepository = new TransactionRepositoryImpl(bank.getTransactions());
		loanRepository = new LoanRepositoryImpl(bank.getLoans());
		loanOfferRepository = new LoanOfferRepositoryImpl(bank.getLoanMarket());
		
		//Initialise services
		customerService = new CustomerServiceImpl(customerRepository);
		accountService = new AccountServiceImpl(accountRepository,customerRepository);
		transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, customerRepository);
		loanService = new LoanServiceImpl(customerRepository,loanRepository);
		loanOfferService = new LoanOfferServiceImpl(customerRepository,accountRepository,loanOfferRepository);
		
		//Initialise controllers:
		customerController = new CustomerController(customerService, accountService, comms);
		accountController = new AccountController(accountService, customerService, comms);
		transactionController = new TransactionController(customerService,customerController,accountService, 
				transactionService, payees,comms);
		loanController = new LoanController(accountController, customerService, accountService,loanService,comms);
		loanOfferController = new LoanOfferController(customerController, accountController, loanController,
				loanOfferService,comms);
	}

	public void run() {
		// keep getting requests from the client and processing them
		// The User is not logged into the system yet so CustomerID is null
		// CustomerID customerID = null;
		String initialRequest;
		String mainRequest;
		String bankRequest;
		String loanRequest;
		
		String response = "";
		UUID customerID = null;
		try {
			while (true) {
				if (customerID == null) {
					// welcome message and choice
					initialRequest = comms.getUserMenuChoice(welcomeMenu, welcomeMenuChoices);
					if (initialRequest.equals("1")) { //1=LOGIN
						customerID = customerController.userLogin();
					} else { //2=REGISTER
						customerID = customerController.userRegistration();
					}
				} else { // if customer is Logged in
					Customer customer = customerController.getCustomer(customerID);
					mainRequest = comms.getUserMenuChoice(mainmenu, mainMenuChoices);
					
					if (mainRequest.equals("1")) { // 1= Banking Menu
						do {
							bankRequest = comms.getUserMenuChoice(bankMenu, bankMenuChoices); //return = 7
							comms.printSystemMessage("Request from: " + customer.getUsername());
							response = processBankingRequest(customerID, bankRequest);
							comms.printSystemMessage(response);
						}while (!bankRequest.equals("7"));
					}
					else if (mainRequest.equals("2")) { // 2= Loan Market Menu
						do {
							comms.printSystemMessage("Request from: " + customer.getUsername());
							loanRequest = comms.getUserMenuChoice(loanMarketMenu, loanMarketMenuChoices); //return = 6
							response = processLoanMarketRequest(customerID, loanRequest);
							comms.printSystemMessage(response);
						}while (!loanRequest.equals("6"));
					}
					else { //logout
						customerController.userLogout(customerID);
						customerID = null;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	
	public synchronized String processBankingRequest(UUID customerID, String request) throws IOException{
		switch (request) {
			case "1":
			case "SHOWMYACCOUNTS":
				return accountController.displayAccounts(customerID);
			case "2":
			case "SHOWTRANSACTIONS":
				return "Select account to show Transactions";
			case "3":
			case "NEWACCOUNT":
				return accountController.newAccount(customerID);
			case "4":
			case "MOVE":
				return transactionController.moveMoney(customerID);
			case "5":
			case "PAY":
				return transactionController.transferMoney(customerID);
			case "6":
			case "CHANGEPASSWORD":
				return customerController.changePassword(customerID);
			case "7":
				return "Returning you to Main Menu";
			default:
				return "FAIL";
		}
	}
	
	
	public synchronized String processLoanMarketRequest(UUID customerID, String request) {
		switch (request) {
			case "1":
				return loanOfferController.displayLoanOffers(customerID);
			case "2":
				return loanOfferController.displayLoanOfferMarket();
			case "3":
				return loanOfferController.newLoanOffer(customerID);
			case "4":
				return loanOfferController.acceptLoanOffer(customerID);
			case "5":
				return loanController.displayLoans(customerID);
			case "6":
				return "Returning you to Main Menu";
			default:
				return "FAIL";
		}
	}

}
