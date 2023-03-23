package se2.groupb.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExampleClient extends Thread {

	// The socket object for connecting to the server
	private Socket server;

	// Output stream to the server for sending commands
	private PrintWriter bankServerOut;

	// Input stream to read user input from the console
	private BufferedReader userInput;

	// Thread to handle incoming responses from the server
	private Thread bankServerResponceThread;

	// Constructor to create a new client instance
	public ExampleClient(String ip, int port) throws UnknownHostException, IOException {
		// Create a socket to connect to the server
		server = new Socket(ip, port);

		// Create a reader to get user input from the console
		userInput = new BufferedReader(new InputStreamReader(System.in));

		// Create a writer to send commands to the server
		bankServerOut = new PrintWriter(server.getOutputStream(), true);

		// Define a thread to handle incoming responses from the server

		bankServerResponceThread = new Thread() {

			// Create a reader to read incoming responses from the server
			private BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream()));

			// Define the run method for the thread
			public void run() {
				try {
					// Continuously read responses from the server
					while (true) {
						String responce = bankServerIn.readLine();
						System.out.println(responce);
						// System.out.println("hi");
					}
				} catch (IOException e) {
					// If an exception occurs, print the stack trace and return
					e.printStackTrace();
					return;
				}
			}
		};

		// Start the thread to handle incoming responses from the server
		bankServerResponceThread.start();
	}

	// Define the run method for the client
	public void run() {
		// Continuously read user input and send commands to the server
		while (true) {
			try {
				while (true) {
					String command = userInput.readLine();
					bankServerOut.println(command);
				}
			} catch (IOException e) {
				// If an exception occurs, print the stack trace
				e.printStackTrace();
			}
		}
	}

	// Main method to create a new client instance and start it
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// Create a new instance of the ExampleClient class with the server's IP address
		// and port number
		new ExampleClient("localhost", 14002).start();
	}
}