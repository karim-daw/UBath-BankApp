package se2.groupb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.ArrayList;

public class UserInput {

	private final BufferedReader in;
	private final PrintWriter out;
	// public static final UserInput userInput = new UserInput(in,out);

	// constructor
	public UserInput(BufferedReader in, PrintWriter out) throws IOException {
		this.in = in;
		this.out = out;
	}

	public void printSystemMessage(String message) {
		out.println(message);
	}
	
	/**
	 *
	 * Helper method for printing the contents of a HashMap<String,String>
	 * 
	 * @return a string of the contents
	 */
	public String mapToString(Map<String,String> map) {
		String s = "";
		if (map.size() > 0) {
			for (Map.Entry<String, String> item : map.entrySet()) {
				s += item.getKey() + " = " + item.getValue() + "\n";
			}
		}
		return s;
	}
	
	
	/**
	 * reads next line from BufferedReader
	 * 
	 * @return the string: could be null if user didn't enter anything or "error" if IO Exception occurred
	 */
	private String readNextLine() {
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
	 * @return non-null string or "error" if IOException
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
	 * @return a number as a string
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
	public Integer convertStringToInt(String userInput) {
		try {
			Integer inputAsInt = Integer.parseInt(userInput);
			return inputAsInt;
		} catch (NumberFormatException e) {
			printSystemMessage("Input isn't an integer.");
			return -1;
		}
	}
	
	
	public Integer getUserIntegerInput(String prompt) {
		
		String userString;
		Integer userInteger = -1;
		boolean valid = false;
		
		while (!valid) {
			userString = getUserString(prompt); // non-null string - could be "error"
			userInteger = convertStringToInt(userString); //if userString= "error" then userInteger=-1
			if (userInteger >= 0) {
				valid = true;
			}
		}
		return userInteger;
	}
	
	// TODO: need to add interaction for when user wants to transfer money to
	// another person

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
	 * // Gets user's input amount for new account balance
	 * public BigDecimal getOpeningBalance(String prompt) {
	 * return getAmount(prompt, new BigDecimal(Double.toString(Double.MAX_VALUE)));
	 */

	// TODO: this needs to return a BigDecimal
	public BigDecimal getOpeningBalance(String prompt) {
		BigDecimal openingBalance = getAmount(prompt, new BigDecimal(Double.toString(Double.MAX_VALUE)));
		return openingBalance;
	}

	/**
	 * gets user's confirmation: 'y' or cancels by entering 'n'
	 * 
	 * @param prompt
	 * @return true if "y" and false if "n"
	 */
	public boolean confirm(String prompt) {
		String userInput = null;
		boolean valid = false;
		do {
			printSystemMessage(prompt);
			userInput = readNextLine();
			if (userInput=="error") {
				return false;
			}
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
