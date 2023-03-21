package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class UserInterface {
	
	private BufferedReader in;
	private PrintWriter out;
	
	//constructor
	public UserInterface(BufferedReader in,PrintWriter out) throws IOException{
		this.in = in;
		this.out = out;
	}
	
	public void printSystemMessage(String message){
		out.println(message);
	}
	
	public String readNextLine(){
		try {
			return in.readLine(); //could be null if user didn't enter anything
		} catch (IOException e) {
			e.printStackTrace();
			printSystemMessage("Could not acquire next line from system input: " + e.getMessage());
	        return "error";
		}
	}
	
	//forces user to enter an input (not null)
	public String getUserString(String prompt) {
		String userInput = null;
		do {
			printSystemMessage(prompt);
			userInput = readNextLine();
		} while (userInput==null);
		return userInput;
	}
	
	//converts user input string to integer (used for Menu choices)
	public int convertStringToInt(String userInput) {
		try {
	        int inputAsInt = Integer.parseInt(userInput);
	        return inputAsInt;
	    }
	    catch (NumberFormatException e) {
	    	printSystemMessage("Input isn't an integer.");
	        return -1;
	    }
	}
	
	//converts user input string to double (used for amounts)
	public double convertStringToDouble(String userInput) {
		try {
	        double inputAsDouble = Double.parseDouble(userInput);
	        return inputAsDouble;
	    }
	    catch (NumberFormatException e) {
	    	printSystemMessage("Input isn't a double.");
	        return -1;
	    }
	}
	
	//gets user's choice from a numbered menu
	public String getUserMenuChoice(String prompt, int choiceNo) {
		String selection = "error";
		boolean valid = false;
		while (!valid) {
			selection = getUserString(prompt); //non-null string - could be "error"
			if (!(selection=="error")) {
				int no=convertStringToInt(selection);
				if ((no>=1) && (no<=choiceNo)) {
					valid = true;
				}
			}
		}
		return selection;
	}
	
	// Gets user's input amount
	public double getAmount(String prompt){
		String userAmount = null;
		double amount=-1;
		boolean valid = false;
		while (!valid) {
			userAmount = getUserString(prompt); //non-null string
			if (!(userAmount=="error")) {
				amount = convertStringToDouble(userAmount);
				if (amount>=0) {
					valid = true;
				}
			}
		}
		return amount;
	}
	
	
	//gets user's confirmation: 'y' or cancels by entering 'n'
	public boolean confirm(String prompt) {
		String userInput = null;
		boolean valid = false;
		do {
			printSystemMessage(prompt);
			userInput = readNextLine();
			valid = ((userInput.charAt(0) =='y') || (userInput.charAt(0) =='Y') || (userInput.charAt(0) =='n') || (userInput.charAt(0) =='N'));
		} while (!valid);
		if ((userInput.charAt(0) =='y') || (userInput.charAt(0) =='Y')) {
			return true;
		}
		else {
			return false;
		}
	}
}
