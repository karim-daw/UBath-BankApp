package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class User {

    Display display;
    private BufferedReader in;
    private PrintWriter out;

    public User() {
        display = new Display();
    }

    /**
     * 
     * displays welcome screen and reads intitial input from user, for now that
     * would be either LOGIN and REGISTER or some gibirish,
     * 
     * @return
     * @throws IOException
     */
    public void userWelcome() throws IOException {
        display.displayWelcome();
    }

    /**
     * 
     * displays welcome screen and reads intitial input from user, for now that
     * would be either LOGIN and REGISTER or some gibirish,
     * 
     * @return
     * @throws IOException
     */
    public void userMainMenu() throws IOException {
        display.displayMainMenu();
    }

    /**
     * this is the method that takes care of teh part where the user chooses to
     * LOGIN or REGISTER
     * 
     * @return returns checkin request, either LOGIN or REGISTER
     * @throws IOException
     */
    public String userCheckin() throws IOException {

        // read input from user
        String request = in.readLine();

        // check if user logged in or registereed
        request = checkLoginOrRegister(request);
        return request;
    }

    /**
     * Login for existing customers
     * 
     * @return customerID of logged in user
     * @throws IOException
     */
    public CustomerID userLogIn(NewBank bank) throws IOException {
        // Not a customer yet
        CustomerID customerID = null;

        // Ask for existing username
        out.println("Enter Username");
        String userName = in.readLine();

        // ask for existing password
        out.println("Enter Password");
        String password = in.readLine();

        // check login details
        out.println("Please wait while we check your details");
        customerID = bank.checkLogInDetails(userName, password);

        // Validate login details
        if (customerID == null) {
            out.println("Log In Failed. Invalid Credentials, please try again.");
        } else {
            out.println("Log In Successful. What do you want to do?");
        }
        return customerID;
    }

    /**
     * Registration for new customers
     * 
     * @return
     * @throws IOException
     */
    public CustomerID userRegistration(NewBank bank) throws IOException {

        // flag for registrationed success
        CustomerID customerID = null;

        // Ask for existing username
        out.println("Enter Username");
        String userName = in.readLine();

        // ask password
        out.println("Choose Password");
        String passwordAttempt1 = in.readLine();
        out.println("Re-Enter Password");
        String passwordAttempt2 = in.readLine();

        if (!passwordAttempt2.equals(passwordAttempt1)) {
            out.println("Passwords do not match");
            return null;
        }
        // check if userName already exists, if yes is registers gets changed to true
        customerID = bank.registerCustomer(userName, passwordAttempt2);
        if (customerID != null) {
            String str = String.format("Registration succesfull. New Customer %s", userName);
            out.println(str);
        } else {
            String str = String.format("Customer name %s already exists, try registerings with a different name",
                    userName);
            out.println(str);
        }
        return customerID;

    }

    /**
     * This method will take a request and check if it is LOGIN or REGISTER, if they
     * are, thhe valid request will get returned. If not, it will continuely ask for
     * LOGIN or REGISTER
     * 
     * @param request
     * @return request string of the valid request i.e LOGIN or REGISTER
     * @throws IOException
     */
    private String checkLoginOrRegister(String request) throws IOException {
        boolean requestValid = false;
        do {
            if (request.equals("LOGIN") || request.equals("REGISTER")) {
                requestValid = true;
            } else {
                out.println("Invalid request: Please enter LOGIN or REGISTER");
                request = in.readLine();
            }
        } while (!requestValid);
        return request;
    }

}
