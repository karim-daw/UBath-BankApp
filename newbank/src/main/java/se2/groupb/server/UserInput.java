package se2.groupb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class UserInput {

	private BufferedReader in;
	private PrintWriter out;

	// constructor
	public UserInput(BufferedReader in, PrintWriter out) throws IOException {
		this.in = in;
		this.out = out;
	}

	public void printSystemMessage(String message) {
		out.println(message);
	}

	public String readNextLine() {
		try {
			return in.readLine(); // could be null if user didn't enter anything
		} catch (IOException e) {
			e.printStackTrace();
			printSystemMessage("Could not acquire next line from system input: " + e.getMessage());
			return "error";
		}
	}

	/**
	 * forces user to enter an input (not null)
	 * 
	 * @param prompt
	 * @return
	 */
	public String getUserString(String prompt) {
		String userInput = null;
		do {
			printSystemMessage(prompt);
			userInput = readNextLine();
		} while (userInput == null);
		return userInput;
	}

	// converts user input string to integer (used for Menu choices)
	public int convertStringToInt(String userInput) {
		try {
			int inputAsInt = Integer.parseInt(userInput);
			return inputAsInt;
		} catch (NumberFormatException e) {
			printSystemMessage("Input isn't an integer.");
			return -1;
		}
	}

	// converts user input string to double (used for amounts)
	public double convertStringToDouble(String userInput) {
		try {
			double inputAsDouble = Double.parseDouble(userInput);
			return inputAsDouble;
		} catch (NumberFormatException e) {
			printSystemMessage("Input isn't a double.");
			return -1;
		}
	}

	/**
	 * converts double to BigDecimal
	 * 
	 * @param d
	 * @return BigInteger as a conversion from input as double
	 */
	public static BigDecimal doubleToBigDecimal(double d) {
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			throw new IllegalArgumentException("Invalid input: " + d);
		}
		return new BigDecimal(Double.toString(d));
	}

	/**
	 * gets user's choice from a numbered menu
	 * 
	 * @param prompt
	 * @param choiceNo
	 * @return
	 */
	public String getUserMenuChoice(String prompt, int choiceNo) {
		String selection = "error";
		boolean valid = false;
		while (!valid) {
			selection = getUserString(prompt); // non-null string - could be "error"
			if (!(selection == "error")) {
				int no = convertStringToInt(selection);
				if ((no >= 1) && (no <= choiceNo)) {
					valid = true;
				}
			}
		}
		return selection;
	}

	// Gets user's input amount for transfers
	public double getAmount(String prompt, double limit) {
		while (true) {
			String userAmount = getUserString(prompt); // non-null string
			if (!(userAmount == "error")) {
				double amount = convertStringToDouble(userAmount);
				if ((amount >= 0) && (amount <= limit)) {
					return amount;
				}
			} else {
				return -1;
			}
		}
	}

	// Gets user's input amount for new account balance
	// TODO: this needs to return a BigDecimal
	public BigDecimal getOpeningBalance(String prompt) {
		double openingBalance = getAmount(prompt, Double.MAX_VALUE);
		BigDecimal convertedOpeningBalance = doubleToBigDecimal(openingBalance);
		return convertedOpeningBalance;
	}

	// gets user's confirmation: 'y' or cancels by entering 'n'
	public boolean confirm(String prompt) {
		String userInput = null;
		boolean valid = false;
		do {
			printSystemMessage(prompt);
			userInput = readNextLine();
			valid = ((userInput.charAt(0) == 'y') || (userInput.charAt(0) == 'Y') || (userInput.charAt(0) == 'n')
					|| (userInput.charAt(0) == 'N'));
		} while (!valid);
		if ((userInput.charAt(0) == 'y') || (userInput.charAt(0) == 'Y')) {
			return true;
		} else {
			return false;
		}
	}
}
