package fr.ouest_insa.exception;

/**
 * Exception to use if the account is not fill correctly.
 * @author Loïc Pelleau
 */
public class AccountNotFillException extends Exception {
	private static final long serialVersionUID = 1L;
	public AccountNotFillException(String s) {
		super(s);
	}
}
