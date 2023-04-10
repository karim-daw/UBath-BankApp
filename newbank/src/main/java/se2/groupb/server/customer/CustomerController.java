package se2.groupb.server.customer;

import java.util.UUID;

import se2.groupb.server.UserInput;
import se2.groupb.server.Payee.Payee;
import se2.groupb.server.account.AccountService;
import se2.groupb.server.security.Authentication;

public class CustomerController {
	// fields

	private final CustomerService customerService;
	private UserInput comms;

	// Constructor
	public CustomerController(CustomerService customerService, AccountService accountService, UserInput comms) {
		this.customerService = customerService;
		this.comms = comms;
	}

	// methods

	/**
	 * Returns the Customer object from the Data Store
	 * 
	 * @param customerID
	 * @return
	 */
	public Customer getCustomer(UUID customerID) {
		return customerService.getCustomerByID(customerID);
	}

	/**
	 * Login for existing customers
	 * 
	 * @return Customer object
	 */
	public UUID userLogin() {
		String systemResponse = "";
		String username = comms.getUserString("Enter Username");
		String password = comms.getUserString("Enter Password");

		// this dto has the plain text password
		CustomerDTO customerDto = new CustomerDTO(username, password);
		comms.printSystemMessage("Please wait while we check your details");

		// get customer
		Customer customer = customerService.getCustomerbyDTO(customerDto);

		// Validate login details
		if (customer == null) {
			systemResponse = "LOGIN FAIL. No such credentials...";
			comms.printSystemMessage(systemResponse);
			return null;
		} else {
			systemResponse = "LOGIN SUCCESS. What do you want to do?";
			comms.printSystemMessage(systemResponse);
			return customer.getCustomerID();
		}
	}

	/**
	 * Create a new Customer
	 * 
	 * @return CustomerDTO
	 */
	// TODO: #40 need to implement a check to see if user hits "enter" instead of
	// typing
	// "Y" to confirm. If you hit enter it goes into an infinite loop and crashes
	public UUID userRegistration() {
		String username;
		boolean duplicateUsername;
		do {
			username = comms.getUserString("Enter a username");
			duplicateUsername = customerService.duplicateUsername(username);
			if (duplicateUsername) {
				comms.printSystemMessage("Username taken. Please try again. ");
			}
		} while (duplicateUsername);

		// ask for a password
		String passwordAttempt1;
		String passwordAttempt2;
		boolean matchedPasswords;
		do {
			passwordAttempt1 = comms.getUserString("Choose Password");
			passwordAttempt2 = comms.getUserString("Re-Enter Password");
			matchedPasswords = passwordAttempt2.equals(passwordAttempt1);
			if (!matchedPasswords) {
				comms.printSystemMessage("Passwords do not match. Please try again.");
			}
		} while (!matchedPasswords);

		String prompt = "Create new user with username " + username + " and password " + passwordAttempt2 + "?";
		boolean userConfirm = comms.confirm(prompt);
		if (userConfirm) {
			CustomerDTO customerDto = new CustomerDTO(username, passwordAttempt2);
			if (customerService.addNewCustomer(customerDto)) {
				String str = String.format("Registration succesfull. Please login to proceed.");
				comms.printSystemMessage(str);
				/*
				 * Customer customer = customerService.getCustomerbyDTO(customerDto);
				 * UUID customerID = customer.getCustomerID();
				 * return customerID;
				 */
			} else {
				String str = String.format("Database update failed. User not registered.");
				comms.printSystemMessage(str);
			}
		} else {
			String str = "Registration cancelled by user.";
			comms.printSystemMessage(str);
		}
		return null;
	}

	/**
	 * @param customerID
	 * @return
	 */
	public String userLogout(UUID customerID) {
		String systemResponse = "";
		String prompt = "Are you sure you want to log out?";
		boolean userConfirm = comms.confirm(prompt);
		if (userConfirm) {
			customerService.userLogout(customerID);
			systemResponse = "LOGOUT SUCCESS";
		} else {
			systemResponse = "LOGOUT CANCELLED. RETURNING YOU TO THE MAIN MENU.";
		}
		return systemResponse;
	}

	/**
	 * displays the customers accounts as a list
	 * 
	 * @param customerDTO
	 * @return
	 */
	public String displayAccounts(UUID customerID) {
		return customerService.displayAccounts(customerID);
	}

	/**
	 * @return returns success if password was changed
	 */
	public String changePassword(UUID customerID) {
		String prompt = "Enter old password";
		String oldPassword = comms.getUserString(prompt);

		Customer customer = customerService.getCustomerByID(customerID);
		String customerPassword = customer.getPassword();

		// check hashed password
		boolean passwordIsMatched = Authentication.authenticatePassword(oldPassword, customerPassword);

		if (!passwordIsMatched) {
			return "FAIL. The old password is incorrect."; // passwords dont match
		}

		prompt = "Enter new password";
		String newPassword = comms.getUserString(prompt);

		prompt = "Enter new password";
		String newPassword2 = comms.getUserString(prompt);

		if (!newPassword.equals(newPassword2)) {
			return "FAIL. your password choice dont match"; // passwords dont match
		}

		customerService.updatePassword(customerID, newPassword);

		return null;
	}

	/**
	 * displays the customers payees as a list
	 * 
	 * @param customerDTO
	 * @return
	 */
	public String displayPayees(UUID customerID) {
		return customerService.displayPayees(customerID);
	}

	/**
	 * @param customerID
	 * @return
	 */
	public String createPayee(UUID customerID) {
		String response = ""; // the system response to the user's request
		String prompt = "Add a new payee: \n";
		prompt = "Enter the payee name: \n";
		boolean duplicateName;
		String payeeName = comms.getUserString(prompt);
		// Check if the payee already exists duplicateName =
		// accountService.alreadyExists(payeeID, payeeName);
		// while (duplicateName);

		prompt = "Enter payee's account number: \n";
		String payeeAccountNumber = comms.getUserString(prompt);

		prompt = "Enter payee's BIC: \n";
		String payeeBIC = comms.getUserString(prompt);

		prompt = "Add " + payeeName + " as a new payee?\nEnter 'y' for Yes or 'n' for No: \n";
		boolean userConfirm = comms.confirm(prompt);

		if (userConfirm) {
			Customer customer = getCustomer(customerID);
			Payee newPayee = new Payee(customer.getCustomerID(), payeeName, payeeAccountNumber,
					payeeBIC);

			customer.addPayee(newPayee); // adds new payee to the customer

			response = "SUCCESS: The payee " + payeeName + " has been added.\nReturning to Main Menu.";
		} else {
			response = "Payee addition was cancelled.\nReturning to the Main Menu.";
		}
		return response;
	}

}
