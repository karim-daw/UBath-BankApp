package newbank.server;

import java.io.PrintWriter;

public class Display {

    public Display() {

    }

    /**
     * Displazs main menu for the user to select next steps in bank transaction
     */
    public static void MainMenu(PrintWriter out) {
        out.println("\n");
        out.println("Select Option...");
        out.println("SHOWMYACCOUNTS");
        out.println("NEWACCOUNT");
        out.println("MOVE");
        out.println("PAY");
        out.println("\n");
    }

    /**
     * Welcome message for user
     */
    public static void WelcomeMessage(PrintWriter out) {
        out.println("##############################\n");
        out.println("**** Welcome to New Bank ****\n");
        out.println("##############################\n\n");
        out.println("If you're an existing customer type LOGIN\n");
        out.println("If you're a new customer type REGISTER\n");
    }

}
