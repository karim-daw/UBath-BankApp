package se2.groupb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import se2.groupb.server.customer.*;
import se2.groupb.server.account.*;
import se2.groupb.server.repository.*;
import se2.groupb.server.transaction.TransactionController;
import se2.groupb.server.transaction.TransactionService;
import se2.groupb.server.transaction.TransactionServiceImpl;

public class NewBankClientHandler extends Thread {

	// statics

	private static final String welcomeMessage = "\n" +
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
	private static final int welcomeChoices = 2;

	private static final String requestMenu = "\n" +
			"====================================================\n" +
			"||           *** NEWBANK MAIN MENU ***            ||\n" +
			"====================================================\n" +
			"|| Please select one of the following options:    ||\n" +
			"||      1. View Accounts                          ||\n" +
			"||      2. Select Account to View Transactions    ||\n" +
			"||      3. Create New Account                     ||\n" +
			"||      4. Move Money                             ||\n" +
			"||      5. Pay Person/Company                     ||\n" +
			"||      6. Change Password                        ||\n" +
			"||      7. Logout                                 ||\n" +
			"|| Enter the number corresponding to your choice  ||\n" +
			"|| and press enter                                ||\n" +
			"====================================================\n" +
			"\nEnter Selection:";

	private static final int mainMenuChoices = 7;

	// fields

	private NewBank bank;
	private final BufferedReader in;
	private final PrintWriter out;
	public final UserInput comms;
	private CustomerController customerController;
	private CustomerService customerService;
	private CustomerRepositoryImpl customerRepository;

	private AccountController accountController;
	private AccountService accountService;
	private AccountRepositoryImpl accountRepository;

	private TransactionController transactionController;
	private TransactionService transactionService;
	private TransactionRepositoryImpl transactionRepository;

	// each client has the same bank but different comms because of different
	// sockets

	public NewBankClientHandler(Socket s) throws IOException {
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		comms = new UserInput(in, out);
		bank = NewBank.getBank(); // static instance of the bank
		// Initialise controllers
		customerRepository = new CustomerRepositoryImpl(bank.getCustomers());
		customerService = new CustomerServiceImpl(customerRepository);
		customerController = new CustomerController(customerService, accountService, comms);

		accountRepository = new AccountRepositoryImpl(bank.getAccounts());
		accountService = new AccountServiceImpl(accountRepository);
		accountController = new AccountController(accountService, customerService, comms);

		transactionRepository = new TransactionRepositoryImpl(bank.getTransactions());
		transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, customerRepository);
		transactionController = new TransactionController(customerService, accountService, transactionService, comms);

	};

	public void run() {
		// keep getting requests from the client and processing them
		// The User is not logged into the system yet so CustomerID is null
		// CustomerID customerID = null;
		String request = "";
		String response = "";
		UUID customerID = null;
		try {
			while (true) {
				if (customerID == null) {
					// welcome message and choice
					request = comms.getUserMenuChoice(welcomeMessage, welcomeChoices);
					// Processes the user's response: 1=LOGIN or 2=REGISTER
					if (request.equals("1")) {
						customerID = customerController.userLogin();
					} else {
						customerID = customerController.userRegistration();
					}
				} else {
					// show menu choices
					request = comms.getUserMenuChoice(requestMenu, mainMenuChoices);
					Customer customer = customerController.getCustomer(customerID);
					comms.printSystemMessage("Request from: " + customer.getUsername());
					// process menu choice request
					response = processRequest(customerID, request);
					comms.printSystemMessage(response);
					boolean logedInStatus = customer.getloggedInStatus();
					if (!logedInStatus) {
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

	public synchronized String processRequest(UUID customerID, String request) throws IOException {

		switch (request) {
			case "1":
			case "SHOWMYACCOUNTS":
				return customerController.displayAccounts(customerID);
			// case "2":
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
			// TODO: PUT in LOAN here
			case "7":
			case "LOGOUT":
				return customerController.userLogout(customerID);
			default:
				return "FAIL";
		}
	}

}
