package epforge;

import java.io.File;

/**
 * Main class for creating fake mails
 *
 * @author Barto
 */
public final class EPForge {
	/**
	 * Main function
	 * 
	 * @param args		console argument
	 */
	public static void main(String[] args) {
		if (args.length < 4 || args[0].contains("--help") || args[0].contains("-h")) {
			help();
		} else {
			Message.Builder message = new Message.Builder();
			//assumption: always the same order without any empty switches...
			try {
				message.setFrom(new Email(args[0]));
				message.setTo(new Email(args[1]));
				message.setSubject(args[2]);
				message.setMessage(new File(args[3]));
				message.build().send();
			} catch (Exception e) {
				displayError(e);
			}
		}
	}

	/**
	 * Help display
	 */
	public static void help() {
		System.out.println("EPForge version 201310 (c) Barto");
		System.out.println();
		System.out.println("Syntax:");
		System.out.println("java -jar EPForge.jar \"Sender<sender@host.org>\" \"Reciever<reciever@host.org>\" \"Subject\" FILE");
	}

	/**
	 * Display the exception that occurred to the user
	 * 
	 * @param e		the exception to report
	 */
	public static void displayError(Exception e) {
		System.out.println("[ERROR] " + e.getMessage());
		System.out.println();
		System.out.println("Look at the help with --help or -h or simply with no switches");
	}
}