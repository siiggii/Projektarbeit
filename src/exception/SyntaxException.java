/**
 * 
 */
package exception;

/**
 * This will be thrown if an expression string cannot be parsed into
 * a HObject recursive hierarchy due to syntactic errors.
 *
 */
public class SyntaxException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param message
	 * The error message
	 */
	public SyntaxException(String message) {
		super(message);
	}

}
