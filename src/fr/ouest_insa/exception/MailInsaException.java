package fr.ouest_insa.exception;

/**
 * Exception to use if the the insa-rennes mail address is not correct.
 * @author Loïc Pelleau
 */
public class MailInsaException extends Exception {
	private static final long serialVersionUID = 1L;
	public MailInsaException(String s) {
		super(s);
	}
}
