package newbank.server.Requests;

import java.io.IOException;

import newbank.server.CustomerID;
import newbank.server.Display;

public class CustomerService {

    public CustomerService() {
    }

    /**
     * Given a valid customerID, system Displays the main menu to the logged in
     * customer and continuesly processes transactions with bank in while loop
     * 
     * and prompts user to
     * 
     * @param customerID
     * @param request
     * @throws IOException
     */
    private void processCustomerRequest(CustomerID customerID, String request) throws IOException {
        // if user succesfully logged-in, get requests from user and process them
        if (customerID != null) {

            // Asking for a request and process the request
            while (true) {

                // show main menu to user and prompt for an input
                Display.MainMenu(out);

                // get user input as request
                request = in.readLine();

                // handle response from bank given request, for now just print
                handleBankResponse(request, customerID);
            }
        }
    }

}
