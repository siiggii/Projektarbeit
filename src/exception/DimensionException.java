package exception;

/**
 * Thrown when an illegal dimensional calculation is performed (e.g. adding matrices
 * of different sizes)
 *  
 *
 */
public class DimensionException extends RuntimeException {

	private static final long serialVersionUID = 4298179502229064227L;

	public DimensionException(String message) {
		super(message);
	}
}
