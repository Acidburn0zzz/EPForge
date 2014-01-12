package epforge;

/**
 * Email address representation
 * 
 * @author Barto
 */
public final class Email {
	private final String fullName;
	private final String userName;
	private final String hostName;

	/**
	 * Constructor
	 * 
	 * @param fn								full name
	 * @param un								user name
	 * @param hn								host name
	 * @throws IllegalArgumentException			if one of the above is wrong
	 */
	public Email(String fn, String un, String hn) throws IllegalArgumentException {
		checkEmail(fn, un, hn);
		fullName = fn;
		userName = un;
		hostName = hn;
	}
	
	/**
	 * Constructor #2
	 * 
	 * @param e								a String like name<name@host.org>
	 * @throws IllegalArgumentException		if invalid email or parsing error
	 */
	public Email(String e) throws IllegalArgumentException {
		if (e == null || e.isEmpty()) {
			throw new IllegalArgumentException("Provided email address is empty or null");
		}
		
		int startUserName = e.indexOf('<');
		int startHostname = e.indexOf('@');
		int endHostname = e.indexOf('>');

		//presence
		//TODO: better alphanumeric check
		if (startUserName > 0 && startHostname > 0 && endHostname > 0 && startUserName < startHostname && startHostname < endHostname) {
			fullName = e.substring(0, startUserName);
			userName = e.substring(startUserName + 1, startHostname);
			hostName = e.substring(startHostname + 1, e.length() - 1);
		} else {
			throw new IllegalArgumentException("\"" + e + "\"" + " is not a valid email address");
		}
	}

	/**
	 * Check email validity
	 * 
	 * Sadly it isn't well done yet, it only checks if it's null or just empty
	 * 
	 * @param fn							full name
	 * @param un							user name
	 * @param hn							host name
	 * @throws IllegalArgumentException		if one of the values up is wrong
	 */
	private void checkEmail(String fn, String un, String hn) throws IllegalArgumentException {
		//TODO: check this input in a better way
		if (fn == null) {
			throw new IllegalArgumentException("Fullname is null");
		} else if (un == null || un.isEmpty()) {
			throw new IllegalArgumentException("\"" + un + "\"" + "is not a valid username");
		} else if (hn == null || hn.isEmpty()) {
			throw new IllegalArgumentException("\"" + hn + "\"" + "is not a valid hostname");
		}
	}

	/**
	 * Getter for the full name
	 * 
	 * @return		full name
	 */
	public String fullName() {
		return fullName;
	}

	/**
	 * Getter for the user name
	 * 
	 * @return		user name
	 */
	public String userName() {
		return userName;
	}

	/**
	 * Getter for the host name
	 * 
	 * @return		host name
	 */
	public String hostName() {
		return hostName;
	}

	/**
	 * Email copying method
	 * 
	 * @return		copy of the instance of the email
	 */
	public Email clone() {
		return new Email(fullName, userName, hostName);
	}

	/**
	 * Compare with another object
	 * 
	 * @return		if the given Object is the same as the email
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			Email e = (Email)obj;
			return e.fullName.equals(fullName) && e.hostName.equals(hostName)&& e.userName.equals(hostName);
		}
		return false;
	}

	/**
	 * Converts email to human-readable address
	 * 
	 * @return		human-readable email address
	 */
	@Override
	public String toString() {
		return fullName + "<" + userName + "@" + hostName + ">";
	}
}