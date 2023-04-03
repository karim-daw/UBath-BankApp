package se2.groupb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class UserInput {

	private final BufferedReader in;
	private final PrintWriter out;
	//public static final UserInput userInput = new UserInput(in,out);
	
	// constructor
	public UserInput(BufferedReader in, PrintWriter out) throws IOException {
		this.in = in;
		this.out = out;
	}
	
	public void printSystemMessage(String message) {
		out.println(message);
	}
	
	/**
	 * reads next line from BufferedReader
	 * @return
	 */
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
	 * keeps prompting the user for input if they haven't entered any
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
	
	/**
	 * converts user input string to integer (used for Menu choices)
	 * 
	 * @param userInput
	 * @return
	 */
	public int convertStringToInt(String userInput) {
		try {
			int inputAsInt = Integer.parseInt(userInput);
			return inputAsInt;
		} catch (NumberFormatException e) {
			printSystemMessage("Input isn't an integer.");
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
	 * 
	 * @param prompt
	 * @param limit
	 * @return
	 */
	public BigDecimal getAmount(String prompt, BigDecimal limit) {
		while (true) {
			String userAmount = getUserString(prompt); // non-null string
			if (!(userAmount == "error")) {
				BigDecimal amount = new BigDecimal(userAmount);
				BigDecimal zero = BigDecimal.ZERO;
				if ((amount.compareTo(zero) >= 0) && (amount.compareTo(limit) <= 0)) {
					return amount;
				}
			} else {
				return new BigDecimal("-1");
			}
		}
	}
	
	/*
	// Gets user's input amount for new account balance
	public BigDecimal getOpeningBalance(String prompt) {
		return getAmount(prompt, new BigDecimal(Double.toString(Double.MAX_VALUE)));
	*/
	
	// TODO: this needs to return a BigDecimal
	public BigDecimal getOpeningBalance(String prompt) {
		double openingBalance = getAmount(prompt, Double.MAX_VALUE);
		BigDecimal convertedOpeningBalance = doubleToBigDecimal(openingBalance);
		return convertedOpeningBalance;
	}


	/**
	 * gets user's confirmation: 'y' or cancels by entering 'n'
	 * @param prompt
	 * @return
	 */
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
