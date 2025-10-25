package util.exceptions;

public class InsufficientFundsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientFundsException(String message) {
		super(message);
	}

	public InsufficientFundsException(String message, String userMessage) {
		super(message + " - " + userMessage);
	}
}