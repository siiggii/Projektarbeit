package exception;

/**
 * Thrown when extra parameters are given to a function that does not support
 * that many parameters
 *  
 *
 */
public class WrongParametersException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2485098497101022477L;

	public WrongParametersException(String message) {
		super(message);
	}
}
