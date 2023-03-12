package newbank.server.Requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import newbank.server.CustomerID;
import newbank.server.NewBank;

public class LoginService {

    private BufferedReader in;
    private PrintWriter out;
    private NewBank bank;

    public LoginService() {
    }

    /**
     * 
     * Handles login request
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public String handleLoginOrRegister() throws IOException {
        String request = in.readLine();
        boolean requestValid = false;
        do {
            if (request.equals("LOGIN") || request.equals("REGISTER")) {
                requestValid = true; // exits out of loop
            } else {
                out.println("Invalid request: Please enter LOGIN or REGISTER");
                request = in.readLine(); // reads input again from user
            }
        } while (!requestValid); // if input is not good everything repeats
        return request;
    }

    /**
     * Login sequence for an existing customer, returns the customerID if login was
     * succesfull
     * 
     * @return customerID if login was successful, else null
     * @throws IOException
     */
    public CustomerID verifyCustomer() throws IOException {
        // Not a customer yet
        CustomerID customer = null;

        // Ask for existing username
        out.println("Enter Username");
        String userName = in.readLine();

        // ask for existing password
        out.println("Enter Password");
        String password = in.readLine();

        out.println("Please wait while we check your details");
        customer = bank.checkLogInDetails(userName, password);

        // Validate login details
        if (customer == null) {
            out.println("Log In Failed. Invalid Credentials, please try again.");
        } else {
            out.println("Log In Successful. What do you want to do?");
        }
        return customer;
    }

}
