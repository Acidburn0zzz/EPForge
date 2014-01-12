package epforge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents the message we will send
 * 
 * @author Barto
 */
public final class Message {
	private final String server = "mail.epfl.ch";
	private final int port = 25;
	private final Email from;
	private final Email to;
	private final String subject;
	private final String message;

	/**
	 * Constructor of the message
	 * 
	 * @param from								from who
	 * @param to								to who
	 * @param subject							subject of the message
	 * @param message							message to send
	 * @throws IllegalArgumentException			If one of the above is invalid
	 */
	private Message(Email from, Email to, String subject, String message) throws IllegalArgumentException {
		checkMessage(from, to, subject, message);
		this.from = from.clone();
		this.to = to.clone();
		this.subject = subject;
		this.message = message;
	}

	/**
	 * Check the validity of the message
	 * 
	 * @param from							sending email
	 * @param to							target email
	 * @param subject						subject of the message
	 * @param message						message to send
	 * @throws IllegalArgumentException		if one of the above is invalid
	 */
	private void checkMessage(Email from, Email to, String subject,	String message) throws IllegalArgumentException {
		if (from == null || to == null || subject == null || message == null) {
			throw new IllegalArgumentException("Invalid email message");
		}
	}

	/**
	 * Establish the connection and send the message
	 * 
	 * @throws IOException		If something goes wrong during the transmission/connection
	 */
	public void send() throws IOException {
		Socket socket = new Socket(server, port);
		PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

		//let's talk
		checkBuffer(socket, "220");
		//handshake
		output.println("HELO EPForge");
		checkBuffer(socket, "250");
		//message
		output.println("MAIL FROM: " + from);
		checkBuffer(socket, "250");
		output.println("RCPT TO: " + to);
		checkBuffer(socket, "250");
		//starting to write email
		output.println("DATA");
		checkBuffer(socket, "354");
		output.println("Subject: " + subject);
		output.println();
		output.println(message);
		//end of message
		output.println(".");
		//if it was correctly sent
		checkBuffer(socket, "250");
		//closure
		output.println("QUIT");
		checkBuffer(socket, "221");
		socket.close();
	}

	/**
	 * Check if we received the given expected reply code
	 * 
	 * It closes the connection is something goes wrong (without any QUIT message though)
	 * 
	 * @param input				from where we'll read the reply
	 * @param reply				the reply code to compare
	 * @throws IOException		if it doens't match
	 */
	private void checkBuffer(Socket socket, String reply) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String fullReply = input.readLine();

		System.out.println(fullReply); //it's cool to see this
		if(!fullReply.contains(reply)) {
			input.close();
			throw new IOException("Got " + "\"" + fullReply + "\" instead of code \"" + reply + "\"");
		}
	}

	/**
	 * Getter of the smtp server
	 * 
	 * @return		smtp server host name
	 */
	public String server() {
		return server;
	}

	/**
	 * Getter of the port number of the smtp server
	 * 
	 * @return		port used for the communication
	 */
	public int port() {
		return port;
	}

	/**
	 * Getter of the sender
	 * 
	 * @return		email's sender
	 */
	public Email from() {
		return from.clone();
	}

	/**
	 * Getter of the recipient
	 * 
	 * @return		target's email
	 */
	public Email to() {
		return to.clone();
	}

	/**
	 * Getter of the subject of the message
	 * 
	 * @return		subject of the message
	 */
	public String subject() {
		return subject;
	}

	/**
	 * Getter of the message
	 * 
	 * @return		the message we want to send
	 */
	public String message() {
		return message;
	}

	/**
	 * Comparison
	 * 
	 * @param obj		object to compare with
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			Message m = (Message)obj;
			return m.from.equals(from) && m.to.equals(to) && m.subject.equals(subject) && m.message.equals(message);
		}
		return false;
	}

	/**
	 * String representation of the message
	 * 
	 * @return		beautiful representation of our current message
	 */
	@Override
	public String toString() {
		return "FROM: " + from + "\nTO: " + to + "\nSubject: " + subject + "\n\n" + message;
	}

	/**
	 * Message Builder
	 * 
	 * @author Barto
	 */
	public static class Builder {
		private Email from;
		private Email to;
		private String subject = "";
		private String message = "";

		/**
		 * Create an instance of Message
		 * 
		 * @return		an instance of our message from our builder
		 */
		public Message build() {
			return new Message(from, to, subject, message);
		}

		/**
		 * Setter of our sender's email address
		 * 
		 * @param f			the sending email address
		 */
		public void setFrom(Email f) {
			from = f.clone();
		}

		/**
		 * Setter for the recipient
		 * 
		 * @param t			target email address
		 */
		public void setTo(Email t) {
			to = t.clone();
		}

		/**
		 * Setter for the subject
		 * 
		 * @param s			the new subject that will replace the old one
		 */
		public void setSubject(String s) {
			subject = s;
		}

		/**
		 * Setter of the message via the given file
		 * 
		 * @param file							file where the content of the message is
		 * @throws FileNotFoundException		if no file is found
		 * @throws IOException					if there are some missing rights
		 */
		public void setMessage(File file) throws FileNotFoundException, IOException {
			BufferedReader br = new BufferedReader(new FileReader(file));
			message = "";
			String temp = br.readLine();

			//read the message
			while (temp != null) {
				message = message + temp;
				message = message + "\n"; //let's re-add the missing new line characters
				temp = br.readLine();
			}
			br.close();
		}

		/**
		 * Converts the current email creation to a viewable string
		 * 
		 * @return		string representation of our currently-being-edited email message
		 */
		@Override
		public String toString() {
			return build().toString();
		}
	}
}