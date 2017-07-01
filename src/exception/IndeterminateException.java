package exception;

/**
 * Thrown when a mathematical indeterminate form is encountered (0/0, 0^0, etc.)
 *  
 *
 */
public class IndeterminateException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5544536779496728774L;

	public IndeterminateException(String message) {
		super(message);
	}
}
