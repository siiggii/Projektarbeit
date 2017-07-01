package exception;

import core.CALC;

/**
 * Thrown when an unidentified function needs to be parsed or evaluated.
 * @see CALC
 *
 */
public class UnsupportedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4339047940003069345L;

	public UnsupportedException(String message) {
		super(message);
	}
}
