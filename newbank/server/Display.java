package server;

import java.io.PrintWriter;

public class Display {

    // private members
    private PrintWriter out;

    // contructor
    public Display() {
    }

    // contructor
    public Display(PrintWriter out) {
        this.out = out;
    }

    /**
     * Prints welcome message
     */
    public void displayWelcome() {
        out.println("##############################\n");
        out.println("**** Welcome to New Bank ****\n");
        out.println("##############################\n\n");
        out.println("If you're an existing customer type: LOGIN\n");
        out.println("If you're a new customer type: REGISTER\n");
    }

    /**
     * Displays main menu to user after login
     */
    public void displayMainMenu() {
        out.println("\n");
        out.println("Select Option...");
        out.println("SHOWMYACCOUNTS");
        out.println("NEWACCOUNT");
        out.println("MOVE");
        out.println("PAY");
        out.println("\n");
    }

}
