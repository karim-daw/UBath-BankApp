package newbank.server.Requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Login implements IRequests {

    private BufferedReader in;
    private PrintWriter out;

    public Login() {
    }

    public Login(BufferedReader in) {
        this.in = in;
    }

    @Override
    /**
     * 
     * Handles login request
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public String handleRequest() throws IOException {
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

    @Override
    public void displayRequest() {
        out.println("Please enter your request (LOGIN or REGISTER):");

    }

}
