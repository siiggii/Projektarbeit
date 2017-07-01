package exception;

/**
 * Thrown when an illegal math operation is required (e.g. 1/0 or arccos(2))
 *  
 *
 */
public class ArithmeticException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2485098497101022477L;

	public ArithmeticException(String message) {
		super(message);
	}
}
